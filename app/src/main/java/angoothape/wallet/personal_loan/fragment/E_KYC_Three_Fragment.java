package angoothape.wallet.personal_loan.fragment;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.custom.SimpleCustomValidation;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;

import angoothape.wallet.R;
import angoothape.wallet.databinding.FragmentEKYCThreeBinding;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.CashePreApprovalRequest;
import angoothape.wallet.dialogs.SingleButtonMessageDialog;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.interfaces.OnDecisionMade;
import angoothape.wallet.personal_loan.viewmodels.PersonalLoanViewModel;
import angoothape.wallet.utils.Utils;

import static android.content.Context.MODE_PRIVATE;
import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class E_KYC_Three_Fragment extends BaseFragment<FragmentEKYCThreeBinding> implements OnDecisionMade {

    private AwesomeValidation mAwesomeValidation;
    PersonalLoanViewModel viewModel;
    String edt_firstname, edt_lastname, address_line_1, address_line_2, edt_landmark, edt_dob, selectedgender;

    String edt_pincode, edt_city, edt_state, txt_accommondation, edt_pan, edt_adhaar, txt_qualification;

    String txt_residing_with, txt_number_of_years_at_current_addressr, edt_spouse_status, txt_no_of_kids, selected_marital_status;

    String edt_company_name, edt_office_phone_no, txt_designation, edt_monthly_income, edt_no_of_years_in_current_work,
            edt_official_email, edt_office_address_1,edt_loan_amount;

    String edt_office_address_2;
    String edt_landmark_office;
    String edt_office_pincode;
    String edt_office_city;
    String edt_office_state;
    String edt_working_since;
    Integer txt_employment_type;

    Integer txt_salary_received_type_id;
    String txt_work_sector;
    String txt_job_function;
    String txt_organization;

    String edt_existing_bank, edt_branch_name, edt_ifsc_code, edt_ac_no, edt_re_enter_account_number, edt_remark;

    String edt_mobile, edt_emailid;

    String edt_co, edt_street, edt_house, edt_Lm, edt_vtc, edt_subdist;

    String edt_dist, edt_kyc_state, edt_pc, edt_po, edt_adharno, edt_name;

    String dobEditTextRegi, edt_compressedAddress;

    String customerId;


    final Calendar myCalendar = Calendar.getInstance();
    int day, month, year, age;
    Calendar mcalendar;
    String kyc_gender = "M";
    int yearOfToday;
    int yearOfBirthday;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(PersonalLoanViewModel.class);
        edt_firstname = getArguments().getString("edt_firstname");
        edt_lastname = getArguments().getString("edt_lastname");
        address_line_1 = getArguments().getString("address_line_1");
        address_line_2 = getArguments().getString("address_line_2");
        edt_landmark = getArguments().getString("edt_landmark");
        edt_dob = getArguments().getString("edt_dob");
        selectedgender = getArguments().getString("selectedgender");


        edt_pincode = getArguments().getString("edt_pincode");
        edt_city = getArguments().getString("edt_city");
        edt_state = getArguments().getString("edt_state");
        txt_accommondation = getArguments().getString("txt_accommondation");
        edt_pan = getArguments().getString("edt_pan");
        edt_adhaar = getArguments().getString("edt_adhaar");
        txt_qualification = getArguments().getString("txt_qualification");


        txt_residing_with = getArguments().getString("txt_residing_with");
        txt_number_of_years_at_current_addressr = getArguments().getString("txt_number_of_years_at_current_addressr");
        edt_spouse_status = getArguments().getString("edt_spouse_status");
        txt_no_of_kids = getArguments().getString("txt_no_of_kids");
        selected_marital_status = getArguments().getString("selected_marital_status");


        edt_company_name = getArguments().getString("edt_company_name");
        edt_office_phone_no = getArguments().getString("edt_office_phone_no");
        txt_designation = getArguments().getString("txt_designation");
        edt_monthly_income = getArguments().getString("edt_monthly_income");
        edt_no_of_years_in_current_work = getArguments().getString("edt_no_of_years_in_current_work");
        edt_official_email = getArguments().getString("edt_official_email");
        edt_office_address_1 = getArguments().getString("edt_office_address_1");
        edt_loan_amount = getArguments().getString("edt_loan_amount");


        edt_office_address_2 = getArguments().getString("edt_office_address_2");
        edt_landmark_office = getArguments().getString("edt_landmark_office");
        edt_office_pincode = getArguments().getString("edt_office_pincode");
        edt_office_city = getArguments().getString("edt_office_city");
        edt_office_state = getArguments().getString("edt_office_state");
        edt_working_since = getArguments().getString("edt_working_since");
        txt_employment_type = Integer.valueOf(getArguments().getString("txt_employment_type"));

        txt_salary_received_type_id = Integer.valueOf(getArguments().getString("txt_salary_received_type_id"));
        txt_work_sector = getArguments().getString("txt_work_sector");
        txt_job_function = getArguments().getString("txt_job_function");
        txt_organization = getArguments().getString("txt_organization");

        edt_existing_bank = getArguments().getString("edt_existing_bank");
        edt_branch_name = getArguments().getString("edt_branch_name");
        edt_ifsc_code = getArguments().getString("edt_ifsc_code");
        edt_ac_no = getArguments().getString("edt_ac_no");
        edt_re_enter_account_number = getArguments().getString("edt_re_enter_account_number");
        edt_remark = getArguments().getString("edt_remark");

        edt_mobile = getArguments().getString("edt_mobile");
        edt_emailid = getArguments().getString("edt_emailid");


        edt_co = getArguments().getString("edt_co");
        edt_street = getArguments().getString("edt_street");
        edt_house = getArguments().getString("edt_house");
        edt_Lm = getArguments().getString("edt_Lm");
        edt_vtc = getArguments().getString("edt_vtc");
        edt_subdist = getArguments().getString("edt_subdist");

        edt_dist = getArguments().getString("edt_dist");
        edt_kyc_state = getArguments().getString("edt_kyc_state");
        edt_pc = getArguments().getString("edt_pc");
        edt_po = getArguments().getString("edt_po");
        edt_adharno = getArguments().getString("edt_adharno");
        edt_name = getArguments().getString("edt_name");


        mcalendar = Calendar.getInstance();
        day = mcalendar.get(Calendar.DAY_OF_MONTH);
        year = mcalendar.get(Calendar.YEAR);
        month = mcalendar.get(Calendar.MONTH);
        binding.dobEditTextRegi.setFocusable(false);

        mAwesomeValidation = new AwesomeValidation(BASIC);
        mAwesomeValidation.addValidation(binding.edtCompressedAddress, "[\\w\\s-,.]+$", getResources().getString(R.string.addre_line_one));
       // mAwesomeValidation.addValidation(binding.dobEditTextRegi, "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$", getResources().getString(R.string.date_of_birth_ex));

        mAwesomeValidation.addValidation(getActivity(), R.id.dobEditTextRegi, new SimpleCustomValidation() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean compare(String input) {
                LocalDate currentDate = LocalDate.now();
                yearOfToday = currentDate.getYear();
                yearOfBirthday = myCalendar.get(Calendar.YEAR);

                age = yearOfToday - yearOfBirthday;
                if (age >= 18 && age <= 55) {
                    mAwesomeValidation.clear();
                    return true;
                }

                else {

                    return false;
                }


            }

        }, R.string.err_birth);


        binding.maleFemaleRadioGroupRegi.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = group.findViewById(checkedId);
            kyc_gender = radioButton.getText().toString().substring(0, 1);
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
                new DatePickerDialog(getActivity(), R.style.PersonalLoanCalendarTheme, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                LocalDate currentDate = LocalDate.now();
                yearOfToday = currentDate.getYear();
                yearOfBirthday = myCalendar.get(Calendar.YEAR);
                age = yearOfToday - yearOfBirthday;
              if (binding.dobEditTextRegi.getText().toString().equals("")){
                  onMessage("Please enter your Date of Birth");
              }
              else if (age < 18 || age > 55) {
                  onMessage("Age Cannot be less than 18-years or greater than 55-years");
              }
               else if (binding.edtCompressedAddress.length()<10 ||binding.edtCompressedAddress.length()>512) {
                        onMessage("Address length should be between 10 to 512 characters");
                    }
                    else {
                        createCustome();
                    }



            }
        });


    }


    public void createCustome() {
        Utils.showCustomProgressDialog(getContext(), false);
        dobEditTextRegi = binding.dobEditTextRegi.getText().toString();
        edt_compressedAddress = binding.edtCompressedAddress.getText().toString();

        CashePreApprovalRequest preApprovalRequest = new CashePreApprovalRequest();

        preApprovalRequest.partner_name = "RitmanPay_Partner";
        preApprovalRequest.reference_Id = "123";
        preApprovalRequest.applicant_id = "456";
        preApprovalRequest.loan_amount = edt_loan_amount;//"5000";
        preApprovalRequest.product_type_name = "CASHe_30";
        preApprovalRequest.First_Name=edt_firstname;
        preApprovalRequest.Last_Name=edt_lastname;
        preApprovalRequest.DOB=edt_dob;
        preApprovalRequest.Gender=selectedgender;
        preApprovalRequest.Address_Line_1=address_line_1;
        preApprovalRequest.Address_Line_2=address_line_2;
        preApprovalRequest.Landmark=edt_landmark;
        preApprovalRequest.Pincode=edt_pincode;
        preApprovalRequest.City=edt_city;
        preApprovalRequest.State=edt_state;
        preApprovalRequest.Accommodation=txt_accommondation;
        preApprovalRequest.PAN=edt_pan;
        preApprovalRequest.Aadhaar=edt_adhaar;
        preApprovalRequest.Qualification=txt_qualification;
        preApprovalRequest.residing_with=txt_residing_with;
        preApprovalRequest.number_of_years_at_current_address=txt_number_of_years_at_current_addressr;
        preApprovalRequest.marital_status=selected_marital_status;
        preApprovalRequest.spouse_employment_status=edt_spouse_status;
        preApprovalRequest.number_of_kids=txt_no_of_kids;
        preApprovalRequest.Company_Name=edt_company_name;
        preApprovalRequest.Office_Phone_no=edt_office_phone_no;
        preApprovalRequest.Designation=txt_designation;
        preApprovalRequest.Monthly_Income=edt_monthly_income;
        preApprovalRequest.Number_of_Years_in_Current_Work=edt_no_of_years_in_current_work;
        preApprovalRequest.Official_Email=edt_official_email;
        preApprovalRequest.Office_Add_1=edt_office_address_1;
        preApprovalRequest.Office_Add_2=edt_office_address_2;
        preApprovalRequest.Landmark_Office=edt_landmark_office;
        preApprovalRequest.Office_Pincode=edt_office_pincode;
        preApprovalRequest.Office_City=edt_office_city;
        preApprovalRequest.Office_State=edt_office_state;
        preApprovalRequest.Working_Since=edt_working_since;
        preApprovalRequest.EmploymentType=txt_employment_type;
        preApprovalRequest.Salary_ReceivedTypeId=txt_salary_received_type_id;
        preApprovalRequest.work_sector=txt_work_sector;
        preApprovalRequest.job_function=txt_job_function;
        preApprovalRequest.organization=txt_organization;
        preApprovalRequest.BankName=edt_existing_bank;
        preApprovalRequest.IFSCCode=edt_ifsc_code;
        preApprovalRequest.AccountNumber=edt_ac_no;
        preApprovalRequest.PartnerBankName="Axis Bank";
        preApprovalRequest.PartnerIFSCCode="UTIB0000008";
        preApprovalRequest.PartnerAccNumber="12324234234";
        preApprovalRequest.PartnerRemarks=edt_remark;
        preApprovalRequest.CustomerMobile=edt_mobile;
        preApprovalRequest.Customer_Email=edt_emailid;
        preApprovalRequest.kyc_co=edt_co;
        preApprovalRequest.kyc_street=edt_street;
        preApprovalRequest.kyc_house=edt_house;
        preApprovalRequest.kyc_lm=edt_Lm;
        preApprovalRequest.kyc_vtc=edt_vtc;
        preApprovalRequest.kyc_subdist=edt_subdist;
        preApprovalRequest.kyc_dist=edt_dist;
        preApprovalRequest.kyc_state=edt_kyc_state;
        preApprovalRequest.kyc_pc=edt_pc;
        preApprovalRequest.kyc_po=edt_po;
        preApprovalRequest.kyc_aadhar_no=edt_adharno;
        preApprovalRequest.kyc_name=edt_name;
        preApprovalRequest.kyc_dob=dobEditTextRegi;
        preApprovalRequest.kyc_gender=kyc_gender;
        preApprovalRequest.kyc_address=edt_compressedAddress;

        viewModel.createCustomer(preApprovalRequest, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()

                , response -> {
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            Utils.hideCustomProgressDialog();
                            customerId= (String) response.resource.data.payLoad;
                            SharedPreferences sp = getActivity().getSharedPreferences("customerId",MODE_PRIVATE);
                            SharedPreferences.Editor ed=sp.edit();
                            ed.putString("customerId",customerId );
                            ed.commit();
                            onMessage(response.resource.description);
                            showSucces();
                        }
                        else if (response.resource.responseCode.equals(100)){
                            Utils.hideCustomProgressDialog();
                            onMessage(response.resource.description);
                       }

                        else if (response.resource.responseCode.equals(305)){
                            Utils.hideCustomProgressDialog();
                            onMessage(response.resource.description);
                        }

                        else {
                            onMessage(response.resource.description);
                            Utils.hideCustomProgressDialog();
                        }
                    }
                });
           }


    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        binding.dobEditTextRegi.setText(sdf.format(myCalendar.getTime()));
    }
    private void showSucces() {
        SingleButtonMessageDialog dialog = new
                SingleButtonMessageDialog("Customer Created!!"
                , ("Customer Registered Successfully\n Your Customer Id is: "+customerId), this,
                false);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_e_k_y_c_three;
    }

    @Override
    public void onProceed() {
      //  getActivity().finish();
        Navigation.findNavController(binding.getRoot()).
                navigate(R.id.action_e_KYC_Three_Fragment_to_createCustomerFragment);
    }

    @Override
    public void onCancel(boolean goBack)  {

    }
}
