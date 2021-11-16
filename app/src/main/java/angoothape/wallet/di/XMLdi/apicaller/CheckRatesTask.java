package angoothape.wallet.di.XMLdi.apicaller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import angoothape.wallet.R;
import angoothape.wallet.di.XMLdi.ApiHelper;
import angoothape.wallet.di.XMLdi.HTTPHelper;
import angoothape.wallet.di.XMLdi.RequestHelper.CalTransferRequest;
import angoothape.wallet.di.XMLdi.ResponseHelper.CalTransferResponse;
import angoothape.wallet.di.XMLdi.SoapActionHelper;
import angoothape.wallet.interfaces.OnGetTransferRates;
import angoothape.wallet.utils.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class CheckRatesTask  extends AsyncTask<CalTransferRequest, Integer , CalTransferResponse> {

    Context context;
    ProgressBar progressBar;
    OnGetTransferRates delegate;

    public CheckRatesTask(Context context,OnGetTransferRates delegate) {
        this.context = context;
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(context ,
                context.getResources().getString(R.string.loading_txt));
    }

    @Override
    protected void onPostExecute(CalTransferResponse calTransferResponse) {
        super.onPostExecute(calTransferResponse);
        progressBar.hideProgressDialogWithTitle();
        if(calTransferResponse != null) {
            delegate.onGetTransferRates(calTransferResponse);
        }
    }

    @Override
    protected CalTransferResponse doInBackground(CalTransferRequest... calTransferRequests) {
        Log.e("doInBackground: ",calTransferRequests[0].getXML() );
        String responseString = HTTPHelper.getResponse(calTransferRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_CAL_TRANSFER
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        CalTransferResponse response = new CalTransferResponse();
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("CalTransferResponse").getJSONObject("CalTransferResult");
            String responseCode = jsonObject.getString("ResponseCode");
            Log.e("JSONResponse: ",jsonObject.toString() );
            String message = jsonObject.getString("Description");
            if (responseCode.equals("101")) {
                response.exchangeRate = jsonObject.getDouble("ExchangeRate");
                response.vatValue = jsonObject.getDouble("VATValue");
                response.payInAmount = jsonObject.getDouble("PayInAmount");
                response.payoutAmount = jsonObject.getDouble("PayoutAmount");
                response.recommendedAgent = jsonObject.getInt("RecommendAgent");
                response.commission = jsonObject.getDouble("Commission");
                response.totalPayable = jsonObject.getDouble("TotalPayable");
                response.vatPercentage = jsonObject.getDouble("VATPercentage");
                response.payoutBranchCode = jsonObject.getInt("PayoutBranchCode");
                try{
                    response.payInCurrency = jsonObject.getString("PayInCurrency");
                } catch (Exception e) {

                }
                try{
                    response.payOutCurrency = jsonObject.getString("PayoutCurrency");
                } catch (Exception ex) {
                    Log.e( "doInBackground: ", ex.getLocalizedMessage());
                }


            } else {
                delegate.onResponseMessage(message);
            //    onMessage(message);
                response = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }
}
