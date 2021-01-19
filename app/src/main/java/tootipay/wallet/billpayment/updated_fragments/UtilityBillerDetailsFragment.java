package tootipay.wallet.billpayment.updated_fragments;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.Navigation;

import tootipay.wallet.R;
import tootipay.wallet.billpayment.BillPaymentMainActivity;
import tootipay.wallet.databinding.FragmentUtilityBillerDetailsBinding;
import tootipay.wallet.di.RequestHelper.WRPayBillRequest;
import tootipay.wallet.di.ResponseHelper.WRBillDetailsResponse;
import tootipay.wallet.fragments.BaseFragment;


public class UtilityBillerDetailsFragment extends BaseFragment<FragmentUtilityBillerDetailsBinding> {


    WRBillDetailsResponse response;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        if(getArguments() != null) {
            response = getArguments().getParcelable("bill_details");

            if(response.customerName.isEmpty()) {
                binding.customerName.setVisibility(View.GONE);
                binding.customerNoTxt.setVisibility(View.GONE);
            }

            binding.customerName.setText(response.customerName);
            binding.dueBalance.setText(response.balanceORDue);
            binding.dueDate.setText(response.billDueDate);
        }


        binding.toPayment.setOnClickListener(v -> {
            WRPayBillRequest request = ((BillPaymentMainActivity) getBaseActivity()).payBillRequest;
            request.customerNo = getSessionManager().getCustomerNo();
            request.payOutAmount = binding.dueBalance.getText().toString();
            request.languageId = getSessionManager().getlanguageselection();
            Bundle bundle = new Bundle();
            bundle.putParcelable("biller_plan", request);
            Navigation.findNavController(getView())
                    .navigate(R.id.action_utilityBillerDetailsFragment_to_WRBillerPaymentFragment2
                            , bundle);
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_utility_biller_details;
    }

}