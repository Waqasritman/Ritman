package angoothape.wallet.di.XMLdi.apicaller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import angoothape.wallet.R;
import angoothape.wallet.di.XMLdi.ApiHelper;
import angoothape.wallet.di.XMLdi.RequestHelper.WalletBalanceRequest;
import angoothape.wallet.di.XMLdi.SoapActionHelper;
import angoothape.wallet.interfaces.OnWalletBalanceReceived;
import angoothape.wallet.utils.ProgressBar;

public class WalletBalanceRequestTask extends
        AsyncTask<WalletBalanceRequest, Void, String> {


    ProgressBar progressBar;
    Context context;
    OnWalletBalanceReceived onWalletBalanceReceived;
    boolean isLogin = false;


    public WalletBalanceRequestTask(Context context, OnWalletBalanceReceived onWalletBalanceReceived) {
        this.context = context;
        this.onWalletBalanceReceived = onWalletBalanceReceived;
    }


    public WalletBalanceRequestTask(Context context, boolean isLogin, OnWalletBalanceReceived onWalletBalanceReceived) {
        this.context = context;
        this.onWalletBalanceReceived = onWalletBalanceReceived;
        this.isLogin = isLogin;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (!isLogin) {
            progressBar = new ProgressBar();
            progressBar.showProgressDialogWithTitle(context,
                    context.getResources().getString(R.string.loading_txt));
        }
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (!isLogin) {
            progressBar.hideProgressDialogWithTitle();
        }
        if (!s.isEmpty()) {
            onWalletBalanceReceived.onWalletBalanceReceived(s);
        }
    }


    @Override
    protected String doInBackground(WalletBalanceRequest... walletBalanceRequests) {
        Log.e("doInBackground: ", walletBalanceRequests[0]
                .getXML());
        String responseString = HTTPHelper.getResponse(walletBalanceRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_WALLET_BALANCE
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();

        String walletBalance = "";
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("WalletBalanceResponse").getJSONObject("WalletBalanceResult");
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            Log.e("doInBackground: ", jsonObject.toString());
            if (responseCode.equals("101")) {
                walletBalance = jsonObject.getString("WalletBalance");
            } else if (responseCode.equals("525")) {
                onWalletBalanceReceived.onLockWalletOption(false);
            }
            else {
                onWalletBalanceReceived.onResponseMessage(message);
                walletBalance = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return walletBalance;
    }
}
