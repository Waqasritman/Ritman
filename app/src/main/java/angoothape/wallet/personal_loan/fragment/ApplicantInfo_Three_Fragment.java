package angoothape.wallet.personal_loan.fragment;

import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import angoothape.wallet.R;
import angoothape.wallet.databinding.FragmentApplicantInfoThreeBinding;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.SimpleRequest;
import angoothape.wallet.di.JSONdi.restResponse.GetCasheAccomodationListResponse;
import angoothape.wallet.dialogs.DialogAccomodation;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.interfaces.CasheAccomodationListInterface;
import angoothape.wallet.personal_loan.viewmodels.PersonalLoanViewModel;
import angoothape.wallet.utils.Utils;


public class ApplicantInfo_Three_Fragment extends BaseFragment<FragmentApplicantInfoThreeBinding> implements CasheAccomodationListInterface {
    PersonalLoanViewModel viewModel;
    List<GetCasheAccomodationListResponse> accomodationList;
    String SelectedType;
    Integer Salary_ReceivedTypeId;

    String edt_firstname, edt_lastname, address_line_1, address_line_2, edt_landmark, edt_dob, selectedgender;

    String edt_pincode, edt_city, edt_state, txt_accommondation, edt_pan, edt_adhaar, txt_qualification;

    String txt_residing_with, txt_number_of_years_at_current_addressr, edt_spouse_status, txt_no_of_kids, selected_marital_status;

    String edt_company_name, edt_office_phone_no, txt_designation, edt_monthly_income, edt_no_of_years_in_current_work,
            edt_official_email, edt_office_address_1,edt_loan_amount;

    String edt_office_address_2, edt_landmark_office, edt_office_pincode, edt_office_city, edt_office_state,
            edt_working_since, txt_employment_type;

    Integer txt_salary_received_type_id;
    String txt_work_sector;
    String txt_job_function;
    String txt_organization;

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


        viewModel = new ViewModelProvider(this).get(PersonalLoanViewModel.class);
        accomodationList = new ArrayList<>();

        binding.txtWorkSector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCasheWorkSectors();
            }
        });

        binding.txtSalaryReceivedTypeId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCasheSalaryTypes();
            }
        });

        binding.txtJobFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCasheJobFunctions();
            }
        });

        binding.txtOrganization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCasheOrganizations();
            }
        });


        binding.personalNext.setOnClickListener(v -> {
            if (binding.txtSalaryReceivedTypeId.getText().toString().equals("")) {
                onMessage("Please select your Salary Received Type");
            }
            else if (binding.txtWorkSector.getText().toString().equals("")){
                onMessage("Please select your Work Sector");
            }

            else if (binding.txtJobFunction.getText().toString().equals("")){
                onMessage("Please select your Job Function");
            }

            else if (binding.txtOrganization.getText().toString().equals("")){
                onMessage("Please select your Organization Name");
            }

            else {
                //  txt_salary_received_type_id=binding.txtSalaryReceivedTypeId.getText().toString();
                txt_salary_received_type_id = Salary_ReceivedTypeId;
                txt_work_sector = binding.txtWorkSector.getText().toString();
                txt_job_function = binding.txtJobFunction.getText().toString();
                txt_organization = binding.txtOrganization.getText().toString();

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


//            bundle.putString("txt_residing_with", txt_residing_with);
//            bundle.putString("txt_number_of_years_at_current_addressr", txt_number_of_years_at_current_addressr);
//            bundle.putString("edt_spouse_status", edt_spouse_status);
//            bundle.putString("txt_no_of_kids", txt_no_of_kids);
//            bundle.putString("selected_marital_status", selected_marital_status);

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

                bundle.putString("txt_salary_received_type_id", String.valueOf(txt_salary_received_type_id));
                bundle.putString("txt_work_sector", txt_work_sector);
                bundle.putString("txt_job_function", txt_job_function);
                bundle.putString("txt_organization", txt_organization);


                Navigation.findNavController(binding.getRoot()).navigate(R.id
                        .action_applicantInfo_Three_Fragment_to_partner_Bank_Fragment, bundle);
            }

        });


    }

    public void getCasheSalaryTypes() {
        Utils.showCustomProgressDialog(getContext(), false);
        SimpleRequest request = new SimpleRequest();
        SelectedType = "CasheSalary";
        viewModel.getCasheSalaryTypes(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()

                , response -> {
                    if (response.status == Status.ERROR) {
                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            onAccomodation(response.resource.data);
                            Utils.hideCustomProgressDialog();

                        } else {
                            onError(response.resource.description);
                            Utils.hideCustomProgressDialog();
                        }
                    }
                });
    }

    public void getCasheWorkSectors() {
        Utils.showCustomProgressDialog(getContext(), false);
        SimpleRequest request = new SimpleRequest();
        SelectedType = "CasheWorkSectors";
        //isSelectedValue1 = true;
        viewModel.getCasheWorkSectors(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()

                , response -> {
                    if (response.status == Status.ERROR) {
                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            onAccomodation(response.resource.data);
                            Utils.hideCustomProgressDialog();

                        } else {
                            onError(response.resource.description);
                            Utils.hideCustomProgressDialog();
                        }
                    }
                });
    }

    public void getCasheJobFunctions() {
        Utils.showCustomProgressDialog(getContext(), false);
        SimpleRequest request = new SimpleRequest();
        SelectedType = "CasheJobFunctions";
        /// isSelectedValue = false;
        viewModel.getCasheJobFunctions(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()

                , response -> {
                    if (response.status == Status.ERROR) {
                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            onAccomodation(response.resource.data);
                            Utils.hideCustomProgressDialog();

                        } else {
                            onError(response.resource.description);
                            Utils.hideCustomProgressDialog();
                        }
                    }
                });
    }

    public void getCasheOrganizations() {
        Utils.showCustomProgressDialog(getContext(), false);
        SimpleRequest request = new SimpleRequest();
        SelectedType = "CasheOrganizations";
        viewModel.getCasheOrganizations(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()

                , response -> {
                    if (response.status == Status.ERROR) {
                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            onAccomodation(response.resource.data);
                        } else {
                            onError(response.resource.description);
                        }
                        Utils.hideCustomProgressDialog();
                    }
                });
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_applicant_info__three;
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
//        if (isSelectedValue) {
//            isSelectedValue1 = false;
//            binding.salaryReceivedTypeId.setText(AccomodationSelect.getValue());
//
//        } else if (isSelectedValue1) {
//            isSelectedValue = false;
//            binding.workSector.setText(AccomodationSelect.getValue());
//
//        } else {
//            binding.jobFunction.setText(AccomodationSelect.getValue());
//
//
//        }

        if (SelectedType.equals("CasheSalary")) {
            binding.txtSalaryReceivedTypeId.setText(AccomodationSelect.getValue());
            Salary_ReceivedTypeId = AccomodationSelect.getID();
        }
        if (SelectedType.equals("CasheWorkSectors")) {
            binding.txtWorkSector.setText(AccomodationSelect.getValue());

        }

        if (SelectedType.equals("CasheJobFunctions")) {
            binding.txtJobFunction.setText(AccomodationSelect.getValue());
        }

        if (SelectedType.equals("CasheOrganizations")) {
            binding.txtOrganization.setText(AccomodationSelect.getValue());
        }
    }
}