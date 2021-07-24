package ritman.wallet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ritman.wallet.databinding.TransferListPurposeDesignBinding;
import ritman.wallet.di.JSONdi.restResponse.GetCasheAccomodationListResponse;
import ritman.wallet.interfaces.CasheAccomodationListInterface;

public class AccomodationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    List<GetCasheAccomodationListResponse> list;
    CasheAccomodationListInterface casheAccomodationListInterface;
    Context context;

    public AccomodationAdapter(Context context, List<GetCasheAccomodationListResponse> list,
                               CasheAccomodationListInterface casheAccomodationListInterface) {
        this.list = list;
        this.context = context;
        this.casheAccomodationListInterface = casheAccomodationListInterface;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TransferListPurposeDesignBinding binding =
                TransferListPurposeDesignBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TransferPurposeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof TransferPurposeViewHolder) {
            ((TransferPurposeViewHolder) holder).binding.purposeName.setText(list.get(position).getValue());
            ((TransferPurposeViewHolder) holder).binding.getRoot().setOnClickListener(v ->
                    casheAccomodationListInterface
                            .onSelectAccomodation(list.get(position)));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TransferPurposeViewHolder  extends RecyclerView.ViewHolder {
        public TransferListPurposeDesignBinding binding;

        public TransferPurposeViewHolder(@NonNull TransferListPurposeDesignBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}

