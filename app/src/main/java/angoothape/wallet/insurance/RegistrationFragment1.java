package angoothape.wallet.insurance;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import angoothape.wallet.R;
import angoothape.wallet.databinding.RegistrationFragment1LayoutBinding;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.PinCodeListRequest;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.personal_loan.viewmodels.PersonalLoanViewModel;

public class RegistrationFragment1 extends BaseFragment<RegistrationFragment1LayoutBinding> implements
        AdapterView.OnItemSelectedListener {

    PersonalLoanViewModel viewModel;

  //  String[] gender = {" Select option ", " Male ", " Female "};
   // String selected_gender = "";

    String sms_rle_branch_code, sms_or_rle_lg_code, dsa_code, branch_code, branch_lg_code, transaction_no, transaction_date,
            relationship_no, application_no, select_title, first_name, middle_name, last_name;

    String dob, spin_gender, address_line1, address_line2, pincodeno, cityname, statename, mobileno, sum_assured, no_of_hospital_cash_benefit,
            hospital_cash_benefit, Premium_Payment_Frequency, Instalment_Premium;

    final Calendar myCalendar = Calendar.getInstance();
    int day, month, year;
    Calendar mcalendar;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(PersonalLoanViewModel.class);

        mcalendar = Calendar.getInstance();
        day = mcalendar.get(Calendar.DAY_OF_MONTH);
        year = mcalendar.get(Calendar.YEAR);
        month = mcalendar.get(Calendar.MONTH);
      //  binding.edtDob.setFocusable(false);

//        binding.spinGender.setOnItemSelectedListener(this);
//        ArrayAdapter aa_gender = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, gender);
//
//        aa_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        binding.spinGender.setAdapter(aa_gender);

        sms_rle_branch_code = getArguments().getString("sms_rle_branch_code");
        sms_or_rle_lg_code = getArguments().getString("sms_or_rle_lg_code");
        dsa_code = getArguments().getString("dsa_code");
        branch_code = getArguments().getString("branch_code");
        branch_lg_code = getArguments().getString("branch_lg_code");
        transaction_no = getArguments().getString("transaction_no");
        transaction_date = getArguments().getString("transaction_date");
        relationship_no = getArguments().getString("relationship_no");
        application_no = getArguments().getString("application_no");
        select_title = getArguments().getString("select_title");
        first_name = getArguments().getString("first_name");
        middle_name = getArguments().getString("middle_name");
        last_name = getArguments().getString("last_name");
        dob = getArguments().getString("dob");
        spin_gender = getArguments().getString("spin_gender");
        address_line1 = getArguments().getString("address_line1");
        address_line2 = getArguments().getString("address_line2");


        binding.personalNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fieldsValidation();

            }
        });

        binding.edtPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                binding.edtCity.setText("");
                binding.edtState.setText("");
                if (binding.edtPincode.getText().length() == 6) {

                    getPinCodeList();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
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
//        binding.edtDob.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                binding.edtDob.setFocusableInTouchMode(true);
//                binding.edtDob.setCursorVisible(false);
//                binding.edtDob.setShowSoftInputOnFocus(false);
//                // TODO Auto-generated method stub
//                new DatePickerDialog(getActivity(), R.style.InsuranceRegistrationCalendarTheme, date,
//                        myCalendar.get(Calendar.YEAR),
//                        myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//
//
//            }
//        });


    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
       // binding.edtDob.setText(sdf.format(myCalendar.getTime()));


    }


    public void fieldsValidation() {
//        if (binding.edtDob.getText().toString().equals("")) {
//            onMessage("Please select Date of Birth");
//            binding.edtDob.requestFocus();
//        }
//        else if (binding.spinGender.getSelectedItemId() == 0) {
//            onMessage("Please select Gender");
//            binding.spinGender.requestFocus();
//        }
//        else if (binding.edtAddress1.getText().toString().equals("")) {
//            onMessage("Please enter Address Line1");
//            binding.edtAddress1.requestFocus();
//        }
//        else if (binding.edtAddress2.getText().toString().equals("")) {
//            onMessage("Please enter Address Line2");
//            binding.edtAddress2.requestFocus();
//        }

        if (binding.edtPincode.getText().toString().equals("")) {
            onMessage("Please enter Pincode Number");
            binding.edtPincode.requestFocus();
        } else if (binding.edtPincode.getText().length() < 6) {
            onMessage("Please enter valid 6-digits pincode number");
            binding.edtPincode.requestFocus();
        } else if (binding.edtCity.getText().toString().equals("")) {
            onMessage("Please enter City Name");
            binding.edtCity.requestFocus();
        } else if (binding.edtState.getText().toString().equals("")) {
            onMessage("Please enter State Name");
            binding.edtState.requestFocus();
        } else if (binding.edtMobileNumber.getText().toString().equals("")) {
            onMessage("Please enter Mobile Number");
            binding.edtMobileNumber.requestFocus();
        }
        else if (binding.edtMobileNumber.getText().toString().length()!=10){
            onMessage("Please enter Valid 10-digits Mobile Number");
            binding.edtMobileNumber.requestFocus();
        }
        else if (binding.edtSumAssured.getText().toString().equals("")) {
            onMessage("Please enter Sum Assured");
            binding.edtSumAssured.requestFocus();
        } else if (binding.edtNoOfHospitalCashBenefit.getText().toString().equals("")) {
            onMessage("Please enter No of Hospital Cash Benefit ");
            binding.edtNoOfHospitalCashBenefit.requestFocus();
        } else if (binding.edtHospitalCashBenefit.getText().toString().equals("")) {
            onMessage("Please enter Hospital Cash Benefit ");
            binding.edtHospitalCashBenefit.requestFocus();
        } else if (binding.edtPremiumPaymentFrequency.getText().toString().equals("")) {
            onMessage("Please enter Premium Payment Frequency ");
            binding.edtPremiumPaymentFrequency.requestFocus();
        } else if (binding.edtInstalmentPremium.getText().toString().equals("")) {
            onMessage("Please enter Instalment Premium ");
            binding.edtInstalmentPremium.requestFocus();
        } else {
          //  dob = binding.edtDob.getText().toString();
           // spin_gender = selected_gender;
            //address_line1 = binding.edtAddress1.getText().toString();
           // address_line2 = binding.edtAddress2.getText().toString();
            pincodeno = binding.edtPincode.getText().toString();
            cityname = binding.edtCity.getText().toString();
            statename = binding.edtState.getText().toString();
            mobileno = binding.edtMobileNumber.getText().toString();
            sum_assured = binding.edtSumAssured.getText().toString();
            no_of_hospital_cash_benefit = binding.edtNoOfHospitalCashBenefit.getText().toString();
            hospital_cash_benefit = binding.edtHospitalCashBenefit.getText().toString();
            Premium_Payment_Frequency = binding.edtPremiumPaymentFrequency.getText().toString();
            Instalment_Premium = binding.edtInstalmentPremium.getText().toString();

            Bundle bundle = new Bundle();

            bundle.putString("sms_rle_branch_code",sms_rle_branch_code);
            bundle.putString("sms_or_rle_lg_code",sms_or_rle_lg_code);
            bundle.putString("dsa_code",dsa_code);
            bundle.putString("branch_code",branch_code);
            bundle.putString("branch_lg_code",branch_lg_code);
            bundle.putString("transaction_no",transaction_no);
            bundle.putString("transaction_date",transaction_date);
            bundle.putString("relationship_no",relationship_no);
            bundle.putString("application_no",application_no);
            bundle.putString("select_title",select_title);
            bundle.putString("first_name",first_name);
            bundle.putString("middle_name",middle_name);
            bundle.putString("last_name",last_name);

            bundle.putString("dob",dob);
            bundle.putString("spin_gender",spin_gender);
            bundle.putString("address_line1",address_line1);
            bundle.putString("address_line2",address_line2);
            bundle.putString("pincodeno",pincodeno);
            bundle.putString("cityname",cityname);
            bundle.putString("statename",statename);
            bundle.putString("mobileno",mobileno);
            bundle.putString("sum_assured",sum_assured);
            bundle.putString("no_of_hospital_cash_benefit",no_of_hospital_cash_benefit);
            bundle.putString("hospital_cash_benefit",hospital_cash_benefit);
            bundle.putString("Premium_Payment_Frequency",Premium_Payment_Frequency);
            bundle.putString("Instalment_Premium",Instalment_Premium);

            Navigation.findNavController(binding.getRoot()).navigate(R.id
                    .action_registrationFragment1_to_registrationFragment2,bundle);
        }
    }


    public void getPinCodeList() {
        PinCodeListRequest request = new PinCodeListRequest();
        request.Pincode = binding.edtPincode.getText().toString();

        viewModel.getCashePincodeList(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()

                , response -> {
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {

                            viewModel.city.setValue(response.resource.data.get(0).City);
                            viewModel.state.setValue(response.resource.data.get(0).getState());
                            binding.edtCity.setText(viewModel.city.getValue());
                            binding.edtState.setText(viewModel.state.getValue());

                        } else {
                            onMessage(response.resource.description);
                            binding.edtCity.setText("");
                            binding.edtState.setText("");
                        }
                    }
                });
    }

    public static boolean isValidEmail(CharSequence email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    @Override
    public int getLayoutId() {
        return R.layout.registration_fragment_1_layout;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        if (adapterView.getId() == R.id.spin_gender) {
            int salutation_pos = adapterView.getSelectedItemPosition();
            if (salutation_pos == 1) {
              //  selected_gender = "Male";
            } else if (salutation_pos == 2) {
               // selected_gender = "Female";
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
