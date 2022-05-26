package angoothape.wallet.credopay.activiteTerminal

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import angoothape.wallet.R
import angoothape.wallet.billpayment.updated_fragments.UtilityPaymentAccountNoFragment
import angoothape.wallet.databinding.FragmentActivateTerminalBinding
import angoothape.wallet.di.AESHelper
import angoothape.wallet.di.JSONdi.restRequest.AERequest
import angoothape.wallet.di.JSONdi.restRequest.credoPayModel.ActivateTerminalRequest
import angoothape.wallet.di.JSONdi.restResponse.AEResponse
import angoothape.wallet.di.JSONdi.restResponse.credopaymodels.GetApiData
import angoothape.wallet.di.JSONdi.restResponse.credopaymodels.MerchantTerminalResponse
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper
import angoothape.wallet.di.JSONdi.retrofit.RestClient
import angoothape.wallet.dialogs.CredoPayMerchantTerminalDialog
import angoothape.wallet.fragments.BaseFragment
import angoothape.wallet.interfaces.OnCredoPayApiDataSelect
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response

class ActivateTerminal : BaseFragment<FragmentActivateTerminalBinding>(), OnCredoPayApiDataSelect {

    val request = ActivateTerminalRequest()
    var imeiNumber: String = ""
    override fun injectView() {

    }


    override fun isValidate(): Boolean {
        if (binding.merchantTerminal.text.toString().isEmpty()) {
            onMessage("Select Merchant Device")
            return false
        } else if (binding.serialNo.text.toString().isEmpty()) {
            onMessage("Enter Serial No")
            return false
        }
        return true
    }

    override fun setUp(savedInstanceState: Bundle?) {

        binding.merchantTerminal.setOnClickListener {
            callDialog("Device")
        }


        binding.nextLayout.setOnClickListener {
            if (isValidate) {
                activateTerminal()
            }
        }

        val permissionCheck = ContextCompat.checkSelfPermission(baseActivity, Manifest.permission.READ_PHONE_STATE)

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(baseActivity, arrayOf(Manifest.permission.READ_PHONE_STATE),
                    1)
        } else {
            imeiNumber = getDeviceId(baseActivity)!!
        }
    }

    private fun getDeviceId(context: Context): String? {
        val deviceId: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Settings.Secure.getString(
                    context.contentResolver,
                    Settings.Secure.ANDROID_ID)
        } else {
            val mTelephony = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (mTelephony.deviceId != null) {
                mTelephony.deviceId
            } else {
                Settings.Secure.getString(
                        context.contentResolver,
                        Settings.Secure.ANDROID_ID)
            }
        }
        return deviceId
    }

    private fun activateTerminal() {
        binding.progressBar.visibility = View.VISIBLE
        val gKey = KeyHelper.getKey(sessionManager.merchantName).trim { it <= ' ' } + KeyHelper.getSKey(KeyHelper
                .getKey(sessionManager.merchantName)).trim { it <= ' ' }

        request.device_serial_number = binding.serialNo.text.toString()
        request.imei_number = imeiNumber
        val body = RestClient.makeGSONString(request)

        Log.e("activateTerminal: ", body)

        val aeRequest = AERequest()
        aeRequest.body = AESHelper.encrypt(body, gKey.trim { it <= ' ' })

        val api = RestClient.getEKYC().activateMerchantTerminal(RestClient.makeGSONRequestBody(aeRequest),
                KeyHelper.getKey(sessionManager.merchantName).trim { it <= ' ' }, KeyHelper.getSKey(KeyHelper
                .getKey(sessionManager.merchantName)).trim { it <= ' ' })

        api.enqueue(object : retrofit2.Callback<AEResponse> {
            override fun onResponse(call: Call<AEResponse>?, response: Response<AEResponse>?) {
                binding.progressBar.visibility = View.GONE
                if (response!!.body() != null) {
                    if (response.body()!!.responseCode.equals(101)) {
                        val bodyy = AESHelper.decrypt(response.body()!!.data.body, gKey)
                        Toast.makeText(context, bodyy, Toast.LENGTH_SHORT).show()
                    } else {
                        onError(response.body()!!.description)
                       // Toast.makeText(context, response.body()!!.description, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<AEResponse>?, t: Throwable?) {
                binding.progressBar.visibility = View.GONE
                Log.e("onFailure: ", t!!.localizedMessage)
            }
        })
    }

    private fun callDialog(title: String) {
        val dialog = CredoPayMerchantTerminalDialog(title, this)
        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        dialog.show(transaction, "")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                imeiNumber = getDeviceId(baseActivity)!!
            } else {
                Log.e("TAG", "Permission Needs: ")
            }
            else -> {
            }
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_activate_terminal
    }

    override fun onSelectApiData(data: GetApiData) {

    }

    override fun onSelectTransactionData(data: List<GetApiData>) {

    }

    override fun onSelectMerchantTerminal(data: MerchantTerminalResponse) {
        request.terminal_id = data.Terminal_ID
        binding.merchantTerminal.text = data.device_model
    }

}