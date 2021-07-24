package ritman.wallet.MoneyTransferModuleV.banktransfer;

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

import ritman.wallet.MoneyTransferModuleV.MoneyTransferMainLayout;
import ritman.wallet.MoneyTransferModuleV.TransactionReceiptActivity;
import ritman.wallet.MoneyTransferModuleV.viewmodels.BankTransferViewModel;
import ritman.wallet.beneficairyRegistration.BeneficiaryRegistrationActivity;
import ritman.wallet.databinding.ActivityCashPaymentThirdBinding;
import ritman.wallet.di.JSONdi.Status;
import ritman.wallet.di.JSONdi.restRequest.CalTransferRequest;
import ritman.wallet.di.JSONdi.restRequest.TransactionRecieptRequest;
import ritman.wallet.di.JSONdi.restResponse.CaltransferResponse;
import ritman.wallet.di.XMLdi.RequestHelper.GetSendRecCurrencyRequest;
import ritman.wallet.di.XMLdi.RequestHelper.GetWalletCurrencyListRequest;
import ritman.wallet.di.XMLdi.RequestHelper.TootiPayRequest;
import ritman.wallet.di.XMLdi.ResponseHelper.CalTransferResponse;
import ritman.wallet.di.XMLdi.ResponseHelper.GetBeneficiaryListResponse;
import ritman.wallet.di.XMLdi.ResponseHelper.GetCountryListResponse;
import ritman.wallet.di.XMLdi.ResponseHelper.GetSendRecCurrencyResponse;
import ritman.wallet.di.XMLdi.ResponseHelper.GetSourceOfIncomeListResponse;
import ritman.wallet.di.XMLdi.ResponseHelper.PurposeOfTransferListResponse;
import ritman.wallet.di.XMLdi.apicaller.CheckRatesTask;
import ritman.wallet.di.XMLdi.apicaller.GetSendRecCurrencyTask;
import ritman.wallet.di.XMLdi.apicaller.GetWalletCurrencyListTask;
import ritman.wallet.dialogs.DialogCurrency;
import ritman.wallet.dialogs.DialogSourceOfIncome;
import ritman.wallet.dialogs.DialogTransferPurpose;
import ritman.wallet.fragments.BaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

import ritman.wallet.R;
import ritman.wallet.interfaces.OnGetTransferRates;
import ritman.wallet.interfaces.OnSelectCountry;
import ritman.wallet.interfaces.OnSelectCurrency;
import ritman.wallet.interfaces.OnSelectSourceOfIncome;
import ritman.wallet.interfaces.OnSelectTransferPurpose;
import ritman.wallet.utils.IsNetworkConnection;
import ritman.wallet.utils.MoneyValueFilter;
import ritman.wallet.utils.NumberFormatter;

import static ritman.wallet.utils.MoneyValueFilter.getDecimalFormattedString;

public class SendMoneyViaBankThirdActivity extends BaseFragment<ActivityCashPaymentThirdBinding>
        implements OnSelectTransferPurpose, OnSelectCountry, OnSelectCurrency, OnGetTransferRates
        , OnSelectSourceOfIncome {
    BankTransferViewModel viewModel;
    Integer soucreOfIncome;
    boolean showingBreakPoint = false;

    GetBeneficiaryListResponse benedetails;
    String customerNo;
    Double PayInAmount;

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
        if(getBaseActivity() instanceof BeneficiaryRegistrationActivity) {

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

                CalTransferRequest request = new CalTransferRequest();
                request.PayInCurrency = "INR";
                request.PayoutCurrency = "INR";
                request.PaymentMode = "BANK";
                request.TransferCurrency = "INR";
                request.TransferAmount = binding.sendingAmountField.getText().toString();


                viewModel.getCalTransfer(request ,getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()
                        , response -> {
                            if (response.status == Status.ERROR) {
                                onMessage(getString(response.messageResourceId));
                            } else {
                                assert response.resource != null;
                                if (response.resource.responseCode.equals(101)) {
                                    onMessage(response.resource.description);
                                    showRates(response.resource.data);
                                    PayInAmount=  response.resource.data.getPayInAmount();
                                } else {
                                    onMessage(response.resource.description);
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


    public void goToNext() {
        Bundle bundle = new Bundle();
        bundle.putString("total_payable", binding.totalPayableAmount.getText().toString());
        bundle.putBoolean("is_from_cash", false);
        bundle.putParcelable("bene" , benedetails);
        bundle.putDouble("PayInAmount",PayInAmount);
        Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_sendMoneyViaBankThirdActivity_to_cashTransferSummaryFragment2
                        , bundle);
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
