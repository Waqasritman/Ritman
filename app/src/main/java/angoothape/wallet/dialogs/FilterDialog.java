package angoothape.wallet.dialogs;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.Locale;

import angoothape.wallet.R;
import angoothape.wallet.base.BaseDialogFragment;
import angoothape.wallet.databinding.FilterDialogDesignBinding;
import angoothape.wallet.di.JSONdi.restRequest.TransactionHistoryRequest;
import angoothape.wallet.interfaces.OnApplyFilterInterface;
import angoothape.wallet.utils.DateAndTime;

public class FilterDialog extends BaseDialogFragment<FilterDialogDesignBinding> implements DatePickerDialog.OnDateSetListener {

    TransactionHistoryRequest request;
    OnApplyFilterInterface onApplyFilterInterface;

    boolean isStartDate;
    boolean isSelectedSome = false;


    Integer[] codeInputs;
    Integer[] codeInputsText;

    public FilterDialog(OnApplyFilterInterface onApplyFilterInterface) {
        this.onApplyFilterInterface = onApplyFilterInterface;
    }

    @Override
    public int getLayoutId() {
        return R.layout.filter_dialog_design;
    }


    public boolean isValidate() {
        if (binding.fromDate.getText().toString().isEmpty()) {
            onMessage(getString(R.string.error_selct_from_date));
            return false;
        } else if (binding.toDate.getText().toString().isEmpty()) {
            onMessage(getString(R.string.error_selct_to_date));
            return false;
        }
        return true;
    }

    @Override
    public int getTheme() {
        return R.style.AppTheme_NoActionBar_FullScreenDialog;
    }

    @Override
    protected void injectView() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.titleTxt.setText("Filter");
        codeInputs = new Integer[]{binding.transactionNo.getId(),
                binding.mobileNumber.getId(), binding.beneName.getId(),
                binding.accountNo.getId()
        };

        codeInputsText = new Integer[]{
                binding.fromDate.getId(),
                binding.toDate.getId()
        };
        binding.backBtn.setOnClickListener(v -> {
            cancelUpload();
        });

        binding.fromDate.setOnClickListener(v -> {
            isStartDate = true;
            showPickerDialog(getString(R.string.select_date_txt));
        });

        binding.toDate.setOnClickListener(v -> {
            isStartDate = false;
            showPickerDialog(getString(R.string.select_date_txt));
        });


        binding.applyBtn.setOnClickListener(v -> {

            //     if (isValidate()) {
            request = new TransactionHistoryRequest();
            request.beneAccountNo = binding.accountNo.getText().toString();
            request.beneName = binding.beneName.getText().toString();
            request.fromDate = binding.fromDate.getText().toString();
            request.toDate = binding.toDate.getText().toString();
            request.mobileNo = binding.mobileNumber.getText().toString();
            request.txnNo = binding.transactionNo.getText().toString();
            onApplyFilterInterface.onApply(request);
            getDialog().dismiss();
            //   }
        });
        binding.applyBtn.setEnabled(false);
        binding.applyBtn.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.edit_text_gray_back, null));
        binding.applyBtn.setTextColor(getResources().getColor(R.color.white));
        addTextWatcher();
        addEditWatcher();
    }

    public void addEditWatcher() {
        for (int id : codeInputs) {
            EditText v = getView().findViewById(id);
            v.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    checkISAnyEmpty();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    public void addTextWatcher() {
        for (int id : codeInputsText) {
            TextView v = getView().findViewById(id);
            v.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    checkISAnyEmpty();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    public void checkISAnyEmpty() {
        if (binding.fromDate.getText().toString().isEmpty() &&
                binding.toDate.getText().toString().isEmpty()
                && binding.transactionNo.getText().toString().isEmpty() &&
                binding.mobileNumber.getText().toString().isEmpty() && binding.beneName.getText().toString().isEmpty() &&
                binding.accountNo.getText().toString().isEmpty()) {
            binding.applyBtn.setEnabled(false);
            binding.applyBtn.setBackground(getResources().getDrawable(R.drawable.edit_text_gray_back));
            binding.applyBtn.setTextColor(getResources().getColor(R.color.white));
        } else if (!binding.fromDate.getText().toString().isEmpty() &&
                !binding.toDate.getText().toString().isEmpty()) {
            binding.applyBtn.setTextColor(getResources().getColor(R.color.white));
            binding.applyBtn.setBackground(getResources().getDrawable(R.drawable.background_button));
            binding.applyBtn.setEnabled(true);
        } else if (!binding.fromDate.getText().toString().isEmpty() &&
                binding.toDate.getText().toString().isEmpty()) {
            binding.applyBtn.setEnabled(false);
            binding.applyBtn.setBackground(getResources().getDrawable(R.drawable.edit_text_gray_back));
            binding.applyBtn.setTextColor(getResources().getColor(R.color.white));
        } else if (binding.fromDate.getText().toString().isEmpty() &&
                !binding.toDate.getText().toString().isEmpty()) {
            binding.applyBtn.setEnabled(false);
            binding.applyBtn.setBackground(getResources().getDrawable(R.drawable.edit_text_gray_back));
            binding.applyBtn.setTextColor(getResources().getColor(R.color.white));
        } else {
            binding.applyBtn.setTextColor(getResources().getColor(R.color.white));
            binding.applyBtn.setBackground(getResources().getDrawable(R.drawable.background_button));
            binding.applyBtn.setEnabled(true);
        }

    }


    /**
     * dialog code for show date picker
     */
    private void showPickerDialog(String title) {
        Calendar calendar = Calendar.getInstance();
        int Year = calendar.get(Calendar.YEAR);
        int Month = calendar.get(Calendar.MONTH);
        int Day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = DatePickerDialog
                .newInstance(this, Year, Month, Day);

        datePickerDialog.setThemeDark(false);
        datePickerDialog.showYearPickerFirst(false);
        datePickerDialog.setAccentColor(Color.parseColor("#342E78"));
        datePickerDialog.setLocale(new Locale("en"));
        // if (isStartDate) {
        datePickerDialog.setMaxDate(calendar);
        // } else {
        //   datePickerDialog.setMinDate(calendar);
        // }

        datePickerDialog.setTitle(title);
        datePickerDialog.show(getParentFragmentManager(), "");
        datePickerDialog.setOnCancelListener(DialogInterface::dismiss);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        if (isStartDate) {
            binding.fromDate.setText(DateAndTime.getLedgerFormat(year, monthOfYear, dayOfMonth));
        } else {
            binding.toDate.setText(DateAndTime.getLedgerFormat(year, monthOfYear, dayOfMonth));
        }
    }
}
