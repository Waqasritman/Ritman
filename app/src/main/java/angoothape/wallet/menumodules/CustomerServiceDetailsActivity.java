package angoothape.wallet.menumodules;

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

import angoothape.wallet.R;
import angoothape.wallet.adapters.CustomerServiceAdapter;
import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.ActivityCustomerServiceDetailsBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.SimpleRequest;
import angoothape.wallet.di.JSONdi.restResponse.CustomerServiceResponse;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.menumodules.viewmodels.CustomerServiceViewModel;
import angoothape.wallet.utils.Utils;

public class CustomerServiceDetailsActivity extends RitmanBaseActivity<ActivityCustomerServiceDetailsBinding> {

    List<CustomerServiceResponse> customerServiceResponseList;
    CustomerServiceViewModel viewModel;
    CustomerServiceAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_customer_service_details;
    }


    @Override
    protected void initUi(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(CustomerServiceViewModel.class);
        binding.toolBarFinal.titleTxt.setText("Customer Service");
        binding.toolBarFinal.crossBtn.setOnClickListener(v -> {
            onClose();
        });
        binding.toolBarFinal.backBtn.setOnClickListener(v -> {
            onBackPressed();
        });


        customerServiceResponseList = new ArrayList<>();
        setAccountsRecyclerView();
        getCustomers();
    }


    private void setAccountsRecyclerView() {

        adapter = new
                CustomerServiceAdapter(customerServiceResponseList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.customerRecycler.setLayoutManager(mLayoutManager);
        binding.customerRecycler.setAdapter(adapter);
    }


    void getCustomers() {
        Utils.showCustomProgressDialog(this, false);


        String gKey = KeyHelper.getKey(sessionManager.getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(sessionManager.getMerchantName())).trim();

        SimpleRequest request = new SimpleRequest();
        String body = RestClient.makeGSONString(request);

        AERequest aeRequest = new AERequest();
        aeRequest.body = AESHelper.encrypt(body.trim(), gKey.trim());


        viewModel.getCustomerDetails(aeRequest, KeyHelper.getKey(sessionManager.getMerchantName()).trim(), KeyHelper.getSKey(KeyHelper
                .getKey(sessionManager.getMerchantName())).trim())
                .observe(this, response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            customerServiceResponseList.clear();
                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , gKey);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<CustomerServiceResponse>>() {
                                }.getType();
                                List<CustomerServiceResponse> data = gson.fromJson(bodyy, type);
                                customerServiceResponseList.addAll(data);
                                adapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

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
}