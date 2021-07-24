package ritman.wallet.insurance;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.navigation.Navigation;

import ritman.wallet.R;
import ritman.wallet.databinding.SelectOptionFragmentLayoutBinding;
import ritman.wallet.fragments.BaseFragment;

public class SelectOptionsFragment extends BaseFragment<SelectOptionFragmentLayoutBinding> {
    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        setZoomInAnimation(binding.linearRegistration);
        setZoomInAnimation(binding.linearUploadDocuments);

        binding.linearRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.getRoot()).navigate(R.id
                        .action_selectOptionsFragment_to_registrationFragment);
            }
        });

        binding.linearUploadDocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.getRoot()).navigate(R.id
                        .action_selectOptionsFragment_to_uploadDocumentsFragment);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.select_option_fragment_layout;
    }

    private void setZoomInAnimation(View view) {
        Animation zoomIn = AnimationUtils.loadAnimation(getContext(), R.anim.item_animation_fall_down);// animation file
        view.startAnimation(zoomIn);
    }
}
