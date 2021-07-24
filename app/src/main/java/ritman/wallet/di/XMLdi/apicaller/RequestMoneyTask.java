package ritman.wallet.di.XMLdi.apicaller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import ritman.wallet.R;
import ritman.wallet.di.XMLdi.ApiHelper;
import ritman.wallet.di.XMLdi.HTTPHelper;
import ritman.wallet.di.XMLdi.RequestHelper.RequestMoney;
import ritman.wallet.di.XMLdi.SoapActionHelper;
import ritman.wallet.interfaces.OnSuccessMessage;
import ritman.wallet.utils.ProgressBar;

public class RequestMoneyTask extends AsyncTask<RequestMoney, Void, String> {

    ProgressBar progressBar;
    Context context;
    OnSuccessMessage onSuccessMessage;

    public RequestMoneyTask(Context context, OnSuccessMessage onSuccessMessage) {
        this.context = context;
        this.onSuccessMessage = onSuccessMessage;
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
            onSuccessMessage.onSuccess(s);
        }
    }

    @Override
    protected String doInBackground(RequestMoney... requestMonies) {
        Log.e("doInBackground: ", requestMonies[0]
                .getXML());
        String responseString = HTTPHelper.getResponse(requestMonies[0]
                        .getXML(),
                SoapActionHelper.ACTION_REQUEST_MONEY
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        String responseCode = "";
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("RequestMoneyResponse").getJSONObject("RequestMoneyResult");
            responseCode = jsonObject.getString("ResponseCode");

            String message = jsonObject.getString("Description");
            if (responseCode.equals("101")) {
                responseCode = message;
            } else {
                onSuccessMessage.onResponseMessage(message);
                //    onMessage(message);
                responseCode = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseCode;
    }
}
