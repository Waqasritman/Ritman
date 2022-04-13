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
        implements OnWRCountryList, OnWRBillerType, OnWRBillerCategoryResponse, OnWRBillerNames {


    List<GetWRCountryListResponseC> countryList;
    List<GetWRBillerTypeResponse> wrBillerTypeResponseList;
    List<GetWRBillerCategoryResponseC> wrBillerCategoryList;
    List<GetWRBillerNamesResponseC> wrBillerOperatorsList;
    WRBillerOperatorAdapter adapter;

    // BankTransferViewModel viewModel;
    BillPaymentViewModel viewModel;

    public String billerTypeId = "1";
    public String billerCategoryId = "";
    public String countryCode = "";
    public String billerId;
    public String paymentamount_validation, currency, biller_logo, partial_pay, pay_after_duedate;


    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = ((BillPaymentMainActivity) getBaseActivity()).viewModel;

        countryList = new ArrayList<>();
        wrBillerTypeResponseList = new ArrayList<>();
        wrBillerCategoryList = new ArrayList<>();
        wrBillerOperatorsList = new ArrayList<>();

        //coming from UtilityCategoryActivity
        assert getArguments() != null;
        {
            billerCategoryId = getArguments().getString("billerCategoryId");
            ((BillPaymentMainActivity) getBaseActivity()).binding.toolBar.titleTxt.setText(
                    getArguments().getString("billerName")
            );
        }

        setSearchView();
        setupRecyclerView();
        getBillerName();

        binding.searchView.setVisibility(View.VISIBLE);

        binding.countryLayout.setOnClickListener(v -> {
            if (countryList.isEmpty()) {
                if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                    Utils.showCustomProgressDialog(getContext(), false);
                    GetWRCountryListRequestC requestc = new GetWRCountryListRequestC();
                    String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                            .getKey(getSessionManager().getMerchantName())).trim();
                    String body = RestClient.makeGSONString(requestc);

                    AERequest request = new AERequest();
                    request.body = AESHelper.encrypt(body.trim(), gKey.trim());
                    viewModel.GetCountryList(request, KeyHelper.getKey(getSessionManager().getMerchantName()).trim(),
                            KeyHelper.getSKey(KeyHelper
                                    .getKey(getSessionManager().getMerchantName())))
                            .observe(getViewLifecycleOwner(), response -> {
                                if (response.status == Status.ERROR) {
                                    onError(getString(response.messageResourceId));
                                } else {
                                    assert response.resource != null;
                                    Utils.hideCustomProgressDialog();
                                    if (response.resource.responseCode.equals(101)) {
                                        String bodyy = AESHelper.decrypt(response.resource.data.body
                                                , gKey);
                                        try {
                                            Gson gson = new Gson();
                                            Type type = new TypeToken<List<GetWRCountryListResponseC>>() {
                                            }.getType();
                                            List<GetWRCountryListResponseC> data = gson.fromJson(bodyy, type);
                                            onWRCountryList(data);

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
                } else {
                    onMessage(getString(R.string.no_internet));
                    Utils.hideCustomProgressDialog();
                }
            } else {
                //showCountryListDialog();
            }
        });


        binding.operator.setOnClickListener(v -> {

            GetWRBillerTypeRequestNew requestc = new GetWRBillerTypeRequestNew();
            requestc.CountryCode = countryCode;
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

        });


        binding.selectCategory.setOnClickListener(v -> {
            if (!billerTypeId.isEmpty()) {

                Utils.showCustomProgressDialog(getContext(), false);
                GetWRBillerCategoryRequestC requestc = new GetWRBillerCategoryRequestC();
                requestc.CountryCode = countryCode;

                //request.BillerTypeId=1;
                requestc.BillerTypeId = Integer.valueOf(billerTypeId);
                String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                        .getKey(getSessionManager().getMerchantName())).trim();
                String body = RestClient.makeGSONString(requestc);

                AERequest request = new AERequest();
                request.body = AESHelper.encrypt(body.trim(), gKey.trim());
                viewModel.GetWRBillerCategory(request, KeyHelper.getKey(getSessionManager().getMerchantName()).trim(),
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
                                        Type type = new TypeToken<List<GetWRBillerCategoryResponseC>>() {
                                        }.getType();
                                        List<GetWRBillerCategoryResponseC> data = gson.fromJson(bodyy, type);
                                        onWRCategory(data);

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

            } else {
                onMessage(getString(R.string.select_utility_bill_type));
            }


        });


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

    void showBillerTypeDialog() {
        WRBillerTypeDialog dialog = new WRBillerTypeDialog(wrBillerTypeResponseList
                , this);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }


    void showCategoryListDialog() {
        WRBillerCategoryDialog dialog = new WRBillerCategoryDialog(wrBillerCategoryList
                , this);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }


    void showOperatorDialog() {
        WRBillerOperatorsDialog dialog = new WRBillerOperatorsDialog(wrBillerOperatorsList
                , this);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }

    @Override
    public void onWRCategory(List<GetWRBillerCategoryResponseC> responseList) {
        wrBillerCategoryList.clear();
        wrBillerCategoryList.addAll(responseList);
        showCategoryListDialog();
    }

    @Override
    public void onSelectCategory(GetWRBillerCategoryResponseC category) {
        billerCategoryId = String.valueOf(category.getID());
        binding.selectCategory.setText(category.getName());
        binding.selectOperator.setText("");
    }

    @Override
    public void onBillerNamesList(List<GetWRBillerNamesResponseC> billerNameList) {
        wrBillerOperatorsList.clear();
        wrBillerOperatorsList.addAll(billerNameList);
        adapter.notifyDataSetChanged();
        // showOperatorDialog();
    }

    @Override
    public void onSelectBillerName(GetWRBillerNamesResponseC billerName) {

        billerId = billerName.Biller_ID;
        paymentamount_validation = billerName.paymentamount_validation;
        partial_pay = billerName.partial_pay;
        pay_after_duedate = billerName.pay_after_duedate;
        currency = billerName.getCurrency();
        biller_logo = billerName.getBiller_logo();


        Bundle bundle = new Bundle();
        bundle.putString("billerId", String.valueOf(billerId));
        bundle.putString("paymentamount_validation", paymentamount_validation);
        bundle.putString("partial_pay", partial_pay);
        bundle.putString("pay_after_duedate", pay_after_duedate);
        bundle.putString("currency", currency);
        bundle.putString("biller_logo", biller_logo);
        // bundle.putString("SkuId", String.valueOf(SkuId));


        Navigation.findNavController(binding.getRoot()).navigate(R.id
                .action_utilityBillPaymentPlanFragment_to_utilityPaymentAccountNoFragment, bundle);
    }


    @Override
    public void onBillerTypeList(List<GetWRBillerTypeResponse> billerTypeList) {
        wrBillerTypeResponseList.clear();
        wrBillerTypeResponseList.addAll(billerTypeList);
        showBillerTypeDialog();
    }

    @Override
    public void onBillerTypeSelect(GetWRBillerTypeResponse billerType) {
        binding.operator.setText(billerType.getName());
        //  if (!billerTypeId.equalsIgnoreCase(billerType.id)) {
        // wrBillerTypeResponseList.clear();
        //   }
        //   billerTypeId = billerType.getID();

        billerTypeId = String.valueOf(billerType.getID());

        binding.selectCategory.setText("");
        binding.selectOperator.setText("");

        ((BillPaymentMainActivity) getBaseActivity()).payBillRequest.billerID = billerType.getID();

    }


    @Override
    public void onWRCountryList(List<GetWRCountryListResponseC> list) {
        countryList.clear();
        countryList.addAll(list);
        //showCountryListDialog();
    }

    @Override
    public void onWRSelectCountry(GetWRCountryListResponseC country) {
        countryCode = country.getCountry_Code();
        binding.countryCode.setText(country.getCountry_Name());
        binding.selectOperator.setText("");
        binding.selectCategory.setText("");
        binding.operator.setText("");
        ((BillPaymentMainActivity) getBaseActivity()).payBillRequest.payoutCurrency = country.getCountry_Currency();
    }


    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
