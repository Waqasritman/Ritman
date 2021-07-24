package ritman.wallet.billpayment.updated_fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.Util;
import ritman.wallet.MoneyTransferModuleV.viewmodels.BankTransferViewModel;
import ritman.wallet.R;
import ritman.wallet.adapters.RechargePlanAdapter;
import ritman.wallet.adapters.WRBillerOperatorAdapter;
import ritman.wallet.billpayment.BillPaymentMainActivity;
import ritman.wallet.billpayment.viewmodel.BillPaymentViewModel;
import ritman.wallet.databinding.UtilityPaymentPlansViewBinding;
import ritman.wallet.di.JSONdi.Status;
import ritman.wallet.di.JSONdi.restRequest.GetWRBillerCategoryRequestC;
import ritman.wallet.di.JSONdi.restRequest.GetWRBillerNamesRequestC;
import ritman.wallet.di.JSONdi.restRequest.GetWRBillerTypeRequestNew;
import ritman.wallet.di.JSONdi.restRequest.GetWRCountryListRequestC;
import ritman.wallet.di.JSONdi.restResponse.GetWRBillerCategoryResponseC;
import ritman.wallet.di.JSONdi.restResponse.GetWRBillerNamesResponseC;
import ritman.wallet.di.JSONdi.restResponse.GetWRBillerTypeResponse;
import ritman.wallet.di.JSONdi.restResponse.GetWRCountryListResponseC;
import ritman.wallet.di.JSONdi.restResponse.RechargePlansResponse;
import ritman.wallet.dialogs.WRBillerCategoryDialog;
import ritman.wallet.dialogs.WRBillerOperatorsDialog;
import ritman.wallet.dialogs.WRBillerTypeDialog;
import ritman.wallet.dialogs.WRCountryListDialog;
import ritman.wallet.fragments.BaseFragment;
import ritman.wallet.interfaces.OnWRBillerCategoryResponse;
import ritman.wallet.interfaces.OnWRBillerNames;
import ritman.wallet.interfaces.OnWRBillerType;
import ritman.wallet.interfaces.OnWRCountryList;
import ritman.wallet.utils.IsNetworkConnection;
import ritman.wallet.utils.Utils;


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
    public String paymentamount_validation,currency,biller_logo,partial_pay,pay_after_duedate;



   /* @Override
    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.operator.getText())) {
            onMessage(getString(R.string.plz_select_top_up_type));
            return false;
        } else if (TextUtils.isEmpty(binding.selectCategory.getText())) {
            onMessage(getString(R.string.plz_select_category));
            return false;
        } else if (TextUtils.isEmpty(binding.selectOperator.getText())) {
            onMessage(getString(R.string.plz_select_operator));
            return false;
        }
        return true;
    }*/

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(BillPaymentViewModel.class);
        countryList = new ArrayList<>();
        wrBillerTypeResponseList = new ArrayList<>();
        wrBillerCategoryList = new ArrayList<>();
        wrBillerOperatorsList = new ArrayList<>();

        //coming from UtilityCategoryActivity
        billerCategoryId = getArguments().getString("billerCategoryId");
        setSearchView();
        setupRecyclerView();
        getBillerName();

        binding.searchView.setVisibility(View.VISIBLE);

        binding.countryLayout.setOnClickListener(v -> {
            if (countryList.isEmpty()) {
                if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                    Utils.showCustomProgressDialog(getContext(), false);
                    GetWRCountryListRequestC request = new GetWRCountryListRequestC();

                    viewModel.GetCountryList(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()

                            , response -> {
                                if (response.status == Status.ERROR) {
                                    onMessage(getString(response.messageResourceId));
                                } else {
                                    assert response.resource != null;
                                    Utils.hideCustomProgressDialog();
                                    if (response.resource.responseCode.equals(101)) {
                                        onWRCountryList(response.resource.data);
                                    } else {
                                        onMessage(response.resource.description);
                                        Utils.hideCustomProgressDialog();
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

            /*if (!countryCode.isEmpty()){
                onMessage(getString(R.string.select_country));
            }else {

            }*/
            GetWRBillerTypeRequestNew request = new GetWRBillerTypeRequestNew();
            request.CountryCode = countryCode;

            viewModel.GetWRBillerType(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()

                    , response -> {
                        if (response.status == Status.ERROR) {
                            onMessage(getString(response.messageResourceId));
                        } else {
                            assert response.resource != null;
                            if (response.resource.responseCode.equals(101)) {

                                String s = response.resource.getName();

                                onBillerTypeList(response.resource.data);
                            } else {
                                onMessage(response.resource.description);
                            }
                        }
                    });

                /*   if (!countryCode.isEmpty()) {
                if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                    GetWRBillerTypeRequest request = new GetWRBillerTypeRequest();
                    request.languageId = getSessionManager().getlanguageselection();
                    request.countryCode = this.countryCode;
                    WRBillerTypeTask task = new WRBillerTypeTask(getContext(), this);
                    task.execute(request);
                } else {
                    onMessage(getString(R.string.no_internet));
                }
            } else {
                onMessage(getString(R.string.plz_select_country_error));
            }*/

        });


        binding.selectCategory.setOnClickListener(v -> {
            if (!billerTypeId.isEmpty()) {

                Utils.showCustomProgressDialog(getContext(), false);
                GetWRBillerCategoryRequestC request = new GetWRBillerCategoryRequestC();
                request.CountryCode = countryCode;

                //request.BillerTypeId=1;
                request.BillerTypeId = Integer.valueOf(billerTypeId);


                viewModel.GetWRBillerCategory(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()

                        , response -> {
                            if (response.status == Status.ERROR) {
                                onMessage(getString(response.messageResourceId));
                            } else {
                                assert response.resource != null;
                                if (response.resource.responseCode.equals(101)) {

                                    String s = response.resource.getName();

                                    onWRCategory(response.resource.data);
                                } else {
                                    onMessage(response.resource.description);
                                }
                            }
                        });



                /*GetWRBillerCategoryRequest request = new GetWRBillerCategoryRequest();
                request.billerTypeId = billerTypeId;
                request.countryCode = countryCode;
                request.languageId = getSessionManager().getlanguageselection();

                GetWRBillerCategoryTask task = new GetWRBillerCategoryTask(getContext(), this);
                task.execute(request);*/

            } else {
                onMessage(getString(R.string.select_utility_bill_type));
            }


        });


    }


    public void getBillerName() {
        Utils.showCustomProgressDialog(getContext(), false);
        GetWRBillerNamesRequestC request = new GetWRBillerNamesRequestC();
        request.CountryCode = "IN";
        request.BillerTypeId = 1;//Integer.valueOf(billerTypeId);
        request.BillerCategoryId =  Integer.valueOf(billerCategoryId);//62;


        viewModel.GetWRBillerNames(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()

                , response -> {
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                        Utils.hideCustomProgressDialog();
                    } else {
                        Utils.hideCustomProgressDialog();
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {

                            onBillerNamesList(response.resource.data);



                        } else {
                            onMessage(response.resource.description);
                            Utils.hideCustomProgressDialog();
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

//    void showCountryListDialog() {
//        WRCountryListDialog dialog = new WRCountryListDialog(countryList,
//                this);
//        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
//        dialog.show(transaction, "");
//    }

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
        paymentamount_validation=billerName.paymentamount_validation;
        partial_pay=billerName.partial_pay;
        pay_after_duedate=billerName.pay_after_duedate;
        currency=billerName.getCurrency();
        biller_logo=billerName.getBiller_logo();



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
