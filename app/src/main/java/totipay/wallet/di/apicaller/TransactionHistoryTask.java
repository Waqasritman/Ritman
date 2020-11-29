package totipay.wallet.di.apicaller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import totipay.wallet.R;
import totipay.wallet.di.ApiHelper;
import totipay.wallet.di.HTTPHelper;
import totipay.wallet.di.RequestHelper.TransactionHistoryRequest;
import totipay.wallet.di.ResponseHelper.TransactionHistoryResponse;
import totipay.wallet.di.SoapActionHelper;
import totipay.wallet.interfaces.OnGetTransactionHistory;
import totipay.wallet.utils.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class TransactionHistoryTask extends AsyncTask<TransactionHistoryRequest, Void, List<TransactionHistoryResponse>> {

    Context context;
    OnGetTransactionHistory delegate;
    ProgressBar progressBar;

    public TransactionHistoryTask(Context context, OnGetTransactionHistory delegate) {
        this.context = context;
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context, context.getResources().getString(R.string.loading_txt));
    }


    @Override
    protected void onPostExecute(List<TransactionHistoryResponse> transactionHistoryResponses) {
        super.onPostExecute(transactionHistoryResponses);
        progressBar.hideProgressDialogWithTitle();
        if (transactionHistoryResponses != null) {
            delegate.onGetHistoryList(transactionHistoryResponses);
        }
    }

    @Override
    protected List<TransactionHistoryResponse> doInBackground(TransactionHistoryRequest... transactionHistoryRequests) {
        Log.e("XML", transactionHistoryRequests[0].getXML());
        String responseString = HTTPHelper.getResponse(transactionHistoryRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_HISTORY
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();

        List<TransactionHistoryResponse> responseList = new ArrayList<>();
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("TransactionHistoryResponse").getJSONObject("TransactionHistoryResult");
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            Log.e("doInBackground: ", jsonObject.toString());
            if (responseCode.equals("101")) {
                JSONArray array = null;

                try {
                    array = jsonObject.getJSONObject("obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("NewDataSet").getJSONArray("Table1");
                } catch (Exception e) {
                    Log.e("TAG", "doInBackground: " + e.getLocalizedMessage());
                }


                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        jsonObject = array.getJSONObject(i);
                        TransactionHistoryResponse response = new TransactionHistoryResponse();
                        response.transactionDate = jsonObject.getString("TRANSACTIONDATE");
                        response.purposeOfTransfer = jsonObject.getString("PURPOSEOFTRANSFER");

                        response.bankName = jsonObject.getString("BANKNAME");
                        response.transactionNumber = jsonObject.getString("TRANSACTIONNUMBER");
                        response.senderName = jsonObject.getString("SENDERNAME");
                        response.receiverName = jsonObject.getString("RECEIVERNAME");
                        response.sendingAmount = jsonObject.getString("SENDINGAMOUNT");
                        response.receiverAmount = jsonObject.getString("RECEIVINGAMOUNT");
                        response.status = jsonObject.getString("STATUS");
                        response.currency = jsonObject.getString("CCYSHORTNAME");

                        try {
                            response.paymentType = jsonObject.getString("PAYMENTTYPE");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        responseList.add(response);
                    }
                } else {
                    jsonObject = jsonObject.getJSONObject("obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("NewDataSet").getJSONObject("Table1");
                    Log.e("doInBackground: ", jsonObject.toString());
                    TransactionHistoryResponse response = new TransactionHistoryResponse();
                    response.transactionDate = jsonObject.getString("TRANSACTIONDATE");
                    response.purposeOfTransfer = jsonObject.getString("PURPOSEOFTRANSFER");
                    response.paymentType = jsonObject.getString("PAYMENTTYPE");
                    response.bankName = jsonObject.getString("BANKNAME");
                    response.transactionNumber = jsonObject.getString("TRANSACTIONNUMBER");
                    response.senderName = jsonObject.getString("SENDERNAME");
                    response.receiverName = jsonObject.getString("RECEIVERNAME");
                    response.sendingAmount = jsonObject.getString("SENDINGAMOUNT");
                    response.receiverAmount = jsonObject.getString("RECEIVINGAMOUNT");
                    response.status = jsonObject.getString("STATUS");
                    response.currency = jsonObject.getString("CCYSHORTNAME");
                    try {
                        response.paymentType = jsonObject.getString("PAYMENTTYPE");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    responseList.add(response);
                }

            } else {
                delegate.onResponseMessage(message);
                responseList = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseList;
    }
}
