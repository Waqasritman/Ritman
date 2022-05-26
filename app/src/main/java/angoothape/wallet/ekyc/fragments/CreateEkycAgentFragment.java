package angoothape.wallet.ekyc.fragments;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import androidx.navigation.Navigation;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Locale;

import angoothape.wallet.R;
import angoothape.wallet.databinding.FragmentCreateEkycAgentBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.YBCreateAgentRequest;
import angoothape.wallet.di.JSONdi.restRequest.YBCreateCustomerRequest;
import angoothape.wallet.di.JSONdi.restResponse.BiometricKYCErrorResponse;
import angoothape.wallet.di.JSONdi.restResponse.CreateCustomerErrorResponse;
import angoothape.wallet.di.JSONdi.restResponse.YBCreateAgentResponse;
import angoothape.wallet.di.JSONdi.restResponse.YBCreateCustomerResponse;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.ekyc.EKYCMainActivity;
import angoothape.wallet.ekyc.viewmodels.EKYCViewModel;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.interfaces.OnDecisionMade;
import angoothape.wallet.utils.DateAndTime;
import angoothape.wallet.utils.Utils;


public class CreateEkycAgentFragment extends BaseFragment<FragmentCreateEkycAgentBinding>
        implements OnDecisionMade, DatePickerDialog.OnDateSetListener {

    //   private AwesomeValidation mAwesomeValidation;
    public EKYCViewModel viewModel;

    String gender = "M";
    String EmailID, Dateofbirth, Pan, Shop;

    @Override
    protected void injectView() {
    }


    @Override
    public boolean isValidate() {
        if (binding.shopName.getText().toString().isEmpty()) {
            onMessage(getResources().getString(R.string.please_enter_shop_name));
            return false;
        } else if (binding.dobEditTextRegi.getText().toString().isEmpty()) {
            onMessage("Please select date of birth");
            return false;
        } else if (binding.panEkyc.getText().toString().isEmpty()) {
            onMessage(getResources().getString(R.string.invalid_pan));
            return false;
        } else if (!binding.panEkyc.getText().toString().matches("[A-Z]{5}[0-9]{4}[A-Z]")) {
            onMessage(getResources().getString(R.string.invalid_pan));
            return false;
        }
        return true;
    }

    public boolean isCustomerValidate() {
        if (binding.shopName.getText().toString().isEmpty()) {
            onMessage(getResources().getString(R.string.please_enter_customr_name));
            return false;
        } else if (binding.mobileNumber.getText().toString().isEmpty()) {
            onMessage("Enter enter valid mobile number");
            return false;
        } else if (binding.dobEditTextRegi.getText().toString().isEmpty()) {
            onMessage("Please select date of birth");
            return false;
        } else if (binding.panEkyc.getText().toString().isEmpty()) {
            onMessage(getResources().getString(R.string.invalid_pan));
            return false;
        } else if (!binding.panEkyc.getText().toString().matches("[A-Z]{5}[0-9]{4}[A-Z]")) {
            onMessage(getResources().getString(R.string.invalid_pan));
            return false;
        }
        return true;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = ((EKYCMainActivity) getBaseActivity()).viewModel;

        if (((EKYCMainActivity) getBaseActivity()).isCustomer) {
            binding.nameFleid.setText("Customer name*");
            binding.shopName.setHint("Customer name");
            binding.mobileFleid.setVisibility(View.VISIBLE);
            binding.mobileNumber.setVisibility(View.VISIBLE);
            binding.genderTxt.setVisibility(View.GONE);
            binding.genderLayout.setVisibility(View.GONE);
        } else {
            binding.nameFleid.setText("Shop name*");
            binding.shopName.setHint("Shop name");
        }

        binding.dobEditTextRegi.setFocusable(false);

        binding.maleFemaleRadioGroupRegi.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = group.findViewById(checkedId);
            gender = radioButton.getText().toString().substring(0, 1);
        });

        binding.dobEditTextRegi.setOnClickListener(v -> {
            binding.dobEditTextRegi.setFocusableInTouchMode(true);
            binding.dobEditTextRegi.setCursorVisible(false);
            binding.dobEditTextRegi.setShowSoftInputOnFocus(false);
            showPickerDialog();
        });


        binding.personalNext.setOnClickListener(v -> {
            if (((EKYCMainActivity) getBaseActivity()).isCustomer) {
                if (isCustomerValidate()) {
                    YBCreateCustomer();
                }
            } else {
                if (isValidate()) {
                    YBCreateAgent();
                }
            }

        });

    }


    /**
     * dialog code for show date picker
     */
    private void showPickerDialog() {
        Calendar calendar = Calendar.getInstance();


        int Year = calendar.get(Calendar.YEAR);
        int Month = calendar.get(Calendar.MONTH);
        int Day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = DatePickerDialog
                .newInstance(this, Year, Month, Day);

        datePickerDialog.setThemeDark(false);
        datePickerDialog.showYearPickerFirst(true);
        datePickerDialog.setAccentColor(Color.parseColor("#342E78"));
        datePickerDialog.setLocale(new Locale("en"));

        datePickerDialog.setMaxDate(calendar);


        datePickerDialog.setTitle("Select Date of Birth");
        datePickerDialog.show(getParentFragmentManager(), "");
        datePickerDialog.setOnCancelListener(DialogInterface::dismiss);
    }


    public void YBCreateAgent() {
        EmailID = binding.emailEditTextRegi.getText().toString();
        gender = gender;
        Dateofbirth = binding.dobEditTextRegi.getText().toString();
        Pan = binding.panEkyc.getText().toString();
        Shop = binding.shopName.getText().toString();

        Utils.showCustomProgressDialog(getActivity(), false);
        YBCreateAgentRequest request = new YBCreateAgentRequest();
        request.agent_date_of_birth = Dateofbirth;
        request.agent_gender = gender;
        request.agent_shop_name = Shop;
        request.agent_pan = Pan;

        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() +
                KeyHelper.getSKey(KeyHelper
                        .getKey(getSessionManager().getMerchantName())).trim();

        String body = RestClient.makeGSONString(request);
        AERequest aeRequest = new AERequest();
        aeRequest.body = AESHelper.encrypt(body.trim(), gKey.trim());

        viewModel.YBCreateAgent(aeRequest, KeyHelper.getKey(getSessionManager().getMerchantName()).trim(), KeyHelper.getSKey(KeyHelper
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
                                    Log.e("YBCreateAgent: ", bodyy);
                                    try {
                                        Gson gson = new Gson();
                                        Type type = new TypeToken<YBCreateAgentResponse>() {
                                        }.getType();

                                        YBCreateAgentResponse data = gson.fromJson(bodyy, type);
                                        viewModel.mobile.setValue(data.Agent_MobileNo);
                                        viewModel.otpToken.setValue(data.AgentCreate_Response.otpToken);
                                        Navigation.findNavController(binding.getRoot()).navigate(R.id
                                                .action_createEkycAgentFragment_to_OTPVerifyFragment);

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
                                    if (bodyy != null) {
                                        try {
                                            Gson gson = new Gson();
                                            Type type = new TypeToken<BiometricKYCErrorResponse>() {
                                            }.getType();
                                            BiometricKYCErrorResponse data = gson.fromJson(bodyy, type);
                                            onError(data.responseMessage);
                                        } catch (Exception e) {
                                            onError(response.resource.description);
                                        }
                                    } else {
                                        onError("Please contact admin body is null");
                                    }

                                }
                            }
                        });


    }

    public void YBCreateCustomer() {
        Utils.showCustomProgressDialog(getContext() , false);
        EmailID = binding.emailEditTextRegi.getText().toString();

        gender = gender;

        Dateofbirth = binding.dobEditTextRegi.getText().toString();
        Pan = binding.panEkyc.getText().toString();
        Shop = binding.shopName.getText().toString();

        Utils.showCustomProgressDialog(getActivity(), false);
        YBCreateCustomerRequest request = new YBCreateCustomerRequest();
        request.customer_date_of_birth = Dateofbirth;
        request.mobile_no = binding.mobileNumber.getText().toString();
        request.customer_full_name = Shop;
        request.customer_pan = Pan;

        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() +
                KeyHelper.getSKey(KeyHelper
                        .getKey(getSessionManager().getMerchantName())).trim();

        String body = RestClient.makeGSONString(request);
        AERequest aeRequest = new AERequest();
        aeRequest.body = AESHelper.encrypt(body.trim(), gKey.trim());

        viewModel.YBCreateCustomer(aeRequest, KeyHelper.getKey(getSessionManager().getMerchantName()).trim(), KeyHelper.getSKey(KeyHelper
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

                                    Log.e("YBCreateCustomer: ", bodyy);
                                    try {
                                        Gson gson = new Gson();
                                        Type type = new TypeToken<YBCreateCustomerResponse>() {
                                        }.getType();

                                        YBCreateCustomerResponse data = gson.fromJson(bodyy, type);
                                        viewModel.mobile.setValue(binding.mobileNumber.getText().toString());
                                        viewModel.otpToken.setValue(data.AgentCreate_Response.otpToken);
                                        Navigation.findNavController(binding.getRoot()).navigate(R.id
                                                .action_createEkycAgentFragment_to_OTPVerifyFragment);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else if (response.resource.responseCode.equals(305)) {
                                    onMessage(response.resource.description + "\nTry again later");
                                    Navigation.findNavController(binding.getRoot()).navigateUp();
                                } else {
                                    Utils.hideCustomProgressDialog();
                                    if (response.resource.data != null) {
                                        String bodyy = AESHelper.decrypt(response.resource.data.body
                                                , gKey);
                                        if (!body.isEmpty()) {
                                            //  onError(bodyy);

                                            Log.e("YBCreateCustomer: ", bodyy);
                                            try {
                                                Gson gson = new Gson();
                                                Type type = new TypeToken<CreateCustomerErrorResponse>() {
                                                }.getType();

                                                CreateCustomerErrorResponse data = gson.fromJson(bodyy, type);
                                                onError(data.responseMessage);

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }


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
        return R.layout.fragment_create_ekyc_agent;
    }

    @Override
    public void onProceed() {

    }

    @Override
    public void onCancel(boolean goBack) {

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        binding.dobEditTextRegi.setText(DateAndTime.setDateFormat(year, monthOfYear, dayOfMonth));

    }
}