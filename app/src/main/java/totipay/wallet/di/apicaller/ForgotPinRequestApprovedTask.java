package totipay.wallet.di.apicaller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import totipay.wallet.R;
import totipay.wallet.di.ApiHelper;
import totipay.wallet.di.HTTPHelper;
import totipay.wallet.di.RequestHelper.ForgotPinRequestApprovedUserRequest;
import totipay.wallet.di.SoapActionHelper;
import totipay.wallet.interfaces.OnSuccessMessage;
import totipay.wallet.utils.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class ForgotPinRequestApprovedTask extends
        AsyncTask<ForgotPinRequestApprovedUserRequest, Void, String> {

    ProgressBar progressBar;
    Context context;
    OnSuccessMessage onResponse;

    public ForgotPinRequestApprovedTask(Context context, OnSuccessMessage onResponse) {
        this.context = context;
        this.onResponse = onResponse;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context
                , context.getResources().getString(R.string.loading_txt));
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressBar.hideProgressDialogWithTitle();
        if (!s.isEmpty()) {
            onResponse.onSuccess(s);
        }
    }

    @Override
    protected String doInBackground(ForgotPinRequestApprovedUserRequest... forgotPinRequestApprovedUserRequests) {
        Log.e("XML", forgotPinRequestApprovedUserRequests[0].getXML() );
        String responseString = HTTPHelper.getResponse(forgotPinRequestApprovedUserRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_FORGOT_REQUEST_APPROVED_USER
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        String responseCode = "";
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("ForgetPINResponse").getJSONObject("ForgetPINResult");
            responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            if (responseCode.equals("101")) {

            } else {
                onResponse.onResponseMessage(message);
                responseCode = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseCode;
    }
}
