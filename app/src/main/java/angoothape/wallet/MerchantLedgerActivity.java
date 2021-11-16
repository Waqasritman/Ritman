package angoothape.wallet;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import angoothape.wallet.adapters.MerchantLedgerAdapter;
import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.ActivityMerchantLedgerBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.LedgerRequest;
import angoothape.wallet.di.JSONdi.restRequest.SimpleRequest;
import angoothape.wallet.di.JSONdi.restResponse.CustomerTransactionHistory;
import angoothape.wallet.di.JSONdi.restResponse.ledger.LedgerRoot;
import angoothape.wallet.di.JSONdi.restResponse.ledger.StatementOfAccount;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.dialogs.SingleButtonMessageDialog;
import angoothape.wallet.home_module.viewmodel.HomeViewModel;
import angoothape.wallet.interfaces.OnDecisionMade;
import angoothape.wallet.utils.DateAndTime;
import angoothape.wallet.utils.Utils;

public class MerchantLedgerActivity extends RitmanBaseActivity<ActivityMerchantLedgerBinding>
        implements DatePickerDialog.OnDateSetListener, OnDecisionMade {

    HomeViewModel viewModel;
    MerchantLedgerAdapter adapter;
    List<StatementOfAccount> statementOfAccounts;
    List<StatementOfAccount> filteredStatementOfAccounts;
    boolean isStarting = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_merchant_ledger;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding.toolBar.titleTxt.setText(getString(R.string.ledger_history_txt));
        binding.toolBar.backBtn.setOnClickListener(v -> onBackPressed());

        binding.toolBar.toolBarFinal
                .setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.toolBar.backBtn
                .setColorFilter(ContextCompat.getColor(this,
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);
        binding.toolBar.crossBtn.setVisibility(View.GONE);
        statementOfAccounts = new ArrayList<>();
        filteredStatementOfAccounts = new ArrayList<>();
        //  setupRecyclerView();
        getLedger(DateAndTime.getLastMothDate()
                , DateAndTime.getCurrentDate());


        //  setupRecyclerView();
        binding.tvApply.setOnClickListener(v -> {
            if (binding.tvFromDate.getText().toString().isEmpty()) {
                onMessage("Please select starting date");
            } else if (binding.tvToDate.getText().toString().isEmpty()) {
                onMessage("Please select ending date");
            } else {
                getLedger(binding.tvFromDate.getText().toString()
                        , binding.tvToDate.getText().toString());
            }

        });


        binding.tvFromDate.setOnClickListener(v -> {
            showPickerDialog(getString(R.string.select_date_txt), true);
        });


        binding.tvToDate.setOnClickListener(v -> showPickerDialog(getString(R.string.select_date_txt), false));


        binding.allLayout.setOnClickListener(v -> {
            filteredStatementOfAccounts.clear();
            filteredStatementOfAccounts.addAll(statementOfAccounts);
            adapter.notifyDataSetChanged();

            binding.topDebitView.setVisibility(View.GONE);
            binding.topCreditView.setVisibility(View.GONE);
            binding.allReceived.setVisibility(View.VISIBLE);


        });

        binding.credit.setOnClickListener(v -> {
            setCredit();
            binding.topDebitView.setVisibility(View.GONE);
            binding.topCreditView.setVisibility(View.VISIBLE);
            binding.allReceived.setVisibility(View.GONE);

        });

        binding.debitLayout.setOnClickListener(v -> {
            setDebit();

            binding.topDebitView.setVisibility(View.VISIBLE);
            binding.topCreditView.setVisibility(View.GONE);
            binding.allReceived.setVisibility(View.GONE);
        });
    }

    public void getLedger(String startDate, String endDate) {
        Utils.showCustomProgressDialog(this, false);

        String gKey = KeyHelper.getKey(sessionManager.getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(sessionManager.getMerchantName())).trim();
        LedgerRequest request = new LedgerRequest();
        request.Start_Date = startDate;
        request.End_Date = endDate;
        String body = RestClient.makeGSONString(request);
        AERequest aeRequest = new AERequest();
        aeRequest.body = AESHelper.encrypt(body.trim(), gKey.trim());
        Log.e("getLedger: ", body);
        viewModel.getMerchantLedger(aeRequest
                , KeyHelper.getKey(getSessionManager().getMerchantName()).trim(), KeyHelper.getSKey(KeyHelper
                        .getKey(getSessionManager().getMerchantName())).trim()).observe(this, response -> {

            Utils.hideCustomProgressDialog();
            if (response.status == Status.ERROR) {
                onMessage(getString(response.messageResourceId));
            } else {
                assert response.resource != null;
                if (response.resource.responseCode.equals(101)) {


                    String bodyy = AESHelper.decrypt(response.resource.data.body
                            , gKey);

                    try {
                        Gson gson = new Gson();
                        Type type = new TypeToken<LedgerRoot>() {
                        }.getType();
                        LedgerRoot data = gson.fromJson(bodyy, type);
                        ;

                        statementOfAccounts = new ArrayList<>();
                        statementOfAccounts.clear();
                        this.filteredStatementOfAccounts = new ArrayList<>();
                        this.filteredStatementOfAccounts.clear();
                        this.filteredStatementOfAccounts.addAll(data.statement_Of_Account_);
                        this.statementOfAccounts.addAll(data.statement_Of_Account_);
                        setupRecyclerView();


                        try {

                            if (data.closing_Bal_data_.get(0).closingBalancesINR.contains("Dr") ||
                                    data.closing_Bal_data_.get(0).closingBalancesINR.contains("DR") ||
                                    data.closing_Bal_data_.get(0).closingBalancesINR.contains("dr")) {
                                //  binding.closingBalance.setTextColor(ContextCompat.getColor(this, R.color.Red));
                                binding.titleText.setText("Debit balance");
                                String bal = data.closing_Bal_data_.get(0).closingBalancesINR;
                                bal = bal.toLowerCase();
                                bal = bal.replace("dr", "");
                                binding.closingBalance.setText(bal + " " + "₹");
                                //Log.e("onBindViewHolder: ", String.valueOf(dValue));
                            } else if (data.closing_Bal_data_.get(0).closingBalancesINR.contains("Cr") ||
                                    data.closing_Bal_data_.get(0).closingBalancesINR.contains("CR") || data.closing_Bal_data_.get(0).closingBalancesINR.contains("cr")) {
                                binding.titleText.setText("Credit balance");
                                String bal = data.closing_Bal_data_.get(0).closingBalancesINR;
                                bal = bal.toLowerCase();
                                bal = bal.replace("cr", "");
                                binding.closingBalance.setText(bal + " " + "₹");
                                //binding.closingBalance.setTextColor(ContextCompat.getColor(this, R.color.cardview0));
                            }
                            binding.topDebitView.setVisibility(View.GONE);
                            binding.topCreditView.setVisibility(View.GONE);
                            binding.allReceived.setVisibility(View.VISIBLE);
                            binding.mainLayout.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            Log.e("ex: ", e.getLocalizedMessage());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    // onMessage(response.resource.description);
                    statementOfAccounts = new ArrayList<>();
                    statementOfAccounts.clear();
                    this.filteredStatementOfAccounts = new ArrayList<>();
                    this.filteredStatementOfAccounts.clear();
                    adapter.notifyDataSetChanged();
                    showSucces(response.resource.description, "Error", true);
                }
            }
        });
    }


    private void showSucces(String message, String title, boolean isError) {
        SingleButtonMessageDialog dialog = new
                SingleButtonMessageDialog(title
                , message, this,
                false, isError);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }

    /**
     * Method will set the recycler view
     */
    private void setupRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        adapter = new
                MerchantLedgerAdapter(filteredStatementOfAccounts, this);
        binding.recyclerViewLedger.setLayoutManager(mLayoutManager);
        binding.recyclerViewLedger.setAdapter(adapter);
//        binding.recyclerViewLedger.addItemDecoration(new DividerItemDecoration(binding.recyclerViewLedger.getContext(),
//                DividerItemDecoration.VERTICAL));
    }


    private void setCredit() {
        filteredStatementOfAccounts.clear();
        for (StatementOfAccount account : statementOfAccounts) {
            try {
                double creditBalance = Double.parseDouble(account.credit_INR);

                if (creditBalance > 0) {
                    filteredStatementOfAccounts.add(account);
                }
            } catch (Exception e) {

            }

        }
        adapter.notifyDataSetChanged();
    }


    private void setDebit() {
        filteredStatementOfAccounts.clear();
        for (StatementOfAccount account : statementOfAccounts) {
            try {
                double creditBalance = Double.parseDouble(account.debit_INR);

                if (creditBalance > 0) {
                    filteredStatementOfAccounts.add(account);
                }
            } catch (Exception e) {

            }

        }
        adapter.notifyDataSetChanged();
    }

    /**
     * dialog code for show date picker
     */
    private void showPickerDialog(String title, boolean isStarting) {
        this.isStarting = isStarting;
        Calendar calendar = Calendar.getInstance();
        int Year = calendar.get(Calendar.YEAR);
        int Month = calendar.get(Calendar.MONTH);
        int Day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = DatePickerDialog
                .newInstance(this, Year, Month, Day);

        datePickerDialog.setThemeDark(false);
        // datePickerDialog.showYearPickerFirst(true);
        datePickerDialog.setAccentColor(Color.parseColor("#931E55"));
        datePickerDialog.setLocale(new Locale("en"));

        datePickerDialog.setMaxDate(calendar);
        datePickerDialog.setTitle(title);
        datePickerDialog.show(getSupportFragmentManager(), "");
        datePickerDialog.setOnCancelListener(DialogInterface::dismiss);
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        //   binding.mainLayout.setVisibility(View.GONE);
        if (isStarting) {
            binding.tvFromDate.setText(DateAndTime.getLedgerFormat(year, monthOfYear, dayOfMonth));
        } else {
            binding.tvToDate.setText(DateAndTime.getLedgerFormat(year, monthOfYear, dayOfMonth));
        }
        // binding.mainLayout.setVisibility(View.GONE);
    }

    @Override
    public void onProceed() {

    }

    @Override
    public void onCancel(boolean goBack) {

    }
}