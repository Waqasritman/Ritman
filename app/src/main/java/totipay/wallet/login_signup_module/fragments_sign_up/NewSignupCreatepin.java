package totipay.wallet.login_signup_module.fragments_sign_up;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import totipay.wallet.home_module.NewDashboardActivity;
import totipay.wallet.R;
import totipay.wallet.databinding.NewpinBankingBinding;
import totipay.wallet.fragments.BaseFragment;

public class NewSignupCreatepin extends BaseFragment<NewpinBankingBinding> {


    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.signuppinconfirm.setOnClickListener(v -> {
            Intent intent=new Intent(getActivity(), NewDashboardActivity.class);
            startActivity(intent);
            getActivity().finish();
            Toast.makeText(getContext(), "New pin created!",
                    Toast.LENGTH_LONG).show();

        });

        binding.skipsignuppin.setOnClickListener(v -> {
            Intent intent=new Intent(getActivity(),NewDashboardActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.newpin_banking;
    }

}
