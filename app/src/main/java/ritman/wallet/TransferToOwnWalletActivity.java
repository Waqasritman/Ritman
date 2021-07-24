package ritman.wallet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

import ritman.wallet.base.RitmanBaseActivity;
import ritman.wallet.databinding.CurrencyConvertLayoutBinding;
import ritman.wallet.di.XMLdi.RequestHelper.CalTransferRequest;
import ritman.wallet.di.XMLdi.RequestHelper.GetCCYForWalletRequest;
import ritman.wallet.di.XMLdi.RequestHelper.WalletToWalletTransferRequest;
import ritman.wallet.di.XMLdi.ResponseHelper.CalTransferResponse;
import ritman.wallet.di.XMLdi.ResponseHelper.GetCountryListResponse;
import ritman.wallet.di.XMLdi.ResponseHelper.GetSendRecCurrencyResponse;
import ritman.wallet.di.XMLdi.apicaller.CheckRatesTask;
import ritman.wallet.di.XMLdi.apicaller.GetCCYForWalletTask;
import ritman.wallet.di.XMLdi.apicaller.WalletToWalletTransferTask;
import ritman.wallet.dialogs.DialogCountry;
import ritman.wallet.dialogs.DialogCurrency;
import ritman.wallet.dialogs.SingleButtonMessageDialog;
import ritman.wallet.dialogs.WalletToWalletConfirmDialog;
import ritman.wallet.interfaces.OnDecisionMade;
import ritman.wallet.interfaces.OnGetTransferRates;
import ritman.wallet.interfaces.OnResponse;
import ritman.wallet.interfaces.OnSelectCountry;
import ritman.wallet.interfaces.OnSelectCurrency;
import ritman.wallet.interfaces.OnWalletTransferConfirmation;
import ritman.wallet.utils.CheckValidation;
import ritman.wallet.utils.IsNetworkConnection;
import ritman.wallet.utils.MoneyValueFilter;
import ritman.wallet.utils.NumberFormatter;
import ritman.wallet.utils.StringHelper;
import ritman.wallet.utils.WalletTypeHelper;

import static ritman.wallet.utils.MoneyValueFilter.getDecimalFormattedString;

public class TransferToOwnWalletActivity extends RitmanBaseActivity<CurrencyConvertLayoutBinding>
        implements OnSelectCountry, OnResponse, OnDecisionMade
        , OnWalletTransferConfirmation, OnSelectCurrency, OnGetTransferRates {

    WalletToWalletTransferRequest request;
    boolean isSendingCurrency = false;
    List<GetSendRecCurrencyResponse> walletCurrency;

    CalTransferRequest calTransferRequest;
    String paymentMode = "Wallet_Transfer";

    boolean isOwnAccount = true;


    @Override
    public void onResume() {
        super.onResume();
        binding.toolbar.titleTxt
                .setText(getString(R.string.wallet_transfer));
        binding.toolbar.toolBarFinal
                .setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.toolbar.backBtn
                .setColorFilter(ContextCompat.getColor(this,
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    public boolean isCalTransferValidate() {
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

    public boolean isValidate() {
        if (!isOwnAccount) {
            if (TextUtils.isEmpty(binding.numberLayout.mobilesignupb.getText().toString())) {
                onMessage(getString(R.string.plz_select_country_code));
                return false;
            } else if (TextUtils.isEmpty(binding.numberLayout.mobilesignupb.getText().toString())) {
                onMessage(getString(R.string.enter_mobile_no_error));
                return false;
            } else if (!CheckValidation.isPhoneNumberValidate(binding.numberLayout.mobilesignupb.getText().toString()
                    , binding.numberLayout.countryCodeTextView.getText().toString())) {
                onMessage(getString(R.string.invalid_number));
                return false;
            }

        }

        if (TextUtils.isEmpty(binding.receiverName.getText().toString())) {
            onMessage(getString(R.string.recever_name_error));
            return false;
        }

        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.currency_convert_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        if (walletCurrency == null) {
            walletCurrency = new ArrayList<>();
        }

        binding.toolbar.crossBtn.setVisibility(View.GONE);
        binding.toolbar.backBtn.setOnClickListener(v -> {
            finish();
        });

        if (isOwnAccount) {
            binding.numberLayout.mobilesignupb.setVisibility(View.GONE);
            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(12);
            binding.numberLayout.mobilesignupb.setFilters(FilterArray);
            binding.numberLayout.mobilesignupb.setEnabled(false);
            binding.numberLayout.mobilesignupb.setText(sessionManager.getCustomerPhone());
            binding.receiverName.setText(sessionManager.getUserName());
            binding.receiverName.setVisibility(View.GONE);
            binding.description.setVisibility(View.GONE);
            binding.walletNameTxt.setVisibility(View.GONE);
            binding.descriptionTxt.setVisibility(View.GONE);
            binding.numberLayout.mainNumber.setVisibility(View.GONE);
            binding.moneyTv.setVisibility(View.GONE);
        }


        request = new WalletToWalletTransferRequest();
        calTransferRequest = new CalTransferRequest();

        binding.convertNow.setOnClickListener(v -> {
            if (isCalTransferValidate()) {
                calTransferRequest.PayoutCurrency = binding.receivingCurrency.getText().toString(); // set receiving currency
                calTransferRequest.PayInCurrency = binding.sendingCurrency.getText().toString();
                calTransferRequest.TransferCurrency = binding.sendingCurrency.getText().toString();
                calTransferRequest.PaymentMode = paymentMode;
                calTransferRequest.TransferAmount = Double.parseDouble(
                        NumberFormatter.removeCommas(binding.sendingAmountField
                                .getText().toString()));
                calTransferRequest.languageId = sessionManager.getlanguageselection();
                if (IsNetworkConnection.checkNetworkConnection(this)) {
                    CheckRatesTask task = new CheckRatesTask(this, this);
                    task.execute(calTransferRequest);
                } else {
                    onMessage(getString(R.string.no_internet));
                }

            }

        });

        binding.sendingCurrencyLayout.setOnClickListener(v -> {
            isSendingCurrency = true;
          //  if (walletCurrency.isEmpty()) {
                if (IsNetworkConnection.checkNetworkConnection(this)) {
//                    GetWalletCurrencyListRequest request = new GetWalletCurrencyListRequest();
//                    request.languageId = sessionManager.getlanguageselection();
//                    GetWalletCurrencyListTask getWalletCurrencyListTask = new GetWalletCurrencyListTask(this
//                            , this);
//                    getWalletCurrencyListTask.execute(request);

                    showWallets();
                } else {
                    onMessage(getString(R.string.no_internet));
                }
           // } else {
             //   showCurrencyDialog(walletCurrency);
           // }
        });

        binding.receivngCurrencyLayout.setOnClickListener(v -> {
            isSendingCurrency = false;
         //   if (walletCurrency.isEmpty()) {
                if (IsNetworkConnection.checkNetworkConnection(this)) {
//                    GetWalletCurrencyListRequest request = new GetWalletCurrencyListRequest();
//                    GetWalletCurrencyListTask getWalletCurrencyListTask = new GetWalletCurrencyListTask(this
//                            , this);
//                    getWalletCurrencyListTask.execute(request);

                    showWallets();
                } else {
                    onMessage(getString(R.string.no_internet));
                }
           // } else {
             //   showCurrencyDialog(walletCurrency);
           // }
        });

        binding.numberLayout.mobilesignupb.setOnClickListener(v -> {
            if (IsNetworkConnection.checkNetworkConnection(this)) {
                DialogCountry dialogCountry = new DialogCountry(this::onSelectCountry);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                dialogCountry.show(transaction, "");
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
                    binding.mainLayout.setVisibility(View.GONE);
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

        binding.sendNowBtn.setOnClickListener(v -> {
            if (isValidate()) {
                if (sessionManager.getISKYCApproved()) {


                    if (!isOwnAccount) {
                        request.receiptMobileNo = StringHelper.parseNumber(binding.numberLayout.countryCodeTextView.getText().toString()
                                + binding.numberLayout.mobilesignupb.getText().toString());
                    } else {
                        request.receiptMobileNo = StringHelper.parseNumber(binding.numberLayout.mobilesignupb.getText().toString());
                    }


                    request.customerNo = sessionManager.getCustomerNo();
                    request.description = binding.description.getText().toString();
                    request.transferAmount = NumberFormatter.removeCommas(
                            binding.sendingAmountField.getText().toString());
                    request.receiptCurrency = binding.receivingCurrency.getText().toString();
                    request.payInCurrency = binding.sendingCurrency.getText().toString();
                    request.ipAddress = sessionManager.getIpAddress();
                    request.ipCountryName = sessionManager.getIpCountryName();


                    WalletToWalletConfirmDialog dialog = new WalletToWalletConfirmDialog(
                            request, binding.receiverName.getText().toString(), this
                    );
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    dialog.show(transaction, "");


                } else {
                    onMessage(getString(R.string.plz_complete_kyc));
                }
            }
        });
    }




    public void showWallets() {
        GetCCYForWalletRequest request = new GetCCYForWalletRequest();
        request.actionType = WalletTypeHelper.RECEIVE;
        request.customerNo = sessionManager.getCustomerNo();
        request.languageID = sessionManager.getlanguageselection();


        GetCCYForWalletTask task = new GetCCYForWalletTask(this, this);
        task.execute(request);
    }

    @Override
    public void onSelectCountry(GetCountryListResponse country) {
        binding.numberLayout.countryCodeTextView.setText(country.countryCode);
        setCountryFlag(country.imageURL);
    }

    @Override
    public void onSuccess() {
        sessionManager.putWalletNeedToUpdate(true);
        SingleButtonMessageDialog dialog = new SingleButtonMessageDialog(getString(R.string.successfully_tranfared)
                , getString(R.string.wallet_traansaction_success), this,
                false);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }

    @Override
    public void onProceed() {
        finish();
    }

    @Override
    public void onCancel() {
        finish();
    }

    @Override
    public void onConfirmed() {
        getPin();
    }


    @Override
    public void onCurrencyResponse(List<GetSendRecCurrencyResponse> response) {
//        if (response.size() == 1) {
//            setSendingCurrencyImage(response.get(0).image_URL);
//            binding.sendingCurrency.setText(response.get(0).currencyShortName);
//        } else {
            showCurrencyDialog(response);
        //}
    }

    void showCurrencyDialog(List<GetSendRecCurrencyResponse> response) {
        DialogCurrency dialogCurrency = new DialogCurrency(response, this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        dialogCurrency.show(transaction, "");
    }

    @Override
    public void onCurrencySelect(GetSendRecCurrencyResponse response) {
        if (isSendingCurrency) {
            binding.sendingCurrency.setText(response.currencyShortName);
            binding.payOutAmount.setText("");
            setSendingCurrencyImage(response.image_URL);
        } else {
            binding.receivingCurrency.setText(response.currencyShortName);
            binding.payOutAmount.setText("");
            setReceivingCurrencyImage(response.image_URL);
        }

        binding.mainLayout.setVisibility(View.GONE);
        binding.convertNow.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGetTransferRates(CalTransferResponse response) {
        showRates(response);
    }

    public void showRates(CalTransferResponse response) {
        binding.payOutAmount.setText(NumberFormatter.decimal(response.payoutAmount.floatValue()));
        binding.commissionAmountTxt.setText(String.valueOf(
                NumberFormatter.decimal(response.commission.floatValue())));
        binding.mainLayout.setVisibility(View.VISIBLE);
        binding.convertNow.setVisibility(View.GONE);
    }


    @Override
    public void onVerifiedPin() {
        loadWallet();
    }

    public void loadWallet() {
        if (IsNetworkConnection.checkNetworkConnection(this)) {
            request.languageId = sessionManager.getlanguageselection();
            WalletToWalletTransferTask task = new WalletToWalletTransferTask(this, this);
            task.execute(request);
        } else {
            onMessage(getString(R.string.no_internet));
        }
    }

    public void setCountryFlag(String url) {
        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource,
                                                @Nullable Transition<? super Bitmap> transition) {
                        binding.numberLayout.imageIcon.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
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