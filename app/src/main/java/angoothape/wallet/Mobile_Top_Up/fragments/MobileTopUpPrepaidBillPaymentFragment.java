package angoothape.wallet.Mobile_Top_Up.fragments;

import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import angoothape.wallet.Mobile_Top_Up.MobileTopUpMainActivity;
import angoothape.wallet.Mobile_Top_Up.viewmodels.MobileTopUpViewModel;
import angoothape.wallet.R;
import angoothape.wallet.databinding.MobileTopupPrepaidBillpaymentLayoutBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.PayBillPaymentRequest;
import angoothape.wallet.di.JSONdi.restResponse.PayBillPaymentResponse;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.utils.Utils;

public class MobileTopUpPrepaidBillPaymentFragment extends BaseFragment<MobileTopupPrepaidBillpaymentLayoutBinding> {

    // BankTransferViewModel viewModel;

    MobileTopUpViewModel viewModel;

    String validationid, IMEINumber, ipAddress, payment_amount, MobileNumber;
    String billdate, billduedate, billstatus, billnumber, paymentmethod, biller_status;
    String transaction_date_time, paymentstatus, bbps_ref_no, transactionrefno, source_ref_no, billerName;

    public List<PayBillPaymentResponse.Bill> bill;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = ((MobileTopUpMainActivity) getBaseActivity()).viewModel;
        assert getArguments() != null;
        validationid = getArguments().getString("validationid");
        IMEINumber = getArguments().getString("IMEINumber");
        ipAddress = getArguments().getString("ipAddress");
        payment_amount = getArguments().getString("payment_amount");
        MobileNumber = getArguments().getString("MobileNumber");
        bill = new ArrayList<>();
        getBillPaymnet();
    }

    @Override
    public int getLayoutId() {
        return R.layout.mobile_topup_prepaid_billpayment_layout;
    }

    void getBillPaymnet() {
        Utils.showCustomProgressDialog(getContext(), false);
        // binding.progressBar.setVisibility(View.VISIBLE);
        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim();
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
        request.Customer_ID = viewModel.customerID;
        String body = RestClient.makeGSONString(request);

        AERequest aeRequest = new AERequest();
        aeRequest.body = AESHelper.encrypt(body.trim(), gKey.trim());
        Log.e("getBillPaymnet: ", body);
        viewModel.getPayBill(aeRequest, KeyHelper.getKey(getSessionManager().getMerchantName()).trim(),
                KeyHelper.getSKey(KeyHelper
                        .getKey(getSessionManager().getMerchantName())))
                .observe(getViewLifecycleOwner(), response -> {
                    if (response.status == Status.ERROR) {
                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            // binding.progressBar.setVisibility(View.GONE);
                            Utils.hideCustomProgressDialog();
                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , gKey);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<PayBillPaymentResponse>() {
                                }.getType();
                                PayBillPaymentResponse data = gson.fromJson(bodyy, type);
                                paymentmethod = data.getBillPayRespData().getPaymentAccount().getPaymentMethod();
                                biller_status = data.getBillPayRespData().getBillerStatus();
                                transaction_date_time = data.getBillPayRespData().getTxnDateTime();
                                paymentstatus = data.getBillPayRespData().getPaymentStatus();
                                bbps_ref_no = data.getBillPayRespData().getBbpsRefNo();
                                transactionrefno = data.getBillPayRespData().getPaymentid();
                                source_ref_no = data.getBillPayRespData().getSourceRefNo();
                                billerName = data.getBillPayRespData().getBillerName();

                                binding.billerName.setText(billerName);
                                binding.billAmount.setText(payment_amount);
                                binding.billDate.setText(billdate);
                                binding.billDueDate.setText(billduedate);
                                binding.billStatus.setText(billstatus);
                                binding.billNumber.setText(billnumber);
                                binding.paymentMethod.setText(paymentmethod);
                                binding.billerStatus.setText(biller_status);
                                binding.transactionDateTime.setText(transaction_date_time);
                                binding.paymentStatus.setText(paymentstatus);
                                binding.payeeMobileno.setText(MobileNumber);
                                binding.bbpsRefNo.setText(bbps_ref_no);
                                binding.transactionrefno.setText(transactionrefno);
                                binding.sourceRefNo.setText(source_ref_no);

                                onMessage(response.resource.description);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            // binding.progressBar.setVisibility(View.GONE);
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
