package ritman.wallet.Mobile_Top_Up.fragments;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;

import ritman.wallet.Mobile_Top_Up.viewmodels.MobileTopUpViewModel;
import ritman.wallet.MoneyTransferModuleV.viewmodels.BankTransferViewModel;
import ritman.wallet.R;
import ritman.wallet.databinding.MobileTopUpPrepaidBilldetailsLayoutBinding;
import ritman.wallet.di.JSONdi.Status;
import ritman.wallet.di.JSONdi.restRequest.BillDetailRequest;
import ritman.wallet.fragments.BaseFragment;
import ritman.wallet.utils.Utils;

import static android.content.Context.WIFI_SERVICE;

public class MobileTopUpPrepaidBillDetailsFragment extends BaseFragment<MobileTopUpPrepaidBilldetailsLayoutBinding> {

   // BankTransferViewModel viewModel;
   MobileTopUpViewModel viewModel;

    String billerid, payment_amount, MobileNumber, IMEINumber, ipAddress, validationid, biller_name,
            biller_id,planid,Field1Name,Field2Name,Field3Name,Field1,Field2,Field3,billerlogo;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(MobileTopUpViewModel.class);
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

        binding.ProceedToPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("validationid",validationid);
                bundle.putString("IMEINumber",IMEINumber);
                bundle.putString("payment_amount",payment_amount);
                bundle.putString("MobileNumber",Field1);
                bundle.putString("ipAddress",ipAddress);
                Navigation.findNavController(binding.getRoot()).
                        navigate(R.id.action_mobileTopUpPrepaidBillDetailsFragment_to_mobileTopUpPrepaidBillPaymentFragment,bundle);
            }
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
        BillDetailRequest request = new BillDetailRequest();
        request.countryCode = "IN";
        request.BillerID = billerid;
        request.MobileNumber = Field1;
        if ((Field1Name!=null && Field1!=null) && (Field2Name!=null && Field2!=null)
                &&(Field3Name!=null && Field3!=null)){
            request.Field1Name = Field1Name;
            request.Field1 = Field1;
            request.Field2Name = Field2Name;
            request.Field2 = Field2;
            request.Field3Name = Field3Name;
            request.Field3 = Field3;
        }
        else if ((Field1Name!=null && Field1!=null) && (Field2Name!=null && Field2!=null)){
            request.Field1Name = Field1Name;
            request.Field1 = Field1;
            request.Field2Name = Field2Name;
            request.Field2 = Field2;
            request.Field3Name = "";
            request.Field3 = "";
        }
        else {
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


        viewModel.getBillDetails(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()

                , response -> {
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            //  binding.progressBar.setVisibility(View.GONE);
                            Utils.hideCustomProgressDialog();
                            validationid = response.resource.data.validationid;
                            biller_name = response.resource.data.biller_name;
                            biller_id = response.resource.data.billerid;

                            binding.billerName.setText(biller_name);
                            binding.billerId.setText(biller_id);
                            binding.paymentAmount.setText(payment_amount);


                        } else if (response.resource.responseCode.equals(100)) {
                            onMessage(response.resource.data.message);
                            Utils.hideCustomProgressDialog();
                            // binding.progressBar.setVisibility(View.GONE);
                        } else {
                            // binding.progressBar.setVisibility(View.GONE);
                            Utils.hideCustomProgressDialog();
                            onMessage(response.resource.description);

                        }
                    }
                });

    }
}
