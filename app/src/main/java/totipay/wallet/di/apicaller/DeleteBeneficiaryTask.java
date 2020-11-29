package totipay.wallet.di.apicaller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import totipay.wallet.R;
import totipay.wallet.di.ApiHelper;
import totipay.wallet.di.HTTPHelper;
import totipay.wallet.di.RequestHelper.DeleteBeneficiaryRequest;
import totipay.wallet.di.SoapActionHelper;
import totipay.wallet.interfaces.OnSuccessMessage;
import totipay.wallet.utils.ProgressBar;

public class DeleteBeneficiaryTask extends AsyncTask<DeleteBeneficiaryRequest, Void, String> {


    ProgressBar progressBar;
    Context context;
    OnSuccessMessage delegate;


    public DeleteBeneficiaryTask(Context context, OnSuccessMessage delegate) {
        this.context = context;
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context, context.getString(R.string.loading_txt));

    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressBar.hideProgressDialogWithTitle();
        if (!s.isEmpty()) {
            delegate.onSuccess(s);
        }
    }


    @Override
    protected String doInBackground(DeleteBeneficiaryRequest... deleteBeneficiaryRequests) {
        String responseString = HTTPHelper.getResponse(deleteBeneficiaryRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_DELETE_BENEFICIARY
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        String response = "";
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("deleteBeneficiaryResponse").getJSONObject("deleteBeneficiaryResult");
            String responseCode = jsonObject.getString("ResponseCode");
            Log.e("JSONResponse: ", jsonObject.toString());
            String message = jsonObject.getString("Description");
            if (responseCode.equals("101")) {
                response = message;
            } else {
                delegate.onResponseMessage(message);
                //    onMessage(message);
                response = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }
}
