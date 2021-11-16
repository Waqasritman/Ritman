package angoothape.wallet.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import angoothape.wallet.databinding.CustomerServiceDesignBinding;
import angoothape.wallet.di.JSONdi.restResponse.CustomerServiceResponse;


public class CustomerServiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<CustomerServiceResponse> customerServiceResponseList;

    public CustomerServiceAdapter(List<CustomerServiceResponse> customerServiceResponseList) {
        this.customerServiceResponseList = customerServiceResponseList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomerServiceDesignBinding binding = CustomerServiceDesignBinding.inflate(LayoutInflater.from(parent.getContext())
                , parent, false);
        return new CustomerServiceViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CustomerServiceViewHolder) {
            CustomerServiceResponse response = customerServiceResponseList.get(position);
            ((CustomerServiceViewHolder) holder).binding.name.setText(response.salesOfficer);
            ((CustomerServiceViewHolder) holder).binding.contactNo.setText(response.contactNo);
            ((CustomerServiceViewHolder) holder).binding.emailId.setText(response.emailId);

        }
    }

    @Override
    public int getItemCount() {
        return customerServiceResponseList.size();
    }


    public static class CustomerServiceViewHolder extends RecyclerView.ViewHolder {
        CustomerServiceDesignBinding binding;

        public CustomerServiceViewHolder(@NonNull CustomerServiceDesignBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
