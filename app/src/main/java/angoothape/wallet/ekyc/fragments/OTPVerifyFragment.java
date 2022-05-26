package angoothape.wallet.ekyc.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.navigation.Navigation;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;

import angoothape.wallet.R;
import angoothape.wallet.databinding.FragmentOTPVerifyBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.ValidateOtpRequest;
import angoothape.wallet.di.JSONdi.restResponse.BiometricKYCErrorResponse;
import angoothape.wallet.di.JSONdi.restResponse.ValidateOtpResponse;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.ekyc.AdharBioEKYCActivity;
import angoothape.wallet.ekyc.EKYCMainActivity;
import angoothape.wallet.ekyc.viewmodels.EKYCViewModel;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.utils.Utils;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;


public class OTPVerifyFragment extends BaseFragment<FragmentOTPVerifyBinding> {

    private AwesomeValidation mAwesomeValidation;
    EKYCViewModel viewModel;
    String otpVerify;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        //  viewModel = new ViewModelProvider(this).get(EKYCViewModel.class);
        viewModel = ((EKYCMainActivity) getBaseActivity()).viewModel;
        mAwesomeValidation = new AwesomeValidation(BASIC);
        mAwesomeValidation.addValidation(binding.otpVerify, "[0-9]{6}", getResources().getString(R.string.enter_otp));

        binding.personalNext.setOnClickListener(v -> {

            if (mAwesomeValidation.validate()) {
                validateOTP();
            }
          /*  Navigation.findNavController(binding.getRoot()).navigate(R.id
                    .action_personal_ZeroFragment_to_personal_One_Fragment2);*/
        });
    }

    public void validateOTP() {

        otpVerify = binding.otpVerify.getText().toString();

        Utils.showCustomProgressDialog(getActivity(), false);
        ValidateOtpRequest request = new ValidateOtpRequest();
        request.mobile_no = viewModel.mobile.getValue();
        request.otp_token = viewModel.otpToken.getValue();//"M";//
        request.otp = otpVerify;


        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim();


        String body = RestClient.makeGSONString(request);

        AERequest aeRequest = new AERequest();
        aeRequest.body = AESHelper.encrypt(body.trim(), gKey.trim());
        viewModel.YBValidateOTP(aeRequest, KeyHelper.getKey(getSessionManager().getMerchantName()).trim(), KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim())
                .observe(getViewLifecycleOwner()
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
                                        Type type = new TypeToken<ValidateOtpResponse>() {
                                        }.getType();
                                        ValidateOtpResponse data = gson.fromJson(bodyy, type);
                                        viewModel.kycToken.setValue(data.kycToken_);
                                        viewModel.wadh.setValue(data.wadh_);

                                        Handler mHandler;
                                        mHandler = new Handler();
                                        mHandler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                mHandler.removeCallbacks(this);
                                                Intent i = new Intent(getActivity(), AdharBioEKYCActivity.class);
                                                // i.putExtra("TransactionNumber", response.resource.data.getTransactionNumber());
                                                i.putExtra("mobile_no", viewModel.mobile.getValue());
                                                i.putExtra("kyc_token", data.kycToken_);
                                                i.putExtra("wadh_value", data.wadh_);
                                                i.putExtra("isCustomer", ((EKYCMainActivity) getBaseActivity()).isCustomer);
                                                startActivity(i);
                                                getBaseActivity().finish();
                                            }
                                        }, 200);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else if (response.resource.responseCode.equals(305)) {
                                    onMessage(response.resource.description + "\nTry again later");
                                    Navigation.findNavController(binding.getRoot()).navigateUp();
                                } else {
                                    Utils.hideCustomProgressDialog();
                                    String bodyy = AESHelper.decrypt(response.resource.data.body
                                            , gKey);
                                    try {
                                        Gson gson = new Gson();
                                        Type type = new TypeToken<BiometricKYCErrorResponse>() {
                                        }.getType();
                                        BiometricKYCErrorResponse data = gson.fromJson(bodyy, type);
                                        onError(data.responseMessage);
                                    } catch (Exception e) {
                                        onError(response.resource.description);
                                    }
                                }
                            }
                        });


    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_o_t_p_verify;
    }

}