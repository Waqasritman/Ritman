package angoothape.wallet.refund_module.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import angoothape.wallet.R;
import angoothape.wallet.TransactionHistory.TransactionHistoryViewModel;
import angoothape.wallet.adapters.TransactionHistoryAdapter;
import angoothape.wallet.databinding.FragmentRefundHistoryBinding;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.TransactionHistoryRequest;
import angoothape.wallet.di.XMLdi.ResponseHelper.GetTransactionReceiptResponse;
import angoothape.wallet.di.XMLdi.ResponseHelper.TransactionHistoryResponse;
import angoothape.wallet.dialogs.FilterDialog;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.interfaces.OnApplyFilterInterface;
import angoothape.wallet.interfaces.OnGetTransactionHistory;
import angoothape.wallet.interfaces.OnSelectTransaction;
import angoothape.wallet.utils.DateAndTime;
import angoothape.wallet.utils.IsNetworkConnection;
import angoothape.wallet.utils.Utils;

public class RefundHistoryFragment extends BaseFragment<FragmentRefundHistoryBinding>
        implements OnGetTransactionHistory, OnSelectTransaction, OnApplyFilterInterface {

    public List<TransactionHistoryResponse> responseList;
    public List<TransactionHistoryResponse> filteredList;
    TransactionHistoryViewModel viewModel;
    TransactionHistoryAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_refund_history;
    }

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(TransactionHistoryViewModel.class);
        setAccountsRecyclerView();

        TransactionHistoryRequest request = new TransactionHistoryRequest();
        request.fromDate = DateAndTime.getLastMothDate();
        request.toDate = DateAndTime.getCurrentDate();
        getHistory(request);

        setTextType(request);

        binding.filterLayout.setOnClickListener(v -> {
            FilterDialog dialog = new FilterDialog(this);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            dialog.show(transaction, "");
        });
    }

    public void getHistory(TransactionHistoryRequest request) {
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            Utils.showCustomProgressDialog(getContext(), false);
            viewModel.getRefundTransactionHistory(request, getSessionManager().getMerchantName())
                    .observe(this, response -> {
                        Utils.hideCustomProgressDialog();
                        assert response.resource != null;
                        if (response.status == Status.ERROR) {
                            onError(getString(response.messageResourceId));
                        } else if (response.status == Status.SUCCESS) {
                            if (response.resource.responseCode.equals(101)) {
                                onGetHistoryList(response.resource.data);
                            } else {
                                onGetHistoryList(new ArrayList<>());
                               // onError(response.resource.description);
                            }
                        } else if (response.status == Status.UNSUCCESS) {
                            onError(response.resource.description);
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
                TransactionHistoryAdapter(getContext(), filteredList, true, this);
        LinearLayoutManager accountLayoutManager = new
                LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.recyclerView.setLayoutManager(accountLayoutManager);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onGetHistoryList(List<TransactionHistoryResponse> historyList) {
        responseList.clear();
        filteredList.clear();
        responseList.addAll(historyList);
        filteredList.addAll(historyList);
        adapter.notifyDataSetChanged();
        binding.recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSelectTransaction(GetTransactionReceiptResponse receiptResponse) {

    }

    @Override
    public void onSelectTransactionReceipt(String txtNumber) {

    }

    @Override
    public void onSelectTransactionAEPSReceipt(String txtNumber) {

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