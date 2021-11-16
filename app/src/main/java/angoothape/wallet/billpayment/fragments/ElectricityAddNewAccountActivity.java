package angoothape.wallet.billpayment.fragments;

import android.os.Bundle;
import android.view.View;


import angoothape.wallet.R;
import angoothape.wallet.databinding.ActivityElectricityAddNewAccountBinding;
import angoothape.wallet.fragments.BaseFragment;


public class ElectricityAddNewAccountActivity extends BaseFragment<ActivityElectricityAddNewAccountBinding> {

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.selectProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Navigation.findNavController(view)
//                        .navigate(R.id.action_electricityAddNewAccountActivity_to_electricityProviderActivity);
            }
        });

        binding.accuntBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_electricity_add_new_account;
    }


}