package angoothape.wallet.personal_loan.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.basgeekball.awesomevalidation.AwesomeValidation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import angoothape.wallet.R;
import angoothape.wallet.databinding.FragmentPersonalOneBinding;
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


public class Personal_One_Fragment extends BaseFragment<FragmentPersonalOneBinding> implements CasheAccomodationListInterface {
    private AwesomeValidation mAwesomeValidation;
    PersonalLoanViewModel viewModel;
    List<GetCasheAccomodationListResponse> accomodationList;

    String value, value1;
    boolean isSelectedValue = false;

    String edt_firstname, edt_lastname, address_line_1, address_line_2, edt_landmark, edt_dob, selectedgender;

    String edt_pincode, edt_city, edt_state, txt_accommondation, edt_pan, edt_adhaar, txt_qualification;

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


        viewModel = new ViewModelProvider(this).get(PersonalLoanViewModel.class);
        accomodationList = new ArrayList<>();
        mAwesomeValidation = new AwesomeValidation(BASIC);
        mAwesomeValidation.addValidation(binding.edtPincode, "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$", getResources().getString(R.string.invalide_pine_code));
        mAwesomeValidation.addValidation(binding.edtCity, "^[A-Za-z\\s]+", getResources().getString(R.string.invalid_city));
        mAwesomeValidation.addValidation(binding.edtState, "^[A-Za-z\\s]+", getResources().getString(R.string.invalid_state));
        // mAwesomeValidation.addValidation(binding.accommondation,"^[A-Za-z\\s]+", getResources().getString(R.string.invalid_accommondation));
        mAwesomeValidation.addValidation(binding.edtPan, "[A-Z]{5}[0-9]{4}[A-Z]{1}", getResources().getString(R.string.invalid_pan));
        mAwesomeValidation.addValidation(binding.edtAdhaar, "^[2-9]{1}[0-9]{3}[0-9]{4}[0-9]{4}$", getResources().getString(R.string.invalid_adhaar));
        binding.txtQualification.setFocusable(false);

        binding.personalNext.setOnClickListener(v -> {

            edt_pincode = binding.edtPincode.getText().toString();
            edt_city = binding.edtCity.getText().toString();
            edt_state = binding.edtState.getText().toString();
            txt_accommondation = binding.txtAccommondation.getText().toString();
            edt_pan = binding.edtPan.getText().toString();
            edt_adhaar = binding.edtAdhaar.getText().toString();
            txt_qualification = binding.txtQualification.getText().toString();

            if (binding.edtPincode.getText().toString().equals("")) {
                onMessage("Please enter your Pincode No.");
            } else if (!isValidPincodeNo(binding.edtPincode.getText().toString())) {
                onMessage(getString(R.string.invalide_pine_code));
            } else if (binding.edtCity.getText().toString().equals("")) {
                onMessage("Please enter your City Name");
            } else if (binding.edtState.getText().toString().equals("")) {
                onMessage("Please enter your State Name");
            } else if (binding.txtAccommondation.getText().toString().equals("")) {
                onMessage("Please Select Accommodation");
            } else if (binding.edtPan.getText().toString().equals("")) {
                onMessage("Please enter your Pancard Number");
            } else if (!isValidPancard(binding.edtPan.getText().toString())) {
                onMessage(getResources().getString(R.string.invalid_pan));
            } else if (binding.edtAdhaar.getText().toString().equals("")) {
                onMessage("Please enter your Aadharcard Number");
            }
            else if (!isValidAadharCard(binding.edtAdhaar.getText().toString())){
                onMessage(getResources().getString(R.string.invalid_adhaar));
            }
            else if (binding.txtQualification.getText().toString().equals("")) {
                onMessage("Please select your Qualification");
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

                Navigation.findNavController(binding.getRoot()).navigate(R.id
                        .action_personal_One_Fragment_to_personal_Two_Fragment, bundle);
            }
        });


        binding.txtAccommondation.setOnClickListener(v -> getCasheAccomodationList());

        binding.edtPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                binding.edtCity.setText("");
                binding.edtState.setText("");
                if (binding.edtPincode.getText().length() == 6) {

                    getPinCodeList();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        binding.txtAccommondation.setOnClickListener(v -> {
            getCasheAccomodationList();

        });

        binding.txtQualification.setOnClickListener(v -> {
            getCasheQualificationList();
            //  binding.txtQualification.setText(value);
        });

    }

    public void getPinCodeList() {
        Utils.showCustomProgressDialog(getContext(), false);
        PinCodeListRequest request = new PinCodeListRequest();
        request.Pincode = binding.edtPincode.getText().toString();

        viewModel.getCashePincodeList(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()

                , response -> {
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            Utils.hideCustomProgressDialog();
                            viewModel.city.setValue(response.resource.data.get(0).City);
                            viewModel.state.setValue(response.resource.data.get(0).getState());
                            binding.edtCity.setText(viewModel.city.getValue());
                            binding.edtState.setText(viewModel.state.getValue());

                        } else {
                            onMessage(response.resource.description);
                            binding.edtCity.setText("");
                            binding.edtState.setText("");
                            Utils.hideCustomProgressDialog();
                        }
                    }
                });
    }


    public void getCasheAccomodationList() {
        Utils.showCustomProgressDialog(getContext(), false);
        SimpleRequest request = new SimpleRequest();
        isSelectedValue = true;
        viewModel.getCasheAccomodationList(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()

                , response -> {
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            Utils.hideCustomProgressDialog();
                            onAccomodation(response.resource.data);

                        } else {
                            onMessage(response.resource.description);
                            Utils.hideCustomProgressDialog();
                        }
                    }
                });
    }

    public void getCasheQualificationList() {
        Utils.showCustomProgressDialog(getContext(), false);
        SimpleRequest request = new SimpleRequest();
        isSelectedValue = false;
        viewModel.getCasheQualificationList(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()

                , response -> {
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            Utils.hideCustomProgressDialog();
                            onAccomodation(response.resource.data);

                        } else {
                            onMessage(response.resource.description);
                            Utils.hideCustomProgressDialog();
                        }
                    }
                });
    }


    @Override
    public void onAccomodation(List<GetCasheAccomodationListResponse> responseAccomodationList) {
        accomodationList.clear();
        accomodationList.addAll(responseAccomodationList);
        showAccomodationDialog();
    }

    @Override
    public void onSelectAccomodation(GetCasheAccomodationListResponse AccomodationSelect) {

        //binding.accommondation.setText(AccomodationSelect.getValue());
        if (isSelectedValue) {
            isSelectedValue = false;
            //value = AccomodationSelect.getValue();
            binding.txtAccommondation.setText(AccomodationSelect.getValue());
        } else {
            // value1 = AccomodationSelect.getValue();
            binding.txtQualification.setText(AccomodationSelect.getValue());
        }

    }

    //TransferPurpose
    public void showAccomodationDialog() {
        DialogAccomodation dialogTransferPurpose =
                new DialogAccomodation(accomodationList, this);
        FragmentTransaction fm = getParentFragmentManager().beginTransaction();
        dialogTransferPurpose.show(fm, "");
    }


    @Override
    public int getLayoutId() {

        return R.layout.fragment_personal__one;

    }

    public static boolean isValidPincodeNo(CharSequence pincode) {
        String PINCODE_PATTERN = "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$";
        Pattern pattern = Pattern.compile(PINCODE_PATTERN);
        Matcher matcher = pattern.matcher(pincode);
        return matcher.matches();
    }

    public static boolean isValidPancard(CharSequence pancard) {
        String PANCARD_PATTERN = "[A-Z]{5}[0-9]{4}[A-Z]{1}";
        Pattern pattern = Pattern.compile(PANCARD_PATTERN);
        Matcher matcher = pattern.matcher(pancard);
        return matcher.matches();
    }

    public static boolean isValidAadharCard(CharSequence aadharcard) {
        String AADHARCARD_PATTERN = "^[2-9]{1}[0-9]{3}[0-9]{4}[0-9]{4}$";
        Pattern pattern = Pattern.compile(AADHARCARD_PATTERN);
        Matcher matcher = pattern.matcher(aadharcard);
        return matcher.matches();
    }
}