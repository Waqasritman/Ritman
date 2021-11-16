package angoothape.wallet.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import angoothape.wallet.databinding.FundingBankDetailsDesignBinding;
import angoothape.wallet.di.JSONdi.restResponse.FundingBankingDetailsResponse;
import angoothape.wallet.dialogs.FundingBankDialog;

public class BankFundingDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<FundingBankingDetailsResponse> FundingBankingDetailsResponseList;

    FundingBankDialog activity;
    boolean isHide = false;

    public BankFundingDetailsAdapter(List<FundingBankingDetailsResponse> fundingBankingDetailsResponseList,
                                     FundingBankDialog activity) {
        FundingBankingDetailsResponseList = fundingBankingDetailsResponseList;
        this.activity = activity;
        isHide = true;
    }

    public BankFundingDetailsAdapter(List<FundingBankingDetailsResponse> FundingBankingDetailsResponseList) {
        this.FundingBankingDetailsResponseList = FundingBankingDetailsResponseList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FundingBankDetailsDesignBinding binding = FundingBankDetailsDesignBinding.inflate(LayoutInflater.from(parent.getContext())
                , parent, false);
        return new BankFundingDetailsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BankFundingDetailsViewHolder) {
            FundingBankingDetailsResponse response = FundingBankingDetailsResponseList.get(position);
            ((BankFundingDetailsViewHolder) holder).binding.name.setText(response.bankName);
            ((BankFundingDetailsViewHolder) holder).binding.accountNo.setText(response.accountNo);
            ((BankFundingDetailsViewHolder) holder).binding.ifscCode.setText(response.ifscCode);
            if (isHide) {
                ((BankFundingDetailsViewHolder) holder).binding.accoutnLayout.setVisibility(View.GONE);
                ((BankFundingDetailsViewHolder) holder).binding.codeLayout.setVisibility(View.GONE);
            } else {
                ((BankFundingDetailsViewHolder) holder).binding.accoutnLayout.setVisibility(View.VISIBLE);
                ((BankFundingDetailsViewHolder) holder).binding.codeLayout.setVisibility(View.VISIBLE);
            }


            ((BankFundingDetailsViewHolder) holder).binding.getRoot().setOnClickListener(v -> {
                if (activity != null) {
                    activity.onSelectBank(response);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return FundingBankingDetailsResponseList.size();
    }


    public static class BankFundingDetailsViewHolder extends RecyclerView.ViewHolder {
        FundingBankDetailsDesignBinding binding;

        public BankFundingDetailsViewHolder(@NonNull FundingBankDetailsDesignBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}