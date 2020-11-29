package totipay.wallet.billpayment.updated_fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

import totipay.wallet.R;
import totipay.wallet.billpayment.BillPaymentMainActivity;
import totipay.wallet.databinding.UtilityPaymentPlansViewBinding;
import totipay.wallet.di.RequestHelper.GetWRBillerCategoryRequest;
import totipay.wallet.di.RequestHelper.GetWRBillerNamesRequest;
import totipay.wallet.di.RequestHelper.GetWRBillerTypeRequest;
import totipay.wallet.di.RequestHelper.GetWRCountryListRequest;
import totipay.wallet.di.ResponseHelper.WRBillerCategoryResponse;
import totipay.wallet.di.ResponseHelper.WRBillerNamesResponse;
import totipay.wallet.di.ResponseHelper.WRBillerTypeResponse;
import totipay.wallet.di.ResponseHelper.WRCountryListResponse;
import totipay.wallet.di.apicaller.GetWRBillerCategoryTask;
import totipay.wallet.di.apicaller.GetWRBillerNamesTask;
import totipay.wallet.di.apicaller.GetWRCountryListTask;
import totipay.wallet.di.apicaller.WRBillerTypeTask;
import totipay.wallet.dialogs.WRBillerCategoryDialog;
import totipay.wallet.dialogs.WRBillerOperatorsDialog;
import totipay.wallet.dialogs.WRBillerTypeDialog;
import totipay.wallet.dialogs.WRCountryListDialog;
import totipay.wallet.fragments.BaseFragment;
import totipay.wallet.interfaces.OnWRBillerCategoryResponse;
import totipay.wallet.interfaces.OnWRBillerNames;
import totipay.wallet.interfaces.OnWRBillerType;
import totipay.wallet.interfaces.OnWRCountryList;
import totipay.wallet.utils.IsNetworkConnection;

public class UtilityBillPaymentPlanFragment extends BaseFragment<UtilityPaymentPlansViewBinding>
        implements OnWRCountryList, OnWRBillerType, OnWRBillerCategoryResponse, OnWRBillerNames {


    List<WRCountryListResponse> countryList;
    List<WRBillerTypeResponse> wrBillerTypeResponseList;
    List<WRBillerCategoryResponse> wrBillerCategoryList;
    List<WRBillerNamesResponse> wrBillerOperatorsList;


    public String billerTypeId = "";
    public String billerCategoryId = "";
    public String countryCode = "";
    public Integer billerId;


    @Override
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
    }

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        countryList = new ArrayList<>();
        wrBillerTypeResponseList = new ArrayList<>();
        wrBillerCategoryList = new ArrayList<>();
        wrBillerOperatorsList = new ArrayList<>();

        binding.countryCode.setHint(getString(R.string.select_country));
        binding.nextLayout.setOnClickListener(v -> {

            if (isValidate()) {
                ((BillPaymentMainActivity) getBaseActivity())
                        .plansRequest.countryCode = this.countryCode;
                ((BillPaymentMainActivity) getBaseActivity())
                        .plansRequest.billerTypeID = this.billerTypeId;
                ((BillPaymentMainActivity) getBaseActivity())
                        .plansRequest.billerCategoryId = this.billerCategoryId;
                ((BillPaymentMainActivity) getBaseActivity())
                        .plansRequest.billerID = this.billerId;

                Navigation.findNavController(v)
                        .navigate(R.id
                                .action_utilityBillPaymentPlanFragment_to_utilityPaymentPlanFragment);
            }


        });


        binding.countryLayout.setOnClickListener(v -> {
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
            if (!countryCode.isEmpty()) {
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
                onMessage(getString(R.string.select_utility_bill_type));
            }


        });


        binding.selectOperator.setOnClickListener(v -> {

            if (billerCategoryId.isEmpty()) {
                onMessage(getString(R.string.select_category));
            } else if (countryCode.isEmpty()) {
                onMessage(getString(R.string.select_country));
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
    }

    @Override
    public int getLayoutId() {
        return R.layout.utility_payment_plans_view;
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
    public void onWRCategory(List<WRBillerCategoryResponse> responseList) {
        wrBillerCategoryList.clear();
        wrBillerCategoryList.addAll(responseList);
        showCategoryListDialog();
    }

    @Override
    public void onSelectCategory(WRBillerCategoryResponse category) {
        billerCategoryId = category.id;
        binding.selectCategory.setText(category.name);

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
        binding.countryCode.setText(country.countryName);
        binding.selectOperator.setText("");
        binding.selectCategory.setText("");
        binding.operator.setText("");
        ((BillPaymentMainActivity) getBaseActivity()).payBillRequest.payoutCurrency = country.countryCurrency;
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
