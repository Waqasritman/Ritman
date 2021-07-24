package ritman.wallet.insurance;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import androidx.annotation.RequiresApi;
import androidx.navigation.Navigation;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.custom.SimpleCustomValidation;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;

import ritman.wallet.R;
import ritman.wallet.databinding.RegistrationFragmentBinding;
import ritman.wallet.fragments.BaseFragment;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class RegistrationFragment extends BaseFragment <RegistrationFragmentBinding>implements
        AdapterView.OnItemSelectedListener{

    String[] title = { " Select option "," Mr ", " Mrs ", " Ms "};
    String selected_title="";

      String[] gender = {" Select option ", " Male ", " Female "};
     String selected_gender = "";

    final Calendar myCalendar = Calendar.getInstance();
    int day, month, year;
    Calendar mcalendar;

    String sms_rle_branch_code,sms_or_rle_lg_code,dsa_code,branch_code,branch_lg_code,transaction_no,transaction_date,
    relationship_no,application_no,select_title,first_name,middle_name,last_name,dob,spin_gender,address_line1, address_line2;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {

        binding.spinTitle.setOnItemSelectedListener(this);
        ArrayAdapter aa_salutation = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,title);

        aa_salutation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinTitle.setAdapter(aa_salutation);

        mcalendar = Calendar.getInstance();
        day = mcalendar.get(Calendar.DAY_OF_MONTH);
        year = mcalendar.get(Calendar.YEAR);
        month = mcalendar.get(Calendar.MONTH);

        binding.edtDob.setFocusable(false);

        binding.spinGender.setOnItemSelectedListener(this);
        ArrayAdapter aa_gender = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, gender);

        aa_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinGender.setAdapter(aa_gender);

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

        binding.edtTransactionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.edtTransactionDate.setFocusableInTouchMode(true);
                binding.edtTransactionDate.setCursorVisible(false);
                binding.edtTransactionDate.setShowSoftInputOnFocus(false);
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), R.style.InsuranceRegistrationCalendarTheme,date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();


            }
        });

        binding.edtDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.edtDob.setFocusableInTouchMode(true);
                binding.edtDob.setCursorVisible(false);
                binding.edtDob.setShowSoftInputOnFocus(false);
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), R.style.InsuranceRegistrationCalendarTheme,date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();


            }
        });

        binding.btnNext.setOnClickListener(v -> {
           fieldsValidation();

        });


    }


    public void fieldsValidation(){

//        if (binding.edtTransactionDate.getText().toString().equals("")){
//            onMessage("Please enter Transaction Date");
//            binding.edtTransactionDate.requestFocus();
//        }
//
//
//        else if (binding.edtApplicationNo.getText().toString().equals("")){
//            onMessage("Please enter Application Number");
//            binding.edtApplicationNo.requestFocus();
//        }

       if (binding.spinTitle.getSelectedItemId()==0){
            onMessage("Please select Title");
            binding.spinTitle.requestFocus();
        }

        else if (binding.edtFirstname.getText().toString().equals("")){
            onMessage("Please enter First Name");
            binding.edtFirstname.requestFocus();
        }
        else if (binding.edtMiddleName.getText().toString().equals("")){
            onMessage("Please enter Middle Name");
            binding.edtMiddleName.requestFocus();
        }
        else if (binding.edtLastName.getText().toString().equals("")){
            onMessage("Please enter Last Name");
            binding.edtLastName.requestFocus();
        }
        else if (binding.edtDob.getText().toString().equals("")) {
            onMessage("Please select Date of Birth");
            binding.edtDob.requestFocus();
        }

       else if (binding.spinGender.getSelectedItemId() == 0) {
            onMessage("Please select Gender");
            binding.spinGender.requestFocus();
        }

       else if (binding.edtAddress1.getText().toString().equals("")) {
            onMessage("Please enter Address Line1");
            binding.edtAddress1.requestFocus();
        }
       else if (binding.edtAddress2.getText().toString().equals("")) {
            onMessage("Please enter Address Line2");
            binding.edtAddress2.requestFocus();
        }

        else {
            sms_rle_branch_code=binding.edtSmsRleBranchCode.getText().toString();
            sms_or_rle_lg_code=binding.edtSmsOrRleLgCode.getText().toString();
            dsa_code=binding.edtDsaCode.getText().toString();
            branch_code=binding.edtBranchCode.getText().toString();
            branch_lg_code=binding.edtBranchLgCode.getText().toString();
            transaction_no=binding.edtTransactionNo.getText().toString();
            transaction_date=binding.edtTransactionDate.getText().toString();
            relationship_no=binding.edtRefNo.getText().toString();
            application_no=binding.edtApplicationNo.getText().toString();
            select_title=selected_title;
            first_name=binding.edtFirstname.getText().toString();
            middle_name=binding.edtMiddleName.getText().toString();
            last_name=binding.edtLastName.getText().toString();
           dob = binding.edtDob.getText().toString();
           spin_gender = selected_gender;
           address_line1 = binding.edtAddress1.getText().toString();
            address_line2 = binding.edtAddress2.getText().toString();

            Bundle bundle=new Bundle();
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


            Navigation.findNavController(binding.getRoot()).navigate(R.id
                    .action_registrationFragment_to_registrationFragment1,bundle);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.registration_fragment;
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        binding.edtDob.setText(sdf.format(myCalendar.getTime()));

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        if (adapterView.getId() == R.id.spin_title) {
            int salutation_pos = adapterView.getSelectedItemPosition();
            if (salutation_pos == 1) {
                selected_title = "Mr";
            }
            else if (salutation_pos == 2) {
                selected_title = "Mrs";
            }
            else if (salutation_pos==3){
                selected_title = "Ms";
            }
        }
        if (adapterView.getId() == R.id.spin_gender) {
            int salutation_pos = adapterView.getSelectedItemPosition();
            if (salutation_pos == 1) {
               selected_gender = "Male";
            } else if (salutation_pos == 2) {
                selected_gender = "Female";
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
