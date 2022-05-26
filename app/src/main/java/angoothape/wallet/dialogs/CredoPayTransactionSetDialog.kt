package angoothape.wallet.dialogs

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import angoothape.wallet.R
import angoothape.wallet.adapters.CredoPayTransactionSetsAdapter
import angoothape.wallet.base.BaseDialogFragment
import angoothape.wallet.databinding.CredopayMerchantDeisgnDialogBinding
import angoothape.wallet.di.AESHelper
import angoothape.wallet.di.JSONdi.restRequest.AERequest
import angoothape.wallet.di.JSONdi.restRequest.credoPayModel.GetApiDataRequest
import angoothape.wallet.di.JSONdi.restResponse.AEResponse
import angoothape.wallet.di.JSONdi.restResponse.credopaymodels.GetApiData
import angoothape.wallet.di.JSONdi.restResponse.credopaymodels.MerchantTerminalResponse
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper
import angoothape.wallet.di.JSONdi.retrofit.RestClient
import angoothape.wallet.interfaces.OnCredoPayApiDataSelect
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response

class CredoPayTransactionSetDialog(var methodType: String,
                                   var titleName: String,
                                   private val delegate: OnCredoPayApiDataSelect) :
        BaseDialogFragment<CredopayMerchantDeisgnDialogBinding>(), OnCredoPayApiDataSelect {

    lateinit var adapter: CredoPayTransactionSetsAdapter

    override fun getTheme(): Int {
        return R.style.AppTheme_NoActionBar_FullScreenDialog
    }

    override fun injectView() {
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }


    override fun getLayoutId(): Int {
        return R.layout.credopay_merchant_deisgn_dialog
    }

    override fun setUp(savedInstanceState: Bundle?) {
        binding.titleOfPage.text = titleName
        binding.closeDialog.setOnClickListener {
            dialog!!.dismiss()
        }
        binding.searchView.visibility = View.VISIBLE

        //binding.searchView.addTextChangedListener(w)
        binding.searchView.addTextChangedListener(textWatcher)

        binding.btnSearch.setOnClickListener {
            delegate.onSelectTransactionData(adapter.data)
            dialog!!.dismiss()
        }

        getData()
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            adapter.filter.filter(s)
        }
    }

    fun getData() {
        binding.progressBar.visibility = View.VISIBLE
        val gKey = KeyHelper.getKey(sessionManager.merchantName).trim { it <= ' ' } + KeyHelper.getSKey(KeyHelper
                .getKey(sessionManager.merchantName)).trim { it <= ' ' }
        val request = GetApiDataRequest()
        request.Method_Name = methodType
        val body = RestClient.makeGSONString(request)
        val aeRequest = AERequest()
        aeRequest.body = AESHelper.encrypt(body, gKey.trim { it <= ' ' })

        val api = RestClient.getEKYC().getCredoPayApiData(RestClient.makeGSONRequestBody(aeRequest),
                KeyHelper.getKey(sessionManager.merchantName).trim { it <= ' ' }, KeyHelper.getSKey(KeyHelper
                .getKey(sessionManager.merchantName)).trim { it <= ' ' })

        api.enqueue(object : retrofit2.Callback<AEResponse> {
            override fun onResponse(call: Call<AEResponse>?, response: Response<AEResponse>?) {
                binding.progressBar.visibility = View.GONE
                if (response!!.body() != null) {
                    if (response.body()!!.responseCode.equals(101)) {
                        val bodyy = AESHelper.decrypt(response.body()!!.data.body, gKey)
                        Log.e("getMerchants: ", bodyy)

                        try {
                            val gson = Gson()
                            val type = object : TypeToken<List<GetApiData>?>() {}.type
                            val data = gson.fromJson<List<GetApiData>>(bodyy, type)
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


    fun recyclerView(data: List<GetApiData>) {
        // this creates a vertical layout Manager
        binding.transferPurposeList.layoutManager = LinearLayoutManager(context)
        // This will pass the ArrayList to our Adapter
        adapter = CredoPayTransactionSetsAdapter(data, data, this)
        // Setting the Adapter with the recyclerview
        binding.transferPurposeList.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onSelectApiData(data: GetApiData) {

    }

    override fun onSelectTransactionData(data: List<GetApiData>) {
        delegate.onSelectTransactionData(adapter.data)
        dialog!!.dismiss()
    }

    override fun onSelectMerchantTerminal(data: MerchantTerminalResponse) {

    }
}