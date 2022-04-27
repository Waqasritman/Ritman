package angoothape.wallet.Mobile_Top_Up.fragments;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;

import angoothape.wallet.Mobile_Top_Up.MobileTopUpMainActivity;
import angoothape.wallet.Mobile_Top_Up.viewmodels.MobileTopUpViewModel;
import angoothape.wallet.R;
import angoothape.wallet.databinding.MobileTopUpPrepaidBilldetailsLayoutBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.BillDetailRequest;
import angoothape.wallet.di.JSONdi.restResponse.BillDetailResponse;
import angoothape.wallet.di.JSONdi.restResponse.BillDetailsMainResponse;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.utils.Utils;

import static android.content.Context.WIFI_SERVICE;

public class MobileTopUpPrepaidBillDetailsFragment extends BaseFragment<MobileTopUpPrepaidBilldetailsLayoutBinding> {

    // BankTransferViewModel viewModel;
    MobileTopUpViewModel viewModel;

    String billerid, payment_amount, IMEINumber, ipAddress, validationid, biller_name,
            biller_id, Field1Name, Field2Name, Field3Name, Field1, Field2, Field3, billerlogo;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = ((MobileTopUpMainActivity) getBaseActivity()).viewModel;
        assert getArguments() != null;
        billerid = getArguments().getString("billerid");
        payment_amount = getArguments().getString("payment_amount");
        Field1 = getArguments().getString("MobileNumber");
        Field2 = getArguments().getString("planid");
        Field1Name = getArguments().getString("Field1Name");
        Field2Name = getArguments().getString("Field2Name");
        Field3Name = getArguments().getString("Field3Name");
        billerlogo = getArguments().getString("billerlogo");

        Glide.with(this)
                .load(billerlogo)
                .placeholder(R.drawable.bbps_horizontal_1)
                .into(binding.imgBillernameLogo);


        IMEINumber = getDeviceId(getContext());
        getIpAddressOfDevice();

        getPrepaidBillDetails();

        binding.ProceedToPay.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("validationid", validationid);
            bundle.putString("IMEINumber", IMEINumber);
            bundle.putString("payment_amount", payment_amount);
            bundle.putString("MobileNumber", Field1);
            bundle.putString("ipAddress", ipAddress);
            Navigation.findNavController(binding.getRoot()).
                    navigate(R.id.action_mobileTopUpPrepaidBillDetailsFragment_to_mobileTopUpPrepaidBillPaymentFragment, bundle);
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.mobile_top_up_prepaid_billdetails_layout;
    }

    public static String getDeviceId(Context context) {

        String deviceId;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deviceId = Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephony.getDeviceId() != null) {
                deviceId = mTelephony.getDeviceId();
            } else {
                deviceId = Settings.Secure.getString(
                        context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
        }

        return deviceId;
    }

    public void getIpAddressOfDevice() {
        WifiManager wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(WIFI_SERVICE);
        ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
    }

    void getPrepaidBillDetails() {
        Utils.showCustomProgressDialog(getContext(), false);
        //binding.progressBar.setVisibility(View.VISIBLE);
        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim();
        BillDetailRequest request = new BillDetailRequest();
        request.countryCode = "IN";
        request.BillerID = billerid;
        request.MobileNumber = Field1;
        if ((Field1Name != null && Field1 != null) && (Field2Name != null && Field2 != null)
                && (Field3Name != null && Field3 != null)) {
            request.Field1Name = Field1Name;
            request.Field1 = Field1;
            request.Field2Name = Field2Name;
            request.Field2 = Field2;
            request.Field3Name = Field3Name;
            request.Field3 = Field3;
        } else if ((Field1Name != null && Field1 != null) && (Field2Name != null && Field2 != null)) {
            request.Field1Name = Field1Name;
            request.Field1 = Field1;
            request.Field2Name = Field2Name;
            request.Field2 = Field2;
            request.Field3Name = "";
            request.Field3 = "";
        } else {
            request.Field1Name = Field1Name;
            request.Field1 = Field1;
            request.Field2Name = "";
            request.Field2 = "";
            request.Field3Name = "";
            request.Field3 = "";
        }

        request.imei = IMEINumber;//"5468748458458454";
        request.ip = ipAddress;
        request.payment_amount = payment_amount;
        request.currency = "356";
        String body = RestClient.makeGSONString(request);
        AERequest requestc = new AERequest();
        requestc.body = AESHelper.encrypt(body.trim(), gKey.trim());
        Utils.showCustomProgressDialog(getContext(), false);
        viewModel.getBillDetails(requestc, KeyHelper.getKey(getSessionManager().getMerchantName()).trim(),
                KeyHelper.getSKey(KeyHelper
                        .getKey(getSessionManager().getMerchantName())))
                .observe(getViewLifecycleOwner(), response -> {
                    if (response.status == Status.ERROR) {
                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            //  binding.progressBar.setVisibility(View.GONE);
                            Utils.hideCustomProgressDialog();

                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , gKey);
                            // RIT23479022547
                            Log.e("getPrepaidBillDetails: ", bodyy);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<BillDetailsMainResponse>() {
                                }.getType();
                                BillDetailsMainResponse data = gson.fromJson(bodyy, type);

                                validationid = data.billDetailResponse.validationid;
                                biller_name = data.billDetailResponse.biller_name;
                                biller_id = data.billDetailResponse.billerid;

                                binding.billerName.setText(biller_name);
                                binding.billerId.setText(biller_id);
                                binding.paymentAmount.setText(payment_amount);
                                viewModel.customerID = data.customerID;


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (response.resource.responseCode.equals(100)) {
                            Utils.hideCustomProgressDialog();
                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , gKey);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<BillDetailsMainResponse>() {
                                }.getType();
                                BillDetailsMainResponse data = gson.fromJson(bodyy, type);
                                onError(data.billDetailResponse.message);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (response.resource.responseCode.equals(305)) {
                            onMessage(response.resource.description + "\nTry again later");
                            Navigation.findNavController(binding.getRoot()).navigateUp();
                        }else {
                            Utils.hideCustomProgressDialog();
                            if (response.resource.data != null) {
                                String bodyy = AESHelper.decrypt(response.resource.data.body
                                        , gKey);
                                Log.e("getBillDetails: ", bodyy);
                                if (!body.isEmpty()) {
                                    onError(bodyy);
                                } else {
                                    onError(response.resource.description);
                                }
                            } else {
                                onError(response.resource.description);
                            }
                        }
                    }
                });

    }
}
