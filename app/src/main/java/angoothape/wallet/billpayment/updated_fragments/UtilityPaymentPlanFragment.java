package angoothape.wallet.billpayment.updated_fragments;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import angoothape.wallet.R;
import angoothape.wallet.adapters.WRBillerPlansAdapter;
import angoothape.wallet.billpayment.BillPaymentMainActivity;
import angoothape.wallet.databinding.FragmentWRBillerNamesBinding;
import angoothape.wallet.di.JSONdi.restResponse.PrepaidPlanResponse;
import angoothape.wallet.di.XMLdi.RequestHelper.WRBillerPlansRequest;
import angoothape.wallet.di.XMLdi.ResponseHelper.WRBillerPlanResponse;
import angoothape.wallet.di.XMLdi.apicaller.WRBillerPlanTask;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.interfaces.OnBillerPlans;
import angoothape.wallet.utils.IsNetworkConnection;

public class UtilityPaymentPlanFragment extends BaseFragment<FragmentWRBillerNamesBinding>
        implements OnBillerPlans {

    List<WRBillerPlanResponse> billerNamesList;
    WRBillerPlansAdapter adapter;


    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        billerNamesList = new ArrayList<>();
        setupRecyclerView();
        binding.title.setText(getString(R.string.select_service_provider));

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_w_r_biller_names;
    }


    /**
     * Method will set the recycler view
     */
    private void setupRecyclerView() {

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new
                WRBillerPlansAdapter(billerNamesList, this);
        binding.plansRecycler.setLayoutManager(mLayoutManager);
        binding.plansRecycler.setHasFixedSize(true);
        binding.plansRecycler.setAdapter(adapter);
    }


    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }


    @Override
    public void onBillerPlanSelect(PrepaidPlanResponse.Plan plan) {

    }
}