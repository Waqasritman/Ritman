package angoothape.wallet.di.XMLdi.apicaller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import angoothape.wallet.R;
import angoothape.wallet.di.XMLdi.ApiHelper;
import angoothape.wallet.di.XMLdi.HTTPHelper;
import angoothape.wallet.di.XMLdi.RequestHelper.WRPayBillRequest;
import angoothape.wallet.di.XMLdi.SoapActionHelper;
import angoothape.wallet.interfaces.OnSuccessMessage;
import angoothape.wallet.utils.ProgressBar;

public class WRPayBillTask extends AsyncTask<WRPayBillRequest, Void, String> {


    ProgressBar progressBar;
    Context context;
    OnSuccessMessage onSuccessMessage;


    public WRPayBillTask(Context context, OnSuccessMessage onSuccessMessage) {
        this.context = context;
        this.onSuccessMessage = onSuccessMessage;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context
                , context.getString(R.string.loading_txt));
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
    protected String doInBackground(WRPayBillRequest... wrPayBillRequests) {
        Log.e("XML: ",wrPayBillRequests[0].getXML() );
        String responseString = HTTPHelper.getResponse(wrPayBillRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_WR_PAY_BILL
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        String message = "";
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("WRPayBillResponse").getJSONObject("WRPayBillResult");
            String responseCode = jsonObject.getString("ResponseCode");
             message = jsonObject.getString("Description");
            if (responseCode.equals("101")) {

            } else {
                onSuccessMessage.onResponseMessage(message);
                message = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }
}
