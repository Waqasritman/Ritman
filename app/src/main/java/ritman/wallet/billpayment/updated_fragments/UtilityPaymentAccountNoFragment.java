package ritman.wallet.billpayment.updated_fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.icu.text.DecimalFormat;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ritman.wallet.AdpatersViewHolder.CountryListViewHolder;
import ritman.wallet.MoneyTransferModuleV.viewmodels.BankTransferViewModel;
import ritman.wallet.R;
import ritman.wallet.billpayment.BillPaymentMainActivity;
import ritman.wallet.billpayment.helpers.BillerInputTypes;
import ritman.wallet.billpayment.viewmodel.BillPaymentViewModel;
import ritman.wallet.databinding.FragmentUtilityPaymentAccountBinding;
import ritman.wallet.di.JSONdi.Status;
import ritman.wallet.di.JSONdi.restRequest.BillDetailRequest;
import ritman.wallet.di.JSONdi.restRequest.GetWRBillerFieldsRequestN;
import ritman.wallet.di.JSONdi.restResponse.GetCasheAccomodationListResponse;
import ritman.wallet.di.JSONdi.restResponse.GetWRBillerFieldsResponseN;
import ritman.wallet.fragments.BaseFragment;
import ritman.wallet.interfaces.OnWRBillerFields;
import ritman.wallet.utils.Utils;

import static android.content.Context.WIFI_SERVICE;


public class UtilityPaymentAccountNoFragment extends BaseFragment<FragmentUtilityPaymentAccountBinding>
        implements OnWRBillerFields {


    List<GetWRBillerFieldsResponseN> wrBillerList;

    // BankTransferViewModel viewModel;
    BillPaymentViewModel viewModel;

    String billerId, fieldName1, fieldName2, fieldName3, MobileNumber, IMEINumber, ipAddress, validationid, billercategory;
    String paymentamount_validation, currency, payment_amount, biller_logo, partial_pay, pay_after_duedate;
    double total_payment_amount;
    String mPattern, mPattern1, mPattern2;
    final Calendar myCalendar = Calendar.getInstance();
    int day, month, year,age;
    Calendar mcalendar;
    SimpleDateFormat sdf;
    String myFormat;
    @Override
    protected void injectView() {

    }

    public boolean isValidateOne() {
        fieldName1 = wrBillerList.get(0).labelName;
        mPattern = wrBillerList.get(0).authenticator_regex;

        if (TextUtils.isEmpty(binding.accountNo.getText().toString())) {
            onMessage(getString(R.string.enter_txt).concat(wrBillerList.get(0).labelName));
            return false;
        } else if (binding.accountNo.getText().length() == 9 && fieldName1.equals("Consumer Number")) {
            binding.accountNo.getText().toString().concat(getString(R.string.comma));

        } else if (mPattern != null) {

            if (!binding.accountNo.getText().toString().matches(mPattern)) {
                onMessage(getString(R.string.enter_txt).concat(" ")
                        .concat(getString(R.string.correct)).concat(wrBillerList.get(0).labelName));
                return false;
            }

        }

        fieldName1 = wrBillerList.get(0).labelName;
        return true;

    }


    public boolean isValidateTwe() {
        mPattern = wrBillerList.get(0).authenticator_regex;
        mPattern1 = wrBillerList.get(1).authenticator_regex;

        if (TextUtils.isEmpty(binding.accountNo.getText().toString())) {
            onMessage(getString(R.string.enter_txt).concat(wrBillerList.get(0).labelName));
            return false;
        } else if (mPattern != null) {
            if (!binding.accountNo.getText().toString().matches(mPattern)) {
                onMessage(getString(R.string.enter_txt).concat(" ")
                        .concat(getString(R.string.correct)).concat(" ").concat(wrBillerList.get(0).labelName));

                return false;
            }
        } else if (TextUtils.isEmpty(binding.accountPolicyNo.getText().toString())) {
            onMessage(getString(R.string.enter_txt).concat(wrBillerList.get(1).labelName));
            return false;
        } else if (mPattern1 != null) {
            if (!binding.accountPolicyNo.getText().toString().matches(mPattern1)) {
                onMessage(getString(R.string.enter_txt).concat(" ")
                        .concat(getString(R.string.correct)).concat(wrBillerList.get(1).labelName));
                return false;
            }
        }
        fieldName1 = wrBillerList.get(0).labelName;
        fieldName2 = wrBillerList.get(1).labelName;
        return true;

    }

    public boolean isValidateThree() {
        mPattern = wrBillerList.get(0).authenticator_regex;
        mPattern1 = wrBillerList.get(1).authenticator_regex;
        mPattern2 = wrBillerList.get(2).authenticator_regex;

        if (TextUtils.isEmpty(binding.accountNo.getText().toString())) {
            onMessage(getString(R.string.enter_txt).concat(wrBillerList.get(0).labelName));
            return false;
        } else if (mPattern != null) {
            if (!binding.accountNo.getText().toString().matches(mPattern)) {
                onMessage(getString(R.string.enter_txt).concat(" ")
                        .concat(getString(R.string.correct)).concat(wrBillerList.get(0).labelName));

                return false;
            }
        } else if (TextUtils.isEmpty(binding.accountPolicyNo.getText().toString())) {
            onMessage(getString(R.string.enter_txt).concat(wrBillerList.get(1).labelName));
            return false;
        } else if (mPattern1 != null) {
            if (!binding.accountPolicyNo.getText().toString().matches(mPattern1)) {
                onMessage(getString(R.string.enter_txt).concat(" ")
                        .concat(getString(R.string.correct)).concat(wrBillerList.get(1).labelName));

                return false;
            }
        } else if (TextUtils.isEmpty(binding.reEnterAccountPolicy.getText().toString())) {
            onMessage(getString(R.string.enter_txt).concat(wrBillerList.get(2).labelName));
            return false;
        } else if (mPattern2 != null) {
            if (!binding.reEnterAccountPolicy.getText().toString().matches(mPattern2)) {
                onMessage(getString(R.string.enter_txt).concat(" ")
                        .concat(getString(R.string.correct)).concat(wrBillerList.get(2).labelName));
                return false;
            }
        }
        fieldName1 = wrBillerList.get(0).labelName;
        fieldName2 = wrBillerList.get(1).labelName;
        fieldName3 = wrBillerList.get(2).labelName;
        return true;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        wrBillerList = new ArrayList<>();
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

        viewModel = new ViewModelProvider(this).get(BillPaymentViewModel.class);

        if (paymentamount_validation.equals("Y")) {
            binding.linearAmount.setVisibility(View.VISIBLE);
        }

        getWRBillerFields();




        if (paymentamount_validation.equals("Y")) {
            binding.nextLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                    }

                }
            });
        } else {
            binding.nextLayout.setOnClickListener(v -> {
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
                }

            });
        }

        IMEINumber = getDeviceId(getContext());
        getIpAddressOfDevice();

    }

    private void updateLabel() {
        myFormat = "yyyy-MM-dd"; //In which you need put here
        sdf = new SimpleDateFormat(myFormat, Locale.US);
        binding.accountPolicyNo.setText(sdf.format(myCalendar.getTime()));

    }

    public void fillData() {
        if (binding.accountNo.getText().length() == 9 && fieldName1.equals("Consumer Number")) {
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

        if (binding.mobileno.getText().toString().equals("")) {
            onMessage("Please enter your mobile number");
        } else if (binding.mobileno.getText().toString().length() != 10) {
            onMessage("Please enter valid 10-digits mobile number");
        } else {
            MobileNumber = binding.mobileno.getText().toString();
            getBillDetails();

        }

    }

    public void fillData1() {
        if (binding.accountNo.getText().length() == 9 && fieldName1.equals("Consumer Number")) {
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
            fieldName2 = response.get(1).fieldName;
            if (fieldName2.equals("Date Of Birth(YYYY-MM-DD)")) {
                binding.accountPolicyNo.setFocusable(false);
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

                binding.accountPolicyNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        binding.accountPolicyNo.setFocusableInTouchMode(true);
                        binding.accountPolicyNo.setCursorVisible(false);
                        binding.accountPolicyNo.setShowSoftInputOnFocus(false);
                        // TODO Auto-generated method stub
                        new DatePickerDialog(getActivity(), R.style.PersonalLoanCalendarTheme,date,
                                myCalendar.get(Calendar.YEAR),
                                myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });
            }
            onSizeTwo(response);
        } else if (response.size() == 3) {
            onSizeThree(response);
        }

    }


    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }

    void onSizeOne(List<GetWRBillerFieldsResponseN> response) {

        binding.accountNoField.setHint(response.get(0).labelName);
        binding.accountNoField.getMaxWidth();
        // binding.accountNo.setFilters(new InputFilter[] { new InputFilter.LengthFilter(wrBillerList.get(0).maxLength) });

        binding.accountNo.setHint(response.get(0).description);
        if (response.get(0).type.equalsIgnoreCase(BillerInputTypes.NUMERIC)) {
            binding.accountNo.setInputType(InputType.TYPE_CLASS_NUMBER);
            //binding.accountNo.setFilters(new InputFilter[] { new InputFilter.LengthFilter(Integer.parseInt(String.valueOf(wrBillerList.get(0).maxLength))) });

        }

        binding.accountPolicyNoField.setVisibility(View.GONE);
        binding.accountPolicyNo.setVisibility(View.GONE);

        binding.descriptionField.setVisibility(View.GONE);
        binding.reEnterAccountPolicy.setVisibility(View.GONE);

    }

    void onSizeTwo(List<GetWRBillerFieldsResponseN> response) {
        binding.accountNoField.setHint(response.get(0).labelName);
        binding.accountNo.setHint(response.get(0).description);
        //binding.accountNo.setFilters(new InputFilter[] { new InputFilter.LengthFilter(Integer.parseInt(String.valueOf(wrBillerList.get(0).maxLength))) });

        if (response.get(0).type.equalsIgnoreCase(BillerInputTypes.NUMERIC)) {
            binding.accountNo.setInputType(InputType.TYPE_CLASS_NUMBER);
            //binding.accountNo.setFilters(new InputFilter[] { new InputFilter.LengthFilter(Integer.parseInt(String.valueOf(wrBillerList.get(0).maxLength))) });

        }

        binding.accountPolicyNoField.setHint(response.get(1).labelName);
        binding.accountPolicyNo.setHint(response.get(1).description);
        // binding.accountPolicyNo.setFilters(new InputFilter[] { new InputFilter.LengthFilter(Integer.parseInt(String.valueOf(wrBillerList.get(1).maxLength))) });

        if (response.get(1).type.equalsIgnoreCase(BillerInputTypes.NUMERIC)) {
            binding.accountPolicyNo.setInputType(InputType.TYPE_CLASS_NUMBER);
            //binding.accountPolicyNo.setFilters(new InputFilter[] { new InputFilter.LengthFilter(Integer.parseInt(String.valueOf(wrBillerList.get(1).maxLength))) });

        }

        binding.descriptionField.setVisibility(View.GONE);
        binding.reEnterAccountPolicy.setVisibility(View.GONE);
    }

    void onSizeThree(List<GetWRBillerFieldsResponseN> response) {
        binding.accountNoField.setHint(response.get(0).labelName);
        binding.accountNo.setHint(response.get(0).description);
        // binding.accountNo.setFilters(new InputFilter[] { new InputFilter.LengthFilter(Integer.parseInt(String.valueOf(wrBillerList.get(0).maxLength))) });

        if (response.get(0).type.equalsIgnoreCase(BillerInputTypes.NUMERIC)) {
            binding.accountNo.setInputType(InputType.TYPE_CLASS_NUMBER);
            //binding.accountNo.setFilters(new InputFilter[] { new InputFilter.LengthFilter(Integer.parseInt(String.valueOf(wrBillerList.get(0).maxLength))) });

        }

        binding.accountPolicyNoField.setHint(response.get(1).labelName);
        binding.accountPolicyNo.setHint(response.get(1).description);
        // binding.accountPolicyNo.setFilters(new InputFilter[] { new InputFilter.LengthFilter(Integer.parseInt(String.valueOf(wrBillerList.get(1).maxLength))) });
        if (response.get(1).type.equalsIgnoreCase(BillerInputTypes.NUMERIC)) {
            binding.accountPolicyNo.setInputType(InputType.TYPE_CLASS_NUMBER);
            // binding.accountPolicyNo.setFilters(new InputFilter[] { new InputFilter.LengthFilter(Integer.parseInt(String.valueOf(wrBillerList.get(1).maxLength))) });

        }
        binding.descriptionField.setHint(response.get(2).labelName);
        binding.reEnterAccountPolicy.setHint(response.get(2).description);
        // binding.reEnterAccountPolicy.setFilters(new InputFilter[] { new InputFilter.LengthFilter(Integer.parseInt(String.valueOf(wrBillerList.get(2).maxLength))) });

        if (response.get(2).type.equalsIgnoreCase(BillerInputTypes.NUMERIC)) {
            binding.reEnterAccountPolicy.setInputType(InputType.TYPE_CLASS_NUMBER);
            // binding.reEnterAccountPolicy.setFilters(new InputFilter[] { new InputFilter.LengthFilter(Integer.parseInt(String.valueOf(wrBillerList.get(2).maxLength))) });

        }
    }

    private void getWRBillerFields() {
        binding.progressBar.setVisibility(View.GONE);
        // GetWRBillerFieldsRequestN request =((BillPaymentMainActivity) getBaseActivity()).request;
        Utils.showCustomProgressDialog(getContext(), false);
        GetWRBillerFieldsRequestN request = new GetWRBillerFieldsRequestN();
        request.BillerID = billerId;
        request.countryCode = "IN";

        viewModel.GetWRBillerFields(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()

                , response -> {
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {

                            binding.colopsField.setVisibility(View.VISIBLE);

                            Utils.hideCustomProgressDialog();
                            //binding.progressBar.setVisibility(View.GONE);
                            //String S= response.resource.labelName;
                            onWRBillerField(response.resource.data);
                            //onMessage(response.resource.description);


                        } else {
                            onMessage(response.resource.description);
                            Utils.hideCustomProgressDialog();
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
        Utils.showCustomProgressDialog(getContext(), false);
        //binding.progressBar.setVisibility(View.VISIBLE);
        BillDetailRequest request = new BillDetailRequest();
        request.countryCode = "IN";
        request.BillerID = billerId;
        request.MobileNumber = MobileNumber;
        request.Field2 = binding.accountPolicyNo.getText().toString();
        request.Field3 = binding.reEnterAccountPolicy.getText().toString();
        if (fieldName1 != null && fieldName2 != null && fieldName3 != null) {
            request.Field1Name = fieldName1;
            request.Field2Name = fieldName2;
            request.Field3Name = fieldName3;
        } else if (fieldName1 != null && fieldName2 != null) {
            request.Field1Name = fieldName1;
            request.Field2Name = fieldName2;
            request.Field3Name = "";
        } else {
            request.Field1Name = fieldName1;
            request.Field2Name = "";
            request.Field3Name = "";
        }

        request.imei = IMEINumber;//"5468748458458454";
        request.ip = ipAddress;

        if (binding.accountNo.getText().length() == 9 && fieldName1.equals("Consumer Number")) {
            request.Field1 = binding.accountNo.getText().toString().concat(getString(R.string.comma));
        } else {
            request.Field1 = binding.accountNo.getText().toString();
        }

        if (paymentamount_validation.equals("Y")) {
            request.payment_amount = payment_amount;
            request.currency = currency;
        }


        viewModel.getBillDetails(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()

                , response -> {
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));

                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            Utils.hideCustomProgressDialog();
                            // binding.progressBar.setVisibility(View.GONE);

                            validationid = response.resource.data.validationid;
                            billercategory = response.resource.data.biller_category;

                            Bundle bundle = new Bundle();
                            if (binding.accountNo.getText().length() == 9 && fieldName1.equals("Consumer Number")) {
                                bundle.putString("Field1", binding.accountNo.getText().toString().
                                        concat(getString(R.string.comma)));

                                bundle.putString("Field2", binding.accountPolicyNo.getText().toString());
                                bundle.putString("Field3", binding.reEnterAccountPolicy.getText().toString());
                                bundle.putString("BillerID", billerId);
                                bundle.putString("MobileNumber", MobileNumber);
                                bundle.putString("fieldName1", fieldName1);
                                bundle.putString("fieldName2", fieldName2);
                                bundle.putString("fieldName3", fieldName3);
                                bundle.putString("biller_logo", biller_logo);

                                bundle.putString("paymentamount_validation", paymentamount_validation);
                                bundle.putString("partial_pay", partial_pay);
                                bundle.putString("pay_after_duedate", pay_after_duedate);

                                if (paymentamount_validation.equals("Y")) {
                                    bundle.putString("payment_amount", payment_amount);
                                    bundle.putString("currency", currency);
                                }

                            } else {
                                bundle.putString("Field1", binding.accountNo.getText().toString());
                                bundle.putString("Field2", binding.accountPolicyNo.getText().toString());
                                bundle.putString("Field3", binding.reEnterAccountPolicy.getText().toString());
                                bundle.putString("BillerID", billerId);
                                bundle.putString("MobileNumber", MobileNumber);
                                bundle.putString("fieldName1", fieldName1);
                                bundle.putString("fieldName2", fieldName2);
                                bundle.putString("fieldName3", fieldName3);
                                bundle.putString("paymentamount_validation", paymentamount_validation);
                                bundle.putString("partial_pay", partial_pay);
                                bundle.putString("biller_logo", biller_logo);
                                bundle.putString("pay_after_duedate", pay_after_duedate);

                                if (paymentamount_validation.equals("Y")) {
                                    bundle.putString("payment_amount", payment_amount);
                                    bundle.putString("currency", currency);
                                }
                            }
                            if (paymentamount_validation.equals("Y")) {
                                Navigation.findNavController(binding.getRoot()).navigate(R.id
                                        .action_utilityPaymentAccountNoFragment_to_utilityBillerDetailsFragment, bundle);
                            } else {
                                Navigation.findNavController(binding.getRoot()).navigate(R.id
                                        .action_utilityPaymentAccountNoFragment_to_utilityBillerDetailsFragment, bundle);
                            }

                        } else if (response.resource.responseCode.equals(100)) {
                            onMessage(response.resource.data.message);
                            Utils.hideCustomProgressDialog();


                        } else {
                            Utils.hideCustomProgressDialog();
                            // binding.progressBar.setVisibility(View.GONE);
                            onMessage(response.resource.description);
//                            showSucces(response.resource.description, getString(R.string.biller_details)
//                                    , true);

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

    public static boolean isValidEmail(String payment_amount) {

        String pattern = "^\\d+(\\.\\d+)?$"; // 4 digits followe by . followed by 2 digits
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(payment_amount);

        return m.matches();
    }

}