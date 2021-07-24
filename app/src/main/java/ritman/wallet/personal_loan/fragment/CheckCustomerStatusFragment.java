package ritman.wallet.personal_loan.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import retrofit2.Call;
import retrofit2.Response;
import ritman.wallet.R;
import ritman.wallet.databinding.CheckCustomerStatusFragmentLayoutBinding;
import ritman.wallet.di.JSONdi.Status;
import ritman.wallet.di.JSONdi.restRequest.CheckCustomerStatusRequest;
import ritman.wallet.di.JSONdi.restRequest.SimpleRequest;
import ritman.wallet.di.JSONdi.restResponse.CashePreApprovalResponse;
import ritman.wallet.di.JSONdi.retrofit.RestClient;
import ritman.wallet.di.generic_response.SimpleResponse;
import ritman.wallet.fragments.BaseFragment;
import ritman.wallet.personal_loan.viewmodels.PersonalLoanViewModel;
import ritman.wallet.utils.Utils;

import static android.content.Context.MODE_PRIVATE;

public class CheckCustomerStatusFragment extends BaseFragment<CheckCustomerStatusFragmentLayoutBinding> {
    String customerId;
    PersonalLoanViewModel viewModel;
    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(PersonalLoanViewModel.class);
        SharedPreferences sp = getActivity().getSharedPreferences("customerId", MODE_PRIVATE);
        customerId=sp.getString("customerId","");
        binding.btnCheckStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.edtPartnerCustomerId.getText().toString().equals("")) {
                   onMessage("Please Enter Partner Customer Id");
                }
                else if (!binding.edtPartnerCustomerId.getText().toString().equals(customerId)){
                    onMessage("Please enter valid Customer Id");
                }
                else {
                    getCustomerStatus();
                }
            }
        });
    }

    private void getCustomerStatus() {

        CheckCustomerStatusRequest request = new CheckCustomerStatusRequest();
        request.partner_customer_id=customerId;

        viewModel.getCustomerStatus(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()

                , response -> {
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            // onMessage(response.resource.description);
                            onMessage((String) response.resource.data.payLoad);
                            Navigation.findNavController(binding.getRoot()).
                                    navigate(R.id.action_checkCustomerStatusFragment_to_createCustomerFragment);
                            Utils.hideCustomProgressDialog();

                        }

                        else {
                            onMessage(response.resource.description);
                            Utils.hideCustomProgressDialog();
                        }
                    }
                });
           }

    @Override
    public int getLayoutId() {
        return R.layout.check_customer_status_fragment_layout;
    }
}
