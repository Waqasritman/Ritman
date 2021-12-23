package angoothape.wallet.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import angoothape.wallet.databinding.BusServiceLayoutDesignBinding;
import angoothape.wallet.databinding.BusStationLayotBinding;
import angoothape.wallet.di.JSONdi.restResponse.Services;
import angoothape.wallet.interfaces.OnSelectBusService;

public class BusServicesAdapter extends RecyclerView.Adapter<BusServicesAdapter.ViewHolder> {

    String endStation;
    String startStation;
    List<Services> servicesList;
    OnSelectBusService onSelectBusService;


    public BusServicesAdapter(String startStation, String endStation, List<Services> servicesList
            , OnSelectBusService onSelectBusService) {
        this.endStation = endStation;
        this.startStation = startStation;
        this.servicesList = servicesList;
        this.onSelectBusService = onSelectBusService;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BusServiceLayoutDesignBinding binding =
                BusServiceLayoutDesignBinding.inflate(LayoutInflater.
                        from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Services service = servicesList.get(position);
        holder.binding.agentName.setText(service.traveler_Agent_Name);
        holder.binding.busType.setText(service.bus_type);
        holder.binding.journeyDate.setText(service.jdate);
        holder.binding.startingCity.setText(startStation);
        holder.binding.endCity.setText(endStation);
        holder.binding.startingTime.setText(service.start_time);
        holder.binding.endTime.setText(service.arr_Time);
        holder.binding.availSeats.setText(service.available_seats.concat(" seats available"));


        holder.binding.select.setOnClickListener(v -> {
            onSelectBusService.onSelectBusService(service);
        });
    }

    @Override
    public int getItemCount() {
        return servicesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public BusServiceLayoutDesignBinding binding;

        public ViewHolder(@NonNull BusServiceLayoutDesignBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
