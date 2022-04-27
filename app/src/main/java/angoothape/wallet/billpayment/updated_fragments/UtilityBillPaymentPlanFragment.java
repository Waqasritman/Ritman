package angoothape.wallet.billpayment.updated_fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.fragment.app.FragmentTransaction;
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
import angoothape.wallet.adapters.WRBillerOperatorAdapter;
import angoothape.wallet.billpayment.BillPaymentMainActivity;
import angoothape.wallet.billpayment.viewmodel.BillPaymentViewModel;
import angoothape.wallet.databinding.UtilityPaymentPlansViewBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.GetWRBillerCategoryRequestC;
import angoothape.wallet.di.JSONdi.restRequest.GetWRBillerNamesRequestC;
import angoothape.wallet.di.JSONdi.restRequest.GetWRBillerTypeRequestNew;
import angoothape.wallet.di.JSONdi.restRequest.GetWRCountryListRequestC;
import angoothape.wallet.di.JSONdi.restResponse.GetWRBillerCategoryResponseC;
import angoothape.wallet.di.JSONdi.restResponse.GetWRBillerNamesResponseC;
import angoothape.wallet.di.JSONdi.restResponse.GetWRBillerTypeResponse;
import angoothape.wallet.di.JSONdi.restResponse.GetWRCountryListResponseC;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.dialogs.WRBillerCategoryDialog;
import angoothape.wallet.dialogs.WRBillerOperatorsDialog;
import angoothape.wallet.dialogs.WRBillerTypeDialog;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.interfaces.OnWRBillerCategoryResponse;
import angoothape.wallet.interfaces.OnWRBillerNames;
import angoothape.wallet.interfaces.OnWRBillerType;
import angoothape.wallet.interfaces.OnWRCountryList;
import angoothape.wallet.utils.IsNetworkConnection;
import angoothape.wallet.utils.Utils;


public class UtilityBillPaymentPlanFragment extends BaseFragment<UtilityPaymentPlansViewBinding>
        implements OnWRBillerNames {

    List<GetWRBillerNamesResponseC> wrBillerOperatorsList;
    WRBillerOperatorAdapter adapter;
    public String billerCategoryId = "";
    BillPaymentViewModel viewModel;


    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = ((BillPaymentMainActivity) getBaseActivity()).viewModel;

        wrBillerOperatorsList = new ArrayList<>();

        assert getArguments() != null;
        {
            billerCategoryId = getArguments().getString("billerCategoryId");
            ((BillPaymentMainActivity) getBaseActivity()).binding.toolBar.titleTxt.setText(
                    getArguments().getString("billerName")
            );
        }

        setSearchView();
        setupRecyclerView();
        if (wrBillerOperatorsList.isEmpty()) {
            getBillerName();
        }


        binding.searchView.setVisibility(View.VISIBLE);

    }


    public void getBillerName() {
        Utils.showCustomProgressDialog(getContext(), false);
        GetWRBillerNamesRequestC requestc = new GetWRBillerNamesRequestC();
        requestc.CountryCode = "IN";
        requestc.BillerTypeId = 1;//Integer.valueOf(billerTypeId);
        requestc.BillerCategoryId = Integer.valueOf(billerCategoryId);//62;
        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim();
        String body = RestClient.makeGSONString(requestc);

        AERequest request = new AERequest();
        request.body = AESHelper.encrypt(body.trim(), gKey.trim());
        viewModel.GetWRBillerNames(request, KeyHelper.getKey(getSessionManager().getMerchantName()).trim(),
                KeyHelper.getSKey(KeyHelper
                        .getKey(getSessionManager().getMerchantName())))
                .observe(getViewLifecycleOwner(), response -> {
                    if (response.status == Status.ERROR) {
                        onError(getString(response.messageResourceId));
                        Utils.hideCustomProgressDialog();
                    } else {
                        Utils.hideCustomProgressDialog();
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , gKey);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<GetWRBillerNamesResponseC>>() {
                                }.getType();
                                List<GetWRBillerNamesResponseC> data = gson.fromJson(bodyy, type);
                                onBillerNamesList(data);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else if (response.resource.responseCode.equals(305)) {
                            onMessage(response.resource.description + "\nTry again later");
                            Navigation.findNavController(binding.getRoot()).navigateUp();
                        } else {
                            Utils.hideCustomProgressDialog();
                            if (response.resource.data != null) {
                                String bodyy = AESHelper.decrypt(response.resource.data.body
                                        , gKey);
                                try {
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<List<GetWRBillerNamesResponseC>>() {
                                    }.getType();
                                    List<GetWRBillerNamesResponseC> data = gson.fromJson(bodyy, type);
                                    onBillerNamesList(data);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    if (!body.isEmpty()) {
                                        onError(bodyy);
                                    } else {
                                        onError(response.resource.description);
                                    }
                                }
                            } else {
                                onError(response.resource.description);
                            }
                        }
                    }
                });
    }


    @Override
    public int getLayoutId() {
        return R.layout.utility_payment_plans_view;
    }

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

    private void setupRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new
                WRBillerOperatorAdapter(getContext(), wrBillerOperatorsList, this);
        binding.recyBillername.setLayoutManager(mLayoutManager);
        binding.recyBillername.setHasFixedSize(true);
        binding.recyBillername.setAdapter(adapter);
    }


    //this is angootha
    @Override
    public void onBillerNamesList(List<GetWRBillerNamesResponseC> billerNameList) {
        wrBillerOperatorsList.clear();
        wrBillerOperatorsList.addAll(billerNameList);
        adapter.notifyDataSetChanged();
    }

    //this is angootha
    @Override
    public void onSelectBillerName(GetWRBillerNamesResponseC billerName) {
        Bundle bundle = new Bundle();
        binding.searchView.setText("");
        bundle.putString("billerId", String.valueOf(billerName.Biller_ID));
        bundle.putString("paymentamount_validation", billerName.paymentamount_validation);
        bundle.putString("partial_pay", billerName.partial_pay);
        bundle.putString("pay_after_duedate", billerName.pay_after_duedate);
        bundle.putString("currency", billerName.getCurrency());
        bundle.putString("biller_logo", billerName.getBiller_logo());
        Navigation.findNavController(binding.getRoot()).navigate(R.id
                .action_utilityBillPaymentPlanFragment_to_utilityPaymentAccountNoFragment, bundle);
    }


    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }
}
