package angoothape.wallet.Mobile_Top_Up.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import angoothape.wallet.Mobile_Top_Up.MobileTopUpMainActivity;
import angoothape.wallet.Mobile_Top_Up.viewmodels.MobileTopUpViewModel;
import angoothape.wallet.R;
import angoothape.wallet.adapters.WrBillerPrepaidPlansAdapter;
import angoothape.wallet.databinding.FragmentWRBillerNamesBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.PrepaidPlanRequest;
import angoothape.wallet.di.JSONdi.restResponse.GetWRBillerFieldsResponseN;
import angoothape.wallet.di.JSONdi.restResponse.PrepaidPlanResponse;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
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
            String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                    .getKey(getSessionManager().getMerchantName())).trim();
            PrepaidPlanRequest requestc = new PrepaidPlanRequest();
            requestc.OperatorCode = Integer.parseInt(operatorCode);
            requestc.CircleCode = Integer.parseInt(circleCode);
            requestc.CountryCode = "IN";
            String body = RestClient.makeGSONString(requestc);
            // PrepaidPlanResponse
            AERequest request = new AERequest();
            request.body = AESHelper.encrypt(body.trim(), gKey.trim());
            viewModel.getPrepaidPlan(request, KeyHelper.getKey(getSessionManager().getMerchantName()).trim(),
                    KeyHelper.getSKey(KeyHelper
                            .getKey(getSessionManager().getMerchantName())))
                    .observe(getViewLifecycleOwner(), response -> {
                        Utils.hideCustomProgressDialog();
                        if (response.status == Status.ERROR) {
                            onError(getString(response.messageResourceId));
                        } else {
                            assert response.resource != null;
                            if (response.resource.responseCode.equals(101)) {
                                String bodyy = AESHelper.decrypt(response.resource.data.body
                                        , gKey);
                                try {
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<PrepaidPlanResponse>() {
                                    }.getType();
                                    PrepaidPlanResponse data = gson.fromJson(bodyy, type);

                                    if (data.plans.TOPUP != null) {
                                        prepaidPlansList.addAll(data.plans.TOPUP);
                                    }
                                    if (data.plans.DATA_3G != null) {
                                        prepaidPlansList.addAll(data.plans.DATA_3G);
                                    }
                                    if (data.plans.SPECIAL != null) {
                                        prepaidPlansList.addAll(data.plans.SPECIAL);
                                    }
                                    filteredList.addAll(prepaidPlansList);

                                    getParsedData("topup");

                                    prepaidPlansAdapter.notifyDataSetChanged();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } else {
                                Utils.hideCustomProgressDialog();
                                if (response.resource.data != null) {
                                    String bodyy = AESHelper.decrypt(response.resource.data.body
                                            , gKey);
                                    Log.e("getBillDetails: ", bodyy);
                                    if (!body.isEmpty()) {
                                        onError(bodyy);
                                    } else {
                                        onError(response.resource.description);
                                    }
                                } else {
                                    onError(response.resource.description);
                                }
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
                            onError(getString(response.messageResourceId));
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
                                onError(response.resource.description);
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
    public void onCancel(boolean goBack) {

    }
}