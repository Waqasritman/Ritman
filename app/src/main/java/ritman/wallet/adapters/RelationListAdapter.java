package ritman.wallet.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ritman.wallet.AdpatersViewHolder.SimpleTextViewHolder;
import ritman.wallet.databinding.TransferListPurposeDesignBinding;
import ritman.wallet.di.JSONdi.restResponse.RelationListResponse;
import ritman.wallet.di.XMLdi.ResponseHelper.GetRelationListResponse;
import ritman.wallet.interfaces.OnSelectRelation;

import java.util.List;

public class RelationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    List<RelationListResponse> list;
    OnSelectRelation onSelectTransferPurpose;

    public RelationListAdapter(List<RelationListResponse> list,
                               OnSelectRelation onSelectTransferPurpose) {
        this.list = list;
        this.onSelectTransferPurpose = onSelectTransferPurpose;
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
        if(holder instanceof SimpleTextViewHolder) {
            ((SimpleTextViewHolder) holder).binding.purposeName.setText(list.get(position).relationName);
            ((SimpleTextViewHolder) holder).binding.imageIcon.setVisibility(View.GONE);
            ((SimpleTextViewHolder) holder).binding.getRoot().setOnClickListener(v ->
                    onSelectTransferPurpose.onSelectRelation(list.get(position)));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
