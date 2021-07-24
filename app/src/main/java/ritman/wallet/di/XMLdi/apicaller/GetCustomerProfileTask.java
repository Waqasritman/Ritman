package ritman.wallet.di.XMLdi.apicaller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import ritman.wallet.R;
import ritman.wallet.di.XMLdi.ApiHelper;
import ritman.wallet.di.XMLdi.HTTPHelper;
import ritman.wallet.di.XMLdi.RequestHelper.GetCustomerProfileRequest;
import ritman.wallet.di.XMLdi.ResponseHelper.CustomerProfile;
import ritman.wallet.di.XMLdi.SoapActionHelper;
import ritman.wallet.interfaces.OnGetCustomerProfile;
import ritman.wallet.utils.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class GetCustomerProfileTask extends AsyncTask<GetCustomerProfileRequest, Void, CustomerProfile> {

    ProgressBar progressBar;
    Context context;
    OnGetCustomerProfile onGetCustomerProfile;
    boolean isLogin = false;


    public GetCustomerProfileTask(Context context, OnGetCustomerProfile onGetCustomerProfile) {
        this.context = context;
        this.onGetCustomerProfile = onGetCustomerProfile;
    }

    public GetCustomerProfileTask(Context context, boolean isLogin, OnGetCustomerProfile onGetCustomerProfile) {
        this.context = context;
        this.onGetCustomerProfile = onGetCustomerProfile;
        this.isLogin = isLogin;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (!isLogin) {
            progressBar = new ProgressBar();
            progressBar.showProgressDialogWithTitle(context, context.getResources()
                    .getString(R.string.loading_txt));
        }

    }


    @Override
    protected void onPostExecute(CustomerProfile customerProfile) {
        super.onPostExecute(customerProfile);
        if (!isLogin) {
            progressBar.hideProgressDialogWithTitle();
        }
        if (customerProfile != null) {
            onGetCustomerProfile.onGetCustomerProfile(customerProfile);
        } else {
            onGetCustomerProfile.onResponseMessage(context.getString(R.string.server_error));
        }
    }

    @Override
    protected CustomerProfile doInBackground(GetCustomerProfileRequest... getCustomerProfileRequests) {
        Log.e("doInBackground: ", getCustomerProfileRequests[0]
                .getXML());
        String responseString = HTTPHelper.getResponse(getCustomerProfileRequests[0]
                        .getXML(),
                SoapActionHelper.ACTION_GET_CUSTOMER
                , ApiHelper.METHOD_POST);
        XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();

        CustomerProfile customerProfile = new CustomerProfile();
        try {
            jsonObject = jsonObject.getJSONObject("s:Envelope").getJSONObject("s:Body")
                    .getJSONObject("GetCustomerProfileResponse").getJSONObject("GetCustomerProfileResult");
            Log.e("doInBackground: ", jsonObject.toString());
            String responseCode = jsonObject.getString("ResponseCode");
            String message = jsonObject.getString("Description");
            if (responseCode.equals("101")) {
                customerProfile.firstName = jsonObject.getString("FirstName");
                customerProfile.middleName = jsonObject.getString("MiddleName");
                customerProfile.lastName = jsonObject.getString("LastName");
                customerProfile.address = jsonObject.getString("Address");
                customerProfile.gender = jsonObject.getString("Gender").trim();
                customerProfile.mobileNo = jsonObject.getString("MobileNumber");
                customerProfile.nationality = jsonObject.getString("Nationality");
                customerProfile.email = jsonObject.getString("EmailID");
                customerProfile.residenceCountry = jsonObject.getString("ResidenceCountry");
                customerProfile.isApprovedKYC = jsonObject.getBoolean("IsKYC_Approved");
                customerProfile.dateOfBirth = jsonObject.getString("DateOfBirth");
                // if (customerProfile.isApprovedKYC) {
                customerProfile.idNumber = jsonObject.getString("IDNumber");
                customerProfile.idType = jsonObject.getString("IDType");
                customerProfile.idIssueDate = jsonObject.getString("IDIssueDate");
                customerProfile.idExpireDate = jsonObject.getString("IDExpiryDate");
                customerProfile.sourceOfFund = jsonObject.getString("SourceOfFund");
                customerProfile.isActive = jsonObject.getString("IsActive");
                customerProfile.idTypeDescription = jsonObject.getString("IDtype_Description");
                customerProfile.residenceCountry = jsonObject.getString("ResidenceCountry");
                customerProfile.sourceOfDescription = jsonObject.getString("SourceOfFund_Desc");
                //  }
                customerProfile.isDocUploaded = jsonObject.getBoolean("isDocUploaded");


            } else {
                onGetCustomerProfile.onResponseMessage(message);
                customerProfile = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return customerProfile;
    }
}
