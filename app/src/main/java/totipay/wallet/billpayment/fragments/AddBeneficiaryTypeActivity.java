package totipay.wallet.billpayment.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.Navigation;

import totipay.wallet.R;
import totipay.wallet.databinding.ActivityAddBaneficilyTypeBinding;
import totipay.wallet.fragments.BaseFragment;

public class AddBeneficiaryTypeActivity extends BaseFragment<ActivityAddBaneficilyTypeBinding> {

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.firstTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v)
                        .navigate(R.id.action_addBeneficiaryTypeActivity_to_electricityAddNewAccountActivity);
              //  startActivity(new Intent(getApplicationContext(), ElectricityAddNewAccountActivity.class));
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_baneficily_type;
    }

}
