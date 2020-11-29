package totipay.wallet.MoneyTransferModuleV.banktransfer;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import totipay.wallet.MoneyTransferModuleV.MoneyTransferMainLayout;
import totipay.wallet.R;
import totipay.wallet.databinding.ActivitySendMoneyViaBankFirstBinding;
import totipay.wallet.di.RequestHelper.GetSendRecCurrencyRequest;
import totipay.wallet.di.ResponseHelper.GetCountryListResponse;
import totipay.wallet.di.ResponseHelper.GetRelationListResponse;
import totipay.wallet.di.ResponseHelper.GetSendRecCurrencyResponse;
import totipay.wallet.di.apicaller.GetSendRecCurrencyTask;
import totipay.wallet.dialogs.DialogCountry;
import totipay.wallet.dialogs.DialogCurrency;
import totipay.wallet.dialogs.DialogRelationList;
import totipay.wallet.fragments.BaseFragment;
import totipay.wallet.interfaces.OnSelectCountry;
import totipay.wallet.interfaces.OnSelectCurrency;
import totipay.wallet.interfaces.OnSelectRelation;
import totipay.wallet.utils.CountrySelector;
import totipay.wallet.utils.IsNetworkConnection;

import java.util.List;

import totipay.wallet.utils.CountryParser;

public class AddBeneficiaryBankTransferPersonalDetailFragment extends BaseFragment<ActivitySendMoneyViaBankFirstBinding>
        implements OnSelectCountry, OnSelectRelation, OnSelectCurrency {

    boolean isRelationSelected = false;
    boolean isCountrySelected = false;

    @Override
    protected void injectView() {

    }


    @Override
    public void onResume() {
        super.onResume();
        ((MoneyTransferMainLayout) getBaseActivity()).binding.toolBar.titleTxt
                .setText(getString(R.string.bank_beneficairy));
    }

    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.firstName.getText().toString())) {
            onMessage(getString(R.string.enter_name_bene__first_name_error));
            return false;
        } else if (TextUtils.isEmpty(binding.lastName.getText().toString())) {
            onMessage(getString(R.string.enter_name_bene__last_name_error));
            return false;
        } else if (TextUtils.isEmpty(binding.selectCountry.getText().toString())) {
            onMessage(getString(R.string.plz_select_country_error));
            return isCountrySelected;
        } else if (TextUtils.isEmpty(binding.selectRelation.getText().toString())) {
            onMessage(getString(R.string.plz_select_relation));
            return isRelationSelected;
        }

//        else if ((CheckValidation.isValidName(
//                binding.beneficairyName.getText().toString()))) {
//            onMessage(getString(R.string.bene_name_special_character_not_allowed));
//            return false;
//        }
        return true;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {

        binding.nextLayout.setOnClickListener(v -> {
            if (isValidate()) {
                ((MoneyTransferMainLayout) getBaseActivity())
                        .bankTransferViewModel.beneficiaryAddRequest.FirstName = binding.firstName
                        .getText().toString();
                ((MoneyTransferMainLayout) getBaseActivity())
                        .bankTransferViewModel.beneficiaryAddRequest.LastName = binding.lastName
                        .getText().toString();
                ((MoneyTransferMainLayout) getBaseActivity())
                        .bankTransferViewModel.beneficiaryAddRequest.MiddleName = "";

                if (((MoneyTransferMainLayout) getBaseActivity()).bankTransferViewModel
                        .beneficiaryAddRequest.beneficiaryCountryId == CountrySelector.COUNTRY_ID_INDIA) {
                    Navigation.findNavController(v)
                            .navigate(R.id.action_sendMoneyViaBankFirstActivity_to_indiaBankBeneficiary);
                } else if (((MoneyTransferMainLayout) getBaseActivity()).bankTransferViewModel
                        .beneficiaryAddRequest.countryRegion == 1) {
                    Navigation.findNavController(binding.getRoot())
                            .navigate(R.id.action_sendMoneyViaBankFirstActivity_to_sepaCountryBankBeneFragment);
                } else {
                    Navigation.findNavController(v)
                            .navigate(R.id.action_sendMoneyViaBankFirstActivity_to_sendMoneyViaBankSecondActivity);
                }
            }
        });


        binding.selectCountry.setOnClickListener(v -> {
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                DialogCountry country = new DialogCountry(CountryParser.RECEIVE, false,
                        this);
                FragmentTransaction fm = getParentFragmentManager().beginTransaction();
                country.show(fm, "");
            } else {
                onMessage(getString(R.string.no_internet));
            }
        });


        binding.selectRelation.setOnClickListener(v -> {
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                DialogRelationList relationList = new DialogRelationList(this);
                FragmentTransaction fm = getParentFragmentManager().beginTransaction();
                relationList.show(fm, "");
            } else {
                onMessage(getString(R.string.no_internet));
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_send_money_via_bank_first;
    }

    @Override
    public void onSelectCountry(GetCountryListResponse country) {
        isCountrySelected = true;
        binding.selectCountry.setText(country.countryName);

        ((MoneyTransferMainLayout) getBaseActivity()).bankTransferViewModel.beneficiaryAddRequest
                .PayoutCountryCode = country.countryShortName;
        ((MoneyTransferMainLayout) getBaseActivity()).bankTransferViewModel
                .beneficiaryAddRequest.beneficiaryCountryId = country.id;
        ((MoneyTransferMainLayout) getBaseActivity()).bankTransferViewModel
                .beneficiaryAddRequest.countryRegion = country.countryOrigin;

        GetSendRecCurrencyRequest request = new GetSendRecCurrencyRequest();
        request.countryType = country.countryType;
        request.countryShortName = country.countryShortName;
        request.languageId = getSessionManager().getlanguageselection();
        if (IsNetworkConnection.checkNetworkConnection(getActivity())) {
            GetSendRecCurrencyTask task = new GetSendRecCurrencyTask(getActivity(), this);
            task.execute(request);
        } else {
            onMessage(getString(R.string.no_internet));
        }
    }

    @Override
    public void onSelectRelation(GetRelationListResponse relation) {
        isRelationSelected = true;
        binding.selectRelation.setText(relation.relationName);
        ((MoneyTransferMainLayout) getBaseActivity()).bankTransferViewModel.beneficiaryAddRequest
                .CustomerRelation
                = relation.relationName;

    }


    @Override
    public void onCurrencyResponse(List<GetSendRecCurrencyResponse> response) {
        if (response.size() == 1) {
            ((MoneyTransferMainLayout) getBaseActivity()).bankTransferViewModel.beneficiaryAddRequest
                    .PayOutCurrency = response.get(0).currencyShortName;
        } else {
            //show dialog
            DialogCurrency dialogCurrency = new DialogCurrency(response, this);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            dialogCurrency.show(transaction, "");
        }
    }

    @Override
    public void onCurrencySelect(GetSendRecCurrencyResponse response) {
        ((MoneyTransferMainLayout) getBaseActivity()).bankTransferViewModel.beneficiaryAddRequest
                .PayOutCurrency = response.currencyShortName;
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }
}
