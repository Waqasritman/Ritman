package angoothape.wallet.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import angoothape.wallet.AdpatersViewHolder.RechargePlanNameViewHolder;
import angoothape.wallet.databinding.PlanNameLayoutBinding;
import angoothape.wallet.databinding.RechargeplanLayoutBinding;
import angoothape.wallet.di.JSONdi.restResponse.RechargePlansResponse;
import angoothape.wallet.interfaces.RechargePlanName;

public class RechargePlanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<RechargePlansResponse> list;
    RechargePlanName type;
    Context context;

    public RechargePlanAdapter(Context context, List<RechargePlansResponse> list,RechargePlanName type) {
        this.list = list;
        this.context = context;
        this.type = type;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RechargeplanLayoutBinding binding =
                RechargeplanLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RechargePlanNameViewHolder(binding);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
       ((RechargePlanNameViewHolder) holder).binding.txtAmount.setText(list.get(position).amount);
       ((RechargePlanNameViewHolder) holder).binding.txtValidity.setText(list.get(position).validity);
       ((RechargePlanNameViewHolder) holder).binding.txtDescription.setText(list.get(position).plan_description);


       if(holder instanceof RechargePlanNameViewHolder) {
            ((RechargePlanNameViewHolder) holder).binding.btnBuy.setOnClickListener(v ->
                    type.onSelectRechargePlanName(list.get(position)));

//            ((RechargePlanNameViewHolder) holder).binding.txtViewDetails.setOnClickListener(v ->
//                    type.onSelectRechargePlanName(list.get(position)));
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CountryListViewHolder  extends RecyclerView.ViewHolder {
        public PlanNameLayoutBinding binding;

        public CountryListViewHolder(@NonNull PlanNameLayoutBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
