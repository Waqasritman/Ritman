package ritman.wallet.KYC.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import ritman.wallet.ImagePickerActivity;
import ritman.wallet.KYC.KYCMainActivity;
import ritman.wallet.R;
import ritman.wallet.databinding.KycPictureLayoutBinding;
import ritman.wallet.di.JSONdi.restRequest.UploadKYCImage;
import ritman.wallet.di.generic_response.SimpleResponse;
import ritman.wallet.di.JSONdi.retrofit.RestClient;
import ritman.wallet.fragments.BaseFragment;
import ritman.wallet.interfaces.OnResponse;
import ritman.wallet.utils.BitmapHelper;
import ritman.wallet.utils.CheckPermission;
import ritman.wallet.utils.ProgressBar;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class KYCCustomerPicture extends BaseFragment<KycPictureLayoutBinding>
        implements OnResponse {

    private final int PERMISSION_REQUEST_CODE = 200;
    private boolean isGallerySelected = false;
    public boolean isUploaded = false;
    public static final int REQUEST_IMAGE = 100;
    UploadKYCImage uploadKYCImageRequest;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        uploadKYCImageRequest = new UploadKYCImage();
        binding.title.setText(getString(R.string.plz_upload_customer_image));
        binding.selfiImg.setOnClickListener(v -> {
            isGallerySelected = false;
            if (CheckPermission.checkCameraPermission(getContext())) {
                //permission granted already
                launchCameraIntent();
            } else {
                requestPermission();
            }
        });

        binding.imageView.setImageResource(R.drawable.customer_selfie);
        binding.documentImg.setOnClickListener(v -> {
            isGallerySelected = true;
            launchGalleryIntent();
        });

        binding.uploadImage.setOnClickListener(v -> {
            if (isUploaded) {
                uploadPicture();
            } else {
                onMessage(getString(R.string.plz_upload_customer_image));
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.kyc_picture_layout;
    }


    //gallery and camera manage code
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            if (isGallerySelected) {
                //gallery
                launchGalleryIntent();
            } else {
                launchCameraIntent();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (storageAccepted && cameraAccepted) {
                    if (isGallerySelected) {
                        //gallery
                        launchGalleryIntent();
                    } else {
                        launchCameraIntent();
                    }
                } else {
                    onMessage(getString(R.string.grant_permission));
                }
            }
        }
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(getBaseActivity(), ImagePickerActivity.class);
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
        Intent intent = new Intent(getBaseActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getBaseActivity().getContentResolver(), uri);
                    isUploaded = true;
                    uploadKYCImageRequest.Image = BitmapHelper.encodeImage(bitmap);
                    uploadKYCImageRequest.Image_Name = "CUSTOMER_IMAGE.jpg";
                    // loading profile image from local cache
                    assert uri != null;
                    loadProfile(uri.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void loadProfile(String url) {
        Glide.with(this).load(url)
                .into(binding.imageView);
    }

    ProgressBar progressBar;

    public void uploadPicture() {
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(getContext(), getString(R.string.loading_txt));

        uploadKYCImageRequest.credentials.LanguageID = Integer.parseInt(getSessionManager().getlanguageselection());
        uploadKYCImageRequest.Customer_No = ((KYCMainActivity) getBaseActivity())
                .sessionManager.getCustomerNo();

        Call<SimpleResponse> call = RestClient.get().uploadAttachment(uploadKYCImageRequest);

        call.enqueue(new retrofit2.Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                progressBar.hideProgressDialogWithTitle();

                if (response.isSuccessful()) {
                    if (response.body().responseCode.equals(101)) {
                        onSuccess();
                    } else {
                        onResponseMessage(response.body().description);
                    }
                }


            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                progressBar.hideProgressDialogWithTitle();
                Log.e("TAG", "onFailure: " + t.getLocalizedMessage());
            }
        });


    }

    @Override
    public void onSuccess() {
        ((KYCMainActivity) getBaseActivity())
                .sessionManager.setDocumentsUploaded(true);
        Navigation.findNavController(getView())
                .navigate(R.id.action_KYCCustomerPicture_to_finalKYCFragment);
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }
}
