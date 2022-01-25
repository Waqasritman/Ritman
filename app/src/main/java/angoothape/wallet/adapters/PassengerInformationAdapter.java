package angoothape.wallet.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import angoothape.wallet.databinding.BusPassengerInfoLayoutBinding;
import angoothape.wallet.databinding.BusServiceLayoutDesignBinding;
import angoothape.wallet.di.JSONdi.models.BoardingInfo;
import angoothape.wallet.interfaces.OnPassengerInformationDetails;

public class PassengerInformationAdapter extends RecyclerView.Adapter<PassengerInformationAdapter.ViewHolder> {


    List<BoardingInfo> selectedSeats;
    OnPassengerInformationDetails onPassengerInformationDetails;


    public PassengerInformationAdapter(List<BoardingInfo> selectedSeats,
                                       OnPassengerInformationDetails onPassengerInformationDetails) {
        this.selectedSeats = selectedSeats;
        this.onPassengerInformationDetails = onPassengerInformationDetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BusPassengerInfoLayoutBinding binding =
                BusPassengerInfoLayoutBinding.inflate(LayoutInflater.
                        from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.seatNo.setText(selectedSeats.get(position).seatNo);


        holder.binding.passengerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onPassengerInformationDetails.onChangeName(s.toString(), position);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        holder.binding.age.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onPassengerInformationDetails.onChangeAge(s.toString(), position);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return selectedSeats.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        BusPassengerInfoLayoutBinding binding;

        public ViewHolder(@NonNull BusPassengerInfoLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
