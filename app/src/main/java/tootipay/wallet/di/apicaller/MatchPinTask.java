package tootipay.wallet.di.apicaller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import tootipay.wallet.R;
import tootipay.wallet.di.ApiHelper;
import tootipay.wallet.di.HTTPHelper;
import tootipay.wallet.di.RequestHelper.MatchPinRequest;
import tootipay.wallet.di.SoapActionHelper;
import tootipay.wallet.interfaces.OnUserPin;
import tootipay.wallet.utils.ProgressBar;

public class MatchPinTask extends AsyncTask<MatchPinRequest, Void, String> {

    ProgressBar progressBar;
    Context context;
    OnUserPin onUserPin;

    public MatchPinTask(Context context, OnUserPin onUserPin) {
        this.context = context;
        this.onUserPin = onUserPin;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context,
                context.getResources().getString(R.string.loading_txt));


    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressBar.hideProgressDialogWithTitle();
        if (!s.equals("")) {
            onUserPin.onVerifiedPin();
        }
    }

    @Override
    protected String doInBackground(MatchPinRequest... loginRequestTasks) {
        Log.e("XML: ", loginRequestTasks[0].getXML());
        String responseString = HTTPHelper.getResponse(loginRequestTasks[0]
                        .getXML(),
                SoapActionHelper.ACTION_MATCH_PIN
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        String customerNo = "";
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("MatchPINResponse").getJSONObject("MatchPINResult");
            String responseCode = jsonObject.getString("ResponseCode");
            Log.e("doInBackground: ", jsonObject.toString());
            String message = jsonObject.getString("Description");
            if (responseCode.equals("101")) {
                // customerNo = jsonObject.getString("CustomerNumber");
                customerNo = responseCode;
            } else {
                onUserPin.onResponseMessage(message);
                //    onMessage(message);
                customerNo = "";
            }
            //{"ResponseCode":100,"Description":"Request failed"}
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return customerNo;
    }
}

