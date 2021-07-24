package ritman.wallet.billpayment.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.List;

import ritman.wallet.MoneyTransferModuleV.viewmodels.BankTransferViewModel;
import ritman.wallet.R;
import ritman.wallet.billpayment.viewmodel.BillPaymentViewModel;
import ritman.wallet.databinding.PaymentAmountValidationLayoutBinding;
import ritman.wallet.di.JSONdi.Status;
import ritman.wallet.di.JSONdi.restRequest.BillDetailRequest;
import ritman.wallet.di.JSONdi.restRequest.PayBillPaymentRequest;
import ritman.wallet.di.JSONdi.restResponse.BillDetailResponse;
import ritman.wallet.di.JSONdi.restResponse.PayBillPaymentResponse;
import ritman.wallet.dialogs.SingleButtonMessageDialog;
import ritman.wallet.fragments.BaseFragment;
import ritman.wallet.home_module.NewDashboardActivity;
import ritman.wallet.interfaces.OnDecisionMade;
import ritman.wallet.utils.Utils;

import static android.content.Context.WIFI_SERVICE;

public class PaymentAmountValidationFragment extends BaseFragment<PaymentAmountValidationLayoutBinding> implements OnDecisionMade {
    public List<BillDetailResponse.billlist> billlists;
    public List<PayBillPaymentResponse.Bill> bill;

    //BankTransferViewModel viewModel;
    BillPaymentViewModel viewModel;

    String Field1, Field2, Field3, MobileNumber, payment_amount, paymentamount_validation,pay_after_duedate,
            partial_pay,currency;
    String fieldName1, fieldName2, fieldName3, name;
    String BillerID, SkuID;
    String PayOutAmount;
    TextView textView, iemi;
    String IMEINumber, ipAddress, validationid, billercategory, paymentamount, CustomerName;

    String billamount,billerid,biller_status,payment_method,txn_date_time,payment_status,Payeemobileno;
    String bbps_ref_no, transactionrefno,source_ref_no;
    private static final int REQUEST_CODE = 101;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        billlists = new ArrayList<>();
        bill = new ArrayList<>();
        viewModel = new ViewModelProvider(this).get(BillPaymentViewModel.class);

        Field1 = getArguments().getString("Field1");
        Field2 = getArguments().getString("Field2");
        Field3 = getArguments().getString("Field3");
        BillerID = getArguments().getString("BillerID");
        MobileNumber = getArguments().getString("MobileNumber");
        fieldName1 = getArguments().getString("fieldName1");
        fieldName2 = getArguments().getString("fieldName2");
        fieldName3 = getArguments().getString("fieldName3");
        payment_amount = getArguments().getString("payment_amount");
        paymentamount_validation = getArguments().getString("paymentamount_validation");
        partial_pay = getArguments().getString("partial_pay");
        pay_after_duedate = getArguments().getString("pay_after_duedate");
        currency = getArguments().getString("currency");

        if (fieldName1 == null) {
            fieldName1 = "";
        } else {
            fieldName1 = getArguments().getString("fieldName1");
        }


        if (fieldName2 == null) {
            fieldName2 = "";
        } else {
            fieldName2 = getArguments().getString("fieldName2");
        }

        if (fieldName3 == null) {
            fieldName3 = "";
        } else {
            fieldName3 = getArguments().getString("fieldName3");
        }

        textView = getView().findViewById(R.id.getIPAddress);
        iemi = getView().findViewById(R.id.iemi);

        //getImeiNumberOfDevice();
        IMEINumber = getDeviceId(getContext());

        getIpAddressOfDevice();

        getBillDetails();


        //  getBillPaymnet();
    }

    @Override
    public int getLayoutId() {
        return R.layout.payment_amount_validation_layout;
    }

//    public void getImeiNumberOfDevice() {
//        TelephonyManager telephonyManager = (TelephonyManager) getContext().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
//        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE);
//            return;
//        }
//        IMEINumber = telephonyManager.getDeviceId();
//    }

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
        // textView.setText("Your Device IP Address: " + ipAddress);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "Permission granted.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Permission denied.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    void getBillDetails() {
        Utils.showCustomProgressDialog(getContext(), false);
        //binding.progressBar.setVisibility(View.VISIBLE);
        BillDetailRequest request = new BillDetailRequest();
        request.countryCode = "IN";
        request.BillerID = BillerID;
        request.Field1 = Field1;
        request.MobileNumber = MobileNumber;
        request.Field2 = Field2;
        request.Field3 = Field3;
        request.Field1Name = fieldName1;
        request.Field2Name = fieldName2;
        request.Field3Name = fieldName3;
        request.imei = IMEINumber;//"5468748458458454";
        request.ip = ipAddress;
        request.payment_amount = payment_amount;
        request.currency = currency;


        viewModel.getBillDetails(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()

                , response -> {
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));

                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            Utils.hideCustomProgressDialog();
                           // binding.progressBar.setVisibility(View.GONE);

                            validationid = response.resource.data.validationid;
                            billercategory = response.resource.data.biller_category;

                            if (validationid != null) {
                                getBillPaymnet();
                            }

                        } else if (response.resource.responseCode.equals(100)) {
                            onMessage(response.resource.data.message);
                            Utils.hideCustomProgressDialog();


                        }
                        else {
                            Utils.hideCustomProgressDialog();
                          // binding.progressBar.setVisibility(View.GONE);
                            onMessage(response.resource.description);
                            showSucces(response.resource.description, getString(R.string.biller_details)
                                    , true);

                        }
                    }
                });

    }


    void getBillPaymnet() {
        Utils.showCustomProgressDialog(getContext(), false);
        //binding.progressBar.setVisibility(View.VISIBLE);
        PayBillPaymentRequest request = new PayBillPaymentRequest();

        request.validationid = validationid;
        request.payment_type = "instapay";
        request.payment_amount = payment_amount;
        request.payment_remarks = "sfreg";
        request.MobileNumber = MobileNumber;
        request.iemiNumber = IMEINumber; //"5468748458458454";
        request.ip = ipAddress;
        request.os = "Android";
        request.AppName = "AGENTAPP";
        request.billercategory = "Pay_Bill";//billercategory;// testting mahanager gas CA no.Field1: 210000875164 mobno. 9852635241
        request.Payment_TypeID = "5";
        request.payment_method = "Wallet";
        request.mobileno = "9876543210";
        request.wallet_name = "RITpay";
        request.currency = currency;

        //  request.AccounNumber=binding.edtAacountNo.getText().toString();
        //  request.ifsc=binding.edtIfscCode.getText().toString();


        viewModel.getPayBill(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()

                , response -> {
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            onMessage(response.resource.description);
                            Utils.hideCustomProgressDialog();
                           // binding.progressBar.setVisibility(View.GONE);

                           billamount=response.resource.data.getBillPayRespData().getPaymentAmount();
                            billerid = response.resource.data.getBillPayRespData().getBillerid();
                            biller_status = response.resource.data.getBillPayRespData().getBillerStatus();
                            payment_method = response.resource.data.getBillPayRespData().getPaymentAccount().getPaymentMethod();
                            txn_date_time = response.resource.data.getBillPayRespData().getTxnDateTime();
                            payment_status = response.resource.data.getBillPayRespData().getPaymentStatus();
                            Payeemobileno = MobileNumber;
                            bbps_ref_no = response.resource.data.getBillPayRespData().getBbpsRefNo();
                            transactionrefno = response.resource.data.getBillPayRespData().getPaymentid();
                            source_ref_no = response.resource.data.getBillPayRespData().getSourceRefNo();

                            binding.billamount.setText(billamount);
                            binding.billerId.setText(billerid);
                            binding.billerStatus.setText(biller_status);
                            binding.paymentMethod.setText(payment_method);
                            binding.transactionDateTime.setText(txn_date_time);
                            binding.paymentStatus.setText(payment_status);
                            binding.payeeMobileno.setText(Payeemobileno);
                            binding.bbpsRefNo.setText(bbps_ref_no);
                            binding.transactionrefno.setText(transactionrefno);
                            binding.sourceRefNo.setText(source_ref_no);

                        } else {
                            Utils.hideCustomProgressDialog();
                             //binding.progressBar.setVisibility(View.GONE);
                            onMessage(response.resource.description);
                        }
                    }
                });

    }

    @Override
    public void showProgress() {
        super.showProgress();
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        super.hideProgress();

        binding.progressBar.setVisibility(View.GONE);
    }

    private void showSucces(String message, String title, boolean isError) {
        SingleButtonMessageDialog dialog = new
                SingleButtonMessageDialog(title
                , message, this,
                false, isError);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }

    @Override
    public void onProceed() {
        Intent intent = new Intent(getActivity(), NewDashboardActivity.class);
        startActivity(intent);
        getBaseActivity().finish();
    }

    @Override
    public void onCancel() {

    }
}
