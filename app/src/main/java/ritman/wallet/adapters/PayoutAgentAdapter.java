package ritman.wallet.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ritman.wallet.AdpatersViewHolder.SimpleTextViewHolder;
import ritman.wallet.databinding.TransferListPurposeDesignBinding;
import ritman.wallet.di.XMLdi.ResponseHelper.GetCashNetworkListResponse;
import ritman.wallet.interfaces.OnSelectPayoutAgent;

public class PayoutAgentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    OnSelectPayoutAgent onSelectPayoutAgent;
    List<GetCashNetworkListResponse> networkListResponses;

    public PayoutAgentAdapter( List<GetCashNetworkListResponse> networkListResponses , OnSelectPayoutAgent onSelectPayoutAgent
    ) {
        this.onSelectPayoutAgent = onSelectPayoutAgent;
        this.networkListResponses = networkListResponses;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TransferListPurposeDesignBinding binding =
                TransferListPurposeDesignBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SimpleTextViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SimpleTextViewHolder) {
            ((SimpleTextViewHolder) holder).binding.purposeName.setText(networkListResponses.get(position).payOutAgent);
            ((SimpleTextViewHolder) holder).binding.imageIcon.setVisibility(View.GONE);
            ((SimpleTextViewHolder) holder).binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSelectPayoutAgent.onSelectPayoutAgent(networkListResponses.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return networkListResponses.size();
    }
}
