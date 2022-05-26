package angoothape.wallet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import angoothape.wallet.AdpatersViewHolder.SimpleTextViewHolder
import angoothape.wallet.databinding.TransferListPurposeDesignBinding
import angoothape.wallet.di.JSONdi.restResponse.credopaymodels.MerchantTerminalResponse
import angoothape.wallet.interfaces.OnCredoPayApiDataSelect
import java.util.ArrayList

class MerchantTerminalAdapter(private var mList: List<MerchantTerminalResponse>,
                              private var listFiltered: List<MerchantTerminalResponse>,
                              private val delegate: OnCredoPayApiDataSelect) :
        RecyclerView.Adapter<SimpleTextViewHolder>(),
        Filterable {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleTextViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val binding: TransferListPurposeDesignBinding = TransferListPurposeDesignBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        return SimpleTextViewHolder(binding)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: SimpleTextViewHolder, position: Int) {
        val model = listFiltered[position]
        holder.binding.purposeName.text = model.device_model
        holder.binding.imageIcon.visibility = View.GONE

        holder.binding.root.setOnClickListener {
            delegate.onSelectMerchantTerminal(model)
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return listFiltered.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                listFiltered = if (charString.isEmpty()) mList else {
                    val filteredList = ArrayList<MerchantTerminalResponse>()
                    mList
                            .filter {
                                (it.device_model.contains(constraint!!)) or
                                        (it.device_model.contains(constraint))

                            }
                            .forEach { filteredList.add(it) }
                    filteredList

                }
                return FilterResults().apply { values = listFiltered }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                listFiltered = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<MerchantTerminalResponse>
                notifyDataSetChanged()
            }
        }
    }
}