package totipay.wallet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import totipay.wallet.AdpatersViewHolder.UserViewHolder;
import totipay.wallet.R;
import totipay.wallet.databinding.RecyelerHomeDesignBinding;
import totipay.wallet.di.ResponseHelper.GetCustomerWalletDetailsResponse;
import totipay.wallet.home_module.NewDashboardActivity;
import totipay.wallet.interfaces.OnCustomerWalletDetails;
import totipay.wallet.utils.BitmapHelper;

public class UserAccountsHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    List<GetCustomerWalletDetailsResponse> list;
    OnCustomerWalletDetails customerWalletDetails;
    Context context;

    public UserAccountsHomeAdapter(Context context, List<GetCustomerWalletDetailsResponse> list) {
        this.list = list;
        this.context = context;
    }


    public UserAccountsHomeAdapter(Context context, List<GetCustomerWalletDetailsResponse> list, OnCustomerWalletDetails customerWalletDetails) {
        this.list = list;
        this.context = context;
        this.customerWalletDetails = customerWalletDetails;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyelerHomeDesignBinding binding = RecyelerHomeDesignBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UserViewHolder) {
            ((UserViewHolder) holder).binding.balance.setText(list.get(position).balance);
            ((UserViewHolder) holder).binding.currencyName.setText(
                    list.get(position).currencyFullName.concat(" ")
                            .concat("(").concat(list.get(position).currencyShortName).concat(")")
            );


            Glide.with(context)
                    .load(list.get(position).imageURL)
                    .placeholder(R.drawable.ic_united_kingdom)
                    .into(((UserViewHolder) holder).binding.dummyImage);

            ((UserViewHolder) holder).binding.getRoot().setOnClickListener(v -> {
                if (customerWalletDetails != null) {
                    customerWalletDetails.onSelectWallet(list.get(position));
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
