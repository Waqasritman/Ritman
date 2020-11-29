package totipay.wallet.prepaid_cards_module.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import totipay.wallet.R;
import totipay.wallet.databinding.FragmentLoadPrepaidCardBinding;
import totipay.wallet.di.RequestHelper.LoadVirtualCardRequest;
import totipay.wallet.di.apicaller.LoadPrepaidCardTask;
import totipay.wallet.fragments.BaseFragment;
import totipay.wallet.interfaces.OnSuccessMessage;
import totipay.wallet.utils.IsNetworkConnection;
import totipay.wallet.utils.MoneyValueFilter;

import static totipay.wallet.utils.MoneyValueFilter.getDecimalFormattedString;

public class LoadPrepaidCardFragment extends BaseFragment<FragmentLoadPrepaidCardBinding>
        implements OnSuccessMessage {


    @Override
    protected void injectView() {

    }


    @Override
    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.sendingAmount.getText().toString())) {
            onMessage(getString(R.string.enter_the_amount));
            return false;
        }
        return true;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.sendingAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                } else {
                    resetQuickPicViews();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                int cursorPosition = binding.sendingAmount.getSelectionEnd();
                String originalStr = binding.sendingAmount.getText().toString();

                //To restrict only two digits after decimal place
                binding.sendingAmount.setFilters(new InputFilter[]{new MoneyValueFilter(2)});

                try {
                    binding.sendingAmount.removeTextChangedListener(this);
                    String value = binding.sendingAmount.getText().toString();

                    if (value != null && !value.equals("")) {
                        if (value.startsWith(".")) {
                            binding.sendingAmount.setText("0.");
                        }
                        if (value.startsWith("0") && !value.startsWith("0.")) {
                            binding.sendingAmount.setText("");
                        }
                        String str = binding.sendingAmount.getText().toString().replaceAll(",", "");
                        if (!value.equals(""))
                            binding.sendingAmount.setText(getDecimalFormattedString(str));

                        int diff = binding.sendingAmount.getText().toString().length() - originalStr.length();
                        binding.sendingAmount.setSelection(cursorPosition + diff);
                    }
                    binding.sendingAmount.addTextChangedListener(this);
                } catch (Exception ex) {
                    Log.e("Text watcher", ex.getMessage());
                    binding.sendingAmount.addTextChangedListener(this);
                }
            }
        });


        binding.amount100.setOnClickListener(v -> {
            onClickView(binding.amount100.getId());
        });

        binding.amount200.setOnClickListener(v -> {
            onClickView(binding.amount200.getId());

        });

        binding.amount300.setOnClickListener(v -> {
            onClickView(binding.amount300.getId());

        });

        binding.amount400.setOnClickListener(v -> {
            onClickView(binding.amount400.getId());

        });

        binding.amount500.setOnClickListener(v -> {
            onClickView(binding.amount500.getId());
        });


        binding.generate.setOnClickListener(v -> {

            if (isValidate()) {
                if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                    LoadVirtualCardRequest request = new LoadVirtualCardRequest();
                    request.loadAmount = binding.sendingAmount.getText().toString();
                    request.virtualCardNo = getSessionManager().getVirtualCardNo();
                    request.customerNo = getSessionManager().getCustomerNo();
                    request.languageID = getSessionManager().getlanguageselection();


                    LoadPrepaidCardTask task = new LoadPrepaidCardTask(getContext()
                            , this);
                    task.execute(request);

                } else {
                    onMessage(getString(R.string.no_internet));
                }
            }


        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_load_prepaid_card;
    }

    /**
     * method will change the views with respect to id
     * selected id become active and other become in active
     *
     * @param id
     */
    public void onClickView(int id) {
        binding.amount100.setBackground(getResources().getDrawable(R.drawable.circle_blue_sky_quick_amount));
        binding.amount200.setBackground(getResources().getDrawable(R.drawable.circle_blue_sky_quick_amount));
        binding.amount300.setBackground(getResources().getDrawable(R.drawable.circle_blue_sky_quick_amount));
        binding.amount400.setBackground(getResources().getDrawable(R.drawable.circle_blue_sky_quick_amount));
        binding.amount500.setBackground(getResources().getDrawable(R.drawable.circle_blue_sky_quick_amount));

        binding.amount100.setTextColor(getResources().getColor(R.color.colorBlack));
        binding.amount200.setTextColor(getResources().getColor(R.color.colorBlack));
        binding.amount300.setTextColor(getResources().getColor(R.color.colorBlack));
        binding.amount400.setTextColor(getResources().getColor(R.color.colorBlack));
        binding.amount500.setTextColor(getResources().getColor(R.color.colorBlack));

        if (id == binding.amount100.getId()) {
            binding.amount100.setBackground(getResources().getDrawable(R.drawable.circle_skyblue_two));
            binding.amount100.setTextColor(getResources().getColor(R.color.black));
            binding.sendingAmount.setText(getString(R.string.one_hundred));
            binding.sendingAmount.requestFocus();
        } else if (id == binding.amount200.getId()) {
            binding.amount200.setBackground(getResources().getDrawable(R.drawable.circle_skyblue_two));
            binding.amount200.setTextColor(getResources().getColor(R.color.black));
            binding.sendingAmount.setText(getString(R.string.two_hundred));
            binding.sendingAmount.requestFocus();
        } else if (id == binding.amount300.getId()) {
            binding.amount300.setBackground(getResources().getDrawable(R.drawable.circle_skyblue_two));
            binding.amount300.setTextColor(getResources().getColor(R.color.black));
            binding.sendingAmount.setText(getString(R.string.three_hundred));
            binding.sendingAmount.requestFocus();
        } else if (id == binding.amount400.getId()) {
            binding.amount400.setBackground(getResources().getDrawable(R.drawable.circle_skyblue_two));
            binding.amount400.setTextColor(getResources().getColor(R.color.black));
            binding.sendingAmount.setText(getString(R.string.four_hundred));
            binding.sendingAmount.requestFocus();
        } else if (id == binding.amount500.getId()) {
            binding.amount500.setBackground(getResources().getDrawable(R.drawable.circle_skyblue_two));
            binding.amount500.setTextColor(getResources().getColor(R.color.black));
            binding.sendingAmount.setText(getString(R.string.five_hundred));
            binding.sendingAmount.requestFocus();
        }

        binding.sendingAmount.setSelection(binding.sendingAmount.getText().length());
    }


    /**
     * method will reset the views to non select
     */
    public void resetQuickPicViews() {
        binding.amount500.setBackground(getResources().getDrawable(R.drawable.circle_blue_sky_quick_amount));
        binding.amount200.setBackground(getResources().getDrawable(R.drawable.circle_blue_sky_quick_amount));
        binding.amount300.setBackground(getResources().getDrawable(R.drawable.circle_blue_sky_quick_amount));
        binding.amount400.setBackground(getResources().getDrawable(R.drawable.circle_blue_sky_quick_amount));
        binding.amount500.setBackground(getResources().getDrawable(R.drawable.circle_blue_sky_quick_amount));

        binding.amount500.setTextColor(getResources().getColor(R.color.colorBlack));
        binding.amount200.setTextColor(getResources().getColor(R.color.colorBlack));
        binding.amount300.setTextColor(getResources().getColor(R.color.colorBlack));
        binding.amount400.setTextColor(getResources().getColor(R.color.colorBlack));
        binding.amount500.setTextColor(getResources().getColor(R.color.colorBlack));
    }

    @Override
    public void onSuccess(String s) {
        onMessage(s);
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }
}