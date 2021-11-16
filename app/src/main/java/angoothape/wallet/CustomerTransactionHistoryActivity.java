package angoothape.wallet;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import angoothape.wallet.TransactionHistory.TransactionHistoryViewModel;
import angoothape.wallet.adapters.CustomerTransactionHistoryAdapter;
import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.ActivityCustomerTransactionHistoryBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.CustomerTransactionHistoryRequest;
import angoothape.wallet.di.JSONdi.restResponse.CustomerTransactionHistory;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.utils.Utils;


public class CustomerTransactionHistoryActivity extends
        RitmanBaseActivity<ActivityCustomerTransactionHistoryBinding> {

    TransactionHistoryViewModel viewModel;
    List<CustomerTransactionHistory> historyList;
    CustomerTransactionHistoryAdapter adapter;


    @Override
    public int getLayoutId() {
        return R.layout.activity_customer_transaction_history;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(TransactionHistoryViewModel.class);
        historyList = new ArrayList<>();

        binding.backBtn.setOnClickListener(v->{
            onBackPressed();
        });
        binding.searchBtn.setOnClickListener(v -> {
            if (TextUtils.isEmpty(binding.seachBene.getText().toString())) {
                onMessage(getString(R.string.enter_beneficairy_number));
            } else {
                getBeneficiaryHistory();
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
    }


    private void getBeneficiaryHistory() {
        Utils.showCustomProgressDialog(this, false);
        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim();
        CustomerTransactionHistoryRequest request = new CustomerTransactionHistoryRequest();
        request.Customer_MobileNo = binding.seachBene.getText().toString();
        String body = RestClient.makeGSONString(request);

        AERequest aeRequest = new AERequest();
        aeRequest.body = AESHelper.encrypt(body.trim(), gKey.trim());

        viewModel.getCustomerTransactionHistory(aeRequest, KeyHelper.getKey(getSessionManager().getMerchantName()).trim(), KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim())
                .observe(this, response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            binding.searchBtn.setVisibility(View.GONE);
                            binding.mainView.setVisibility(View.VISIBLE);
                            onMessage(response.resource.description);
                            historyList = new ArrayList<>();
                            historyList.clear();


                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , gKey);

                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<CustomerTransactionHistory>>() {
                                }.getType();
                                List<CustomerTransactionHistory> data = gson.fromJson(bodyy, type);

                                historyList.clear();
                                historyList.addAll(data);
                                setAccountsRecyclerView();
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
                CustomerTransactionHistoryAdapter(this, historyList);
        LinearLayoutManager accountLayoutManager = new
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.beneficiaryRecyclerView.setLayoutManager(accountLayoutManager);
        binding.beneficiaryRecyclerView.setAdapter(adapter);
    }
}