package ritman.wallet.beneficairyRegistration.fragments;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import ritman.wallet.MoneyTransferModuleV.MoneyTransferMainLayout;
import ritman.wallet.R;
import ritman.wallet.beneficairyRegistration.BeneficiaryRegistrationActivity;
import ritman.wallet.beneficairyRegistration.viewmodel.RegisterBeneficiaryViewModel;
import ritman.wallet.databinding.FragmentIndiaBankBeneficiaryBinding;
import ritman.wallet.di.JSONdi.Status;
import ritman.wallet.di.JSONdi.restRequest.BankNetworkListRequest;
import ritman.wallet.di.JSONdi.restRequest.RegisterBeneficiaryRequest;
import ritman.wallet.di.JSONdi.restResponse.BankListResponse;
import ritman.wallet.di.JSONdi.retrofit.RestClient;

import ritman.wallet.dialogs.DialogBankNetwork;
import ritman.wallet.dialogs.SingleButtonMessageBankDialog;
import ritman.wallet.dialogs.SingleButtonMessageDialog;
import ritman.wallet.fragments.BaseFragment;
import ritman.wallet.interfaces.OnDecisionMade;
import ritman.wallet.interfaces.OnSelectBank;
import ritman.wallet.utils.IsNetworkConnection;
import ritman.wallet.utils.Utils;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;


public class IndiaBankBeneficiary extends BaseFragment<FragmentIndiaBankBeneficiaryBinding>
        implements
        OnSelectBank , OnDecisionMade {

    RegisterBeneficiaryViewModel viewModel;
    RegisterBeneficiaryRequest request;
    private AwesomeValidation mAwesomeValidation;
    @Override
    public void onResume() {
        super.onResume();
        if (getBaseActivity() instanceof BeneficiaryRegistrationActivity) {
            ((BeneficiaryRegistrationActivity) getBaseActivity()).binding.toolBar.titleTxt
                    .setText(getString(R.string.bank_beneficairy));
            viewModel = ((BeneficiaryRegistrationActivity)getBaseActivity()).viewModel;
        } else {
            ((MoneyTransferMainLayout) getBaseActivity()).binding.toolBar.titleTxt
                    .setText(getString(R.string.bank_beneficairy));
            viewModel = ((MoneyTransferMainLayout)getBaseActivity()).viewModel;

            binding.isfsCodeIndia.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.background_beneficiary_bank_pos_red));
            binding.indiaBankName.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.background_beneficiary_bank_pos_red));
            binding.branchName.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.background_beneficiary_bank_pos_red));
            binding.searchBank.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.colorPrimary));
            binding.accountNumber.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.background_beneficiary_bank_pos_red));
            binding.reEnterAccountNumber.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.background_beneficiary_bank_pos_red));
            binding.nextLayout.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.colorPrimary));
        }



        viewModel.beneRegister.observe(getViewLifecycleOwner(), registerBeneficiaryRequest -> {
            request = registerBeneficiaryRequest;
        });
    }

    @Override
    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.accountNumber.getText().toString())) {
            //onMessage(getString(R.string.enter_account_no_error));
            binding.accountNumber.setError(getString(R.string.enter_account_no_error));
            return false;
        }
        else if (TextUtils.isEmpty(binding.reEnterAccountNumber.getText().toString())) {
            //onMessage(getString(R.string.enter_account_no_error));
            binding.accountNumber.setError(getString(R.string.enter_account_no_error));
            return false;
        }
        if (!TextUtils.equals(binding.accountNumber.getText().toString(), binding.reEnterAccountNumber.getText().toString())) {
            onMessage(getString(R.string.account_no_same_error));
            return false;
        }
        return true;
    }


    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.pageTitle.setText(getString(R.string.enter_bank_details));
        binding.ifscLayoutIndia.setVisibility(View.VISIBLE);

        mAwesomeValidation=new AwesomeValidation(BASIC);
        mAwesomeValidation.addValidation(binding.accountNumber,  "^[A-Za-z\\s]+", getResources().getString(R.string.enter_account_no_error));
        mAwesomeValidation.addValidation(binding.reEnterAccountNumber,  "^[A-Za-z\\s]+", getResources().getString(R.string.enter_account_no_error));
        if (getBaseActivity() instanceof BeneficiaryRegistrationActivity) {
            binding.nextLayout.setOnClickListener(v -> {
                if (isValidate()) {

                    Utils.showCustomProgressDialog(getContext() , false);


                    request.AccountNumber = binding.accountNumber.getText().toString();
                    viewModel.beneRegister.setValue(request);


                    viewModel.createBeneficairy(getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()
                            , response -> {
                                Utils.hideCustomProgressDialog();
                                if (response.status == Status.ERROR) {
                                    onMessage(getString(response.messageResourceId));
                                } else {
                                    assert response.resource != null;
                                    if (response.resource.responseCode.equals(101)) {
                                        //   onMessage(response.resource.description);
                                        SingleButtonMessageDialog dialog = new
                                                SingleButtonMessageDialog(getString(R.string.successfully_txt)
                                                , getString(R.string.bene_added_successfully), this,
                                                false);
                                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                                        dialog.show(transaction, "");



                                    } else {
                                        onMessage(response.resource.description);
                                    }
                                }
                            });
                }
            });
        }
        else {
            binding.nextLayout.setOnClickListener(v -> {
                if (isValidate()) {

                    Utils.showCustomProgressDialog(getContext() , false);


                    request.AccountNumber = binding.accountNumber.getText().toString();
                    viewModel.beneRegister.setValue(request);


                    viewModel.createBeneficairy(getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()
                            , response -> {
                                Utils.hideCustomProgressDialog();
                                if (response.status == Status.ERROR) {
                                    onMessage(getString(response.messageResourceId));
                                } else {
                                    assert response.resource != null;
                                    if (response.resource.responseCode.equals(101)) {
                                        //   onMessage(response.resource.description);
                                        SingleButtonMessageBankDialog dialog = new
                                                SingleButtonMessageBankDialog(getString(R.string.successfully_txt)
                                                , getString(R.string.bene_added_successfully), this,
                                                false);
                                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                                       dialog.show(transaction, "");



                                    } else {
                                        onMessage(response.resource.description);
                                    }
                                }
                            });
                }
            });
        }


        binding.searchBank.setOnClickListener(v -> {
            int count = 0;
            if (TextUtils.isEmpty(binding.isfsCodeIndia.getText())) {
                count += 1;
            }
            if (TextUtils.isEmpty(binding.indiaBankName.getText().toString())) {
                count += 1;
            }

            if (count < 2) {
                if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                    getBankList();
                } else {
                    onMessage(getString(R.string.no_internet));
                }
            } else {
                onMessage("Enter bank name or ifsc code for bank details");
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


    void getBankList() {
        Utils.showCustomProgressDialog(getContext() , false);
        binding.progressBar.setVisibility(View.GONE);
        binding.searchBank.setVisibility(View.GONE);
        BankNetworkListRequest request = new BankNetworkListRequest();
        request.BankName = binding.indiaBankName.getText().toString();
        request.BranchIFSC = binding.isfsCodeIndia.getText().toString();
        request.BranchName = binding.branchName.getText().toString();

        Call<BankListResponse> call = RestClient.get().getBankNetworkList(RestClient.makeGSONRequestBody(request)
                , getSessionManager().getMerchantName());
        call.enqueue(new retrofit2.Callback<BankListResponse>() {
            @Override
            public void onResponse(Call<BankListResponse> call, Response<BankListResponse> response) {
                binding.progressBar.setVisibility(View.GONE);
                Utils.hideCustomProgressDialog();
                binding.searchBank.setVisibility(View.VISIBLE);

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().responseCode.equals(101)) {
                        showBankList(response.body().data);
                    } else {
                        onMessage(response.body().description);
                    }
                } else {
                    onMessage(getString(R.string.some_thing_wrong));
                }
            }

            @Override
            public void onFailure(Call<BankListResponse> call, Throwable t) {
                onMessage(t.getLocalizedMessage());
                Utils.hideCustomProgressDialog();
                binding.progressBar.setVisibility(View.GONE);
                binding.searchBank.setVisibility(View.VISIBLE);
            }
        });

    }


    void showBankList(List<BankListResponse> responseList) {
        DialogBankNetwork banks = new DialogBankNetwork(responseList, this);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        banks.show(transaction, "");
    }


    @Override
    public void onSelectBank(BankListResponse bankDetails) {
        binding.mainLayout.setVisibility(View.VISIBLE);
        binding.searchBank.setVisibility(View.GONE);
        binding.indiaBankName.setText(bankDetails.bankName);
        binding.branchName.setText(bankDetails.branchName);
        binding.isfsCodeIndia.setText(bankDetails.bankCode);

        request.BankName = bankDetails.bankName;
        request.BankBranch = bankDetails.branchName;
        request.BankCode = bankDetails.bankCode;
        request.PaymentMode = "bank";
        request.BranchNameAndAddress = bankDetails.bankAddress;

    }

    @Override
    public void onSelectBankName(String bankName) {

    }

    @Override
    public void onSelectBranch(BankListResponse branchName) {

    }


    @Override
    public void showProgress() {
        super.showProgress();
    }


    @Override
    public void hideProgress() {
        super.hideProgress();
    }

    @Override
    public void onProceed() {
        Navigation.findNavController(binding.getRoot())
                .popBackStack(R.id.selectBeneficiaryFragment
                        , false);
    }

    @Override
    public void onCancel() {

    }
}