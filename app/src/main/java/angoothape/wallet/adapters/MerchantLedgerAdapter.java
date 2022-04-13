package angoothape.wallet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import angoothape.wallet.AdpatersViewHolder.EmptyBeneficiaryListViewHolder;
import angoothape.wallet.R;
import angoothape.wallet.databinding.EmptyViewBeneBinding;
import angoothape.wallet.databinding.LedgerDesignMerchantBinding;
import angoothape.wallet.databinding.TransactionHistoryDesignBinding;
import angoothape.wallet.di.JSONdi.restResponse.ledger.StatementOfAccount;

public class MerchantLedgerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int EMPTY_VIEW = 1;
    private final int ITEM = 0;

    Context context;
    List<StatementOfAccount> statementOfAccounts;
    int showType = 0;

    public MerchantLedgerAdapter(List<StatementOfAccount> statementOfAccounts, Context context) {
        this.statementOfAccounts = statementOfAccounts;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (ITEM == viewType) {
            LedgerDesignMerchantBinding binding =
                    LedgerDesignMerchantBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new MerchantLedgerViewHolder(binding);
        } else {
            EmptyViewBeneBinding binding =
                    EmptyViewBeneBinding.inflate(LayoutInflater.from(parent.getContext())
                            , parent, false);
            return new EmptyBeneficiaryListViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MerchantLedgerViewHolder) {
            StatementOfAccount account = statementOfAccounts.get(position);
            //  ((MerchantLedgerViewHolder) holder).binding.viewReciept.setVisibility(View.GONE);
            ((MerchantLedgerViewHolder) holder).binding.txnDateTime.setText(account.date);
            ((MerchantLedgerViewHolder) holder).binding.status.setText(account.description);
            ((MerchantLedgerViewHolder) holder).binding.transactionNo.setText(account.ref_No);


            try {

                double creditBalance = Double.parseDouble(account.credit_INR);

                if (creditBalance > 0) {
                    ((MerchantLedgerViewHolder) holder).binding.crDrIcon.setImageResource(R.drawable.ic_arrow_down_green);
                    ((MerchantLedgerViewHolder) holder).binding.sendingAmount.setTextColor(ContextCompat.getColor(context, R.color.Green));
                    ((MerchantLedgerViewHolder) holder).binding.sendingAmount.setText("₹ " + account.credit_INR);
                } else {
                    ((MerchantLedgerViewHolder) holder).binding.crDrIcon.setImageResource(R.drawable.ic_arrow_up_red);
                    ((MerchantLedgerViewHolder) holder).binding.sendingAmount.setTextColor(ContextCompat.getColor(context, R.color.Red));
                    ((MerchantLedgerViewHolder) holder).binding.sendingAmount.setText("₹ " + account.debit_INR);
                }


            } catch (Exception e) {

            }
        } else if (holder instanceof EmptyBeneficiaryListViewHolder) {

        }
    }

    public void setType(int type) {
        this.showType = type;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (statementOfAccounts == null) {
            return 0;
        } else if (statementOfAccounts.size() == 0) {
            //Return 1 here to show nothing
            return EMPTY_VIEW;
        }
        return statementOfAccounts.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (statementOfAccounts.isEmpty()) {
            return EMPTY_VIEW;
        }
        return super.getItemViewType(position);
    }

    public static class MerchantLedgerViewHolder extends RecyclerView.ViewHolder {
        LedgerDesignMerchantBinding binding;

        public MerchantLedgerViewHolder(@NonNull LedgerDesignMerchantBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
