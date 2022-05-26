package angoothape.wallet.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import angoothape.wallet.R
import angoothape.wallet.adapters.CredoPayApiDataAdapter
import angoothape.wallet.adapters.MerchantTerminalAdapter
import angoothape.wallet.databinding.TransferDialogPurposeBinding
import angoothape.wallet.di.AESHelper
import angoothape.wallet.di.JSONdi.restRequest.AERequest
import angoothape.wallet.di.JSONdi.restRequest.SimpleRequest
import angoothape.wallet.di.JSONdi.restRequest.credoPayModel.GetApiDataRequest
import angoothape.wallet.di.JSONdi.restResponse.AEResponse
import angoothape.wallet.di.JSONdi.restResponse.credopaymodels.GetApiData
import angoothape.wallet.di.JSONdi.restResponse.credopaymodels.MerchantTerminalResponse
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper
import angoothape.wallet.di.JSONdi.retrofit.RestClient
import angoothape.wallet.interfaces.OnCredoPayApiDataSelect
import angoothape.wallet.utils.SessionManager
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CredoPayMerchantTerminalDialog(var titleName: String,
                                     private val delegate: OnCredoPayApiDataSelect) : DialogFragment(),
        OnCredoPayApiDataSelect {

    lateinit var sessionManager: SessionManager
    lateinit var adapter: MerchantTerminalAdapter
    lateinit var binding: TransferDialogPurposeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.transfer_dialog_purpose, container, false);
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
            dialog!!.window!!.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(context)
        binding.titleOfPage.text = titleName
        binding.closeDialog.setOnClickListener {
            dialog!!.dismiss()
        }
        binding.searchView.visibility = View.VISIBLE

        //binding.searchView.addTextChangedListener(w)
        binding.searchView.addTextChangedListener(textWatcher)

        getData()
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            adapter?.filter?.filter(s)
        }
    }

    fun getData() {
        binding.progressBar.visibility = View.VISIBLE
        val gKey = KeyHelper.getKey(sessionManager.merchantName).trim { it <= ' ' } + KeyHelper.getSKey(KeyHelper
                .getKey(sessionManager.merchantName)).trim { it <= ' ' }
        val request = SimpleRequest()
        val body = RestClient.makeGSONString(request)
        val aeRequest = AERequest()
        aeRequest.body = AESHelper.encrypt(body, gKey.trim { it <= ' ' })

        val api = RestClient.getEKYC().getMerchantTerminals(RestClient.makeGSONRequestBody(aeRequest),
                KeyHelper.getKey(sessionManager.merchantName).trim { it <= ' ' }, KeyHelper.getSKey(KeyHelper
                .getKey(sessionManager.merchantName)).trim { it <= ' ' })

        api.enqueue(object : Callback<AEResponse> {
            override fun onResponse(call: Call<AEResponse>?, response: Response<AEResponse>?) {
                binding.progressBar.visibility = View.GONE
                if (response!!.body() != null) {
                    if (response.body()!!.responseCode.equals(101)) {
                        val bodyy = AESHelper.decrypt(response.body()!!.data.body, gKey)
                        Log.e("getMerchants: ", bodyy)

                        try {
                            val gson = Gson()
                            val type = object : TypeToken<List<MerchantTerminalResponse>?>() {}.type
                            val data = gson.fromJson<List<MerchantTerminalResponse>>(bodyy, type)
                            if (data.isNotEmpty()) {
                                recyclerView(data)
                            } else {
                                Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    } else {
                        Toast.makeText(context, response.body()!!.description, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<AEResponse>?, t: Throwable?) {
                binding.progressBar.visibility = View.GONE
                Log.e("onFailure: ", t!!.localizedMessage)
            }
        })
    }


    fun recyclerView(data: List<MerchantTerminalResponse>) {
        // this creates a vertical layout Manager
        binding.transferPurposeList.layoutManager = LinearLayoutManager(context)
        // This will pass the ArrayList to our Adapter
        adapter = MerchantTerminalAdapter(data, data, this)
        // Setting the Adapter with the recyclerview
        binding.transferPurposeList.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
        )
    }

    override fun onSelectApiData(data: GetApiData) {

    }

    override fun onSelectTransactionData(data: List<GetApiData>) {

    }


    override fun onSelectMerchantTerminal(data: MerchantTerminalResponse) {
        delegate.onSelectMerchantTerminal(data)
        dialog!!.dismiss()
    }
}