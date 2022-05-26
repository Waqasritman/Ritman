package angoothape.wallet.di.XMLdi.apicaller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import angoothape.wallet.di.XMLdi.ApiHelper;
import angoothape.wallet.di.XMLdi.RequestHelper.CreateWalletRequest;
import angoothape.wallet.di.XMLdi.SoapActionHelper;
import angoothape.wallet.interfaces.OnSuccessMessage;
import angoothape.wallet.utils.ProgressBar;

public class CreateWalletTask extends AsyncTask<CreateWalletRequest , Void , String> {
    ProgressBar progressBar;
    Context context;
    OnSuccessMessage delegate;


    public CreateWalletTask(Context context, OnSuccessMessage delegate) {
        this.context = context;
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context , "Please Wait.....");
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressBar.hideProgressDialogWithTitle();
        if(!s.isEmpty()) {
            delegate.onSuccess(s);
        }
    }



    @Override
    protected String doInBackground(CreateWalletRequest... createWalletRequests) {
        String xml = createWalletRequests[0]
                .getXML();
        Log.e("doInBackground: ", xml);
        String responseString = HTTPHelper.getResponse(createWalletRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_CREATE_WALLET
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        String response = "";
        try {
            assert jsonObject != null;
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("CreateWalletResponse").getJSONObject("CreateWalletResult");
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            Log.e("doInBackground:", jsonObject.toString());
            if (responseCode.equals("101")) {
                response = jsonObject.getString("Description");
                //    delegate.onSuccessResponse("Beneficairy Added Successfully");
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
