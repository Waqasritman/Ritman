package totipay.wallet.SendMoney;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import totipay.wallet.MoneyTransferModuleV.MoneyTransferMainLayout;
import totipay.wallet.R;
import totipay.wallet.databinding.ActivityRequestMoneyBinding;
import totipay.wallet.di.RequestHelper.RequestMoney;
import totipay.wallet.di.ResponseHelper.GetCountryListResponse;
import totipay.wallet.di.apicaller.RequestMoneyTask;
import totipay.wallet.dialogs.AlertDialog;
import totipay.wallet.dialogs.DialogCountry;
import totipay.wallet.fragments.BaseFragment;
import totipay.wallet.interfaces.OnDecisionMade;
import totipay.wallet.interfaces.OnSelectCountry;
import totipay.wallet.interfaces.OnSuccessMessage;
import totipay.wallet.utils.CheckValidation;
import totipay.wallet.utils.IsNetworkConnection;
import totipay.wallet.utils.MoneyValueFilter;
import totipay.wallet.utils.NumberFormatter;
import totipay.wallet.utils.StringHelper;

import static totipay.wallet.utils.MoneyValueFilter.getDecimalFormattedString;

public class RequestMoneyActivity extends BaseFragment<ActivityRequestMoneyBinding>
        implements OnSelectCountry, OnSuccessMessage, OnDecisionMade {


    @Override
    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.numberLayout.mobilesignupb.getText().toString())) {
            onMessage(getString(R.string.plz_select_country_code));
            return false;
        } else if (TextUtils.isEmpty(binding.numberLayout.mobilesignupb.getText().toString())) {
            onMessage(getString(R.string.enter_mobile_no_error));
            return false;
        } else if (TextUtils.isEmpty(binding.sendingAmountField.getText().toString())) {
            onMessage(getString(R.string.please_enter_amount));
            return false;
        } else if (!CheckValidation.isValidPhone(binding.numberLayout.countryCodeTextView.getText().toString()
                + binding.numberLayout.mobilesignupb.getText().toString())) {
            onMessage(getString(R.string.invalid_number));
            return false;
        }
        return true;
    }


    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.numberLayout.countrySpinnerSignIn.setOnClickListener(v -> {
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                DialogCountry dialogCountry = new DialogCountry(this::onSelectCountry);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
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
                String userNumber = StringHelper.parseNumber(binding.numberLayout.countryCodeTextView.getText().toString()
                        + binding.numberLayout.mobilesignupb.getText().toString());
                RequestMoney request = new RequestMoney();
                request.mobileNo = userNumber;
                request.customerNo = ((MoneyTransferMainLayout) getBaseActivity()).sessionManager.getCustomerNo();
                request.description = binding.description.getText().toString();
                request.amount = Double.parseDouble(NumberFormatter.removeCommas(binding.sendingAmountField.
                        getText().toString()));
                request.languageId = getSessionManager().getlanguageselection();
                if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                    RequestMoneyTask task = new RequestMoneyTask(getContext(), this);
                    task.execute(request);
                } else {
                    onMessage(getString(R.string.no_internet));
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_request_money;
    }


    @Override
    public void onSelectCountry(GetCountryListResponse country) {
        binding.numberLayout.countryCodeTextView.setText(country.countryCode);
        setCountryFlag(country.imageURL);
    }

    @Override
    public void onSuccess(String s) {
        AlertDialog alertDialog = new AlertDialog(getString(R.string.request_money)
                , getString(R.string.request_money_message), this);
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        alertDialog.show(fragmentTransaction, "");
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }

    @Override
    public void onProceed() {
        getBaseActivity().finish();
    }

    @Override
    public void onCancel() {
        getBaseActivity().finish();
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
