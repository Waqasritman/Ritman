package angoothape.wallet;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import angoothape.wallet.beneficairyRegistration.viewmodel.RegisterBeneficiaryViewModel;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.GetBeneficiaryRequest;
import angoothape.wallet.di.JSONdi.restResponse.AEResponse;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import retrofit2.Call;
import retrofit2.Response;
import angoothape.wallet.databinding.ActivityRegistrationBinding;
import angoothape.wallet.di.JSONdi.restRequest.RegisterCustomerResponse;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.interfaces.OnDecisionMade;
import angoothape.wallet.di.RegisterModel;
import angoothape.wallet.utils.Utils;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class RegistrationActivity extends BaseFragment<ActivityRegistrationBinding>
        implements OnDecisionMade {

    private AwesomeValidation mAwesomeValidation;


    String gender = "m";
    RegisterBeneficiaryViewModel viewModel;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        //     binding.toolBar.titleTxt.setText("Customer Registration");
        viewModel = new ViewModelProvider(this).get(RegisterBeneficiaryViewModel.class);
        if (getArguments() != null) {
            if (getArguments().getString("phone") != null) {
                binding.mobileNumberEditTextRegi.setText(getArguments().getString("phone"));
            }
        }

        mAwesomeValidation = new AwesomeValidation(BASIC);
        mAwesomeValidation.addValidation(binding.firstNameEditTextRegi, "^[A-Za-z\\s]+", getResources().getString(R.string.firstname_personal));

        binding.maleFemaleRadioGroupRegi.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = group.findViewById(checkedId);
            gender = radioButton.getText().toString().substring(0, 1);
        });


        binding.registerRegi.setOnClickListener(v -> {
            validate();
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_registration;
    }


    public void validate() {
        if (binding.firstNameEditTextRegi.getText().toString().equals("")) {
            onMessage("Enter your First Name");
        } else if (binding.mobileNumberEditTextRegi.getText().toString().equals("")) {
            onMessage("Enter your Mobile");
        } else if (binding.mobileNumberEditTextRegi.getText().toString().length() != 10) {
            onMessage("Please enter valid 10-digits Mobile Number");
        } else if (!TextUtils.isDigitsOnly(binding.mobileNumberEditTextRegi.getText().toString())) {
            onMessage("Please enter valid 10-digits Mobile Number");
            binding.mobileNumberEditTextRegi.requestFocus();
        } else {
            getUserRegister();
        }
    }


    public void getBalanceCustomerLimit(String customerNo) {

        GetBeneficiaryRequest request = new GetBeneficiaryRequest();
        request.Customer_MobileNo = binding.mobileNumberEditTextRegi.getText().toString();

        viewModel.getBalanceLimitForCustomer(request, getSessionManager().getMerchantName().trim())
                .observe(getViewLifecycleOwner(), response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {

                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            onMessage("Customer Registered Successfully");
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("isDMTLive", response.resource.isDMTLive);
                            bundle.putString("customer_no", customerNo);
                            Navigation.findNavController(binding.getRoot())
                                    .navigate(R.id.sendMoneyViaBankFirstActivity, bundle);

                        } else {
                            onError(response.resource.description);
                        }
                    }
                });
    }

    public void getUserRegister() {
        Utils.showCustomProgressDialog(getContext(), false);

        RegisterModel registerModel = new RegisterModel();
        registerModel.credentials.LanguageID = Integer.parseInt(getSessionManager().getlanguageselection());
        registerModel.firstName = binding.firstNameEditTextRegi.getText().toString();
        registerModel.gender = gender;
        registerModel.mobileNumber = binding.mobileNumberEditTextRegi.getText().toString();

        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim();

        String body = RestClient.makeGSONString(registerModel);
        Log.e("body ", body);
        AERequest request = new AERequest();
        request.body = AESHelper.encrypt(body.trim(), gKey.trim());


        Call<AEResponse> call = RestClient.getREST().createRegister(RestClient.makeGSONRequestBody(request)
                , KeyHelper.getKey(getSessionManager().getMerchantName()).trim(), KeyHelper.getSKey(KeyHelper
                        .getKey(getSessionManager().getMerchantName())).trim());

        call.enqueue(new retrofit2.Callback<AEResponse>() {
            @Override
            public void onResponse(Call<AEResponse> call, Response<AEResponse> response) {


                if (response.isSuccessful()) {
                    assert response.body() != null;

                    if (response.body().responseCode.equals(101)) {
                        String bodyy = AESHelper.decrypt(response.body().data.body
                                , gKey);
                        Log.e("response ", body);
                        try {
                            Gson gson = new Gson();
                            Type type = new TypeToken<RegisterCustomerResponse>() {
                            }.getType();
                            RegisterCustomerResponse data = gson.fromJson(bodyy, type);
                            getBalanceCustomerLimit(data.customerNo);

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
                    Utils.hideCustomProgressDialog();
                }

            }

            @Override
            public void onFailure(Call<AEResponse> call, Throwable t) {
                Utils.hideCustomProgressDialog();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

//        Call<RegisterCustomerResponse> call = RestClient.get().createRegister(registerModel, getSessionManager().getMerchantName());
//        call.enqueue(new retrofit2.Callback<RegisterCustomerResponse>() {
//            @Override
//            public void onResponse(Call<RegisterCustomerResponse> call, Response<RegisterCustomerResponse> response) {
//                Utils.hideCustomProgressDialog();
//                assert response.body() != null;
//                if (response.body().responseCode.equals(101)) {
//                    onMessage("Customer Registered Successfully");
//                    Bundle bundle = new Bundle();
//                    bundle.putString("customer_no", response.body().customerNo);
//                    Navigation.findNavController(binding.getRoot())
//                            .navigate(R.id.sendMoneyViaBankFirstActivity , bundle);
//                } else {
//                    onError(response.body().description);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<RegisterCustomerResponse> call, Throwable t) {
//                Utils.hideCustomProgressDialog();
//                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });


    }


    @Override
    public void onProceed() {
        // finish();
    }

    @Override
    public void onCancel(boolean goBack) {

    }
}
