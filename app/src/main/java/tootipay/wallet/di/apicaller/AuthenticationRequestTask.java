package tootipay.wallet.di.apicaller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import tootipay.wallet.R;
import tootipay.wallet.di.ApiHelper;
import tootipay.wallet.di.HTTPHelper;
import tootipay.wallet.di.RequestHelper.AuthenticationRequest;
import tootipay.wallet.di.SoapActionHelper;
import tootipay.wallet.interfaces.OnSuccessMessage;
import tootipay.wallet.utils.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class AuthenticationRequestTask extends AsyncTask<AuthenticationRequest, Void, String> {

    Context context;
    ProgressBar progressBar;
    OnSuccessMessage onMessageInterface;

    public AuthenticationRequestTask(Context context, OnSuccessMessage onMessageInterface) {
        this.context = context;
        this.onMessageInterface = onMessageInterface;
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
        if (!s.isEmpty()) {
            onMessageInterface.onSuccess(s);
        }
    }

    @Override
    protected String doInBackground(AuthenticationRequest... authenticationRequests) {
        Log.e("XMLAuth: ", authenticationRequests[0]
                .getXML());
        String responseString = HTTPHelper.getResponse(authenticationRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_AUTHENTICATION
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        String responseCode = "";
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("AuthenticationResponse").getJSONObject("AuthenticationResult");
            responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            if (responseCode.equals("101")) {
                responseCode = message;
                Log.e("Auth: ", message);
            } else {
                onMessageInterface.onResponseMessage(message);
                responseCode = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseCode;
    }
}
