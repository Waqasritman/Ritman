package angoothape.wallet.e_gift.fragments;

import android.os.Bundle;

import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;

import angoothape.wallet.R;
import angoothape.wallet.databinding.FragmentGiftReceiverBinding;
import angoothape.wallet.fragments.BaseFragment;


public class GiftReceiverFragment extends BaseFragment<FragmentGiftReceiverBinding> {

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

        binding.procedGiftPay.setOnClickListener(v -> Navigation.findNavController(v)
                .navigate(R.id.action_giftReceiverFragment_to_SendEGiftActivity));

        binding.toolBar.backBtn.setOnClickListener(view -> getActivity().finish());
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_gift_receiver;
    }
}