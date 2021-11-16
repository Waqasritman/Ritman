package angoothape.wallet.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import angoothape.wallet.databinding.DistributorMerchantsNamesBinding;
import angoothape.wallet.di.JSONdi.restResponse.DistributorAgents;
import angoothape.wallet.dialogs.DistributorAgentDialog;

public class DistributorListAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<DistributorAgents> distributorAgents;
    DistributorAgentDialog distributorAgentDialog;


    public DistributorListAdapter(List<DistributorAgents> distributorAgents, DistributorAgentDialog distributorAgentDialog) {
        this.distributorAgents = distributorAgents;
        this.distributorAgentDialog = distributorAgentDialog;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DistributorMerchantsNamesBinding binding = DistributorMerchantsNamesBinding.inflate(LayoutInflater.from(parent.getContext())
                , parent, false);
        return new DistributorListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DistributorListViewHolder) {
            ((DistributorListViewHolder) holder).binding.name.setText(distributorAgents.get(position).agentName);
            ((DistributorListViewHolder) holder).binding.balance.setText("Balance: " + distributorAgents.get(position).balance + " INR");


            ((DistributorListViewHolder) holder).binding.getRoot().setOnClickListener(v -> {
                distributorAgentDialog.selectAgent(distributorAgents.get(position));
            });
        }
    }

    @Override
    public int getItemCount() {
        return distributorAgents.size();
    }


    public static class DistributorListViewHolder extends RecyclerView.ViewHolder {

        public DistributorMerchantsNamesBinding binding;

        public DistributorListViewHolder(@NonNull DistributorMerchantsNamesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
