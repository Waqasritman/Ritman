package angoothape.wallet.credopay.createMerchant

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation
import angoothape.wallet.R
import angoothape.wallet.databinding.FragmentMerchantDetailsBinding
import angoothape.wallet.di.JSONdi.restRequest.credoPayModel.CreateMerchantRepo
import angoothape.wallet.di.JSONdi.restResponse.credopaymodels.GetApiData
import angoothape.wallet.di.JSONdi.restResponse.credopaymodels.MerchantTerminalResponse
import angoothape.wallet.dialogs.CredoPayDataDialog
import angoothape.wallet.dialogs.CredoPayTransactionSetDialog
import angoothape.wallet.fragments.BaseFragment
import angoothape.wallet.interfaces.OnCredoPayApiDataSelect
import angoothape.wallet.utils.DateAndTime
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import java.util.*

class MerchantDetailsFragment : BaseFragment<FragmentMerchantDetailsBinding>(),
        OnCredoPayApiDataSelect, DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {
    var service = arrayOf("Mr", "Mrs", "Ms")
    var selectedType: String = ""

    override fun injectView() {

    }


    override fun isValidate(): Boolean {
        return true
    }

    override fun setUp(savedInstanceState: Bundle?) {


        val aa = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, service)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        with(binding.merchantTitle) {
            adapter = aa
            setSelection(0, false)
            onItemSelectedListener = this@MerchantDetailsFragment
            prompt = "Select your Service"
            gravity = android.view.Gravity.CENTER
        }

        binding.radio.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == binding.own.id) {
                CreateMerchantRepo.merchant_has_own_house = "1"
            } else if (checkedId == binding.rented.id) {
                CreateMerchantRepo.merchant_has_own_house = "0"
            }
        }


        binding.nextLayout.setOnClickListener {
            if (isValidate) {
                Navigation.findNavController(binding.root)
                        .navigate(R.id.merchantDocumentsFragment)
            }

        }


        binding.merchantPincode.setOnClickListener {
            callDialog("Pincode_List", "PinCode")
        }

        binding.merchantCategory.setOnClickListener {
            callDialog("Merchant_Category", "Merchant Category")
        }

        binding.merchantType.setOnClickListener {
            callDialog("Merchant_Type", "Merchant Type")
        }

        binding.merchantTxnSets.setOnClickListener {
            callDialogTrnx()
            //CredoPayTransactionSetDialog
        }


        binding.dob.setOnClickListener {
            showPickerDialog("Select Date")
        }
    }

    private fun callDialog(type: String, title: String) {
        selectedType = type
        val dialog = CredoPayDataDialog(selectedType, title, this)
        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        dialog.show(transaction, "")
    }


    private fun callDialogTrnx() {
        selectedType = "Transaction_Sets"
        val dialog = CredoPayTransactionSetDialog(selectedType, "Transaction Sets", this)
        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        dialog.show(transaction, "")
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_merchant_details
    }

    override fun onSelectApiData(data: GetApiData) {
        when (selectedType) {
            "Merchant_Category" -> {
                binding.merchantCategory.text = data.name_
                CreateMerchantRepo.merchant_category_code_id = data.value_
            }
            "Merchant_Type" -> {
                binding.merchantType.text = data.name_
                CreateMerchantRepo.merchant_type = data.value_
            }

            "Pincode_List" -> {
                binding.merchantPincode.text = data.name_
                CreateMerchantRepo.merchant_pincode = data.value_
            }
        }
    }


    override fun onSelectTransactionData(data: List<GetApiData>) {
        var text = ""
        for (item in data) {
            if (text.isEmpty()) {
                text = item.name_.plus(",").plus(item.value_)
            } else {
                text = text.plus("|").plus(item.name_).plus(",").plus(item.value_)
            }
        }

        binding.merchantTxnSets.text = text
        CreateMerchantRepo.merchant_txn_sets = text
    }

    override fun onSelectMerchantTerminal(data: MerchantTerminalResponse) {

    }

    /**
     * dialog code for show date picker
     */
    private fun showPickerDialog(title: String) {
        val calendar = Calendar.getInstance()
        val Year = calendar[Calendar.YEAR]
        val Month = calendar[Calendar.MONTH]
        val Day = calendar[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog
                .newInstance(this, Year, Month, Day)

        datePickerDialog.isThemeDark = false
        datePickerDialog.showYearPickerFirst(true)
        datePickerDialog.accentColor = Color.parseColor("#342E78")
        datePickerDialog.locale = Locale("en")

        datePickerDialog.maxDate = calendar

        datePickerDialog.setTitle(title)
        datePickerDialog.show(parentFragmentManager, "")
        datePickerDialog.setOnCancelListener { obj: DialogInterface -> obj.dismiss() }
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        binding.dob.text = DateAndTime.setDateFormat(year, monthOfYear, dayOfMonth, "yyyy-MM-mm")
        CreateMerchantRepo.merchant_dob = binding.dob.text.toString()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        CreateMerchantRepo.merchant_title = service[position]
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }


}