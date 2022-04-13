package angoothape.wallet.personal_loan.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import angoothape.wallet.R;
import angoothape.wallet.databinding.FragmentDataBinding;
import angoothape.wallet.di.JSONdi.restRequest.BankNetworkListRequest;
import angoothape.wallet.di.JSONdi.restResponse.BankListResponse;
import angoothape.wallet.di.JSONdi.restResponse.BanksList;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.dialogs.DialogBankNetwork;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.interfaces.OnSelectBank;
import angoothape.wallet.utils.IsNetworkConnection;

public class DataFragment extends BaseFragment <FragmentDataBinding> implements OnSelectBank {
    String existingBank,branchName,ifscCode;
    String edt_firstname,edt_lastname,address_line_1,address_line_2,edt_landmark,edt_dob,selectedgender;

    String edt_pincode,edt_city,edt_state,txt_accommondation,edt_pan,edt_adhaar,txt_qualification;

    String txt_residing_with, txt_number_of_years_at_current_addressr, edt_spouse_status, txt_no_of_kids,selected_marital_status;

    String edt_company_name, edt_office_phone_no, txt_designation, edt_monthly_income,edt_no_of_years_in_current_work,
            edt_official_email,edt_office_address_1,edt_loan_amount;

    String edt_office_address_2, edt_landmark_office, edt_office_pincode, edt_office_city,edt_office_state,
            edt_working_since,txt_employment_type;

    String txt_salary_received_type_id, txt_work_sector, txt_job_function, txt_organization;
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

        binding.btnSearch.setOnClickListener(v -> {
            binding.bankDetails.setVisibility(View.VISIBLE);
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


                } else {
                    onMessage(getString(R.string.no_internet));
                }
            } else {
                onMessage("Enter bank name or ifsc code for bank details");
            }

        });

    }

    void getBankList() {
        binding.progressBar.setVisibility(View.VISIBLE);
        //binding.txtSearchBank.setVisibility(View.GONE);
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
                //binding.txtSearchBank.setVisibility(View.VISIBLE);

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().responseCode.equals(101)) {
                        showBankList(response.body().data);
                    } else {
                        onError(response.body().description);
                    }
                } else {
                    onMessage(getString(R.string.some_thing_wrong));
                }
            }

            @Override
            public void onFailure(Call<BankListResponse> call, Throwable t) {
                onMessage(t.getLocalizedMessage());
                binding.progressBar.setVisibility(View.GONE);
              //  binding.txtSearchBank.setVisibility(View.VISIBLE);
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
        return R.layout.fragment_data;
    }

    @Override
    public void onSelectBank(BankListResponse bankDetails) {
        existingBank=bankDetails.getBankName();
        branchName=bankDetails.getBranchName();
        ifscCode=bankDetails.getBankCode();

        Bundle bundle = new Bundle();
        bundle.putString("branchName", branchName);
        bundle.putString("existingBank", existingBank);
        bundle.putString("ifscCode", ifscCode);
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


        bundle.putString("txt_residing_with", txt_residing_with);
        bundle.putString("txt_number_of_years_at_current_addressr", txt_number_of_years_at_current_addressr);
        bundle.putString("edt_spouse_status", edt_spouse_status);
        bundle.putString("txt_no_of_kids", txt_no_of_kids);
        bundle.putString("selected_marital_status", selected_marital_status);

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
                .action_dataFragment2_to_partner_Bank_Fragment,bundle);
    }

    @Override
    public void onSelectBank(BanksList bankDetails) {

    }

    @Override
    public void onSelectBankName(String bankName) {

    }

    @Override
    public void onSelectBranch(BankListResponse branchName) {

    }
}
