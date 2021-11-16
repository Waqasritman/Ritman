package angoothape.wallet.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import angoothape.wallet.AdpatersViewHolder.PlanNameViewHolder;
import angoothape.wallet.R;
import angoothape.wallet.databinding.PlanNameLayoutBinding;
import angoothape.wallet.di.JSONdi.restResponse.PlanCategoriesResponse;
import angoothape.wallet.interfaces.PlanName;

public class PlanNameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    List<PlanCategoriesResponse> list;
    PlanName type;
    Context context;

    public PlanNameAdapter(Context context, List<PlanCategoriesResponse> list,PlanName type) {
        this.list = list;
        this.context = context;
        this.type = type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PlanNameLayoutBinding binding =
                PlanNameLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PlanNameViewHolder(binding);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((PlanNameViewHolder) holder).binding.planname.setText(list.get(position).PlanCategory);
        if (position == 0) {
            ((PlanNameViewHolder) holder).binding.cardview.setCardBackgroundColor(context.getColor(R.color.cardview0));
        }
        if (position == 1) {
            ((PlanNameViewHolder) holder).binding.cardview.setCardBackgroundColor(context.getColor(R.color.cardview1));
        }

        if (position == 2) {
            ((PlanNameViewHolder) holder).binding.cardview.setCardBackgroundColor(context.getColor(R.color.cardview2));
        }
        if (position == 3) {
            ((PlanNameViewHolder) holder).binding.cardview.setCardBackgroundColor(context.getColor(R.color.cardview3));
        }
        if (position == 4) {
            ((PlanNameViewHolder) holder).binding.cardview.setCardBackgroundColor(context.getColor(R.color.cardview4));
        }
        if (position == 5) {
            ((PlanNameViewHolder) holder).binding.cardview.setCardBackgroundColor(context.getColor(R.color.cardview5));
        }


//        Random rnd = new Random();
//        final int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
//        ((PlanNameViewHolder) holder).binding.cardview.setCardBackgroundColor(color);

        if(holder instanceof PlanNameViewHolder) {
            ((PlanNameViewHolder) holder).binding.getRoot().setOnClickListener(v ->
                    type.onSelectPlanName(list.get(position)));
        }

        setZoomInAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void setZoomInAnimation(View view) {
        Animation zoomIn = AnimationUtils.loadAnimation(context, R.anim.item_animation_fall_down);// animation file
        view.startAnimation(zoomIn);
    }

    public class CountryListViewHolder  extends RecyclerView.ViewHolder {
        public PlanNameLayoutBinding binding;

        public CountryListViewHolder(@NonNull PlanNameLayoutBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}

