package angoothape.wallet.insurance;

import android.os.Bundle;

import angoothape.wallet.R;
import angoothape.wallet.databinding.UploadDocumentsFragmentLayoutBinding;
import angoothape.wallet.fragments.BaseFragment;

import androidx.annotation.Nullable;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import com.bumptech.glide.Glide;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import angoothape.wallet.ImagePickerActivity;
import angoothape.wallet.di.JSONdi.restRequest.UploadKYCImage;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.di.generic_response.SimpleResponse;
import angoothape.wallet.interfaces.OnResponse;
import angoothape.wallet.utils.BitmapHelper;
import angoothape.wallet.utils.CheckPermission;
import angoothape.wallet.utils.ProgressBar;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class UploadDocumentsFragment extends BaseFragment<UploadDocumentsFragmentLayoutBinding>implements OnResponse {
    String appno;
    private final int PERMISSION_REQUEST_CODE = 200;
    private boolean isGallerySelected = false;
    public boolean isUploaded = false;
    public static final int REQUEST_IMAGE = 100;
    public static final int REQUEST_IMAGE1 = 101;
    public static final int REQUEST_IMAGE2 = 102;
    String finalImage,ImageOne,ImageTwo,ImageThree;
    UploadKYCImage uploadKYCImageRequest;
    int x;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        uploadKYCImageRequest = new UploadKYCImage();
        binding.title.setText(getString(R.string.plz_upload_front_image));
        binding.selfiImg.setOnClickListener(v -> {
            isGallerySelected = false;
            if (CheckPermission.checkCameraPermission(getContext())) {
                //permission granted already
                x=1;
                launchCameraIntent();
            } else {
                requestPermission();
            }
        });
        //2nd Img
        binding.selfiImg1.setOnClickListener(v -> {
            isGallerySelected = false;
            if (CheckPermission.checkCameraPermission(getContext())) {
                //permission granted already
                x=2;
                launchCameraIntent();
            } else {
                requestPermission();
            }
        });
        //3rd Img
        binding.selfiImg2.setOnClickListener(v -> {
            isGallerySelected = false;
            if (CheckPermission.checkCameraPermission(getContext())) {
                //permission granted already
                x=3;
                launchCameraIntent();
            } else {
                requestPermission();
            }
        });

        binding.imageView.setImageResource(R.drawable.id_front);
        binding.documentImg.setOnClickListener(v -> {
            isGallerySelected = true;
            x=1;
            launchGalleryIntent();
        });

        binding.imageView1.setImageResource(R.drawable.id_front);
        binding.documentImg1.setOnClickListener(v -> {
            isGallerySelected = true;
            x=2;
            launchGalleryIntent();
        });

        binding.imageView2.setImageResource(R.drawable.id_front);
        binding.documentImg2.setOnClickListener(v -> {
            isGallerySelected = true;
            x=3;
            launchGalleryIntent();
        });


        binding.uploadImage.setOnClickListener(v -> {
            if (isUploaded) {
                uploadPicture();
            } else {
                onMessage(getString(R.string.plz_upload_front_image));
            }
        });

        //finalImage=ImageOne+"|"+ImageTwo+"|"+ImageThree;
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
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
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
        Intent intent = new Intent(getContext(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        if (x==1){
            startActivityForResult(intent, REQUEST_IMAGE);
        }else if (x==2){
            startActivityForResult(intent, REQUEST_IMAGE1);
        }else {
            startActivityForResult(intent, REQUEST_IMAGE2);
        }


    }
    private void launchGalleryIntent() {
        Intent intent = new Intent(getContext(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        if (x==1){
            startActivityForResult(intent, REQUEST_IMAGE);
        }else if (x==2){
            startActivityForResult(intent, REQUEST_IMAGE1);
        }else {
            startActivityForResult(intent, REQUEST_IMAGE2);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    isUploaded = true;
                    ImageOne = BitmapHelper.encodeImage(bitmap);
                    uploadKYCImageRequest.Image_Name = "FRONT_IMAGE.jpg";
                    // loading profile image from local cache
                    assert uri != null;
                    loadProfile(uri.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else if (requestCode == REQUEST_IMAGE1){
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    isUploaded = true;
                    ImageTwo = BitmapHelper.encodeImage(bitmap);
                    uploadKYCImageRequest.Image_Name = "FRONT_IMAGE.jpg";
                    // loading profile image from local cache
                    assert uri != null;
                    loadProfile1(uri.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }else if (requestCode == REQUEST_IMAGE2){
            assert data != null;
            Uri uri = data.getParcelableExtra("path");
            try {
                // You can update this bitmap to your server
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                isUploaded = true;
                // uploadKYCImageRequest.Image = BitmapHelper.encodeImage(bitmap);
                ImageThree = BitmapHelper.encodeImage(bitmap);
                uploadKYCImageRequest.Image_Name = "FRONT_IMAGE.jpg";
                // loading profile image from local cache
                assert uri != null;
                loadProfile2(uri.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadProfile(String url) {
        Glide.with(this).load(url)
                .into(binding.imageView);
    }

    private void loadProfile1(String url) {
        Glide.with(this).load(url)
                .into(binding.imageView1);
    }
    private void loadProfile2(String url) {
        Glide.with(this).load(url)
                .into(binding.imageView2);
    }

    ProgressBar progressBar;
    public void uploadPicture() {
        progressBar = new ProgressBar();
        progressBar.showProgressDialogWithTitle(getContext(), getString(R.string.loading_txt));

        uploadKYCImageRequest.credentials.LanguageID = Integer.parseInt(getSessionManager().getlanguageselection());
        uploadKYCImageRequest.Customer_No = getSessionManager().getCustomerNo();
        uploadKYCImageRequest.Image=ImageOne.concat(" | ").concat(ImageTwo).concat(" | ").concat(ImageThree);//+"|"+ImageTwo+"|"+ImageThree;

        Call<SimpleResponse> call = RestClient.get().uploadAttachment(uploadKYCImageRequest);

        call.enqueue(new retrofit2.Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                progressBar.hideProgressDialogWithTitle();

                if (response.isSuccessful()) {
                    assert response.body() != null;
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
    public int getLayoutId() {
        return R.layout.upload_documents_fragment_layout;
    }

    @Override
    public void onSuccess() {

    }
}
