package angoothape.wallet.adapters;

import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import angoothape.wallet.AdpatersViewHolder.CountryListViewHolder;
import angoothape.wallet.R;
import angoothape.wallet.databinding.CountryDesignBinding;
import angoothape.wallet.di.JSONdi.restResponse.IINListResponse;
import angoothape.wallet.interfaces.InterfaceIINList;
import angoothape.wallet.utils.StringHelper;

public class IINTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements Filterable {

    List<IINListResponse> list;
    List<IINListResponse> listFiltered;
    InterfaceIINList type;
    Context context;

    public IINTypeAdapter(Context context, List<IINListResponse> listFiltered, InterfaceIINList type) {
        this.context = context;
        this.listFiltered = listFiltered;
        this.list = listFiltered;
        this.type = type;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listFiltered = list;
                } else {
                    List<IINListResponse> filteredList = new ArrayList<>();
                    for (IINListResponse row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match

                        if (row.Issuer_Bank_Name.toLowerCase().contains(charString.toLowerCase())) {
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
                listFiltered = (ArrayList<IINListResponse>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CountryDesignBinding binding =
                CountryDesignBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CountryListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CountryListViewHolder) {
            ((CountryListViewHolder) holder).binding.purposeName.setText(
                    StringHelper.firstLetterCap(listFiltered.get(position).Issuer_Bank_Name));
            ((CountryListViewHolder) holder).binding.purposeName
                    .setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
           /* Glide.with(context)
                    .load(list.get(position).getURL())
                    .placeholder(R.drawable.ic_utilities)
                    .into(((CountryListViewHolder) holder).binding.iconImage);
*/
            ((CountryListViewHolder) holder).binding.iconImage.setImageResource(R.drawable.ic_bank);
            Log.e("list: ", String.valueOf(position));
            ((CountryListViewHolder) holder).binding.getRoot().setOnClickListener(v ->
                    type.onSelectIINList(listFiltered.get(position)));
        }
    }

    @Override
    public int getItemCount() {
        return listFiltered.size();
    }
}
