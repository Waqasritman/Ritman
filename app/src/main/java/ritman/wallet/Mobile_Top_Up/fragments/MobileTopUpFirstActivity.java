package ritman.wallet.Mobile_Top_Up.fragments;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import java.util.ArrayList;

import ritman.wallet.Mobile_Top_Up.MobileTopUpMainActivity;
import ritman.wallet.Mobile_Top_Up.viewmodels.MobileTopUpViewModel;
import ritman.wallet.MoneyTransferModuleV.viewmodels.BankTransferViewModel;
import ritman.wallet.R;

import ritman.wallet.databinding.ActivityMobileTopUpFirstBinding;
import ritman.wallet.di.JSONdi.Status;
import ritman.wallet.di.JSONdi.restRequest.PrepaidOperatorRequest;

import ritman.wallet.di.JSONdi.restResponse.PrepaidOperatorResponse;
import ritman.wallet.fragments.BaseFragment;
import ritman.wallet.utils.CheckValidation;
import ritman.wallet.utils.IsNetworkConnection;
import ritman.wallet.utils.Utils;

public class MobileTopUpFirstActivity extends BaseFragment<ActivityMobileTopUpFirstBinding> {

    MobileTopUpViewModel viewModel;
    String billerid, circle_name, MobileNumber,Field1Name,Field2Name,Field3Name,billerlogo;
    ArrayList<PrepaidOperatorResponse.BillerField> billerFields;

    @Override
    protected void injectView() {

    }

    public boolean isNumberValidate() {
        if (TextUtils.isEmpty(binding.numberLayout.mobilesignupb.getText().toString())) {
            onMessage(getString(R.string.plz_enter_mobile_no));
            return false;
        } else if (!CheckValidation.isPhoneNumberValidate(binding.numberLayout.mobilesignupb.getText().toString(),
                binding.numberLayout.countryCodeTextView.getText().toString())) {
            onMessage(getString(R.string.invalid_number));

            return false;
        }
        return true;
    }

    @Override
    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.numberLayout.mobilesignupb.getText().toString())) {
            onMessage(getString(R.string.plz_enter_mobile_no));
            return false;
        } else if (!CheckValidation.isPhoneNumberValidate(binding.numberLayout.mobilesignupb.getText().toString(),
                binding.numberLayout.countryCodeTextView.getText().toString())) {
            onMessage(getString(R.string.invalid_number));
            return false;
        } else if (TextUtils.isEmpty(binding.operator.getText())) {
            onMessage(getString(R.string.plz_select_top_up_type));
            return false;
        } else if (TextUtils.isEmpty(binding.selectCategory.getText())) {
            onMessage(getString(R.string.plz_select_category));
            return false;
        } else if (TextUtils.isEmpty(binding.selectOperator.getText())) {
            onMessage(getString(R.string.plz_select_operator));
            return false;
        }
        return true;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        billerFields = new ArrayList<>();
        viewModel = ((MobileTopUpMainActivity) getBaseActivity()).viewModel;
        binding.nextLayout.setOnClickListener(v -> {
            if (isNumberValidate()) {
                MobileNumber = binding.numberLayout.mobilesignupb.getText().toString();
                getPrepaidOperator();
            }
        });
    }

    void getPrepaidOperator() {
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            Utils.showCustomProgressDialog(getContext(), false);
            PrepaidOperatorRequest request = new PrepaidOperatorRequest();
            request.MobileNo = binding.numberLayout.mobilesignupb.getText().toString();
            request.CountryCode = "IN";

            viewModel.getOperator(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()
                    , response -> {
                        Utils.hideCustomProgressDialog();
                        if (response.status == Status.ERROR) {
                            onMessage(getString(response.messageResourceId));
                        } else {
                            assert response.resource != null;
                            if (response.resource.responseCode.equals(101)) {
                                billerid = response.resource.data.getBillerid();
                                circle_name = response.resource.data.getCircleName();
                                billerlogo = response.resource.data.getBillerLogo();
                                billerFields.addAll(response.resource.data.getBillerFields());
                                for (int i = 0; i < billerFields.size(); i++) {
                                    if (billerFields.size()==1){
                                        Field1Name=billerFields.get(0).getParameterName();
                                    }
                                     if (billerFields.size()==2){
                                        Field1Name=billerFields.get(0).getParameterName();
                                        Field2Name=billerFields.get(1).getParameterName();
                                    }
                                    if (billerFields.size()==3){
                                        Field1Name=billerFields.get(0).getParameterName();
                                        Field2Name=billerFields.get(1).getParameterName();
                                        Field3Name=billerFields.get(2).getParameterName();
                                    }
                                }
                                Bundle bundle = new Bundle();
                                bundle.putString("billerid", billerid);
                                bundle.putString("circle_name", circle_name);
                                bundle.putString("MobileNumber", MobileNumber);
                                bundle.putString("Field1Name", Field1Name);
                                bundle.putString("Field2Name", Field2Name);
                                bundle.putString("Field3Name", Field3Name);
                                bundle.putString("billerlogo", billerlogo);

                                Navigation.findNavController(binding.getRoot()).navigate(R.id
                                        .action_mobileTopUpFirstActivity_to_planNameFragment, bundle);
                            } else {
                                onMessage(response.resource.description);
                            }
                        }
                    });


        } else {
            onMessage(getString(R.string.no_internet));
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_mobile_top_up_first;
    }

}
