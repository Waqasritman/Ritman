package tootipay.wallet.AdpatersViewHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import tootipay.wallet.databinding.BeneficiaryListAdpaterDesignBinding;

public class BeneficiaryListViewHolder  extends RecyclerView.ViewHolder {
    public BeneficiaryListAdpaterDesignBinding binding;

    public BeneficiaryListViewHolder(@NonNull BeneficiaryListAdpaterDesignBinding itemView) {
        super(itemView.getRoot());
        this.binding = itemView;
    }
}
