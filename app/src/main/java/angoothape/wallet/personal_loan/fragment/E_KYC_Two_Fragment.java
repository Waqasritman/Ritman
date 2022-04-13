package angoothape.wallet.personal_loan.fragment;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;

import com.basgeekball.awesomevalidation.AwesomeValidation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import angoothape.wallet.R;
import angoothape.wallet.databinding.FragmentEKYCTwoBinding;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.PinCodeListRequest;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.personal_loan.viewmodels.PersonalLoanViewModel;
import angoothape.wallet.utils.Utils;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;


public class E_KYC_Two_Fragment extends BaseFragment<FragmentEKYCTwoBinding> {
    private AwesomeValidation mAwesomeValidation;

    String edt_firstname, edt_lastname, address_line_1, address_line_2, edt_landmark, edt_dob, selectedgender;

    String edt_pincode, edt_city, edt_state, txt_accommondation, edt_pan, edt_adhaar, txt_qualification;

    String txt_residing_with, txt_number_of_years_at_current_addressr, edt_spouse_status, txt_no_of_kids, selected_marital_status;

    String edt_company_name, edt_office_phone_no, txt_designation, edt_monthly_income, edt_no_of_years_in_current_work,
            edt_official_email, edt_office_address_1, edt_loan_amount;

    String edt_office_address_2, edt_landmark_office, edt_office_pincode, edt_office_city, edt_office_state,
            edt_working_since, txt_employment_type;

    String txt_salary_received_type_id, txt_work_sector, txt_job_function, txt_organization;

    String edt_existing_bank, edt_branch_name, edt_ifsc_code, edt_ac_no, edt_re_enter_account_number, edt_remark;

    String edt_mobile, edt_emailid;

    String edt_co, edt_street, edt_house, edt_Lm, edt_vtc, edt_subdist;

    String edt_dist, edt_kyc_state, edt_pc, edt_po, edt_adharno, edt_name;

    PersonalLoanViewModel viewModel;

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

        edt_mobile = getArguments().getString("edt_mobile");
        edt_emailid = getArguments().getString("edt_emailid");


        edt_co = getArguments().getString("edt_co");
        edt_street = getArguments().getString("edt_street");
        edt_house = getArguments().getString("edt_house");
        edt_Lm = getArguments().getString("edt_Lm");
        edt_vtc = getArguments().getString("edt_vtc");
        edt_subdist = getArguments().getString("edt_subdist");


        mAwesomeValidation = new AwesomeValidation(BASIC);
        mAwesomeValidation.addValidation(binding.edtDist, "[\\w\\s-,.]+$", getResources().getString(R.string.enter_distic));
        mAwesomeValidation.addValidation(binding.edtKycState, "^[A-Za-z\\s]+", getResources().getString(R.string.invalid_state));
        mAwesomeValidation.addValidation(binding.edtPc, "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$", getResources().getString(R.string.invalide_pine_code));
        mAwesomeValidation.addValidation(binding.edtPo, "^[A-Za-z\\s]+", getResources().getString(R.string.enter_post));
        mAwesomeValidation.addValidation(binding.edtAdharno, "^[2-9]{1}[0-9]{3}[0-9]{4}[0-9]{4}$", getResources().getString(R.string.invalid_adhaar));
        mAwesomeValidation.addValidation(binding.edtName, "^[A-Za-z\\s]+", getResources().getString(R.string.name_personal));


        binding.personalNext.setOnClickListener(v -> {

            if (binding.edtDist.getText().toString().equals("")) {
                onMessage("Please enter your District Name");
            } else if (binding.edtPc.getText().toString().equals("")) {
                onMessage("Enter your Pincode Number");
            }
            else if (!isValidPincodeNo(binding.edtPc.getText().toString())) {
                onMessage(getString(R.string.invalide_pine_code));
            }
            else if (binding.edtKycState.getText().toString().equals("")){
                onMessage("Enter your State Name");
            }
            else if (binding.edtPo.getText().toString().equals("")){
                onMessage("Enter your Post Office Name");
            }
            else if (binding.edtAdharno.getText().toString().equals("")){
                onMessage("Enter your Aadhaar Card No.");
            }
            else if (!isValidAadharCard(binding.edtAdharno.getText().toString())){
                onMessage(getResources().getString(R.string.invalid_adhaar));
            }
            else if (binding.edtName.getText().toString().equals("")){
                onMessage("Enter your Name");
            }
            else {
                edt_dist = binding.edtDist.getText().toString();
                edt_kyc_state = binding.edtKycState.getText().toString();
                edt_pc = binding.edtPc.getText().toString();
                edt_po = binding.edtPo.getText().toString();
                edt_adharno = binding.edtAdharno.getText().toString();
                edt_name = binding.edtName.getText().toString();

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


                bundle.putString("edt_dist", edt_dist);
                bundle.putString("edt_kyc_state", edt_kyc_state);
                bundle.putString("edt_pc", edt_pc);
                bundle.putString("edt_po", edt_po);
                bundle.putString("edt_adharno", edt_adharno);
                bundle.putString("edt_name", edt_name);

                Navigation.findNavController(binding.getRoot()).navigate(R.id
                        .action_e_KYC_Two_Fragment_to_e_KYC_Three_Fragment, bundle);
            }

        });

        binding.edtPc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // binding.edtCity.setText("");
                binding.edtKycState.setText("");
                if (binding.edtPc.getText().length() == 6) {

                    getPinCodeList();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void getPinCodeList() {
        Utils.showCustomProgressDialog(getContext(), false);
        PinCodeListRequest request = new PinCodeListRequest();
        request.Pincode = binding.edtPc.getText().toString();

        viewModel.getCashePincodeList(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()

                , response -> {
                    if (response.status == Status.ERROR) {
                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {

                            viewModel.city.setValue(response.resource.data.get(0).City);
                            viewModel.state.setValue(response.resource.data.get(0).getState());
                            // binding.edtCity.setText(viewModel.city.getValue());
                            binding.edtKycState.setText(viewModel.state.getValue());
                            Utils.hideCustomProgressDialog();

                        } else {
                            onError(response.resource.description);
                            //binding.edtCity.setText("");
                            binding.edtKycState.setText("");
                            Utils.hideCustomProgressDialog();
                        }
                    }
                });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_e__k_y_c__two;
    }

    public static boolean isValidPincodeNo(CharSequence pincode) {
        String PINCODE_PATTERN = "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$";
        Pattern pattern = Pattern.compile(PINCODE_PATTERN);
        Matcher matcher = pattern.matcher(pincode);
        return matcher.matches();
    }

    public static boolean isValidAadharCard(CharSequence aadharcard) {
        String AADHARCARD_PATTERN = "^[2-9]{1}[0-9]{3}[0-9]{4}[0-9]{4}$";
        Pattern pattern = Pattern.compile(AADHARCARD_PATTERN);
        Matcher matcher = pattern.matcher(aadharcard);
        return matcher.matches();
    }

}