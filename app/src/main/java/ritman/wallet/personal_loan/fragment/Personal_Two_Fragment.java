package ritman.wallet.personal_loan.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;

import ritman.wallet.R;
import ritman.wallet.databinding.FragmentPersonalOneBinding;
import ritman.wallet.databinding.FragmentPersonalTwoBinding;
import ritman.wallet.databinding.PersonalInformationTwoBinding;
import ritman.wallet.di.JSONdi.Status;
import ritman.wallet.di.JSONdi.restRequest.SimpleRequest;
import ritman.wallet.di.JSONdi.restResponse.GetCasheAccomodationListResponse;
import ritman.wallet.dialogs.DialogAccomodation;
import ritman.wallet.fragments.BaseFragment;
import ritman.wallet.interfaces.CasheAccomodationListInterface;
import ritman.wallet.personal_loan.viewmodels.PersonalLoanViewModel;
import ritman.wallet.utils.Utils;

public class Personal_Two_Fragment extends BaseFragment<FragmentPersonalTwoBinding> implements CasheAccomodationListInterface,
        AdapterView.OnItemSelectedListener {
    PersonalLoanViewModel viewModel;
    List<GetCasheAccomodationListResponse> accomodationList;
    String SelectedType;
    String marital_status, selected_marital_status = "Married";

    String edt_firstname, edt_lastname, address_line_1, address_line_2, edt_landmark, edt_dob, selectedgender;

    String edt_pincode, edt_city, edt_state, txt_accommondation, edt_pan, edt_adhaar, txt_qualification;

    String txt_residing_with, txt_number_of_years_at_current_addressr, edt_spouse_status, txt_no_of_kids;

    String[] employment_status = {" Select ", " Employed", " Unemployed"};

    String selected_employment_status = "";


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


        viewModel = new ViewModelProvider(this).get(PersonalLoanViewModel.class);
        accomodationList = new ArrayList<>();


        binding.txtNoOfKids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNoOfKids();
            }
        });

        binding.txtResidingWith.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCasheResidingWith();
            }
        });

        binding.txtNumberOfYearsAtCurrentAddressr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCasheYearsAtCurrentAdd();
            }
        });

        binding.marriedUnmarriedRadiogp.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = group.findViewById(checkedId);
            selected_marital_status = radioButton.getText().toString();
        });


        binding.personalNext.setOnClickListener(v -> {
            if (binding.txtResidingWith.getText().toString().equals("")) {
                onMessage("Please Select Residing With");

            } else if (binding.txtNumberOfYearsAtCurrentAddressr.getText().toString().equals("")) {
                onMessage("Please Select No. of Years at Current Work");
            } else if (binding.spouseEmploymentStatus.getSelectedItemId() == 0) {
                onMessage("Please Select Spouse Employment Status");
            } else if (binding.txtNoOfKids.getText().equals("")) {
                onMessage("Please Select No. of Kids");
            }

            else {
                txt_residing_with = binding.txtResidingWith.getText().toString();
                txt_number_of_years_at_current_addressr = binding.txtNumberOfYearsAtCurrentAddressr.getText().toString();
                //txt_no_of_kids =binding.txtResidingWith.getText().toString();
                // txt_residing_with =binding.txtResidingWith.getText().toString();
                edt_spouse_status = selected_employment_status;

                txt_no_of_kids = binding.txtNoOfKids.getText().toString();
                marital_status = selected_marital_status;

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


                Navigation.findNavController(binding.getRoot()).navigate(R.id
                        .action_personal_Two_Fragment_to_applicantInfoOneFragment, bundle);
            }


        });

        binding.spouseEmploymentStatus.setOnItemSelectedListener(this);
        ArrayAdapter aa_salutation = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, employment_status);

        aa_salutation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spouseEmploymentStatus.setAdapter(aa_salutation);
    }

    public void getCasheResidingWith() {
        Utils.showCustomProgressDialog(getContext(), false);
        SimpleRequest request = new SimpleRequest();
        SelectedType = "residingwith";
        viewModel.getCasheResidingWith(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()

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


    public void getNoOfKids() {
        Utils.showCustomProgressDialog(getContext(), false);
        SimpleRequest request = new SimpleRequest();
        SelectedType = "NoOfKids";
        viewModel.getNoOfKids(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()

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


    public void getCasheYearsAtCurrentAdd() {
        Utils.showCustomProgressDialog(getContext(), false);
        SimpleRequest request = new SimpleRequest();
        SelectedType = "YearsAtCurrentAdd";
        viewModel.getCasheYearsAtCurrentAdd(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()

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
        return R.layout.fragment_personal__two;
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

        if (SelectedType.equals("residingwith")) {
            binding.txtResidingWith.setText(AccomodationSelect.getValue());
        }
        if (SelectedType.equals("NoOfKids")) {
            binding.txtNoOfKids.setText(AccomodationSelect.getValue());
        }

        if (SelectedType.equals("YearsAtCurrentAdd")) {
            binding.txtNumberOfYearsAtCurrentAddressr.setText(AccomodationSelect.getValue());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        if (adapterView.getId() == R.id.spouse_employment_status) {
            int pos = adapterView.getSelectedItemPosition();
            if (pos == 1) {
                selected_employment_status = "Employed";
            } else if (pos == 2) {
                selected_employment_status = "Unemployed";
            } else {

            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}