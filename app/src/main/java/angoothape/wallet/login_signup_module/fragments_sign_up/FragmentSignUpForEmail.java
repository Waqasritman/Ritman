package angoothape.wallet.login_signup_module.fragments_sign_up;

import android.os.Bundle;

import androidx.navigation.Navigation;

import android.text.TextUtils;

import angoothape.wallet.R;
import angoothape.wallet.databinding.FragmentSignUpForEmailBinding;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.login_signup_module.MainActivityLoginSignUp;
import angoothape.wallet.utils.CheckValidation;
import angoothape.wallet.utils.IsNetworkConnection;


public class FragmentSignUpForEmail extends BaseFragment<FragmentSignUpForEmailBinding> {


    @Override
    public boolean isValidate() {
        if(TextUtils.isEmpty(binding.mobilesignupb.getText().toString())) {
            onMessage(getString(R.string.pleaseenter_email_address));
            return false;
        } else if(!CheckValidation.isEmailValid(binding.mobilesignupb.getText().toString())) {
            onMessage(getString(R.string.invalid_email_address_txt));
            return false;
        }
        return true;
    }

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.continuesighupb.setOnClickListener(v -> {
            if(isValidate()) {
                ((MainActivityLoginSignUp) getBaseActivity())
                        .viewModel.registerUserRequest.email = binding.mobilesignupb.getText().toString();
                ((MainActivityLoginSignUp) getBaseActivity())
                        .viewModel.registerUserRequest.phoneNumber = "";
                Navigation.findNavController(v)
                        .navigate(R.id.action_fragmentSignUpForEmail_to_signUpOtpMobileFragment);
            }
        });


        binding.sentOtpEmail.setOnClickListener(v -> {
            if(IsNetworkConnection.checkNetworkConnection(getContext())) {
                Navigation.findNavController(v)
                        .navigateUp();
            } else {
                onMessage(getString(R.string.no_internet));
            }

        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_sign_up_for_email;
    }


}