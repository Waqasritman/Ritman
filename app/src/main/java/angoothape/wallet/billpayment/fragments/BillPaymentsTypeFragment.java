package angoothape.wallet.billpayment.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import angoothape.wallet.MoneyTransferModuleV.viewmodels.BankTransferViewModel;
import angoothape.wallet.R;
import angoothape.wallet.adapters.WRBillerTypeAdapter;
import angoothape.wallet.billpayment.BillPaymentMainActivity;
import angoothape.wallet.databinding.ActivityBillPaymentsBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.GetWRBillerTypeRequestNew;
import angoothape.wallet.di.JSONdi.restResponse.GetWRBillerTypeResponse;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.interfaces.OnWRBillerType;
import angoothape.wallet.utils.Utils;

public class BillPaymentsTypeFragment extends BaseFragment<ActivityBillPaymentsBinding>
        implements OnWRBillerType {


    List<GetWRBillerTypeResponse> wrBillerTypeResponseList;
    WRBillerTypeAdapter adapter;
    BankTransferViewModel viewModel;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(BankTransferViewModel.class);

        wrBillerTypeResponseList = new ArrayList<>();
        setSearchView();
        setupRecyclerView();
        binding.title.setText(getString(R.string.select_recharge_type));

        getBillPaymentType();




      /*  if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            GetWRBillerTypeRequest request = new GetWRBillerTypeRequest();
            request.languageId = getSessionManager().getlanguageselection();

            WRBillerTypeTask task = new WRBillerTypeTask(getContext(), this);
            task.execute(request);
        } else {
            onMessage(getString(R.string.no_internet));
        }*/
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_bill_payments;
    }

    /**
     * method will init the search box
     */
    private void setSearchView() {
        binding.searchView.setMaxWidth(Integer.MAX_VALUE);
        binding.searchView.requestFocus();
        binding.searchView.setFocusable(true);
        binding.searchView.setHint(getString(R.string.search));


        binding.searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) {
                    adapter.getFilter().filter(s);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    /**
     * Method will set the recycler view
     */
    private void setupRecyclerView() {

        Collections.sort(wrBillerTypeResponseList, (o1, o2) ->
                o1.getName().compareToIgnoreCase(o2.getName()));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new
                WRBillerTypeAdapter(getContext(), wrBillerTypeResponseList, this);
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBillerTypeList(List<GetWRBillerTypeResponse> billerTypeList) {
        wrBillerTypeResponseList.clear();
        wrBillerTypeResponseList.addAll(billerTypeList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBillerTypeSelect(GetWRBillerTypeResponse billerType) {
        ((BillPaymentMainActivity) getBaseActivity())
                .plansRequest.billerTypeID = String.valueOf(billerType.getID());
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }


    private void getBillPaymentType() {

        GetWRBillerTypeRequestNew requestc = new GetWRBillerTypeRequestNew();
        requestc.CountryCode = "IN";
        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim();
        String body = RestClient.makeGSONString(requestc);

        AERequest request = new AERequest();
        request.body = AESHelper.encrypt(body.trim(), gKey.trim());
        viewModel.GetWRBillerType(request, KeyHelper.getKey(getSessionManager().getMerchantName()).trim(),
                KeyHelper.getSKey(KeyHelper
                        .getKey(getSessionManager().getMerchantName())))
                .observe(getViewLifecycleOwner(), response -> {

                    if (response.status == Status.ERROR) {
                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {

                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , gKey);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<GetWRBillerTypeResponse>>() {
                                }.getType();
                                List<GetWRBillerTypeResponse> data = gson.fromJson(bodyy, type);
                                onBillerTypeList(data);

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