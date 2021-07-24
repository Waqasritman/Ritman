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
import ritman.wallet.di.XMLdi.RequestHelper.LoadWalletRequest;
import ritman.wallet.di.XMLdi.SoapActionHelper;
import ritman.wallet.interfaces.OnSuccessMessage;
import ritman.wallet.utils.ProgressBar;

public class LoadWalletTask extends AsyncTask<LoadWalletRequest, Void, String> {

    ProgressBar progressBar;
    Context context;
    OnSuccessMessage onSuccessMessage;

    public LoadWalletTask(Context context, OnSuccessMessage onSuccessMessage) {
        this.context = context;
        this.onSuccessMessage = onSuccessMessage;
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
            onSuccessMessage.onSuccess(s);
        }
    }

    @Override
    protected String doInBackground(LoadWalletRequest... loadWalletRequests) {
        Log.e("XML: ", loadWalletRequests[0].getXML());
        String responseString = HTTPHelper.getResponse(loadWalletRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_LOAD_WALLET
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        Log.e("doInBackground: ", xmlToJson.toJson().toString());
        JSONObject jsonObject = xmlToJson.toJson();
        String referenceNo = "";
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("LoadWalletResponse").getJSONObject("LoadWalletResult");
            String responseCode = jsonObject.getString("ResponseCode");
            Log.e("doInBackground: ", jsonObject.toString());
            String message = jsonObject.getString("Description");
            if (responseCode.equals("101")) {
                referenceNo = jsonObject.getString("Refrence_No");
            } else {
                onSuccessMessage.onResponseMessage(message);
                referenceNo = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return referenceNo;
    }
}
