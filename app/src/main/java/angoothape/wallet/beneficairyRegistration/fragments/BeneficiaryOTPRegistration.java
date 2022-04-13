package angoothape.wallet.beneficairyRegistration.fragments;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;

import angoothape.wallet.R;
import angoothape.wallet.beneficairyRegistration.viewmodel.RegisterBeneficiaryViewModel;
import angoothape.wallet.databinding.GenerateOtpFragmentLayoutBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.CreateBeneficiaryRequest;
import angoothape.wallet.di.JSONdi.restRequest.RegisterBeneficiaryRequest;
import angoothape.wallet.di.JSONdi.restRequest.VerifyBeneficiaryRequest;
import angoothape.wallet.di.JSONdi.restResponse.BeneficiaryOTPResponse;
import angoothape.wallet.di.JSONdi.restResponse.VerifyBeneficiary;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.di.XMLdi.ResponseHelper.GetBeneficiaryListResponse;
import angoothape.wallet.dialogs.SingleButtonMessageDialog;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.interfaces.OnDecisionMade;
import angoothape.wallet.utils.Utils;

public class BeneficiaryOTPRegistration extends BaseFragment<GenerateOtpFragmentLayoutBinding> implements
        OnDecisionMade {

    RegisterBeneficiaryViewModel viewModel;
    RegisterBeneficiaryRequest registerBeneficiaryRequest;
    GetBeneficiaryListResponse benedetails;

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public boolean isValidate() {
        if (binding.edtOtp.getText().toString().isEmpty()) {
            onMessage(getString(R.string.enter_otp));
            return false;
        }
        return true;
    }

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(RegisterBeneficiaryViewModel.class);
        assert getArguments() != null;
        registerBeneficiaryRequest = getArguments().getParcelable("bene");
        //  viewModel.beneRegister.postValue(registerBeneficiaryRequest);
        binding.txtResendOtp.setOnClickListener(v -> {
            getOtp();
        });


        binding.btnVerifyOtp.setOnClickListener(v -> {
            regiterBene();
        });

        getOtp();
    }

    @Override
    public int getLayoutId() {
        return R.layout.generate_otp_fragment_layout;
    }


    public void regiterBene() {
        if (isValidate()) {
            Utils.showCustomProgressDialog(getContext(), false);
            registerBeneficiaryRequest.otp = binding.edtOtp.getText().toString();
            //viewModel.beneRegister.setValue(registerBeneficiaryRequest);

            String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                    .getKey(getSessionManager().getMerchantName())).trim();
            String body = RestClient.makeGSONString(registerBeneficiaryRequest);
            Log.e("regiterBene", body);
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

    public void getOtp() {
        Utils.showCustomProgressDialog(getContext(), false);
        CreateBeneficiaryRequest request = new CreateBeneficiaryRequest();
        request.CustomerNo = registerBeneficiaryRequest.CustomerNo;
        request.FirstName = registerBeneficiaryRequest.FirstName;
        request.AccountNumber = registerBeneficiaryRequest.AccountNumber;

        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim();

        String body = RestClient.makeGSONString(request);
        Log.e("getOtp: ", body);
        AERequest aeRequest = new AERequest();
        aeRequest.body = AESHelper.encrypt(body.trim(), gKey.trim());


        viewModel.createBeneficairyOTP(aeRequest, KeyHelper.getKey(getSessionManager().getMerchantName()).trim(), KeyHelper.getSKey(KeyHelper
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
                            Log.e("regiterBene: ", bodyy);

                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<BeneficiaryOTPResponse>() {
                                }.getType();
                                BeneficiaryOTPResponse data = gson.fromJson(bodyy, type);

                                if (data.responseCode.equalsIgnoreCase("00")) {
                                    onMessage("OTP Sent Successfully");
                                } else {
                                    onMessage(data.message);
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {

                            showSuccess(response.resource.description, "Error", true);
                        }
                    }
                });

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
                    .navigate(R.id.action_beneficiaryOTPRegistration_to_bank_tranfer_navigation
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
        Log.e("verifyBeneficiary: ",body );
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
                             , "Error" , true);
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