package ritman.wallet.personal_loan.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import ritman.wallet.BankDetailsActivity;
import ritman.wallet.R;
import ritman.wallet.databinding.FragmentPartnerBankBinding;
import ritman.wallet.di.JSONdi.restRequest.BankNetworkListRequest;
import ritman.wallet.di.JSONdi.restResponse.BankListResponse;
import ritman.wallet.di.JSONdi.retrofit.RestClient;
import ritman.wallet.dialogs.BankDetails;
import ritman.wallet.dialogs.DialogBankNetwork;
import ritman.wallet.fragments.BaseFragment;
import ritman.wallet.interfaces.OnBackPressed;
import ritman.wallet.interfaces.OnDecisionMade;
import ritman.wallet.interfaces.OnSelectBank;
import ritman.wallet.utils.IsNetworkConnection;


public class Partner_Bank_Fragment extends BaseFragment<FragmentPartnerBankBinding> implements
        OnSelectBank, OnDecisionMade {
    String branchName,existingBank,ifscCode;

    String edt_firstname,edt_lastname,address_line_1,address_line_2,edt_landmark,edt_dob,selectedgender;

    String edt_pincode,edt_city,edt_state,txt_accommondation,edt_pan,edt_adhaar,txt_qualification;

    String txt_residing_with, txt_number_of_years_at_current_addressr, edt_spouse_status, txt_no_of_kids,selected_marital_status;

    String edt_company_name, edt_office_phone_no, txt_designation, edt_monthly_income,edt_no_of_years_in_current_work,
            edt_official_email,edt_office_address_1,edt_loan_amount;

    String edt_office_address_2, edt_landmark_office, edt_office_pincode, edt_office_city,edt_office_state,
            edt_working_since,txt_employment_type;

    String txt_salary_received_type_id, txt_work_sector, txt_job_function, txt_organization;

    String edt_existing_bank, edt_branch_name, edt_ifsc_code, edt_ac_no,edt_re_enter_account_number,edt_remark;
    Bundle bundle;
    int account_no;
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


        binding.edtExistingBank.setEnabled(false);
        binding.edtBranchName.setEnabled(false);
        binding.edtIfscCode.setEnabled(false);
        binding.edtAcNo.setEnabled(false);
        binding.edtReEnterAccountNumber.setEnabled(false);
        binding.edtRemark.setEnabled(false);

        bundle=new Bundle();

        if (getArguments() != null) {
            branchName=getArguments().getString("branchName");
            existingBank=getArguments().getString("existingBank");
            ifscCode=getArguments().getString("ifscCode");

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

            //binding.existingBank.setText(bankDetails.bankName);
            binding.edtBranchName.setText(branchName);
            binding.edtExistingBank.setText(existingBank);
            binding.edtIfscCode.setText(ifscCode);

            binding.edtAcNo.setEnabled(true);
            binding.edtReEnterAccountNumber.setEnabled(true);
            binding.edtRemark.setEnabled(true);
            // binding.ifscCode.setText(bankDetails.bankCode);
        }


        binding.txtSearchBank.setOnClickListener(v ->{
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

            bundle.putString("txt_salary_received_type_id", txt_salary_received_type_id);
            bundle.putString("txt_work_sector", txt_work_sector);
            bundle.putString("txt_job_function", txt_job_function);
            bundle.putString("txt_organization", txt_organization);

            Navigation.findNavController(binding.getRoot()).navigate(R.id
                    .action_partner_Bank_Fragment_to_dataFragment2,bundle);
                });


        binding.btnSearch.setOnClickListener(v -> {
            int count = 0;
            if (TextUtils.isEmpty(binding.ifscCode1.getText())) {
                count += 1;
            }
            if (TextUtils.isEmpty(binding.existingBank1.getText().toString())) {
                count += 1;
            }

            if (count < 2) {
                if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                    getBankList();
                    binding.bankDetails.setVisibility(View.GONE);
                    binding.linearDisable.setVisibility(View.VISIBLE);
                    binding.linearAccountno.setVisibility(View.VISIBLE);
                    binding.txtSearchBankDetail.setVisibility(View.GONE);
                    binding.txtPartnerBankDetail.setVisibility(View.VISIBLE);

                    binding.edtAcNo.setEnabled(true);
                    binding.edtReEnterAccountNumber.setEnabled(true);
                    binding.edtRemark.setEnabled(true);

                } else {
                    onMessage(getString(R.string.no_internet));
                }
            } else {
                onMessage("Enter bank name or ifsc code for bank details");
            }

        });


        binding.personalNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_existing_bank=existingBank;
                edt_branch_name=branchName;
                edt_ifsc_code=ifscCode;
                edt_ac_no=binding.edtAcNo.getText().toString();
                edt_re_enter_account_number=binding.edtReEnterAccountNumber.getText().toString();

                edt_remark=binding.edtRemark.getText().toString();

                if (binding.edtExistingBank.getText().toString().equals("")) {
                    onMessage("Search Your Bank Name First");
                } else if (binding.edtAcNo.getText().toString().equals("")) {
                    onMessage("Please enter account number");
                } else if (binding.edtReEnterAccountNumber.getText().toString().equals("")) {
                    onMessage("Please re-enter your account number");
                } else if (binding.edtRemark.getText().toString().equals("")) {
                    onMessage("Please enter some remark");

                } else if (!TextUtils.equals(binding.edtAcNo.getText().toString(), binding.edtReEnterAccountNumber.getText().toString())) {
                    onMessage(getString(R.string.same_account_no_error));

                    //  return false;
                } else {

                    edt_existing_bank=existingBank;
                    edt_branch_name=branchName;
                    edt_ifsc_code=ifscCode;
                    edt_ac_no=binding.edtAcNo.getText().toString();
                    edt_re_enter_account_number=binding.edtReEnterAccountNumber.getText().toString();

                    edt_remark=binding.edtRemark.getText().toString();

//                    try {
//                        account_no = Integer.parseInt(edt_ac_no);
//
//                    }
//                    catch (NumberFormatException nfe) {
//                        onMessage("Could not parse" + nfe);
//                    }

                    if (edt_ac_no.length()<5) {
                        onMessage("Account no. lenght should be between 5 to 18");
                    }

                    else {
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


//                    bundle.putString("txt_residing_with", txt_residing_with);
//                    bundle.putString("txt_number_of_years_at_current_addressr", txt_number_of_years_at_current_addressr);
//                    bundle.putString("edt_spouse_status", edt_spouse_status);
//                    bundle.putString("txt_no_of_kids", txt_no_of_kids);
//                    bundle.putString("selected_marital_status", selected_marital_status);

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


                        Navigation.findNavController(binding.getRoot()).navigate(R.id
                                .action_partner_Bank_Fragment_to_contact_Info_Fragment,bundle);
                    }

                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    void getBankList() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.txtSearchBank.setVisibility(View.GONE);
        BankNetworkListRequest request = new BankNetworkListRequest();
        request.BankName = binding.existingBank1.getText().toString();
        request.BranchIFSC = binding.ifscCode1.getText().toString();
        request.BranchName = binding.branchName1.getText().toString();

        Call<BankListResponse> call = RestClient.get().getBankNetworkList(RestClient.makeGSONRequestBody(request)
                , getSessionManager().getMerchantName());
        call.enqueue(new retrofit2.Callback<BankListResponse>() {
            @Override
            public void onResponse(Call<BankListResponse> call, Response<BankListResponse> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.txtSearchBank.setVisibility(View.VISIBLE);

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().responseCode.equals(101)) {
                        showBankList(response.body().data);
                    } else {
                        onMessage(response.body().description);
                    }
                } else {
                    onMessage(getString(R.string.some_thing_wrong));
                }
            }

            @Override
            public void onFailure(Call<BankListResponse> call, Throwable t) {
                onMessage(t.getLocalizedMessage());
                binding.progressBar.setVisibility(View.GONE);
                binding.txtSearchBank.setVisibility(View.VISIBLE);
            }
        });

    }

    void showBankList(List<BankListResponse> responseList) {
        DialogBankNetwork banks = new DialogBankNetwork(responseList, this);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        banks.show(transaction, "");
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_partner__bank;
    }

    @Override
    public void onSelectBank(BankListResponse bankDetails) {
        binding.personalNext.setVisibility(View.VISIBLE);
        binding.txtSearchBank.setVisibility(View.VISIBLE);

        binding.edtExistingBank.setText(bankDetails.bankName);
        binding.edtBranchName.setText(bankDetails.branchName);
        binding.edtIfscCode.setText(bankDetails.bankCode);

    }

    @Override
    public void onSelectBankName(String bankName) {

    }

    @Override
    public void onSelectBranch(BankListResponse branchName) {

    }

    @Override
    public void onProceed() {

    }

    @Override
    public void onCancel() {

    }


}