package ritman.wallet.billpayment.updated_fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import ritman.wallet.MoneyTransferModuleV.viewmodels.BankTransferViewModel;
import ritman.wallet.R;
import ritman.wallet.billpayment.viewmodel.BillPaymentViewModel;
import ritman.wallet.databinding.PayBillFragmentLayoutBinding;
import ritman.wallet.di.JSONdi.Status;
import ritman.wallet.di.JSONdi.restRequest.BillDetailRequest;
import ritman.wallet.di.JSONdi.restRequest.PayBillPaymentRequest;
import ritman.wallet.di.JSONdi.restRequest.device;
import ritman.wallet.di.JSONdi.restRequest.payment_account;
import ritman.wallet.di.JSONdi.restResponse.PayBillPaymentResponse;
import ritman.wallet.fragments.BaseFragment;

public class PayBillFragment extends BaseFragment<PayBillFragmentLayoutBinding> {

    String validationid, paymentamount, MobileNumber, iemiNumber, ipAddress, billercategory;

    String CustomerName, billamount, netbillamount, billdate, billduedate, billstatus, billnumber, billerid, paymentmethod, biller_status;
    String transaction_date_time, paymentstatus, payeemobileno, bbps_ref_no, transactionrefno, source_ref_no, partial_pay,
            partial_payment, paymentamount_validation;

   // BankTransferViewModel viewModel;
    BillPaymentViewModel viewModel;

    String customerid;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(BillPaymentViewModel.class);

        CustomerName = getArguments().getString("CustomerName");
        billamount = getArguments().getString("billamount");
        netbillamount = getArguments().getString("netbillamount");
        billdate = getArguments().getString("billdate");
        billduedate = getArguments().getString("billduedate");
        billstatus = getArguments().getString("billstatus");
        billnumber = getArguments().getString("billnumber");
        billerid = getArguments().getString("billerid");
        paymentmethod = getArguments().getString("paymentmethod");
        biller_status = getArguments().getString("biller_status");
        transaction_date_time = getArguments().getString("transaction_date_time");
        paymentstatus = getArguments().getString("paymentstatus");
        payeemobileno = getArguments().getString("payeemobileno");
        bbps_ref_no = getArguments().getString("bbps_ref_no");
        transactionrefno = getArguments().getString("transactionrefno");
        source_ref_no = getArguments().getString("source_ref_no");
        paymentamount_validation = getArguments().getString("paymentamount_validation");
          partial_pay = getArguments().getString("partial_pay");
          partial_payment = getArguments().getString("partial_payment");
        if (paymentamount_validation.equals("Y")) {
            binding.billAmount.setText(billamount);
            binding.billerId.setText(billerid);
            binding.billerStatus.setText(biller_status);
            binding.paymentMethod.setText(paymentmethod);
            binding.transactionDateTime.setText(transaction_date_time);
            binding.paymentStatus.setText(paymentstatus);
            binding.payeeMobileno.setText(payeemobileno);
            binding.bbpsRefNo.setText(bbps_ref_no);
            binding.transactionrefno.setText(transactionrefno);
            binding.sourceRefNo.setText(source_ref_no);
        }

        else {
            binding.customerName.setText(CustomerName);
            binding.netBillAmount.setText(netbillamount);
            binding.billDate.setText(billdate);
            binding.billDueDate.setText(billduedate);
            binding.billStatus.setText(billstatus);
            binding.billNumber.setText(billnumber);
            binding.paymentMethod.setText(paymentmethod);
            binding.billerStatus.setText(biller_status);
            binding.transactionDateTime.setText(transaction_date_time);
            binding.paymentStatus.setText(paymentstatus);
            binding.payeeMobileno.setText(payeemobileno);
            binding.bbpsRefNo.setText(bbps_ref_no);
            binding.transactionrefno.setText(transactionrefno);
            binding.sourceRefNo.setText(source_ref_no);
          //  binding.billAmount.setText(billamount);
            //binding.billerId.setText(billerid);

            if (partial_pay.equals("Y")) {
                binding.billAmount.setText(partial_payment);
                binding.billerId.setText(billerid);
            } else {
                binding.billAmount.setText(billamount);
                binding.billerId.setText(billerid);
            }
        }







    }

    @Override
    public int getLayoutId() {
        return R.layout.pay_bill_fragment_layout;
    }


}
