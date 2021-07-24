package ritman.wallet.di.XMLdi.apicaller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import ritman.wallet.R;
import ritman.wallet.di.XMLdi.ApiHelper;
import ritman.wallet.di.XMLdi.HTTPHelper;
import ritman.wallet.di.XMLdi.RequestHelper.GetCCYForWalletRequest;
import ritman.wallet.di.XMLdi.ResponseHelper.GetSendRecCurrencyResponse;
import ritman.wallet.di.XMLdi.SoapActionHelper;
import ritman.wallet.interfaces.OnSelectCurrency;
import ritman.wallet.utils.ProgressBar;

public class GetCCYForWalletTask extends AsyncTask<GetCCYForWalletRequest ,
        Void , List<GetSendRecCurrencyResponse>> {
    Context context;
    OnSelectCurrency delegate;
    ProgressBar progressBar;

    public GetCCYForWalletTask(Context context, OnSelectCurrency delegate) {
        this.context = context;
        this.delegate = delegate;
    }

    @Override
    protected List<GetSendRecCurrencyResponse> doInBackground(GetCCYForWalletRequest... getSendRecCurrencyRequests) {
        String responseString = HTTPHelper.getResponse(getSendRecCurrencyRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_GET_CCY_WALLET
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        List<GetSendRecCurrencyResponse> responseList = new ArrayList<>();
        try {
            assert jsonObject != null;
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("GetCCYForWalletResponse").getJSONObject("GetCCYForWalletResult");
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            Log.e("doInBackground:", jsonObject.toString());
            if (responseCode.equals("101")) {
                JSONArray array = null;

                try {
                    array = jsonObject.getJSONObject("Obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("DocumentElement").getJSONArray("CurrencyList");
                } catch (Exception e) {
                    Log.e("TAG", "doInBackground: " + e.getLocalizedMessage());
                }
                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        jsonObject = array.getJSONObject(i);
                        GetSendRecCurrencyResponse response = new GetSendRecCurrencyResponse();
                        response.id = jsonObject.getString("Currency_Id");
                        response.currencyShortName = jsonObject.getString("CurrencyShortName");
                        response.image_URL = jsonObject.getString("Image_URL");
                        responseList.add(response);
                    }
                } else {
                    jsonObject = jsonObject.getJSONObject("Obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("DocumentElement").getJSONObject("CurrencyList");
                    GetSendRecCurrencyResponse response = new GetSendRecCurrencyResponse();
                    response.id = jsonObject.getString("Currency_Id");
                    response.currencyShortName = jsonObject.getString("CurrencyShortName");
                    response.image_URL = jsonObject.getString("Image_URL");
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


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context, context.getResources().getString(R.string.loading_txt));
    }


    @Override
    protected void onPostExecute(List<GetSendRecCurrencyResponse> getSendRecCurrencyResponses) {
        super.onPostExecute(getSendRecCurrencyResponses);
        progressBar.hideProgressDialogWithTitle();
        if (getSendRecCurrencyResponses != null) {
            delegate.onCurrencyResponse(getSendRecCurrencyResponses);
        }
    }
}
