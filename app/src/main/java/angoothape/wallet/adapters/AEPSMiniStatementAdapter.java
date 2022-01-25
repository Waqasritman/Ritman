package angoothape.wallet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import angoothape.wallet.R;
import angoothape.wallet.databinding.MiniStatementAepsDesignBinding;
import angoothape.wallet.di.JSONdi.restResponse.AEPS_Trans_Response;

public class AEPSMiniStatementAdapter extends RecyclerView.Adapter<AEPSMiniStatementAdapter.MiniStatementViewHolder> {

    Context context;
    List<AEPS_Trans_Response.MiniStatement> miniStatementList;

    public AEPSMiniStatementAdapter(Context context, List<AEPS_Trans_Response.MiniStatement> miniStatementList) {
        this.miniStatementList = miniStatementList;
        this.context = context;
    }

    @NonNull
    @Override
    public MiniStatementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MiniStatementAepsDesignBinding binding =
                MiniStatementAepsDesignBinding.inflate(LayoutInflater.
                        from(parent.getContext()), parent, false);
        return new MiniStatementViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MiniStatementViewHolder holder, int position) {
        if (position == 0) {
            holder.binding.mainView.setBackgroundColor(context.getResources().getColor(R.color.black));
            holder.binding.amount.setTextColor(context.getResources().getColor(R.color.white));
            holder.binding.date.setTextColor(context.getResources().getColor(R.color.white));
            holder.binding.modeOfTxn.setTextColor(context.getResources().getColor(R.color.white));
            holder.binding.drDc.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            holder.binding.mainView.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.binding.amount.setTextColor(context.getResources().getColor(R.color.black));
            holder.binding.date.setTextColor(context.getResources().getColor(R.color.black));
            holder.binding.modeOfTxn.setTextColor(context.getResources().getColor(R.color.black));
            holder.binding.drDc.setTextColor(context.getResources().getColor(R.color.black));

            holder.binding.amount.setText(miniStatementList.get(position + 1).amount);
            holder.binding.date.setText(miniStatementList.get(position + 1).date);
            holder.binding.drDc.setText(miniStatementList.get(position + 1).dc);
            holder.binding.modeOfTxn.setText(miniStatementList.get(position + 1).modeOfTxn);
        }

    }

    @Override
    public int getItemCount() {
        if (miniStatementList.size() > 0) {
            return miniStatementList.size() + 1;
        } else {
            return 0;
        }
    }

    public static class MiniStatementViewHolder extends RecyclerView.ViewHolder {
        public MiniStatementAepsDesignBinding binding;

        public MiniStatementViewHolder(@NonNull MiniStatementAepsDesignBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
