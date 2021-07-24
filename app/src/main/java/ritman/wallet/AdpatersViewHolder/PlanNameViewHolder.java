package ritman.wallet.AdpatersViewHolder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ritman.wallet.databinding.PlanNameLayoutBinding;

public class PlanNameViewHolder extends RecyclerView.ViewHolder{

    public PlanNameLayoutBinding binding;

    public PlanNameViewHolder(@NonNull PlanNameLayoutBinding itemView) {
        super(itemView.getRoot());
        this.binding = itemView;
    }
}
