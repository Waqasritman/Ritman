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
import ritman.wallet.di.XMLdi.RequestHelper.WRPrepaidRechargeRequest;
import ritman.wallet.di.XMLdi.SoapActionHelper;
import ritman.wallet.interfaces.OnSuccessMessage;
import ritman.wallet.utils.ProgressBar;

public class WRPrepaidPayBillTask extends AsyncTask<WRPrepaidRechargeRequest, Void, String> {


    ProgressBar progressBar;
    Context context;
    OnSuccessMessage onSuccessMessage;


    public WRPrepaidPayBillTask(Context context, OnSuccessMessage onSuccessMessage) {
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
    protected String doInBackground(WRPrepaidRechargeRequest... wrPayBillRequests) {
//      //  Log.e("XML: ", wrPayBillRequests[0].getXML());
//        String responseString = HTTPHelper.getResponse(wrPayBillRequests[0]
//                        .getXML(),
//                SoapActionHelper.ACTION_WR_PERPAID_RECHARGE
//                , ApiHelper.METHOD_POST);
//        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
//        // convert to a JSONObject
//        JSONObject jsonObject = xmlToJson.toJson();
//        String message = "";
//        Log.e("doInBackground: ", jsonObject.toString());
//        try {
//            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
//                    .getJSONObject("WRPrepaidRechargeResponse").getJSONObject("WRPrepaidRechargeResult");
//            String responseCode = jsonObject.getString("ResponseCode");
//            message = jsonObject.getString("Description");
//            if (responseCode.equals("101")) {
//                message = jsonObject.getString("RechargeId");
//            } else {
//                onSuccessMessage.onResponseMessage(message);
//                message = "";
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        return "";
    }
}
