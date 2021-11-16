package angoothape.wallet.di.XMLdi.apicaller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import angoothape.wallet.R;
import angoothape.wallet.di.XMLdi.RequestHelper.ChangePin;
import angoothape.wallet.interfaces.OnSuccessMessage;
import angoothape.wallet.utils.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class ChangePinTask extends AsyncTask<ChangePin, Void, String> {

    ProgressBar progressBar;
    Context context;
    OnSuccessMessage onResponse;

    public ChangePinTask(Context context, OnSuccessMessage onResponse) {
        this.context = context;
        this.onResponse = onResponse;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context, context.getResources().getString(R.string.loading_txt));
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
    protected String doInBackground(ChangePin... changePins) {

        String responseString = "";
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        Log.e("doInBackground: ", jsonObject.toString());
        String responseCode = "";
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("ChangePINResponse").getJSONObject("ChangePINResult");
            responseCode = jsonObject.getString("ResponseCode");

            String message = jsonObject.getString("Description");
            if (responseCode.equals("101")) {
                responseCode = message;
            } else {
                onResponse.onResponseMessage(message);
                //    onMessage(message);
                responseCode = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseCode;
    }
}
