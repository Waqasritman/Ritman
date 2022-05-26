package angoothape.wallet.credopay.createMerchant

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.widget.RadioButton
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation
import angoothape.wallet.R
import angoothape.wallet.databinding.FragmentMerchantCompanyDetailsBinding
import angoothape.wallet.di.JSONdi.restRequest.credoPayModel.CreateMerchantRepo
import angoothape.wallet.di.JSONdi.restResponse.credopaymodels.GetApiData
import angoothape.wallet.di.JSONdi.restResponse.credopaymodels.MerchantTerminalResponse
import angoothape.wallet.dialogs.CredoPayDataDialog
import angoothape.wallet.fragments.BaseFragment
import angoothape.wallet.interfaces.OnCredoPayApiDataSelect
import angoothape.wallet.utils.DateAndTime
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import java.util.*

class MerchantCompanyDetailsFragment : BaseFragment<FragmentMerchantCompanyDetailsBinding>(),
        OnCredoPayApiDataSelect, DatePickerDialog.OnDateSetListener {

    var selectedType: String = ""

    override fun injectView() {

    }

    override fun isValidate(): Boolean {
        when {
            binding.legalName.text.isEmpty() -> {
                onMessage("Please enter company legal name")
                return false
            }
            binding.brandName.text.isEmpty() -> {
                onMessage("Please enter company brand name")
                return false
            }
            binding.regNo.text.isEmpty() -> {
                onMessage("Please enter company registration no")
                return false
            }
            binding.address.text.isEmpty() -> {
                onMessage("Please enter company address")
                return false
            }
            binding.pan.text.isEmpty() -> {
                onMessage("Please enter company PAN")
                return false
            }
            binding.pinCode.text.isEmpty() -> {
                onMessage("Please enter company pin code")
                return false
            }
            binding.businessType.text.isEmpty() -> {
                onMessage("Please enter company business type")
                return false
            }
            binding.companySince.text.isEmpty() -> {
                onMessage("Please select Company established date ")
                return false
            }
            binding.businessNature.text.isEmpty() -> {
                onMessage("Please enter company business nature")
                return false
            }
            binding.companyContactNumber.text.isEmpty() -> {
                onMessage("Please enter company contact number")
                return false
            }
            binding.companyContactEmail.text.isEmpty() -> {
                onMessage("Please enter company email address")
                return false
            }
            else -> return true
        }
    }

    override fun setUp(savedInstanceState: Bundle?) {
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == binding.yes.id) {
                CreateMerchantRepo.international_card_accepted = "1"
            } else if (checkedId == binding.no.id) {
                CreateMerchantRepo.international_card_accepted = "0"
            }
        }

        binding.nextLayout.setOnClickListener {
            if (isValidate) {
                CreateMerchantRepo.company_legal_name = binding.legalName.text.toString()
                CreateMerchantRepo.company_brand_name = binding.brandName.text.toString()
                CreateMerchantRepo.company_registration_no = binding.regNo.text.toString()
                CreateMerchantRepo.company_registered_address = binding.address.text.toString()
                CreateMerchantRepo.company_pan = binding.pan.text.toString()

                CreateMerchantRepo.company_established_year = binding.companySince.text.toString()
                CreateMerchantRepo.company_business_nature = binding.businessNature.text.toString()
                CreateMerchantRepo.company_contact_mobile = binding.companyContactNumber.text.toString()
                CreateMerchantRepo.company_contact_email = binding.companyContactEmail.text.toString()
                CreateMerchantRepo.company_contact_name = binding.companyContactName.text.toString()
                Navigation.findNavController(binding.root)
                        .navigate(R.id.merchantDetailsFragment)
            }

        }


        binding.pinCode.setOnClickListener {
            //Pincode_List
            callDialog("Pincode_List", "PinCode")

        }

        binding.companySince.setOnClickListener {
            showPickerDialog("Select Date")
        }


        binding.businessType.setOnClickListener {
            callDialog("Business_Type", "Business Type")
        }
    }

    private fun callDialog(type: String, title: String) {
        selectedType = type
        val dialog = CredoPayDataDialog(selectedType, title, this)
        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        dialog.show(transaction, "")
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_merchant_company_details
    }


    override fun onSelectApiData(data: GetApiData) {
        if (selectedType == "Pincode_List") {
            binding.pinCode.text = data.name_
            CreateMerchantRepo.company_registered_pincode = data.value_
        } else if (selectedType == "Business_Type") {
            binding.businessType.text = data.name_
            CreateMerchantRepo.company_business_type = data.value_
        }
    }

    override fun onSelectTransactionData(data: List<GetApiData>) {
        TODO("Not yet implemented")
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
        binding.companySince.text = DateAndTime.setDateFormat(year, monthOfYear, dayOfMonth, "yyyy-MM-mm")
        CreateMerchantRepo.company_established_year = binding.companySince.text.toString()
    }
}