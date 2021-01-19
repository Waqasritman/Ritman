package tootipay.wallet.billpayment.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.Navigation;

import tootipay.wallet.R;
import tootipay.wallet.databinding.ActivityAddBaneficilyTypeBinding;
import tootipay.wallet.fragments.BaseFragment;

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
