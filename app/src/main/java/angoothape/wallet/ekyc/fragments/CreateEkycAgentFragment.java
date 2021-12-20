package angoothape.wallet.ekyc.fragments;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.basgeekball.awesomevalidation.utility.custom.SimpleCustomValidation;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;

import angoothape.wallet.R;
import angoothape.wallet.databinding.FragmentCreateEkycAgentBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.YBCreateAgentRequest;
import angoothape.wallet.di.JSONdi.restResponse.YBCreateAgentResponse;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.dialogs.SingleButtonMessageDialog;
import angoothape.wallet.ekyc.EKYCMainActivity;
import angoothape.wallet.ekyc.viewmodels.EKYCViewModel;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.interfaces.OnDecisionMade;
import angoothape.wallet.utils.Utils;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;


public class CreateEkycAgentFragment extends BaseFragment<FragmentCreateEkycAgentBinding> implements OnDecisionMade {

    private AwesomeValidation mAwesomeValidation;
    public EKYCViewModel viewModel;
    final Calendar myCalendar = Calendar.getInstance();
    int day, month, year, age;
    Calendar mcalendar;
    String gender = "M";
    int yearOfToday;
    int yearOfBirthday;
    String EmailID, FirstName, LastName, MobileNumber, Dateofbirth, Pan, Shop;

    @Override
    protected void injectView() {
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        // viewModel = new ViewModelProvider(this).get(EKYCViewModel.class);
        viewModel = ((EKYCMainActivity) getBaseActivity()).viewModel;
        mcalendar = Calendar.getInstance();
        day = mcalendar.get(Calendar.DAY_OF_MONTH);
        year = mcalendar.get(Calendar.YEAR);
        month = mcalendar.get(Calendar.MONTH);
        binding.dobEditTextRegi.setFocusable(false);

        mAwesomeValidation = new AwesomeValidation(BASIC);
     //   mAwesomeValidation.addValidation(binding.firstNameEditTextRegi, "^[A-Za-z\\s]+", getResources().getString(R.string.firstname_personal));
     //   mAwesomeValidation.addValidation(binding.lastNameEditTextRegi, "^[A-Za-z\\s]+", getResources().getString(R.string.lastname_personal));
        mAwesomeValidation.addValidation(binding.shopName, "[\\w\\s-,.]+$", getResources().getString(R.string.please_enter_shop_name));
      //  mAwesomeValidation.addValidation(binding.emailEditTextRegi, Patterns.EMAIL_ADDRESS, getResources().getString(R.string.invalid_email));
     //   mAwesomeValidation.addValidation(binding.mobileNumberEditTextRegi, RegexTemplate.TELEPHONE, getResources().getString(R.string.invalid_phone_no));
        mAwesomeValidation.addValidation(binding.panEkyc, "[A-Z]{5}[0-9]{4}[A-Z]{1}", getResources().getString(R.string.invalid_pan));

//        mAwesomeValidation.addValidation(getActivity(), R.id.dobEditTextRegi, new SimpleCustomValidation() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public boolean compare(String input) {
//                LocalDate currentDate = LocalDate.now();
//                yearOfToday = currentDate.getYear();
//                yearOfBirthday = myCalendar.get(Calendar.YEAR);
//                age = yearOfToday - yearOfBirthday;
//                if (age > 18 && age < 55) {
//                    mAwesomeValidation.clear();
//                    return true;
//                } else {
//                    return false;
//                }
//
//            }
//        }, R.string.err_birth);
//

        binding.maleFemaleRadioGroupRegi.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = group.findViewById(checkedId);
            gender = radioButton.getText().toString().substring(0, 1);
        });

        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        binding.dobEditTextRegi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.dobEditTextRegi.setFocusableInTouchMode(true);
                binding.dobEditTextRegi.setCursorVisible(false);
                binding.dobEditTextRegi.setShowSoftInputOnFocus(false);
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        binding.personalNext.setOnClickListener(v -> {

            if (mAwesomeValidation.validate()) {
                YBCreateAgent();
            }

        });
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        binding.dobEditTextRegi.setText(sdf.format(myCalendar.getTime()));
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
       // request.agent_email_id = EmailID;
        request.agent_shop_name = Shop;

        request.agent_pan = Pan;

        Log.e("merhcant: ", getSessionManager().getMerchantName().trim());
        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() +
                KeyHelper.getSKey(KeyHelper
                        .getKey(getSessionManager().getMerchantName())).trim();

        String body = RestClient.makeGSONString(request);
        Log.e( "body: ",body );
        AERequest aeRequest = new AERequest();
        aeRequest.body = AESHelper.encrypt(body.trim(), gKey.trim());

        Log.e("gkey: ", gKey.trim());
        Log.e("skey: ", KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())));

        viewModel.YBCreateAgent(aeRequest, KeyHelper.getKey(getSessionManager().getMerchantName()).trim(), KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim())
                .observe(getViewLifecycleOwner()
                        , response -> {
                            Utils.hideCustomProgressDialog();
                            if (response.status == Status.ERROR) {
                                onMessage(getString(response.messageResourceId));
                            } else {
                                assert response.resource != null;
                                if (response.resource.responseCode.equals(101)) {

                                    String bodyy = AESHelper.decrypt(response.resource.data.body
                                            , gKey);

                                    Log.e("YBCreateAgent: ",bodyy );
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
                                } else {
                                    onMessage(response.resource.description);
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
    public void onCancel(boolean goBack)  {

    }
}