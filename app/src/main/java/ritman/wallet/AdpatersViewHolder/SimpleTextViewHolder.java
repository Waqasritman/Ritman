package ritman.wallet.AdpatersViewHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ritman.wallet.databinding.TransferListPurposeDesignBinding;

public class SimpleTextViewHolder extends RecyclerView.ViewHolder {
    public TransferListPurposeDesignBinding binding;

    public SimpleTextViewHolder(@NonNull TransferListPurposeDesignBinding itemView) {
        super(itemView.getRoot());
        this.binding = itemView;
    }
}
