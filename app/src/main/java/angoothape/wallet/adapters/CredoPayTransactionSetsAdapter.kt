package angoothape.wallet.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import angoothape.wallet.databinding.CheckboxDesignBinding
import angoothape.wallet.di.JSONdi.restResponse.credopaymodels.GetApiData
import angoothape.wallet.interfaces.OnCredoPayApiDataSelect
import java.util.*

class CredoPayTransactionSetsAdapter(private var mList: List<GetApiData>, private var listFiltered: List<GetApiData>,
                                     private val delegate: OnCredoPayApiDataSelect) :
        RecyclerView.Adapter<CredoPayTransactionSetsAdapter.CheckBoxDesignViewHolder>(),
        Filterable {

    public var data: ArrayList<GetApiData> = ArrayList()

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckBoxDesignViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val binding: CheckboxDesignBinding = CheckboxDesignBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        return CheckBoxDesignViewHolder(binding)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: CheckBoxDesignViewHolder, position: Int) {
        val model = listFiltered[position]
        holder.binding.purposeName.text = model.name_

        holder.binding.purposeName.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                data.add(model)
            } else {
                data.removeAll { it.value_ == model.value_ }
            }
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
                if (charString.isEmpty()) listFiltered = mList else {
                    val filteredList = ArrayList<GetApiData>()
                    mList
                            .filter {
                                (it.name_.contains(constraint!!)) or
                                        (it.name_.contains(constraint))

                            }
                            .forEach { filteredList.add(it) }
                    listFiltered = filteredList

                }
                return FilterResults().apply { values = listFiltered }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                listFiltered = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<GetApiData>
                notifyDataSetChanged()
            }
        }
    }


    class CheckBoxDesignViewHolder(var binding: CheckboxDesignBinding) : RecyclerView.ViewHolder(binding.root)

}