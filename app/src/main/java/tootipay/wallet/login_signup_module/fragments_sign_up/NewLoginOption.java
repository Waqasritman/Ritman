package tootipay.wallet.login_signup_module.fragments_sign_up;

import android.os.Bundle;
import androidx.navigation.Navigation;

import tootipay.wallet.R;
import tootipay.wallet.databinding.OptionLoginBinding;
import tootipay.wallet.fragments.BaseFragment;



public class NewLoginOption extends BaseFragment<OptionLoginBinding> {


    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {

        binding.umobile.setOnClickListener(v -> Navigation.findNavController(v)
                .navigate(R.id.action_newLoginOption_to_loginWithNumber));


        binding.usingEmail.setOnClickListener(v -> {
            Navigation.findNavController(v)
                    .navigate(R.id.action_newLoginOption_to_loginFragment);
        });

        binding.signup.setOnClickListener(v -> {
            Navigation.findNavController(v)
            .navigate(R.id.action_newLoginOption_to_signUpMobileNoFragment);
        });


        binding.toolBar.backBtn.setOnClickListener(v -> Navigation.findNavController(v)
                .navigateUp());

    }

    @Override
    public int getLayoutId() {
        return R.layout.option_login;
    }


}
