package angoothape.wallet.billpayment.updated_fragments;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import angoothape.wallet.R;
import angoothape.wallet.billpayment.BillPaymentMainActivity;
import angoothape.wallet.billpayment.viewmodel.BillPaymentViewModel;
import angoothape.wallet.databinding.PayBillFragmentLayoutBinding;
import angoothape.wallet.fragments.BaseFragment;

public class PayBillFragment extends BaseFragment<PayBillFragmentLayoutBinding> {

    String CustomerName, billamount, netbillamount, billdate, billduedate, billstatus, billnumber, billerid, paymentmethod, biller_status;
    String transaction_date_time, paymentstatus, payeemobileno, bbps_ref_no, transactionrefno, source_ref_no, partial_pay,
            partial_payment, paymentamount_validation;

    BillPaymentViewModel viewModel;
    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = ((BillPaymentMainActivity) getBaseActivity()).viewModel;

        assert getArguments() != null;
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
        } else {
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
            if (partial_pay.equals("Y")) {
                binding.billAmount.setText(partial_payment);
            } else {
                binding.billAmount.setText(billamount);
            }
            binding.billerId.setText(billerid);
        }


    }

    @Override
    public int getLayoutId() {
        return R.layout.pay_bill_fragment_layout;
    }


}
