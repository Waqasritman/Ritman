package angoothape.wallet.personal_loan.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;

import com.basgeekball.awesomevalidation.AwesomeValidation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import angoothape.wallet.R;
import angoothape.wallet.databinding.FragmentApplicantInfoTwoBinding;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.PinCodeListRequest;
import angoothape.wallet.di.JSONdi.restRequest.SimpleRequest;
import angoothape.wallet.di.JSONdi.restResponse.GetCasheAccomodationListResponse;
import angoothape.wallet.dialogs.DialogAccomodation;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.interfaces.CasheAccomodationListInterface;
import angoothape.wallet.personal_loan.viewmodels.PersonalLoanViewModel;
import angoothape.wallet.utils.Utils;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class ApplicantInfo_Two_Fragment extends BaseFragment<FragmentApplicantInfoTwoBinding> implements CasheAccomodationListInterface {

    private AwesomeValidation mAwesomeValidation;
    PersonalLoanViewModel viewModel;
    List<GetCasheAccomodationListResponse> accomodationList;
    Integer Selected_employement_type;
    boolean isSelectedValue = false;
    String mPattern = "^[a-zA-Z0-9-,. ]*$";
    String edt_firstname, edt_lastname, address_line_1, address_line_2, edt_landmark, edt_dob, selectedgender;

    String edt_pincode, edt_city, edt_state, txt_accommondation, edt_pan, edt_adhaar, txt_qualification;

    String txt_residing_with, txt_number_of_years_at_current_addressr, edt_spouse_status, txt_no_of_kids, selected_marital_status;

    String edt_company_name, edt_office_phone_no, txt_designation, edt_monthly_income, edt_no_of_years_in_current_work,
            edt_official_email, edt_office_address_1, edt_loan_amount;


    String edt_office_address_2;
    String edt_landmark_office;
    String edt_office_pincode;
    String edt_office_city;
    String edt_office_state;
    String edt_working_since;
    Integer txt_employment_type;

    final Calendar myCalendar = Calendar.getInstance();
    int day, month, year, age;
    Calendar mcalendar;
    int yearOfToday;
    int yearOfBirthday;

    SimpleDateFormat sdf;
    String myFormat;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {

        mcalendar = Calendar.getInstance();
        day = mcalendar.get(Calendar.DAY_OF_MONTH);
        year = mcalendar.get(Calendar.YEAR);
        month = mcalendar.get(Calendar.MONTH);

        binding.edtWorkingSince.setFocusable(false);

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


        accomodationList = new ArrayList<>();
        viewModel = new ViewModelProvider(this).get(PersonalLoanViewModel.class);
        mAwesomeValidation = new AwesomeValidation(BASIC);
        mAwesomeValidation.addValidation(binding.edtOfficeAddress2, "^[a-zA-Z0-9-,. ]*$", getResources().getString(R.string.addre_line_one));
        mAwesomeValidation.addValidation(binding.edtLandmarkOffice, "^[A-Za-z\\s]+", getResources().getString(R.string.landmark));
        mAwesomeValidation.addValidation(binding.edtOfficePincode, "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$", getResources().getString(R.string.invalide_pine_code));
        //  mAwesomeValidation.addValidation(binding.edtWorkingSince, "[\\d]+", getResources().getString(R.string.no_of_work_year));
        //  mAwesomeValidation.addValidation(binding.edtWorkingSince, "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$", getResources().getString(R.string.workingsince));
        binding.txtEmploymentType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCasheEmploymentType();
            }
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


        binding.edtWorkingSince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.edtWorkingSince.setFocusableInTouchMode(true);
                binding.edtWorkingSince.setCursorVisible(false);
                binding.edtWorkingSince.setShowSoftInputOnFocus(false);
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), R.style.PersonalLoanCalendarTheme, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();


            }
        });


        binding.personalNext.setOnClickListener(v -> {

            edt_office_address_2 = binding.edtOfficeAddress2.getText().toString();
            edt_landmark_office = binding.edtLandmarkOffice.getText().toString();
            edt_office_pincode = binding.edtOfficePincode.getText().toString();
            edt_office_city = binding.edtOfficeCity.getText().toString();
            edt_office_state = binding.edtOfficeState.getText().toString();
            edt_working_since = binding.edtWorkingSince.getText().toString();
            txt_employment_type = Selected_employement_type;

            if (edt_office_address_2.length() < 10 || edt_office_address_2.length() > 512) {
                onMessage("Office Address2 length should be between 10 to 512 characters");
            }
            else if (!binding.edtOfficeAddress2.getText().toString().matches(mPattern)) {
                onMessage(getResources().getString(R.string.addre_line_two));
            }
            else if (binding.edtLandmarkOffice.getText().toString().equals("")){
                onMessage("Please enter your Office Landmark");
            }
            else if (binding.edtOfficePincode.getText().toString().equals("")){
                onMessage("Please enter your Office Pincode Number");
            }
            else if (!isValidPincodeNo(binding.edtOfficePincode.getText().toString())) {
                onMessage(getString(R.string.invalide_pine_code));
            }
            else if (binding.edtOfficeCity.getText().toString().equals("")){
                onMessage("Please enter your Office City Name");
            }
            else if (binding.edtOfficeState.getText().toString().equals("")){
                onMessage("Please enter your Office State Name");
            }
            else if (binding.edtWorkingSince.getText().toString().equals("")) {
                onMessage("Please Select Working Since Date");
            }
            else if (binding.txtEmploymentType.getText().equals("")) {
                onMessage("Please Select Employment Type");
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


                bundle.putString("edt_office_address_2", edt_office_address_2);
                bundle.putString("edt_landmark_office", edt_landmark_office);
                bundle.putString("edt_office_pincode", edt_office_pincode);
                bundle.putString("edt_office_city", edt_office_city);
                bundle.putString("edt_office_state", edt_office_state);
                bundle.putString("edt_working_since", edt_working_since);
                bundle.putString("txt_employment_type", String.valueOf(txt_employment_type));


                Navigation.findNavController(binding.getRoot()).navigate(R.id
                        .action_applicantInfo_Two_Fragment_to_applicantInfo_Three_Fragment, bundle);


            }


        });


        binding.edtOfficePincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                binding.edtOfficeCity.setText("");
                binding.edtOfficeState.setText("");
                if (binding.edtOfficePincode.length() == 6) {
                    getPinCodeList();
                } else {
                    onMessage("Please Enter Pin Code");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void updateLabel() {
        myFormat = "MM/dd/yyyy"; //In which you need put here
        sdf = new SimpleDateFormat(myFormat, Locale.US);
        binding.edtWorkingSince.setText(sdf.format(myCalendar.getTime()));

    }

    public void getPinCodeList() {
        Utils.showCustomProgressDialog(getContext(), false);
        PinCodeListRequest request = new PinCodeListRequest();
        request.Pincode = binding.edtOfficePincode.getText().toString();

        viewModel.getCashePincodeList(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()

                , response -> {
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {

                            binding.edtOfficeCity.setText(response.resource.data.get(0).getCity());
                            binding.edtOfficeState.setText(response.resource.data.get(0).getState());
                            Utils.hideCustomProgressDialog();

                        } else {
                            onMessage(response.resource.description);
                            binding.edtOfficeCity.setText("");
                            binding.edtOfficeState.setText("");
                            Utils.hideCustomProgressDialog();
                        }
                    }
                });
    }


    public void getCasheEmploymentType() {
        Utils.showCustomProgressDialog(getContext(), false);
        SimpleRequest request = new SimpleRequest();
        isSelectedValue = true;
        viewModel.getCasheEmploymentType(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()

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
        return R.layout.fragment_applicant_info__two;
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

        binding.txtEmploymentType.setText(AccomodationSelect.getValue());
        Selected_employement_type = AccomodationSelect.getID();


    }

    public static boolean isValidPincodeNo(CharSequence pincode) {
        String PINCODE_PATTERN = "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$";
        Pattern pattern = Pattern.compile(PINCODE_PATTERN);
        Matcher matcher = pattern.matcher(pincode);
        return matcher.matches();
    }
}