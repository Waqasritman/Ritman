package angoothape.wallet.AdpatersViewHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import angoothape.wallet.databinding.PlanNameLayoutBinding;

public class PlanNameViewHolder extends RecyclerView.ViewHolder{

    public PlanNameLayoutBinding binding;

    public PlanNameViewHolder(@NonNull PlanNameLayoutBinding itemView) {
        super(itemView.getRoot());
        this.binding = itemView;
    }
}
