package totipay.wallet;


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

import java.util.ArrayList;
import java.util.List;

import totipay.wallet.base.TootiBaseActivity;
import totipay.wallet.databinding.ActivityRepeatWalletTransactionBinding;
import totipay.wallet.di.RequestHelper.CalTransferRequest;
import totipay.wallet.di.RequestHelper.GetWalletCurrencyListRequest;
import totipay.wallet.di.RequestHelper.WalletToWalletTransferRequest;
import totipay.wallet.di.ResponseHelper.CalTransferResponse;
import totipay.wallet.di.ResponseHelper.GetSendRecCurrencyResponse;
import totipay.wallet.di.apicaller.CheckRatesTask;
import totipay.wallet.di.apicaller.GetWalletCurrencyListTask;
import totipay.wallet.di.apicaller.WalletToWalletTransferTask;
import totipay.wallet.dialogs.DialogCurrency;
import totipay.wallet.dialogs.SingleButtonMessageDialog;
import totipay.wallet.dialogs.WalletToWalletConfirmDialog;
import totipay.wallet.interfaces.OnDecisionMade;
import totipay.wallet.interfaces.OnGetTransferRates;
import totipay.wallet.interfaces.OnResponse;
import totipay.wallet.interfaces.OnSelectCurrency;
import totipay.wallet.interfaces.OnWalletTransferConfirmation;
import totipay.wallet.utils.IsNetworkConnection;
import totipay.wallet.utils.MoneyValueFilter;
import totipay.wallet.utils.NumberFormatter;
import totipay.wallet.utils.StringHelper;

import static totipay.wallet.utils.MoneyValueFilter.getDecimalFormattedString;

public class RepeatWalletTransactionActivity extends TootiBaseActivity<ActivityRepeatWalletTransactionBinding>
        implements OnResponse, OnDecisionMade
        , OnWalletTransferConfirmation,
        OnSelectCurrency, OnGetTransferRates {

    WalletToWalletTransferRequest request;
    boolean isSendingCurrency = false;
    List<GetSendRecCurrencyResponse> walletCurrency;

    CalTransferRequest calTransferRequest;
    String paymentMode = "Wallet_Transfer";
    String customerNo;

    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.mobilesignupb.getText().toString())) {
            onMessage(getString(R.string.enter_mobile_no_error));
            return false;
        } else if (TextUtils.isEmpty(binding.receiverName.getText().toString())) {
            onMessage(getString(R.string.recever_name_error));
            return false;
        } else if (TextUtils.isEmpty(binding.sendingAmountField.getText().toString())) {
            onMessage(getString(R.string.please_enter_amount));
            return false;
        } else if (TextUtils.isEmpty(binding.description.getText().toString())) {
            onMessage(getString(R.string.plz_enter_description));
            return false;
        }
        return true;
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

    @Override
    public int getLayoutId() {
        return R.layout.activity_repeat_wallet_transaction;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        customerNo = getIntent().getStringExtra("customer_no");
        String receiverName = getIntent().getStringExtra("receiver_name");
        binding.countrySpinnerSignIn.setVisibility(View.GONE);
      //  binding.numberLayout.countrySpinnerSignIn.setVisibility(View.GONE);
        binding.mobilesignupb.setText(customerNo);
        binding.receiverName.setText(receiverName);

        binding.mobilesignupb.setEnabled(false);
        binding.receiverName.setEnabled(false);

        if (walletCurrency == null) {
            walletCurrency = new ArrayList<>();
        }

        binding.toolbar.backBtn
                .setColorFilter(ContextCompat.getColor(this,
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);
        binding.toolbar.toolBarFinal.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        binding.toolbar.titleTxt.setText(getString(R.string.wallet_transfer));
        binding.toolbar.crossBtn.setVisibility(View.GONE);
        binding.toolbar.crossBtn.setOnClickListener(v -> {
            onClose();
        });

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
            if (walletCurrency.isEmpty()) {
                if (IsNetworkConnection.checkNetworkConnection(this)) {
                    GetWalletCurrencyListRequest request = new GetWalletCurrencyListRequest();
                    GetWalletCurrencyListTask getWalletCurrencyListTask = new GetWalletCurrencyListTask(this
                            , this);
                    getWalletCurrencyListTask.execute(request);
                } else {
                    onMessage(getString(R.string.no_internet));
                }
            } else {
                showCurrencyDialog(walletCurrency);
            }
        });

        binding.receivngCurrencyLayout.setOnClickListener(v -> {
            isSendingCurrency = false;
            if (walletCurrency.isEmpty()) {
                if (IsNetworkConnection.checkNetworkConnection(this)) {
                    GetWalletCurrencyListRequest request = new GetWalletCurrencyListRequest();
                    request.languageId = sessionManager.getlanguageselection();
                    GetWalletCurrencyListTask getWalletCurrencyListTask = new GetWalletCurrencyListTask(this
                            , this);
                    getWalletCurrencyListTask.execute(request);
                } else {
                    onMessage(getString(R.string.no_internet));
                }
            } else {
                showCurrencyDialog(walletCurrency);
            }
        });


        binding.toolbar.backBtn.setOnClickListener(v -> {
            finish();
        });

        binding.sendingAmountField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

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
                    request.receiptMobileNo = binding.mobilesignupb.getText().toString();
                    request.customerNo = sessionManager.getCustomerNo();
                    request.description = binding.description.getText().toString();
                    request.transferAmount = NumberFormatter.removeCommas(
                            binding.sendingAmountField.getText().toString());
                    request.receiptCurrency = binding.receivingCurrency.getText().toString();
                    request.payInCurrency = binding.sendingCurrency.getText().toString();

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

    void showCurrencyDialog(List<GetSendRecCurrencyResponse> response) {
        DialogCurrency dialogCurrency = new DialogCurrency(response, this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        dialogCurrency.show(transaction, "");
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
    public void onCurrencyResponse(List<GetSendRecCurrencyResponse> response) {
        if (response.size() == 1) {
            binding.sendingCurrency.setText(response.get(0).currencyShortName);
            setSendingCurrencyImage(response.get(0).image_URL);
        } else {
            showCurrencyDialog(response);
        }
    }

    @Override
    public void onCurrencySelect(GetSendRecCurrencyResponse response) {
        if (isSendingCurrency) {
            binding.sendingCurrency.setText(response.currencyShortName);
            setSendingCurrencyImage(response.image_URL);
        } else {
            binding.receivingCurrency.setText(response.currencyShortName);
            setReceivingCurrencyImage(response.image_URL);
        }
    }

    @Override
    public void onConfirmed() {
        getPin();
    }

    @Override
    public void onVerifiedPin() {
        loadWallet();
    }

    public void loadWallet() {
        if (IsNetworkConnection.checkNetworkConnection(this)) {
            request.languageId = sessionManager.getlanguageselection();
            WalletToWalletTransferTask task = new WalletToWalletTransferTask(this
                    , this);
            task.execute(request);
        } else {
            onMessage(getString(R.string.no_internet));
        }
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