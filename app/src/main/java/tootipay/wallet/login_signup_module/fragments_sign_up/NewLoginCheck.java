package tootipay.wallet.login_signup_module.fragments_sign_up;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.navigation.Navigation;

import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;

import tootipay.wallet.NewSplash;
import tootipay.wallet.R;

import tootipay.wallet.databinding.NewSignupLoginBinding;
import tootipay.wallet.fragments.BaseFragment;
import tootipay.wallet.utils.ContextWrapper;


public class NewLoginCheck extends BaseFragment<NewSignupLoginBinding> {


    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.buttonCreate.setOnClickListener(v -> Navigation.findNavController(v)
                .navigate(R.id.action_newLoginCheck_to_signUpMobileNoFragment));


        binding.already.setOnClickListener(v -> {
            Navigation.findNavController(v)
                    .navigate(R.id.action_newLoginCheck_to_newLoginOption);
        });


        if (getSessionManager().getDefaultLanguage().equalsIgnoreCase("en")) {
            binding.languageSpinner.setHint(getString(R.string.arabic_txt));
        } else {
            binding.languageSpinner.setHint(getString(R.string.english_txt));
        }


        binding.languageSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(int position, String item) {
                onSignOut();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.new_signup_login;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onSignOut() {
        if (getSessionManager().getlanguageselection().equalsIgnoreCase("4")) {
            ContextWrapper.changeLang(getContext(), "en");
            getSessionManager().setDefaultLanuguage("en");
            getSessionManager().setlanguageselection("1");
        } else if (getSessionManager().getlanguageselection().equalsIgnoreCase("1")) {
            ContextWrapper.changeLang(getContext(), "ar");
            getSessionManager().setDefaultLanuguage("ar");
            getSessionManager().setlanguageselection("4");
        }
        Intent intent = new Intent(getBaseActivity(), NewSplash.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }

}
