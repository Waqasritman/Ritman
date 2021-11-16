package angoothape.wallet.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import angoothape.wallet.AdpatersViewHolder.SimpleTextViewHolder;
import angoothape.wallet.databinding.TransferListPurposeDesignBinding;
import angoothape.wallet.di.JSONdi.restResponse.BanksList;
import angoothape.wallet.interfaces.OnSelectBank;

public class BanksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements Filterable {

    List<BanksList> list;
    List<BanksList> listFiltered;
    OnSelectBank onSelectBank;

    public BanksAdapter(List<BanksList> list,
                        OnSelectBank onSelectBank) {
        this.list = list;
        this.onSelectBank = onSelectBank;
        this.listFiltered = list;
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
            ((SimpleTextViewHolder) holder).binding.purposeName.setText(listFiltered.get(position).bank_name);
            ((SimpleTextViewHolder) holder).binding.imageIcon.setVisibility(View.GONE);
            ((SimpleTextViewHolder) holder).binding.getRoot().setOnClickListener(v ->
                    onSelectBank.onSelectBank(listFiltered.get(position)));
        }
    }

    @Override
    public int getItemCount() {
        return listFiltered.size();
    }


    @SuppressWarnings("unchecked")
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listFiltered = list;
                } else {
                    List<BanksList> filteredList = new ArrayList<>();
                    for (BanksList row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.bank_name.toLowerCase().contains(charString.toLowerCase()) ||
                                row.bank_name.contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    listFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listFiltered = (ArrayList<BanksList>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
