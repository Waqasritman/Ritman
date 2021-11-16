package angoothape.wallet.personal_loan.fragment;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.navigation.Navigation;

import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;

import com.basgeekball.awesomevalidation.AwesomeValidation;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;

import angoothape.wallet.R;
import angoothape.wallet.databinding.FragmentPersonalZeroBinding;
import angoothape.wallet.fragments.BaseFragment;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class Personal_ZeroFragment extends BaseFragment<FragmentPersonalZeroBinding> {


    private AwesomeValidation mAwesomeValidation;
    final Calendar myCalendar = Calendar.getInstance();
    int day, month, year, age;
    Calendar mcalendar;
    String gender = "M";
    int yearOfToday;
    int yearOfBirthday;
    String mPattern = "^[a-zA-Z0-9-,. ]*$";
    String edt_firstname, edt_lastname, edt_dob, selectedgender, address_line_1, address_line_2, edt_landmark;
    SimpleDateFormat sdf;
    String myFormat;

    @Override
    protected void injectView() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void setUp(Bundle savedInstanceState) {
        mcalendar = Calendar.getInstance();
        day = mcalendar.get(Calendar.DAY_OF_MONTH);
        year = mcalendar.get(Calendar.YEAR);
        month = mcalendar.get(Calendar.MONTH);

        binding.edtDob.setFocusable(false);
        mAwesomeValidation = new AwesomeValidation(BASIC);

        mAwesomeValidation.addValidation(binding.edtFirstname, "^[A-Za-z\\s]+", getResources().getString(R.string.firstname_personal));
        mAwesomeValidation.addValidation(binding.edtLastname, "^[A-Za-z\\s]+", getResources().getString(R.string.lastname_personal));
        // mAwesomeValidation.addValidation(binding.addressLine1, "^[a-zA-Z0-9-,. ]*$", getResources().getString(R.string.addre_line_one));
        //mAwesomeValidation.addValidation(binding.addressLine2, "^[a-zA-Z0-9-,. ]*$", getResources().getString(R.string.addre_line_two));
        // mAwesomeValidation.addValidation(binding.edtLandmark, "[\\w\\s-,.]+$", getResources().getString(R.string.landmark));
        //mAwesomeValidation.addValidation(binding.dobEditTextRegi, "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$", getResources().getString(R.string.date_of_birth_ex));

//        mAwesomeValidation.addValidation(getActivity(), R.id.edt_dob, new SimpleCustomValidation() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public boolean compare(String input) {
//                LocalDate currentDate = LocalDate.now();
//                yearOfToday = currentDate.getYear();
//                yearOfBirthday = myCalendar.get(Calendar.YEAR);
//                age = yearOfToday - yearOfBirthday;
//                if (age >= 18 && age <= 55) {
//                    mAwesomeValidation.clear();
//                    return true;
//                }
//
//                else {
//
//                    return false;
//                }
//
//
//            }
//
//        }, R.string.err_birth);


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

        binding.edtDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.edtDob.setFocusableInTouchMode(true);
                binding.edtDob.setCursorVisible(false);
                binding.edtDob.setShowSoftInputOnFocus(false);
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), R.style.PersonalLoanCalendarTheme, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });


        binding.personalNext.setOnClickListener(v -> {

            edt_firstname = binding.edtFirstname.getText().toString();
            edt_lastname = binding.edtLastname.getText().toString();
            address_line_1 = binding.addressLine1.getText().toString();
            address_line_2 = binding.addressLine2.getText().toString();
            edt_landmark = binding.edtLandmark.getText().toString();
            edt_dob = binding.edtDob.getText().toString();
            selectedgender = gender;

            LocalDate currentDate = LocalDate.now();
            yearOfToday = currentDate.getYear();
            yearOfBirthday = myCalendar.get(Calendar.YEAR);
            age = yearOfToday - yearOfBirthday;

            if (binding.edtFirstname.getText().toString().equals("")) {
                onMessage("Please enter your First Name");
            }
            else if (binding.edtLastname.getText().toString().equals("")) {
                onMessage("Please enter your Last Name");
            }

            else if (binding.edtDob.getText().toString().equals("")) {
                onMessage("Please enter your Date Of Birth (D.O.B.)");
            }

            else if (age < 18 || age > 55) {
                onMessage("Age Cannot be less than 18-years or greater than 55-years");
            }

           else if (address_line_1.length() < 10 || address_line_1.length() > 512) {
                onMessage("Address length should be between 10 to 512 characters");
            }
            else if (!binding.addressLine1.getText().toString().matches(mPattern)) {
                onMessage(getResources().getString(R.string.addre_line_one));
            }
            else if (address_line_2.length() < 10 || address_line_2.length() > 512) {
                onMessage("Address length should be between 10 to 512 characters");
            }
           else if (!binding.addressLine2.getText().toString().matches(mPattern)) {
                onMessage(getResources().getString(R.string.addre_line_two));
            }
           else if (binding.edtLandmark.getText().toString().equals("")){
               onMessage("Please enter your Landmark");
            }
           else {
                Bundle bundle = new Bundle();
                bundle.putString("edt_firstname", edt_firstname);
                bundle.putString("edt_lastname", edt_lastname);
                bundle.putString("address_line_1", address_line_1);
                bundle.putString("address_line_2", address_line_2);
                bundle.putString("edt_landmark", edt_landmark);
                bundle.putString("edt_dob", edt_dob);
                bundle.putString("selectedgender", selectedgender);

                Navigation.findNavController(binding.getRoot()).navigate(R.id
                        .action_personal_ZeroFragment_to_personal_One_Fragment2, bundle);
            }


        });

    }


    private void updateLabel() {
        myFormat = "MM/dd/yyyy"; //In which you need put here
        sdf = new SimpleDateFormat(myFormat, Locale.US);
        binding.edtDob.setText(sdf.format(myCalendar.getTime()));

    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_personal__zero;
    }

}