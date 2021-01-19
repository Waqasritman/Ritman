package tootipay.wallet.adapters;

import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import tootipay.wallet.AdpatersViewHolder.CountryListViewHolder;
import tootipay.wallet.R;
import tootipay.wallet.databinding.CountryDesignBinding;
import tootipay.wallet.di.ResponseHelper.WRBillerCategoryResponse;
import tootipay.wallet.interfaces.OnWRBillerCategoryResponse;
import tootipay.wallet.utils.StringHelper;

public class WRBillerCategoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements Filterable {


    List<WRBillerCategoryResponse> list;
    List<WRBillerCategoryResponse> listFiltered;
    OnWRBillerCategoryResponse onWRCountryList;

    Context context;

    public WRBillerCategoryListAdapter(Context context, List<WRBillerCategoryResponse> filteredList, OnWRBillerCategoryResponse onWRCountryList) {
        this.listFiltered = filteredList;
        this.context = context;
        this.list = filteredList;
        this.onWRCountryList = onWRCountryList;
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
                    List<WRBillerCategoryResponse> filteredList = new ArrayList<>();
                    for (WRBillerCategoryResponse row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match

                        if (row.name.toLowerCase().contains(charString.toLowerCase()) ||
                                row.name.contains(charSequence)) {
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
                listFiltered = (ArrayList<WRBillerCategoryResponse>) filterResults.values;
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
                    StringHelper.firstLetterCap(listFiltered.get(position).name));
            ((CountryListViewHolder) holder).binding.purposeName
                    .setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

            Glide.with(context)
                    .load(listFiltered.get(position).imageURL)
                    .placeholder(R.drawable.ic_worldwide)
                    .into(((CountryListViewHolder) holder).binding.iconImage);
            Log.e("list: ", String.valueOf(position));
            ((CountryListViewHolder) holder).binding.getRoot().setOnClickListener(v ->
                    onWRCountryList.onSelectCategory(listFiltered.get(position)));
        }
    }

    @Override
    public int getItemCount() {
        return listFiltered.size();
    }
}

