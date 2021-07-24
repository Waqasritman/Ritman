package ritman.wallet.AdpatersViewHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ritman.wallet.databinding.BeneficiaryListAdpaterDesignBinding;

public class BeneficiaryListViewHolder  extends RecyclerView.ViewHolder {
    public BeneficiaryListAdpaterDesignBinding binding;

    public BeneficiaryListViewHolder(@NonNull BeneficiaryListAdpaterDesignBinding itemView) {
        super(itemView.getRoot());
        this.binding = itemView;
    }
}
