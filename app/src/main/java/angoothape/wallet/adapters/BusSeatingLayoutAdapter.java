package angoothape.wallet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import angoothape.wallet.R;
import angoothape.wallet.databinding.BusSeatingLayoutDesignBinding;
import angoothape.wallet.di.JSONdi.models.BoardingInfo;
import angoothape.wallet.interfaces.OnSelectSeat;


public class BusSeatingLayoutAdapter extends RecyclerView.Adapter<BusSeatingLayoutAdapter.ViewHolder> {

    Context context;
    List<BoardingInfo> boardingInfoList;
    OnSelectSeat onSelectSeat;


    public BusSeatingLayoutAdapter(Context context, List<BoardingInfo> boardingInfoList, OnSelectSeat onSelectSeat) {
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

        holder.binding.seat.setText(boardingInfo.seatNo);


        holder.binding.seat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                onSelectSeat.onSelectSeat(boardingInfo);
            } else if(!isChecked) {
                onSelectSeat.onUnSelectSeat(boardingInfo);
            } else {

            }
        });
    }

    @Override
    public int getItemCount() {
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
