package totipay.wallet.di.apicaller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import totipay.wallet.R;
import totipay.wallet.di.ApiHelper;
import totipay.wallet.di.HTTPHelper;
import totipay.wallet.di.RequestHelper.WalletHistoryRequest;
import totipay.wallet.di.ResponseHelper.WalletTransferHistoryResponse;
import totipay.wallet.di.SoapActionHelper;
import totipay.wallet.interfaces.OnWalletHistoryResponse;
import totipay.wallet.utils.ProgressBar;

public class WalletHistoryRequestTask extends AsyncTask<WalletHistoryRequest, Void, List<WalletTransferHistoryResponse>> {


    ProgressBar progressBar;
    Context context;
    OnWalletHistoryResponse onResponse;


    public WalletHistoryRequestTask(Context context, OnWalletHistoryResponse onResponse) {
        this.context = context;
        this.onResponse = onResponse;
    }


    @Override
    protected void onPostExecute(List<WalletTransferHistoryResponse> list) {
        super.onPostExecute(list);
        progressBar.hideProgressDialogWithTitle();
        if (list != null) {
            onResponse.onHistory(list);
        }
    }

    @Override
    protected List<WalletTransferHistoryResponse> doInBackground(WalletHistoryRequest... walletHistoryRequests) {
        Log.e("doInBackground: ", walletHistoryRequests[0]
                .getXML());
        String responseString = HTTPHelper.getResponse(walletHistoryRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_WALLET_HISTORY
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();

        List<WalletTransferHistoryResponse> list = null;
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("WalletTransactionHistoryResponse").getJSONObject("WalletTransactionHistoryResult");
            Log.e("bene response", jsonObject.toString());
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            list = new ArrayList<>();
            if (responseCode.equals("101")) {
                jsonObject = jsonObject.getJSONObject("obj").getJSONObject("diffgr:diffgram")
                        .getJSONObject("NewDataSet");
                Log.e("doInBackground: ", jsonObject.toString());
                JSONArray array = null;
                try {
                    array = jsonObject.getJSONArray("Table1");
                } catch (Exception e) {
                    Log.e("doInBackground: ", e.getLocalizedMessage());
                }

                list.clear();
                list = new ArrayList<>();
                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        WalletTransferHistoryResponse response = new WalletTransferHistoryResponse();
                        JSONObject object = array.getJSONObject(i);
                        response.transactionDate = object.getString("TRANSACTIONDATE");
                        response.paymentType = object.getString("PAYMENTTYPE");
                        response.transactionNumber = object.getString("TRANSACTIONNUMBER");
                        response.sendingCurrency = object.getString("SENDINGCURRENCY");
                        response.customerNo = object.getString("CUSTOMERNO");
                        response.status = object.getString("STATUS");


                        response.sendingAmount = object.getString("SENDINGAMOUNT");
                        response.receivingAmount = object.getString("RECEIVINGAMOUNT");
                        if (!response.status.equalsIgnoreCase("RECEIVED")) {
                            try{
                                response.receiverName = object.getString("RECEIVERNAME");
                                response.receiverNumber = object.getString("BENE_MOBILE_NO");
                            } catch (Exception e) {
                                Log.e("doInBackground: ", e.getLocalizedMessage());
                            }

                        }
                        if (response.status.equalsIgnoreCase("RECEIVED")) {
                            response.senderName = object.getString("SENDERNAME");
                        }
                        list.add(response);
                    }
                } else {
                    jsonObject = jsonObject.getJSONObject("Table1");
                    WalletTransferHistoryResponse response = new WalletTransferHistoryResponse();

                    response.transactionDate = jsonObject.getString("TRANSACTIONDATE");
                    response.paymentType = jsonObject.getString("PAYMENTTYPE");
                    response.transactionNumber = jsonObject.getString("TRANSACTIONNUMBER");
                    response.sendingCurrency = jsonObject.getString("SENDINGCURRENCY");
                    response.customerNo = jsonObject.getString("CUSTOMERNO");
                    response.status = jsonObject.getString("STATUS");
                    response.sendingAmount = jsonObject.getString("SENDINGAMOUNT");
                    response.receivingAmount = jsonObject.getString("RECEIVINGAMOUNT");
                    if (!response.status.equalsIgnoreCase("RECEIVED")) {
                        try{
                            response.receiverName = jsonObject.getString("RECEIVERNAME");
                            response.receiverNumber = jsonObject.getString("BENE_MOBILE_NO");
                        } catch (Exception e) {
                            Log.e("doInBackground: ", e.getLocalizedMessage());
                        }

                    }
                    if (response.status.equalsIgnoreCase("RECEIVED")) {
                        response.senderName = jsonObject.getString("SENDERNAME");
                    }


                    list.add(response);
                }
                // onSuccess();
                // adapter.notifyDataSetChanged();
            } else {
                onResponse.onResponseMessage(message);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context,
                context.getResources().getString(R.string.loading_txt));
    }


}
