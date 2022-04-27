package angoothape.wallet.beneficairyRegistration.fragments;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.aepsSettlement.AEPSAddBeneficiaryRequest;
import angoothape.wallet.di.JSONdi.restResponse.AEResponse;
import angoothape.wallet.di.JSONdi.restResponse.VerifyBeneficiary;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.settlementaeps.AEPSSettlementTransactionActivity;
import angoothape.wallet.settlementaeps.viewmodels.AEPSSettlementViewModel;
import retrofit2.Call;
import retrofit2.Response;
import angoothape.wallet.MoneyTransferModuleV.MoneyTransferMainLayout;
import angoothape.wallet.R;
import angoothape.wallet.beneficairyRegistration.BeneficiaryRegistrationActivity;
import angoothape.wallet.beneficairyRegistration.viewmodel.RegisterBeneficiaryViewModel;
import angoothape.wallet.databinding.ActivitySendMoneyViaBankFirstBinding;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.BankNetworkListRequest;
import angoothape.wallet.di.JSONdi.restRequest.RegisterBeneficiaryRequest;
import angoothape.wallet.di.JSONdi.restRequest.VerifyBeneficiaryRequest;
import angoothape.wallet.di.JSONdi.restResponse.BankListResponse;
import angoothape.wallet.di.JSONdi.restResponse.BanksList;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.di.XMLdi.ResponseHelper.GetBeneficiaryListResponse;
import angoothape.wallet.dialogs.DialogBanks;
import angoothape.wallet.dialogs.SingleButtonMessageDialog;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.interfaces.OnDecisionMade;
import angoothape.wallet.interfaces.OnSelectBank;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import angoothape.wallet.utils.Utils;


public class AddBeneficiaryBankTransferPersonalDetailFragment
        extends BaseFragment<ActivitySendMoneyViaBankFirstBinding> implements OnSelectBank
        , OnDecisionMade {

    RegisterBeneficiaryViewModel viewModel;
    AEPSSettlementViewModel aepsSettlementViewModel;
    RegisterBeneficiaryRequest request;
    List<BanksList> banksList;
    boolean isDMTLive = false;
    boolean isAEPSBene = false;
    GetBeneficiaryListResponse benedetails;
    AEPSAddBeneficiaryRequest aepsAddBeneficiaryRequest;

    @Override
    protected void injectView() {

    }

    @Override
    public void onResume() {
        super.onResume();

        if (getBaseActivity() instanceof BeneficiaryRegistrationActivity) {
            ((BeneficiaryRegistrationActivity) getBaseActivity()).binding.toolBar.titleTxt
                    .setText(getString(R.string.bank_beneficairy));
            viewModel = ((BeneficiaryRegistrationActivity) getBaseActivity()).viewModel;
            viewModel.beneRegister.observe(this
                    , registerBeneficiaryRequest -> {
                        binding.firstName.setText(registerBeneficiaryRequest.FirstName);
                        binding.merchaentNumber.setText(registerBeneficiaryRequest.CustomerNo);
                    });
        } else if (getBaseActivity() instanceof AEPSSettlementTransactionActivity) {
            ((AEPSSettlementTransactionActivity) getBaseActivity()).binding.toolBar.titleTxt
                    .setText(getString(R.string.aeps_beneficiary));
            aepsSettlementViewModel = ((AEPSSettlementTransactionActivity) getBaseActivity()).viewModel;

        } else {
            ((MoneyTransferMainLayout) getBaseActivity()).binding.toolBar.titleTxt
                    .setText(getString(R.string.bank_beneficairy));
            viewModel = ((MoneyTransferMainLayout) getBaseActivity()).viewModel;
            viewModel.beneRegister.observe(this
                    , registerBeneficiaryRequest -> {
                        binding.firstName.setText(registerBeneficiaryRequest.FirstName);
                        binding.merchaentNumber.setText(registerBeneficiaryRequest.CustomerNo);
                    });

        }
        binding.merchaentNumber.setBackground(ContextCompat.getDrawable(getBaseActivity(), R.drawable.background_beneficiary_bank_pos_red));
        binding.firstName.setBackground(ContextCompat.getDrawable(getBaseActivity(),
                R.drawable.background_beneficiary_bank_pos_red));
        binding.nextLayout.setBackgroundTintList(getBaseActivity().getResources()
                .getColorStateList(R.color.colorPrimary));
        binding.merchaentNumber.setBackground(ContextCompat.getDrawable(getBaseActivity(), R.drawable.background_beneficiary_bank_pos_red));
        binding.merchaentNumber.setBackground(ContextCompat.getDrawable(getBaseActivity(), R.drawable.background_beneficiary_bank_pos_red));
    }

    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.merchaentNumber.getText().toString()) && !isAEPSBene) {
            onMessage(getString(R.string.enter_merchant_number));
            return false;
        } else if (TextUtils.isEmpty(binding.firstName.getText().toString())) {
            onMessage(getString(R.string.enter_name_bene__first_name_error));
            return false;
        } else if (TextUtils.isEmpty(binding.indiaBankName.getText().toString())) {
            onMessage(getString(R.string.select_bank));
            return false;
        } else if (TextUtils.isEmpty(binding.accountNumber.getText().toString())) {
            //onMessage(getString(R.string.enter_account_no_error));
            onMessage(getString(R.string.enter_account_no_error));
            return false;
        }
        return true;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        banksList = new ArrayList<>();
        request = new RegisterBeneficiaryRequest();
        benedetails = new GetBeneficiaryListResponse();
        aepsAddBeneficiaryRequest = new AEPSAddBeneficiaryRequest();
        assert getArguments() != null;
        binding.merchaentNumber.setText(getArguments().getString("customer_no"));
        isDMTLive = getArguments().getBoolean("isDMTLive", false);
        isAEPSBene = getArguments().getBoolean("isAEPSBene", false);
        binding.merchaentNumber.setEnabled(false);

        binding.nextLayout.setOnClickListener(v -> {
            if (isValidate()) {
                if (!isAEPSBene) {
                    request.FirstName = binding.firstName.getText().toString();
                    request.title = binding.beneficairyTitle.getSelectedItem().toString();
                    request.CustomerNo = binding.merchaentNumber.getText().toString();
                    request.AccountNumber = binding.accountNumber.getText().toString();
                    viewModel.beneRegister.setValue(request);

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("bene", viewModel.beneRegister.getValue());

                    Navigation.findNavController(binding.getRoot())
                            .navigate(R.id.action_sendMoneyViaBankFirstActivity_to_beneficiaryOTPRegistration
                                    , bundle);
                } else {
                    createAEPSBeneficiary();
                }


            }
        });


        binding.indiaBankName.setOnClickListener(v -> {
            if (banksList.isEmpty()) {
                getBankList();
            } else {
                showBankList(banksList);
            }
        });

    }

    public void createAEPSBeneficiary() {
        if (isValidate()) {
            binding.nextLayout.setEnabled(false);
            Utils.showCustomProgressDialog(getContext(), false);

            aepsAddBeneficiaryRequest.Beneficiary_Name = binding.firstName.getText().toString();
            aepsAddBeneficiaryRequest.Beneficiary_Account_No = binding.accountNumber.getText().toString();
            String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                    .getKey(getSessionManager().getMerchantName())).trim();
            String body = RestClient.makeGSONString(aepsAddBeneficiaryRequest);
            Log.e("body", body);
            AERequest request = new AERequest();
            request.body = AESHelper.encrypt(body.trim(), gKey.trim());

            aepsSettlementViewModel.addAEPSBeneficiary(request
                    , KeyHelper.getKey(getSessionManager().getMerchantName()).trim(), KeyHelper.getSKey(KeyHelper
                            .getKey(getSessionManager().getMerchantName())).trim()).observe(getViewLifecycleOwner()
                    , response -> {
                        Utils.hideCustomProgressDialog();
                        binding.nextLayout.setEnabled(true);
                        if (response.status == Status.ERROR) {
                            onError(getString(response.messageResourceId));
                        } else {
                            assert response.resource != null;
                            if (response.resource.responseCode.equals(101)) {
                                onMessage("Beneficiary Added Successfully");
                                Navigation.findNavController(binding.getRoot())
                                        .navigateUp();
                            } else {

                                Utils.hideCustomProgressDialog();
                                if (response.resource.data != null) {
                                    String bodyy = AESHelper.decrypt(response.resource.data.body
                                            , gKey);
                                    if (!body.isEmpty()) {
                                        onError(bodyy);
                                    } else {
                                        onError(response.resource.description);
                                    }
                                } else {
                                    onError(response.resource.description);
                                }

                            }


                        }
                    });
        }

    }

    public void regiterBene() {
        if (isValidate()) {
            Utils.showCustomProgressDialog(getContext(), false);
            request.otp = "111111";
            //   viewModel.beneRegister.setValue(registerBeneficiaryRequest);

            String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                    .getKey(getSessionManager().getMerchantName())).trim();
            String body = RestClient.makeGSONString(request);
            Log.e("body", body);
            AERequest request = new AERequest();
            request.body = AESHelper.encrypt(body.trim(), gKey.trim());


            viewModel.createBeneficairy(request
                    , KeyHelper.getKey(getSessionManager().getMerchantName()).trim(), KeyHelper.getSKey(KeyHelper
                            .getKey(getSessionManager().getMerchantName())).trim()).observe(getViewLifecycleOwner()
                    , response -> {
                        Utils.hideCustomProgressDialog();
                        if (response.status == Status.ERROR) {
                            onError(getString(response.messageResourceId));
                        } else {
                            assert response.resource != null;


                            if (response.resource.responseCode.equals(101)) {
                                String bodyy = AESHelper.decrypt(response.resource.data.body
                                        , gKey);


                                try {
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<List<GetBeneficiaryListResponse>>() {
                                    }.getType();
                                    List<GetBeneficiaryListResponse> list = gson.fromJson(bodyy, type);
                                    this.benedetails = list.get(0);
                                    SingleButtonMessageDialog dialog = new
                                            SingleButtonMessageDialog(getString(R.string.successfully_txt)
                                            , getString(R.string.bene_added_successfully), "Verify With transaction", "Verify without transaction", this,
                                            false);
                                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                                    dialog.show(transaction, "");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Utils.hideCustomProgressDialog();
                                if (response.resource.data != null) {
                                    String bodyy = AESHelper.decrypt(response.resource.data.body
                                            , gKey);
                                    if (!body.isEmpty()) {
                                        onError(bodyy);
                                    } else {
                                        onError(response.resource.description);
                                    }
                                } else {
                                    onError(response.resource.description);
                                }
                            }


                        }
                    });
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_send_money_via_bank_first;
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }

    void getBankList() {
        Utils.showCustomProgressDialog(getContext(), false);
        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim();
        BankNetworkListRequest request = new BankNetworkListRequest();
        String body = RestClient.makeGSONString(request);
        AERequest aeRequest = new AERequest();
        aeRequest.body = AESHelper.encrypt(body.trim(), gKey.trim());
        Call<AEResponse> call = RestClient.getEKYC().getBanks(RestClient.makeGSONRequestBody(aeRequest)
                , KeyHelper.getKey(getSessionManager().getMerchantName()).trim(), KeyHelper.getSKey(KeyHelper
                        .getKey(getSessionManager().getMerchantName())).trim());
        call.enqueue(new retrofit2.Callback<AEResponse>() {
            @Override
            public void onResponse(Call<AEResponse> call, Response<AEResponse> response) {
                Utils.hideCustomProgressDialog();

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().responseCode.equals(101)) {

                        String bodyy = AESHelper.decrypt(response.body().data.body
                                , gKey);
                        try {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<BanksList>>() {
                            }.getType();
                            List<BanksList> list = gson.fromJson(bodyy, type);
                            showBankList(list);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Utils.hideCustomProgressDialog();
                        if (response.body().data != null) {
                            String bodyy = AESHelper.decrypt(response.body().data.body
                                    , gKey);
                            if (!body.isEmpty()) {
                                onError(bodyy);
                            } else {
                                onError(response.body().description);
                            }
                        } else {
                            onError(response.body().description);
                        }
                    }
                } else {
                    onMessage(getString(R.string.some_thing_wrong));
                }
            }

            @Override
            public void onFailure(Call<AEResponse> call, Throwable t) {
                onMessage(t.getLocalizedMessage());
                Utils.hideCustomProgressDialog();
            }
        });


    }

    void showBankList(List<BanksList> responseList) {
        DialogBanks banks = new DialogBanks(responseList, this);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        banks.show(transaction, "");
    }

    @Override
    public void onSelectBank(BankListResponse bankDetails) {

    }

    @Override
    public void onSelectBank(BanksList bankDetails) {
        binding.indiaBankName.setText(bankDetails.bank_name);
        if (isAEPSBene) {
            aepsAddBeneficiaryRequest.Beneficiary_IFSC_Code = bankDetails.ifscCode;
        } else {
            request.BankCode = bankDetails.ifscCode;
            request.BranchNameAndAddress = bankDetails.bank_name;
            request.BankBranch = bankDetails.bank_name;
            request.BankName = bankDetails.bank_name;
        }


    }

    @Override
    public void onSelectBankName(String bankName) {

    }

    @Override
    public void onSelectBranch(BankListResponse branchName) {

    }

    @Override
    public void onProceed() {
        verifyBeneficiary(benedetails.beneficiaryNumber, benedetails.customerNo);
    }

    @Override
    public void onCancel(boolean goBack) {
        if (goBack) {
            Navigation.findNavController(binding.getRoot())
                    .navigateUp();
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("customer_no", benedetails.customerNo);
            bundle.putParcelable("bene", benedetails); //bene add
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_sendMoneyViaBankFirstActivity_to_bank_tranfer_navigation
                            , bundle);
        }
    }

    private void verifyBeneficiary(String beneNo, String customerNo) {
        Utils.showCustomProgressDialog(getContext(), false);
        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim();

        VerifyBeneficiaryRequest verifyBeneficiaryRequest = new VerifyBeneficiaryRequest();
        verifyBeneficiaryRequest.Credentials.LanguageID = Integer.parseInt(
                getSessionManager().getlanguageselection());
        verifyBeneficiaryRequest.customerNo = customerNo;
        verifyBeneficiaryRequest.BeneficiaryNo = beneNo;
        String body = RestClient.makeGSONString(verifyBeneficiaryRequest);
        Log.e("verifyBeneficiary: ", body);
        AERequest request = new AERequest();
        request.body = AESHelper.encrypt(body.trim(), gKey.trim());

        viewModel.verifyBeneficairy(request, KeyHelper.getKey(getSessionManager().getMerchantName()).trim(),
                KeyHelper.getSKey(KeyHelper
                        .getKey(getSessionManager().getMerchantName())))
                .observe(getViewLifecycleOwner(), response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {

                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , gKey);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<VerifyBeneficiary>() {
                                }.getType();
                                VerifyBeneficiary data = gson.fromJson(bodyy, type);

                                Bundle bundle = new Bundle();
                                bundle.putParcelable("verified_bene", data.beneficiaryData.details);
                                bundle.putString("customer_no", benedetails.customerNo);
                                bundle.putParcelable("bene", benedetails); //bene add
                                Navigation.findNavController(binding.getRoot())
                                        .navigate(R.id.confirmBeneficiary
                                                , bundle);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            showSuccess(response.resource.description
                                    , "Error", true);
                            //   onMessage("Beneficiary verification failed");
                        }
                    }
                });
    }

    private void showSuccess(String message, String title, boolean isError) {
        SingleButtonMessageDialog dialog = new
                SingleButtonMessageDialog(title
                , message, this,
                false, isError);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }
}
