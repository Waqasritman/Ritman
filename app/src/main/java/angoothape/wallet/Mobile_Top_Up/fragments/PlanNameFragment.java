package angoothape.wallet.Mobile_Top_Up.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.navigation.Navigation;
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
import angoothape.wallet.adapters.PlanNameAdapter;
import angoothape.wallet.databinding.PlanNameFragmentLayoutBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.PlanCategoriesRequest;
import angoothape.wallet.di.JSONdi.restResponse.PlanCategoriesResponse;
import angoothape.wallet.di.JSONdi.restResponse.PrepaidOperatorResponse;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.interfaces.PlanName;
import angoothape.wallet.utils.IsNetworkConnection;
import angoothape.wallet.utils.Utils;

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
            String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                    .getKey(getSessionManager().getMerchantName())).trim();
            PlanCategoriesRequest request = new PlanCategoriesRequest();
            request.billerid = billerid;
            request.circle_name = circle_name;
            String body = RestClient.makeGSONString(request);
            Log.e( "getPlanName: ", body);
            AERequest aeRequest = new AERequest();
            aeRequest.body = AESHelper.encrypt(body.trim(), gKey.trim());
            viewModel.getPlanName(aeRequest, KeyHelper.getKey(getSessionManager().getMerchantName()).trim(),
                    KeyHelper.getSKey(KeyHelper
                            .getKey(getSessionManager().getMerchantName())))
                    .observe(getViewLifecycleOwner(), response -> {
                        Utils.hideCustomProgressDialog();
                        if (response.status == Status.ERROR) {
                            onError(getString(response.messageResourceId));
                        } else {
                            assert response.resource != null;
                            if (response.resource.responseCode.equals(101)) {
                                Utils.hideCustomProgressDialog();
                                String bodyy = AESHelper.decrypt(response.resource.data.body
                                        , gKey);
                                Log.e("getPlanName: ",bodyy );
                                try {
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<List<PlanCategoriesResponse>>() {
                                    }.getType();
                                    List<PlanCategoriesResponse> data = gson.fromJson(bodyy, type);
                                    if (data.isEmpty()) {
                                        binding.noplans.setVisibility(View.VISIBLE);
                                    } else {
                                        onPlanName(data);
                                        binding.noplans.setVisibility(View.GONE);
                                    }
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
        Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_planNameFragment_to_rechargePlansFragment, bundle);
    }

}
