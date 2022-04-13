package angoothape.wallet.insurance;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Response;
import angoothape.wallet.R;
import angoothape.wallet.databinding.RegistrationFragment2LayoutBinding;
import angoothape.wallet.di.JSONdi.restRequest.HospicareMemebrFormRequest;
import angoothape.wallet.di.JSONdi.restRequest.esb_hospicare_group_req;
import angoothape.wallet.di.JSONdi.restResponse.HospicareMemebrFormResponse;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.dialogs.SingleButtonMessageDialog;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.interfaces.OnDecisionMade;
import angoothape.wallet.utils.Utils;

public class RegistrationFragment2 extends BaseFragment<RegistrationFragment2LayoutBinding> implements OnDecisionMade,
        AdapterView.OnItemSelectedListener {

    final Calendar myCalendar = Calendar.getInstance();
    final Calendar myCalendar1 = Calendar.getInstance();
    final Calendar myCalendar2 = Calendar.getInstance();
    final Calendar myCalendar3 = Calendar.getInstance();
    int day, month, year;
    Calendar mcalendar;

    String[] member_relationship = {" Select option ", " Spouse ", " Son ", " Daughter ", " Mother ", " Father "};
    String selected_member_relationship = "";

    String[] nominee_relationship = {" Select option ", " Spouse ", " Son ", " Daughter ", " Mother ", " Father "};
    String selected_nominee_relationship = "";

    String sms_rle_branch_code, sms_or_rle_lg_code, dsa_code, branch_code, branch_lg_code, transaction_no, transaction_date,
            relationship_no, application_no, select_title, first_name, middle_name, last_name;

    String dob, spin_gender, address_line1, address_line2, pincodeno, cityname, statename, mobileno, sum_assured, no_of_hospital_cash_benefit,
            hospital_cash_benefit, Premium_Payment_Frequency, Instalment_Premium;

    String nominee_name,Percentage_Share,nominee_dob,Relationship_with_Member,Appointee_Name,Appointee_DOB,
            Relationship_with_Nominee,dogh_flag,otp_consent_flag,OTP_Consent_Date_Time_Stamp,Source,
            mph_name,date_time_of_insertion,Product_Type,emailId;

   @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        mcalendar = Calendar.getInstance();
        day = mcalendar.get(Calendar.DAY_OF_MONTH);
        year = mcalendar.get(Calendar.YEAR);
        month = mcalendar.get(Calendar.MONTH);
        binding.edtDob.setFocusable(false);

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

        dob=getArguments().getString("dob");
        spin_gender=getArguments().getString("spin_gender");
        address_line1=getArguments().getString("address_line1");
        address_line2=getArguments().getString("address_line2");
        pincodeno=getArguments().getString("pincodeno");
        cityname=getArguments().getString("cityname");
        statename=getArguments().getString("statename");
        mobileno=getArguments().getString("mobileno");
        sum_assured=getArguments().getString("sum_assured");
        no_of_hospital_cash_benefit=getArguments().getString("no_of_hospital_cash_benefit");
        hospital_cash_benefit=getArguments().getString("hospital_cash_benefit");
        Premium_Payment_Frequency=getArguments().getString("Premium_Payment_Frequency");
        Instalment_Premium=getArguments().getString("Instalment_Premium");

        binding.spinMemberRelationship.setOnItemSelectedListener(this);
        ArrayAdapter aa_member_relationship = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, member_relationship);

        aa_member_relationship.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinMemberRelationship.setAdapter(aa_member_relationship);


        binding.spinRelationshipWithNominee.setOnItemSelectedListener(this);
        ArrayAdapter aa_nominee_relationship = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, nominee_relationship);

        aa_nominee_relationship.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinRelationshipWithNominee.setAdapter(aa_nominee_relationship);


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

        DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub

                myCalendar1.set(Calendar.YEAR, year);
                myCalendar1.set(Calendar.MONTH, monthOfYear);
                myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabel1();
            }
        };

        DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub

                myCalendar2.set(Calendar.YEAR, year);
                myCalendar2.set(Calendar.MONTH, monthOfYear);
                myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabel2();
            }
        };

        DatePickerDialog.OnDateSetListener date3 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub

                myCalendar3.set(Calendar.YEAR, year);
                myCalendar3.set(Calendar.MONTH, monthOfYear);
                myCalendar3.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabel3();
            }
        };

        binding.edtDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.edtDob.setFocusableInTouchMode(true);
                binding.edtDob.setCursorVisible(false);
                binding.edtDob.setShowSoftInputOnFocus(false);
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), R.style.InsuranceRegistrationCalendarTheme, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();



            }
        });

        binding.edtOTPConsentDateTimeStamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.edtOTPConsentDateTimeStamp.setFocusableInTouchMode(true);
                binding.edtOTPConsentDateTimeStamp.setCursorVisible(false);
                binding.edtOTPConsentDateTimeStamp.setShowSoftInputOnFocus(false);
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), R.style.InsuranceRegistrationCalendarTheme, date1,
                        myCalendar1.get(Calendar.YEAR),
                        myCalendar1.get(Calendar.MONTH),
                        myCalendar1.get(Calendar.DAY_OF_MONTH)).show();


            }
        });

        binding.edtDateTimeOfInsertion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.edtDateTimeOfInsertion.setFocusableInTouchMode(true);
                binding.edtDateTimeOfInsertion.setCursorVisible(false);
                binding.edtDateTimeOfInsertion.setShowSoftInputOnFocus(false);
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), R.style.InsuranceRegistrationCalendarTheme, date2,
                        myCalendar2.get(Calendar.YEAR),
                        myCalendar2.get(Calendar.MONTH),
                        myCalendar2.get(Calendar.DAY_OF_MONTH)).show();


            }
        });

        binding.edtAppointeeDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.edtAppointeeDob.setFocusableInTouchMode(true);
                binding.edtAppointeeDob.setCursorVisible(false);
                binding.edtAppointeeDob.setShowSoftInputOnFocus(false);
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), R.style.InsuranceRegistrationCalendarTheme, date3,
                        myCalendar3.get(Calendar.YEAR),
                        myCalendar3.get(Calendar.MONTH),
                        myCalendar3.get(Calendar.DAY_OF_MONTH)).show();


            }
        });

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fieldsValidation();

            }


        });



    }

    public void fieldsValidation() {
        if (binding.edtNomineeName.getText().toString().equals("")) {
            onMessage("Please enter Nominee Name");
            binding.edtNomineeName.requestFocus();
        }
        else if (binding.edtPercentageShare.getText().toString().equals("")){
            onMessage("Please enter Percentage Share");
            binding.edtPercentageShare.requestFocus();
        }
        else if (binding.edtDob.getText().toString().equals("")){
            onMessage("Please select Nominee Date of Birth");
            binding.edtDob.requestFocus();
        }
        else if (binding.spinMemberRelationship.getSelectedItemId()==0){
            onMessage("Please select Relationship with member");
            binding.spinMemberRelationship.requestFocus();
        }
//        else if (binding.edtAppointeeName.getText().toString().equals("")){
//            onMessage("Please select enter Appointee Name");
//            binding.edtAppointeeName.requestFocus();
//        }
//        else if (binding.edtAppointeeDob.getText().toString().equals("")){
//            onMessage("Please select Appointee Date of Birth");
//            binding.edtAppointeeDob.requestFocus();
//        }
//        else if (binding.spinRelationshipWithNominee.getSelectedItemId()==0){
//            onMessage("Please select Relationship with Nominee");
//            binding.spinRelationshipWithNominee.requestFocus();
//        }

//        else if (binding.edtDoghFlag.getText().toString().equals("")){
//            onMessage("Please enter Dogh Flag");
//            binding.edtDoghFlag.requestFocus();
//        }
//
//        else if (binding.edtOtpConsentFlag.getText().toString().equals("")){
//            onMessage("Please enter Otp Consent Flag");
//            binding.edtOtpConsentFlag.requestFocus();
//        }
//
//        else if (binding.edtOTPConsentDateTimeStamp.getText().toString().equals("")){
//            onMessage("Please select Otp Consent DateTime Stamp");
//            binding.edtOTPConsentDateTimeStamp.requestFocus();
//        }
//
//        else if (binding.edtSource.getText().toString().equals("")){
//            onMessage("Please enter Source");
//            binding.edtSource.requestFocus();
//        }
//
//        else if (binding.edtMphName.getText().toString().equals("")){
//            onMessage("Please enter MPH Name");
//            binding.edtMphName.requestFocus();
//        }
//
//        else if (binding.edtDateTimeOfInsertion.getText().toString().equals("")){
//            onMessage("Please select Date Time of Insertion");
//            binding.edtDateTimeOfInsertion.requestFocus();
//        }
//
//        else if (binding.edtProductType.getText().toString().equals("")){
//            onMessage("Please enter Product Type");
//            binding.edtProductType.requestFocus();
//        }

        else if (binding.edtEmailId.getText().toString().equals("")){
            onMessage("Please enter Email Id");
            binding.edtEmailId.requestFocus();
        }
        else if (!isValidEmail(binding.edtEmailId.getText().toString())){
            onMessage("Please enter Valid Email Id");
            binding.edtEmailId.requestFocus();
        }
        else if (!(binding.checkGoodHealthDeclaration).isChecked()){
            onMessage("Please accept Good Health Declaration condition");
            binding.checkGoodHealthDeclaration.requestFocus();
        }
        else if (!(binding.checkCovidQuestionnaire).isChecked()){
            onMessage("Please accept Covid Questionnaire condition");
            binding.checkCovidQuestionnaire.requestFocus();
        }

        else if (!(binding.checkTermsCondition).isChecked()){
            onMessage("Please accept Terms condition");
            binding.checkTermsCondition.requestFocus();
        }

        else {
            nominee_name=binding.edtNomineeName.getText().toString();
            Percentage_Share=binding.edtPercentageShare.getText().toString();
            nominee_dob=binding.edtDob.getText().toString();
            Relationship_with_Member=selected_member_relationship;
            Appointee_Name=binding.edtAppointeeName.getText().toString();
            Appointee_DOB=binding.edtAppointeeDob.getText().toString();
            Relationship_with_Nominee=selected_nominee_relationship;
            dogh_flag=binding.edtDoghFlag.getText().toString();
            otp_consent_flag=binding.edtOtpConsentFlag.getText().toString();
            OTP_Consent_Date_Time_Stamp=binding.edtOTPConsentDateTimeStamp.getText().toString();
            Source=binding.edtSource.getText().toString();
            mph_name=binding.edtMphName.getText().toString();
            date_time_of_insertion=binding.edtDateTimeOfInsertion.getText().toString();
            Product_Type=binding.edtProductType.getText().toString();
            emailId=binding.edtEmailId.getText().toString();

            getHospicareMemberForm();
        }
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        binding.edtDob.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel1() {
        String myFormat = "MM/dd/yyyy HH:mm:ss"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        binding.edtOTPConsentDateTimeStamp.setText(sdf.format(myCalendar1.getTime()));
       // binding.edtDateTimeOfInsertion.setText(sdf.format(myCalendar.getTime()));

    }

    private void updateLabel2() {
        String myFormat = "MM/dd/yyyy HH:mm:ss"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        binding.edtDateTimeOfInsertion.setText(sdf.format(myCalendar2.getTime()));
        // binding.edtDateTimeOfInsertion.setText(sdf.format(myCalendar.getTime()));

    }

    private void updateLabel3() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        binding.edtAppointeeDob.setText(sdf.format(myCalendar3.getTime()));
        // binding.edtDateTimeOfInsertion.setText(sdf.format(myCalendar.getTime()));

    }

    private void showSucces() {
        SingleButtonMessageDialog dialog = new
                SingleButtonMessageDialog(getString(R.string.successfully_txt)
                , ("Customer Registered Successfully"), this,
                false);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }

    @Override
    public int getLayoutId() {
        return R.layout.registration_fragment_2_layout;
    }

    @Override
    public void onProceed() {
        getActivity().finish();
    }

    @Override
    public void onCancel(boolean goBack)  {

    }

    public void getHospicareMemberForm(){
        Utils.showCustomProgressDialog(getContext(), false);
      HospicareMemebrFormRequest hospicarememebrFormrequest = new HospicareMemebrFormRequest();
       // hospicarememebrFormrequest.hospicareGroup=new esb_hospicare_group_req();
        esb_hospicare_group_req hospicareGroup=new esb_hospicare_group_req();

        hospicareGroup.SMS_or_RLE_Branch_Code="sms_rle_branch_code";
        hospicareGroup.SMS_or_RLE_LG_Code="sms_or_rle_lg_code";
        hospicareGroup.DSA_Code="dsa_code";
        hospicareGroup.Branch_Code="branch_code";
        hospicareGroup.Branch_LG_Code="branch_lg_code";
        hospicareGroup.Transaction_No="transaction_no";
        hospicareGroup.Date_of_Transaction="transaction_date";
        hospicareGroup.Account_or_Reference_or_Relationship_No="relationship_no";
        hospicareGroup.Application_Number="application_no";
        hospicareGroup.Title=select_title;
        hospicareGroup.First_Name=first_name;
        hospicareGroup.Middle_Name=middle_name;
        hospicareGroup.Last_Name=last_name;
        hospicareGroup.Date_Of_Birth=dob;
        hospicareGroup.Gender=spin_gender;
        hospicareGroup.Adress_Line_1=address_line1;
        hospicareGroup.Adress_Line_2=address_line2;
        hospicareGroup.City=cityname;
        hospicareGroup.State=statename;
        hospicareGroup.Pincode=pincodeno;
        hospicareGroup.Mobile_Number=mobileno;
        hospicareGroup.Sum_Assured=sum_assured;
        hospicareGroup.No_of_Hospital_Cash_Benefit_during_term_of_the_policy=no_of_hospital_cash_benefit;
        hospicareGroup.Hospital_Cash_Benefit= Integer.valueOf(hospital_cash_benefit);
        hospicareGroup.Premium_Payment_Frequency=Premium_Payment_Frequency;
        hospicareGroup.Instalment_Premium=Instalment_Premium;

        hospicareGroup.Nominee_Name=nominee_name;
        hospicareGroup.Percentage_Share=Percentage_Share;
        hospicareGroup.DOB_Of_Nominee=nominee_dob;
        hospicareGroup.Relationship_with_Member=Relationship_with_Member;
        hospicareGroup.Appointee_Name=Appointee_Name;
        hospicareGroup.Appointee_DOB=Appointee_DOB;
        hospicareGroup.Relationship_with_Nominee=Relationship_with_Nominee;
        hospicareGroup.DOGH_Flag="dogh_flag";
        hospicareGroup.OTP_Consent_Flag="otp_consent_flag";
        hospicareGroup.OTP_Consent_Date_Time_Stamp="OTP_Consent_Date_Time_Stamp";
        hospicareGroup.Source="Source";
        hospicareGroup.MPH_Name="mph_name";
        hospicareGroup.Date_Time_Of_Insertion="date_time_of_insertion";
        hospicareGroup.Product_Type="Product_Type";
        hospicareGroup.email_ID=emailId;
        hospicarememebrFormrequest.esb_hospicare_group_req=hospicareGroup;

        Call<HospicareMemebrFormResponse> call = RestClient.getEKYC().getHospicareMemberForm(hospicarememebrFormrequest, getSessionManager().getMerchantName());
        call.enqueue(new retrofit2.Callback<HospicareMemebrFormResponse>() {
            @Override
            public void onResponse(Call<HospicareMemebrFormResponse> call, Response<HospicareMemebrFormResponse> response) {
                Utils.hideCustomProgressDialog();
                assert response.body() != null;

                if (response.body().responseCode.equals(101)){
                    onMessage(response.body().description);
                   // showSucces();
                    Navigation.findNavController(binding.getRoot()).
                            navigate(R.id.action_registrationFragment2_to_verifyOtpFragment);
                }

                else {
                    onError(response.body().description);
                    Utils.hideCustomProgressDialog();
                }

            }

            @Override
            public void onFailure(Call<HospicareMemebrFormResponse> call, Throwable t) {
                Utils.hideCustomProgressDialog();
                // Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                //onError(response.body().description);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        if (adapterView.getId() == R.id.spin_member_relationship) {
            int member_relationship = adapterView.getSelectedItemPosition();
            if (member_relationship == 1) {
                selected_member_relationship = "Spouse";
            } else if (member_relationship == 2) {
                selected_member_relationship = "Son";
            } else if (member_relationship == 3) {
                selected_member_relationship = "Daughter";
            } else if (member_relationship == 4) {
                selected_member_relationship = "Mother";
            } else if (member_relationship == 5) {
                selected_member_relationship = "Father";
            }
        }


        if (adapterView.getId() == R.id.spin_relationship_with_nominee) {
            int nominee_relationship = adapterView.getSelectedItemPosition();
            if (nominee_relationship == 1) {
                selected_nominee_relationship = "Spouse";
            } else if (nominee_relationship == 2) {
                selected_nominee_relationship = "Son";
            } else if (nominee_relationship == 3) {
                selected_nominee_relationship = "Daughter";
            } else if (nominee_relationship == 4) {
                selected_nominee_relationship = "Mother";
            } else if (nominee_relationship == 5) {
                selected_nominee_relationship = "Father";
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static boolean isValidEmail(CharSequence email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
