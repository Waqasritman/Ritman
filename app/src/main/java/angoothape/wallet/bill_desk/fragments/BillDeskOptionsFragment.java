package angoothape.wallet.bill_desk.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import angoothape.wallet.R;
import angoothape.wallet.databinding.FragmentBillDeskOptionsBinding;
import angoothape.wallet.fragments.BaseFragment;

public class BillDeskOptionsFragment extends BaseFragment<FragmentBillDeskOptionsBinding> {


    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {

        binding.staus.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.checkComplaintStatusFragment);
        });

        binding.history.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.complaintHistoryFragment);
        });

        binding.queryTranscation.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.quickTransactionFragment);
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_bill_desk_options;
    }

}