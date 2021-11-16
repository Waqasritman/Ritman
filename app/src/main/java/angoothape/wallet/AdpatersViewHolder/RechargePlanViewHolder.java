package angoothape.wallet.AdpatersViewHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import angoothape.wallet.databinding.PlanNameLayoutBinding;

public class RechargePlanViewHolder extends RecyclerView.ViewHolder {

    public PlanNameLayoutBinding binding;

    public RechargePlanViewHolder(@NonNull PlanNameLayoutBinding itemView) {
        super(itemView.getRoot());
        this.binding = itemView;
    }
}
