package angoothape.wallet.di.XMLdi.apicaller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import angoothape.wallet.R;
import angoothape.wallet.di.XMLdi.ApiHelper;
import angoothape.wallet.di.XMLdi.RequestHelper.ForgotPinSetNewRequest;
import angoothape.wallet.di.XMLdi.SoapActionHelper;
import angoothape.wallet.interfaces.OnSuccessMessage;
import angoothape.wallet.utils.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class SetNewPinTask extends AsyncTask<ForgotPinSetNewRequest, Void, String> {

    ProgressBar progressBar;
    Context context;
    OnSuccessMessage onResponse;

    public SetNewPinTask(Context context, OnSuccessMessage onResponse) {
        this.context = context;
        this.onResponse = onResponse;
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
            onResponse.onSuccess(s);
        }
    }

    @Override
    protected String doInBackground(ForgotPinSetNewRequest... forgotPinSetNewRequests) {
        Log.e("XML: ", forgotPinSetNewRequests[0].getXML());
        String responseString = HTTPHelper.getResponse(forgotPinSetNewRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_SET_NEW_PIN
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        String message = "";
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                        .getJSONObject("SetNewPINResponse").getJSONObject("SetNewPINResult");
            String responseCode = jsonObject.getString("ResponseCode");
            message = jsonObject.getString("Description");
            if (responseCode.equals("101")) {
                message = message;
            } else {
                onResponse.onResponseMessage(message);
                message = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }
}
