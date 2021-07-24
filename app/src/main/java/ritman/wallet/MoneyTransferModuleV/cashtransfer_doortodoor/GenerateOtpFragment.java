package ritman.wallet.MoneyTransferModuleV.cashtransfer_doortodoor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import ritman.wallet.MoneyTransferModuleV.MoneyTransferMainLayout;
import ritman.wallet.MoneyTransferModuleV.TransactionReceiptActivity;
import ritman.wallet.MoneyTransferModuleV.viewmodels.BankTransferViewModel;
import ritman.wallet.R;
import ritman.wallet.beneficairyRegistration.BeneficiaryRegistrationActivity;
import ritman.wallet.databinding.GenerateOtpFragmentLayoutBinding;
import ritman.wallet.di.JSONdi.Status;
import ritman.wallet.di.JSONdi.restRequest.GetOtpRequest;
import ritman.wallet.di.JSONdi.restRequest.RitmanPaySendRequest;
import ritman.wallet.di.JSONdi.restRequest.VerifyOtpRequest;
import ritman.wallet.di.XMLdi.ResponseHelper.GetBeneficiaryListResponse;
import ritman.wallet.fragments.BaseFragment;
import ritman.wallet.utils.Utils;

public class GenerateOtpFragment extends BaseFragment<GenerateOtpFragmentLayoutBinding> {
    GetBeneficiaryListResponse benedetails;
    BankTransferViewModel viewModel;
    String TransactionNumber,OptSend,Verify,customerNo;
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
            if (binding.edtOtp.getText().toString().equals("")){
                onMessage("Please enter your OTP");
            }
            else if (binding.edtOtp.getText().toString().length()!=6){
                onMessage("Invalid OTP. Please enter correct OTP");
            }

            else {
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

    public void getOtp(){
        Utils.showCustomProgressDialog(getContext(), false);
        GetOtpRequest request = new GetOtpRequest();
        request.CustomerNo = benedetails.customerNo;


        viewModel.getOtp(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()
                , response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            onMessage("OTP Sent Successfully");

                        } else {
                            onMessage(response.resource.description);
                        }
                    }
                });

    }


    public void VerifyOtp(){

        Utils.showCustomProgressDialog(getContext(), false);
        VerifyOtpRequest request = new VerifyOtpRequest();
        request.CustomerNo = benedetails.customerNo;
        request.CustomerOtp=binding.edtOtp.getText().toString();


        viewModel.verifyOtp(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()
                , response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            onMessage("OTP Verified Successfully");
                            getSummary();

                        }
                        else if (response.resource.responseCode.equals(100)){
                            onMessage("Invalid OTP. Please enter correct OTP");
                        }

                        else {
                            onMessage(response.resource.description);
                        }
                    }
                });
    }

    public void getSummary(){

        Utils.showCustomProgressDialog(getContext(), false);
        RitmanPaySendRequest ritmanPaySendRequest = new RitmanPaySendRequest();
        ritmanPaySendRequest.Customer_No = benedetails.customerNo;
        ritmanPaySendRequest.PayOutCurrency = "INR";
        ritmanPaySendRequest.TransferAmount = PayInAmount;
        ritmanPaySendRequest.Payin_Currency = "INR";
        ritmanPaySendRequest.Beneficiary_No = benedetails.beneficiaryNumber;


        viewModel.getSummary(ritmanPaySendRequest, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()
                , response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            TransactionNumber = response.resource.data.getTransactionNumber();
                            Handler mHandler;
                            mHandler = new Handler();
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mHandler.removeCallbacks(this);
                                    Intent i = new Intent(getActivity(), TransactionReceiptActivity.class);
                                    i.putExtra("TransactionNumber", response.resource.data.getTransactionNumber());
                                    startActivity(i);
                                    getBaseActivity().finish();
                                }
                            }, 200);

                        } else {
                            onMessage(response.resource.description);
                        }
                    }
                });


    }

    }






