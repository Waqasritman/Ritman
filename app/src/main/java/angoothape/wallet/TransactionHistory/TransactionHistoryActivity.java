package angoothape.wallet.TransactionHistory;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import angoothape.wallet.AEPSReceiptActivity;
import angoothape.wallet.MoneyTransferModuleV.TransactionReceiptActivity;
import angoothape.wallet.R;
import angoothape.wallet.adapters.TransactionHistoryAdapter;
import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.ActivityTransacationHistoryBinding;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.SimpleRequest;
import angoothape.wallet.di.JSONdi.restRequest.TransactionHistoryRequest;
import angoothape.wallet.di.XMLdi.ResponseHelper.TransactionHistoryResponse;
import angoothape.wallet.di.XMLdi.ResponseHelper.GetTransactionReceiptResponse;
import angoothape.wallet.dialogs.FilterDialog;
import angoothape.wallet.interfaces.OnApplyFilterInterface;
import angoothape.wallet.interfaces.OnGetTransactionHistory;
import angoothape.wallet.interfaces.OnSelectTransaction;
import angoothape.wallet.utils.DateAndTime;
import angoothape.wallet.utils.IsNetworkConnection;
import angoothape.wallet.utils.Utils;

public class TransactionHistoryActivity extends RitmanBaseActivity<ActivityTransacationHistoryBinding>
        implements OnGetTransactionHistory, OnSelectTransaction, OnApplyFilterInterface {


    public List<TransactionHistoryResponse> responseList;
    public List<TransactionHistoryResponse> filteredList;
    TransactionHistoryAdapter adapter;

    TransactionHistoryViewModel viewModel;

    @Override
    public int getLayoutId() {
        return R.layout.activity_transacation_history;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(TransactionHistoryViewModel.class);
        setAccountsRecyclerView();
        binding.inculdeLayout.backBtn
                .setColorFilter(ContextCompat.getColor(this,
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);
        binding.inculdeLayout.toolBarFinal.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        binding.inculdeLayout.crossBtn.setOnClickListener(v -> {
            onClose();
        });

        binding.inculdeLayout.crossBtn.setVisibility(View.GONE);
        binding.inculdeLayout.titleTxt.setText(getString(R.string.transaction_history_txt));


        TransactionHistoryRequest request = new TransactionHistoryRequest();
        request.fromDate = DateAndTime.getLastMothDate();
        request.toDate = DateAndTime.getCurrentDate();
        getHistory(request);

        setTextType(request);

        binding.paidLayout.setOnClickListener(v -> {
            filteredList.clear();
            for (int i = 0; i < responseList.size(); i++) {
                if (responseList.get(i).status.equalsIgnoreCase("paid")) {
                    filteredList.add(responseList.get(i));
                }
            }

            binding.paidView.setVisibility(View.VISIBLE);
            binding.allReceived.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        });
        binding.allLayout.setOnClickListener(v -> {
            filteredList.clear();
            filteredList.addAll(responseList);

            binding.paidView.setVisibility(View.GONE);
            binding.allReceived.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        });

        binding.inculdeLayout.backBtn.setOnClickListener(v -> finish());


        binding.filterLayout.setOnClickListener(v -> {
            FilterDialog dialog = new FilterDialog(this);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            dialog.show(transaction, "");
        });
    }

    @Override
    public void onGetHistoryList(List<TransactionHistoryResponse> historyList) {
        responseList.clear();
        filteredList.clear();
        filteredList.addAll(historyList);
        responseList.addAll(historyList);
        adapter.notifyDataSetChanged();
        binding.recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }


    public void getHistory(TransactionHistoryRequest request) {
        if (IsNetworkConnection.checkNetworkConnection(this)) {
            Utils.showCustomProgressDialog(this, false);

            viewModel.getTransactionHistory(request, sessionManager.getMerchantName())
                    .observe(this, response -> {
                        Utils.hideCustomProgressDialog();
                        assert response.resource != null;
                        if (response.status == Status.ERROR) {
                            onMessage(getString(response.messageResourceId));
                        } else if (response.status == Status.SUCCESS) {
                            if (response.resource.responseCode.equals(101)) {
                                onGetHistoryList(response.resource.data);
                            } else {
                                onMessage(response.resource.description);
                            }
                        } else if (response.status == Status.UNSUCCESS) {
                            onMessage(response.resource.description);
                        }
                    });
        } else {
            onMessage(getString(R.string.no_internet));
        }

    }

    public void setTextType(TransactionHistoryRequest request) {
        binding.filterType.setText(request.toString());
    }

    /**
     * setup the recycler view when screen load after that just notify the adapter
     */
    private void setAccountsRecyclerView() {
        responseList = new ArrayList<>();
        filteredList = new ArrayList<>();
        adapter = new
                TransactionHistoryAdapter(this, filteredList, this);
        LinearLayoutManager accountLayoutManager = new
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.recyclerView.setLayoutManager(accountLayoutManager);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSelectTransaction(GetTransactionReceiptResponse receiptResponse) {

    }

    @Override
    public void onSelectTransactionReceipt(String txtNumber) {
        Intent intent = new Intent(this, TransactionReceiptActivity.class);
        intent.putExtra("TransactionNumber", txtNumber);
        startActivity(intent);
    }

    @Override
    public void onSelectTransactionAEPSReceipt(String txtNumber) {
        Intent intent = new Intent(this, AEPSReceiptActivity.class);
        intent.putExtra("TransactionNumber", txtNumber);
        startActivity(intent);
    }

    @Override
    public void onRepeatTransaction(GetTransactionReceiptResponse receiptResponse) {

    }

    @Override
    public void onApply(TransactionHistoryRequest request) {
        getHistory(request);
        setTextType(request);
    }
}