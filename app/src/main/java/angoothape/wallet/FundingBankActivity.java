package angoothape.wallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import angoothape.wallet.adapters.BankFundingDetailsAdapter;
import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.ActivityFundingBankBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.SimpleRequest;
import angoothape.wallet.di.JSONdi.restResponse.FundingBankingDetailsResponse;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.menumodules.viewmodels.CustomerServiceViewModel;
import angoothape.wallet.utils.Utils;

public class FundingBankActivity extends RitmanBaseActivity<ActivityFundingBankBinding> {

    List<FundingBankingDetailsResponse> detailsResponseList;
    CustomerServiceViewModel viewModel;
    BankFundingDetailsAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_funding_bank;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(CustomerServiceViewModel.class);

        binding.toolBar.backBtn.setOnClickListener(v -> {
            onBackPressed();
        });


        detailsResponseList = new ArrayList<>();
        setAccountsRecyclerView();
        getFundingBanksDetails();
    }

    private void getFundingBanksDetails() {
        Utils.showCustomProgressDialog(this, false);


        String gKey = KeyHelper.getKey(sessionManager.getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(sessionManager.getMerchantName())).trim();

        SimpleRequest request = new SimpleRequest();
        String body = RestClient.makeGSONString(request);

        AERequest aeRequest = new AERequest();
        aeRequest.body = AESHelper.encrypt(body.trim(), gKey.trim());


        viewModel.getFundingBankDetails(aeRequest, KeyHelper.getKey(sessionManager.getMerchantName()).trim(), KeyHelper.getSKey(KeyHelper
                .getKey(sessionManager.getMerchantName())).trim())
                .observe(this, response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            detailsResponseList.clear();
                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , gKey);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<FundingBankingDetailsResponse>>() {
                                }.getType();
                                List<FundingBankingDetailsResponse> data = gson.fromJson(bodyy, type);
                                detailsResponseList.addAll(data);
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

    private void setAccountsRecyclerView() {
        adapter = new
                BankFundingDetailsAdapter(detailsResponseList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.recyclerViewLedger.setLayoutManager(mLayoutManager);
        binding.recyclerViewLedger.setAdapter(adapter);
    }

}