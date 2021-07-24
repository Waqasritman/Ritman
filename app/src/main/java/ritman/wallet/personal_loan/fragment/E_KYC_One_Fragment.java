package ritman.wallet.personal_loan.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.basgeekball.awesomevalidation.AwesomeValidation;

import ritman.wallet.R;
import ritman.wallet.databinding.FragmentEKYCOneBinding;
import ritman.wallet.fragments.BaseFragment;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class E_KYC_One_Fragment extends BaseFragment<FragmentEKYCOneBinding> {

    private AwesomeValidation mAwesomeValidation;

    String edt_firstname,edt_lastname,address_line_1,address_line_2,edt_landmark,edt_dob,selectedgender;

    String edt_pincode,edt_city,edt_state,txt_accommondation,edt_pan,edt_adhaar,txt_qualification;

    String txt_residing_with, txt_number_of_years_at_current_addressr, edt_spouse_status, txt_no_of_kids,selected_marital_status;

    String edt_company_name, edt_office_phone_no, txt_designation, edt_monthly_income,edt_no_of_years_in_current_work,
            edt_official_email,edt_office_address_1,edt_loan_amount;

    String edt_office_address_2, edt_landmark_office, edt_office_pincode, edt_office_city,edt_office_state,
            edt_working_since,txt_employment_type;

    String txt_salary_received_type_id, txt_work_sector, txt_job_function, txt_organization;

    String edt_existing_bank, edt_branch_name, edt_ifsc_code, edt_ac_no,edt_re_enter_account_number,edt_remark;

    String edt_mobile, edt_emailid;

    String edt_co, edt_street,edt_house,edt_Lm,edt_vtc,edt_subdist;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {

        edt_firstname=getArguments().getString("edt_firstname");
        edt_lastname=getArguments().getString("edt_lastname");
        address_line_1=getArguments().getString("address_line_1");
        address_line_2=getArguments().getString("address_line_2");
        edt_landmark=getArguments().getString("edt_landmark");
        edt_dob=getArguments().getString("edt_dob");
        selectedgender=getArguments().getString("selectedgender");


        edt_pincode=getArguments().getString("edt_pincode");
        edt_city=getArguments().getString("edt_city");
        edt_state=getArguments().getString("edt_state");
        txt_accommondation=getArguments().getString("txt_accommondation");
        edt_pan=getArguments().getString("edt_pan");
        edt_adhaar=getArguments().getString("edt_adhaar");
        txt_qualification=getArguments().getString("txt_qualification");


        txt_residing_with=getArguments().getString("txt_residing_with");
        txt_number_of_years_at_current_addressr=getArguments().getString("txt_number_of_years_at_current_addressr");
        edt_spouse_status=getArguments().getString("edt_spouse_status");
        txt_no_of_kids=getArguments().getString("txt_no_of_kids");
        selected_marital_status=getArguments().getString("selected_marital_status");


        edt_company_name=getArguments().getString("edt_company_name");
        edt_office_phone_no=getArguments().getString("edt_office_phone_no");
        txt_designation=getArguments().getString("txt_designation");
        edt_monthly_income=getArguments().getString("edt_monthly_income");
        edt_no_of_years_in_current_work=getArguments().getString("edt_no_of_years_in_current_work");
        edt_official_email=getArguments().getString("edt_official_email");
        edt_office_address_1=getArguments().getString("edt_office_address_1");
        edt_loan_amount=getArguments().getString("edt_loan_amount");


        edt_office_address_2=getArguments().getString("edt_office_address_2");
        edt_landmark_office=getArguments().getString("edt_landmark_office");
        edt_office_pincode=getArguments().getString("edt_office_pincode");
        edt_office_city=getArguments().getString("edt_office_city");
        edt_office_state=getArguments().getString("edt_office_state");
        edt_working_since=getArguments().getString("edt_working_since");
        txt_employment_type=getArguments().getString("txt_employment_type");

        txt_salary_received_type_id=getArguments().getString("txt_salary_received_type_id");
        txt_work_sector=getArguments().getString("txt_work_sector");
        txt_job_function=getArguments().getString("txt_job_function");
        txt_organization=getArguments().getString("txt_organization");

        edt_existing_bank=getArguments().getString("edt_existing_bank");
        edt_branch_name=getArguments().getString("edt_branch_name");
        edt_ifsc_code=getArguments().getString("edt_ifsc_code");
        edt_ac_no=getArguments().getString("edt_ac_no");
        edt_re_enter_account_number=getArguments().getString("edt_re_enter_account_number");
        edt_remark=getArguments().getString("edt_remark");

        edt_mobile=getArguments().getString("edt_mobile");
        edt_emailid=getArguments().getString("edt_emailid");


        mAwesomeValidation=new AwesomeValidation(BASIC);
        mAwesomeValidation.addValidation(binding.edtCo,"^[A-Za-z\\s]+", getResources().getString(R.string.care_of));
        mAwesomeValidation.addValidation(binding.edtStreet, "^[A-Za-z\\s]+", getResources().getString(R.string.enter_street));
        mAwesomeValidation.addValidation(binding.edtHouse,"[\\w\\s-,.]+$", getResources().getString(R.string.enter_house));
        mAwesomeValidation.addValidation(binding.edtLm,"[\\w\\s-,.]+$", getResources().getString(R.string.landmark));
        mAwesomeValidation.addValidation(binding.edtVtc, "[\\w\\s-,.]+$", getResources().getString(R.string.enter_vill_cit_to));
        mAwesomeValidation.addValidation(binding.edtSubdist,"[\\w\\s-,.]+$", getResources().getString(R.string.enter_subdisc));

        binding.personalNext.setOnClickListener(v -> {

            if(binding.edtCo.getText().toString().equals("")){
                onMessage("Please enter your Care Of (CO) Name");
            }
           else if(binding.edtStreet.getText().toString().equals("")){
                onMessage("Please enter your Street Name");
            }
            else if(binding.edtHouse.getText().toString().equals("")){
                onMessage("Please enter your House Number/Nmae");
            }
            else if(binding.edtLm.getText().toString().equals("")){
                onMessage("Please enter your Land Mark");
            }
            else if(binding.edtVtc.getText().toString().equals("")){
                onMessage("Please enter Vtc");
            }
            else if(binding.edtSubdist.getText().toString().equals("")){
                onMessage("Please enter your Subdist");
            }

            else{

                edt_co=binding.edtCo.getText().toString();
                edt_street=binding.edtStreet.getText().toString();
                edt_house=binding.edtHouse.getText().toString();
                edt_Lm=binding.edtLm.getText().toString();
                edt_vtc=binding.edtVtc.getText().toString();
                edt_subdist=binding.edtSubdist.getText().toString();

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

                bundle.putString("edt_co", edt_co);
                bundle.putString("edt_street", edt_street);
                bundle.putString("edt_house", edt_house);
                bundle.putString("edt_Lm", edt_Lm);
                bundle.putString("edt_vtc", edt_vtc);
                bundle.putString("edt_subdist", edt_subdist);

                Navigation.findNavController(binding.getRoot()).navigate(R.id
                        .action_e_KYC_One_Fragment_to_e_KYC_Two_Fragment,bundle);

            }


        });



    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_e__k_y_c_one;
    }


}