package angoothape.wallet.AdpatersViewHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import angoothape.wallet.databinding.CountryDesignBinding;

public class CountryListViewHolder extends RecyclerView.ViewHolder {
    public CountryDesignBinding binding;

    public CountryListViewHolder(@NonNull CountryDesignBinding itemView) {
        super(itemView.getRoot());
        this.binding = itemView;
    }
}
