package angoothape.wallet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.beneficairyRegistration.viewmodel.RegisterBeneficiaryViewModel;
import angoothape.wallet.databinding.ActivityUploadCashDetailsBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.SimpleRequest;
import angoothape.wallet.di.JSONdi.restRequest.UploadCashDetailsRequest;
import angoothape.wallet.di.JSONdi.restResponse.FundingBankingDetailsResponse;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.dialogs.CustomPhotoDialog;
import angoothape.wallet.dialogs.FundingBankDialog;
import angoothape.wallet.home_module.viewmodel.HomeViewModel;
import angoothape.wallet.interfaces.OnChooseImageType;
import angoothape.wallet.menumodules.viewmodels.CustomerServiceViewModel;
import angoothape.wallet.utils.BitmapHelper;
import angoothape.wallet.utils.Utils;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class UploadCashDetailsActivity extends RitmanBaseActivity<ActivityUploadCashDetailsBinding>
        implements OnChooseImageType {

    UploadCashDetailsRequest request;
    RegisterBeneficiaryViewModel registerBeneficiaryViewModel;
    HomeViewModel viewModel;
    CustomerServiceViewModel customerServiceViewModel;

    private final int PERMISSION_REQUEST_CODE = 200;
    public boolean isUploaded = false;
    public static final int REQUEST_IMAGE = 100;

    @Override
    public int getLayoutId() {
        return R.layout.activity_upload_cash_details;
    }


    public boolean isValidate() {
        if (binding.indiaBankName.getText().toString().isEmpty()) {
            onMessage("Please select bank");
            return false;
        } else if (binding.referenceNo.getText().toString().isEmpty()) {
            onMessage("Please enter reference no");
            return false;
        } else if (binding.amount.getText().toString().isEmpty()) {
            onMessage("Please enter amount");
            return false;
        } else if (!isUploaded) {
            onMessage("Please browse slip");
            return false;
        }
        return true;
    }


    @Override
    protected void initUi(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        registerBeneficiaryViewModel = new ViewModelProvider(this).get(RegisterBeneficiaryViewModel.class);
        customerServiceViewModel = new ViewModelProvider(this).get(CustomerServiceViewModel.class);
        request = new UploadCashDetailsRequest();


        binding.toolBar.backBtn.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.indiaBankName.setOnClickListener(v -> {
            getFundingBanksDetails();
        });


        binding.broswe.setOnClickListener(v -> {
            requestPermission();
        });


        binding.uploadBtn.setOnClickListener(v -> {
            if (isValidate()) {
                request.Deposit_Amount = binding.amount.getText().toString();
                request.Reference_No = binding.referenceNo.getText().toString();
                uploadCashDetails();
            }
        });


    }


    public void uploadCashDetails() {

        Utils.showCustomProgressDialog(this, false);
        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim();

        String body = RestClient.makeGSONString(request);
        AERequest aeRequest = new AERequest();
        aeRequest.body = AESHelper.encrypt(body.trim(), gKey.trim());


        viewModel.uploadCashDetails(aeRequest
                , KeyHelper.getKey(getSessionManager().getMerchantName()).trim(), KeyHelper.getSKey(KeyHelper
                        .getKey(getSessionManager().getMerchantName())).trim()).observe(this, response -> {
            Utils.hideCustomProgressDialog();
            if (response.status == Status.ERROR) {
                onError(getString(response.messageResourceId));
            } else if (response.status == Status.SUCCESS) {
                assert response.resource != null;
                onMessage(response.resource.description);
                if (response.resource.responseCode.equals(101)) {
                    Handler mHandler;
                    mHandler = new Handler();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mHandler.removeCallbacks(this);
                            finish();
                        }
                    }, 400);
                }
            }

        });


    }


    public void openPhotoSelectDialog() {
        //Do the stuff that requires permission...
        CustomPhotoDialog dialog = new CustomPhotoDialog(this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        dialog.show(transaction, "");

    }


    private void getFundingBanksDetails() {
        Utils.showCustomProgressDialog(this, false);


        String gKey = KeyHelper.getKey(sessionManager.getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(sessionManager.getMerchantName())).trim();

        SimpleRequest request = new SimpleRequest();
        String body = RestClient.makeGSONString(request);

        AERequest aeRequest = new AERequest();
        aeRequest.body = AESHelper.encrypt(body.trim(), gKey.trim());


        customerServiceViewModel.getFundingBankDetails(aeRequest, KeyHelper.getKey(sessionManager.getMerchantName()).trim(), KeyHelper.getSKey(KeyHelper
                .getKey(sessionManager.getMerchantName())).trim())
                .observe(this, response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {

                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , gKey);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<FundingBankingDetailsResponse>>() {
                                }.getType();
                                List<FundingBankingDetailsResponse> data = gson.fromJson(bodyy, type);
                                showBankList(data);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Utils.hideCustomProgressDialog();
                            if (response.resource.data != null) {
                                String bodyy = AESHelper.decrypt(response.resource.data.body
                                        , gKey);
                                Log.e("getBillDetails: ", bodyy);
                                if (!body.isEmpty()) {
                                    onError(bodyy);
                                } else {
                                    onError(response.resource.description);
                                }
                            } else {
                                onError(response.resource.description);
                            }
                        }
                    }
                });

    }


    void showBankList(List<FundingBankingDetailsResponse> data) {
        FundingBankDialog banks = new FundingBankDialog(data, this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        banks.show(transaction, "");
    }

    public void onSelectBank(FundingBankingDetailsResponse bankDetails) {
        binding.indiaBankName.setText(bankDetails.bankName);
        request.Bank_Name = bankDetails.bankName;
    }


    //gallery and camera manage code
    private void requestPermission() {
        requestPermissions(new String[]{Manifest.permission.CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (storageAccepted && cameraAccepted) {
                    openPhotoSelectDialog();
                } else {
                    onMessage(getString(R.string.grant_permission));
                }
            }
        }
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    isUploaded = true;
                    request.Deposit_Slip = BitmapHelper.encodeImage(bitmap);
                    binding.broswe.setText("Uploaded");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onClickCamera() {
        launchCameraIntent();
    }

    @Override
    public void onClickGallery() {
        launchGalleryIntent();
    }
}