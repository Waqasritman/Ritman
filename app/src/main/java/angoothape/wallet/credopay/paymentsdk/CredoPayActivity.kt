package angoothape.wallet.credopay.paymentsdk

import `in`.credopay.payment.sdk.CredopayPaymentConstants
import `in`.credopay.payment.sdk.PaymentActivity

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import angoothape.wallet.R
import angoothape.wallet.base.RitmanBaseActivity
import angoothape.wallet.databinding.ActivityCredoPayBinding
import angoothape.wallet.di.AESHelper
import angoothape.wallet.di.JSONdi.models.MicroTransactionTypes
import angoothape.wallet.di.JSONdi.restRequest.AERequest
import angoothape.wallet.di.JSONdi.restRequest.credoPayModel.GetTerminalCredentials
import angoothape.wallet.di.JSONdi.restRequest.credoPayModel.UpdateTerminalCredentials
import angoothape.wallet.di.JSONdi.restResponse.AEResponse
import angoothape.wallet.di.JSONdi.restResponse.credopaymodels.GetApiData
import angoothape.wallet.di.JSONdi.restResponse.credopaymodels.GetMerchantCredentials
import angoothape.wallet.di.JSONdi.restResponse.credopaymodels.MerchantTerminalResponse
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper
import angoothape.wallet.di.JSONdi.retrofit.RestClient
import angoothape.wallet.dialogs.CredoPayMerchantTerminalDialog
import angoothape.wallet.interfaces.OnCredoPayApiDataSelect
import angoothape.wallet.utils.Utils
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response

class CredoPayActivity : RitmanBaseActivity<ActivityCredoPayBinding>(), AdapterView.OnItemSelectedListener, OnCredoPayApiDataSelect {

    lateinit var binding: ActivityCredoPayBinding
    private var trType: Int = CredopayPaymentConstants.BALANCE_ENQUIRY
    var loginId = ""
    var password = ""
    var service = arrayOf(MicroTransactionTypes("Balance Enquiry", CredopayPaymentConstants.BALANCE_ENQUIRY),
            MicroTransactionTypes("Micro ATM", CredopayPaymentConstants.MICROATM))

    private val getCredentials: GetTerminalCredentials = GetTerminalCredentials()
    override fun getLayoutId(): Int {
        return R.layout.activity_credo_pay
    }

    override fun initUi(savedInstanceState: Bundle?) {
        binding.toolBar.backBtn
                .setColorFilter(ContextCompat.getColor(this,
                        R.color.colorWhite), PorterDuff.Mode.SRC_IN)
        binding.toolBar.toolBar.setBackgroundColor(resources.getColor(R.color.posRed))
        binding.toolBar.titleTxt.setText("Micro ATM")
        binding.toolBar.backBtn.setOnClickListener(View.OnClickListener { finish() })
        binding.toolBar.crossBtn.setOnClickListener { onClose() }

        binding.btnNext.setOnClickListener {
            if (isValidate()) {
                startPayment(loginId, password)
            }
        }

        binding.merchantTerminal.setOnClickListener {
            callDialog("Device")
        }

        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, service)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        with(binding.serviceType) {
            adapter = aa
            setSelection(0, false)
            onItemSelectedListener = this@CredoPayActivity
            prompt = "Select your Service"
            gravity = Gravity.CENTER
        }
    }


    private fun callDialog(title: String) {
        val dialog = CredoPayMerchantTerminalDialog(title, this)
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        dialog.show(transaction, "")
    }

    fun isValidate(): Boolean {
        if (binding.merchantTerminal.text.isEmpty()) {
            onMessage("Please select Your device")
            return false
        } else if (binding.tAmount.text.isEmpty()) {
            onMessage("Please Enter Amount")
            return false
        }
        return true
    }


    private fun getCred() {
        Utils.showCustomProgressDialog(this, false)
        val gKey = KeyHelper.getKey(sessionManager.merchantName).trim { it <= ' ' } + KeyHelper.getSKey(KeyHelper
                .getKey(sessionManager.merchantName)).trim { it <= ' ' }

        val body = RestClient.makeGSONString(getCredentials)

        Log.e("activateTerminal: ", body)

        val aeRequest = AERequest()
        aeRequest.body = AESHelper.encrypt(body, gKey.trim { it <= ' ' })

        val api = RestClient.getEKYC().getTerminalCredentials(RestClient.makeGSONRequestBody(aeRequest),
                KeyHelper.getKey(sessionManager.merchantName).trim { it <= ' ' }, KeyHelper.getSKey(KeyHelper
                .getKey(sessionManager.merchantName)).trim { it <= ' ' })

        api.enqueue(object : retrofit2.Callback<AEResponse> {
            override fun onResponse(call: Call<AEResponse>?, response: Response<AEResponse>?) {

                if (response!!.body() != null) {
                    if (response.body()!!.responseCode.equals(101)) {
                        val bodyy = AESHelper.decrypt(response.body()!!.data.body, gKey)
                        Log.e("getMerchants: ", bodyy)

                        try {
                            val gson = Gson()
                            val type = object : TypeToken<GetMerchantCredentials?>() {}.type
                            val data = gson.fromJson<GetMerchantCredentials>(bodyy, type)
                            loginId = data.Login_Id_
                            if (data.Change_Password_Value_.equals("na", true)) {
                                //pass already changed
                                password = data.Password_
                                Utils.hideCustomProgressDialog()
                            } else {
                                password = data.Password_
                                updateCred(data.Login_Id_, data.Change_Password_Value_)
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    } else {
                        Toast.makeText(this@CredoPayActivity, response.body()!!.description, Toast.LENGTH_SHORT).show()
                    }
                }
                Utils.hideCustomProgressDialog()
            }

            override fun onFailure(call: Call<AEResponse>?, t: Throwable?) {
                Utils.hideCustomProgressDialog()
                Log.e("onFailure: ", t!!.localizedMessage)
            }
        })
    }

    private fun updateCred(loginId: String, password: String) {
        Utils.showCustomProgressDialog(this, false)
        val gKey = KeyHelper.getKey(sessionManager.merchantName).trim { it <= ' ' } + KeyHelper.getSKey(KeyHelper
                .getKey(sessionManager.merchantName)).trim { it <= ' ' }

        val request = UpdateTerminalCredentials()
        request.terminal_id = getCredentials.terminal_id
        request.login_id = loginId
        request.password_ = password
        val body = RestClient.makeGSONString(request)

        Log.e("updateCred: ", body)

        val aeRequest = AERequest()
        aeRequest.body = AESHelper.encrypt(body, gKey.trim { it <= ' ' })

        val api = RestClient.getEKYC().updateTerminalCredentials(RestClient.makeGSONRequestBody(aeRequest),
                KeyHelper.getKey(sessionManager.merchantName).trim { it <= ' ' }, KeyHelper.getSKey(KeyHelper
                .getKey(sessionManager.merchantName)).trim { it <= ' ' })

        api.enqueue(object : retrofit2.Callback<AEResponse> {
            override fun onResponse(call: Call<AEResponse>?, response: Response<AEResponse>?) {
                Utils.hideCustomProgressDialog()
                if (response!!.body() != null) {
                    if (response.body()!!.responseCode.equals(101)) {
                        val bodyy = AESHelper.decrypt(response.body()!!.data.body, gKey)
                        Log.e("updateCredBody: ", bodyy)
                    } else {
                        Toast.makeText(this@CredoPayActivity, response.body()!!.description, Toast.LENGTH_SHORT).show()
                    }
                }
                Utils.hideCustomProgressDialog()
            }

            override fun onFailure(call: Call<AEResponse>?, t: Throwable?) {
                Utils.hideCustomProgressDialog()
                Log.e("onFailure: ", t!!.localizedMessage)
            }
        })
    }

    private fun startPayment(loginId: String, pass: String) {
        val amount = binding.tAmount.text.toString().toDouble()
        val newAmount = (amount * 100).toInt()
        val intent = Intent(this, PaymentActivity::class.java)
        intent.putExtra("TRANSACTION_TYPE", trType)
        intent.putExtra("DEBUG_MODE", true)
        intent.putExtra("PRODUCTION", false)
        intent.putExtra("AMOUNT", newAmount)
        intent.putExtra("LOGIN_ID", loginId)
        intent.putExtra("LOGIN_PASSWORD", pass)
        intent.putExtra("CUSTOM_FIELD1", getCredentials.terminal_id);
        startActivityForResult(intent, 1)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (position == 0) {
            trType = CredopayPaymentConstants.BALANCE_ENQUIRY
        } else if (position == 1) {
            trType = CredopayPaymentConstants.MICROATM
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("onActivityResult: ", resultCode.toString())
        if (requestCode == 1) {
            Log.e("requestCode: ", resultCode.toString())
            when (resultCode) {
                CredopayPaymentConstants.TRANSACTION_COMPLETED -> {
                    if (data != null) {
                        showToast(data)
                    }
                }
                CredopayPaymentConstants.TRANSACTION_CANCELLED -> showError(data)
                CredopayPaymentConstants.VOID_CANCELLED -> Toast.makeText(this@CredoPayActivity, "Cancelled", Toast.LENGTH_SHORT).show()
                CredopayPaymentConstants.LOGIN_FAILED -> showError(data)
                CredopayPaymentConstants.CHANGE_PASSWORD -> startPayment(loginId, password)
                CredopayPaymentConstants.CHANGE_PASSWORD_FAILED -> showError(data)
                CredopayPaymentConstants.CHANGE_PASSWORD_SUCCESS -> startPayment(loginId, password)
                CredopayPaymentConstants.BLUETOOTH_CONNECTIVITY_FAILED -> Toast.makeText(this@CredoPayActivity, "BLUETOOTH_CONNECTIVITY_FAILED", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showToast(data: Intent?) {
        val intent = Intent(this, CredoPayTransactionCompletedActivity::class.java)
        if (data != null) {
            intent.putExtra("rrn", data.getStringExtra("rrn"))
            intent.putExtra("transaction_id", data.getStringExtra("transaction_id"))
            intent.putExtra("masked_pan", data.getStringExtra("masked_pan"))
            intent.putExtra("tc", data.getStringExtra("tc"))
            intent.putExtra("tvr", data.getStringExtra("tvr"))
            intent.putExtra("tsi", data.getStringExtra("tsi"))
            intent.putExtra("approval_code", data.getStringExtra("approval_code"))
            intent.putExtra("network", data.getStringExtra("network"))
            intent.putExtra("card_application_name", data.getStringExtra("card_application_name"))
            intent.putExtra("card_holder_name", data.getStringExtra("card_holder_name"))
            intent.putExtra("app_version", data.getStringExtra("app_version"))
            intent.putExtra("card_type", data.getStringExtra("card_type"))
            intent.putExtra("account_balance", data.getStringExtra("account_balance"))
            intent.putExtra("transaction_type", data.getStringExtra("transaction_type"))

            startActivity(intent)
            finish()
        } else {
            onError("Data is null")
        }

    }

    private fun showError(data: Intent?) {
        if (data != null) {
            val error = data.getStringExtra("error")
            if (error != null) {
                onError(error)
            }
        }
    }

    override fun onSelectApiData(data: GetApiData) {

    }

    override fun onSelectTransactionData(data: List<GetApiData>) {

    }

    override fun onSelectMerchantTerminal(data: MerchantTerminalResponse) {
        if (data.Is_Terminal_Active.equals("1")) {
            getCredentials.terminal_id = data.Terminal_ID
            binding.merchantTerminal.text = data.device_model
            getCred()
        } else {
            onError("Terminal is not active")
        }
    }
}