package angoothape.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;

import angoothape.wallet.aeps.activity.SelectDeviceActivity;
import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.ActivityRegisterFromAEPSBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restResponse.AEResponse;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.di.RegisterModel;
import angoothape.wallet.utils.Utils;
import retrofit2.Call;
import retrofit2.Response;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class RegisterFromAEPS extends RitmanBaseActivity<ActivityRegisterFromAEPSBinding> {


    private AwesomeValidation mAwesomeValidation;
    String gender = "m";

    @Override
    public int getLayoutId() {
        return R.layout.activity_register_from_a_e_p_s;
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

    @Override
    protected void initUi(Bundle savedInstanceState) {
        if (getIntent() != null) {
            if (getIntent().getExtras().getString("phone") != null) {
                binding.mobileNumberEditTextRegi.setText(getIntent().getExtras().getString("phone"));
            }
        }


        binding.toolBar.backBtn.setOnClickListener(v -> onBackPressed());

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

    public void getUserRegister() {


        Utils.showCustomProgressDialog(this, false);

        RegisterModel registerModel = new RegisterModel();
        registerModel.credentials.LanguageID = Integer.parseInt(sessionManager.getlanguageselection());
        registerModel.firstName = binding.firstNameEditTextRegi.getText().toString();
        registerModel.gender = gender;
        registerModel.mobileNumber = binding.mobileNumberEditTextRegi.getText().toString();

        String gKey = KeyHelper.getKey(sessionManager.getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(sessionManager.getMerchantName())).trim();

        String body = RestClient.makeGSONString(registerModel);
        Log.e("body ", body);
        AERequest request = new AERequest();
        request.body = AESHelper.encrypt(body.trim(), gKey.trim());



        Call<AEResponse> call = RestClient.get().createRegister(RestClient.makeGSONRequestBody(request)
                , KeyHelper.getKey(sessionManager.getMerchantName()).trim(), KeyHelper.getSKey(KeyHelper
                        .getKey(sessionManager.getMerchantName())).trim());

        call.enqueue(new retrofit2.Callback<AEResponse>() {
            @Override
            public void onResponse(Call<AEResponse> call, Response<AEResponse> response) {
                Utils.hideCustomProgressDialog();
                assert response.body() != null;
                String bodyy = AESHelper.decrypt(response.body().data.body
                        , gKey);
                Log.e("response ", body);
                if (response.body().responseCode.equals(101)) {

                    try {

                        startActivity(new Intent(RegisterFromAEPS.this, SelectDeviceActivity.class));
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    Utils.hideCustomProgressDialog();
                    if (response.body().data != null) {

                        Log.e("getBillDetails: ", bodyy);
                        if (!body.isEmpty()) {
                            onError(bodyy);
                        } else {
                            onError(response.body().description);
                        }
                    } else {
                        onError(response.body().description);
                    }

                }
            }

            @Override
            public void onFailure(Call<AEResponse> call, Throwable t) {
                Utils.hideCustomProgressDialog();
                Toast.makeText(RegisterFromAEPS.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }


}