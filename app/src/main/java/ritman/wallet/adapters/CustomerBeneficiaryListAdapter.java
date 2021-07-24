package ritman.wallet.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import ritman.wallet.AdpatersViewHolder.BeneficiaryListViewHolder;
import ritman.wallet.AdpatersViewHolder.EmptyBeneficiaryListViewHolder;
import ritman.wallet.R;
import ritman.wallet.beneficairyRegistration.BeneficiaryRegistrationActivity;
import ritman.wallet.databinding.BeneficiaryListAdpaterDesignBinding;
import ritman.wallet.databinding.EmptyViewBeneBinding;
import ritman.wallet.di.XMLdi.ResponseHelper.GetBeneficiaryListResponse;
import ritman.wallet.interfaces.OnSelectBeneficiary;
import ritman.wallet.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CustomerBeneficiaryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
 implements Filterable {
    private final int EMPTY_VIEW = 1;
    private final int ITEM = 0;
    OnSelectBeneficiary onSelectBeneficiary;
    List<GetBeneficiaryListResponse> beneficiaryListResponses;
    List<GetBeneficiaryListResponse> listFiltered;
    public SessionManager sessionManager;
    String BeneName;
    Context context;

    public CustomerBeneficiaryListAdapter(List<GetBeneficiaryListResponse> beneficiaryListResponses
            , OnSelectBeneficiary onSelectBeneficiary) {
        this.beneficiaryListResponses = beneficiaryListResponses;
        this.onSelectBeneficiary = onSelectBeneficiary;
        this.listFiltered = beneficiaryListResponses;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (ITEM == viewType) {
            BeneficiaryListAdpaterDesignBinding binding =
                    BeneficiaryListAdpaterDesignBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

            return new BeneficiaryListViewHolder(binding);

        } else {
            EmptyViewBeneBinding binding =
                    EmptyViewBeneBinding.inflate(LayoutInflater.from(parent.getContext())
                            , parent, false);
            return new EmptyBeneficiaryListViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BeneficiaryListViewHolder) {
            Log.e("onBindViewHolder: ", String.valueOf(position));
            GetBeneficiaryListResponse response = listFiltered.get(position);
            ((BeneficiaryListViewHolder) holder).binding.beneName.setText(response.firstName
            .concat(" ").concat(response.lastName).concat( " (").concat(response.payOutCurrency).concat(")"));

            if (response.paymentMode.equalsIgnoreCase("bank")) {
                ((BeneficiaryListViewHolder) holder).binding.beneNumber.setText(response.accountNumber);
                ((BeneficiaryListViewHolder) holder).binding.accountTitle.setText("Account no: ");

            } else if (response.paymentMode.equalsIgnoreCase("cash")) {
                ((BeneficiaryListViewHolder) holder).binding.accountTitle.setText("Contact no: ");
                ((BeneficiaryListViewHolder) holder).binding.beneNumber.setText(response.telephone);
            }

            ((BeneficiaryListViewHolder) holder).binding.beneNumber.setText(response.accountNumber);
            ((BeneficiaryListViewHolder) holder).binding.accountTitle.setText("Account no: ");
            ((BeneficiaryListViewHolder) holder).binding.getRoot().setOnClickListener(v -> onSelectBeneficiary.onSelectBeneficiary(response));

//            if (context instanceof BeneficiaryRegistrationActivity) {
//
//            }
//            else {
//                ((BeneficiaryListViewHolder) holder).binding.imgArrow.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
//            }
        }

        else if (holder instanceof EmptyBeneficiaryListViewHolder) {

        }

    }


    @Override
    public int getItemCount() {
        if (listFiltered == null) {
            return 0;
        } else if (listFiltered.size() == 0) {
            //Return 1 here to show nothing
            return EMPTY_VIEW;
        }

        return listFiltered.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (listFiltered.isEmpty()) {
            return EMPTY_VIEW;
        }
        return super.getItemViewType(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listFiltered = beneficiaryListResponses;
                } else {
                    List<GetBeneficiaryListResponse> filteredList = new ArrayList<>();
                    for (GetBeneficiaryListResponse row : beneficiaryListResponses) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match

                            if (row.firstName.toLowerCase().contains(charString.toLowerCase()) ||
                                    row.firstName.contains(charSequence)) {
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
                listFiltered = (ArrayList<GetBeneficiaryListResponse>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
