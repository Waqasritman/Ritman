package angoothape.wallet.MoneyTransferModuleV.cashtransfer_doortodoor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;

import angoothape.wallet.MoneyTransferModuleV.MoneyTransferMainLayout;
import angoothape.wallet.MoneyTransferModuleV.TransactionReceiptActivity;
import angoothape.wallet.MoneyTransferModuleV.viewmodels.BankTransferViewModel;
import angoothape.wallet.R;
import angoothape.wallet.beneficairyRegistration.BeneficiaryRegistrationActivity;
import angoothape.wallet.databinding.FragmentMoneyTransferSummaryBinding;

import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.RitmanPaySendRequest;
import angoothape.wallet.di.JSONdi.restRequest.VerifyOtpRequest;
import angoothape.wallet.di.JSONdi.restResponse.PaymentModes;
import angoothape.wallet.di.JSONdi.restResponse.RitmanPaySendResponse;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.di.XMLdi.ResponseHelper.GetBeneficiaryListResponse;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.utils.SessionManager;
import angoothape.wallet.utils.Utils;

public class CashTransferSummaryFragment extends BaseFragment<FragmentMoneyTransferSummaryBinding> {

    String totalPayable, customerNo;
    boolean isCash = true;
    public SessionManager sessionManager;
    GetBeneficiaryListResponse benedetails;
    Double PayInAmount;
    BankTransferViewModel viewModel;
    String TransactionNumber;
    PaymentModes selectedPaymentMode;

    @Override
    public void onResume() {
        super.onResume();
        if (getBaseActivity() instanceof BeneficiaryRegistrationActivity) {
            ((BeneficiaryRegistrationActivity) getBaseActivity()).binding.toolBar.titleTxt
                    .setText(getString(R.string.summary));
        } else {
            ((MoneyTransferMainLayout) getBaseActivity()).binding.toolBar.titleTxt
                    .setText(getString(R.string.summary));

            binding.confirmBtn.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.colorPrimary));
        }

    }


    @Override
    protected void injectView() {

    }


    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(BankTransferViewModel.class);
        assert getArguments() != null;
        totalPayable = getArguments().getString("total_payable");
        isCash = getArguments().getBoolean("is_from_cash", true);
        selectedPaymentMode = getArguments().getParcelable("payment_mode");
        benedetails = getArguments().getParcelable("bene");
        customerNo = getArguments().getString("customer_no");
        PayInAmount = getArguments().getDouble("PayInAmount");

        binding.totalPayableAmount.setText(totalPayable);
        binding.transferAmount.setText(String.valueOf(PayInAmount));

        binding.beneficairyName.setText(benedetails.firstName.concat("  ").concat(benedetails.lastName));
        binding.sendingCurrency.setText("INR");

        binding.paymentMode.setText(selectedPaymentMode.paymentName);

        binding.confirmBtn.setOnClickListener(v -> {
            getSummary();
        });
    }


    public void getSummary() {
        binding.confirmBtn.setEnabled(false);
        Utils.showCustomProgressDialog(getContext(), false);
        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim();

        RitmanPaySendRequest ritmanPaySendRequest = new RitmanPaySendRequest();
        ritmanPaySendRequest.Customer_No = benedetails.customerNo;
        ritmanPaySendRequest.PayOutCurrency = "INR";
        ritmanPaySendRequest.TransferAmount = PayInAmount;
        ritmanPaySendRequest.Payin_Currency = "INR";
        ritmanPaySendRequest.Beneficiary_No = benedetails.beneficiaryNumber;
        String body = RestClient.makeGSONString(ritmanPaySendRequest);
        Log.e("getSummary: ", body);
        AERequest request = new AERequest();
        request.body = AESHelper.encrypt(body.trim(), gKey.trim());

        viewModel.getSummary(request, KeyHelper.getKey(getSessionManager().getMerchantName()).trim(),
                KeyHelper.getSKey(KeyHelper
                        .getKey(getSessionManager().getMerchantName())).trim()).observe(getViewLifecycleOwner()
                , response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        binding.confirmBtn.setEnabled(true);
                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;

                        if (response.resource.responseCode.equals(101)) {

                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , gKey);

                            Log.e("getSummary: ", body);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<RitmanPaySendResponse>() {
                                }.getType();
                                RitmanPaySendResponse data = gson.fromJson(bodyy, type);
                                // TransactionNumber = data.getTransactionNumber();
                                Handler mHandler;
                                mHandler = new Handler();
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mHandler.removeCallbacks(this);
                                        binding.confirmBtn.setEnabled(true);
                                        Log.e("getSummary: ", data.getTransactionNumber());
                                        Intent i = new Intent(getActivity(), TransactionReceiptActivity.class);
                                        i.putExtra("TransactionNumber", data.getTransactionNumber());
                                        startActivity(i);
                                        getBaseActivity().finish();
                                    }
                                }, 200);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            binding.confirmBtn.setEnabled(true);
                            Utils.hideCustomProgressDialog();
                            if (response.resource.data != null) {
                                String bodyy = AESHelper.decrypt(response.resource.data.body
                                        , gKey);
                                Log.e("getBillDetails: ", bodyy);
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


    @Override
    public int getLayoutId() {
        return R.layout.fragment_money_transfer_summary;
    }

}