package angoothape.wallet.personal_loan.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.navigation.Navigation;

import angoothape.wallet.R;
import angoothape.wallet.databinding.CreateCustomerFragmentLayoutBinding;
import angoothape.wallet.fragments.BaseFragment;

public class CreateCustomerFragment extends BaseFragment<CreateCustomerFragmentLayoutBinding> {
    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        setZoomInAnimation(binding.cardCheckCustomerStatus);
        setZoomInAnimation(binding.cardCustomerRegistration);
        setZoomInAnimation(binding.linearUploadDocuments);

        binding.cardCustomerRegistration.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).navigate(R.id
                .action_createCustomerFragment_to_personal_ZeroFragment));

        binding.cardCheckCustomerStatus.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).navigate(R.id
                .action_createCustomerFragment_to_checkCustomerStatusFragment));

        binding.linearUploadDocuments.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).navigate(R.id
                .action_createCustomerFragment_to_uploadDocumentFragment));


        binding.create.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).navigate(R.id
                .action_createCustomerFragment_to_personal_ZeroFragment));

        binding.check.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).navigate(R.id
                .action_createCustomerFragment_to_checkCustomerStatusFragment));

        binding.upload.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).navigate(R.id
                .action_createCustomerFragment_to_uploadDocumentFragment));
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
