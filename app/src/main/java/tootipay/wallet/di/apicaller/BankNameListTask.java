package tootipay.wallet.di.apicaller;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import tootipay.wallet.R;
import tootipay.wallet.di.ApiHelper;
import tootipay.wallet.di.HTTPHelper;
import tootipay.wallet.di.RequestHelper.BankNameListRequest;
import tootipay.wallet.di.SoapActionHelper;
import tootipay.wallet.interfaces.OnGetBankNameList;
import tootipay.wallet.utils.ProgressBar;

public class BankNameListTask extends AsyncTask<BankNameListRequest, Void, List<String>> {

    ProgressBar progressBar;
    Context context;
    OnGetBankNameList delegate;

    public BankNameListTask(Context context, OnGetBankNameList delegate) {
        this.context = context;
        this.delegate = delegate;
    }

    @Override
    protected List<String> doInBackground(BankNameListRequest... bankNameListRequests) {
        String responseString = HTTPHelper.getResponse(bankNameListRequests[0].getXML(),
                SoapActionHelper.ACTION_GET_BANK_NAME
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        List<String> strings = new ArrayList<>();
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("BankNameListResponse").getJSONObject("BankNameListResult");
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            if (responseCode.equals("101")) {
                jsonObject = jsonObject.getJSONObject("obj").getJSONObject("diffgr:diffgram")
                        .getJSONObject("DocumentElement");
                JSONArray array = jsonObject.getJSONArray("BankList");
                //  purposeList.clear();
                strings = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    strings.add(object.getString("BankName"));
                }

            } else {
                delegate.onResponseMessage(message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return strings;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context, context.getResources().getString(R.string.loading_txt));
    }

    @Override
    protected void onPostExecute(List<String> strings) {
        super.onPostExecute(strings);
        progressBar.hideProgressDialogWithTitle();

        if (!strings.isEmpty()) {
            delegate.onGetBankList(strings);
        }
    }

}
