package angoothape.wallet.Mobile_Top_Up.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import angoothape.wallet.Mobile_Top_Up.MobileTopUpMainActivity;
import angoothape.wallet.Mobile_Top_Up.viewmodels.MobileTopUpViewModel;
import angoothape.wallet.R;
import angoothape.wallet.adapters.WrBillerPrepaidPlansAdapter;
import angoothape.wallet.databinding.FragmentWRBillerNamesBinding;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.PrepaidPlanRequest;
import angoothape.wallet.di.JSONdi.restResponse.PrepaidPlanResponse;
import angoothape.wallet.di.XMLdi.RequestHelper.WRPrepaidRechargeRequest;
import angoothape.wallet.dialogs.SingleButtonMessageDialog;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.interfaces.OnBillerPlans;
import angoothape.wallet.interfaces.OnDecisionMade;
import angoothape.wallet.utils.IsNetworkConnection;
import angoothape.wallet.utils.Utils;

public class WRBillerPlansFragment extends BaseFragment<FragmentWRBillerNamesBinding>
        implements OnBillerPlans, OnDecisionMade {

    List<PrepaidPlanResponse.Plan> prepaidPlansList;
    List<PrepaidPlanResponse.Plan> filteredList;
    WrBillerPrepaidPlansAdapter prepaidPlansAdapter;


    MobileTopUpViewModel viewModel;
    String operatorCode, circleCode;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        prepaidPlansList = new ArrayList<>();
        filteredList = new ArrayList<>();

        viewModel = ((MobileTopUpMainActivity) getBaseActivity()).viewModel;
        operatorCode = viewModel.operatorCode.getValue();
        circleCode = viewModel.circleCode.getValue();
//        viewModel.operatorCode.observe(getViewLifecycleOwner(), s -> {
//            = s;
//        });
//
//        viewModel.circleCode.observe(getViewLifecycleOwner(), s -> {
//            circleCode = s;
//        });


        setupPrePaidRecyclerView();
        getPrepaidPlan();

        binding.topupLayout.setOnClickListener(v -> {
            getParsedData("TOPUP");
            binding.title.setText(getString(R.string.select_plan));
            binding.viewTopup.setVisibility(View.VISIBLE);
            binding.viewSpecial.setVisibility(View.GONE);
            binding.viewData3g.setVisibility(View.GONE);
        });

        binding.data3gLayout.setOnClickListener(v -> {
            getParsedData("DATA_3G");
            binding.title.setText(getString(R.string.select_plan_3g));
            binding.viewTopup.setVisibility(View.GONE);
            binding.viewSpecial.setVisibility(View.GONE);
            binding.viewData3g.setVisibility(View.VISIBLE);
        });

        binding.specialLayout.setOnClickListener(v -> {
            getParsedData("SPECIAL");
            binding.title.setText(getString(R.string.select_plan_special));
            binding.viewTopup.setVisibility(View.GONE);
            binding.viewSpecial.setVisibility(View.VISIBLE);
            binding.viewData3g.setVisibility(View.GONE);
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_w_r_biller_names;
    }


    /**
     * Method will set the recycler view
     */

    public void getPrepaidPlan() {
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            Utils.showCustomProgressDialog(getContext(), false);
            PrepaidPlanRequest request = new PrepaidPlanRequest();
            request.OperatorCode = Integer.parseInt(operatorCode);
            request.CircleCode = Integer.parseInt(circleCode);
            request.CountryCode = "IN";

            viewModel.getPrepaidPlan(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()
                    , response -> {
                        Utils.hideCustomProgressDialog();
                        if (response.status == Status.ERROR) {
                            onMessage(getString(response.messageResourceId));
                        } else {
                            assert response.resource != null;
                            if (response.resource.responseCode.equals(101)) {
                                // onMessage(response.resource.description);
                                if (response.resource.data.plans.TOPUP != null) {
                                    prepaidPlansList.addAll(response.resource.data.plans.TOPUP);
                                }
                                if (response.resource.data.plans.DATA_3G != null) {
                                    prepaidPlansList.addAll(response.resource.data.plans.DATA_3G);
                                }
                                if (response.resource.data.plans.SPECIAL != null) {
                                    prepaidPlansList.addAll(response.resource.data.plans.SPECIAL);
                                }
                                filteredList.addAll(prepaidPlansList);

                                getParsedData("topup");

                                prepaidPlansAdapter.notifyDataSetChanged();
                            } else {
                                onMessage(response.resource.description);
                            }
                        }
                    });


        } else {
            onMessage(getString(R.string.no_internet));
        }
    }

    void getParsedData(String parsedType) {
        filteredList.clear();
        for (PrepaidPlanResponse.Plan item :
                prepaidPlansList) {
            if (item.rechargeSubType.equalsIgnoreCase(parsedType)) {
                filteredList.add(item);
            }
        }
        prepaidPlansAdapter.notifyDataSetChanged();
    }


    private void setupPrePaidRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        prepaidPlansAdapter = new WrBillerPrepaidPlansAdapter(filteredList, this);
        binding.plansRecycler.setLayoutManager(mLayoutManager);
        binding.plansRecycler.setHasFixedSize(true);
        binding.plansRecycler.setAdapter(prepaidPlansAdapter);
    }


    /**
     * Method will set the recycler view
     */


    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }


    @Override
    public void onBillerPlanSelect(PrepaidPlanResponse.Plan plan) {
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            Utils.showCustomProgressDialog(getContext(), false);

            WRPrepaidRechargeRequest rechargeRequest = new WRPrepaidRechargeRequest();
            rechargeRequest.PayOutAmount = plan.rechargeAmount;
            rechargeRequest.PlanId = plan.planId;
            rechargeRequest.MobileNumber = viewModel.mobileNo.getValue();

            viewModel.mobileRecharge(rechargeRequest, getSessionManager().getMerchantName())
                    .observe(getViewLifecycleOwner(), response -> {
                        Utils.hideCustomProgressDialog();
                        if (response.status == Status.ERROR) {
                            onMessage(getString(response.messageResourceId));
                        } else {
                            assert response.resource != null;
                            if (response.resource.responseCode.equals(101)) {
//                                Bundle bundle = new Bundle();
//                                bundle.putString("request_id", response.resource.data);
//                                bundle.putString("transfer_amount", plan.rechargeAmount.toString());
//                                bundle.putString("operator_name", viewModel.operatorName.getValue());
//                                Navigation.findNavController(binding.getRoot())
//                                        .navigate(R.id.action_WRBillerNamesFragment_to_mobileTopupStatusFragment, bundle);
                                showSuccess(response.resource.data);

                            } else {
                                onMessage(response.resource.description);
                            }
                        }
                    });

        }
    }


    private void showSuccess(String rechargeId) {
        SingleButtonMessageDialog dialog = new
                SingleButtonMessageDialog(getString(R.string.successfully_txt)
                , "RechargeId = " + rechargeId, this,
                false);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }


    @Override
    public void onProceed() {
        getBaseActivity().finish();
    }

    @Override
    public void onCancel(boolean goBack)  {

    }
}