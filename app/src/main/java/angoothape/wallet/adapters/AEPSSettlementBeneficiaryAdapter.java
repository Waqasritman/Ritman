package angoothape.wallet.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import angoothape.wallet.AdpatersViewHolder.BeneficiaryListViewHolder;
import angoothape.wallet.AdpatersViewHolder.EmptyBeneficiaryListViewHolder;
import angoothape.wallet.databinding.BeneficiaryListAdpaterDesignBinding;
import angoothape.wallet.databinding.EmptyViewBeneBinding;
import angoothape.wallet.di.JSONdi.restResponse.aepssattlement.AEPSBeneficiary;
import angoothape.wallet.di.XMLdi.ResponseHelper.GetBeneficiaryListResponse;
import angoothape.wallet.interfaces.OnSelectBeneficiary;
import angoothape.wallet.utils.SessionManager;

public class AEPSSettlementBeneficiaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements Filterable {
    private final int EMPTY_VIEW = 1;
    private final int ITEM = 0;
    OnSelectBeneficiary onSelectBeneficiary;
    List<AEPSBeneficiary> beneficiaryListResponses;
    List<AEPSBeneficiary> listFiltered;
    public SessionManager sessionManager;

    public AEPSSettlementBeneficiaryAdapter(List<AEPSBeneficiary> beneficiaryListResponses
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
            AEPSBeneficiary response = listFiltered.get(position);
            ((BeneficiaryListViewHolder) holder).binding.beneName.setText(response.beneficiaryName);


            ((BeneficiaryListViewHolder) holder).binding.beneNumber.setText(response.beneficiaryAccountNo);
            ((BeneficiaryListViewHolder) holder).binding.accountTitle.setText("Account no: ");
            ((BeneficiaryListViewHolder) holder).binding.bankLayout.setVisibility(View.GONE);
            ((BeneficiaryListViewHolder) holder).binding.getRoot().setOnClickListener(v -> onSelectBeneficiary.onSelectAEPSBeneficiary(response));

            ((BeneficiaryListViewHolder) holder).binding.deActiveBene.setVisibility(View.GONE);

        } else if (holder instanceof EmptyBeneficiaryListViewHolder) {

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
                    List<AEPSBeneficiary> filteredList = new ArrayList<>();
                    for (AEPSBeneficiary row : beneficiaryListResponses) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match

                        if (row.beneficiaryName.toLowerCase().contains(charString.toLowerCase()) ||
                                row.beneficiaryName.contains(charSequence)) {
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
                listFiltered = (ArrayList<AEPSBeneficiary>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
