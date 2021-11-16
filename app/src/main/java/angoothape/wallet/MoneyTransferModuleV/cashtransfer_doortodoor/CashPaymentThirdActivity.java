package angoothape.wallet.MoneyTransferModuleV.cashtransfer_doortodoor;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import angoothape.wallet.MoneyTransferModuleV.MoneyTransferMainLayout;
import angoothape.wallet.databinding.ActivityCashPaymentThirdBinding;
import angoothape.wallet.di.XMLdi.RequestHelper.CalTransferRequest;
import angoothape.wallet.di.XMLdi.RequestHelper.CountryPayOutCurrencyRequest;
import angoothape.wallet.di.XMLdi.RequestHelper.GetSendRecCurrencyRequest;
import angoothape.wallet.di.XMLdi.RequestHelper.GetWalletCurrencyListRequest;
import angoothape.wallet.di.XMLdi.RequestHelper.TootiPayRequest;
import angoothape.wallet.di.XMLdi.ResponseHelper.CalTransferResponse;
import angoothape.wallet.di.XMLdi.ResponseHelper.GetCountryListResponse;
import angoothape.wallet.di.XMLdi.ResponseHelper.GetSendRecCurrencyResponse;
import angoothape.wallet.di.XMLdi.ResponseHelper.GetSourceOfIncomeListResponse;
import angoothape.wallet.di.XMLdi.ResponseHelper.PurposeOfTransferListResponse;
import angoothape.wallet.di.XMLdi.apicaller.CheckRatesTask;
import angoothape.wallet.di.XMLdi.apicaller.CountryPayoutCurrencyTask;
import angoothape.wallet.di.XMLdi.apicaller.GetSendRecCurrencyTask;
import angoothape.wallet.di.XMLdi.apicaller.GetWalletCurrencyListTask;
import angoothape.wallet.dialogs.DialogCurrency;
import angoothape.wallet.dialogs.DialogTransferPurpose;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.interfaces.OnGetTransferRates;
import angoothape.wallet.interfaces.OnSelectCountry;
import angoothape.wallet.interfaces.OnSelectCurrency;
import angoothape.wallet.interfaces.OnSelectSourceOfIncome;
import angoothape.wallet.interfaces.OnSelectTransferPurpose;
import angoothape.wallet.utils.IsNetworkConnection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import angoothape.wallet.R;

import java.util.List;

import angoothape.wallet.utils.MoneyValueFilter;
import angoothape.wallet.utils.NumberFormatter;

import static angoothape.wallet.utils.MoneyValueFilter.getDecimalFormattedString;

public class CashPaymentThirdActivity extends BaseFragment<ActivityCashPaymentThirdBinding>
        implements OnSelectTransferPurpose, OnSelectCountry, OnSelectCurrency, OnGetTransferRates
        , OnSelectSourceOfIncome {


    CalTransferRequest calTransferRequest; // variable to store the calTransfer
    Integer soucreOfIncome;
    String paymentMode = "cash"; // default bank
    boolean isTootiPay = false;
    TootiPayRequest tootiPayRequest;
    boolean showingBreakPoint = false;
    boolean isSendingCurrencySelected = false;


    @Override
    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.sendingCurrency.getText().toString())) {
            onMessage(getString(R.string.plz_select_sending_currency));
            return false;
        } else if (TextUtils.isEmpty(binding.receivingCurrency.getText().toString())) {
            onMessage(getString(R.string.plz_select_receiving_currency));
            return false;
        }

        else if (TextUtils.isEmpty(binding.sendingAmountField.getText().toString())) {
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
        ((MoneyTransferMainLayout) getBaseActivity()).binding.toolBar.toolBarFinal
                .setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        ((MoneyTransferMainLayout) getBaseActivity()).binding.toolBar.backBtn
                .setColorFilter(ContextCompat.getColor(getContext(),
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);
        ((MoneyTransferMainLayout) getBaseActivity()).binding.toolBar.titleTxt
                .setText(getString(R.string.cash_transfer));
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        tootiPayRequest = new TootiPayRequest();
        calTransferRequest = new CalTransferRequest();
        binding.receivingCurrency.setText(((MoneyTransferMainLayout) getBaseActivity()).bankTransferViewModel
                .beneficiaryDetails.payOutCurrency);

        setReceivingCurrencyImage(((MoneyTransferMainLayout) getBaseActivity()).bankTransferViewModel
                .beneficiaryDetails.imageURL);
        calTransferRequest.PayoutCurrency = binding.receivingCurrency.getText().toString(); // set receiving currency
        calTransferRequest.PayInCurrency = binding.sendingCurrency.getText().toString();
        calTransferRequest.TransferCurrency = binding.sendingCurrency.getText().toString();
        calTransferRequest.languageId = getSessionManager().getlanguageselection();
        calTransferRequest.payInCountry = getSessionManager().getResidenceCountry();
        calTransferRequest.payOutCountry = ((MoneyTransferMainLayout) getBaseActivity()).bankTransferViewModel
                .beneficiaryDetails.payOutCountryCode;



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

        });

        binding.convertNow.setOnClickListener(v -> {
            if (isValidate()) {

                calTransferRequest.PaymentMode = paymentMode;
                calTransferRequest.TransferAmount = Double.parseDouble(
                        NumberFormatter.removeCommas(binding.sendingAmountField
                                .getText().toString()));
                if (IsNetworkConnection.checkNetworkConnection(getActivity())) {
                    CheckRatesTask task = new CheckRatesTask(getActivity(), this);
                    task.execute(calTransferRequest);
                } else {
                    onMessage(getString(R.string.no_internet));
                }

            }

        });




        binding.sendingCurrencyLayout.setOnClickListener(v -> {
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                isSendingCurrencySelected = true;
                GetWalletCurrencyListRequest request = new GetWalletCurrencyListRequest();
                request.languageId = getSessionManager().getlanguageselection();
                GetWalletCurrencyListTask getWalletCurrencyListTask = new GetWalletCurrencyListTask(getContext()
                        , this);
                getWalletCurrencyListTask.execute(request);
            } else {
                onMessage(getString(R.string.no_internet));
            }
        });



        binding.receivngCurrencyLayout.setOnClickListener( v-> {
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                isSendingCurrencySelected = false;
                CountryPayOutCurrencyRequest request = new CountryPayOutCurrencyRequest();
                request.languageId = getSessionManager().getlanguageselection();
                request.countryShortName = calTransferRequest.payOutCountry;

                CountryPayoutCurrencyTask task = new CountryPayoutCurrencyTask(getContext()
                 , this);
                task.execute(request);

            } else {
                onMessage(getString(R.string.no_internet));
            }
        });





    }



    public void showTransferDialog() {
        DialogTransferPurpose dialogTransferPurpose =
                new DialogTransferPurpose(this);
        FragmentTransaction fm = getParentFragmentManager().beginTransaction();
        dialogTransferPurpose.show(fm, "");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_cash_payment_third;
    }

    @Override
    public void onSelectTransferPurpose(PurposeOfTransferListResponse response) {

        tootiPayRequest.purposeOfTransfer = response.purposeOfTransferID;
    }


    public void showRates(CalTransferResponse response) {
        binding.payOutAmount.setText(NumberFormatter.decimal(response.payoutAmount.floatValue()));
        binding.sendingAmountTxt.setText(NumberFormatter.decimal(
                response.payInAmount.floatValue()));
        binding.totalPayableAmount.setText(String.valueOf(
                NumberFormatter.decimal(response.totalPayable.floatValue())));
        binding.commissionAmountTxt.setText(String.valueOf(
                NumberFormatter.decimal(response.commission.floatValue())));
        binding.afterConvertRatesLayout.setVisibility(View.VISIBLE);
        binding.convertNow.setVisibility(View.GONE);
    }

    @Override
    public void onSelectCountry(GetCountryListResponse country) {
        GetSendRecCurrencyRequest request = new GetSendRecCurrencyRequest();
        request.countryType = country.countryType;
        request.countryShortName = country.countryShortName;
        if (IsNetworkConnection.checkNetworkConnection(getActivity())) {
            GetSendRecCurrencyTask task = new GetSendRecCurrencyTask(getActivity(), this);
            task.execute(request);
        } else {
            onMessage(getString(R.string.no_internet));
        }

        binding.afterConvertRatesLayout.setVisibility(View.GONE);
        binding.convertNow.setVisibility(View.VISIBLE);
    }


    @Override
    public void onCurrencyResponse(List<GetSendRecCurrencyResponse> response) {
//        if (response.size() == 1) {
//            binding.sendingCurrency.setText(response.get(0).currencyShortName);
//            calTransferRequest.PayInCurrency = response.get(0).currencyShortName;
//            calTransferRequest.TransferCurrency = response.get(0).currencyShortName;
//            binding.payOutAmount.setText("");
//            setSendingCurrencyImage(response.get(0).image_URL);
//            binding.afterConvertRatesLayout.setVisibility(View.GONE);
//            binding.convertNow.setVisibility(View.VISIBLE);
//        } else {
            //show dialog
            DialogCurrency dialogCurrency = new DialogCurrency(response, this);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            dialogCurrency.show(transaction, "");
       // }
    }


    @Override
    public void onCurrencySelect(GetSendRecCurrencyResponse response) {
        if(isSendingCurrencySelected) {
            setSendingCurrencyImage(response.image_URL);
            binding.sendingCurrency.setText(response.currencyShortName);
            calTransferRequest.PayInCurrency = response.currencyShortName;
            calTransferRequest.TransferCurrency = response.currencyShortName;
        } else {
            setReceivingCurrencyImage(response.image_URL);
            binding.receivingCurrency.setText(response.currencyShortName);
            calTransferRequest.PayoutCurrency = response.currencyShortName;
        }


        binding.convertNow.setVisibility(View.VISIBLE);
        binding.payOutAmount.setText("");
        binding.afterConvertRatesLayout.setVisibility(View.GONE);
        binding.convertNow.setVisibility(View.VISIBLE);
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
