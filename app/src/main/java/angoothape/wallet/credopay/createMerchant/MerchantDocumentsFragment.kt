package angoothape.wallet.credopay.createMerchant

import android.Manifest.permission
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import angoothape.wallet.ImagePickerActivity
import angoothape.wallet.KYC.fragments.KYCBackPictureFragment
import angoothape.wallet.KYC.fragments.TakeKYCFrontIdPictureFragment
import angoothape.wallet.R
import angoothape.wallet.databinding.FragmentMerchantDocumentsBinding
import angoothape.wallet.di.AESHelper
import angoothape.wallet.di.JSONdi.restRequest.AERequest
import angoothape.wallet.di.JSONdi.restRequest.credoPayModel.CreateCredoMerchantRequest
import angoothape.wallet.di.JSONdi.restRequest.credoPayModel.CreateMerchantRepo
import angoothape.wallet.di.JSONdi.restRequest.credoPayModel.GetApiDataRequest
import angoothape.wallet.di.JSONdi.restResponse.AEResponse
import angoothape.wallet.di.JSONdi.restResponse.credopaymodels.GetApiData
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper
import angoothape.wallet.di.JSONdi.retrofit.RestClient
import angoothape.wallet.fragments.BaseFragment
import angoothape.wallet.utils.BitmapHelper
import angoothape.wallet.utils.CheckPermission
import angoothape.wallet.utils.Utils
import com.bumptech.glide.Glide
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

class MerchantDocumentsFragment : BaseFragment<FragmentMerchantDocumentsBinding>() {

    private val PERMISSION_REQUEST_CODE = 200
    private var isGallerySelected = false


    //0 - pan , 1 - adhar , 2- cheque
    var selectedId: Int = 0

    override fun injectView() {

    }

    override fun setUp(savedInstanceState: Bundle?) {


        binding.uploadImage.setOnClickListener {
            when {
                CreateMerchantRepo.merchant_document_pan_card.isEmpty() -> {
                    onMessage("Please Upload PanCard")
                }
                CreateMerchantRepo.merchant_document_aadhar_card.isEmpty() -> {
                    onMessage("Please Upload Adhaar Card")
                }
                CreateMerchantRepo.merchant_document_cancelled_cheque.isEmpty() -> {
                    onMessage("Please Upload Cancelled Cheque")
                }
                else -> {
                    createMerchant()
                }
            }
        }

        binding.panCardCamera.setOnClickListener {
            selectedId = 0
            isGallerySelected = false
            if (CheckPermission.checkCameraPermission(context)) {
                //permission granted already
                launchCameraIntent()
            } else {
                requestPermission()
            }
        }

        binding.panCardGallery.setOnClickListener {
            isGallerySelected = true
            selectedId = 0
            launchGalleryIntent()
        }

        binding.adhaarCardCamera.setOnClickListener {
            selectedId = 1
            isGallerySelected = false
            if (CheckPermission.checkCameraPermission(context)) {
                //permission granted already
                launchCameraIntent()
            } else {
                requestPermission()
            }
        }

        binding.addharCardGallery.setOnClickListener {
            isGallerySelected = true
            selectedId = 1
            launchGalleryIntent()
        }

        binding.cancelChequeCamera.setOnClickListener {
            selectedId = 2
            isGallerySelected = false
            if (CheckPermission.checkCameraPermission(context)) {
                //permission granted already
                launchCameraIntent()
            } else {
                requestPermission()
            }
        }

        binding.cancelChequeGallery.setOnClickListener {
            isGallerySelected = true
            selectedId = 2
            launchGalleryIntent()
        }
    }


    private fun createMerchant() {
        binding.progressBar.visibility = View.VISIBLE
        binding.uploadImage.visibility = View.GONE
        val gKey = KeyHelper.getKey(sessionManager.merchantName).trim { it <= ' ' } + KeyHelper.getSKey(KeyHelper
                .getKey(sessionManager.merchantName)).trim { it <= ' ' }

        val request = CreateCredoMerchantRequest()
        val body = RestClient.makeGSONString(request)
        Log.e("createMerchant: ", body)
        val aeRequest = AERequest()
        aeRequest.body = AESHelper.encrypt(body, gKey.trim { it <= ' ' })

        val api = RestClient.getEKYC().createCredoPayMerchant(RestClient.makeGSONRequestBody(aeRequest),
                KeyHelper.getKey(sessionManager.merchantName).trim { it <= ' ' }, KeyHelper.getSKey(KeyHelper
                .getKey(sessionManager.merchantName)).trim { it <= ' ' })
        api.enqueue(object : retrofit2.Callback<AEResponse> {
            override fun onResponse(call: Call<AEResponse>?, response: Response<AEResponse>?) {
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
                        binding.progressBar.visibility = View.GONE
                        binding.uploadImage.visibility = View.VISIBLE
                    } else {
                        binding.progressBar.visibility = View.GONE
                        binding.uploadImage.visibility = View.VISIBLE
                        Toast.makeText(context, response.body()!!.description, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<AEResponse>?, t: Throwable?) {
                binding.progressBar.visibility = View.GONE
                binding.uploadImage.visibility = View.VISIBLE
                Log.e("onFailure: ", t!!.localizedMessage)
            }
        })
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(permission.CAMERA, permission.READ_EXTERNAL_STORAGE, permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        } else {
            if (isGallerySelected) {
                //gallery
                launchGalleryIntent()
            } else {
                launchCameraIntent()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty()) {
                val storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (storageAccepted && cameraAccepted) {
                    if (isGallerySelected) {
                        //gallery
                        launchGalleryIntent()
                    } else {
                        launchCameraIntent()
                    }
                } else {
                    onMessage(getString(R.string.grant_permission))
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == TakeKYCFrontIdPictureFragment.REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                val uri = data!!.getParcelableExtra<Uri>("path")
                try {
                    // You can update this bitmap to your server
                    val bitmap = MediaStore.Images.Media.getBitmap(baseActivity.contentResolver, uri)

                    when (selectedId) {
                        0 -> {
                            CreateMerchantRepo.merchant_document_pan_card = BitmapHelper.encodeImage(bitmap)
                            if (uri != null) {
                                loadImage(uri.toString(), binding.imgPanCard)
                            }
                        }
                        1 -> {
                            CreateMerchantRepo.merchant_document_aadhar_card = BitmapHelper.encodeImage(bitmap)
                            if (uri != null) {
                                loadImage(uri.toString(), binding.imgAdhharCard)
                            }
                        }
                        2 -> {
                            CreateMerchantRepo.merchant_document_cancelled_cheque = BitmapHelper.encodeImage(bitmap)
                            if (uri != null) {
                                loadImage(uri.toString(), binding.imgCancelCheque)
                            }
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }


    private fun loadImage(url: String, target: ImageView) {
        Glide.with(this).load(url)
                .into(target)
    }

    private fun launchCameraIntent() {
        val intent = Intent(baseActivity, ImagePickerActivity::class.java)
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE)

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000)
        startActivityForResult(intent, TakeKYCFrontIdPictureFragment.REQUEST_IMAGE)
    }

    private fun launchGalleryIntent() {
        val intent = Intent(context, ImagePickerActivity::class.java)
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE)

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)
        startActivityForResult(intent, KYCBackPictureFragment.REQUEST_IMAGE)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_merchant_documents
    }
}