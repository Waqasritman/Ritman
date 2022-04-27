package angoothape.wallet.billpayment.updated_fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import angoothape.wallet.R;
import angoothape.wallet.billpayment.BillPaymentMainActivity;
import angoothape.wallet.billpayment.helpers.BillerInputTypes;
import angoothape.wallet.billpayment.viewmodel.BillPaymentViewModel;
import angoothape.wallet.databinding.FragmentUtilityPaymentAccountBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.BillDetailRequest;
import angoothape.wallet.di.JSONdi.restRequest.GetWRBillerFieldsRequestN;
import angoothape.wallet.di.JSONdi.restResponse.BillDetailResponse;
import angoothape.wallet.di.JSONdi.restResponse.BillDetailsErrorResponse;
import angoothape.wallet.di.JSONdi.restResponse.BillDetailsMainResponse;
import angoothape.wallet.di.JSONdi.restResponse.GetWRBillerCategoryResponseC;
import angoothape.wallet.di.JSONdi.restResponse.GetWRBillerFieldsResponseN;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.interfaces.OnWRBillerFields;
import angoothape.wallet.utils.Utils;
import okhttp3.internal.Util;

import static android.content.Context.WIFI_SERVICE;

public class UtilityPaymentAccountNoFragment extends BaseFragment<FragmentUtilityPaymentAccountBinding>
        implements OnWRBillerFields {

    List<GetWRBillerFieldsResponseN> wrBillerList;
    BillDetailRequest request;
    BillPaymentViewModel viewModel;
    String ipAddress = "";
    String MobileNumber = "";
    String billerId, IMEINumber, validationid, billercategory;
    String paymentamount_validation, currency, payment_amount, biller_logo, partial_pay, pay_after_duedate;
    final Calendar myCalendar = Calendar.getInstance();
    int indexNo = -1;

    @Override
    protected void injectView() {

    }

    public boolean isValidateOne() {
        request.Field1 = wrBillerList.get(0).labelName;
        if (TextUtils.isEmpty(binding.accountNo.getText().toString())) {
            onMessage(getString(R.string.enter_txt).concat(" ").concat(getString(R.string.correct))
                    .concat(" ").concat(wrBillerList.get(0).labelName));
            return false;
        } else if (binding.accountNo.getText().length() == 9 && request.Field1.equals("Consumer Number*")) {
            binding.accountNo.getText().toString().concat(getString(R.string.comma));
        } else if (!binding.accountNo.getText().toString().matches(wrBillerList.get(0).authenticator_regex)) {
            onMessage(getString(R.string.enter_txt).concat(" ")
                    .concat(getString(R.string.correct)).concat(" ").concat(wrBillerList.get(0).labelName));
            return false;

        }

        request.Field1 = wrBillerList.get(0).labelName;
        return true;

    }

    public boolean isValidateTwe() {
        if (TextUtils.isEmpty(binding.accountNo.getText().toString())) {
            onMessage(getString(R.string.enter_txt).concat(" ").concat(getString(R.string.correct)).concat(" ").concat(wrBillerList.get(0).labelName));
            return false;
        } else if (!binding.accountNo.getText().toString().matches(wrBillerList.get(0).authenticator_regex)) {
            onMessage(getString(R.string.enter_txt).concat(" ")
                    .concat(getString(R.string.correct)).concat(" ").concat(wrBillerList.get(0).labelName));

            return false;

        } else if (TextUtils.isEmpty(binding.accountPolicyNo.getText().toString())) {
            onMessage(getString(R.string.enter_txt).concat(" ").concat(getString(R.string.correct)).concat(" ").concat(wrBillerList.get(1).labelName));
            return false;
        } else if (!binding.accountPolicyNo.getText().toString().matches(wrBillerList.get(1).authenticator_regex)) {
            onMessage(getString(R.string.enter_txt).concat(" ")
                    .concat(getString(R.string.correct)).concat(wrBillerList.get(1).labelName));
            return false;

        }
        request.Field1 = wrBillerList.get(0).labelName;
        request.Field2 = wrBillerList.get(1).labelName;
        return true;

    }

    public boolean isValidateThree() {

        if (TextUtils.isEmpty(binding.accountNo.getText().toString())) {
            onMessage(getString(R.string.enter_txt).concat(" ").concat(getString(R.string.correct)).concat(" ").concat(wrBillerList.get(0).labelName));
            return false;
        } else if (!binding.accountNo.getText().toString().matches(wrBillerList.get(0).authenticator_regex)) {
            onMessage(getString(R.string.enter_txt).concat(" ")
                    .concat(getString(R.string.correct)).concat(" ").concat(wrBillerList.get(0).labelName));
            return false;

        } else if (TextUtils.isEmpty(binding.accountPolicyNo.getText().

                toString())) {
            onMessage(getString(R.string.enter_txt).concat(" ").concat(getString(R.string.correct)).concat(" ").concat(wrBillerList.get(1).labelName));
            return false;
        } else if (!binding.accountPolicyNo.getText().toString().matches(wrBillerList.get(1).authenticator_regex)) {
            onMessage(getString(R.string.enter_txt).concat(" ")
                    .concat(getString(R.string.correct)).concat(" ").concat(wrBillerList.get(1).labelName));
            return false;

        } else if (TextUtils.isEmpty(binding.reEnterAccountPolicy.getText().

                toString())) {
            onMessage(getString(R.string.enter_txt).concat(" ").concat(getString(R.string.correct)).concat(" ").concat(wrBillerList.get(2).labelName));
            return false;
        } else if (!binding.reEnterAccountPolicy.getText().toString().matches(wrBillerList.get(2).authenticator_regex)) {
            onMessage(getString(R.string.enter_txt).concat(" ")
                    .concat(getString(R.string.correct)).concat(" ").concat(wrBillerList.get(2).labelName));
            return false;
        }


        request.Field1 = wrBillerList.get(0).labelName;
        request.Field2 = wrBillerList.get(1).labelName;
        request.Field3 = wrBillerList.get(2).labelName;

        return true;
    }

    public boolean isValidateFour() {

        if (TextUtils.isEmpty(binding.accountNo.getText().toString())) {
            onMessage(getString(R.string.enter_txt).concat(" ").concat(getString(R.string.correct)).concat(" ").concat(wrBillerList.get(0).labelName));
            return false;
        } else if (!binding.accountNo.getText().toString().matches(wrBillerList.get(0).authenticator_regex)) {
            onMessage(getString(R.string.enter_txt).concat(" ")
                    .concat(getString(R.string.correct)).concat(" ").concat(wrBillerList.get(0).labelName));
            return false;

        } else if (TextUtils.isEmpty(binding.accountPolicyNo.getText().

                toString())) {
            onMessage(getString(R.string.enter_txt).concat(" ").concat(getString(R.string.correct)).concat(" ").concat(wrBillerList.get(1).labelName));
            return false;
        } else if (!binding.accountPolicyNo.getText().toString().matches(wrBillerList.get(1).authenticator_regex)) {
            onMessage(getString(R.string.enter_txt).concat(" ")
                    .concat(getString(R.string.correct)).concat(" ").concat(wrBillerList.get(1).labelName));
            return false;

        } else if (TextUtils.isEmpty(binding.reEnterAccountPolicy.getText().

                toString())) {
            onMessage(getString(R.string.enter_txt).concat(" ").concat(getString(R.string.correct)).concat(" ").concat(wrBillerList.get(2).labelName));
            return false;
        } else if (!binding.reEnterAccountPolicy.getText().toString().matches(wrBillerList.get(2).authenticator_regex)) {
            onMessage(getString(R.string.enter_txt).concat(" ")
                    .concat(getString(R.string.correct)).concat(" ").concat(wrBillerList.get(2).labelName));
            return false;

        } else if (TextUtils.isEmpty(binding.sizeFourTitleInput.getText().

                toString())) {
            onMessage(getString(R.string.enter_txt).concat(" ").concat(getString(R.string.correct)).concat(" ").concat(wrBillerList.get(3).labelName));
            return false;
        } else if (!binding.sizeFourTitleInput.getText().toString().matches(wrBillerList.get(3).authenticator_regex)) {
            onMessage(getString(R.string.enter_txt).concat(" ")
                    .concat(getString(R.string.correct)).concat(" ").concat(wrBillerList.get(3).labelName));
            return false;

        }

        request.Field1 = wrBillerList.get(0).labelName;
        request.Field2 = wrBillerList.get(1).labelName;
        request.Field3 = wrBillerList.get(2).labelName;
        request.Field4 = wrBillerList.get(3).labelName;
        return true;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        wrBillerList = new ArrayList<>();
        request = new BillDetailRequest();
        assert getArguments() != null;
        billerId = getArguments().getString("billerId");
        paymentamount_validation = getArguments().getString("paymentamount_validation");
        partial_pay = getArguments().getString("partial_pay");
        pay_after_duedate = getArguments().getString("pay_after_duedate");
        currency = getArguments().getString("currency");
        biller_logo = getArguments().getString("biller_logo");

        Glide.with(this)
                .load(biller_logo)
                .placeholder(R.drawable.bbps_horizontal_1)
                .into(binding.imgBillernameLogo);

        viewModel = ((BillPaymentMainActivity) getBaseActivity()).viewModel;

        if (paymentamount_validation.equals("Y")) {
            binding.linearAmount.setVisibility(View.VISIBLE);
        }

        getWRBillerFields();
        binding.nextLayout.setOnClickListener(v -> {
            if (paymentamount_validation.equals("Y")) {
                if (wrBillerList.size() == 1) {
                    if (isValidateOne()) {
                        fillData1();
                    }
                } else if (wrBillerList.size() == 2) {
                    if (isValidateTwe()) {
                        fillData1();
                    }
                } else if (wrBillerList.size() == 3) {
                    if (isValidateThree()) {
                        fillData1();
                    }
                } else if (wrBillerList.size() == 4) {
                    if (isValidateFour()) {
                        fillData1();
                    }
                }
            } else {
                if (wrBillerList.size() == 1) {
                    if (isValidateOne()) {
                        fillData();
                    }
                } else if (wrBillerList.size() == 2) {
                    if (isValidateTwe()) {
                        fillData();
                    }
                } else if (wrBillerList.size() == 3) {
                    if (isValidateThree()) {
                        fillData();
                    }
                } else if (wrBillerList.size() == 4) {
                    if (isValidateFour()) {
                        fillData();
                    }
                }
            }
        });

        int permissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getBaseActivity(), new String[]{Manifest.permission.READ_PHONE_STATE},
                    1);
        } else {
            IMEINumber = getDeviceId(getContext());
            getIpAddressOfDevice();
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    IMEINumber = getDeviceId(getContext());
                    getIpAddressOfDevice();
                } else {
                    Log.e("TAG", "Permission Needs: ");
                }
                break;

            default:
                break;
        }
    }

    private void updateLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        binding.accountPolicyNo.setText(sdf.format(myCalendar.getTime()));
    }

    public void fillData() {
        if (binding.accountNo.getText().length() == 9 && request.Field1.equals("Consumer Number*")) {
            ((BillPaymentMainActivity) getBaseActivity())
                    .payBillRequest.mobileAccount = binding.accountNo.getText().toString().
                    concat(getString(R.string.comma));
        } else {
            ((BillPaymentMainActivity) getBaseActivity())
                    .payBillRequest.mobileAccount = binding.accountNo.getText().toString();
            ((BillPaymentMainActivity) getBaseActivity())
                    .payBillRequest.mobileAccount2 = binding.accountPolicyNo.getText().toString();
            ((BillPaymentMainActivity) getBaseActivity())
                    .payBillRequest.mobileAccount3 = binding.reEnterAccountPolicy.getText().toString();
        }

        if (indexNo == -1) {
            if (binding.mobileno.getText().toString().equals("")) {
                onMessage("Please enter your mobile number");
            } else if (binding.mobileno.getText().toString().length() != 10) {
                onMessage("Please enter valid 10-digits mobile number");
            } else {
                MobileNumber = binding.mobileno.getText().toString();
                getBillDetails();

            }
        } else {
            getBillDetails();
        }

    }

    public void fillData1() {
        if (binding.accountNo.getText().length() == 9 && request.Field1.equals("Consumer Number*")) {
            ((BillPaymentMainActivity) getBaseActivity())
                    .payBillRequest.mobileAccount = binding.accountNo.getText().toString().
                    concat(getString(R.string.comma));
        } else {
            ((BillPaymentMainActivity) getBaseActivity())
                    .payBillRequest.mobileAccount = binding.accountNo.getText().toString();
            ((BillPaymentMainActivity) getBaseActivity())
                    .payBillRequest.mobileAccount2 = binding.accountPolicyNo.getText().toString();
            ((BillPaymentMainActivity) getBaseActivity())
                    .payBillRequest.mobileAccount3 = binding.reEnterAccountPolicy.getText().toString();
        }

        if (binding.edtPaymentAmount.getText().toString().equals("")) {
            onMessage("Please enter your Payment amount");
        } else if (binding.mobileno.getText().toString().equals("")) {
            onMessage("Please enter your mobile number");
        } else if (binding.mobileno.getText().toString().length() != 10) {
            onMessage("Please enter valid 10-digits mobile number");
        } else {
            MobileNumber = binding.mobileno.getText().toString();
            payment_amount = binding.edtPaymentAmount.getText().toString().concat(getString(R.string.concat_zero));
            double total_payment_amount = 0.0;
            try {
                total_payment_amount = Double.parseDouble(payment_amount);
            } catch (NumberFormatException e) {

            }

            if (total_payment_amount < 100) {
                onMessage("Payment amount should not be less than Rs.100");
            } else {
                getBillDetails();
            }

        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_utility_payment_account;
    }

    @Override
    public void onWRBillerField(List<GetWRBillerFieldsResponseN> response) {
        wrBillerList = new ArrayList<>();
        wrBillerList.addAll(response);

        if (response.size() == 1) {
            onSizeOne(response);
        } else if (response.size() == 2) {
            request.Field2 = response.get(1).fieldName;
            if (request.Field2.equals("Date Of Birth(YYYY-MM-DD)")) {
                binding.accountPolicyNo.setFocusable(false);
                DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel();
                };

                binding.accountPolicyNo.setOnClickListener(v -> {
                    binding.accountPolicyNo.setFocusableInTouchMode(true);
                    binding.accountPolicyNo.setCursorVisible(false);
                    binding.accountPolicyNo.setShowSoftInputOnFocus(false);
                    // TODO Auto-generated method stub
                    new DatePickerDialog(getActivity(), R.style.PersonalLoanCalendarTheme, date,
                            myCalendar.get(Calendar.YEAR),
                            myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                });
            }
            onSizeTwo(response);
        } else if (response.size() == 3) {
            onSizeThree(response);
        } else if (response.size() == 4) {
            onSizeFour(response);
        }

    }


    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }

    void onSizeOne(List<GetWRBillerFieldsResponseN> response) {
        if (Pattern.compile(Pattern.quote(response.get(0).labelName),
                Pattern.CASE_INSENSITIVE).matcher("Mobile Number").find()
                || Pattern.compile(Pattern.quote(response.get(0).labelName),
                Pattern.CASE_INSENSITIVE).matcher("Registered Contact Number").find()) {
            indexNo = 0;
            binding.txtMobileno.setVisibility(View.GONE);
            binding.mobileno.setVisibility(View.GONE);
        }

        request.Field1Name = response.get(0).labelName;
        binding.accountNoField.setHint(response.get(0).labelName + "*");
        if (response.get(0).maxLength != null) {
            binding.accountNoField.setMaxEms(response.get(0).maxLength);
        }
        // binding.accountNo.setFilters(new InputFilter[] { new InputFilter.LengthFilter(wrBillerList.get(0).maxLength) });

        binding.accountNo.setHint(response.get(0).description);
        if (response.get(0).type.equalsIgnoreCase(BillerInputTypes.NUMERIC)) {
            binding.accountNo.setInputType(InputType.TYPE_CLASS_NUMBER);
            //binding.accountNo.setFilters(new InputFilter[] { new InputFilter.LengthFilter(Integer.parseInt(String.valueOf(wrBillerList.get(0).maxLength))) });

        }
        binding.sizeFourTitleInput.setVisibility(View.GONE);
        binding.sizeFourTitle.setVisibility(View.GONE);
        binding.accountPolicyNoField.setVisibility(View.GONE);
        binding.accountPolicyNo.setVisibility(View.GONE);

        binding.descriptionField.setVisibility(View.GONE);
        binding.reEnterAccountPolicy.setVisibility(View.GONE);

    }

    void onSizeTwo(List<GetWRBillerFieldsResponseN> response) {
        binding.accountNoField.setHint(response.get(0).labelName + "*");
        if (response.get(0).maxLength != null) {
            binding.accountNoField.setMaxEms(response.get(0).maxLength);
        }
        binding.accountNo.setHint(response.get(0).description);
        if (response.get(0).type.equalsIgnoreCase(BillerInputTypes.NUMERIC)) {
            binding.accountNo.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        request.Field1Name = response.get(0).labelName;
        request.Field2Name = response.get(1).labelName;
        binding.accountPolicyNoField.setHint(response.get(1).labelName + "*");
        if (response.get(1).maxLength != null) {
            binding.accountPolicyNoField.setMaxEms(response.get(1).maxLength);
        }

        binding.accountPolicyNo.setHint(response.get(1).description);
        // binding.accountPolicyNo.setFilters(new InputFilter[] { new InputFilter.LengthFilter(Integer.parseInt(String.valueOf(wrBillerList.get(1).maxLength))) });

        if (response.get(1).type.equalsIgnoreCase(BillerInputTypes.NUMERIC)) {
            binding.accountPolicyNo.setInputType(InputType.TYPE_CLASS_NUMBER);
            //binding.accountPolicyNo.setFilters(new InputFilter[] { new InputFilter.LengthFilter(Integer.parseInt(String.valueOf(wrBillerList.get(1).maxLength))) });

        }
        if (Pattern.compile(Pattern.quote(response.get(0).labelName),
                Pattern.CASE_INSENSITIVE).matcher("Mobile Number").find() || Pattern.compile(Pattern.quote(response.get(0).labelName),
                Pattern.CASE_INSENSITIVE).matcher("Registered Contact Number").find()) {
            indexNo = 0;
        } else if (Pattern.compile(Pattern.quote(response.get(1).labelName),
                Pattern.CASE_INSENSITIVE).matcher("Mobile Number").find() || Pattern.compile(Pattern.quote(response.get(1).labelName),
                Pattern.CASE_INSENSITIVE).matcher("Registered Contact Number").find()) {
            indexNo = 1;
        }
        if (indexNo > -1) {
            binding.txtMobileno.setVisibility(View.GONE);
            binding.mobileno.setVisibility(View.GONE);
        }
        binding.sizeFourTitleInput.setVisibility(View.GONE);
        binding.sizeFourTitle.setVisibility(View.GONE);
        binding.descriptionField.setVisibility(View.GONE);
        binding.reEnterAccountPolicy.setVisibility(View.GONE);
    }

    void onSizeThree(List<GetWRBillerFieldsResponseN> response) {
        binding.accountNoField.setHint(response.get(0).labelName + "*");
        binding.accountNo.setHint(response.get(0).description);


        if (response.get(0).type.equalsIgnoreCase(BillerInputTypes.NUMERIC)) {
            binding.accountNo.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        request.Field1Name = response.get(0).labelName;
        request.Field2Name = response.get(1).labelName;
        request.Field3Name = response.get(2).labelName;
        binding.accountPolicyNoField.setHint(response.get(1).labelName + "*");
        binding.accountPolicyNo.setHint(response.get(1).description);
        // binding.accountPolicyNo.setFilters(new InputFilter[] { new InputFilter.LengthFilter(Integer.parseInt(String.valueOf(wrBillerList.get(1).maxLength))) });
        if (response.get(1).type.equalsIgnoreCase(BillerInputTypes.NUMERIC)) {
            binding.accountPolicyNo.setInputType(InputType.TYPE_CLASS_NUMBER);
            // binding.accountPolicyNo.setFilters(new InputFilter[] { new InputFilter.LengthFilter(Integer.parseInt(String.valueOf(wrBillerList.get(1).maxLength))) });

        }
        binding.descriptionField.setHint(response.get(2).labelName + "*");
        binding.reEnterAccountPolicy.setHint(response.get(2).description);

        if (response.get(2).type.equalsIgnoreCase(BillerInputTypes.NUMERIC)) {
            binding.reEnterAccountPolicy.setInputType(InputType.TYPE_CLASS_NUMBER);
            // binding.reEnterAccountPolicy.setFilters(new InputFilter[] { new InputFilter.LengthFilter(Integer.parseInt(String.valueOf(wrBillerList.get(2).maxLength))) });

        }
        if (response.get(0).maxLength != null) {
            binding.accountNoField.setMaxEms(response.get(0).maxLength);
        }
        if (response.get(1).maxLength != null) {
            binding.accountPolicyNoField.setMaxEms(response.get(1).maxLength);
        }
        if (response.get(2).maxLength != null) {
            binding.reEnterAccountPolicy.setMaxEms(response.get(2).maxLength);
        }
        if (Pattern.compile(Pattern.quote(response.get(0).labelName),
                Pattern.CASE_INSENSITIVE).matcher("Mobile Number").find() || Pattern.compile(Pattern.quote(response.get(0).labelName),
                Pattern.CASE_INSENSITIVE).matcher("Registered Contact Number").find()) {
            indexNo = 0;
        } else if (Pattern.compile(Pattern.quote(response.get(1).labelName),
                Pattern.CASE_INSENSITIVE).matcher("Mobile Number").find() || Pattern.compile(Pattern.quote(response.get(1).labelName),
                Pattern.CASE_INSENSITIVE).matcher("Registered Contact Number").find()) {
            indexNo = 1;
        } else if (Pattern.compile(Pattern.quote(response.get(2).labelName),
                Pattern.CASE_INSENSITIVE).matcher("Mobile Number").find() || Pattern.compile(Pattern.quote(response.get(2).labelName),
                Pattern.CASE_INSENSITIVE).matcher("Registered Contact Number").find()) {
            indexNo = 2;
        }
        if (indexNo > -1) {
            binding.txtMobileno.setVisibility(View.GONE);
            binding.mobileno.setVisibility(View.GONE);
        }

        binding.sizeFourTitleInput.setVisibility(View.GONE);
        binding.sizeFourTitle.setVisibility(View.GONE);

    }

    void onSizeFour(List<GetWRBillerFieldsResponseN> response) {
        binding.accountNoField.setHint(response.get(0).labelName + "*");
        binding.accountNo.setHint(response.get(0).description);

        if (response.get(0).type.equalsIgnoreCase(BillerInputTypes.NUMERIC)) {
            binding.accountNo.setInputType(InputType.TYPE_CLASS_NUMBER);
            //binding.accountNo.setFilters(new InputFilter[] { new InputFilter.LengthFilter(Integer.parseInt(String.valueOf(wrBillerList.get(0).maxLength))) });

        }
        request.Field1Name = response.get(0).labelName;
        request.Field2Name = response.get(1).labelName;
        request.Field3Name = response.get(2).labelName;
        request.Field4Name = response.get(3).labelName;
        binding.accountPolicyNoField.setHint(response.get(1).labelName + "*");
        binding.accountPolicyNo.setHint(response.get(1).description);
        // binding.accountPolicyNo.setFilters(new InputFilter[] { new InputFilter.LengthFilter(Integer.parseInt(String.valueOf(wrBillerList.get(1).maxLength))) });
        if (response.get(1).type.equalsIgnoreCase(BillerInputTypes.NUMERIC)) {
            binding.accountPolicyNo.setInputType(InputType.TYPE_CLASS_NUMBER);
            // binding.accountPolicyNo.setFilters(new InputFilter[] { new InputFilter.LengthFilter(Integer.parseInt(String.valueOf(wrBillerList.get(1).maxLength))) });

        }

        binding.descriptionField.setHint(response.get(2).labelName + "*");
        binding.reEnterAccountPolicy.setHint(response.get(2).description);
        // binding.reEnterAccountPolicy.setFilters(new InputFilter[] { new InputFilter.LengthFilter(Integer.parseInt(String.valueOf(wrBillerList.get(2).maxLength))) });

        if (response.get(2).type.equalsIgnoreCase(BillerInputTypes.NUMERIC)) {
            binding.reEnterAccountPolicy.setInputType(InputType.TYPE_CLASS_NUMBER);
            // binding.reEnterAccountPolicy.setFilters(new InputFilter[] { new InputFilter.LengthFilter(Integer.parseInt(String.valueOf(wrBillerList.get(2).maxLength))) });

        }
        if (response.get(0).maxLength != null) {
            binding.accountNoField.setMaxEms(response.get(0).maxLength);
        }
        if (response.get(1).maxLength != null) {
            binding.accountPolicyNoField.setMaxEms(response.get(1).maxLength);
        }
        if (response.get(2).maxLength != null) {
            binding.reEnterAccountPolicy.setMaxEms(response.get(2).maxLength);
        }
        if (response.get(3).maxLength != null) {
            binding.sizeFourTitleInput.setMaxEms(response.get(3).maxLength);
        }

        binding.sizeFourTitle.setHint(response.get(3).labelName + "*");
        binding.sizeFourTitleInput.setHint(response.get(3).description);

        if (response.get(3).type.equalsIgnoreCase(BillerInputTypes.NUMERIC)) {
            binding.sizeFourTitleInput.setInputType(InputType.TYPE_CLASS_NUMBER);
            // binding.reEnterAccountPolicy.setFilters(new InputFilter[] { new InputFilter.LengthFilter(Integer.parseInt(String.valueOf(wrBillerList.get(2).maxLength))) });
        }
        if (Pattern.compile(Pattern.quote(response.get(0).labelName),
                Pattern.CASE_INSENSITIVE).matcher("Mobile Number").find() || Pattern.compile(Pattern.quote(response.get(0).labelName),
                Pattern.CASE_INSENSITIVE).matcher("Registered Contact Number").find()) {
            indexNo = 0;
        } else if (Pattern.compile(Pattern.quote(response.get(1).labelName),
                Pattern.CASE_INSENSITIVE).matcher("Mobile Number").find() || Pattern.compile(Pattern.quote(response.get(1).labelName),
                Pattern.CASE_INSENSITIVE).matcher("Registered Contact Number").find()) {
            indexNo = 1;
        } else if (Pattern.compile(Pattern.quote(response.get(2).labelName),
                Pattern.CASE_INSENSITIVE).matcher("Mobile Number").find() || Pattern.compile(Pattern.quote(response.get(2).labelName),
                Pattern.CASE_INSENSITIVE).matcher("Registered Contact Number").find()) {
            indexNo = 2;
        }
//        else if (response.get(3).labelName.contains("Mobile Number")) {
//            indexNo = 3;
//        }
        else if (Pattern.compile(Pattern.quote(response.get(3).labelName),
                Pattern.CASE_INSENSITIVE).matcher("Mobile Number").find() || Pattern.compile(Pattern.quote(response.get(3).labelName),
                Pattern.CASE_INSENSITIVE).matcher("Registered Contact Number").find()) {
            indexNo = 3;
        }
        if (indexNo > -1) {
            binding.txtMobileno.setVisibility(View.GONE);
            binding.mobileno.setVisibility(View.GONE);
        }
    }

    private void getWRBillerFields() {
        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim();
        GetWRBillerFieldsRequestN requestc = new GetWRBillerFieldsRequestN();
        requestc.BillerID = billerId;
        requestc.countryCode = "IN";
        String body = RestClient.makeGSONString(requestc);

        AERequest request = new AERequest();
        request.body = AESHelper.encrypt(body.trim(), gKey.trim());
        Log.e("getWRBillerFields: ", body);
        Utils.showCustomProgressDialog(getContext(), false);
        viewModel.GetWRBillerFields(request, KeyHelper.getKey(getSessionManager().getMerchantName()).trim(),
                KeyHelper.getSKey(KeyHelper
                        .getKey(getSessionManager().getMerchantName())))
                .observe(getViewLifecycleOwner(), response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            binding.colopsField.setVisibility(View.VISIBLE);
                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , gKey);
                            Log.e("getWRBillerFields: ", bodyy);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<GetWRBillerFieldsResponseN>>() {
                                }.getType();
                                List<GetWRBillerFieldsResponseN> data = gson.fromJson(bodyy, type);

                                onWRBillerField(data);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            binding.mainView.setVisibility(View.VISIBLE);
                        } else if (response.resource.responseCode.equals(206)) {
                            onMessage(response.resource.description);
                            Navigation.findNavController(binding.getRoot()).navigateUp();
                        } else if (response.resource.responseCode.equals(305)) {
                            onMessage(response.resource.description + "\nTry again later");
                            Navigation.findNavController(binding.getRoot()).navigateUp();
                        } else {
                            Utils.hideCustomProgressDialog();
                            if (response.resource.data != null) {
                                String bodyy = AESHelper.decrypt(response.resource.data.body
                                        , gKey);
                                binding.colopsField.setVisibility(View.VISIBLE);

                                Log.e("getWRBillerFields: ", bodyy);
                                try {
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<List<GetWRBillerFieldsResponseN>>() {
                                    }.getType();
                                    List<GetWRBillerFieldsResponseN> data = gson.fromJson(bodyy, type);

                                    onWRBillerField(data);
                                    binding.mainView.setVisibility(View.VISIBLE);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    if (!body.isEmpty()) {
                                        onError(bodyy);
                                    } else {
                                        onError(response.resource.description);
                                    }
                                }

                            } else {
                                onError(response.resource.description);
                            }
                        }
                    }
                });
    }


    public static String getDeviceId(Context context) {
        String deviceId;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deviceId = Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephony.getDeviceId() != null) {
                deviceId = mTelephony.getDeviceId();
            } else {
                deviceId = Settings.Secure.getString(
                        context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
        }

        return deviceId;
    }


    public void getIpAddressOfDevice() {
        WifiManager wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(WIFI_SERVICE);
        ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
    }

    void getBillDetails() {

        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim();

        request.countryCode = "IN";
        request.BillerID = billerId;

        if (indexNo > -1) {
            if (indexNo == 0) {
                request.MobileNumber = binding.accountNo.getText().toString();
            } else if (indexNo == 1) {
                request.MobileNumber = binding.accountPolicyNo.getText().toString();
            } else if (indexNo == 2) {
                request.MobileNumber = binding.reEnterAccountPolicy.getText().toString();
            } else if (indexNo == 3) {
                request.MobileNumber = binding.sizeFourTitleInput.getText().toString();
            }
        } else {
            request.MobileNumber = MobileNumber;
        }
        if (binding.accountNo.getText().length() == 9 && request.Field1.equals("Consumer Number*")) {
            request.Field1 = binding.accountNo.getText().toString().concat(getString(R.string.comma));
        } else {
            request.Field1 = binding.accountNo.getText().toString();
        }
        request.Field4 = binding.sizeFourTitleInput.getText().toString();
        request.Field2 = binding.accountPolicyNo.getText().toString();
        request.Field3 = binding.reEnterAccountPolicy.getText().toString();

//        request.Field1Name = request.Field1;
//        request.Field2Name = request.Field2;
//        request.Field3Name = request.Field3;
//        request.Field4Name = request.Field4;

//        if (request.Field1  != null && request.Field2 != null && request.Field3 != null) {
//            request.Field1Name = request.Field1 ;
//            request.Field2Name = request.Field2;
//            request.Field3Name = request.Field3;
//            request.Field4Name = "";
//        } else if (request.Field1  != null && request.Field2 != null) {
//            request.Field1Name = request.Field1 ;
//            request.Field2Name = request.Field2;
//            request.Field3Name = "";
//            request.Field4Name = "";
//        } else if (request.Field1  != null && request.Field2 != null && request.Field3 != null && request.Field4 != null) {
//            request.Field1Name = request.Field1 ;
//            request.Field2Name = request.Field2;
//            request.Field3Name = request.Field3;
//            request.Field4Name = request.Field4;
//        } else {
//            request.Field1Name = request.Field1 ;
//            request.Field2Name = "";
//            request.Field3Name = "";
//            request.Field4Name = "";
//        }


        if (ipAddress.isEmpty()) {
            IMEINumber = getDeviceId(getContext());
            getIpAddressOfDevice();
        }
        request.ip = ipAddress;
        request.imei = IMEINumber;//"5468748458458454";


        if (paymentamount_validation.equals("Y")) {
            request.payment_amount = payment_amount;
            request.currency = currency;
        }

        String body = RestClient.makeGSONString(request);
        Log.e("getBillDetails: ", body);
        AERequest aeRequest = new AERequest();
        aeRequest.body = AESHelper.encrypt(body.trim(), gKey.trim());
        Utils.showCustomProgressDialog(getContext(), false);
        viewModel.getBillDetails(aeRequest, KeyHelper.getKey(getSessionManager().getMerchantName()).trim(),
                KeyHelper.getSKey(KeyHelper
                        .getKey(getSessionManager().getMerchantName())))
                .observe(getViewLifecycleOwner(), response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        onError(getString(response.messageResourceId));

                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {

                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , gKey);
                            Log.e("getBillDetails: ", bodyy);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<BillDetailsMainResponse>() {
                                }.getType();
                                BillDetailsMainResponse data = gson.fromJson(bodyy, type);
                                viewModel.customerId = data.customerID;

                                validationid = data.billDetailResponse.validationid;
                                billercategory = data.billDetailResponse.biller_category;

                                Bundle bundle = new Bundle();
                                bundle.putParcelable("bill_details", data);
                                if (binding.accountNo.getText().length() == 9 && request.Field1.equals("Consumer Number*")) {
//                                    bundle.putString("Field1", binding.accountNo.getText().toString().
//                                            concat(getString(R.string.comma)));
//
//                                    bundle.putString("Field2", binding.accountPolicyNo.getText().toString());
//                                    bundle.putString("Field3", binding.reEnterAccountPolicy.getText().toString());
                                    bundle.putString("BillerID", billerId);
                                    bundle.putString("MobileNumber", MobileNumber);
                                    bundle.putString("request.Field1 ", request.Field1);
                                    bundle.putString("request.Field2", request.Field2);
                                    bundle.putString("request.Field3", request.Field3);
                                    bundle.putString("biller_logo", biller_logo);

                                    bundle.putString("paymentamount_validation", paymentamount_validation);
                                    bundle.putString("partial_pay", partial_pay);

                                } else {
//                                    bundle.putString("Field1", binding.accountNo.getText().toString());
//                                    bundle.putString("Field2", binding.accountPolicyNo.getText().toString());
//                                    bundle.putString("Field3", binding.reEnterAccountPolicy.getText().toString());
//                                    bundle.putString("BillerID", billerId);
                                    bundle.putString("MobileNumber", MobileNumber);
                                    bundle.putString("request.Field1 ", request.Field1);
                                    bundle.putString("request.Field2", request.Field2);
                                    bundle.putString("request.Field3", request.Field3);
                                    bundle.putString("paymentamount_validation", paymentamount_validation);
                                    bundle.putString("partial_pay", partial_pay);
                                    bundle.putString("biller_logo", biller_logo);

                                }
                                bundle.putString("pay_after_duedate", pay_after_duedate);
                                if (paymentamount_validation.equals("Y")) {
                                    bundle.putString("payment_amount", payment_amount);
                                    bundle.putString("currency", currency);
                                }
                                Navigation.findNavController(binding.getRoot()).navigate(R.id
                                        .action_utilityPaymentAccountNoFragment_to_utilityBillerDetailsFragment, bundle);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (response.resource.responseCode.equals(100)) {
                            Utils.hideCustomProgressDialog();
                            if (response.resource.data != null) {
                                String bodyy = AESHelper.decrypt(response.resource.data.body
                                        , gKey);
                                Log.e("getBillDetails: ", bodyy);

                                if (!body.isEmpty()) {
                                    try {
                                        Gson gson = new Gson();
                                        Type type = new TypeToken<BillDetailsErrorResponse>() {
                                        }.getType();
                                        BillDetailsErrorResponse data = gson.fromJson(bodyy, type);

                                        onError(data.message.replace("ERR_1", ""));
                                    } catch (Exception e) {
                                        onError(response.resource.description);
                                    }
                                } else {
                                    onError(response.resource.description);
                                }


                            } else {
                                onError(response.resource.description);
                            }

                        } else if (response.resource.responseCode.equals(305)) {
                            onError(response.resource.description + "\nTry again later");
                        } else {

                            onError(response.resource.description);

                        }
                    }
                });

    }

    @Override
    public void showProgress() {
        super.showProgress();
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        super.hideProgress();
        binding.progressBar.setVisibility(View.GONE);
    }


}