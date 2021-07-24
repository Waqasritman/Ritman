package ritman.wallet.e_gift.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;

import ritman.wallet.R;
import ritman.wallet.SendMoney.SelectCardActivity_gift;
import ritman.wallet.databinding.ActivitySendEGiftBinding;
import ritman.wallet.fragments.BaseFragment;

public class SendEGiftActivity extends BaseFragment<ActivitySendEGiftBinding> {

    @Override
    protected void injectView() {

    }

    @Override
    public void onResume() {
        super.onResume();
        binding.toolBar.toolBar
                .setBackgroundColor(getResources().getColor(R.color.colorYellow));
        binding.toolBar.backBtn
                .setColorFilter(ContextCompat.getColor(getContext(),
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);
        binding.toolBar.titleTxt.setVisibility(View.GONE);
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.proceedToPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SelectCardActivity_gift.class));
            }
        });


        binding.toolBar.backBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_send_e_gift;
    }


}