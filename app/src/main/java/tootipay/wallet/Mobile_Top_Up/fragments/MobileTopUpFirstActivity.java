package tootipay.wallet.Mobile_Top_Up.fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

import tootipay.wallet.Mobile_Top_Up.MobileTopUpMainActivity;
import tootipay.wallet.Mobile_Top_Up.helpers.MobileTopUpType;
import tootipay.wallet.R;
import tootipay.wallet.databinding.ActivityMobileTopUpFirstBinding;
import tootipay.wallet.di.RequestHelper.GetWRBillerCategoryRequest;
import tootipay.wallet.di.RequestHelper.GetWRBillerNamesRequest;
import tootipay.wallet.di.RequestHelper.GetWRCountryListRequest;
import tootipay.wallet.di.RequestHelper.GetWRMobileTopupTypesRequest;
import tootipay.wallet.di.RequestHelper.GetWRPrepaidOperatorRequest;
import tootipay.wallet.di.ResponseHelper.PrepaidOperatorResponse;
import tootipay.wallet.di.ResponseHelper.WRBillerCategoryResponse;
import tootipay.wallet.di.ResponseHelper.WRBillerNamesResponse;
import tootipay.wallet.di.ResponseHelper.WRBillerTypeResponse;
import tootipay.wallet.di.ResponseHelper.WRCountryListResponse;
import tootipay.wallet.di.apicaller.GetWRBillerCategoryTask;
import tootipay.wallet.di.apicaller.GetWRBillerNamesTask;
import tootipay.wallet.di.apicaller.GetWRCountryListTask;
import tootipay.wallet.di.apicaller.MobileTopUpTypesTask;
import tootipay.wallet.di.apicaller.PrepaidOperatorTask;
import tootipay.wallet.dialogs.WRBillerCategoryDialog;
import tootipay.wallet.dialogs.WRBillerOperatorsDialog;
import tootipay.wallet.dialogs.WRBillerTypeDialog;
import tootipay.wallet.dialogs.WRCountryListDialog;
import tootipay.wallet.fragments.BaseFragment;
import tootipay.wallet.interfaces.OnGetPrepaidOperator;
import tootipay.wallet.interfaces.OnWRBillerCategoryResponse;
import tootipay.wallet.interfaces.OnWRBillerNames;
import tootipay.wallet.interfaces.OnWRBillerType;
import tootipay.wallet.interfaces.OnWRCountryList;
import tootipay.wallet.utils.CheckValidation;
import tootipay.wallet.utils.IsNetworkConnection;
import tootipay.wallet.utils.StringHelper;

public class MobileTopUpFirstActivity extends BaseFragment<ActivityMobileTopUpFirstBinding>
        implements OnWRCountryList, OnWRBillerType, OnWRBillerCategoryResponse, OnWRBillerNames
        , OnGetPrepaidOperator {


    List<WRCountryListResponse> countryList;
    List<WRBillerTypeResponse> wrBillerTypeResponseList;
    List<WRBillerCategoryResponse> wrBillerCategoryList;
    List<WRBillerNamesResponse> wrBillerOperatorsList;

    public String billerTypeId = "";
    public String billerCategoryId = "";
    public String countryCode = "";
    public Integer billerId;


    @Override
    protected void injectView() {

    }


    private boolean isPrepaidValidate() {
        if (TextUtils.isEmpty(binding.numberLayout.countryCodeTextView.getText().toString())) {
            onMessage(getString(R.string.select_country));
            return false;
        } else if (TextUtils.isEmpty(binding.numberLayout.mobilesignupb.getText().toString())) {
            onMessage(getString(R.string.plz_enter_mobile_no));
            return false;
        } else if (!CheckValidation.isPhoneNumberValidate(binding.numberLayout.mobilesignupb.getText().toString()
                , binding.numberLayout.countryCodeTextView.getText().toString())) {
            onMessage(getString(R.string.invalid_number));
            return false;
        } else if (TextUtils.isEmpty(binding.prepaidOperator.getText().toString())) {
            onMessage(getString(R.string.select_operator));
        }
        return true;
    }

    public boolean isNumberValidate() {
        if (TextUtils.isEmpty(binding.numberLayout.countryCodeTextView.getText().toString())) {
            onMessage(getString(R.string.select_country));
            return false;
        } else if (TextUtils.isEmpty(binding.numberLayout.mobilesignupb.getText().toString())) {
            onMessage(getString(R.string.plz_enter_mobile_no));
            return false;
        } else if (!CheckValidation.isPhoneNumberValidate(binding.numberLayout.mobilesignupb.getText().toString()
                , binding.numberLayout.countryCodeTextView.getText().toString())) {
            onMessage(getString(R.string.invalid_number));
            return false;
        }
        return true;
    }

    @Override
    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.numberLayout.mobilesignupb.getText().toString())) {
            onMessage(getString(R.string.plz_enter_mobile_no));
            return false;
        } else if (!CheckValidation.isPhoneNumberValidate(binding.numberLayout.mobilesignupb.getText().toString()
                , binding.numberLayout.countryCodeTextView.getText().toString())) {
            onMessage(getString(R.string.invalid_number));
            return false;
        } else if (TextUtils.isEmpty(binding.operator.getText())) {
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
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        countryList = new ArrayList<>();
        wrBillerTypeResponseList = new ArrayList<>();
        wrBillerCategoryList = new ArrayList<>();
        wrBillerOperatorsList = new ArrayList<>();
        binding.nextLayout.setOnClickListener(v -> {
            if (((MobileTopUpMainActivity) getBaseActivity()).topUpType.equals(MobileTopUpType.PRE_PAID)) {
                onPrePaidPlans();
            } else if (((MobileTopUpMainActivity) getBaseActivity()).topUpType.equals(MobileTopUpType.POST_PAID)) {
                onPostPaidPlans();
            } else {
                onMessage(getString(R.string.some_thing_wrong));
            }
        });




        binding.numberLayout.mobilesignupb.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!billerTypeId.isEmpty()) {
                    if(Integer.parseInt(billerTypeId) == MobileTopUpType.PRE_PAID) {
                        binding.operator.setText("");
                        binding.prepaidOperatorLayout.setVisibility(View.GONE);
                        binding.prepaidOperator.setText("");
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        binding.numberLayout.countrySpinnerSignIn.setOnClickListener(v -> {
            if (countryList.isEmpty()) {
                if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                    GetWRCountryListRequest request = new GetWRCountryListRequest();
                    request.languageId = getSessionManager().getlanguageselection();

                    GetWRCountryListTask task = new GetWRCountryListTask(getContext(), this);
                    task.execute(request);
                } else {
                    onMessage(getString(R.string.no_internet));
                }
            } else {
                showCountryListDialog();
            }
        });


        binding.operator.setOnClickListener(v -> {
            if (isNumberValidate()) {
                if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                    GetWRMobileTopupTypesRequest request = new GetWRMobileTopupTypesRequest();
                    request.languageId = getSessionManager().getlanguageselection();
                    request.countryCode = this.countryCode;
                    MobileTopUpTypesTask task = new MobileTopUpTypesTask(getContext(), this);
                    task.execute(request);
                } else {
                    onMessage(getString(R.string.no_internet));
                }
            }

        });


        binding.selectCategory.setOnClickListener(v -> {
            if (!billerTypeId.isEmpty()) {
                GetWRBillerCategoryRequest request = new GetWRBillerCategoryRequest();
                request.billerTypeId = billerTypeId;
                request.countryCode = countryCode;
                request.languageId = getSessionManager().getlanguageselection();

                GetWRBillerCategoryTask task = new GetWRBillerCategoryTask(getContext(), this);
                task.execute(request);
            } else {
                onMessage(getString(R.string.plz_select_top_up_type));
            }
        });


        binding.selectOperator.setOnClickListener(v -> {
            if (billerCategoryId.isEmpty()) {
                onMessage(getString(R.string.select_category));
            } else if (countryCode.isEmpty()) {
                onMessage(getString(R.string.invalid_number));
            } else {
                GetWRBillerNamesRequest request = new GetWRBillerNamesRequest();
                request.countryCode = countryCode;
                request.billerCategoryID = billerCategoryId;
                request.billerTypeID = billerTypeId;
                request.languageId = getSessionManager().getlanguageselection();
                if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                    GetWRBillerNamesTask task = new GetWRBillerNamesTask(getActivity(), this);
                    task.execute(request);
                } else {
                    onMessage(getString(R.string.no_internet));
                }
            }
        });


        binding.prepaidOperator.setOnClickListener(v -> {

        });
    }


    void onPrePaidPlans() {
        if (isPrepaidValidate()) {
            ((MobileTopUpMainActivity) getBaseActivity())
                    .prepaidPlansRequest.countryCode = this.countryCode;
            ((MobileTopUpMainActivity) getBaseActivity())
                    .prepaidPlansRequest.languageId = getSessionManager().getlanguageselection();
            ((MobileTopUpMainActivity) getBaseActivity()).topUpType = Integer.parseInt(billerTypeId);

            ((MobileTopUpMainActivity)getBaseActivity()).prepaidRechargeRequest
                    .operatorName = binding.prepaidOperator.getText().toString();
            ((MobileTopUpMainActivity) getBaseActivity()).prepaidRechargeRequest.mobileNumber
                    = binding.numberLayout.mobilesignupb.getText().toString();

            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id
                            .action_mobileTopUpFirstActivity_to_WRBillerNamesFragment);
        }
    }


    void onPostPaidPlans() {
        if (isValidate()) {
            ((MobileTopUpMainActivity) getBaseActivity()).topUpType = Integer.parseInt(billerTypeId);
            ((MobileTopUpMainActivity) getBaseActivity())
                    .plansRequest.countryCode = this.countryCode;
            ((MobileTopUpMainActivity) getBaseActivity())
                    .plansRequest.billerTypeID = this.billerTypeId;
            ((MobileTopUpMainActivity) getBaseActivity())
                    .plansRequest.billerCategoryId = this.billerCategoryId;
            ((MobileTopUpMainActivity) getBaseActivity())
                    .plansRequest.billerID = this.billerId;


            ((MobileTopUpMainActivity) getBaseActivity()).payBillRequest
                    .mobileAccount = StringHelper.removeFirst(
                    binding.numberLayout.countryCodeTextView.getText()
                            .toString() + binding.numberLayout.mobilesignupb.getText().toString()
            );
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id
                            .action_mobileTopUpFirstActivity_to_WRBillerNamesFragment);
        }
    }


    void getPrepaidOperator() {
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            GetWRPrepaidOperatorRequest request = new GetWRPrepaidOperatorRequest();
            request.mobileNo = binding.numberLayout.mobilesignupb.getText().toString();
            request.countryShortName = countryCode;
            request.languageId = getSessionManager().getlanguageselection();


            PrepaidOperatorTask task = new PrepaidOperatorTask(getContext(), this);
            task.execute(request);
        } else {
            onMessage(getString(R.string.no_internet));
        }
    }


    void showBillerTypeDialog() {
        WRBillerTypeDialog dialog = new WRBillerTypeDialog(wrBillerTypeResponseList
                , this);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }


    void showCountryListDialog() {
        WRCountryListDialog dialog = new WRCountryListDialog(countryList,
                this);
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
    public int getLayoutId() {
        return R.layout.activity_mobile_top_up_first;
    }

    @Override
    public void onWRCountryList(List<WRCountryListResponse> list) {
        countryList.clear();
        countryList.addAll(list);
        showCountryListDialog();
    }

    @Override
    public void onWRSelectCountry(WRCountryListResponse country) {
        countryCode = country.countryShortName;
        binding.numberLayout.countryCodeTextView.setText(country.countryCode);
        binding.selectOperator.setText("");
        binding.selectCategory.setText("");
        binding.operator.setText("");

        setSendingCurrencyImage(country.image_URL);

        ((MobileTopUpMainActivity) getBaseActivity()).payBillRequest.payoutCurrency = country.countryCurrency;
        ((MobileTopUpMainActivity) getBaseActivity()).payBillRequest.countryCode = country.countryShortName;

        ((MobileTopUpMainActivity) getBaseActivity()).prepaidRechargeRequest.payoutCurrency = country.countryCurrency;
        ((MobileTopUpMainActivity) getBaseActivity()).prepaidRechargeRequest.countryCode = country.countryShortName;
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }

    @Override
    public void onBillerTypeList(List<WRBillerTypeResponse> billerTypeList) {
        wrBillerTypeResponseList.clear();
        wrBillerTypeResponseList.addAll(billerTypeList);
        showBillerTypeDialog();
    }

    @Override
    public void onBillerTypeSelect(WRBillerTypeResponse billerType) {
        binding.operator.setText(billerType.billerName);
        //  if (!billerTypeId.equalsIgnoreCase(billerType.id)) {
        // wrBillerTypeResponseList.clear();
        //   }
        billerTypeId = billerType.id;
        binding.selectCategory.setText("");
        binding.selectOperator.setText("");
        getPrepaidOperator();
        ((MobileTopUpMainActivity) getBaseActivity()).topUpType = Integer.parseInt(billerType.id);
    }

    @Override
    public void onWRCategory(List<WRBillerCategoryResponse> responseList) {
        wrBillerCategoryList.clear();
        wrBillerCategoryList.addAll(responseList);
        showCategoryListDialog();
    }

    @Override
    public void onSelectCategory(WRBillerCategoryResponse category) {
        billerCategoryId = category.id;
        binding.selectCategory.setText(category.name);
        //  if (!billerCategoryId.equalsIgnoreCase(category.id)) {
        // wrBillerCategoryList.clear();
        // }

        binding.selectOperator.setText("");
    }

    @Override
    public void onBillerNamesResponse(List<WRBillerNamesResponse> responses) {
        wrBillerOperatorsList.clear();
        wrBillerOperatorsList.addAll(responses);
        showOperatorDialog();
    }

    @Override
    public void onSelectBillerName(WRBillerNamesResponse billerName) {
        billerId = billerName.billerId;
        binding.selectOperator.setText(billerName.billerName);
    }

    public void setSendingCurrencyImage(String url) {
        Glide.with(this)
                .asBitmap()
                .load(url)
                .placeholder(R.drawable.ic_united_kingdom)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource,
                                                @Nullable Transition<? super Bitmap> transition) {
                        binding.numberLayout.imageIcon.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    @Override
    public void onGetPrepaidOperator(PrepaidOperatorResponse operator) {
        binding.prepaidOperator.setText(operator.operatorName);
        ((MobileTopUpMainActivity) getBaseActivity())
                .prepaidPlansRequest.operatorCode = operator.operatorCode;
        ((MobileTopUpMainActivity) getBaseActivity())
                .prepaidPlansRequest.circleCode = operator.circleCode;


        if(!billerTypeId.isEmpty()) {
            if (Integer.parseInt(billerTypeId) == MobileTopUpType.PRE_PAID) {
                binding.prepaidOperatorLayout.setVisibility(View.VISIBLE);
                binding.postpaidPlanDecideLayout.setVisibility(View.GONE);

            } else if (Integer.parseInt(billerTypeId) == MobileTopUpType.POST_PAID) {
                binding.prepaidOperatorLayout.setVisibility(View.GONE);
                binding.postpaidPlanDecideLayout.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void onErrorWithCode(String code, String error) {
        if(code == "411" && error.replace(" " ,"")
                .equalsIgnoreCase("operatornotdetected")) {

        } else {
            onMessage(error);
        }
    }
}
