package totipay.wallet.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import totipay.wallet.Mobile_Top_Up.helpers.MobileTopUpType;
import totipay.wallet.databinding.BillerPlanDesignBinding;
import totipay.wallet.di.ResponseHelper.GetPrepaidPlansResponse;
import totipay.wallet.di.ResponseHelper.WRBillerPlanResponse;
import totipay.wallet.interfaces.OnBillerPlans;
import totipay.wallet.interfaces.OnGetPrepaidPlans;

public class WrBillerPrepaidPlansAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<GetPrepaidPlansResponse> list;
    OnGetPrepaidPlans plans;


    public WrBillerPrepaidPlansAdapter(List<GetPrepaidPlansResponse> list, OnGetPrepaidPlans plans) {
        this.list = list;
        this.plans = plans;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BillerPlanDesignBinding binding =
                BillerPlanDesignBinding.inflate(LayoutInflater.
                        from(parent.getContext()), parent, false);
        return new BillerPlanViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BillerPlanViewHolder) {

            ((BillerPlanViewHolder) holder).binding.billerName.setText(list.get(position).rechargeSubType);
            ((BillerPlanViewHolder) holder).binding.payableAmount.
                    setText(list.get(position).rechargeAmount);
            ((BillerPlanViewHolder) holder).binding.description.setText(list.get(position).benefits);
            ((BillerPlanViewHolder) holder).binding.mobilePlanLayout.setVisibility(View.VISIBLE);
            ((BillerPlanViewHolder) holder).binding.serviceProvider.setVisibility(View.GONE);

            Log.e("onBindViewHolder: ", String.valueOf(position));
            ((BillerPlanViewHolder) holder).binding.getRoot().setOnClickListener(v -> {
                plans.onSelectPlan(list.get(position));
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BillerPlanViewHolder extends RecyclerView.ViewHolder {
        public BillerPlanDesignBinding binding;

        public BillerPlanViewHolder(@NonNull BillerPlanDesignBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

}

