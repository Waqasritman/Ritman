package angoothape.wallet.settlementaeps.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import angoothape.wallet.MoneyTransferModuleV.TransactionReceiptActivity;
import angoothape.wallet.MoneyTransferModuleV.viewmodels.BankTransferViewModel;
import angoothape.wallet.R;
import angoothape.wallet.databinding.FragmentAepsSettlementRateBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.GetModeRequest;
import angoothape.wallet.di.JSONdi.restRequest.SimpleRequest;
import angoothape.wallet.di.JSONdi.restRequest.aepsSettlement.AEPSSettlementCommission;
import angoothape.wallet.di.JSONdi.restRequest.aepsSettlement.AEPSSettlementTransactionRequest;
import angoothape.wallet.di.JSONdi.restResponse.PaymentModes;
import angoothape.wallet.di.JSONdi.restResponse.aepssattlement.AEPSBeneficiary;
import angoothape.wallet.di.JSONdi.restResponse.aepssattlement.AEPSClosingBalance;
import angoothape.wallet.di.JSONdi.restResponse.aepssattlement.AEPSSettlementTransaction;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.fragments.BaseFragment;

import angoothape.wallet.interfaces.OnDecisionMade;
import angoothape.wallet.settlementaeps.AEPSSettlementTransactionActivity;
import angoothape.wallet.utils.Utils;

public class AEPSSettlementTransferRate extends BaseFragment<FragmentAepsSettlementRateBinding>
        implements OnDecisionMade {

    BankTransferViewModel viewModel;
    boolean isModeSelected = false;
    PaymentModes selectedPaymentMode;

    AEPSBeneficiary aepsBeneficiary;

    double closingBalance;

    @Override
    public void onResume() {
        super.onResume();
        ((AEPSSettlementTransactionActivity) getBaseActivity()).binding.toolBar.titleTxt
                .setText(getString(R.string.aeps_settlement));
    }

    @Override
    protected void injectView() {

    }

    @Override
    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.sendingAmountField.getText().toString())) {
            onMessage(getString(R.string.please_enter_amount));
            return false;
        } else if (Double.parseDouble(binding.sendingAmountField.getText().toString()) <= 0) {
            onMessage("Please enter valid amount");
            return false;
        }
        return true;
    }


    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(BankTransferViewModel.class);
        assert getArguments() != null;
        aepsBeneficiary = getArguments().getParcelable("bene");
        getPaymentModes();

        binding.processedToPay.setOnClickListener(v -> {
            if (isValidate()) {

                binding.processedToPay.setEnabled(false);

                AEPSSettlementCommission request = new AEPSSettlementCommission();
                request.Payment_Id = selectedPaymentMode.paymentID;
                request.Transfer_Amount = binding.sendingAmountField.getText().toString();


                Bundle bundle = new Bundle();
                bundle.putParcelable("request", request);
                bundle.putString("beneid", String.valueOf(aepsBeneficiary.id));
                bundle.putString("paymentModename", selectedPaymentMode.paymentName);

                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.AEPSSettlementSummaryFragment, bundle);

            }
        });

    }

    void aepsSettlement() {
        Utils.showCustomProgressDialog(getContext(), false);
        AEPSSettlementTransactionRequest settlementTransactionRequest = new AEPSSettlementTransactionRequest();
        settlementTransactionRequest.amount_ = binding.sendingAmountField.getText().toString();
        settlementTransactionRequest.Payment_ID = selectedPaymentMode.paymentID;
        settlementTransactionRequest.Beneficiary_ID = String.valueOf(aepsBeneficiary.id);

        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim();
        String body = RestClient.makeGSONString(settlementTransactionRequest);

        AERequest request = new AERequest();
        request.body = AESHelper.encrypt(body.trim(), gKey.trim());

        ((AEPSSettlementTransactionActivity) getBaseActivity()).viewModel.settlementTransaction(request,
                KeyHelper.getKey(getSessionManager().getMerchantName()).trim(), KeyHelper.getSKey(KeyHelper
                        .getKey(getSessionManager().getMerchantName())).trim()).observe(getViewLifecycleOwner()
                , response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        binding.processedToPay.setEnabled(true);
                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            onMessage(response.resource.description);

                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , gKey);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<AEPSSettlementTransaction>() {
                                }.getType();

                                AEPSSettlementTransaction balance = gson.fromJson(bodyy, type);
                                Handler mHandler;
                                mHandler = new Handler();
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mHandler.removeCallbacks(this);
                                        binding.processedToPay.setEnabled(true);
                                        Intent i = new Intent(getActivity(), TransactionReceiptActivity.class);
                                        i.putExtra("TransactionNumber", balance.txnNo);
                                        startActivity(i);
                                        getBaseActivity().finish();
                                    }
                                }, 200);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            binding.processedToPay.setEnabled(true);
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

    private void getPaymentModes() {
        Utils.showCustomProgressDialog(getContext(), false);
        binding.paymentModesSpinner.setPrompt("Select Payment mode");
        List<PaymentModes> paymentModesList = new ArrayList<>();
        PaymentModes neft = new PaymentModes();
        neft.paymentID = "69";
        neft.paymentName = "NEFT";
        paymentModesList.add(neft);
        neft = new PaymentModes();
        neft.paymentID = "70";
        neft.paymentName = "IMPS";
        paymentModesList.add(neft);
        neft = new PaymentModes();
        neft.paymentID = "71";
        neft.paymentName = "RTGS";

        paymentModesList.add(neft);


        fillSpinner(paymentModesList);
        getClosingBalance();
    }


    void getClosingBalance() {
        SimpleRequest getModeRequest = new SimpleRequest();
        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim();
        String body = RestClient.makeGSONString(getModeRequest);

        AERequest request = new AERequest();
        request.body = AESHelper.encrypt(body.trim(), gKey.trim());
        ((AEPSSettlementTransactionActivity) getBaseActivity()).viewModel.getAEPSClosingBalance(request, KeyHelper.getKey(getSessionManager().getMerchantName()).trim(),
                KeyHelper.getSKey(KeyHelper
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
                            Log.e("getClosingBalance: ", bodyy);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<AEPSClosingBalance>() {
                                }.getType();

                                AEPSClosingBalance balance = gson.fromJson(bodyy, type);
                                closingBalance = Double.parseDouble(balance.AEPSClosingBalance);
                                binding.closingBalance.setText("INR " + balance.AEPSClosingBalance);
                                binding.closingViewLayout.setVisibility(View.VISIBLE);
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

    void fillSpinner(List<PaymentModes> paymentModes) {
        final ArrayAdapter userAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, paymentModes);

        binding.paymentModesSpinner.setAdapter(userAdapter);
        binding.paymentModesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPaymentMode = new PaymentModes();
                isModeSelected = true;
                selectedPaymentMode = (PaymentModes) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_aeps_settlement_rate;
    }


    @Override
    public void onProceed() {

    }

    @Override
    public void onCancel(boolean goBack) {

    }
}
