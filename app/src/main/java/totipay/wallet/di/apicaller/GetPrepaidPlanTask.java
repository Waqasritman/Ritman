package totipay.wallet.di.apicaller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import totipay.wallet.R;
import totipay.wallet.di.ApiHelper;
import totipay.wallet.di.HTTPHelper;
import totipay.wallet.di.RequestHelper.GetWRPrepaidPlansRequest;
import totipay.wallet.di.ResponseHelper.GetPrepaidPlansResponse;
import totipay.wallet.di.ResponseHelper.WRBillerTypeResponse;
import totipay.wallet.di.SoapActionHelper;
import totipay.wallet.interfaces.OnGetPrepaidPlans;
import totipay.wallet.utils.ProgressBar;

public class GetPrepaidPlanTask extends
        AsyncTask<GetWRPrepaidPlansRequest, Void, List<GetPrepaidPlansResponse>> {


    ProgressBar progressBar;
    Context context;
    OnGetPrepaidPlans prepaidPlans;


    public GetPrepaidPlanTask(Context context, OnGetPrepaidPlans prepaidPlans) {
        this.context = context;
        this.prepaidPlans = prepaidPlans;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context
                , context.getString(R.string.loading_txt));
    }


    @Override
    protected void onPostExecute(List<GetPrepaidPlansResponse> getPrepaidPlansResponses) {
        super.onPostExecute(getPrepaidPlansResponses);
        progressBar.hideProgressDialogWithTitle();
        if (!getPrepaidPlansResponses.isEmpty()) {
            prepaidPlans.onGetPrepaidPlans(getPrepaidPlansResponses);
        }
    }

    @Override
    protected List<GetPrepaidPlansResponse> doInBackground(GetWRPrepaidPlansRequest... getWRPrepaidPlansRequests) {
        Log.e("doInBackground: ",getWRPrepaidPlansRequests[0].getXML() );
        String responseString = HTTPHelper.getResponse(getWRPrepaidPlansRequests[0].getXML(),
                SoapActionHelper.ACTION_GET_PREPAID_PLAN
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        Log.e("doInBackground: ",jsonObject.toString());
        List<GetPrepaidPlansResponse> responseList = new ArrayList<>();
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("GetWRPrepaidPlansResponse").getJSONObject("GetWRPrepaidPlansResult");
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            if (responseCode.equals("101")) {
                JSONArray array = null;

                try {
                    array = jsonObject.getJSONObject("Obj").getJSONObject("plans")
                            .getJSONObject("TOPUP").getJSONArray("plansdata");
                } catch (Exception e) {
                    Log.e("TAG", "doInBackground: " + e.getLocalizedMessage());
                }


                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        jsonObject = array.getJSONObject(i);
                        GetPrepaidPlansResponse response = new GetPrepaidPlansResponse();
                        response.planId = jsonObject.getString("planId");
                        response.rechargeSubType = jsonObject.getString("rechargeSubType");
                        response.mrp = jsonObject.getString("MRP");
                        response.rechargeAmount = jsonObject.getString("rechargeAmount");
                        response.benefits = jsonObject.getString("benefits");
                        response.talkTime = jsonObject.getString("talktime");
                        responseList.add(response);
                    }
                } else {
                    jsonObject = jsonObject.getJSONObject("Obj").getJSONObject("plans")
                            .getJSONObject("TOPUP").getJSONObject("plansdata");
                    GetPrepaidPlansResponse response = new GetPrepaidPlansResponse();
                    response.planId = jsonObject.getString("planId");
                    response.rechargeSubType = jsonObject.getString("rechargeSubType");
                    response.mrp = jsonObject.getString("MRP");
                    response.rechargeAmount = jsonObject.getString("rechargeAmount");
                    response.benefits = jsonObject.getString("benefits");
                    response.talkTime = jsonObject.getString("talktime");
                    responseList.add(response);
                }
            } else {
                prepaidPlans.onResponseMessage(message);
                responseList.clear();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("listSize: ", String.valueOf(responseList.size()));
        return responseList;
    }
}
