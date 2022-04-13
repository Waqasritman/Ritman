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
import angoothape.wallet.databinding.GenerateOtpFragmentLayoutBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.GetOtpRequest;
import angoothape.wallet.di.JSONdi.restRequest.RitmanPaySendRequest;
import angoothape.wallet.di.JSONdi.restRequest.VerifyOtpRequest;
import angoothape.wallet.di.JSONdi.restResponse.RitmanPaySendResponse;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.di.XMLdi.ResponseHelper.GetBeneficiaryListResponse;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.utils.Utils;

public class GenerateOtpFragment extends BaseFragment<GenerateOtpFragmentLayoutBinding> {
    GetBeneficiaryListResponse benedetails;
    BankTransferViewModel viewModel;
    String TransactionNumber, OptSend, Verify, customerNo;
    Double PayInAmount;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(BankTransferViewModel.class);
        PayInAmount = getArguments().getDouble("PayInAmount");
        benedetails = getArguments().getParcelable("benedetails");

        if (getBaseActivity() instanceof BeneficiaryRegistrationActivity) {
            ((BeneficiaryRegistrationActivity) getBaseActivity()).binding.toolBar.titleTxt
                    .setText("OTP Verification");
        } else {
            ((MoneyTransferMainLayout) getBaseActivity()).binding.toolBar.titleTxt
                    .setText("OTP Verification");

        }

        getOtp();

        binding.btnVerifyOtp.setOnClickListener(v -> {
            if (binding.edtOtp.getText().toString().equals("")) {
                onMessage("Please enter your OTP");
            } else if (binding.edtOtp.getText().toString().length() != 6) {
                onMessage("Invalid OTP. Please enter correct OTP");
            } else {
                VerifyOtp();
            }


        });


        binding.txtResendOtp.setOnClickListener(v -> {

            getOtp();

        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.generate_otp_fragment_layout;
    }

    public void getOtp() {
        Utils.showCustomProgressDialog(getContext(), false);
        GetOtpRequest request = new GetOtpRequest();
        request.CustomerNo = benedetails.customerNo;


        viewModel.getOtp(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()
                , response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            onMessage("OTP Sent Successfully");

                        } else {
                            onError(response.resource.description);
                        }
                    }
                });

    }


    public void VerifyOtp() {

        Utils.showCustomProgressDialog(getContext(), false);
        VerifyOtpRequest request = new VerifyOtpRequest();
        request.CustomerNo = benedetails.customerNo;
        request.CustomerOtp = binding.edtOtp.getText().toString();


        viewModel.verifyOtp(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()
                , response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            onMessage("OTP Verified Successfully");
                            getSummary();

                        } else if (response.resource.responseCode.equals(100)) {
                            onError("Invalid OTP. Please enter correct OTP");
                        } else {
                            onError(response.resource.description);
                        }
                    }
                });
    }

    public void getSummary() {

//        Utils.showCustomProgressDialog(getContext(), false);
//        RitmanPaySendRequest ritmanPaySendRequest = new RitmanPaySendRequest();
//        ritmanPaySendRequest.Customer_No = benedetails.customerNo;
//        ritmanPaySendRequest.PayOutCurrency = "INR";
//        ritmanPaySendRequest.TransferAmount = PayInAmount;
//        ritmanPaySendRequest.Payin_Currency = "INR";
//        ritmanPaySendRequest.Beneficiary_No = benedetails.beneficiaryNumber;
//

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
                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        String bodyy = AESHelper.decrypt(response.resource.data.body
                                , gKey);

                        Log.e("getSummary: ", body);
                        if (response.resource.responseCode.equals(101)) {
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
                            Utils.hideCustomProgressDialog();
                            if (response.resource.data != null) {

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
//
//        viewModel.getSummary(ritmanPaySendRequest, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()
//                , response -> {
//                    Utils.hideCustomProgressDialog();
//                    if (response.status == Status.ERROR) {
//                        onError(getString(response.messageResourceId));
//                    } else {
//                        assert response.resource != null;
//                        if (response.resource.responseCode.equals(101)) {
//                            TransactionNumber = response.resource.data.getTransactionNumber();
//                            Handler mHandler;
//                            mHandler = new Handler();
//                            mHandler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mHandler.removeCallbacks(this);
//                                    Intent i = new Intent(getActivity(), TransactionReceiptActivity.class);
//                                    i.putExtra("TransactionNumber", response.resource.data.getTransactionNumber());
//                                    startActivity(i);
//                                    getBaseActivity().finish();
//                                }
//                            }, 200);
//
//                        } else {
//                            onError(response.resource.description);
//                        }
//                    }
//                });


    }

}






