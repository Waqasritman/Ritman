package ritman.wallet.AdpatersViewHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ritman.wallet.databinding.CategoryDesignBinding;


public class CategoryListViewHolder extends RecyclerView.ViewHolder  {

        public CategoryDesignBinding binding;

        public CategoryListViewHolder(@NonNull CategoryDesignBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

}
