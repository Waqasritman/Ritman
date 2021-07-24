package ritman.wallet.TransactionHistory;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import ritman.wallet.MoneyTransferModuleV.TransactionReceiptActivity;
import ritman.wallet.R;
import ritman.wallet.adapters.TransactionHistoryAdapter;
import ritman.wallet.base.RitmanBaseActivity;
import ritman.wallet.databinding.ActivityTransacationHistoryBinding;
import ritman.wallet.di.JSONdi.NetworkResource;
import ritman.wallet.di.JSONdi.Status;
import ritman.wallet.di.JSONdi.restRequest.SimpleRequest;
import ritman.wallet.di.XMLdi.RequestHelper.TransactionHistoryRequest;
import ritman.wallet.di.XMLdi.ResponseHelper.TransactionHistoryResponse;
import ritman.wallet.di.XMLdi.ResponseHelper.GetTransactionReceiptResponse;
import ritman.wallet.di.XMLdi.apicaller.TransactionHistoryTask;
import ritman.wallet.interfaces.OnGetTransactionHistory;
import ritman.wallet.interfaces.OnSelectTransaction;
import ritman.wallet.utils.IsNetworkConnection;
import ritman.wallet.utils.Utils;

public class TransactionHistoryActivity extends RitmanBaseActivity<ActivityTransacationHistoryBinding>
        implements OnGetTransactionHistory, OnSelectTransaction {


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

        if (IsNetworkConnection.checkNetworkConnection(this)) {
            Utils.showCustomProgressDialog(this , false);
            SimpleRequest request = new SimpleRequest();
            request.Credentials.LanguageID = Integer.parseInt(sessionManager.getlanguageselection());
            viewModel.getTransactionHistory(request, sessionManager.getMerchantName())
                    .observe(this, response -> {
                        Utils.hideCustomProgressDialog();
                        assert response.resource != null;
                        if(response.status == Status.ERROR) {
                            onMessage(getString(response.messageResourceId));
                        } else if(response.status == Status.SUCCESS) {
                            if(response.resource.responseCode.equals(101)) {
                                onGetHistoryList(response.resource.data);
                            } else {
                                onMessage(response.resource.description);
                            }
                        } else if(response.status == Status.UNSUCCESS) {
                            onMessage(response.resource.description);
                        }
                    });
        } else {
            onMessage(getString(R.string.no_internet));
        }


        binding.paidLayout.setOnClickListener(v -> {
            filteredList.clear();
            for (int i = 0; i < responseList.size(); i++) {
                if (responseList.get(i).status.equalsIgnoreCase("paid")) {
                    filteredList.add(responseList.get(i));
                }
            }
//can u see this code filterng the list yes
            //bhai yeh kh rha tha aissa banao ye status kya lena hai

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
    public void onRepeatTransaction(GetTransactionReceiptResponse receiptResponse) {

    }
}