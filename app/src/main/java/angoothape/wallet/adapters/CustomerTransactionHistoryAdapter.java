package angoothape.wallet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import angoothape.wallet.AdpatersViewHolder.EmptyBeneficiaryListViewHolder;
import angoothape.wallet.R;
import angoothape.wallet.databinding.CustomerTrHistoryDesignBinding;
import angoothape.wallet.databinding.EmptyViewBeneBinding;
import angoothape.wallet.di.JSONdi.restResponse.CustomerTransactionHistory;


public class CustomerTransactionHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int EMPTY_VIEW = 1;
    private final int ITEM = 0;
    List<CustomerTransactionHistory> beneficiaryListResponses;
    Context context;

    public CustomerTransactionHistoryAdapter(Context context, List<CustomerTransactionHistory>
            beneficiaryListResponses) {
        this.context = context;
        this.beneficiaryListResponses = beneficiaryListResponses;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (ITEM == viewType) {
            CustomerTrHistoryDesignBinding binding =
                    CustomerTrHistoryDesignBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
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
            CustomerTransactionHistory response = beneficiaryListResponses.get(position);

            ((TransactionHistoryListHolder) holder).binding.beneficairyName.setText(response.beneName);//

            ((TransactionHistoryListHolder) holder).binding.date.setText(response.txnDate);//

            ((TransactionHistoryListHolder) holder)
                    .binding.sendingAmount.setText(String.valueOf(response.TxnAmount)//
                    .concat(" ").concat("INR"));


            ((TransactionHistoryListHolder) holder).binding.noTr.setText(response.txnNo);//

            ((TransactionHistoryListHolder) holder).binding.accountNo.setText(response.AccountNumber);

            ((TransactionHistoryListHolder) holder).binding.paymentMode.setText(response.TxnMode);

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
        public CustomerTrHistoryDesignBinding binding;

        public TransactionHistoryListHolder(@NonNull CustomerTrHistoryDesignBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

}
