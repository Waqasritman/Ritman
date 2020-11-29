package totipay.wallet.adapters;

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

import totipay.wallet.AdpatersViewHolder.CountryListViewHolder;
import totipay.wallet.R;
import totipay.wallet.databinding.CountryDesignBinding;
import totipay.wallet.di.ResponseHelper.WRCountryListResponse;
import totipay.wallet.interfaces.OnWRCountryList;
import totipay.wallet.utils.StringHelper;

public class WRCountryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements Filterable {


    List<WRCountryListResponse> list;
    List<WRCountryListResponse> listFiltered;
    OnWRCountryList onWRCountryList;
    Context context;

    public WRCountryListAdapter(Context context, List<WRCountryListResponse> filteredList, OnWRCountryList onWRCountryList) {
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
                    List<WRCountryListResponse> filteredList = new ArrayList<>();
                    for (WRCountryListResponse row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match

                        if (row.countryName.toLowerCase().contains(charString.toLowerCase()) ||
                                row.countryCode.contains(charSequence)) {
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
                listFiltered = (ArrayList<WRCountryListResponse>) filterResults.values;
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
                    StringHelper.firstLetterCap(listFiltered.get(position).countryName));
            ((CountryListViewHolder) holder).binding.purposeName
                    .setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

            ((CountryListViewHolder) holder).binding.countryCode.setText(listFiltered.get(position).countryCode);
            Glide.with(context)
                    .load(listFiltered.get(position).image_URL)
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
