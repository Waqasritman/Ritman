package ritman.wallet.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ritman.wallet.AdpatersViewHolder.EmptyBeneficiaryListViewHolder;
import ritman.wallet.databinding.BillerPlanDesignBinding;
import ritman.wallet.databinding.EmptyViewBeneBinding;
import ritman.wallet.di.JSONdi.restResponse.PrepaidPlanResponse;
import ritman.wallet.di.XMLdi.ResponseHelper.GetPrepaidPlansResponse;
import ritman.wallet.interfaces.OnBillerPlans;
import ritman.wallet.interfaces.OnGetPrepaidPlans;

public class WrBillerPrepaidPlansAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<PrepaidPlanResponse.Plan> list;
    private final int EMPTY_VIEW = 1;
    OnBillerPlans plans;

    public WrBillerPrepaidPlansAdapter(List<PrepaidPlanResponse.Plan> list, OnBillerPlans plans) {
        this.list = list;
        this.plans = plans;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (0 == viewType) {
            BillerPlanDesignBinding binding =
                    BillerPlanDesignBinding.inflate(LayoutInflater.
                            from(parent.getContext()), parent, false);
            return new BillerPlanViewHolder(binding);
        } else {
            EmptyViewBeneBinding binding =
                    EmptyViewBeneBinding.inflate(LayoutInflater.from(parent.getContext())
                            , parent, false);
            return new EmptyBeneficiaryListViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BillerPlanViewHolder) {

            ((BillerPlanViewHolder) holder).binding.txtMrp.setText(String.valueOf(
                    list.get(position).MRP));
            ((BillerPlanViewHolder) holder).binding.txtRechargeamount.setText(
                    String.valueOf(list.get(position).rechargeAmount));
            ((BillerPlanViewHolder) holder).binding.txtValidity.setText(list.get(position).validity);
            ((BillerPlanViewHolder) holder).binding.txtBenefits.setText(list.get(position).benefits);
            ((BillerPlanViewHolder) holder).binding.txtTalktime.setText(list.get(position).talktime);
            ((BillerPlanViewHolder) holder).binding.txtDataMB.setText(list.get(position).dataMB);

            ((BillerPlanViewHolder) holder).binding.planDetailsLayout.setVisibility(View.VISIBLE);
            ((BillerPlanViewHolder) holder).binding.serviceProvider.setVisibility(View.GONE);

            Log.e("onBindViewHolder: ", String.valueOf(position));
            ((BillerPlanViewHolder) holder).binding.getRoot().setOnClickListener(v -> {
                plans.onBillerPlanSelect(list.get(position));
            });
        } else if (holder instanceof EmptyBeneficiaryListViewHolder) {
            ((EmptyBeneficiaryListViewHolder) holder).binding.errorEmpty.setText("No plan found");
        }
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        } else if (list.size() == 0) {
            //Return 1 here to show nothing
            return EMPTY_VIEW;
        }

        return list.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (list.isEmpty()) {
            return EMPTY_VIEW;
        }
        return super.getItemViewType(position);
    }


    public static class BillerPlanViewHolder extends RecyclerView.ViewHolder {
        public BillerPlanDesignBinding binding;

        public BillerPlanViewHolder(@NonNull BillerPlanDesignBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

}

