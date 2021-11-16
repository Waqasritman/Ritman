package angoothape.wallet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import angoothape.wallet.R;
import angoothape.wallet.databinding.LedgerDesignMerchantBinding;
import angoothape.wallet.databinding.TransactionHistoryDesignBinding;
import angoothape.wallet.di.JSONdi.restResponse.ledger.StatementOfAccount;

public class MerchantLedgerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<StatementOfAccount> statementOfAccounts;
    int showType = 0;
    // 0 - all , 1 - credit 2 - debit

    public MerchantLedgerAdapter(List<StatementOfAccount> statementOfAccounts, Context context) {
        this.statementOfAccounts = statementOfAccounts;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LedgerDesignMerchantBinding binding =
                LedgerDesignMerchantBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MerchantLedgerViewHolder(binding);
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


            // if (showType == 0) {
//            ((MerchantLedgerViewHolder) holder).binding.allLayout.setVisibility(View.VISIBLE);
//            //    ((MerchantLedgerViewHolder) holder).binding.creditLayout.setVisibility(View.GONE);
//            //  ((MerchantLedgerViewHolder) holder).binding.debitLayout.setVisibility(View.GONE);
//            ((MerchantLedgerViewHolder) holder).binding.date.setText(account.date);
//            ((MerchantLedgerViewHolder) holder).binding.description.setText(account.description);
//
//            ((MerchantLedgerViewHolder) holder).binding.debit.setText(account.debit_INR);
//            ((MerchantLedgerViewHolder) holder).binding.credit.setText(account.credit_INR);
//            } else if (showType == 1) {
//                ((MerchantLedgerViewHolder) holder).binding.allLayout.setVisibility(View.GONE);
//                ((MerchantLedgerViewHolder) holder).binding.creditLayout.setVisibility(View.VISIBLE);
//                ((MerchantLedgerViewHolder) holder).binding.debitLayout.setVisibility(View.GONE);
//                ((MerchantLedgerViewHolder) holder).binding.creditDate.setText(account.date);
//                ((MerchantLedgerViewHolder) holder).binding.creditDescription.setText(account.description);
//
//                ((MerchantLedgerViewHolder) holder).binding.creditcr.setText(account.credit_INR);
//            } else if (showType == 2) {
//                ((MerchantLedgerViewHolder) holder).binding.allLayout.setVisibility(View.GONE);
//                ((MerchantLedgerViewHolder) holder).binding.creditLayout.setVisibility(View.GONE);
//                ((MerchantLedgerViewHolder) holder).binding.debitLayout.setVisibility(View.VISIBLE);
//                ((MerchantLedgerViewHolder) holder).binding.debitDate.setText(account.date);
//                ((MerchantLedgerViewHolder) holder).binding.debitDescription.setText(account.description);
//
//                ((MerchantLedgerViewHolder) holder).binding.debitdebit.setText(account.debit_INR);
//            }


        }
    }


    public void setType(int type) {
        this.showType = type;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return statementOfAccounts.size();
    }


    public static class MerchantLedgerViewHolder extends RecyclerView.ViewHolder {
        LedgerDesignMerchantBinding binding;

        public MerchantLedgerViewHolder(@NonNull LedgerDesignMerchantBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
