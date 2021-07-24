package ritman.wallet.Mobile_Top_Up.fragments;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ritman.wallet.Mobile_Top_Up.MobileTopUpMainActivity;
import ritman.wallet.Mobile_Top_Up.viewmodels.MobileTopUpViewModel;
import ritman.wallet.R;
import ritman.wallet.adapters.PlanNameAdapter;
import ritman.wallet.adapters.WRBillerOperatorAdapter;
import ritman.wallet.databinding.PlanNameFragmentLayoutBinding;
import ritman.wallet.di.JSONdi.Status;
import ritman.wallet.di.JSONdi.restRequest.PlanCategoriesRequest;
import ritman.wallet.di.JSONdi.restRequest.PrepaidOperatorRequest;
import ritman.wallet.di.JSONdi.restResponse.GetWRBillerNamesResponseC;
import ritman.wallet.di.JSONdi.restResponse.PlanCategoriesResponse;
import ritman.wallet.di.XMLdi.ResponseHelper.GetCardDetailsResponse;
import ritman.wallet.fragments.BaseFragment;
import ritman.wallet.interfaces.PlanName;
import ritman.wallet.utils.IsNetworkConnection;
import ritman.wallet.utils.Utils;

public class PlanNameFragment extends BaseFragment<PlanNameFragmentLayoutBinding> implements PlanName {
    String billerid, circle_name, MobileNumber, Field1Name, Field2Name, Field3Name, billerlogo;

    MobileTopUpViewModel viewModel;

    List<PlanCategoriesResponse> planCategoriesResponseList;
    PlanNameAdapter planNameAdapter;
    String PlanCategory;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        planCategoriesResponseList = new ArrayList<>();
        viewModel = ((MobileTopUpMainActivity) getBaseActivity()).viewModel;
        billerid = getArguments().getString("billerid");
        circle_name = getArguments().getString("circle_name");
        MobileNumber = getArguments().getString("MobileNumber");
        Field1Name = getArguments().getString("Field1Name");
        Field2Name = getArguments().getString("Field2Name");
        Field3Name = getArguments().getString("Field3Name");
        billerlogo = getArguments().getString("billerlogo");

        setupRecyclerView();
        getPlanName();
    }

    void getPlanName() {

        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            Utils.showCustomProgressDialog(getContext(), false);
            PlanCategoriesRequest request = new PlanCategoriesRequest();
            request.billerid = billerid;
            request.circle_name = circle_name;

            viewModel.getPlanName(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()
                    , response -> {
                        Utils.hideCustomProgressDialog();
                        if (response.status == Status.ERROR) {
                            onMessage(getString(response.messageResourceId));
                        } else {
                            assert response.resource != null;
                            if (response.resource.responseCode.equals(101)) {
                                Utils.hideCustomProgressDialog();
                                if (response.resource.data.isEmpty()) {
                                    binding.noplans.setVisibility(View.VISIBLE);
                                } else {
                                    onPlanName(response.resource.data);
                                    binding.noplans.setVisibility(View.GONE);
                                }


                            } else {
                                Utils.hideCustomProgressDialog();
                                onMessage(response.resource.description);

                            }
                        }
                    });


        } else {
            onMessage(getString(R.string.no_internet));
            Utils.hideCustomProgressDialog();
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.plan_name_fragment_layout;
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        planNameAdapter = new
                PlanNameAdapter(getContext(), planCategoriesResponseList, this);
        binding.recyPlanName.setLayoutManager(mLayoutManager);
        binding.recyPlanName.setHasFixedSize(true);
        binding.recyPlanName.setAdapter(planNameAdapter);

    }

    @Override
    public void onPlanName(List<PlanCategoriesResponse> responseList) {
        planCategoriesResponseList.clear();
        planCategoriesResponseList.addAll(responseList);
        planNameAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSelectPlanName(PlanCategoriesResponse PlanName) {
        PlanCategory = PlanName.getPlanCategory();
        Bundle bundle = new Bundle();
        bundle.putString("billerid", billerid);
        bundle.putString("circle_name", circle_name);
        bundle.putString("PlanCategory", PlanCategory);
        bundle.putString("MobileNumber", MobileNumber);
        bundle.putString("Field1Name", Field1Name);
        bundle.putString("Field2Name", Field2Name);
        bundle.putString("Field3Name", Field3Name);
        bundle.putString("billerlogo", billerlogo);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_planNameFragment_to_rechargePlansFragment, bundle);
    }

}
