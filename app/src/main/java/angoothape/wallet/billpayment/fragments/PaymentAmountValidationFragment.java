package angoothape.wallet.billpayment.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import angoothape.wallet.R;
import angoothape.wallet.billpayment.BillPaymentMainActivity;
import angoothape.wallet.billpayment.viewmodel.BillPaymentViewModel;
import angoothape.wallet.databinding.PaymentAmountValidationLayoutBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.BillDetailRequest;
import angoothape.wallet.di.JSONdi.restRequest.PayBillPaymentRequest;
import angoothape.wallet.di.JSONdi.restResponse.BillDetailResponse;
import angoothape.wallet.di.JSONdi.restResponse.BillDetailsMainResponse;
import angoothape.wallet.di.JSONdi.restResponse.PayBillPaymentResponse;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.dialogs.SingleButtonMessageDialog;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.home_module.NewDashboardActivity;
import angoothape.wallet.interfaces.OnDecisionMade;
import angoothape.wallet.utils.Utils;

import static android.content.Context.WIFI_SERVICE;

public class PaymentAmountValidationFragment extends BaseFragment<PaymentAmountValidationLayoutBinding> implements OnDecisionMade {
    public List<BillDetailResponse.billlist> billlists;
    public List<PayBillPaymentResponse.Bill> bill;

    //BankTransferViewModel viewModel;
    BillPaymentViewModel viewModel;

    String Field1, Field2, Field3, MobileNumber, payment_amount, paymentamount_validation, pay_after_duedate,
            partial_pay, currency;
    String fieldName1, fieldName2, fieldName3, name;
    String BillerID, SkuID;
    String PayOutAmount;
    TextView textView, iemi;
    String IMEINumber, ipAddress, validationid, billercategory, paymentamount, CustomerName;

    String billamount, billerid, biller_status, payment_method, txn_date_time, payment_status, Payeemobileno;
    String bbps_ref_no, transactionrefno, source_ref_no;
    private static final int REQUEST_CODE = 101;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        billlists = new ArrayList<>();
        bill = new ArrayList<>();
        viewModel = ((BillPaymentMainActivity)getBaseActivity()).viewModel;

        assert getArguments() != null;
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
        //binding.progressBar.setVisibility(View.VISIBLE);]
        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim();
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
        String body = RestClient.makeGSONString(request);
        AERequest requestc = new AERequest();
        requestc.body = AESHelper.encrypt(body.trim(), gKey.trim());

        viewModel.getBillDetails(requestc, KeyHelper.getKey(getSessionManager().getMerchantName()).trim(),
                KeyHelper.getSKey(KeyHelper
                        .getKey(getSessionManager().getMerchantName())))
                .observe(getViewLifecycleOwner(), response -> {
                    if (response.status == Status.ERROR) {
                        onError(getString(response.messageResourceId));

                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            Utils.hideCustomProgressDialog();
                            // binding.progressBar.setVisibility(View.GONE);
                            Utils.hideCustomProgressDialog();

                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , gKey);
                            Log.e( "getBillDetails: ",bodyy );
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<BillDetailsMainResponse>() {
                                }.getType();
                                BillDetailsMainResponse data = gson.fromJson(bodyy, type);
                                validationid = data.billDetailResponse.validationid;
                                billercategory = data.billDetailResponse.biller_category;
                                viewModel.customerId = data.customerID;
                                if (validationid != null) {
                                    getBillPaymnet();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else if (response.resource.responseCode.equals(100)) {
                            Utils.hideCustomProgressDialog();
                            if (response.resource.data != null) {
                                String bodyy = AESHelper.decrypt(response.resource.data.body
                                        , gKey);
                                if (!body.isEmpty()) {
                                    onError(bodyy);
                                } else {
                                    onError(response.resource.description);
                                }
                            } else {
                                onError(response.resource.description);
                            }


                        } else {

                            Utils.hideCustomProgressDialog();
                            if (response.resource.data != null) {
                                String bodyy = AESHelper.decrypt(response.resource.data.body
                                        , gKey);
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


    void getBillPaymnet() {
        Utils.showCustomProgressDialog(getContext(), false);
        //binding.progressBar.setVisibility(View.VISIBLE);
        PayBillPaymentRequest request = new PayBillPaymentRequest();
        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim();
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
        request.Customer_ID = viewModel.customerId;
        //  request.AccounNumber=binding.edtAacountNo.getText().toString();
        //  request.ifsc=binding.edtIfscCode.getText().toString();
        String body = RestClient.makeGSONString(request);

        AERequest aeRequest = new AERequest();
        aeRequest.body = AESHelper.encrypt(body.trim(), gKey.trim());
        Utils.showCustomProgressDialog(getContext(), false);

        viewModel.getPayBill(aeRequest, KeyHelper.getKey(getSessionManager().getMerchantName()).trim(),
                KeyHelper.getSKey(KeyHelper
                        .getKey(getSessionManager().getMerchantName())))
                .observe(getViewLifecycleOwner(), response -> {
                    if (response.status == Status.ERROR) {
                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            onMessage(response.resource.description);
                            Utils.hideCustomProgressDialog();
                            // binding.progressBar.setVisibility(View.GONE);
                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , gKey);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<PayBillPaymentResponse>() {
                                }.getType();
                                PayBillPaymentResponse data = gson.fromJson(bodyy, type);
                                billamount = data.getBillPayRespData().getPaymentAmount();
                                billerid = data.getBillPayRespData().getBillerid();
                                biller_status = data.getBillPayRespData().getBillerStatus();
                                payment_method = data.getBillPayRespData().getPaymentAccount().getPaymentMethod();
                                txn_date_time = data.getBillPayRespData().getTxnDateTime();
                                payment_status = data.getBillPayRespData().getPaymentStatus();
                                Payeemobileno = MobileNumber;
                                bbps_ref_no = data.getBillPayRespData().getBbpsRefNo();
                                transactionrefno = data.getBillPayRespData().getPaymentid();
                                source_ref_no = data.getBillPayRespData().getSourceRefNo();

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
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            Utils.hideCustomProgressDialog();
                            if (response.resource.data != null) {
                                String bodyy = AESHelper.decrypt(response.resource.data.body
                                        , gKey);
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
    public void onCancel(boolean goBack) {

    }
}
