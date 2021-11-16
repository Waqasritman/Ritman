package angoothape.wallet.di.XMLdi.apicaller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import angoothape.wallet.R;
import angoothape.wallet.di.XMLdi.ApiHelper;
import angoothape.wallet.di.XMLdi.HTTPHelper;
import angoothape.wallet.di.XMLdi.RequestHelper.EditCustomerProfileRequest;
import angoothape.wallet.di.XMLdi.SoapActionHelper;
import angoothape.wallet.interfaces.OnResponse;
import angoothape.wallet.utils.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class EditCustomerProfileTask extends AsyncTask<EditCustomerProfileRequest , Void , String> {

    ProgressBar progressBar;
    Context context;
    OnResponse delegate;

    public EditCustomerProfileTask(Context context, OnResponse delegate) {
        this.context = context;
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context , context.getResources().getString(R.string.loading_txt));
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressBar.hideProgressDialogWithTitle();
        if(!s.isEmpty()) {
            delegate.onSuccess();
        }

    }

    @Override
    protected String doInBackground(EditCustomerProfileRequest... editCustomerProfileRequests) {
        Log.e("XML: ", editCustomerProfileRequests[0]
                .getXML());
        String responseString = HTTPHelper.getResponse(editCustomerProfileRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_EDIT_PROFILE
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        String response = "";
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("EditCustomerProfileResponse").getJSONObject("EditCustomerProfileResult");
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            Log.e("doInBackground:", jsonObject.toString());
            if (responseCode.equals("101")) {
               response = jsonObject.getString("CustomerNumber");
              //  delegate.onSuccessResponse(message);
            } else {
                delegate.onResponseMessage(message);
                response = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }
}
