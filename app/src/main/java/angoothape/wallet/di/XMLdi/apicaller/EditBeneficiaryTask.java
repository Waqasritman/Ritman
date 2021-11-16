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
import angoothape.wallet.di.XMLdi.RequestHelper.EditBeneficiaryRequest;
import angoothape.wallet.di.XMLdi.SoapActionHelper;
import angoothape.wallet.interfaces.OnSuccessMessage;
import angoothape.wallet.utils.ProgressBar;

public class EditBeneficiaryTask extends AsyncTask<EditBeneficiaryRequest, Void, String> {


    ProgressBar progressBar;
    Context context;
    OnSuccessMessage onSuccessMessage;

    public EditBeneficiaryTask(Context context, OnSuccessMessage onSuccessMessage) {
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
    protected String doInBackground(EditBeneficiaryRequest... editBeneficiaryRequests) {
        String xml = editBeneficiaryRequests[0]
                .getXML();
        Log.e("doInBackground: ", xml);
        String responseString = HTTPHelper.getResponse(editBeneficiaryRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_EDIT_BENEFICIARY
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        String response = "";
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("EditBeneficiaryProfileResponse").getJSONObject("EditBeneficiaryProfileResult");
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            Log.e("doInBackground:", jsonObject.toString());
            if (responseCode.equals("101")) {
                response = jsonObject.getString("BeneficiaryNo");
                //    delegate.onSuccessResponse("Beneficairy Added Successfully");
            } else {
                onSuccessMessage.onResponseMessage(message);
                response = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }
}
