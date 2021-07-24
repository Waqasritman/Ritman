package ritman.wallet.AdpatersViewHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ritman.wallet.databinding.PlanNameLayoutBinding;
import ritman.wallet.databinding.RechargeplanLayoutBinding;

public class RechargePlanNameViewHolder extends RecyclerView.ViewHolder{

    public RechargeplanLayoutBinding binding;

    public RechargePlanNameViewHolder(@NonNull RechargeplanLayoutBinding itemView) {
        super(itemView.getRoot());
        this.binding = itemView;
    }
}
