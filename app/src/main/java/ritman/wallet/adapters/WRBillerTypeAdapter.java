package ritman.wallet.adapters;

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

import ritman.wallet.AdpatersViewHolder.CountryListViewHolder;
import ritman.wallet.R;
import ritman.wallet.databinding.CountryDesignBinding;
import ritman.wallet.di.JSONdi.restResponse.GetWRBillerTypeResponse;
import ritman.wallet.di.XMLdi.ResponseHelper.WRBillerTypeResponse;
import ritman.wallet.interfaces.OnWRBillerType;
import ritman.wallet.utils.StringHelper;

public class WRBillerTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements Filterable {

    List<GetWRBillerTypeResponse> list;
    List<GetWRBillerTypeResponse> listFiltered;
    OnWRBillerType type;
    Context context;

    public WRBillerTypeAdapter(Context context, List<GetWRBillerTypeResponse> listFiltered, OnWRBillerType type) {
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
                    List<GetWRBillerTypeResponse> filteredList = new ArrayList<>();
                    for (GetWRBillerTypeResponse row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match

                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
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
                listFiltered = (ArrayList<GetWRBillerTypeResponse>) filterResults.values;
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
                    StringHelper.firstLetterCap(listFiltered.get(position).getName()));
            ((CountryListViewHolder) holder).binding.purposeName
                    .setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
            Glide.with(context)
                    .load(list.get(position).getURL())
                    .placeholder(R.drawable.ic_utilities)
                    .into(((CountryListViewHolder) holder).binding.iconImage);


            Log.e("list: ", String.valueOf(position));
            ((CountryListViewHolder) holder).binding.getRoot().setOnClickListener(v ->
                    type.onBillerTypeSelect(listFiltered.get(position)));
        }
    }

    @Override
    public int getItemCount() {
        return listFiltered.size();
    }
}
