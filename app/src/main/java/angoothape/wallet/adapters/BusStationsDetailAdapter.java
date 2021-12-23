package angoothape.wallet.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import angoothape.wallet.databinding.BusStationLayotBinding;
import angoothape.wallet.di.JSONdi.restResponse.GetBusStationsResponse;
import angoothape.wallet.interfaces.OnSelectBusStation;

public class BusStationsDetailAdapter extends RecyclerView.Adapter<BusStationsDetailAdapter.ViewHolder>
        implements Filterable {

    List<GetBusStationsResponse> list;
    List<GetBusStationsResponse> listFiltered;
    OnSelectBusStation onSelectBusStation;



    public BusStationsDetailAdapter(List<GetBusStationsResponse> busStationsResponseList, OnSelectBusStation onSelectBusStation) {
        this.list = busStationsResponseList;
        this.onSelectBusStation = onSelectBusStation;
        this.listFiltered = busStationsResponseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BusStationLayotBinding binding =
                BusStationLayotBinding.inflate(LayoutInflater.
                        from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.name.setText(listFiltered.get(position).name);

        holder.binding.getRoot().setOnClickListener(v -> {
            onSelectBusStation.onSelectBusStation(listFiltered.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return listFiltered.size();
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
                    List<GetBusStationsResponse> filteredList = new ArrayList<>();
                    for (GetBusStationsResponse row : list) {
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
                listFiltered = (ArrayList<GetBusStationsResponse>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        BusStationLayotBinding binding;

        public ViewHolder(@NonNull BusStationLayotBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
