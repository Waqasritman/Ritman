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
import ritman.wallet.di.XMLdi.RequestHelper.KYCImageUploadRequest;
import ritman.wallet.di.XMLdi.SoapActionHelper;
import ritman.wallet.interfaces.OnResponse;
import ritman.wallet.utils.ProgressBar;

public class KYCImageUploadTask extends AsyncTask<KYCImageUploadRequest
        , Void, String> {

    ProgressBar progressBar;
    Context context;
    OnResponse onResponse;


    public KYCImageUploadTask(Context context, OnResponse onResponse) {
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
            onResponse.onSuccess();
        }
    }

    @Override
    protected String doInBackground(KYCImageUploadRequest... kycImageUploadRequests) {
        Log.e("XML: ", kycImageUploadRequests[0].getXML());
        String responseString = HTTPHelper.getResponse(kycImageUploadRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_UPLOAD_IMAGE
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        String customerNo = "";
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("UploadKYCImageResponse").getJSONObject("UploadKYCImageResult");
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            Log.e("doInBackground: ", jsonObject.toString());
            if (responseCode.equals("101")) {
                customerNo = message;
            } else {
                onResponse.onResponseMessage(message);
                //    onMessage(message);
                customerNo = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return customerNo;
    }
}
