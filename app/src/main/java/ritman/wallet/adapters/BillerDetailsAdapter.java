package ritman.wallet.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import ritman.wallet.databinding.BillerDetailsLayoutBinding;
import ritman.wallet.di.JSONdi.restResponse.BillDetailResponse;

public class BillerDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<BillDetailResponse.billlist>billlists;

    // OnGetPrepaidPlans plans;


    public BillerDetailsAdapter(List<BillDetailResponse.billlist>billlists) {
        this.billlists = billlists;
        //  this.plans = plans;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BillerDetailsLayoutBinding binding =
                BillerDetailsLayoutBinding.inflate(LayoutInflater.
                        from(parent.getContext()), parent, false);
        return new BillerPlanViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BillerPlanViewHolder) {

            ((BillerPlanViewHolder) holder).binding.customerName.setText(billlists.get(position).customer_name);
            ((BillerPlanViewHolder) holder).binding.billAmount.setText(billlists.get(position).billamount);
            ((BillerPlanViewHolder) holder).binding.netBillAmount.setText(billlists.get(position).net_billamount);
            ((BillerPlanViewHolder) holder).binding.billDate.setText(billlists.get(position).billdate);
            ((BillerPlanViewHolder) holder).binding.billDueDate.setText(billlists.get(position).billduedate);
            ((BillerPlanViewHolder) holder).binding.billStatus.setText(billlists.get(position).billstatus);
            ((BillerPlanViewHolder) holder).binding.billNumber.setText(billlists.get(position).billnumber);
            ((BillerPlanViewHolder) holder).binding.billPeriod.setText(billlists.get(position).billperiod);

            Log.e("onBindViewHolder: ", String.valueOf(position));
            ((BillerPlanViewHolder) holder).binding.getRoot().setOnClickListener(v -> {
                // plans.onSelectPlan(list.get(position));
            });
        }
    }

    @Override
    public int getItemCount() {
        return billlists.size();
    }

    public class BillerPlanViewHolder extends RecyclerView.ViewHolder {
        public BillerDetailsLayoutBinding binding;

        public BillerPlanViewHolder(@NonNull BillerDetailsLayoutBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

}

