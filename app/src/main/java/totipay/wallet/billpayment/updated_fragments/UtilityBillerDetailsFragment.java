package totipay.wallet.billpayment.updated_fragments;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.Navigation;

import totipay.wallet.R;
import totipay.wallet.billpayment.BillPaymentMainActivity;
import totipay.wallet.databinding.FragmentUtilityBillerDetailsBinding;
import totipay.wallet.di.RequestHelper.WRBillDetailRequest;
import totipay.wallet.di.RequestHelper.WRPayBillRequest;
import totipay.wallet.di.ResponseHelper.WRBillDetailsResponse;
import totipay.wallet.di.apicaller.WRBillDetailsTask;
import totipay.wallet.fragments.BaseFragment;
import totipay.wallet.interfaces.OnWRBillDetail;
import totipay.wallet.utils.Constants;
import totipay.wallet.utils.IsNetworkConnection;


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