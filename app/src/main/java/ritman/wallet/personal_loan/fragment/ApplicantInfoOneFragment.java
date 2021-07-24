package ritman.wallet.personal_loan.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ritman.wallet.R;
import ritman.wallet.databinding.ApplicantInformationBinding;
import ritman.wallet.databinding.FragmentApplicantInfoOneBinding;
import ritman.wallet.databinding.FragmentPersonalOneBinding;
import ritman.wallet.di.JSONdi.Status;
import ritman.wallet.di.JSONdi.restRequest.SimpleRequest;
import ritman.wallet.di.JSONdi.restResponse.GetCasheAccomodationListResponse;
import ritman.wallet.dialogs.DialogAccomodation;
import ritman.wallet.fragments.BaseFragment;
import ritman.wallet.interfaces.CasheAccomodationListInterface;
import ritman.wallet.personal_loan.viewmodels.PersonalLoanViewModel;
import ritman.wallet.utils.Utils;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;


public class ApplicantInfoOneFragment extends BaseFragment<FragmentApplicantInfoOneBinding> implements CasheAccomodationListInterface {

    private AwesomeValidation mAwesomeValidation;
    List<GetCasheAccomodationListResponse> accomodationList;
    PersonalLoanViewModel viewModel;
    boolean isSelectedValue = false;
    String mPattern = "^[a-zA-Z0-9-,. ]*$";
    String edt_firstname, edt_lastname, address_line_1, address_line_2, edt_landmark, edt_dob, selectedgender;

    String edt_pincode, edt_city, edt_state, txt_accommondation, edt_pan, edt_adhaar, txt_qualification;

    String txt_residing_with, txt_number_of_years_at_current_addressr, edt_spouse_status, txt_no_of_kids, selected_marital_status;

    String edt_company_name, edt_office_phone_no, txt_designation, edt_monthly_income, edt_no_of_years_in_current_work,
            edt_official_email, edt_office_address_1, edt_loan_amount;
    int monthly_salary;

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

        viewModel = new ViewModelProvider(this).get(PersonalLoanViewModel.class);
        accomodationList = new ArrayList<>();

        mAwesomeValidation = new AwesomeValidation(BASIC);
        mAwesomeValidation.addValidation(binding.edtCompanyName, "^[A-Za-z\\s]+", getResources().getString(R.string.company_name_null));
        //  mAwesomeValidation.addValidation(binding.edtOfficePhoneNo, RegexTemplate.TELEPHONE, getResources().getString(R.string.invalid_phone_no));
        mAwesomeValidation.addValidation(binding.edtOfficePhoneNo, "^(?:0)[0-9\\s.\\/-]{10}$", getResources().getString(R.string.invalid_phone_no));
        mAwesomeValidation.addValidation(binding.edtMonthlyIncome, "[\\d.]+", getResources().getString(R.string.invalid_amount));
        mAwesomeValidation.addValidation(binding.edtNoOfYearsInCurrentWork, "[\\d]+", getResources().getString(R.string.no_of_work_year));
        mAwesomeValidation.addValidation(binding.edtOfficialEmail, Patterns.EMAIL_ADDRESS, getResources().getString(R.string.invalid_email));
        mAwesomeValidation.addValidation(binding.edtOfficeAddress1, "^[a-zA-Z0-9-,. ]*$", getResources().getString(R.string.addre_line_one));
        mAwesomeValidation.addValidation(binding.edtLoanAmount, "[\\d.]+", getResources().getString(R.string.enter_loan_amount));

        binding.txtDesignation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCasheDesignationList();
            }
        });


        binding.personalNext.setOnClickListener(v -> {

            edt_company_name = binding.edtCompanyName.getText().toString();
            edt_office_phone_no = binding.edtOfficePhoneNo.getText().toString();
            txt_designation = binding.txtDesignation.getText().toString();
            edt_monthly_income = binding.edtMonthlyIncome.getText().toString();
            edt_no_of_years_in_current_work = binding.edtNoOfYearsInCurrentWork.getText().toString();
            edt_official_email = binding.edtOfficialEmail.getText().toString();
            edt_office_address_1 = binding.edtOfficeAddress1.getText().toString();
            edt_loan_amount = binding.edtLoanAmount.getText().toString();

            if (binding.edtCompanyName.getText().toString().equals("")) {
                onMessage("Please enter your Company Name");
            }
            else if (binding.edtOfficePhoneNo.getText().toString().equals("")){
                onMessage("Please enter your Office Phone Number");
            }
            else if (!isValidOfficePhoneNo(binding.edtOfficePhoneNo.getText().toString())){
                onMessage(getResources().getString(R.string.invalid_phone_no));
            }
           else if (binding.txtDesignation.getText().toString().equals("")) {
                onMessage("Please select your Designation");
            }
           else if (binding.edtMonthlyIncome.getText().toString().equals("")){
               onMessage("Please enter your Monthly Income");
            }
            else if (binding.edtLoanAmount.getText().toString().equals("")){
                onMessage("Please enter your Loan Amount");
            }
            else if (binding.edtNoOfYearsInCurrentWork.getText().toString().equals("")){
                onMessage("Please enter No. of Years in Current Work");
            }
            else if (binding.edtOfficialEmail.getText().toString().equals("")){
                onMessage("Please enter your Office Email Id");
            }
            else if (!isValidEmailId(binding.edtOfficialEmail.getText().toString())){
                onMessage("Please enter valid Email Id");
            }

            else if (edt_office_address_1.length() < 10 || edt_office_address_1.length() > 512) {
                onMessage("Office Address1 length should be between 10 to 512 characters");
            }
            else if (!binding.edtOfficeAddress1.getText().toString().matches(mPattern)) {
                onMessage(getResources().getString(R.string.addre_line_one));
            }
            else {

                try {
                    monthly_salary = Integer.parseInt(edt_monthly_income);

                } catch (NumberFormatException nfe) {
                    onMessage("Could not parse" + nfe);
                }

                if (monthly_salary < 15000 || monthly_salary > 999999) {
                    onMessage("Monthly income should be between 15000 to 999999 thousand");
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

                    Navigation.findNavController(binding.getRoot()).navigate(R.id
                            .action_applicantInfoOneFragment_to_applicantInfo_Two_Fragment, bundle);
                }

            }


        });


    }

    public void getCasheDesignationList() {
        Utils.showCustomProgressDialog(getContext(), false);
        SimpleRequest request = new SimpleRequest();

        viewModel.getCasheDesignationList(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()

                , response -> {
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            onAccomodation(response.resource.data);
                            Utils.hideCustomProgressDialog();

                        } else {
                            onMessage(response.resource.description);
                            Utils.hideCustomProgressDialog();
                        }
                    }
                });
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_applicant_info_one;
    }

    public static boolean isValidOfficePhoneNo(CharSequence officephoneno) {
        String OFFICE_PHONE_NUMBER_PATTERN = "^(?:0)[0-9\\s.\\/-]{10}$";
        Pattern pattern = Pattern.compile(OFFICE_PHONE_NUMBER_PATTERN);
        Matcher matcher = pattern.matcher(officephoneno);
        return matcher.matches();
    }

    public static boolean isValidEmailId(CharSequence emailid) {
        String OFFICE_EMAILID_PATTERN = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
                + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
                + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+";
        Pattern pattern = Pattern.compile(OFFICE_EMAILID_PATTERN);
        Matcher matcher = pattern.matcher(emailid);
        return matcher.matches();
    }

    public void showAccomodationDialog() {
        DialogAccomodation dialogTransferPurpose =
                new DialogAccomodation(accomodationList, this);
        FragmentTransaction fm = getParentFragmentManager().beginTransaction();
        dialogTransferPurpose.show(fm, "");
    }

    @Override
    public void onAccomodation(List<GetCasheAccomodationListResponse> responseAccomodationList) {
        accomodationList.clear();
        accomodationList.addAll(responseAccomodationList);
        showAccomodationDialog();
    }

    @Override
    public void onSelectAccomodation(GetCasheAccomodationListResponse AccomodationSelect) {

        binding.txtDesignation.setText(AccomodationSelect.getValue());


    }
}