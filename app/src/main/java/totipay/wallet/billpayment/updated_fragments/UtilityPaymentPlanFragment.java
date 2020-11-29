package totipay.wallet.billpayment.updated_fragments;

import android.os.Bundle;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import totipay.wallet.R;
import totipay.wallet.adapters.WRBillerPlansAdapter;
import totipay.wallet.billpayment.BillPaymentMainActivity;
import totipay.wallet.databinding.FragmentWRBillerNamesBinding;
import totipay.wallet.di.RequestHelper.WRBillerPlansRequest;
import totipay.wallet.di.RequestHelper.WRPayBillRequest;
import totipay.wallet.di.ResponseHelper.WRBillerPlanResponse;
import totipay.wallet.di.apicaller.WRBillerPlanTask;
import totipay.wallet.fragments.BaseFragment;
import totipay.wallet.interfaces.OnBillerPlans;
import totipay.wallet.utils.Constants;
import totipay.wallet.utils.IsNetworkConnection;

public class UtilityPaymentPlanFragment extends BaseFragment<FragmentWRBillerNamesBinding>
        implements OnBillerPlans {

    WRBillerPlansRequest billerNamesRequest;
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

        if (IsNetworkConnection.checkNetworkConnection(getActivity())) {
            billerNamesRequest = new WRBillerPlansRequest();
            billerNamesRequest.billerID = ((BillPaymentMainActivity) getBaseActivity()).plansRequest.billerID;
            billerNamesRequest.billerCategoryId = ((BillPaymentMainActivity) getBaseActivity()).plansRequest.billerCategoryId;
            billerNamesRequest.billerTypeID = ((BillPaymentMainActivity) getBaseActivity()).plansRequest.billerTypeID;
            billerNamesRequest.countryCode = ((BillPaymentMainActivity) getBaseActivity())
                    .plansRequest.countryCode;
            billerNamesRequest.languageId = getSessionManager().getlanguageselection();
            WRBillerPlanTask task = new WRBillerPlanTask(getContext(), this);
            task.execute(billerNamesRequest);
        } else {
            onMessage(getString(R.string.no_internet));
        }
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
    public void onBillerPlans(List<WRBillerPlanResponse> plansList) {
        billerNamesList.clear();
        billerNamesList.addAll(plansList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBillerPlanSelect(WRBillerPlanResponse billerPlan) {
        ((BillPaymentMainActivity) getBaseActivity()).payBillRequest.skuID
                = billerPlan.billerSKUId;
        ((BillPaymentMainActivity) getBaseActivity())
                .payBillRequest.billerID = billerPlan.billerId;
        ((BillPaymentMainActivity) getBaseActivity()).payBillRequest.countryCode
                = billerPlan.countryCode;
        Navigation.findNavController(getView())
                .navigate(R.id.action_utilityPaymentPlanFragment_to_utilityPaymentAccountNoFragment);
    }
}