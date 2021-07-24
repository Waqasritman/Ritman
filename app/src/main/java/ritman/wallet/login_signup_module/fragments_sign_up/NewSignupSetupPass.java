package ritman.wallet.login_signup_module.fragments_sign_up;

import android.content.Intent;
import android.os.Bundle;

import androidx.navigation.Navigation;

import ritman.wallet.home_module.NewDashboardActivity;
import ritman.wallet.NewLoginFScan;
import ritman.wallet.R;
import ritman.wallet.databinding.SetupPassNonbBinding;
import ritman.wallet.fragments.BaseFragment;

public class NewSignupSetupPass extends BaseFragment<SetupPassNonbBinding> {

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.fingersignup.setOnClickListener(v -> {
            Intent intent=new Intent(getActivity(), NewLoginFScan.class);
            startActivity(intent);

        });
        binding.createpinsignup.setOnClickListener(v -> Navigation.findNavController(v)
                .navigate(R.id.action_newSignupSetupPass_to_newSignupCreatepin));
        binding.skipsignup.setOnClickListener(v -> {
            Intent intent=new Intent(getActivity(), NewDashboardActivity.class);
            startActivity(intent);

        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.setup_pass_nonb;
    }

}
