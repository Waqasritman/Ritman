package totipay.wallet.e_gift.fragments;

import android.os.Bundle;
import android.view.View;
import androidx.navigation.Navigation;

import totipay.wallet.R;
import totipay.wallet.databinding.ActivityEGiftSentBinding;
import totipay.wallet.fragments.BaseFragment;

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