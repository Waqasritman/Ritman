package angoothape.wallet.di.XMLdi.apicaller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import angoothape.wallet.R;
import angoothape.wallet.di.XMLdi.ApiHelper;
import angoothape.wallet.di.XMLdi.RequestHelper.GetSendRecCurrencyRequest;
import angoothape.wallet.di.XMLdi.ResponseHelper.GetSendRecCurrencyResponse;
import angoothape.wallet.di.XMLdi.SoapActionHelper;
import angoothape.wallet.interfaces.OnSelectCurrency;
import angoothape.wallet.utils.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class GetSendRecCurrencyTask extends AsyncTask<GetSendRecCurrencyRequest, Void,
        List<GetSendRecCurrencyResponse>> {
    Context context;
    OnSelectCurrency delegate;
    ProgressBar progressBar;

    public GetSendRecCurrencyTask(Context context, OnSelectCurrency delegate) {
        this.context = context;
        this.delegate = delegate;
    }

    @Override
    protected List<GetSendRecCurrencyResponse> doInBackground(GetSendRecCurrencyRequest... getSendRecCurrencyRequests) {
        String responseString = HTTPHelper.getResponse(getSendRecCurrencyRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_GET_REC_CURRENCY
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        List<GetSendRecCurrencyResponse> responseList = new ArrayList<>();
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("GetSendRecCurrencyResponse").getJSONObject("GetSendRecCurrencyResult");
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            Log.e("doInBackground:", jsonObject.toString());
            if (responseCode.equals("101")) {
                JSONArray array = null;

                try {
                    array = jsonObject.getJSONObject("obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("GetSendRecCurrency").getJSONArray("tblGetSendRecCurrency");
                } catch (Exception e) {
                    Log.e("TAG", "doInBackground: " + e.getLocalizedMessage());
                }
                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        jsonObject = array.getJSONObject(i);
                        GetSendRecCurrencyResponse response = new GetSendRecCurrencyResponse();
                        response.currencyName = jsonObject.getString("CurrencyName");
                        response.currencyShortName = jsonObject.getString("CurrencyShortName");
                        response.image_URL = jsonObject.getString("Image_URL");
                        responseList.add(response);
                    }
                } else {
                    jsonObject = jsonObject.getJSONObject("obj").getJSONObject("diffgr:diffgram")
                            .getJSONObject("GetSendRecCurrency").getJSONObject("tblGetSendRecCurrency");
                    GetSendRecCurrencyResponse response = new GetSendRecCurrencyResponse();
                    response.currencyName = jsonObject.getString("CurrencyName");
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
