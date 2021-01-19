package tootipay.wallet.KYC.fragments;

import android.os.Bundle;


import tootipay.wallet.databinding.ActivityFinalKycBinding;
import tootipay.wallet.fragments.BaseFragment;
import tootipay.wallet.R;

public class FinalKYCFragment extends BaseFragment<ActivityFinalKycBinding> {


    //cross buttons
    //kyc close
    //show card on bank
    //

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.finishBtn.setOnClickListener(v -> {
            getActivity().finish();
        });




    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_final_kyc;
    }

}
