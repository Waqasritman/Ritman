package totipay.wallet.payviaqrcode;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

import totipay.wallet.MoneyTransferModuleV.MoneyTransferMainLayout;
import totipay.wallet.R;
import totipay.wallet.databinding.ActivityWalletTransferBinding;
import totipay.wallet.di.RequestHelper.CalTransferRequest;
import totipay.wallet.di.RequestHelper.GetCustomerProfileRequest;
import totipay.wallet.di.RequestHelper.GetWalletCurrencyListRequest;
import totipay.wallet.di.RequestHelper.WalletToWalletTransferRequest;
import totipay.wallet.di.ResponseHelper.CalTransferResponse;
import totipay.wallet.di.ResponseHelper.CustomerProfile;
import totipay.wallet.di.ResponseHelper.GetSendRecCurrencyResponse;
import totipay.wallet.di.apicaller.CheckRatesTask;
import totipay.wallet.di.apicaller.GetCustomerProfileTask;
import totipay.wallet.di.apicaller.GetWalletCurrencyListTask;
import totipay.wallet.di.apicaller.WalletToWalletTransferTask;
import totipay.wallet.dialogs.AlertDialog;
import totipay.wallet.dialogs.DialogCurrency;
import totipay.wallet.dialogs.SingleButtonMessageDialog;
import totipay.wallet.dialogs.WalletToWalletConfirmDialog;
import totipay.wallet.fragments.BaseFragment;
import totipay.wallet.home_module.WalletViaQRCodeActivity;
import totipay.wallet.interfaces.OnDecisionMade;
import totipay.wallet.interfaces.OnGetCustomerProfile;
import totipay.wallet.interfaces.OnGetTransferRates;
import totipay.wallet.interfaces.OnResponse;
import totipay.wallet.interfaces.OnSelectCurrency;
import totipay.wallet.interfaces.OnWalletTransferConfirmation;
import totipay.wallet.utils.CheckValidation;
import totipay.wallet.utils.IsNetworkConnection;
import totipay.wallet.utils.NumberFormatter;

public class WalletToWalletViaQrCode extends BaseFragment<ActivityWalletTransferBinding>
        implements OnResponse, OnDecisionMade, OnGetCustomerProfile
        , OnWalletTransferConfirmation, OnSelectCurrency, OnGetTransferRates {


    boolean isSendingCurrency = false;
    List<GetSendRecCurrencyResponse> walletCurrency;
    WalletToWalletTransferRequest request;
    CalTransferRequest calTransferRequest;
    String paymentMode = "Wallet_Transfer";
    String mobile_no = "";

    @Override
    public void onResume() {
        super.onResume();
        ((WalletViaQRCodeActivity) getActivity())
                .changeTitle(getString(R.string.wallet_to_wallet));
    }

    String customerNo;

    @Override
    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.numberLayout.mobilesignupb.getText().toString())) {
            onMessage(getString(R.string.enter_mobile_no_error));
            return false;
        } else if (TextUtils.isEmpty(binding.receiverName.getText().toString())) {
            onMessage(getString(R.string.recever_name_error));
            return false;
        } else if (TextUtils.isEmpty(binding.sendingAmountField.getText().toString())) {
            onMessage(getString(R.string.please_enter_amount));
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
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        customerNo = getArguments().getString("customer_no", "");

        if (walletCurrency == null) {
            walletCurrency = new ArrayList<>();
        }

        request = new WalletToWalletTransferRequest();
        calTransferRequest = new CalTransferRequest();
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(12);
        binding.numberLayout.mobilesignupb.setFilters(FilterArray);
        binding.numberLayout.mobilesignupb.setEnabled(false);
        binding.numberLayout.countrySpinnerSignIn.setVisibility(View.GONE);
        binding.receiverName.setEnabled(false);
        if (!customerNo.isEmpty()) {
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                GetCustomerProfileRequest request = new GetCustomerProfileRequest();
                request.customerNo = customerNo;
                request.languageId = getSessionManager().getlanguageselection();
                GetCustomerProfileTask task = new GetCustomerProfileTask(getContext(), this);
                task.execute(request);
            } else {
                onMessage(getString(R.string.no_internet));
            }
        }


        binding.convertNow.setOnClickListener(v -> {
            if (isCalTransferValidate()) {
                calTransferRequest.PayoutCurrency = binding.receivingCurrency.getText().toString(); // set receiving currency
                calTransferRequest.PayInCurrency = binding.sendingCurrency.getText().toString();
                calTransferRequest.TransferCurrency = binding.sendingCurrency.getText().toString();
                calTransferRequest.PaymentMode = paymentMode;
                calTransferRequest.TransferAmount = Double.parseDouble(
                        NumberFormatter.removeCommas(binding.sendingAmountField
                                .getText().toString()));
                calTransferRequest.languageId = getSessionManager().getlanguageselection();
                if (IsNetworkConnection.checkNetworkConnection(getActivity())) {
                    CheckRatesTask task = new CheckRatesTask(getActivity(), this);
                    task.execute(calTransferRequest);
                } else {
                    onMessage(getString(R.string.no_internet));
                }

            }

        });


        binding.sendingCurrencyLayout.setOnClickListener(v -> {
            isSendingCurrency = true;
            if (walletCurrency.isEmpty()) {
                if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                    GetWalletCurrencyListRequest request = new GetWalletCurrencyListRequest();
                    request.languageId = getSessionManager().getlanguageselection();
                    GetWalletCurrencyListTask getWalletCurrencyListTask = new GetWalletCurrencyListTask(getContext()
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
                if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                    GetWalletCurrencyListRequest request = new GetWalletCurrencyListRequest();
                    GetWalletCurrencyListTask getWalletCurrencyListTask = new GetWalletCurrencyListTask(getContext()
                            , this);
                    getWalletCurrencyListTask.execute(request);
                } else {
                    onMessage(getString(R.string.no_internet));
                }
            } else {
                showCurrencyDialog(walletCurrency);
            }
        });


        binding.sendNowBtn.setOnClickListener(v -> {
            if (isValidate()) {

                if (getSessionManager().getISKYCApproved()) {
                    request.receiptMobileNo = mobile_no;
                    request.customerNo = getSessionManager().getCustomerNo();
                    request.description = binding.description.getText().toString();
                    request.transferAmount = NumberFormatter.removeCommas(
                            binding.sendingAmountField.getText().toString());
                    request.receiptCurrency = binding.receivingCurrency.getText().toString();
                    request.payInCurrency = binding.sendingCurrency.getText().toString();

                    WalletToWalletConfirmDialog dialog = new WalletToWalletConfirmDialog(
                            request, binding.receiverName.getText().toString(), this
                    );
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    dialog.show(transaction, "");
                } else {
                    onMessage(getString(R.string.plz_complete_kyc));
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet_transfer;
    }

    @Override
    public void onSuccess() {
        getSessionManager().putWalletNeedToUpdate(true);
        SingleButtonMessageDialog dialog = new SingleButtonMessageDialog(getString(R.string.successfully_tranfared)
                , getString(R.string.wallet_traansaction_success), this,
                false);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }

    @Override
    public void onProceed() {
        getActivity().finish();
    }

    @Override
    public void onCancel() {
        getActivity().finish();
    }

    @Override
    public void onConfirmed() {
        getPin();
    }

    @Override
    public void onGetCustomerProfile(CustomerProfile customerProfile) {
        binding.receiverName.setText(customerProfile.firstName.concat(" ").concat(customerProfile.lastName));
        binding.numberLayout.mobilesignupb.setText(customerProfile.mobileNo);
        mobile_no = customerProfile.mobileNo;
    }


    @Override
    public void onVerifiedPin() {
        loadWallet();
    }

    void showCurrencyDialog(List<GetSendRecCurrencyResponse> response) {
        DialogCurrency dialogCurrency = new DialogCurrency(response, this);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        dialogCurrency.show(transaction, "");
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
            binding.payOutAmount.setText("");
            setSendingCurrencyImage(response.image_URL);
        } else {
            binding.receivingCurrency.setText(response.currencyShortName);
            setReceivingCurrencyImage(response.image_URL);
            binding.payOutAmount.setText("");
        }

        binding.mainLayout.setVisibility(View.GONE);
        binding.convertNow.setVisibility(View.VISIBLE);
    }

    public void loadWallet() {
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            request.languageId = getSessionManager().getlanguageselection();
            WalletToWalletTransferTask task = new WalletToWalletTransferTask(getContext(), this);
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

    public void setCountryFlag(String url) {
        Glide.with(this)
                .asBitmap()
                .load(url)
                .placeholder(R.drawable.ic_united_kingdom)
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
}
