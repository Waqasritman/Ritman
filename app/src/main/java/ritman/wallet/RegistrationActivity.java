package ritman.wallet;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ritman.wallet.base.RitmanBaseActivity;
import ritman.wallet.databinding.ActivityRegistrationBinding;
import ritman.wallet.di.JSONdi.restRequest.Credentials;
import ritman.wallet.di.generic_response.SimpleResponse;
import ritman.wallet.di.JSONdi.retrofit.RestClient;
import ritman.wallet.dialogs.AlertDialog;
import ritman.wallet.dialogs.SingleButtonMessageDialog;
import ritman.wallet.home_module.NewDashboardActivity;
import ritman.wallet.interfaces.OnDecisionMade;
import ritman.wallet.model.RegisterModel;
import ritman.wallet.utils.Utils;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.basgeekball.awesomevalidation.utility.custom.SimpleCustomValidation;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class RegistrationActivity extends RitmanBaseActivity<ActivityRegistrationBinding>
        implements OnDecisionMade {

    private AwesomeValidation mAwesomeValidation;
    final Calendar myCalendar = Calendar.getInstance();
    int day, month, year, age;
    Calendar mcalendar;
    String gender = "m";
    int yearOfToday;
    int yearOfBirthday;


    @Override
    public int getLayoutId() {
        return R.layout.activity_registration;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void initUi(Bundle savedInstanceState) {

        binding.toolBar.titleTxt.setText("Customer Registration");

        mcalendar = Calendar.getInstance();
        day = mcalendar.get(Calendar.DAY_OF_MONTH);
        year = mcalendar.get(Calendar.YEAR);
        month = mcalendar.get(Calendar.MONTH);
        binding.dobEditTextRegi.setFocusable(false);

        mAwesomeValidation = new AwesomeValidation(BASIC);
        mAwesomeValidation.addValidation(binding.firstNameEditTextRegi, "^[A-Za-z\\s]+", getResources().getString(R.string.firstname_personal));
        mAwesomeValidation.addValidation(binding.lastNameEditTextRegi, "^[A-Za-z\\s]+", getResources().getString(R.string.lastname_personal));
        mAwesomeValidation.addValidation(binding.middleNameEditTextRegi, "[\\w\\s-,.]+$", getResources().getString(R.string.middlename_personal));
        mAwesomeValidation.addValidation(binding.pincodeEditTextRegi, "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$", getResources().getString(R.string.invalide_pine_code));
        //mAwesomeValidation.addValidation(binding.dobEditTextRegi, "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$", getResources().getString(R.string.date_of_birth_ex));
        mAwesomeValidation.addValidation(binding.emailEditTextRegi, Patterns.EMAIL_ADDRESS, getResources().getString(R.string.invalid_email));
      //  mAwesomeValidation.addValidation(binding.mobileNumberEditTextRegi, "[6-9][0-9]{9}", getResources().getString(R.string.invalid_mob_no));
       // mAwesomeValidation.addValidation(binding.addressEditTextRegi, "[\\w\\s-,.]+$", getResources().getString(R.string.addre_line_one));
        mAwesomeValidation.addValidation(binding.pincodeEditTextRegi, "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$", getResources().getString(R.string.invalide_pine_code));
//        mAwesomeValidation.addValidation(RegistrationActivity.this, R.id.dobEditTextRegi, new SimpleCustomValidation() {
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


        binding.toolBar.backBtn.setOnClickListener(v -> onBackPressed());

        binding.toolBar.toolBarFinal
                .setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.toolBar.backBtn
                .setColorFilter(ContextCompat.getColor(this,
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);


        binding.toolBar.crossBtn.setOnClickListener(v -> {
            //   if (SelectBeneficiaryFragment!=null){
            //     Intent intent=new Intent(RegistrationActivity.this,NewDashboardActivity.class);
            //   startActivity(intent);

            //onClose();
            //}
            //else {
            onClose();
            //}

        });


        // binding.registerRegi.setOnClickListener(v -> validate());
        // selectDateofBirth();

        binding.maleFemaleRadioGroupRegi.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = group.findViewById(checkedId);
            gender = radioButton.getText().toString().substring(0, 1);
        });


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        binding.dobEditTextRegi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.dobEditTextRegi.setFocusableInTouchMode(true);
                binding.dobEditTextRegi.setCursorVisible(false);
                binding.dobEditTextRegi.setShowSoftInputOnFocus(false);
                // TODO Auto-generated method stub
                new DatePickerDialog(RegistrationActivity.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        binding.registerRegi.setOnClickListener(v -> {
//            if (mAwesomeValidation.validate()) {
//               if (binding.mobileNumberEditTextRegi.getText().toString().equals("")) {
//                    onMessage("Please enter your Mobile Number");
//                }
//               else if (binding.mobileNumberEditTextRegi.getText().toString().length()!=10){
//                   onMessage("Please enter valid 10-digits Mobile Number");
//               }
//               else if (binding.addressEditTextRegi.getText().toString().equals("")){
//                   onMessage("Please enter your Address");
//               }
//               else {
//                   getUserRegister();
//               }
//
//            }

            validate();
        });



    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        binding.dobEditTextRegi.setText(sdf.format(myCalendar.getTime()));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void validate() {

        LocalDate currentDate = LocalDate.now();
        yearOfToday = currentDate.getYear();
        yearOfBirthday = myCalendar.get(Calendar.YEAR);
        age = yearOfToday - yearOfBirthday;

        if (binding.firstNameEditTextRegi.getText().toString().equals("")) {
            onMessage("Enter your First Name");
        }

        else if (binding.middleNameEditTextRegi.getText().toString().equals("")) {
            onMessage("Enter your Middle Name");
        }

        else if (binding.lastNameEditTextRegi.getText().toString().equals("")) {
            onMessage("Enter your Last Name");
        } else if (binding.dobEditTextRegi.getText().toString().equals("")) {
            onMessage("Enter your Date Of Birth (D.O.B.)");
        }
        else if (age < 18 || age > 55) {
            onMessage("Age Cannot be less than 18-years or greater than 55-years");
        }
        else if (binding.pincodeEditTextRegi.getText().toString().equals("")) {
            onMessage("Enter your Pincode No.");
        } else if (!isValidPincodeNo(binding.pincodeEditTextRegi.getText().toString())) {
            onMessage("Enter Valid Pincode No.");
        } else if (binding.mobileNumberEditTextRegi.getText().toString().equals("")) {
            onMessage("Enter your Mobile");
        }

        else if (binding.mobileNumberEditTextRegi.getText().toString().length()!=10){
            onMessage("Please enter valid 10-digits Mobile Number");
        }

        else if (binding.emailEditTextRegi.getText().toString().equals("")) {
            onMessage("Enter your Email Id");
        }

        else if (!TextUtils.isDigitsOnly(binding.mobileNumberEditTextRegi.getText().toString())){
            onMessage("Please enter valid 10-digits Mobile Number");
            binding.mobileNumberEditTextRegi.requestFocus();
        }

        else if (!validateEmail(binding.emailEditTextRegi.getText().toString())){
            onMessage("Please enter valid Email Id");
        }

        else if (binding.addressEditTextRegi.getText().toString().equals("")) {
            onMessage("Enter your Address");
        } else {

             getUserRegister();

        }
    }

    public void getUserRegister() {
        Utils.showCustomProgressDialog(this, false);
        RegisterModel registerModel = new RegisterModel();
        registerModel.credentials.LanguageID = Integer.parseInt(sessionManager.getlanguageselection());
        registerModel.emailID = binding.emailEditTextRegi.getText().toString();
        registerModel.address = binding.addressEditTextRegi.getText().toString();
        registerModel.firstName = binding.firstNameEditTextRegi.getText().toString();
        registerModel.middleName = binding.middleNameEditTextRegi.getText().toString();
        registerModel.lastName = binding.lastNameEditTextRegi.getText().toString();
        registerModel.gender = gender;
        registerModel.mobileNumber = binding.mobileNumberEditTextRegi.getText().toString();
        registerModel.dateofbirth = binding.dobEditTextRegi.getText().toString();
        registerModel.pincode = binding.pincodeEditTextRegi.getText().toString();

        Call<SimpleResponse> call = RestClient.get().createRegister(registerModel, sessionManager.getMerchantName());

        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                Utils.hideCustomProgressDialog();
                assert response.body() != null;
                if (response.body().responseCode.equals(101)) {
                    showSucces();
                } else {
                    onMessage(response.body().description);
                }

            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                Utils.hideCustomProgressDialog();
                Toast.makeText(RegistrationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public boolean validateEmail(String strEmail) {
        Matcher matcher;
        String EMAIL_PATTERN = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
                + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
                + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(strEmail);
        return matcher.matches();
    }

    public void selectDateofBirth() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                binding.dobEditTextRegi.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

            }
        };
        DatePickerDialog dpDialog = new DatePickerDialog(RegistrationActivity.this, listener, year, month, day);

        dpDialog.show();
    }


    private void showSucces() {
        SingleButtonMessageDialog dialog = new
                SingleButtonMessageDialog(getString(R.string.successfully_txt)
                , ("Customer Registered Successfully"), this,
                false);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }


    public static boolean isValidPincodeNo(CharSequence pincode) {
        String PINCODE_PATTERN = "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$";
        Pattern pattern = Pattern.compile(PINCODE_PATTERN);
        Matcher matcher = pattern.matcher(pincode);
        return matcher.matches();
    }


    @Override
    public void onProceed() {
        finish();
    }
}
