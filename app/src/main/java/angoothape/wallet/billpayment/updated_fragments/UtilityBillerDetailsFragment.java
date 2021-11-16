package angoothape.wallet.billpayment.updated_fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.DecimalFormat;
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
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import angoothape.wallet.R;
import angoothape.wallet.adapters.BillerDetailsAdapter;
import angoothape.wallet.billpayment.viewmodel.BillPaymentViewModel;
import angoothape.wallet.databinding.FragmentUtilityBillerDetailsBinding;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.BillDetailRequest;
import angoothape.wallet.di.JSONdi.restRequest.PayBillPaymentRequest;
import angoothape.wallet.di.JSONdi.restResponse.BillDetailResponse;
import angoothape.wallet.di.JSONdi.restResponse.PayBillPaymentResponse;
import angoothape.wallet.dialogs.SingleButtonMessageDialog;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.home_module.NewDashboardActivity;
import angoothape.wallet.interfaces.OnDecisionMade;
import angoothape.wallet.utils.Utils;

import static android.content.Context.WIFI_SERVICE;


public class UtilityBillerDetailsFragment extends BaseFragment<FragmentUtilityBillerDetailsBinding>
        implements OnDecisionMade {


    public List<BillDetailResponse.billlist> billlists;
    public List<PayBillPaymentResponse.Bill> bill;
    BillerDetailsAdapter billerDetailsAdapter;

    // BankTransferViewModel viewModel;
    BillPaymentViewModel viewModel;

    String Field1, Field2, Field3, MobileNumber, payment_amount, paymentamount_validation, currency, partial_payment;
    String fieldName1, fieldName2, fieldName3, name;
    String BillerID, SkuID;
    String PayOutAmount;
    TextView textView, iemi;
    String IMEINumber, ipAddress, validationid, billercategory, paymentamount, CustomerName;

    String billamount, netbillamount, billdate, billduedate, billstatus, billnumber, billerid, paymentmethod, biller_status;
    String transaction_date_time, paymentstatus, payeemobileno, bbps_ref_no, transactionrefno, source_ref_no, billperiod, biller_logo,
            partial_pay, pay_after_duedate, current_date;

    Double partial_payment_double, netbillamount_double;
    String price2;
    Date CurrentDate, BillDueDate;

    private static final int REQUEST_CODE = 101;
    //   ArrayList<BillDetailResponse> billDetailResponses;

    @Override
    protected void injectView() {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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
        biller_logo = getArguments().getString("biller_logo");

        Glide.with(this)
                .load(biller_logo)
                .placeholder(R.drawable.bbps_horizontal_1)
                .into(binding.imgBillernameLogo);
        binding.billAmount.setEnabled(false);

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        current_date = dateFormat.format(date);

        if (partial_pay.equals("Y")) {
            binding.billAmount.setEnabled(true);

        } else {
            binding.billAmount.setEnabled(false);
        }


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

        // setupPrePaidRecyclerView();


        binding.toPayment.setOnClickListener(v -> {

            if (partial_pay.equals("Y")) {

                if (binding.billAmount.getText().toString().length() == 0) {
                    onMessage("Please enter bill amount");
                }

                else {
                    if (pay_after_duedate.equals("Y")) {
                        // onMessage(getString(R.string.billdue_date));
                       if (billduedate!=null && current_date!=null) {
                           if (billduedate.compareTo(current_date) > 0) {
                               onMessage(getString(R.string.billdue_date));
                           }
                           else {
                               partial_payment_double = Double.parseDouble(binding.billAmount.getText().toString());
                               DecimalFormat decim = new DecimalFormat("0.00");
                               partial_payment = (decim.format(partial_payment_double));

                               netbillamount_double = Double.parseDouble(netbillamount);

                               if (partial_payment_double > netbillamount_double) {
                                   onMessage("Bill amount cannot be greater than Netbill amount");
                               }

                               if (partial_payment_double <= netbillamount_double) {
                                   getBillPaymnet();
                               }
                           }
                       }
                       else {
                           getBillPaymnet();
                       }
                    }

                    else {
                        partial_payment_double = Double.parseDouble(binding.billAmount.getText().toString());
                        DecimalFormat decim = new DecimalFormat("0.00");
                        partial_payment = (decim.format(partial_payment_double));

                        netbillamount_double = Double.parseDouble(netbillamount);

                        if (partial_payment_double > netbillamount_double) {
                            onMessage("Bill amount cannot be greater than Netbill amount");
                        }

                        if (partial_payment_double <= netbillamount_double) {
                            getBillPaymnet();
                        }
                    }


                }
            }


            else {

                if (pay_after_duedate.equals("Y")) {
                   // onMessage(getString(R.string.billdue_date));
                    if (billduedate != null && current_date != null) {
                        if (billduedate.compareTo(current_date) > 0) {
                            onMessage(getString(R.string.billdue_date));
                        }
                        else {
                            getBillPaymnet();
                        }
                    }
                    else {
                        getBillPaymnet();
                    }


                }
                else {
                    getBillPaymnet();
                }
            }


        });

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
        textView.setText("Your Device IP Address: " + ipAddress);
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


    @Override
    public int getLayoutId() {
        return R.layout.fragment_utility_biller_details;
    }


    private void setupPrePaidRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        billerDetailsAdapter = new BillerDetailsAdapter(billlists);
        binding.plansRecycler.setLayoutManager(mLayoutManager);
        binding.plansRecycler.setHasFixedSize(true);
        binding.plansRecycler.setAdapter(billerDetailsAdapter);
    }


    void getBillDetails() {
        Utils.showCustomProgressDialog(getContext(), false);
        //binding.progressBar.setVisibility(View.VISIBLE);
        BillDetailRequest request = new BillDetailRequest();
        if (paymentamount_validation.equals("Y")) {
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
        } else {
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
        }


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
                            billercategory = response.resource.data.biller_category;
                            payment_amount = response.resource.data.payment_amount;

                            billlists.addAll(response.resource.data.billlist);
                            if (paymentamount_validation.equals("Y")) {
                                binding.billAmount.setText(payment_amount);
                                binding.customerName.setText("NA");
                                binding.netBillAmount.setText("NA");
                                binding.billDate.setText("NA");
                                binding.billDueDate.setText("NA");
                                binding.billStatus.setText("NA");
                            } else {
                                for (int i = 0; i < billlists.size(); i++) {
                                    CustomerName = billlists.get(i).customer_name;
                                    billamount = billlists.get(i).billamount;
                                    netbillamount = billlists.get(i).net_billamount;
                                    billdate = billlists.get(i).billdate;
                                    billduedate = billlists.get(i).billduedate;
                                    billstatus = billlists.get(i).billstatus;
                                    billnumber = billlists.get(i).billnumber;
                                    billperiod = billlists.get(i).billperiod;

                                    binding.customerName.setText(CustomerName);
                                    binding.billAmount.setText(billamount);
                                    binding.netBillAmount.setText(netbillamount);
                                    binding.billDate.setText(billdate);
                                    binding.billDueDate.setText(billduedate);
                                    binding.billStatus.setText(billstatus);


                                    if (billnumber == null || billnumber.equals("")) {
                                        binding.billNumber.setText("NA");
                                    } else {
                                        binding.billNumber.setText(billnumber);
                                    }

                                    if (billperiod == null || billperiod.equals("")) {
                                        binding.billPeriod.setText("NA");
                                    } else {
                                        binding.billPeriod.setText(billperiod);
                                    }

                                }

                            }

                            //billerDetailsAdapter.notifyDataSetChanged();

                        } else if (response.resource.responseCode.equals(100)) {
                            onMessage(response.resource.data.message);
                            Utils.hideCustomProgressDialog();
                            // binding.progressBar.setVisibility(View.GONE);
                        } else {
                            // binding.progressBar.setVisibility(View.GONE);
                            Utils.hideCustomProgressDialog();
                            // onMessage(response.resource.description);
                            showSucces(response.resource.description, getString(R.string.biller_details)
                                    , true);
                        }
                    }
                });

    }


    void getBillPaymnet() {
        Utils.showCustomProgressDialog(getContext(), false);
        // binding.progressBar.setVisibility(View.VISIBLE);
        PayBillPaymentRequest request = new PayBillPaymentRequest();
        if (partial_pay.equals("Y")) {
            request.validationid = validationid;
            request.payment_type = "instapay";
            request.payment_amount = partial_payment;
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
        }


        request.validationid = validationid;
        request.payment_type = "instapay";
        if (paymentamount_validation.equals("Y")) {
            request.payment_amount = payment_amount;
            request.currency = currency;
        } else {
            request.payment_amount = netbillamount;
        }

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


        viewModel.getPayBill(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()

                , response -> {
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            // binding.progressBar.setVisibility(View.GONE);
                            Utils.hideCustomProgressDialog();
                            if (paymentamount_validation.equals("Y")) {
                                billamount = response.resource.data.getBillPayRespData().getPaymentAmount();
                                billerid = response.resource.data.getBillPayRespData().getBillerid();
                                biller_status = response.resource.data.getBillPayRespData().getBillerStatus();
                                paymentmethod = response.resource.data.getBillPayRespData().getPaymentAccount().getPaymentMethod();
                                transaction_date_time = response.resource.data.getBillPayRespData().getTxnDateTime();
                                paymentstatus = response.resource.data.getBillPayRespData().getPaymentStatus();
                                payeemobileno = MobileNumber;
                                bbps_ref_no = response.resource.data.getBillPayRespData().getBbpsRefNo();
                                transactionrefno = response.resource.data.getBillPayRespData().getPaymentid();
                                source_ref_no = response.resource.data.getBillPayRespData().getSourceRefNo();

                                Bundle bundle = new Bundle();
                                bundle.putString("billamount", billamount);
                                bundle.putString("billerid", billerid);
                                bundle.putString("biller_status", biller_status);
                                bundle.putString("paymentmethod", paymentmethod);
                                bundle.putString("transaction_date_time", transaction_date_time);
                                bundle.putString("paymentstatus", paymentstatus);
                                bundle.putString("payeemobileno", payeemobileno);
                                bundle.putString("bbps_ref_no", bbps_ref_no);
                                bundle.putString("transactionrefno", transactionrefno);
                                bundle.putString("source_ref_no", source_ref_no);
                                bundle.putString("paymentamount_validation", paymentamount_validation);

                                Navigation.findNavController(binding.getRoot()).navigate(R.id
                                        .action_utilityBillerDetailsFragment_to_payBillFragment, bundle);
                            } else {
                                bill.addAll(response.resource.data.getBillPayRespData().getBilllist());

                                for (int i = 0; i < bill.size(); i++) {
                                    billamount = bill.get(i).getBillamount();
                                    netbillamount = bill.get(i).getNetBillamount();
                                    billdate = bill.get(i).getBilldate();
                                    billduedate = bill.get(i).getBillduedate();
                                    billstatus = bill.get(i).getBillstatus();
                                    billnumber = bill.get(i).getBillnumber();
                                    billerid = bill.get(i).getBillerid();
                                }


                                paymentmethod = response.resource.data.getBillPayRespData().getPaymentAccount().getPaymentMethod();
                                biller_status = response.resource.data.getBillPayRespData().getBillerStatus();
                                transaction_date_time = response.resource.data.getBillPayRespData().getTxnDateTime();
                                paymentstatus = response.resource.data.getBillPayRespData().getPaymentStatus();
                                payeemobileno = MobileNumber;
                                bbps_ref_no = response.resource.data.getBillPayRespData().getBbpsRefNo();
                                transactionrefno = response.resource.data.getBillPayRespData().getPaymentid();
                                source_ref_no = response.resource.data.getBillPayRespData().getSourceRefNo();

                                Bundle bundle = new Bundle();

                                bundle.putString("CustomerName", CustomerName);
                                bundle.putString("billamount", billamount);
                                bundle.putString("netbillamount", netbillamount);
                                bundle.putString("billdate", billdate);
                                bundle.putString("billduedate", billduedate);
                                bundle.putString("billstatus", billstatus);
                                bundle.putString("billnumber", billnumber);
                                bundle.putString("billerid", billerid);
                                bundle.putString("paymentmethod", paymentmethod);
                                bundle.putString("biller_status", biller_status);
                                bundle.putString("transaction_date_time", transaction_date_time);
                                bundle.putString("paymentstatus", paymentstatus);
                                bundle.putString("payeemobileno", payeemobileno);
                                bundle.putString("bbps_ref_no", bbps_ref_no);
                                bundle.putString("transactionrefno", transactionrefno);
                                bundle.putString("source_ref_no", source_ref_no);
                                bundle.putString("paymentamount_validation", paymentamount_validation);
                                bundle.putString("partial_payment", partial_payment);
                                bundle.putString("partial_pay", partial_pay);

                                Navigation.findNavController(binding.getRoot()).navigate(R.id
                                        .action_utilityBillerDetailsFragment_to_payBillFragment, bundle);


                                onMessage(response.resource.description);
                            }
                        } else {
                            // binding.progressBar.setVisibility(View.GONE);
                            Utils.hideCustomProgressDialog();
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
    public void onCancel(boolean goBack)  {

    }
}