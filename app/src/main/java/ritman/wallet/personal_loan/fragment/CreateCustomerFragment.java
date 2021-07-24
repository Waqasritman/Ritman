package ritman.wallet.personal_loan.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.navigation.Navigation;

import ritman.wallet.R;
import ritman.wallet.databinding.CreateCustomerFragmentLayoutBinding;
import ritman.wallet.fragments.BaseFragment;

public class CreateCustomerFragment extends BaseFragment<CreateCustomerFragmentLayoutBinding> {
    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        setZoomInAnimation(binding.cardCheckCustomerStatus);
        setZoomInAnimation(binding.cardCustomerRegistration);
        setZoomInAnimation(binding.linearUploadDocuments);

        binding.cardCustomerRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.getRoot()).navigate(R.id
                        .action_createCustomerFragment_to_personal_ZeroFragment);
            }
        });

        binding.cardCheckCustomerStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.getRoot()).navigate(R.id
                        .action_createCustomerFragment_to_checkCustomerStatusFragment);
            }
        });

        binding.linearUploadDocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.getRoot()).navigate(R.id
                        .action_createCustomerFragment_to_uploadDocumentFragment);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.create_customer_fragment_layout;
    }

    private void setZoomInAnimation(View view) {
        Animation zoomIn = AnimationUtils.loadAnimation(getContext(), R.anim.item_animation_fall_down);// animation file
        view.startAnimation(zoomIn);
    }
}
