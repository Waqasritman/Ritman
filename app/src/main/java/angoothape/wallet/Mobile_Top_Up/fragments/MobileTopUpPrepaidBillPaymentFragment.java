package angoothape.wallet.Mobile_Top_Up.fragments;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import angoothape.wallet.Mobile_Top_Up.viewmodels.MobileTopUpViewModel;
import angoothape.wallet.R;
import angoothape.wallet.databinding.MobileTopupPrepaidBillpaymentLayoutBinding;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.PayBillPaymentRequest;
import angoothape.wallet.di.JSONdi.restResponse.PayBillPaymentResponse;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.utils.Utils;

public class MobileTopUpPrepaidBillPaymentFragment extends BaseFragment<MobileTopupPrepaidBillpaymentLayoutBinding> {

   // BankTransferViewModel viewModel;

    MobileTopUpViewModel viewModel;

    String validationid, IMEINumber, ipAddress, payment_amount, MobileNumber;
    String billamount, netbillamount, billdate, billduedate, billstatus, billnumber, billerid, paymentmethod, biller_status;
    String transaction_date_time, paymentstatus, payeemobileno, bbps_ref_no, transactionrefno, source_ref_no,billerName;

    public List<PayBillPaymentResponse.Bill> bill;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(MobileTopUpViewModel.class);
        validationid=getArguments().getString("validationid");
        IMEINumber=getArguments().getString("IMEINumber");
        ipAddress=getArguments().getString("ipAddress");
        payment_amount=getArguments().getString("payment_amount");
        MobileNumber=getArguments().getString("MobileNumber");
        bill=new ArrayList<>();
        getBillPaymnet();
    }

    @Override
    public int getLayoutId() {
        return R.layout.mobile_topup_prepaid_billpayment_layout;
    }

    void getBillPaymnet() {
        Utils.showCustomProgressDialog(getContext(), false);
        // binding.progressBar.setVisibility(View.VISIBLE);
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

        viewModel.getPayBill(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()

                , response -> {
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            // binding.progressBar.setVisibility(View.GONE);
                            Utils.hideCustomProgressDialog();


                            //bill.addAll(response.resource.data.getBillPayRespData().getBilllist());

//                            for (int i = 0; i < bill.size(); i++) {
//                                billamount = bill.get(i).getBillamount();
//                                netbillamount = bill.get(i).getNetBillamount();
//                                billdate = bill.get(i).getBilldate();
//                                billduedate = bill.get(i).getBillduedate();
//                                billstatus = bill.get(i).getBillstatus();
//                                billnumber = bill.get(i).getBillnumber();
//                                billerid = bill.get(i).getBillerid();
//                            }


                            paymentmethod = response.resource.data.getBillPayRespData().getPaymentAccount().getPaymentMethod();
                            biller_status = response.resource.data.getBillPayRespData().getBillerStatus();
                            transaction_date_time = response.resource.data.getBillPayRespData().getTxnDateTime();
                            paymentstatus = response.resource.data.getBillPayRespData().getPaymentStatus();
                            bbps_ref_no = response.resource.data.getBillPayRespData().getBbpsRefNo();
                            transactionrefno = response.resource.data.getBillPayRespData().getPaymentid();
                            source_ref_no = response.resource.data.getBillPayRespData().getSourceRefNo();
                            billerName = response.resource.data.getBillPayRespData().getBillerName();

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

                        } else {
                            // binding.progressBar.setVisibility(View.GONE);
                            Utils.hideCustomProgressDialog();
                            onMessage(response.resource.description);
                        }
                    }
                });

    }
}
