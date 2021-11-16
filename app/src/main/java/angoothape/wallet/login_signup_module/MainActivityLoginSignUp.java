package angoothape.wallet.login_signup_module;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import angoothape.wallet.R;
import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.MainSignupLoginLayoutBinding;
import angoothape.wallet.login_signup_module.viewmodels.LoginRegisterViewModel;

public class MainActivityLoginSignUp extends RitmanBaseActivity<MainSignupLoginLayoutBinding> {

    NavController navController;
    public LoginRegisterViewModel viewModel;


    @Override
    public int getLayoutId() {
        return R.layout.main_signup_login_layout;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        navController = Navigation.findNavController(this, R.id.dashboard);
        viewModel = new ViewModelProvider(this).get(LoginRegisterViewModel.class);

    }


    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (navController.getCurrentDestination().getId() == R.id.newLoginCheck) {
            finish();
        } else {
            navController.navigateUp();
        }
    }
}
