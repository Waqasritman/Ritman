package angoothape.wallet.adapters;

import android.content.Context;
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

import angoothape.wallet.AdpatersViewHolder.CountryListViewHolder;
import angoothape.wallet.R;
import angoothape.wallet.databinding.CountryDesignBinding;
import angoothape.wallet.di.JSONdi.restResponse.GetWRCountryListResponseC;
import angoothape.wallet.interfaces.OnWRCountryList;

public class WRCountryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements Filterable {


    List<GetWRCountryListResponseC> list;
    List<GetWRCountryListResponseC> listFiltered;
    OnWRCountryList onWRCountryList;
    Context context;

    public WRCountryListAdapter(Context context, List<GetWRCountryListResponseC> filteredList, OnWRCountryList onWRCountryList) {
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
                    List<GetWRCountryListResponseC> filteredList = new ArrayList<>();
                    for (GetWRCountryListResponseC row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match

                        if (row.getCountry_Name().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getCountry_Code().contains(charSequence)) {
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
                listFiltered = (ArrayList<GetWRCountryListResponseC>) filterResults.values;
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
                    (listFiltered.get(position).getCountry_Name()));


            ((CountryListViewHolder) holder).binding.countryCode.setText(listFiltered.get(position).getCountry_Code());

            Glide.with(context)
                    .load(listFiltered.get(position).getURL())
                    .placeholder(R.drawable.ic_worldwide)
                    .into(((CountryListViewHolder) holder).binding.iconImage);
            Log.e("list: ", String.valueOf(position));
            ((CountryListViewHolder) holder).binding.getRoot().setOnClickListener(v ->
                    onWRCountryList.onWRSelectCountry(listFiltered.get(position)));
        }
    }

    @Override
    public int getItemCount() {
        return listFiltered.size();
    }
}
