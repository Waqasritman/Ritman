package ritman.wallet.beneficiary_list_module.fragments;

import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ritman.wallet.R;
import ritman.wallet.beneficiary_list_module.BeneficiaryListActivity;
import ritman.wallet.databinding.FragmentUpdateCashBeneficiaryBinding;
import ritman.wallet.di.JSONdi.restResponse.RelationListResponse;
import ritman.wallet.di.XMLdi.RequestHelper.EditBeneficiaryRequest;
import ritman.wallet.di.XMLdi.RequestHelper.GetSendRecCurrencyRequest;
import ritman.wallet.di.XMLdi.ResponseHelper.GetBeneficiaryListResponse;
import ritman.wallet.di.XMLdi.ResponseHelper.GetCashNetworkListResponse;
import ritman.wallet.di.XMLdi.ResponseHelper.GetCountryListResponse;
import ritman.wallet.di.XMLdi.ResponseHelper.GetRelationListResponse;
import ritman.wallet.di.XMLdi.ResponseHelper.GetSendRecCurrencyResponse;
import ritman.wallet.di.XMLdi.apicaller.EditBeneficiaryTask;
import ritman.wallet.di.XMLdi.apicaller.GetSendRecCurrencyTask;
import ritman.wallet.dialogs.DialogCountry;
import ritman.wallet.dialogs.DialogCurrency;
import ritman.wallet.dialogs.DialogRelationList;
import ritman.wallet.fragments.BaseFragment;
import ritman.wallet.interfaces.OnGetCashNetworkList;
import ritman.wallet.interfaces.OnSelectCountry;
import ritman.wallet.interfaces.OnSelectCurrency;
import ritman.wallet.interfaces.OnSelectRelation;
import ritman.wallet.interfaces.OnSuccessMessage;
import ritman.wallet.utils.CheckValidation;
import ritman.wallet.utils.IsNetworkConnection;
import ritman.wallet.utils.StringHelper;

public class UpdateCashBeneficiaryFragment extends BaseFragment<FragmentUpdateCashBeneficiaryBinding> implements
        OnSelectCountry, OnSelectRelation, OnSelectCurrency, OnGetCashNetworkList
        , OnSuccessMessage {

    GetBeneficiaryListResponse beneficiaryDetails;
    EditBeneficiaryRequest editBeneficiaryRequest;
    List<GetCashNetworkListResponse> networkLists;

    @Override
    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.firstName.getText().toString())) {
            onMessage(getString(R.string.enter_name_bene__first_name_error));
            return false;
        } else if (TextUtils.isEmpty(binding.middleName.getText().toString())) {
            onMessage(getString(R.string.enter_name_bene__middle_name_error));
            return false;
        } else if (TextUtils.isEmpty(binding.lastName.getText().toString())) {
            onMessage(getString(R.string.enter_name_bene__last_name_error));
            return false;
        } else if (TextUtils.isEmpty(binding.mobileNumber.getText().toString())) {
            onMessage(getString(R.string.plz_enter_mobile_no));
            return false;
        } else if (TextUtils.isEmpty(binding.country.getText().toString())) {
            onMessage(getString(R.string.plz_select_country_error));
            return false;
        } else if (TextUtils.isEmpty(binding.address.getText().toString())) {
            onMessage(getString(R.string.address_enter));
            return false;
        } else if (TextUtils.isEmpty(binding.relation.getText().toString())) {
            onMessage(getString(R.string.plz_select_relation));
            return false;
        }
//        else if (CheckValidation.isValidName(
//                binding.firstName.getText().toString())) {
//            onMessage(getString(R.string.first_name_special_character_not_allowed));
//            return false;
//        }
//        else if (CheckValidation.isValidName(
//                binding.middleName.getText().toString())) {
//            onMessage(getString(R.string.middle_name_spe_char));
//            return false;
//        } else if ((CheckValidation.isValidName(binding.lastName.getText().toString()))) {
//            onMessage(getString(R.string.last_name_spe_char));
//            return false;
//        }
        else if (!CheckValidation.isValidPhone(StringHelper.parseNumber(
                binding.mobileNumber.getText().toString()))) {
            onMessage(getString(R.string.invalid_number));
            return false;
        }
        return true;
    }

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        if (getArguments() != null) {
            beneficiaryDetails = getArguments().getParcelable("bene_details");
            showBeneficiary();
        }

        editBeneficiaryRequest = new EditBeneficiaryRequest();
        fillRequestOnStart(beneficiaryDetails);
        binding.country.setOnClickListener(v -> {
            DialogCountry dialogCountry = new DialogCountry(this, false);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            dialogCountry.show(transaction, "");
        });

        binding.relation.setOnClickListener(v -> {
            DialogRelationList dialogRelationList = new DialogRelationList(this);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            dialogRelationList.show(transaction, "");
        });


        binding.nextLayout.setOnClickListener(v -> {
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {

                editBeneficiaryRequest.FirstName = binding.firstName.getText().toString();
                editBeneficiaryRequest.LastName = binding.lastName.getText().toString();
                editBeneficiaryRequest.MiddleName = binding.middleName.getText().toString();
                editBeneficiaryRequest.customerNo = ((BeneficiaryListActivity) getBaseActivity())
                        .sessionManager.getCustomerNo();
                editBeneficiaryRequest.Telephone = binding.mobileNumber.getText().toString();
                editBeneficiaryRequest.Address = binding.address.getText().toString();
                editBeneficiaryRequest.languageId = getSessionManager().getDefaultLanguage();

                if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                    EditBeneficiaryTask task = new EditBeneficiaryTask(getContext(), this);
                    task.execute(editBeneficiaryRequest);
                } else {
                    onMessage(getString(R.string.no_internet));
                }

            } else {
                onMessage(getString(R.string.no_internet));
            }
        });
    }

    private void fillRequestOnStart(GetBeneficiaryListResponse request) {
        editBeneficiaryRequest.FirstName = request.firstName;
        editBeneficiaryRequest.LastName = request.lastName;
        editBeneficiaryRequest.MiddleName = request.middleName;
        editBeneficiaryRequest.customerNo = ((BeneficiaryListActivity) getBaseActivity())
                .sessionManager.getCustomerNo();
        editBeneficiaryRequest.Telephone = request.telephone;
        editBeneficiaryRequest.Address = request.address;
        editBeneficiaryRequest.PayOutCurrency = request.payOutCurrency;
        editBeneficiaryRequest.PaymentMode = request.paymentMode;
        editBeneficiaryRequest.PayOutBranchCode = request.paymentBranchCode; // OKAY
        editBeneficiaryRequest.PayoutCountryCode = request.payOutCountryCode;
        editBeneficiaryRequest.CustomerRelation = request.customerRelation;
        editBeneficiaryRequest.beneficiaryNo = request.beneficiaryNo;
        editBeneficiaryRequest.languageId = getSessionManager().getDefaultLanguage();
    }

    private void showBeneficiary() {
        binding.firstName.setText(beneficiaryDetails.firstName);
        binding.lastName.setText(beneficiaryDetails.lastName);
        binding.middleName.setText(beneficiaryDetails.middleName);
        binding.mobileNumber.setText(beneficiaryDetails.telephone);
        binding.country.setText(beneficiaryDetails.payOutCountryCode);
        binding.address.setText(beneficiaryDetails.address);
        binding.relation.setText(beneficiaryDetails.customerRelation);
        binding.cityName.setVisibility(View.GONE);
        binding.agentsForCash.setText(beneficiaryDetails.payoutBranchName);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_update_cash_beneficiary;
    }

    @Override
    public void onSelectCountry(GetCountryListResponse country) {
        binding.country.setText(country.countryName);

        editBeneficiaryRequest.PayoutCountryCode = country.countryShortName;
        editBeneficiaryRequest.BankCountry = country.countryShortName;


        GetSendRecCurrencyRequest currencyRequest = new GetSendRecCurrencyRequest();
        currencyRequest.countryType = country.countryType;
        currencyRequest.countryShortName = country.countryShortName;
        currencyRequest.languageId = getSessionManager().getDefaultLanguage();
        if (IsNetworkConnection.checkNetworkConnection(getActivity())) {
            GetSendRecCurrencyTask task = new GetSendRecCurrencyTask(getActivity(), this);
            task.execute(currencyRequest);
        } else {
            onMessage(getString(R.string.no_internet));
        }
    }

    @Override
    public void onCurrencyResponse(List<GetSendRecCurrencyResponse> response) {
        if (response.size() == 1) {
            editBeneficiaryRequest.PayOutCurrency = response.get(0).currencyShortName;
        } else {
            //show dialog
            DialogCurrency dialogCurrency = new DialogCurrency(response, this);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            dialogCurrency.show(transaction, "");
        }
    }

    @Override
    public void onCurrencySelect(GetSendRecCurrencyResponse response) {
        editBeneficiaryRequest.PayOutCurrency = response.currencyShortName;
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }

    @Override
    public void onSelectRelation(RelationListResponse relation) {
        binding.relation.setText(relation.relationName);
        editBeneficiaryRequest.CustomerRelation = relation.relationName;
    }

    @Override
    public void onGetNetworkList(List<GetCashNetworkListResponse> networkLists) {
        this.networkLists = new ArrayList<>();
        if (networkLists.size() == 1) {
            editBeneficiaryRequest.PaymentMode = networkLists.get(0).paymentMode;
            editBeneficiaryRequest.PayOutBranchCode = networkLists.get(0).payOutBranchCode;
            binding.agentsForCash.setText(networkLists.get(0).payOutAgent);
        }
        this.networkLists.addAll(networkLists);
    }

    @Override
    public void onSuccess(String s) {
        onMessage(s);
    }
}