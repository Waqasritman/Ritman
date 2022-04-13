package angoothape.wallet.settlementaeps.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import angoothape.wallet.R;
import angoothape.wallet.adapters.AEPSSettlementBeneficiaryAdapter;
import angoothape.wallet.databinding.ActivitySelectBeneficialyBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.SimpleRequest;
import angoothape.wallet.di.JSONdi.restResponse.aepssattlement.AEPSBeneficiary;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.di.XMLdi.ResponseHelper.GetBeneficiaryListResponse;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.interfaces.OnSelectBeneficiary;
import angoothape.wallet.settlementaeps.viewmodels.AEPSSettlementViewModel;
import angoothape.wallet.utils.Utils;

public class AEPSBeneficiaryListFragment extends BaseFragment<ActivitySelectBeneficialyBinding>
        implements OnSelectBeneficiary {

    private List<AEPSBeneficiary> list;
    AEPSSettlementViewModel viewModel;
    AEPSSettlementBeneficiaryAdapter adapter;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(AEPSSettlementViewModel.class);
        binding.backBtn.setVisibility(View.GONE);
        binding.searchViw.setVisibility(View.GONE);
        binding.dailyLimit.setVisibility(View.GONE);
        setAccountsRecyclerView();
        getBeneficiaryList();


        binding.addBene.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("customer_no", "");
            bundle.putBoolean("isDMTLive", false);
            bundle.putBoolean("isAEPSBene", true);
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.sendMoneyViaBankFirstActivity
                            , bundle);
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_beneficialy;
    }


    /**
     * setup the recycler view when screen load after that just notify the adapter
     */
    private void setAccountsRecyclerView() {
        list = new ArrayList<>();
        adapter = new
                AEPSSettlementBeneficiaryAdapter(list, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        binding.beneficiaryRecyclerView.setLayoutManager(mLayoutManager);
        binding.beneficiaryRecyclerView.setAdapter(adapter);
    }


    void getBeneficiaryList() {
        Utils.showCustomProgressDialog(getContext(), false);

        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim();

        SimpleRequest request = new SimpleRequest();
        String body = RestClient.makeGSONString(request);

        AERequest aeRequest = new AERequest();
        aeRequest.body = AESHelper.encrypt(body.trim(), gKey.trim());

        viewModel.getAEPSBeneficiaryList(aeRequest, KeyHelper.getKey(getSessionManager().getMerchantName()).trim(), KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim())
                .observe(getViewLifecycleOwner(), response -> {
                    Utils.hideCustomProgressDialog();
                    hideProgress();
                    if (response.status == Status.ERROR) {
                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            list.clear();
                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , gKey);
                            Log.e("getBeneficiaryList: ", body);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<AEPSBeneficiary>>() {
                                }.getType();
                                List<AEPSBeneficiary> data = gson.fromJson(bodyy, type);
                                list.addAll(data);
                                hideProgress();
                                adapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            onMessage(response.resource.description);
                        }
                    }
                });
    }


    @Override
    public void showProgress() {
        super.showProgress();
        binding.mainView.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);
    }


    @Override
    public void hideProgress() {
        super.hideProgress();
        binding.mainView.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onSelectBeneficiary(GetBeneficiaryListResponse response) {

    }

    @Override
    public void onSelectAEPSBeneficiary(AEPSBeneficiary response) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("bene", response);
        Navigation.findNavController(binding.getRoot())
                .navigate(R.id.AEPSSettlementTransferRate
                        , bundle);
    }

    @Override
    public void onChangeTheStatusOfBeneficiary(GetBeneficiaryListResponse response, int pushToActive, int position) {

    }
}
