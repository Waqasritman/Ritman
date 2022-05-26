package angoothape.wallet.credopay

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import angoothape.wallet.R
import angoothape.wallet.databinding.FragmentMerchnatAddTerminalBinding
import angoothape.wallet.di.AESHelper
import angoothape.wallet.di.JSONdi.restRequest.AERequest
import angoothape.wallet.di.JSONdi.restRequest.credoPayModel.CreateCredoMerchantRequest
import angoothape.wallet.di.JSONdi.restRequest.credoPayModel.CreateMerchantRepo
import angoothape.wallet.di.JSONdi.restRequest.credoPayModel.MerchantAddTerminalRequest
import angoothape.wallet.di.JSONdi.restResponse.AEResponse
import angoothape.wallet.di.JSONdi.restResponse.credopaymodels.GetApiData
import angoothape.wallet.di.JSONdi.restResponse.credopaymodels.MerchantTerminalResponse
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper
import angoothape.wallet.di.JSONdi.retrofit.RestClient
import angoothape.wallet.dialogs.CredoPayDataDialog
import angoothape.wallet.fragments.BaseFragment
import angoothape.wallet.interfaces.OnCredoPayApiDataSelect
import angoothape.wallet.utils.Utils
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response

class MerchantAddTerminalFragment : BaseFragment<FragmentMerchnatAddTerminalBinding>(),
        OnCredoPayApiDataSelect {

    var selectedType: String = ""
    lateinit var addTerminalRequest: MerchantAddTerminalRequest

    override fun isValidate(): Boolean {
        when {
            binding.locality.text.isEmpty() -> {
                onMessage("Please enter locality")
                return false
            }
            binding.city.text.isEmpty() -> {
                onMessage("Please enter city")
                return false
            }
            binding.address.text.isEmpty() -> {
                onMessage("Please enter address")
                return false
            }
            binding.pinCode.text.isEmpty() -> {
                onMessage("Please select pin code")
                return false
            }
            binding.terminalType.text.isEmpty() -> {
                onMessage("Please select terminal type")
                return false
            }
            binding.deviceModel.text.isEmpty() -> {
                onMessage("Please select device model")
                return false
            }
        }
        return true
    }

    override fun injectView() {

    }

    override fun setUp(savedInstanceState: Bundle?) {
        addTerminalRequest = MerchantAddTerminalRequest()

        binding.pinCode.setOnClickListener {
            callDialog("Pincode_List", "PinCode")
        }

        binding.terminalType.setOnClickListener {
            callDialog("Terminal_Type", "Terminal Type")
        }

        binding.deviceModel.setOnClickListener {
            callDialog("Device_Model", "Device Model")
        }

        binding.nextLayout.setOnClickListener {
            if (isValidate) {
                addTerminal()
            }
        }
    }


    private fun callDialog(type: String , title:String) {
        selectedType = type
        val dialog = CredoPayDataDialog(selectedType, title,this)
        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        dialog.show(transaction, "")
    }


    fun addTerminal() {
        addTerminalRequest.address = binding.address.text.toString()
        addTerminalRequest.location = binding.locality.text.toString().plus(" ")
                .plus(binding.city.text.toString())
        Utils.showCustomProgressDialog(context, false)
        val gKey = KeyHelper.getKey(sessionManager.merchantName).trim { it <= ' ' } + KeyHelper.getSKey(KeyHelper
                .getKey(sessionManager.merchantName)).trim { it <= ' ' }

        val body = RestClient.makeGSONString(addTerminalRequest)
        Log.e("createMerchant: ", body)
        val aeRequest = AERequest()
        aeRequest.body = AESHelper.encrypt(body, gKey.trim { it <= ' ' })

        val api = RestClient.getEKYC().addCredoPayMerchantTerminal(RestClient.makeGSONRequestBody(aeRequest),
                KeyHelper.getKey(sessionManager.merchantName).trim { it <= ' ' }, KeyHelper.getSKey(KeyHelper
                .getKey(sessionManager.merchantName)).trim { it <= ' ' })

        api.enqueue(object : retrofit2.Callback<AEResponse> {
            override fun onResponse(call: Call<AEResponse>, response: Response<AEResponse>) {
                Utils.hideCustomProgressDialog()
                if (response!!.body() != null) {
                    if (response.body()!!.responseCode.equals(101)) {
                        val bodyy = AESHelper.decrypt(response.body()!!.data.body, gKey)
                        Log.e("getMerchants: ", bodyy)

                        try {
                            val gson = Gson()
                            val type = object : TypeToken<String?>() {}.type
                            val data = gson.fromJson<String>(bodyy, type)
                            Toast.makeText(context, data, Toast.LENGTH_SHORT).show()
                            baseActivity.finish()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    } else {
                        Toast.makeText(context, response.body()!!.description, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<AEResponse>, t: Throwable) {
                Utils.hideCustomProgressDialog()
            }

        })

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_merchnat_add_terminal
    }

    override fun onSelectApiData(data: GetApiData) {
        when (selectedType) {
            "Device_Model" -> {
                binding.deviceModel.text = data.name_
                addTerminalRequest.device_model = data.value_
            }
            "Pincode_List" -> {
                binding.pinCode.text = data.name_
                addTerminalRequest.pincode = data.value_
            }
            "Terminal_Type" -> {
                binding.terminalType.text = data.name_
                addTerminalRequest.terminal_type = data.value_
            }

        }
    }

    override fun onSelectTransactionData(data: List<GetApiData>) {

    }

    override fun onSelectMerchantTerminal(data: MerchantTerminalResponse) {

    }


}