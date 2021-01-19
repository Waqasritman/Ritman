package tootipay.wallet.AdpatersViewHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import tootipay.wallet.databinding.RecyelerHomeDesignBinding;

public class UserViewHolder  extends RecyclerView.ViewHolder {
    public RecyelerHomeDesignBinding binding;

    public UserViewHolder(@NonNull RecyelerHomeDesignBinding itemView) {
        super(itemView.getRoot());
        this.binding = itemView;
    }
}
