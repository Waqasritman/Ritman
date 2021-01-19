package tootipay.wallet.di.apicaller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import tootipay.wallet.R;
import tootipay.wallet.di.ApiHelper;
import tootipay.wallet.di.HTTPHelper;
import tootipay.wallet.di.RequestHelper.GetWRPrepaidOperatorRequest;
import tootipay.wallet.di.ResponseHelper.PrepaidOperatorResponse;
import tootipay.wallet.di.SoapActionHelper;
import tootipay.wallet.interfaces.OnGetPrepaidOperator;
import tootipay.wallet.utils.ProgressBar;

public class PrepaidOperatorTask extends
        AsyncTask<GetWRPrepaidOperatorRequest, Void, PrepaidOperatorResponse> {


    ProgressBar progressBar;
    Context context;
    OnGetPrepaidOperator onGetPrepaidOperator;


    public PrepaidOperatorTask(Context context, OnGetPrepaidOperator onGetPrepaidOperator) {
        this.context = context;
        this.onGetPrepaidOperator = onGetPrepaidOperator;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context
                , context.getResources().getString(R.string.loading_txt));
    }

    @Override
    protected void onPostExecute(PrepaidOperatorResponse prepaidOperatorResponse) {
        super.onPostExecute(prepaidOperatorResponse);
        progressBar.hideProgressDialogWithTitle();
        if (prepaidOperatorResponse != null) {
            onGetPrepaidOperator.onGetPrepaidOperator(prepaidOperatorResponse);
        }
    }

    @Override
    protected PrepaidOperatorResponse doInBackground(GetWRPrepaidOperatorRequest... getWRPrepaidOperatorRequests) {
        Log.e("XMLAuth: ", getWRPrepaidOperatorRequests[0]
                .getXML());
        String responseString = HTTPHelper.getResponse(getWRPrepaidOperatorRequests[0]
                        .getXML(),
                SoapActionHelper.GET_WR_PREPAID_OPERATOR_ACTION
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        PrepaidOperatorResponse operator = new PrepaidOperatorResponse();
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("GetWRPrepaidOperatorResponse").getJSONObject("GetWRPrepaidOperatorResult");
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            if (responseCode.equals("101")) {
                operator.operatorCode = jsonObject.getString("operatorCode");
                operator.operatorName = jsonObject.getString("operatorName");

                try {
                    operator.circleCode = jsonObject.getString("circleCode");
                } catch (Exception e) {

                }

                try {
                    operator.circleName = jsonObject.getString("circleName");
                } catch (Exception e) {

                }


            } else {
                onGetPrepaidOperator.onErrorWithCode(responseCode , message);
                operator = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return operator;
    }

}
