package angoothape.wallet.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import angoothape.wallet.R;
import angoothape.wallet.bus_booking.fragments.BusSeatingFragment;
import angoothape.wallet.databinding.BusSeatingLayoutDesignBinding;
import angoothape.wallet.di.JSONdi.models.BoardingInfo;
import angoothape.wallet.interfaces.OnSelectSeat;


public class BusSeatingLayoutAdapter extends RecyclerView.Adapter<BusSeatingLayoutAdapter.ViewHolder> {

    BusSeatingFragment context;
    List<BoardingInfo> boardingInfoList;
    OnSelectSeat onSelectSeat;

    public void fillData(List<BoardingInfo> boardingInfoList) {
        this.boardingInfoList = boardingInfoList;
        notifyDataSetChanged();
    }

    public BusSeatingLayoutAdapter(BusSeatingFragment context, List<BoardingInfo> boardingInfoList, OnSelectSeat onSelectSeat) {
        this.boardingInfoList = boardingInfoList;
        this.context = context;
        this.onSelectSeat = onSelectSeat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BusSeatingLayoutDesignBinding binding =
                BusSeatingLayoutDesignBinding.inflate(LayoutInflater.
                        from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BoardingInfo boardingInfo = boardingInfoList.get(position);

        if (boardingInfo.getMale()) {
            holder.binding.seat.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        } else {
            holder.binding.seat.setBackgroundColor(context.getResources().getColor(R.color.cardview0));

        }
        Log.e("onBindViewHolder: ", String.valueOf(boardingInfo.availbility));
        Log.e("onBindViewHolder: ", String.valueOf(boardingInfo.isAvailable()));
        if (boardingInfo.availbility.equalsIgnoreCase("y")) {
            holder.binding.seat.setBackgroundColor(context.getResources().getColor(R.color.zxing_transparent));
            holder.binding.seat.setEnabled(true);
        } else {
            holder.binding.seat.setBackgroundColor(context.getResources().getColor(R.color.blue_color));
            holder.binding.seat.setEnabled(false);
        }
        if (getIsThere(boardingInfo)) {
            boardingInfo.isSelected = true;
            holder.binding.seat.setChecked(true);
        } else {
            boardingInfo.isSelected = false;
            holder.binding.seat.setChecked(false);
        }
        holder.binding.seat.setText(boardingInfo.seatNo);
        holder.binding.seat.setOnClickListener(v -> {
            if (getIsThere(boardingInfo)) {
                onSelectSeat.onUnSelectSeat(boardingInfo);
            } else {
                onSelectSeat.onSelectSeat(boardingInfo);
            }
        });

//        holder.binding.seat.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (isChecked) {
//                onSelectSeat.onSelectSeat(boardingInfo);
//            } else if (!isChecked) {
//                onSelectSeat.onUnSelectSeat(boardingInfo);
//            } else {
//
//            }
//        });


    }


    public boolean getIsThere(BoardingInfo selectedSeats) {
        for (int i = 0; i < context.viewModel.selectedSeats.size(); i++) {
            if (selectedSeats.id == context.viewModel.selectedSeats.get(i).id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        if (boardingInfoList == null) {
            return 0;
        }
        return boardingInfoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        BusSeatingLayoutDesignBinding binding;

        public ViewHolder(@NonNull BusSeatingLayoutDesignBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
