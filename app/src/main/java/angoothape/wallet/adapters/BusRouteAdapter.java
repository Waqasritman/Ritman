package angoothape.wallet.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import angoothape.wallet.databinding.BusServiceLayoutDesignBinding;
import angoothape.wallet.databinding.BusStationLayotBinding;
import angoothape.wallet.di.JSONdi.models.RoutePickUp;
import angoothape.wallet.di.JSONdi.restResponse.GetBusStationsResponse;
import angoothape.wallet.interfaces.OnSelectRoute;

public class BusRouteAdapter extends RecyclerView.Adapter<BusRouteAdapter.ViewHolder>
        implements Filterable {


    List<RoutePickUp> list;
    List<RoutePickUp> listFiltered;
    OnSelectRoute onSelectRoute;


    public BusRouteAdapter(List<RoutePickUp> routePickUps, OnSelectRoute onSelectRoute) {
        this.list = routePickUps;
        this.onSelectRoute = onSelectRoute;
        this.listFiltered = routePickUps;
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
        holder.binding.name.setText(listFiltered.get(position).station.concat(" ")
                .concat(listFiltered.get(position).time));


        holder.binding.getRoot().setOnClickListener(v -> {
            onSelectRoute.onSelectRoute(listFiltered.get(position));
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
                    List<RoutePickUp> filteredList = new ArrayList<>();
                    for (RoutePickUp row : list) {
                        if (row.station.toLowerCase().contains(charString.toLowerCase()) ||
                                row.station.contains(charSequence)) {
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
                listFiltered = (ArrayList<RoutePickUp>) filterResults.values;
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
