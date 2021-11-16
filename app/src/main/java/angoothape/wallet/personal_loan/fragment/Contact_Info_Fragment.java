package angoothape.wallet.personal_loan.fragment;

import android.os.Bundle;

import androidx.navigation.Navigation;

import android.util.Patterns;

import com.basgeekball.awesomevalidation.AwesomeValidation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import angoothape.wallet.R;
import angoothape.wallet.databinding.FragmentContactInfoBinding;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.personal_loan.viewmodels.PersonalLoanViewModel;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;


public class Contact_Info_Fragment extends BaseFragment<FragmentContactInfoBinding> {
    private AwesomeValidation mAwesomeValidation;
    PersonalLoanViewModel viewModel;

    String edt_firstname, edt_lastname, address_line_1, address_line_2, edt_landmark, edt_dob, selectedgender;

    String edt_pincode, edt_city, edt_state, txt_accommondation, edt_pan, edt_adhaar, txt_qualification;

    String txt_residing_with, txt_number_of_years_at_current_addressr, edt_spouse_status, txt_no_of_kids, selected_marital_status;

    String edt_company_name, edt_office_phone_no, txt_designation, edt_monthly_income, edt_no_of_years_in_current_work,
            edt_official_email, edt_office_address_1,edt_loan_amount;

    String edt_office_address_2, edt_landmark_office, edt_office_pincode, edt_office_city, edt_office_state,
            edt_working_since, txt_employment_type;

    String txt_salary_received_type_id, txt_work_sector, txt_job_function, txt_organization;

    String edt_existing_bank, edt_branch_name, edt_ifsc_code, edt_ac_no, edt_re_enter_account_number, edt_remark;

    String edt_mobile, edt_emailid;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {

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
        txt_employment_type = getArguments().getString("txt_employment_type");

        txt_salary_received_type_id = getArguments().getString("txt_salary_received_type_id");
        txt_work_sector = getArguments().getString("txt_work_sector");
        txt_job_function = getArguments().getString("txt_job_function");
        txt_organization = getArguments().getString("txt_organization");

        edt_existing_bank = getArguments().getString("edt_existing_bank");
        edt_branch_name = getArguments().getString("edt_branch_name");
        edt_ifsc_code = getArguments().getString("edt_ifsc_code");
        edt_ac_no = getArguments().getString("edt_ac_no");
        edt_re_enter_account_number = getArguments().getString("edt_re_enter_account_number");
        edt_remark = getArguments().getString("edt_remark");

        mAwesomeValidation = new AwesomeValidation(BASIC);
        mAwesomeValidation.addValidation(binding.edtMobile, "[6-9][0-9]{9}", getResources().getString(R.string.invalid_mob_no));
        mAwesomeValidation.addValidation(binding.edtEmailid, Patterns.EMAIL_ADDRESS, getResources().getString(R.string.invalid_email));


        binding.personalNext.setOnClickListener(v -> {
            if (binding.edtMobile.getText().toString().equals("")){
                onMessage("Please enter your Mobile Number");
            }
            else if (binding.edtMobile.getText().toString().length()!=10){
                onMessage("Please enter valid 10-digits Mobile Number");
            }
            else if (binding.edtEmailid.getText().toString().equals("")){
                onMessage("Please enter your Email Id");
            }
            else if (!isValidEmailId(binding.edtEmailid.getText().toString())){
                onMessage("Please enter valid Email Id");
            }
            else{

                edt_mobile = binding.edtMobile.getText().toString();
                edt_emailid = binding.edtEmailid.getText().toString();

                Bundle bundle = new Bundle();

                bundle.putString("edt_firstname", edt_firstname);
                bundle.putString("edt_lastname", edt_lastname);
                bundle.putString("address_line_1", address_line_1);
                bundle.putString("address_line_2", address_line_2);
                bundle.putString("edt_landmark", edt_landmark);
                bundle.putString("edt_dob", edt_dob);
                bundle.putString("selectedgender", selectedgender);


                bundle.putString("edt_pincode", edt_pincode);
                bundle.putString("edt_city", edt_city);
                bundle.putString("edt_state", edt_state);
                bundle.putString("txt_accommondation", txt_accommondation);
                bundle.putString("edt_pan", edt_pan);
                bundle.putString("edt_adhaar", edt_adhaar);
                bundle.putString("txt_qualification", txt_qualification);

                bundle.putString("txt_residing_with", txt_residing_with);
                bundle.putString("txt_number_of_years_at_current_addressr", txt_number_of_years_at_current_addressr);
                bundle.putString("edt_spouse_status", edt_spouse_status);
                bundle.putString("txt_no_of_kids", txt_no_of_kids);
                bundle.putString("selected_marital_status", selected_marital_status);


//                bundle.putString("txt_residing_with", txt_residing_with);
//                bundle.putString("txt_number_of_years_at_current_addressr", txt_number_of_years_at_current_addressr);
//                bundle.putString("edt_spouse_status", edt_spouse_status);
//                bundle.putString("txt_no_of_kids", txt_no_of_kids);
//                bundle.putString("selected_marital_status", selected_marital_status);

                bundle.putString("edt_company_name", edt_company_name);
                bundle.putString("edt_office_phone_no", edt_office_phone_no);
                bundle.putString("txt_designation", txt_designation);
                bundle.putString("edt_monthly_income", edt_monthly_income);
                bundle.putString("edt_no_of_years_in_current_work", edt_no_of_years_in_current_work);
                bundle.putString("edt_official_email", edt_official_email);
                bundle.putString("edt_office_address_1", edt_office_address_1);
                bundle.putString("edt_loan_amount", edt_loan_amount);


                bundle.putString("edt_office_address_2", edt_office_address_2);
                bundle.putString("edt_landmark_office", edt_landmark_office);
                bundle.putString("edt_office_pincode", edt_office_pincode);
                bundle.putString("edt_office_city", edt_office_city);
                bundle.putString("edt_office_state", edt_office_state);
                bundle.putString("edt_working_since", edt_working_since);
                bundle.putString("txt_employment_type", txt_employment_type);

                bundle.putString("txt_salary_received_type_id", txt_salary_received_type_id);
                bundle.putString("txt_work_sector", txt_work_sector);
                bundle.putString("txt_job_function", txt_job_function);
                bundle.putString("txt_organization", txt_organization);


                bundle.putString("edt_existing_bank", edt_existing_bank);
                bundle.putString("edt_branch_name", edt_branch_name);
                bundle.putString("edt_ifsc_code", edt_ifsc_code);
                bundle.putString("edt_ac_no", edt_ac_no);
                bundle.putString("edt_re_enter_account_number", edt_re_enter_account_number);
                bundle.putString("edt_remark", edt_remark);


                bundle.putString("edt_mobile", edt_mobile);
                bundle.putString("edt_emailid", edt_emailid);


                Navigation.findNavController(binding.getRoot()).navigate(R.id
                        .action_contact_Info_Fragment_to_e_KYC_One_Fragment,bundle);
            }


        });


    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_contact__info;
    }

    public static boolean isValidEmailId(CharSequence emailid) {
        String EMAILID_PATTERN = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
                + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
                + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+";
        Pattern pattern = Pattern.compile(EMAILID_PATTERN);
        Matcher matcher = pattern.matcher(emailid);
        return matcher.matches();
    }

}