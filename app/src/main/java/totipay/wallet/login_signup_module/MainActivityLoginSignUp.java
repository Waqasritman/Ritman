package totipay.wallet.login_signup_module;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import totipay.wallet.R;
import totipay.wallet.base.TootiBaseActivity;
import totipay.wallet.databinding.MainSignupLoginLayoutBinding;
import totipay.wallet.login_signup_module.viewmodels.LoginRegisterViewModel;
import totipay.wallet.utils.Constants;

public class MainActivityLoginSignUp extends TootiBaseActivity<MainSignupLoginLayoutBinding> {

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
