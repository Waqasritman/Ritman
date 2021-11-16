package angoothape.wallet.SendMoney;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

import angoothape.wallet.MoneyTransferModuleV.MoneyTransferMainLayout;
import angoothape.wallet.databinding.ActivityTransferMoneyBinding;
import angoothape.wallet.R;
import angoothape.wallet.di.XMLdi.RequestHelper.CalTransferRequest;
import angoothape.wallet.di.XMLdi.ResponseHelper.CalTransferResponse;
import angoothape.wallet.di.XMLdi.ResponseHelper.GetCountryListResponse;
import angoothape.wallet.di.XMLdi.ResponseHelper.GetSendRecCurrencyResponse;
import angoothape.wallet.di.XMLdi.apicaller.CheckRatesTask;
import angoothape.wallet.dialogs.DialogCountry;
import angoothape.wallet.dialogs.DialogCurrency;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.home_module.ClassChangerHelper;
import angoothape.wallet.home_module.NewDashboardActivity;
import angoothape.wallet.home_module.fragments.HomeFragment;
import angoothape.wallet.interfaces.OnGetTransferRates;
import angoothape.wallet.interfaces.OnSelectCountry;
import angoothape.wallet.interfaces.OnSelectCurrency;
import angoothape.wallet.utils.CountryParser;
import angoothape.wallet.utils.IsNetworkConnection;
import angoothape.wallet.utils.MoneyValueFilter;
import angoothape.wallet.utils.NumberFormatter;

import static angoothape.wallet.utils.MoneyValueFilter.getDecimalFormattedString;

public class ConvertRatesActivity extends BaseFragment<ActivityTransferMoneyBinding>
        implements OnSelectCountry, OnSelectCurrency, OnGetTransferRates {

    CalTransferRequest calTransferRequest; // variable to store the calTransfer
    String paymentMode = "bank"; // default bank
    boolean isSending = false;

    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.sendingCurrency.getText().toString())) {
            onMessage(getString(R.string.plz_select_sending_currency));
            return false;
        } else if (TextUtils.isEmpty(binding.receivingCurrency.getText().toString())) {
            onMessage(getString(R.string.plz_select_receiving_currency));
            return false;
        } else if (TextUtils.isEmpty(binding.sendingAmountField.getText().toString())) {
            onMessage(getString(R.string.please_enter_amount));
            return false;
        }
        return true;
    }


    @Override
    public void onResume() {
        super.onResume();
        ((NewDashboardActivity) getBaseActivity()).hideHumBurger(ClassChangerHelper.SAVE_BANK);

    }

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.toolBar.backBtn
                .setColorFilter(ContextCompat.getColor(getContext(),
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);
        binding.toolBar.toolBarFinal.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        binding.toolBar.crossBtn.setVisibility(View.GONE);

        binding.toolBar.backBtn.setOnClickListener(v -> {
            ((NewDashboardActivity) getBaseActivity())
                    .moveToFragment(new HomeFragment());
        });


        binding.toolBar.titleTxt.setText(getString(R.string.best_rate));

        binding.toolBar.crossBtn.setVisibility(View.GONE);

        calTransferRequest = new CalTransferRequest();
        calTransferRequest.PayInCurrency = binding.sendingCurrency.getText().toString();
        calTransferRequest.TransferCurrency = binding.sendingCurrency.getText().toString();
        calTransferRequest.languageId = getSessionManager().getlanguageselection();
        binding.convertNow.setOnClickListener(v -> {
            if (isValidate()) {
                calTransferRequest.PaymentMode = paymentMode;
                calTransferRequest.TransferAmount = Double.parseDouble(NumberFormatter.removeCommas(
                        binding.sendingAmountField.getText().toString()));

                if (IsNetworkConnection.checkNetworkConnection(getBaseActivity())) {
                    CheckRatesTask task = new CheckRatesTask(getBaseActivity(), this);
                    task.execute(calTransferRequest);
                } else {
                    onMessage(getString(R.string.no_internet));
                }
            }
        });


        binding.receivngCurrencyLayout.setOnClickListener(v -> {
            if (IsNetworkConnection.checkNetworkConnection(getBaseActivity())) {
                isSending = false;
                DialogCountry dialogCountry = new DialogCountry(CountryParser.RECEIVE, this
                        , true);
                FragmentTransaction fm = getParentFragmentManager().beginTransaction();
                dialogCountry.show(fm, "");
            } else {
                onMessage(getString(R.string.no_internet));
            }

        });

        binding.transferNow.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseActivity(), MoneyTransferMainLayout.class);
            startActivity(intent);
        });

        binding.sendingCurrencyLayout.setOnClickListener(v -> {
            isSending = true;
            if (IsNetworkConnection.checkNetworkConnection(getBaseActivity())) {
                DialogCountry dialogCountry = new DialogCountry(CountryParser.SEND, this
                        , true);
                FragmentTransaction fm = getParentFragmentManager().beginTransaction();
                dialogCountry.show(fm, "");
            } else {
                onMessage(getString(R.string.no_internet));
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

    @Override
    public int getLayoutId() {
        return R.layout.activity_transfer_money;
    }


    public void convertRatesRequest() {
        calTransferRequest.PaymentMode = paymentMode;
        calTransferRequest.TransferAmount = 1d;
        calTransferRequest.languageId = getSessionManager().getlanguageselection();
        if (IsNetworkConnection.checkNetworkConnection(getBaseActivity())) {
            CheckRatesTask task = new CheckRatesTask(getBaseActivity(), this);
            task.execute(calTransferRequest);
        } else {
            onMessage(getString(R.string.no_internet));
        }
    }

    @Override
    public void onGetTransferRates(CalTransferResponse response) {
        showRates(response);
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }

    @Override
    public void onSelectCountry(GetCountryListResponse country) {

        if (isSending) {
            binding.sendingCurrency.setText(country.currencyShortName);
            calTransferRequest.PayInCurrency = country.currencyShortName;
            calTransferRequest.TransferCurrency = country.currencyShortName;
            setSendingCurrencyImage(country.imageURL);
        } else {
            binding.receivingCurrency.setText(country.currencyShortName);
            calTransferRequest.PayoutCurrency = country.currencyShortName;
            setReceivingCurrencyImage(country.imageURL);
        }

        if (!TextUtils.isEmpty(binding.sendingCurrency.getText().toString())
                && !TextUtils.isEmpty(binding.receivingCurrency.getText().toString())) {
            if (TextUtils.isEmpty(binding.sendingAmountField.getText().toString())) {
                binding.sendingAmountField.setText("1");
                convertRatesRequest();
            }
            binding.sendingAmountField.requestFocus();
        }


//        GetSendRecCurrencyRequest request = new GetSendRecCurrencyRequest();
//        request.countryType = country.countryType;
//        request.countryShortName = country.countryShortName;
//        request.languageId = getSessionManager().getlanguageselection();
//        if (IsNetworkConnection.checkNetworkConnection(getBaseActivity())) {
//            GetSendRecCurrencyTask task = new GetSendRecCurrencyTask(getBaseActivity(), this);
//            task.execute(request);
//        } else {
//            onMessage(getString(R.string.no_internet));
//        }
//
//        binding.afterConvertRatesLayout.setVisibility(View.GONE);
    }

    @Override
    public void onCurrencyResponse(List<GetSendRecCurrencyResponse> response) {
        if (response.size() == 1) {
            if (isSending) {
                binding.sendingCurrency.setText(response.get(0).currencyShortName);
                calTransferRequest.PayInCurrency = response.get(0).currencyShortName;
                calTransferRequest.TransferCurrency = response.get(0).currencyShortName;
                setSendingCurrencyImage(response.get(0).image_URL);
            } else {
                binding.receivingCurrency.setText(response.get(0).currencyShortName);
                calTransferRequest.PayoutCurrency = response.get(0).currencyShortName;
                setReceivingCurrencyImage(response.get(0).image_URL);
            }

            if (!TextUtils.isEmpty(binding.sendingCurrency.getText().toString())
                    && !TextUtils.isEmpty(binding.receivingCurrency.getText().toString())) {
                if (TextUtils.isEmpty(binding.sendingAmountField.getText().toString())) {
                    binding.sendingAmountField.setText("1");
                    convertRatesRequest();
                }
                binding.sendingAmountField.requestFocus();
            }
        } else {
            //show dialog
            DialogCurrency dialogCurrency = new DialogCurrency(response, this);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            dialogCurrency.show(transaction, "");
        }

        binding.payOutAmount.setText("");
    }

    @Override
    public void onCurrencySelect(GetSendRecCurrencyResponse response) {
        if (isSending) {
            binding.sendingCurrency.setText(response.currencyShortName);
            setSendingCurrencyImage(response.image_URL);
            calTransferRequest.PayInCurrency = response.currencyShortName;
            calTransferRequest.TransferCurrency = response.currencyShortName;
        } else {
            binding.receivingCurrency.setText(response.currencyShortName);
            calTransferRequest.PayoutCurrency = response.currencyShortName;
            setReceivingCurrencyImage(response.image_URL);
        }

        binding.convertNow.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(binding.sendingCurrency.getText().toString())
                && !TextUtils.isEmpty(binding.receivingCurrency.getText().toString())) {
            if (TextUtils.isEmpty(binding.sendingAmountField.getText().toString())) {
                binding.sendingAmountField.setText("1");
                convertRatesRequest();
            }
            binding.sendingAmountField.requestFocus();
        }
        binding.payOutAmount.setText("");
    }

    public void showRates(CalTransferResponse response) {
        binding.payOutAmount.setText(NumberFormatter.decimal(response.payoutAmount.floatValue()));
        binding.sendingAmountTxt.setText(NumberFormatter.decimal(
                response.payInAmount.floatValue()));
        binding.totalPayableAmount.setText(NumberFormatter.decimal(
                response.totalPayable.floatValue()));
        binding.afterConvertRatesLayout.setVisibility(View.GONE);
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
