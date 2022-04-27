package angoothape.wallet.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import angoothape.wallet.AdpatersViewHolder.EmptyBeneficiaryListViewHolder;
import angoothape.wallet.R;
import angoothape.wallet.databinding.EmptyViewBeneBinding;
import angoothape.wallet.databinding.TransactionHistoryDesignBinding;
import angoothape.wallet.di.XMLdi.ResponseHelper.TransactionHistoryResponse;
import angoothape.wallet.interfaces.OnSelectTransaction;
import angoothape.wallet.utils.Utils;
import okhttp3.internal.Util;

import java.util.List;

public class TransactionHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int EMPTY_VIEW = 1;
    private final int ITEM = 0;
    OnSelectTransaction onSelectTransaction;
    List<TransactionHistoryResponse> beneficiaryListResponses;
    Context context;
    boolean isRefundHistory = false;

    public TransactionHistoryAdapter(Context context, List<TransactionHistoryResponse> beneficiaryListResponses
            , OnSelectTransaction onSelectTransaction) {
        this.context = context;
        this.beneficiaryListResponses = beneficiaryListResponses;
        this.onSelectTransaction = onSelectTransaction;
    }

    public TransactionHistoryAdapter(Context context, List<TransactionHistoryResponse> beneficiaryListResponses
            , boolean isRefund, OnSelectTransaction onSelectTransaction) {
        this.context = context;
        this.isRefundHistory = isRefund;
        this.beneficiaryListResponses = beneficiaryListResponses;
        this.onSelectTransaction = onSelectTransaction;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (ITEM == viewType) {
            TransactionHistoryDesignBinding binding =
                    TransactionHistoryDesignBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new TransactionHistoryListHolder(binding);
        } else {
            EmptyViewBeneBinding binding =
                    EmptyViewBeneBinding.inflate(LayoutInflater.from(parent.getContext())
                            , parent, false);
            return new EmptyBeneficiaryListViewHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TransactionHistoryListHolder) {
            Log.e("onBindViewHolder: ", String.valueOf(position));
            TransactionHistoryResponse response = beneficiaryListResponses.get(position);
            ((TransactionHistoryListHolder) holder).binding.beneficairyName.setText(response.receiverName);
            ((TransactionHistoryListHolder) holder).binding.paymentType.setText(response.paymentType);
            if (response.paymentType == null) {
                ((TransactionHistoryListHolder) holder).binding.paymentTypeLayout.setVisibility(View.GONE);
            } else {
                ((TransactionHistoryListHolder) holder).binding.paymentTypeLayout.setVisibility(View.VISIBLE);
            }

            String timeDate = response.transactionDate.concat("Z");
            ((TransactionHistoryListHolder) holder).binding.txnDateTime.setText(Utils.getDateFromServerTime(timeDate));
            ((TransactionHistoryListHolder) holder).binding.sendingAmount.setText(
                    ((response.sendingAmount != null) ? response.sendingAmount : "0.00").concat(" ").concat(response.currency));
            ((TransactionHistoryListHolder) holder).binding.status.setText(response.status);
            ((TransactionHistoryListHolder) holder).binding.transactionNo.setText(response.transactionNumber);
            ((TransactionHistoryListHolder) holder).binding.viewReciept
                    .setOnClickListener(v -> {
                        if (response.receiverName.trim().equalsIgnoreCase("AEPS AEPS")) {
                            onSelectTransaction.onSelectTransactionAEPSReceipt(response.transactionNumber);
                        } else {
                            onSelectTransaction.onSelectTransactionReceipt(response.transactionNumber);
                        }
                    });


            if (isRefundHistory) {
                ((TransactionHistoryListHolder) holder).binding.viewReciept.setVisibility(View.GONE);
            }

        } else if (holder instanceof EmptyBeneficiaryListViewHolder) {
            ((EmptyBeneficiaryListViewHolder) holder).binding.errorEmpty
                    .setText(context.getResources().getString(R.string.no_history_found));
        }
    }

    @Override
    public int getItemCount() {
        if (beneficiaryListResponses == null) {
            return ITEM;
        } else if (beneficiaryListResponses.size() == 0) {
            //Return 1 here to show nothing
            return EMPTY_VIEW;
        }

        // Add extra view to show the footer view
        return beneficiaryListResponses.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (beneficiaryListResponses.isEmpty()) {
            return EMPTY_VIEW;
        }
        return super.getItemViewType(position);
    }

    public static class TransactionHistoryListHolder extends RecyclerView.ViewHolder {
        public TransactionHistoryDesignBinding binding;

        public TransactionHistoryListHolder(@NonNull TransactionHistoryDesignBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

}
