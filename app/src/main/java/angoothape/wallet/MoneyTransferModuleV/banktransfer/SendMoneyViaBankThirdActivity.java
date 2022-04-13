package angoothape.wallet.MoneyTransferModuleV.banktransfer;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import angoothape.wallet.MoneyTransferModuleV.MoneyTransferMainLayout;
import angoothape.wallet.MoneyTransferModuleV.viewmodels.BankTransferViewModel;
import angoothape.wallet.beneficairyRegistration.BeneficiaryRegistrationActivity;
import angoothape.wallet.databinding.ActivityCashPaymentThirdBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.CalTransferRequest;
import angoothape.wallet.di.JSONdi.restRequest.ConfirmRitmanPayRequest;
import angoothape.wallet.di.JSONdi.restRequest.GetModeRequest;
import angoothape.wallet.di.JSONdi.restResponse.CaltransferResponse;
import angoothape.wallet.di.JSONdi.restResponse.ConfirmRitmanPayTransfer;
import angoothape.wallet.di.JSONdi.restResponse.PaymentModes;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.di.XMLdi.RequestHelper.GetSendRecCurrencyRequest;
import angoothape.wallet.di.XMLdi.ResponseHelper.CalTransferResponse;
import angoothape.wallet.di.XMLdi.ResponseHelper.GetBeneficiaryListResponse;
import angoothape.wallet.di.XMLdi.ResponseHelper.GetCountryListResponse;
import angoothape.wallet.di.XMLdi.ResponseHelper.GetSendRecCurrencyResponse;
import angoothape.wallet.di.XMLdi.ResponseHelper.GetSourceOfIncomeListResponse;
import angoothape.wallet.di.XMLdi.ResponseHelper.PurposeOfTransferListResponse;
import angoothape.wallet.di.XMLdi.apicaller.GetSendRecCurrencyTask;
import angoothape.wallet.dialogs.DialogCurrency;
import angoothape.wallet.fragments.BaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;

import angoothape.wallet.R;
import angoothape.wallet.interfaces.OnGetTransferRates;
import angoothape.wallet.interfaces.OnSelectCountry;
import angoothape.wallet.interfaces.OnSelectCurrency;
import angoothape.wallet.interfaces.OnSelectSourceOfIncome;
import angoothape.wallet.interfaces.OnSelectTransferPurpose;
import angoothape.wallet.utils.IsNetworkConnection;
import angoothape.wallet.utils.MoneyValueFilter;
import angoothape.wallet.utils.Utils;
import okhttp3.internal.Util;

import static angoothape.wallet.utils.MoneyValueFilter.getDecimalFormattedString;

public class SendMoneyViaBankThirdActivity extends BaseFragment<ActivityCashPaymentThirdBinding>
        implements OnSelectTransferPurpose, OnSelectCountry, OnSelectCurrency, OnGetTransferRates
        , OnSelectSourceOfIncome {
    BankTransferViewModel viewModel;
    Integer soucreOfIncome;
    boolean showingBreakPoint = false;

    GetBeneficiaryListResponse benedetails;
    String customerNo;
    Double PayInAmount;

    boolean isModeSelected = false;
    PaymentModes selectedPaymentMode;

    @Override
    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.sendingCurrency.getText().toString())) {
            onMessage(getString(R.string.plz_select_sending_currency));
            return false;
        } else if (TextUtils.isEmpty(binding.sendingAmountField.getText().toString())) {
            onMessage(getString(R.string.please_enter_amount));
            return false;
        }
        return true;
    }


    @Override
    protected void injectView() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (getBaseActivity() instanceof BeneficiaryRegistrationActivity) {
            ((BeneficiaryRegistrationActivity) getBaseActivity()).binding.toolBar.titleTxt
                    .setText(getString(R.string.bank_transfer));
        } else {
            ((MoneyTransferMainLayout) getBaseActivity()).binding.toolBar.titleTxt
                    .setText(getString(R.string.bank_transfer));

            binding.convertNow.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.background_beneficiary_bank_pos_red));
            binding.viewPriceBreakDown.setTextColor(Color.parseColor("#FF2D55"));
            binding.sendNowLayout.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.colorPrimary));
        }
    }


    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(BankTransferViewModel.class);
        getPaymentModes();
        benedetails = getArguments().getParcelable("bene");
        customerNo = getArguments().getString("customer_no");
        binding.receivingCurrency.setText(benedetails.payOutCurrency);
        binding.viewPriceBreakDown.setOnClickListener(v -> {
            if (!showingBreakPoint) {
                showingBreakPoint = true;
                binding.commissionLayout.setVisibility(View.VISIBLE);
                binding.sendingAmountLayout.setVisibility(View.VISIBLE);
                binding.viewPriceBreakDown.setText(getString(R.string.hide_break_down));
            } else {
                showingBreakPoint = false;
                binding.commissionLayout.setVisibility(View.GONE);
                binding.sendingAmountLayout.setVisibility(View.GONE);
                binding.viewPriceBreakDown.setText(getString(R.string.view_price_break_down));
            }

        });

        binding.sendNowLayout.setOnClickListener(view -> {
            goToNext();
        });

        binding.convertNow.setOnClickListener(v -> {
            if (isValidate()) {
                binding.convertNow.setEnabled(false);
                Utils.showCustomProgressDialog(getContext(), false);
                CalTransferRequest request = new CalTransferRequest();
                request.PayInCurrency = "INR";
                request.PayoutCurrency = "INR";
                request.PaymentMode = "BANK";
                request.TransferCurrency = "INR";
                request.TransferAmount = binding.sendingAmountField.getText().toString();

                viewModel.getCalTransfer(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()
                        , response -> {
                            binding.convertNow.setEnabled(true);
                            Utils.hideCustomProgressDialog();
                            if (response.status == Status.ERROR) {
                                onError(getString(response.messageResourceId));
                            } else {
                                assert response.resource != null;
                                if (response.resource.responseCode.equals(101)) {
                                    onSuccess(response.resource.description);
                                    showRates(response.resource.data);
                                    PayInAmount = response.resource.data.getPayInAmount();
                                } else {
                                    onError(response.resource.description);
                                }
                            }
                        });
            }

        });


        binding.sendingAmountField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    binding.afterConvertRatesLayout.setVisibility(View.GONE);
                    binding.convertNow.setVisibility(View.VISIBLE);
                    binding.commissionLayout.setVisibility(View.GONE);
                    binding.sendingAmountLayout.setVisibility(View.GONE);
                    binding.viewPriceBreakDown.setText(getString(R.string.view_price_break_down));
                    binding.payOutAmount.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                int cursorPosition = binding.sendingAmountField.getSelectionEnd();
                String originalStr = binding.sendingAmountField.getText().toString();
                //To restrict only two digits after decimal place
                binding.sendingAmountField.setFilters(new InputFilter[]{new MoneyValueFilter(2)});
                try {
                    binding.sendingAmountField.removeTextChangedListener(this);
                    String value = binding.sendingAmountField.getText().toString();

                    if (value != null && !value.equals("")) {
                        if (value.startsWith(".")) {
                            binding.sendingAmountField.setText("0.");
                        }
                        if (value.startsWith("0") && !value.startsWith("0.")) {
                            binding.sendingAmountField.setText("");
                        }
                        String str = binding.sendingAmountField.getText().toString().replaceAll(",", "");
                        if (!value.equals(""))
                            binding.sendingAmountField.setText(getDecimalFormattedString(str));

                        int diff = binding.sendingAmountField.getText().toString().length() - originalStr.length();
                        binding.sendingAmountField.setSelection(cursorPosition + diff);

                    }
                    binding.sendingAmountField.addTextChangedListener(this);
                } catch (Exception ex) {
                    Log.e("Textwatcher", ex.getMessage());
                    binding.sendingAmountField.addTextChangedListener(this);
                }
            }
        });

    }


    private void getPaymentModes() {
        binding.paymentModesSpinner.setPrompt("Select Payment mode");
        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim();

        GetModeRequest getModeRequest = new GetModeRequest();

        String body = RestClient.makeGSONString(getModeRequest);

        AERequest request = new AERequest();
        request.body = AESHelper.encrypt(body.trim(), gKey.trim());


        viewModel.getPaymentModes(request, KeyHelper.getKey(getSessionManager().getMerchantName()).trim(),
                KeyHelper.getSKey(KeyHelper
                        .getKey(getSessionManager().getMerchantName())).trim()).observe(getViewLifecycleOwner()
                , response -> {
                    if (response.status == Status.ERROR) {
                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            onMessage(response.resource.description);

                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , gKey);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<PaymentModes>>() {
                                }.getType();

                                List<PaymentModes> paymentModesList = gson.fromJson(bodyy, type);
                                fillSpinner(paymentModesList);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            onError(response.resource.description);
                        }
                    }
                });


    }


    void fillSpinner(List<PaymentModes> paymentModes) {
        ArrayAdapter userAdapter = new ArrayAdapter(getContext(), R.layout.spinner, paymentModes);

        binding.paymentModesSpinner.setAdapter(userAdapter);
        // binding.paymentModesSpinner.setSelection(1);
        binding.paymentModesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPaymentMode = new PaymentModes();
                isModeSelected = true;
                selectedPaymentMode = (PaymentModes) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void confirmRitmanPay() {
        Utils.showCustomProgressDialog(getContext(), false);
        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim();

        ConfirmRitmanPayRequest confirmRitmanPayRequest = new ConfirmRitmanPayRequest();
        confirmRitmanPayRequest.BeneficiaryNo = benedetails.beneficiaryNumber;
        confirmRitmanPayRequest.PaymentID = selectedPaymentMode.paymentID;
        String body = RestClient.makeGSONString(confirmRitmanPayRequest);
        Log.e("confirmRitmanPay: ", body);
        AERequest request = new AERequest();
        request.body = AESHelper.encrypt(body.trim(), gKey.trim());

        viewModel.confirmRitmanPay(request, KeyHelper.getKey(getSessionManager().getMerchantName()).trim(),
                KeyHelper.getSKey(KeyHelper
                        .getKey(getSessionManager().getMerchantName())).trim()).observe(getViewLifecycleOwner()
                , response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            //         onMessage(response.resource.description);
                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , gKey);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<ConfirmRitmanPayTransfer>() {
                                }.getType();
                                ConfirmRitmanPayTransfer data = gson.fromJson(bodyy, type);
                                if (data.verification) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("total_payable", binding.totalPayableAmount.getText().toString());
                                    bundle.putBoolean("is_from_cash", false);
                                    bundle.putParcelable("bene", benedetails);
                                    bundle.putDouble("PayInAmount", PayInAmount);
                                    bundle.putParcelable("payment_mode", selectedPaymentMode);
                                    Navigation.findNavController(binding.getRoot())
                                            .navigate(R.id.action_sendMoneyViaBankThirdActivity_to_cashTransferSummaryFragment2
                                                    , bundle);
                                } else {
                                    onError("Payment mode is not verified");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            onError(response.resource.description);
                        }
                    }
                });


    }


    public void goToNext() {
        if (!isModeSelected) {
            onMessage("Select Payment mode");
        } else {
            confirmRitmanPay();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_cash_payment_third;
    }


    @Override
    public void onSelectTransferPurpose(PurposeOfTransferListResponse response) {

    }


    public void showRates(CaltransferResponse response) {
        binding.payOutAmount.setText(response.getPayoutAmount().toString());
        binding.sendingAmountTxt.setText(response.getPayoutAmount().toString());
        binding.commissionAmountTxt.setText(response.getCommission().toString());
        binding.totalPayableAmount.setText(response.getTotalPayable().toString());
        binding.afterConvertRatesLayout.setVisibility(View.VISIBLE);
        binding.convertNow.setVisibility(View.GONE);
    }

    @Override
    public void onSelectCountry(GetCountryListResponse country) {
        GetSendRecCurrencyRequest request = new GetSendRecCurrencyRequest();
        request.countryType = country.countryType;
        request.countryShortName = country.countryShortName;
        request.languageId = getSessionManager().getlanguageselection();
        if (IsNetworkConnection.checkNetworkConnection(getActivity())) {
            GetSendRecCurrencyTask task = new GetSendRecCurrencyTask(getActivity(), this);
            task.execute(request);
        } else {
            onMessage(getString(R.string.no_internet));
        }
        binding.afterConvertRatesLayout.setVisibility(View.GONE);
    }


    @Override
    public void onCurrencyResponse(List<GetSendRecCurrencyResponse> response) {
        if (response.size() == 1) {
            binding.sendingCurrency.setText(response.get(0).currencyShortName);

            binding.payOutAmount.setText("");
            setSendingCurrencyImage(response.get(0).image_URL);
            binding.afterConvertRatesLayout.setVisibility(View.GONE);
            binding.convertNow.setVisibility(View.VISIBLE);
        } else {
            //show dialog
            DialogCurrency dialogCurrency = new DialogCurrency(response, this);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            dialogCurrency.show(transaction, "");
        }
    }


    @Override
    public void onCurrencySelect(GetSendRecCurrencyResponse response) {
        binding.sendingCurrency.setText(response.currencyShortName);

        setSendingCurrencyImage(response.image_URL);
        binding.payOutAmount.setText("");
        binding.afterConvertRatesLayout.setVisibility(View.GONE);
        binding.convertNow.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGetTransferRates(CalTransferResponse response) {
        // showRates(response);
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }


    @Override
    public void onSelectSourceOfIncome(GetSourceOfIncomeListResponse response) {
        soucreOfIncome = response.id;
    }


    public void setSendingCurrencyImage(String url) {
        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource,
                                                @Nullable Transition<? super Bitmap> transition) {
                        binding.sendingCurrencyImage.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    public void setReceivingCurrencyImage(String url) {
        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource,
                                                @Nullable Transition<? super Bitmap> transition) {
                        binding.receivingCurrencyImage.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }
}
