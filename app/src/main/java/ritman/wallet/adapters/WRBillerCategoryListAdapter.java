package ritman.wallet.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ritman.wallet.AdpatersViewHolder.CategoryListViewHolder;
import ritman.wallet.R;
import ritman.wallet.databinding.CategoryDesignBinding;
import ritman.wallet.di.JSONdi.restResponse.GetWRBillerCategoryResponseC;
import ritman.wallet.interfaces.OnWRBillerCategoryResponse;
import ritman.wallet.utils.StringHelper;


public class WRBillerCategoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements Filterable {


    List<GetWRBillerCategoryResponseC> list;
    List<GetWRBillerCategoryResponseC> listFiltered;
    OnWRBillerCategoryResponse onWRCountryList;
    int row_index;
    Context context;

    public WRBillerCategoryListAdapter(Context context, List<GetWRBillerCategoryResponseC> filteredList, OnWRBillerCategoryResponse onWRCountryList) {
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
                    List<GetWRBillerCategoryResponseC> filteredList = new ArrayList<>();
                    for (GetWRBillerCategoryResponseC row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match

                        if (row.Name.toLowerCase().contains(charString.toLowerCase()) ||
                                row.Name.contains(charSequence)) {
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
                listFiltered = (ArrayList<GetWRBillerCategoryResponseC>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CategoryDesignBinding binding =
                CategoryDesignBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CategoryListViewHolder(binding);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CategoryListViewHolder) {
            ((CategoryListViewHolder) holder).binding.purposeName.setText(
                    StringHelper.firstLetterCap(listFiltered.get(position).Name));
            ((CategoryListViewHolder) holder).binding.purposeName
                    .setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

            Glide.with(context)
                    .load(listFiltered.get(position).IconURL)
                    .placeholder(R.drawable.ic_worldwide)
                    .into(((CategoryListViewHolder) holder).binding.iconImage);
            ((CategoryListViewHolder) holder).binding.iconImage.setColorFilter(ContextCompat.getColor(context, R.color.colorWhite));

            ((CategoryListViewHolder) holder).binding.createWallet.setCardBackgroundColor(context.getColor(R.color.colorPrimary));
            ((CategoryListViewHolder) holder).binding.getRoot().setOnClickListener(v -> {
                onWRCountryList.onSelectCategory(listFiltered.get(position));
            });
        }


    }

    @Override
    public int getItemCount() {
        return listFiltered.size();
    }
}

