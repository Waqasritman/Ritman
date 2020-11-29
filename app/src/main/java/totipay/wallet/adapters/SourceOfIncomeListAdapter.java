package totipay.wallet.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import totipay.wallet.AdpatersViewHolder.SimpleTextViewHolder;
import totipay.wallet.databinding.TransferListPurposeDesignBinding;
import totipay.wallet.di.ResponseHelper.GetSourceOfIncomeListResponse;
import totipay.wallet.interfaces.OnSelectSourceOfIncome;

import java.util.List;

public class SourceOfIncomeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    List<GetSourceOfIncomeListResponse> list;
    OnSelectSourceOfIncome onSelectTransferPurpose;

    public SourceOfIncomeListAdapter(List<GetSourceOfIncomeListResponse> list,
                                     OnSelectSourceOfIncome onSelectTransferPurpose) {
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
            ((SimpleTextViewHolder) holder).binding.purposeName.setText(list.get(position).incomeName);
            ((SimpleTextViewHolder) holder).binding.imageIcon.setVisibility(View.GONE);
            ((SimpleTextViewHolder) holder).binding.getRoot().setOnClickListener(v ->
                    onSelectTransferPurpose.onSelectSourceOfIncome(list.get(position)));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
