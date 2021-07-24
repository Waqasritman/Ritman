package ritman.wallet.MoneyTransferModuleV.banktransfer;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.fragment.app.FragmentTransaction;

import ritman.wallet.MoneyTransferModuleV.MoneyTransferMainLayout;
import ritman.wallet.R;
import ritman.wallet.databinding.ActivitySendMoneyViaBankSecondBinding;
import ritman.wallet.di.JSONdi.restResponse.BankListResponse;
import ritman.wallet.di.XMLdi.RequestHelper.BeneficiaryAddRequest;
import ritman.wallet.di.XMLdi.RequestHelper.GetBankNetworkListRequest;
import ritman.wallet.di.XMLdi.ResponseHelper.AddBeneficiaryResponse;
import ritman.wallet.di.XMLdi.ResponseHelper.GetBankNetworkListResponse;
import ritman.wallet.di.XMLdi.apicaller.AddBeneficiaryTask;
import ritman.wallet.di.XMLdi.apicaller.GetBankNetworkListTask;
import ritman.wallet.dialogs.BankNameListDialog;
import ritman.wallet.dialogs.DialogBankNetwork;
import ritman.wallet.fragments.BaseFragment;
import ritman.wallet.interfaces.OnApiResponse;
import ritman.wallet.interfaces.OnGetBankNetworkListTaskInterface;
import ritman.wallet.interfaces.OnSelectBank;
import ritman.wallet.utils.IsNetworkConnection;

import java.util.ArrayList;
import java.util.List;

public class AddBeneficiaryBankTransferBankDetailFragment extends
        BaseFragment<ActivitySendMoneyViaBankSecondBinding>  {

    BeneficiaryAddRequest request;
    List<BankListResponse> bankList;

    @Override
    protected void injectView() {
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MoneyTransferMainLayout) getBaseActivity()).binding.toolBar.titleTxt
                .setText(getString(R.string.bank_beneficairy));
    }


    @Override
    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.bankName.getText().toString())) {
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
        }

        return true;
    }


    public boolean isValidateForBranch() {
        if (TextUtils.isEmpty(binding.bankName.getText().toString())) {
            onMessage(getString(R.string.plz_select_bank));
            return false;
        } else if (TextUtils.isEmpty(binding.cityName.getText().toString())) {
            onMessage(getString(R.string.plz_enter_city));
            return false;
        } else if (TextUtils.isEmpty(binding.location.getText().toString())) {
            onMessage(getString(R.string.enter_location));
            return false;
        }

        return true;
    }


    @Override
    protected void setUp(Bundle savedInstanceState) {
        bankList = new ArrayList<>();
        binding.searchBranch.setOnClickListener(v -> {
            if (isValidateForBranch()) {
                GetBankNetworkListRequest request = new GetBankNetworkListRequest();
                request.countryCode = ((MoneyTransferMainLayout) getBaseActivity()).bankTransferViewModel
                        .beneficiaryAddRequest.PayoutCountryCode;
                request.city = binding.cityName.getText().toString();
                request.bankName = binding.bankName.getText().toString();
                request.branchName = binding.location.getText().toString();
                request.languageId = getSessionManager().getlanguageselection();
//                GetBankNetworkListTask task = new GetBankNetworkListTask(getContext(), this);
//                task.execute(request);
            }
        });


        binding.bankName.setOnClickListener(v -> {
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
//                BankNameListDialog dialog = new BankNameListDialog(((MoneyTransferMainLayout)
//                        getBaseActivity()).bankTransferViewModel
//                        .beneficiaryAddRequest.PayoutCountryCode,
//                        this);
//                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
//                dialog.show(transaction, "");
            } else {
                onMessage(getString(R.string.no_internet));
            }
        });


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
                request.ipAddress = getSessionManager().getIpAddress();
                request.ipCountry = getSessionManager().getIpCountryName();


                if (IsNetworkConnection.checkNetworkConnection(getContext())) {
//                    AddBeneficiaryTask task = new AddBeneficiaryTask(getContext(), this);
//                    task.execute(request);
                } else {
                    onMessage(getString(R.string.no_internet));
                }
            }
        });


        binding.branchName.setOnClickListener(v -> {
//            DialogBankNetwork banks = new DialogBankNetwork(bankList, this);
//            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
//            banks.show(transaction, "");
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_send_money_via_bank_second;
    }

//    /**
//     * getting bank data form country
//     *
//     * @param responseList
//     */
//    @Override
//    public void onSuccess(List<BankListResponse> responseList) {
//        bankList.clear();
//        bankList.addAll(responseList);
//        binding.mainLayout.setVisibility(View.VISIBLE);
//        binding.searchBranch.setVisibility(View.GONE);
//
//        if (bankList != null && bankList.size() == 1) {
//            BankListResponse response = bankList.get(0);
//            binding.mainLayout.setVisibility(View.VISIBLE);
//            binding.searchBranch.setVisibility(View.GONE);
//
//        }
//    }


//    @Override
//    public void onMessageResponse(String message) {
//        onMessage(message);
//    }
//
//
//    @Override
//    public void onSuccessResponse(Object response) {
//        if (response instanceof AddBeneficiaryResponse) {
//            onMessage(getString(R.string.bene_added_successfully));
//            request.beneficiaryNo = ((AddBeneficiaryResponse) response).beneficiaryNo;
//            ((MoneyTransferMainLayout) getBaseActivity())
//                    .bankTransferViewModel.fillBeneficiaryDetails(request);
////            Bundle bundle = new Bundle();
////            bundle.putParcelable("beneficiary_details", ((MoneyTransferMainLayout) getBaseActivity()).bankTransferViewModel
////                    .beneficiaryDetails);
////            Navigation.findNavController(getView())
////                    .navigate(R.id.action_sendMoneyViaBankSecondActivity_to_sendMoneyViaBankThirdActivity
////                            , bundle);
//
//            ((MoneyTransferMainLayout) getBaseActivity()).navController.popBackStack(R.id.selectedBeneficiaryForBankTransferFragment
//                    , false);
//        }
//    }
//
//    @Override
//    public void onError(String message) {
//        onMessage(message);
//    }
//
//    @Override
//    public void onSelectBank(BankListResponse bankDetails) {
//        if (TextUtils.isEmpty(binding.bankName.getText())) {
//            binding.mainLayout.setVisibility(View.VISIBLE);
//            binding.searchBranch.setVisibility(View.GONE);
//        } else {
//            binding.mainLayout.setVisibility(View.GONE);
//            binding.searchBranch.setVisibility(View.VISIBLE);
//        }
//
//        binding.bankName.setText(bankDetails.bankName);
//
//    }
//
//    @Override
//    public void onSelectBankName(String bankName) {
//        binding.bankName.setText(bankName);
//        if (TextUtils.isEmpty(binding.bankName.getText())) {
//            binding.mainLayout.setVisibility(View.VISIBLE);
//            binding.searchBranch.setVisibility(View.GONE);
//        } else {
//            binding.mainLayout.setVisibility(View.GONE);
//            binding.searchBranch.setVisibility(View.VISIBLE);
//        }
//    }
//
//    @Override
//    public void onSelectBranch(BankListResponse branchName) {
//        binding.branchName.setText(branchName.branchName);
//    }

}
