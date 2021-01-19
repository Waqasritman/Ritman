package tootipay.wallet.e_gift.fragments;

import android.os.Bundle;
import android.view.View;
import androidx.navigation.Navigation;

import tootipay.wallet.R;
import tootipay.wallet.databinding.ActivityEGiftSentBinding;
import tootipay.wallet.fragments.BaseFragment;

public class EGiftSentActivity extends BaseFragment<ActivityEGiftSentBinding> {

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view)
                        .navigateUp();
            }
        });

        binding.viewReciept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_e_gift_sent;
    }
}