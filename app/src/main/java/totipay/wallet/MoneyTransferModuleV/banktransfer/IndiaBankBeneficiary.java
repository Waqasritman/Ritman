package totipay.wallet.MoneyTransferModuleV.banktransfer;

import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.View;

import java.util.List;

import totipay.wallet.MoneyTransferModuleV.MoneyTransferMainLayout;
import totipay.wallet.R;
import totipay.wallet.databinding.FragmentIndiaBankBeneficiaryBinding;
import totipay.wallet.di.RequestHelper.BeneficiaryAddRequest;
import totipay.wallet.di.RequestHelper.GetBankNetworkListRequest;
import totipay.wallet.di.ResponseHelper.AddBeneficiaryResponse;
import totipay.wallet.di.ResponseHelper.GetBankNetworkListResponse;
import totipay.wallet.di.apicaller.AddBeneficiaryTask;
import totipay.wallet.di.apicaller.GetBankNetworkListTask;
import totipay.wallet.dialogs.DialogBankNetwork;
import totipay.wallet.fragments.BaseFragment;
import totipay.wallet.interfaces.OnApiResponse;
import totipay.wallet.interfaces.OnGetBankNetworkListTaskInterface;
import totipay.wallet.interfaces.OnSelectBank;
import totipay.wallet.utils.IsNetworkConnection;

public class IndiaBankBeneficiary extends BaseFragment<FragmentIndiaBankBeneficiaryBinding>
        implements
        OnGetBankNetworkListTaskInterface, OnApiResponse, OnSelectBank {

    boolean isBankNameSelected = false;
    boolean isFSCSSelected = false;
    BeneficiaryAddRequest request;


    @Override
    public void onResume() {
        super.onResume();
        ((MoneyTransferMainLayout) getBaseActivity()).binding.toolBar.titleTxt
                .setText(getString(R.string.bank_beneficairy));
    }

    @Override
    public boolean isValidate() {
        if (!isBankNameSelected) {
            onMessage(getString(R.string.please_select_bank_error));
            return false;
        } else if (TextUtils.isEmpty(binding.accountNumber.getText().toString())) {
            onMessage(getString(R.string.enter_account_no_error));
            return false;
        } else if (TextUtils.isEmpty(binding.reEnterAccountNumber.getText().toString())) {
            onMessage(getString(R.string.enter_account_no_error));
            return false;
        } else if (!TextUtils.equals(binding.accountNumber.getText().toString(), binding.reEnterAccountNumber.getText().toString())) {
            onMessage(getString(R.string.account_no_same_error));
            return false;
        } else if (!isFSCSSelected) {
            onMessage(getString(R.string.fscs_slect_error));
            return false;
        }
        return true;
    }


    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.pageTitle.setText(getString(R.string.enter_ifsc_code));
        binding.ifscLayoutIndia.setVisibility(View.VISIBLE);


        binding.nextLayout.setOnClickListener(v -> {
            if (isValidate()) {
                ((MoneyTransferMainLayout) getBaseActivity())
                        .bankTransferViewModel.beneficiaryAddRequest.AccountNumber = binding
                        .accountNumber.getText().toString();
                request = ((MoneyTransferMainLayout) getActivity())
                        .bankTransferViewModel.beneficiaryAddRequest;
                request.customerNo = ((MoneyTransferMainLayout) getBaseActivity())
                        .sessionManager.getCustomerNo();
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

        binding.searchBank.setOnClickListener(v -> {
            if (TextUtils.isEmpty(binding.isfsCodeIndia.getText())) {
                onMessage(getString(R.string.enter_IFSC_code_error));
            } else if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                GetBankNetworkListRequest request = new GetBankNetworkListRequest();
                request.countryCode = "ind";
                request.branchIFSC = binding.isfsCodeIndia.getText().toString();
                request.languageId = getSessionManager().getlanguageselection();
                GetBankNetworkListTask task = new GetBankNetworkListTask(getContext(), this);
                task.execute(request);
            } else {
                onMessage(getString(R.string.no_internet));
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_india_bank_beneficiary;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                    .navigate(R.id.action_indiaBankBeneficiary_to_sendMoneyViaBankThirdActivity
                            , bundle);
        }
    }

    @Override
    public void onError(String message) {
        onMessage(message);
    }

    @Override
    public void onSuccess(List<GetBankNetworkListResponse> responseList) {
        if (responseList != null && responseList.size() == 1) {
            GetBankNetworkListResponse response = responseList.get(0);
            binding.ifscLayoutIndia.setVisibility(View.GONE);
            binding.mainLayout.setVisibility(View.VISIBLE);
            binding.indiaBankName.setText(response.bankName);
            binding.isfsCode.setText(response.bankCode);
            binding.isfsCode.setEnabled(false);
            isBankNameSelected = true;
            isFSCSSelected = true;
            ((MoneyTransferMainLayout) getBaseActivity()).bankTransferViewModel
                    .fillBankOf(response);
        } else if (responseList != null) {
            DialogBankNetwork banks = new DialogBankNetwork(responseList, this);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            banks.show(transaction, "");
        }
    }

    @Override
    public void onMessageResponse(String message) {
        onMessage(message);
    }

    @Override
    public void onSelectBank(GetBankNetworkListResponse bankDetails) {
        binding.mainLayout.setVisibility(View.VISIBLE);
        ((MoneyTransferMainLayout) getBaseActivity()).bankTransferViewModel
                .fillBankOf(bankDetails);
        isBankNameSelected = true;
        isFSCSSelected = true;
    }

    @Override
    public void onSelectBankName(String bankName) {

    }

    @Override
    public void onSelectBranch(GetBankNetworkListResponse branchName) {

    }
}