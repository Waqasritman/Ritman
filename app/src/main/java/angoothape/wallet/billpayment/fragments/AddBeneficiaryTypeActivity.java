package angoothape.wallet.billpayment.fragments;

import android.os.Bundle;
import android.view.View;

import angoothape.wallet.R;
import angoothape.wallet.databinding.ActivityAddBaneficilyTypeBinding;
import angoothape.wallet.fragments.BaseFragment;

public class AddBeneficiaryTypeActivity extends BaseFragment<ActivityAddBaneficilyTypeBinding> {

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.firstTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Navigation.findNavController(v)
//                        .navigate(R.id.action_addBeneficiaryTypeActivity_to_electricityAddNewAccountActivity);

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_baneficily_type;
    }

}
