package angoothape.wallet.billpayment.updated_fragments;

import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import angoothape.wallet.R;
import angoothape.wallet.billpayment.BillPaymentMainActivity;
import angoothape.wallet.billpayment.viewmodel.BillPaymentViewModel;
import angoothape.wallet.databinding.FragmentUtilityBillerDetailsBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.PayBillPaymentRequest;
import angoothape.wallet.di.JSONdi.restResponse.BillDetailResponse;
import angoothape.wallet.di.JSONdi.restResponse.BillDetailsMainResponse;
import angoothape.wallet.di.JSONdi.restResponse.PayBillPaymentResponse;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.home_module.NewDashboardActivity;
import angoothape.wallet.interfaces.OnDecisionMade;
import angoothape.wallet.utils.Utils;


public class UtilityBillerDetailsFragment extends BaseFragment<FragmentUtilityBillerDetailsBinding>
        implements OnDecisionMade {

    BillPaymentViewModel viewModel;
    String payment_amount, paymentamount_validation, currency, partial_payment, MobileNumber, netbillamount;
    String IMEINumber, ipAddress, validationid, CustomerName, partial_pay, biller_logo, pay_after_duedate, current_date, billduedate;
    Double partial_payment_double, netbillamount_double;

    BillDetailsMainResponse billDetails;


    @Override
    protected void injectView() {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void setUp(Bundle savedInstanceState) {

        viewModel = ((BillPaymentMainActivity) getBaseActivity()).viewModel;

        assert getArguments() != null;
        billDetails = getArguments().getParcelable("bill_details");

        MobileNumber = getArguments().getString("MobileNumber");
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

        binding.billAmount.setEnabled(partial_pay.equals("Y"));
        fillData(billDetails);

        binding.toPayment.setOnClickListener(v -> {

            if (partial_pay.equals("Y")) {

                if (binding.billAmount.getText().toString().length() == 0) {
                    onMessage("Please enter bill amount");
                } else {
                    if (pay_after_duedate.equals("Y")) {
                        // onMessage(getString(R.string.billdue_date));
                        if (billduedate != null && current_date != null) {
                            if (billduedate.compareTo(current_date) > 0) {
                                onMessage(getString(R.string.billdue_date));
                            } else {
                                partial_payment_double = Double.parseDouble(binding.billAmount.getText().toString());
                                DecimalFormat decim = new DecimalFormat("0.00");
                                partial_payment = (decim.format(partial_payment_double));

                                netbillamount_double = Double.parseDouble(netbillamount);

                                if (partial_payment_double > netbillamount_double) {
                                    onMessage("Bill amount cannot be greater than Netbill amount");
                                }

                                if (partial_payment_double <= netbillamount_double) {
                                    getBillPayment();
                                }
                            }
                        } else {
                            getBillPayment();
                        }
                    } else {
                        partial_payment_double = Double.parseDouble(binding.billAmount.getText().toString());
                        DecimalFormat decim = new DecimalFormat("0.00");
                        partial_payment = (decim.format(partial_payment_double));

                        netbillamount_double = Double.parseDouble(netbillamount);

                        if (partial_payment_double > netbillamount_double) {
                            onMessage("Bill amount cannot be greater than Netbill amount");
                        }

                        if (partial_payment_double <= netbillamount_double) {
                            getBillPayment();
                        }
                    }


                }
            } else {

                if (pay_after_duedate.equals("Y")) {
                    if (billduedate != null && current_date != null) {
                        if (billduedate.compareTo(current_date) > 0) {
                            onMessage(getString(R.string.billdue_date));
                        } else {
                            getBillPayment();
                        }
                    } else {
                        getBillPayment();
                    }
                } else {
                    getBillPayment();
                }
            }


        });

    }

    void fillData(BillDetailsMainResponse data) {
        validationid = data.billDetailResponse.validationid;
        // billercategory = data.billDetailResponse.biller_category;
        payment_amount = data.billDetailResponse.payment_amount;
        viewModel.customerId = data.customerID;
        List<BillDetailResponse.billlist> billlists = new ArrayList<>();
        if (data.billDetailResponse.billlist != null) {
            billlists.addAll(data.billDetailResponse.billlist);
        }

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
                netbillamount = billlists.get(i).net_billamount;
                billduedate = billlists.get(i).billduedate;

                binding.customerName.setText(CustomerName);
                binding.billAmount.setText(billlists.get(i).billamount);
                binding.netBillAmount.setText(billlists.get(i).net_billamount);
                binding.billDate.setText(billlists.get(i).billdate);
                binding.billDueDate.setText(billlists.get(i).billduedate);
                binding.billStatus.setText(billlists.get(i).billstatus);


                if (billlists.get(i).billnumber == null) {
                    if (billlists.get(i).billnumber.equals("")) {
                        binding.billNumber.setText("NA");
                    }
                } else {
                    binding.billNumber.setText(billlists.get(i).billnumber);
                }

                if (billlists.get(i).billperiod == null) {
                    if (billlists.get(i).billperiod.isEmpty())
                        binding.billPeriod.setText("NA");
                } else {
                    binding.billPeriod.setText(billlists.get(i).billperiod);
                }

            }

        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_utility_biller_details;
    }


    void getBillPayment() {
        Utils.showCustomProgressDialog(getContext(), false);
        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim();
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
        request.Customer_ID = viewModel.customerId;
        String body = RestClient.makeGSONString(request);
        Log.e("getBillPaymnet: ", body);
        AERequest aeRequest = new AERequest();
        aeRequest.body = AESHelper.encrypt(body.trim(), gKey.trim());

        viewModel.getPayBill(aeRequest, KeyHelper.getKey(getSessionManager().getMerchantName()).trim(),
                KeyHelper.getSKey(KeyHelper
                        .getKey(getSessionManager().getMerchantName())))
                .observe(getViewLifecycleOwner(), response -> {
                    if (response.status == Status.ERROR) {
                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            Utils.hideCustomProgressDialog();
                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , gKey);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<PayBillPaymentResponse>() {
                                }.getType();
                                PayBillPaymentResponse data = gson.fromJson(bodyy, type);
                                if (paymentamount_validation.equals("Y")) {

                                    Bundle bundle = new Bundle();
                                    bundle.putString("billamount", data.getBillPayRespData().getPaymentAmount());
                                    bundle.putString("billerid", data.getBillPayRespData().getBillerid());
                                    bundle.putString("biller_status", data.getBillPayRespData().getBillerStatus());
                                    bundle.putString("paymentmethod", data.getBillPayRespData().getPaymentAccount().getPaymentMethod());
                                    bundle.putString("transaction_date_time", data.getBillPayRespData().getTxnDateTime());
                                    bundle.putString("paymentstatus", data.getBillPayRespData().getPaymentStatus());
                                    bundle.putString("payeemobileno", MobileNumber);
                                    bundle.putString("bbps_ref_no", data.getBillPayRespData().getBbpsRefNo());
                                    bundle.putString("transactionrefno", data.getBillPayRespData().getPaymentid());
                                    bundle.putString("source_ref_no", data.getBillPayRespData().getSourceRefNo());
                                    bundle.putString("paymentamount_validation", paymentamount_validation);

                                    Navigation.findNavController(binding.getRoot()).navigate(R.id
                                            .action_utilityBillerDetailsFragment_to_payBillFragment, bundle);
                                } else {
                                    List<PayBillPaymentResponse.Bill> bill = new ArrayList<>();
                                    bill.addAll(data.getBillPayRespData().getBilllist());
                                    String billamount = "null", billdate = "", billstatus = "", billnumber = "", billerid = "";
                                    for (int i = 0; i < bill.size(); i++) {
                                        billamount = bill.get(i).getBillamount();
                                        netbillamount = bill.get(i).getNetBillamount();
                                        billdate = bill.get(i).getBilldate();
                                        billduedate = bill.get(i).getBillduedate();
                                        billstatus = bill.get(i).getBillstatus();
                                        billnumber = bill.get(i).getBillnumber();
                                        billerid = bill.get(i).getBillerid();
                                    }

                                    Bundle bundle = new Bundle();

                                    bundle.putString("CustomerName", CustomerName);
                                    bundle.putString("billamount", billamount);
                                    bundle.putString("netbillamount", netbillamount);
                                    bundle.putString("billdate", billdate);
                                    bundle.putString("billduedate", billduedate);
                                    bundle.putString("billstatus", billstatus);
                                    bundle.putString("billnumber", billnumber);
                                    bundle.putString("billerid", billerid);
                                    bundle.putString("paymentmethod", data.getBillPayRespData().getPaymentAccount().getPaymentMethod());
                                    bundle.putString("biller_status", data.getBillPayRespData().getBillerStatus());
                                    bundle.putString("transaction_date_time", data.getBillPayRespData().getTxnDateTime());
                                    bundle.putString("paymentstatus", data.getBillPayRespData().getPaymentStatus());
                                    bundle.putString("payeemobileno", MobileNumber);
                                    bundle.putString("bbps_ref_no", data.getBillPayRespData().getBbpsRefNo());
                                    bundle.putString("transactionrefno", data.getBillPayRespData().getPaymentid());
                                    bundle.putString("source_ref_no", data.getBillPayRespData().getSourceRefNo());
                                    bundle.putString("paymentamount_validation", paymentamount_validation);
                                    bundle.putString("partial_payment", partial_payment);
                                    bundle.putString("partial_pay", partial_pay);

                                    Navigation.findNavController(binding.getRoot()).navigate(R.id
                                            .action_utilityBillerDetailsFragment_to_payBillFragment, bundle);


                                    onMessage(response.resource.description);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (response.resource.responseCode.equals(305)) {
                            onMessage(response.resource.description + "\nTry again later");
                            Navigation.findNavController(binding.getRoot()).navigateUp();
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