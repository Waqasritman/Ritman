package ritman.wallet.MoneyTransferModuleV.cashtransfer_doortodoor;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import ritman.wallet.MoneyTransferModuleV.MoneyTransferMainLayout;
import ritman.wallet.R;
import ritman.wallet.databinding.ActivityCashPickupFirstBinding;
import ritman.wallet.di.JSONdi.restResponse.RelationListResponse;
import ritman.wallet.di.XMLdi.RequestHelper.BeneficiaryAddRequest;
import ritman.wallet.di.XMLdi.ResponseHelper.GetCountryListResponse;
import ritman.wallet.di.XMLdi.ResponseHelper.GetRelationListResponse;
import ritman.wallet.di.XMLdi.ResponseHelper.GetSendRecCurrencyResponse;
import ritman.wallet.dialogs.DialogCountry;
import ritman.wallet.dialogs.DialogCurrency;
import ritman.wallet.dialogs.DialogRelationList;
import ritman.wallet.fragments.BaseFragment;
import ritman.wallet.interfaces.OnSelectCountry;
import ritman.wallet.interfaces.OnSelectCurrency;
import ritman.wallet.interfaces.OnSelectRelation;
import ritman.wallet.utils.CheckValidation;
import ritman.wallet.utils.StringHelper;

import java.util.List;

public class CashPickUpBeneficiaryPersonalDetailsFragment extends BaseFragment<ActivityCashPickupFirstBinding>
        implements OnSelectCountry, OnSelectRelation, OnSelectCurrency {


    boolean isCashTransfer = false;
    BeneficiaryAddRequest request;

    @Override
    protected void injectView() {

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MoneyTransferMainLayout) getBaseActivity()).binding.toolBar.titleTxt
                .setText(getString(R.string.cash_beneficiary));
    }

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
//        } else if (CheckValidation.isValidName(
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
    protected void setUp(Bundle savedInstanceState) {
        request = new BeneficiaryAddRequest();
        if (getArguments() != null) {
            isCashTransfer = getArguments().getBoolean("is_cash_transfer", false);
        }

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
            if (isValidate()) {
                request.FirstName = binding.firstName.getText().toString();
                request.LastName = binding.lastName.getText().toString();
                request.MiddleName = binding.middleName.getText().toString();
                request.customerNo = getSessionManager().getCustomerNo();
                request.Telephone = binding.mobileNumber.getText().toString();
                request.Address = binding.address.getText().toString();
                request.languageId = getSessionManager().getlanguageselection();
                ((MoneyTransferMainLayout) getBaseActivity())
                        .bankTransferViewModel.beneficiaryAddRequest = request;
                Navigation.findNavController(getView())
                        .navigate(R.id.action_cashPickupFirstActivity_to_cashPickupSecondActivity);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_cash_pickup_first;
    }

    @Override
    public void onSelectCountry(GetCountryListResponse country) {
        binding.country.setText(country.countryName);

        request.PayoutCountryCode = country.countryShortName;
        request.BankCountry = country.countryShortName;
        request.languageId = getSessionManager().getlanguageselection();
        request.countryID = country.id;
        request.PayOutCurrency = country.currencyShortName;
//        GetSendRecCurrencyRequest currencyRequest = new GetSendRecCurrencyRequest();
//        currencyRequest.countryType = country.countryType;
//        currencyRequest.countryShortName = country.countryShortName;
//        currencyRequest.languageId = getSessionManager().getlanguageselection();
//        if (IsNetworkConnection.checkNetworkConnection(getActivity())) {
//            GetSendRecCurrencyTask task = new GetSendRecCurrencyTask(getActivity(), this);
//            task.execute(currencyRequest);
//        } else {
//            onMessage(getString(R.string.no_internet));
//        }
    }

    @Override
    public void onSelectRelation(RelationListResponse relation) {
        binding.relation.setText(relation.relationName);
        request.CustomerRelation = relation.relationName;
    }

    @Override
    public void onCurrencyResponse(List<GetSendRecCurrencyResponse> response) {
        if (response.size() == 1) {
            request.PayOutCurrency = response.get(0).currencyShortName;
        } else {
            //show dialog
            DialogCurrency dialogCurrency = new DialogCurrency(response, this);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            dialogCurrency.show(transaction, "");
        }
    }

    /**
     * if currencies are more than 1
     *
     * @param response
     */
    @Override
    public void onCurrencySelect(GetSendRecCurrencyResponse response) {
        request.PayOutCurrency = response.currencyShortName;
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }

}
