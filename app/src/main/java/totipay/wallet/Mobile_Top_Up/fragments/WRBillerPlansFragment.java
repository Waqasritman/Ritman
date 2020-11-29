package totipay.wallet.Mobile_Top_Up.fragments;

import android.os.Bundle;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import totipay.wallet.Mobile_Top_Up.MobileTopUpMainActivity;
import totipay.wallet.Mobile_Top_Up.helpers.MobileTopUpType;
import totipay.wallet.R;
import totipay.wallet.adapters.WRBillerMobilePlansAdapter;
import totipay.wallet.adapters.WRBillerPlansAdapter;
import totipay.wallet.adapters.WrBillerPrepaidPlansAdapter;
import totipay.wallet.databinding.FragmentWRBillerNamesBinding;
import totipay.wallet.di.RequestHelper.WRBillerPlansRequest;
import totipay.wallet.di.RequestHelper.WRPayBillRequest;
import totipay.wallet.di.RequestHelper.WRPrepaidRechargeRequest;
import totipay.wallet.di.ResponseHelper.GetPrepaidPlansResponse;
import totipay.wallet.di.ResponseHelper.WRBillerPlanResponse;
import totipay.wallet.di.apicaller.GetPrepaidPlanTask;
import totipay.wallet.di.apicaller.WRBillerPlanTask;
import totipay.wallet.fragments.BaseFragment;
import totipay.wallet.interfaces.OnBillerPlans;
import totipay.wallet.interfaces.OnGetPrepaidPlans;
import totipay.wallet.utils.Constants;
import totipay.wallet.utils.IsNetworkConnection;

public class WRBillerPlansFragment extends BaseFragment<FragmentWRBillerNamesBinding>
        implements OnBillerPlans, OnGetPrepaidPlans {

    WRBillerPlansRequest billerNamesRequest;
    List<WRBillerPlanResponse> billerNamesList;
    WRBillerMobilePlansAdapter adapter;

    List<GetPrepaidPlansResponse> prepaidPlansList;
    WrBillerPrepaidPlansAdapter prepaidPlansAdapter;


    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {


        if (((MobileTopUpMainActivity) getBaseActivity()).topUpType
                == MobileTopUpType.PRE_PAID) {
            prepaidPlansList = new ArrayList<>();
            setupPrePaidRecyclerView();


            if (IsNetworkConnection.checkNetworkConnection(getContext())) {

                GetPrepaidPlanTask prepaidPlanTask = new GetPrepaidPlanTask(getContext(), this);
                prepaidPlanTask.execute(((MobileTopUpMainActivity) getBaseActivity()).prepaidPlansRequest);

            } else {
                onMessage(getString(R.string.no_internet));
            }
        } else if (((MobileTopUpMainActivity) getBaseActivity()).topUpType
                == MobileTopUpType.POST_PAID) {
            billerNamesList = new ArrayList<>();
            setupPostPaidRecyclerView();
            billerNamesRequest = new WRBillerPlansRequest();

            if (IsNetworkConnection.checkNetworkConnection(getActivity())) {
                billerNamesRequest = ((MobileTopUpMainActivity) getBaseActivity()).plansRequest;
                billerNamesRequest.languageId = getSessionManager().getlanguageselection();
                WRBillerPlanTask task = new WRBillerPlanTask(getContext(), this);
                task.execute(billerNamesRequest);
            } else {
                onMessage(getString(R.string.no_internet));
            }
        }


    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_w_r_biller_names;
    }


    /**
     * Method will set the recycler view
     */
    private void setupPostPaidRecyclerView() {

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new
                WRBillerMobilePlansAdapter(billerNamesList, this);
        binding.plansRecycler.setLayoutManager(mLayoutManager);
        binding.plansRecycler.setHasFixedSize(true);
        binding.plansRecycler.setAdapter(adapter);
    }


    /**
     * Method will set the recycler view
     */
    private void setupPrePaidRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        prepaidPlansAdapter = new
                WrBillerPrepaidPlansAdapter(prepaidPlansList, this);
        binding.plansRecycler.setLayoutManager(mLayoutManager);
        binding.plansRecycler.setHasFixedSize(true);
        binding.plansRecycler.setAdapter(prepaidPlansAdapter);
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


    /**
     * filling the pay bill request
     * if mobile top up is postpaid
     * will get the bill details
     *
     * @param billerPlan
     */
    @Override
    public void onBillerPlanSelect(WRBillerPlanResponse billerPlan) {

        ((MobileTopUpMainActivity) getBaseActivity()).payBillRequest.skuID = billerPlan.billerSKUId;
        ((MobileTopUpMainActivity) getBaseActivity()).payBillRequest.billerID = billerPlan.billerId;
        ((MobileTopUpMainActivity) getBaseActivity()).payBillRequest.customerNo = getSessionManager().getCustomerNo();
        ((MobileTopUpMainActivity) getBaseActivity()).payBillRequest.payOutAmount = billerPlan.payableAmount.toString();
        ((MobileTopUpMainActivity) getBaseActivity()).payBillRequest.languageId = getSessionManager().getlanguageselection();


        /**
         * if type will be pre paid then package ids will go
         * other wise will fetch the bill details
         */
        if (billerPlan.billerTypeId == MobileTopUpType.PRE_PAID) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("biller_plan", ((MobileTopUpMainActivity) getBaseActivity()).payBillRequest);
            Navigation.findNavController(getView())
                    .navigate(R.id.action_WRBillerNamesFragment_to_WRBillerPaymentFragment, bundle);
        } else if (billerPlan.billerTypeId == MobileTopUpType.POST_PAID) {
            Navigation.findNavController(getView())
                    .navigate(R.id.action_WRBillerNamesFragment_to_MobileTopupBillDetailsFragment);
        } else {
            onMessage(getString(R.string.some_thing_wrong));
        }


    }

    @Override
    public void onGetPrepaidPlans(List<GetPrepaidPlansResponse> plansList) {
        prepaidPlansList.clear();
        prepaidPlansList.addAll(plansList);
        prepaidPlansAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSelectPlan(GetPrepaidPlansResponse plan) {

        ((MobileTopUpMainActivity)getBaseActivity()).prepaidRechargeRequest.planId = plan.planId;
        ((MobileTopUpMainActivity)getBaseActivity()).prepaidRechargeRequest.customerNo = getSessionManager().getCustomerNo();
        ((MobileTopUpMainActivity)getBaseActivity()).prepaidRechargeRequest.payOutAmount = plan.rechargeAmount;

        Navigation.findNavController(getView())
                .navigate(R.id.action_WRBillerNamesFragment_to_WRBillerPaymentFragment);
    }
}