package angoothape.wallet.beneficiary_list_module;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import angoothape.wallet.R;
import angoothape.wallet.adapters.CustomerBeneficiaryListAdapter;
import angoothape.wallet.adapters.DeActiveBeneAdapter;
import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.beneficairyRegistration.viewmodel.RegisterBeneficiaryViewModel;
import angoothape.wallet.databinding.ActivityBeneficiaryListBinding;
import angoothape.wallet.databinding.ActivitySelectBeneficialyBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.ActiveDeActiveBeneRequest;
import angoothape.wallet.di.JSONdi.restRequest.GetBeneficiaryRequest;
import angoothape.wallet.di.JSONdi.restResponse.aepssattlement.AEPSBeneficiary;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.di.XMLdi.ResponseHelper.GetBeneficiaryListResponse;
import angoothape.wallet.interfaces.OnDecisionMade;
import angoothape.wallet.interfaces.OnSelectBeneficiary;
import angoothape.wallet.utils.Utils;

public class BeneficiaryListActivity extends RitmanBaseActivity<ActivitySelectBeneficialyBinding>
        implements OnSelectBeneficiary, OnDecisionMade {

    public List<GetBeneficiaryListResponse> list = null;
    DeActiveBeneAdapter adapter;
    RegisterBeneficiaryViewModel viewModel;
    String customerN0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_beneficialy;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(RegisterBeneficiaryViewModel.class);
        list = new ArrayList<>();
        setAccountsRecyclerView();
        binding.addBene.setVisibility(View.GONE);

        binding.searchBtn.setOnClickListener(v -> {
            if (TextUtils.isEmpty(binding.seachBene.getText().toString())) {
                onMessage(getString(R.string.enter_beneficairy_number));
            } else {
                getBeneficiary();
            }
        });
        binding.seachBene.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.mainView.setVisibility(View.GONE);
                binding.searchBtn.setVisibility(View.VISIBLE);
                binding.searchViw.setVisibility(View.VISIBLE);
            }
        });
        binding.backBtn.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.crossBtn.setVisibility(View.GONE);

        binding.titleTxt.setText("Activate Beneficiary");
    }

    private void getBeneficiary() {
        Utils.showCustomProgressDialog(this, false);
        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim();

        GetBeneficiaryRequest request = new GetBeneficiaryRequest();
        request.CustomerNo = binding.seachBene.getText().toString();
        String body = RestClient.makeGSONString(request);

        AERequest aeRequest = new AERequest();
        aeRequest.body = AESHelper.encrypt(body.trim(), gKey.trim());
        Log.e("getBeneficiary: ", body);
        viewModel.getDeActiveBenficiary(aeRequest, KeyHelper.getKey(getSessionManager().getMerchantName()).trim(),
                KeyHelper.getSKey(KeyHelper
                        .getKey(getSessionManager().getMerchantName())).trim())
                .observe(this, response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            binding.searchBtn.setVisibility(View.GONE);
                            binding.mainView.setVisibility(View.VISIBLE);
                            onMessage(response.resource.description);
                            list.clear();

                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , gKey);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<GetBeneficiaryListResponse>>() {
                                }.getType();
                                List<GetBeneficiaryListResponse> data = gson.fromJson(bodyy, type);
                                list.addAll(data);
                                customerN0 = data.get(0).customerNo;
                                adapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            if (list.size() >= 25) {
                                binding.addBene.setVisibility(View.GONE);
                            }

                        } else if (response.resource.responseCode.equals(721)) {
                            onMessage("Customer is not registered");
                        } else if (response.resource.responseCode.equals(206)) {

                            binding.searchBtn.setVisibility(View.GONE);
                            binding.mainView.setVisibility(View.VISIBLE);

                            String[] a = response.resource.description.split("\\|");

                            customerN0 = a[2];
                            binding.seachBene.requestFocus();
                        } else {
                            Utils.hideCustomProgressDialog();
                            if (response.resource.data != null) {
                                String bodyy = AESHelper.decrypt(response.resource.data.body
                                        , gKey);
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

    /**
     * setup the recycler view when screen load after that just notify the adapter
     */
    private void setAccountsRecyclerView() {
        adapter = new
                DeActiveBeneAdapter(list, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.beneficiaryRecyclerView.setLayoutManager(mLayoutManager);
        binding.beneficiaryRecyclerView.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onProceed() {

    }

    @Override
    public void onCancel(boolean goBack) {

    }

    @Override
    public void onSelectBeneficiary(GetBeneficiaryListResponse response) {

    }

    @Override
    public void onSelectAEPSBeneficiary(AEPSBeneficiary response) {

    }

    @Override
    public void onChangeTheStatusOfBeneficiary(GetBeneficiaryListResponse response, int pushToActive, int position) {
        activeDeactive(response.beneficiaryNumber, pushToActive, position);
    }


    private void activeDeactive(String beneId, int pushToActive, int position) {
        Utils.showCustomProgressDialog(this, false);
        String gKey = KeyHelper.getKey(sessionManager.getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(sessionManager.getMerchantName())).trim();

        ActiveDeActiveBeneRequest request = new ActiveDeActiveBeneRequest();
        request.CustomerNo = customerN0;
        request.BeneficiaryNo = beneId;
        request.BeneficiaryStatus = pushToActive;
        String body = RestClient.makeGSONString(request);
        Log.e("activeDeactive: ", body);
        AERequest aeRequest = new AERequest();
        aeRequest.body = AESHelper.encrypt(body.trim(), gKey.trim());


        viewModel.activeDeActiveBeneficiary(aeRequest, KeyHelper.getKey(getSessionManager().getMerchantName()).trim(), KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim())
                .observe(this, response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            String bodyy = AESHelper.decrypt(response.resource.data.body, gKey);
                            Log.e("bodyy: ", bodyy);

                            list.remove(position);
                            adapter.notifyDataSetChanged();

                        } else {
                            onError(response.resource.description);
                        }
                    }
                });
    }

}