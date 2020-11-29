package totipay.wallet.MoneyTransferModuleV.banktransfer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import totipay.wallet.MoneyTransferModuleV.MoneyTransferMainLayout;
import totipay.wallet.R;
import totipay.wallet.databinding.FragmentSepaCountryBankBeneBinding;
import totipay.wallet.di.RequestHelper.BeneficiaryAddRequest;
import totipay.wallet.di.ResponseHelper.AddBeneficiaryResponse;
import totipay.wallet.di.apicaller.AddBeneficiaryTask;
import totipay.wallet.fragments.BaseFragment;
import totipay.wallet.interfaces.OnApiResponse;
import totipay.wallet.utils.IsNetworkConnection;

public class SepaCountryBankBeneFragment extends BaseFragment<FragmentSepaCountryBankBeneBinding>
 implements  OnApiResponse {

    BeneficiaryAddRequest request;
    @Override
    public void onResume() {
        super.onResume();
        ((MoneyTransferMainLayout) getBaseActivity()).binding.toolBar.titleTxt
                .setText(getString(R.string.bank_beneficairy));
    }


    @Override
    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.bankName.getText().toString())) {
            onMessage(getString(R.string.select_bank_name));
            return false;
        } else if (TextUtils.isEmpty(binding.ibanNumber.getText().toString())) {
            onMessage(getString(R.string.enter_iban_number));
            return false;
        }

        return true;
    }


    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {

        binding.addBene.setOnClickListener(v -> {
            if (isValidate()) {
                ((MoneyTransferMainLayout) getBaseActivity())
                        .bankTransferViewModel.beneficiaryAddRequest.AccountNumber = binding
                        .ibanNumber.getText().toString();
                request = ((MoneyTransferMainLayout) getActivity())
                        .bankTransferViewModel.beneficiaryAddRequest;
                request.BankCode = binding
                        .ibanNumber.getText().toString();
                request.BankCountry = request.PayoutCountryCode;
                request.BankName = binding.bankName.getText().toString();
                request.customerNo = ((MoneyTransferMainLayout) getBaseActivity())
                        .sessionManager.getCustomerNo();
                request.Address = "sepaempty";
                request.BranchNameAndAddress =  "sepaEmpty";
                request.BankBranch = "sepaEpty";
                request.Telephone = "1234567890"; //dummy
                request.PaymentMode = "bank";
                request.languageId = getSessionManager().getlanguageselection();
                if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                    AddBeneficiaryTask task = new AddBeneficiaryTask(getContext(), this);
                    task.execute(request);
                } else {
                    onMessage(getString(R.string.no_internet));
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_sepa_country_bank_bene;
    }

    @Override
    public void onSuccessResponse(Object response) {
        if (response instanceof AddBeneficiaryResponse) {
            onMessage(getString(R.string.bene_added_successfully));
            request.beneficiaryNo = ((AddBeneficiaryResponse) response).beneficiaryNo;
            ((MoneyTransferMainLayout) getBaseActivity())
                    .bankTransferViewModel.fillBeneficiaryDetails(request);
            Bundle bundle = new Bundle();
            bundle.putParcelable("beneficiary_details", ((MoneyTransferMainLayout) getBaseActivity()).bankTransferViewModel
                    .beneficiaryDetails);
            Navigation.findNavController(getView())
                    .navigate(R.id.action_sepaCountryBankBeneFragment_to_sendMoneyViaBankThirdActivity
                            , bundle);
        }
    }


    @Override
    public void onError(String message) {
        onMessage(message);
    }
}