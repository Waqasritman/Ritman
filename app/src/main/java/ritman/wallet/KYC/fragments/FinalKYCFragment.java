package ritman.wallet.KYC.fragments;

import android.os.Bundle;


import ritman.wallet.databinding.ActivityFinalKycBinding;
import ritman.wallet.fragments.BaseFragment;
import ritman.wallet.R;

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
