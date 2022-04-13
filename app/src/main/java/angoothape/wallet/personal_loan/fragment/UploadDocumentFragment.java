package angoothape.wallet.personal_loan.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import angoothape.wallet.R;
import angoothape.wallet.databinding.UploadDocumentFragmentBinding;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.UploadDocumentsRequest;
import angoothape.wallet.dialogs.SingleButtonMessageDialog;
import angoothape.wallet.fragments.BaseFragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import angoothape.wallet.ImagePickerActivity;
import angoothape.wallet.di.JSONdi.restRequest.UploadKYCImage;
import angoothape.wallet.interfaces.OnDecisionMade;
import angoothape.wallet.interfaces.OnResponse;
import angoothape.wallet.personal_loan.viewmodels.PersonalLoanViewModel;
import angoothape.wallet.utils.BitmapHelper;
import angoothape.wallet.utils.CheckPermission;
import angoothape.wallet.utils.Utils;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class UploadDocumentFragment extends BaseFragment<UploadDocumentFragmentBinding> implements OnResponse, OnDecisionMade,
        AdapterView.OnItemSelectedListener {

    private final int PERMISSION_REQUEST_CODE = 200;
    private boolean isGallerySelected = false;
    public boolean isUploaded = false;
    public static final int REQUEST_IMAGE1 = 101;
    public static final int REQUEST_IMAGE2 = 102;
    public static final int REQUEST_IMAGE3 = 103;

    public static final int REQUEST_IMAGE4 = 104;
    public static final int REQUEST_IMAGE5 = 105;
    public static final int REQUEST_IMAGE6 = 106;
    public static final int REQUEST_IMAGE7 = 107;
    public static final int REQUEST_PDF = 108;
    public static final int REQUEST_PDF1 = 109;
    public static final int REQUEST_PDF2 = 110;
    public static final int REQUEST_PDF3 = 111;
    public static final int REQUEST_PDF4 = 112;
    public static final int REQUEST_PDF5 = 113;
    public static final int REQUEST_PDF6 = 114;
    public static final int REQUEST_PDF7 = 115;

    String ImageOne = "";
    String ImageTwo = "";
    String ImageThree = "";
    String ImageFour = "";
    String ImageFive = "";
    String ImageSix = "";
    String ImageSeven = "";

    String AddPass = "";
    String SlipPass = "";
    String PanPass = "";
    String BankStatmentPass = "";
    String EmpPass = "";
    String Adharpass = "";

    String ImageNameOne = "";
    String ImageNameTwo = "";
    String ImageNameThree = "";
    String ImageNameFour = "";
    String ImageNameFive = "";
    String ImageNameSix = "";
    String ImageNameSeven = "";
    String customer_no;
    UploadKYCImage uploadKYCImageRequest;
    int x;
    Uri uri;
    String customerId;

    //String[] arrayAddProof = {"Select Address proof", "Utility Bill", "Telephone Bill", "Passport", "Voter Id", "Driving License", "Rental Agreement", "Aadhar Card Address"};

    String[] arrayAddProof = {"Select Address proof", "utilityBill", "telephoneBill", "passport", "voterId", "drivingLicense", "rentalAgreement", "aadharcardAddress"};


    String selected_address_proof = "";
    String SelectedImageType1, SelectedImageType2, SelectedImageType3, SelectedImageType4, SelectedImageType5, SelectedImageType6,
            SelectedImageType7;

    String path, displayName;
    Cursor cursor;

    PersonalLoanViewModel viewModel;
    final CharSequence[] options = {"Upload Image", "Upload PDF", "Cancel"};
    Bitmap selectedImage;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(PersonalLoanViewModel.class);
        // viewModel = ((PLActivity) getBaseActivity()).viewModel;

        uploadKYCImageRequest = new UploadKYCImage();
        binding.title.setText(getString(R.string.plz_upload_front_image));

        binding.addProofSpinner.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, arrayAddProof);

        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.addProofSpinner.setAdapter(aa);

        SharedPreferences sp = getActivity().getSharedPreferences("customerId", MODE_PRIVATE);
        customerId = sp.getString("customerId", "");

        if (SelectedImageType1 != null && ImageOne != null) {
            SelectedImageType1 = getArguments().getString("SelectedImageType1");
            ImageOne = getArguments().getString("ImageOne");
        }
        binding.addProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGallerySelected = false;
                if (CheckPermission.checkCameraPermission(getActivity())) {
                    //permission granted already
                    x = 1;
                    launchCameraIntent();
                } else {
                    requestPermission();
                }
            }
        });

//2nd Img
        binding.selfiImg.setOnClickListener(v -> {
            isGallerySelected = false;
            if (CheckPermission.checkCameraPermission(getActivity())) {
                //permission granted already
                x = 2;
                launchCameraIntent();
            } else {
                requestPermission();
            }
        });

        //2nd Img
        binding.selfiImg1.setOnClickListener(v -> {
            isGallerySelected = false;
            if (CheckPermission.checkCameraPermission(getActivity())) {
                //permission granted already
                x = 3;
                launchCameraIntent();
            } else {
                requestPermission();
            }
        });

        //3rd Img
        binding.selfiImg2.setOnClickListener(v -> {
            isGallerySelected = false;
            if (CheckPermission.checkCameraPermission(getActivity())) {
                //permission granted already
                x = 4;
                launchCameraIntent();
            } else {
                requestPermission();
            }
        });

//4th Img
        binding.selfiImgStatment.setOnClickListener(v -> {
            isGallerySelected = false;
            if (CheckPermission.checkCameraPermission(getActivity())) {
                //permission granted already
                x = 5;
                launchCameraIntent();
            } else {
                requestPermission();
            }
        });
//5th Img
        binding.selfiImgEmp.setOnClickListener(v -> {
            isGallerySelected = false;
            if (CheckPermission.checkCameraPermission(getActivity())) {
                //permission granted already
                x = 6;
                launchCameraIntent();
            } else {
                requestPermission();
            }
        });

//6th Img
        binding.selfiImgAdhar.setOnClickListener(v -> {
            isGallerySelected = false;
            if (CheckPermission.checkCameraPermission(getActivity())) {
                //permission granted already
                x = 7;
                launchCameraIntent();
            } else {
                requestPermission();
            }
        });

        binding.addproofImg.setOnClickListener(v -> {
            String S = selectImage(getActivity());

            //  ImageName.put("ImageNane1", S);
            isGallerySelected = true;
            x = 1;
            // launchGalleryIntent();
        });

        // binding.imgProfilePic.setImageResource(R.drawable.id_front);

        binding.documentImg.setOnClickListener(v -> {
            isGallerySelected = true;
            x = 2;
            // selectImage(getActivity());
            launchGalleryIntent();
        });

        //binding.imgPaySlip.setImageResource(R.drawable.id_front);

        binding.documentImg1.setOnClickListener(v -> {
            String S = selectImage(getActivity());

            isGallerySelected = true;
            x = 3;
            //  launchGalleryIntent();
        });

        //  binding.imgPancard.setImageResource(R.drawable.id_front);
        binding.documentImg2.setOnClickListener(v -> {
            String S = selectImage(getActivity());

            isGallerySelected = true;
            x = 4;
            // launchGalleryIntent();
        });

        binding.documentImgStatment.setOnClickListener(v -> {
            String S = selectImage(getActivity());

            isGallerySelected = true;
            x = 5;
            // launchGalleryIntent();
        });

        binding.documentImgEmp.setOnClickListener(v -> {
            String S = selectImage(getActivity());

            isGallerySelected = true;
            x = 6;
            //  launchGalleryIntent();
        });

        binding.documentAdhar.setOnClickListener(v -> {
            String S = selectImage(getActivity());

            isGallerySelected = true;
            x = 7;
            // launchGalleryIntent();
        });

        binding.uploadImage.setOnClickListener(v -> {
            customer_no = binding.edtCustomerNo.getText().toString();
            if (binding.edtCustomerNo.getText().toString().equals("")) {
                onMessage("Please enter Application Number (Registered Customer No.)");
                binding.edtCustomerNo.requestFocus();
            } else if (!binding.edtCustomerNo.getText().toString().equals(customerId)) {
                onMessage("Please enter valid Application Number (Registered Customer No.)");
                binding.edtCustomerNo.requestFocus();
            } else if (binding.addProofSpinner.getSelectedItemId() == 0) {
                onMessage("Please select Address Proof");
                binding.addProofSpinner.requestFocus();
            } else if (binding.imgAddressProof.getDrawable() == null) {
                onMessage("Please upload atleast one Address proof ");
                binding.imgAddressProof.requestFocus();
            } else if (binding.imgProfilePic.getDrawable() == null) {
                onMessage("Please upload your Profile Picture ");
                binding.imgProfilePic.requestFocus();
            } else if (binding.imgPaySlip.getDrawable() == null) {
                onMessage("Please upload your Pay Slip ");
                binding.imgPaySlip.requestFocus();
            } else if (binding.imgPancard.getDrawable() == null) {
                onMessage("Please upload your PanCard ");
                binding.imgPancard.requestFocus();
            } else if (binding.imgBankStatement.getDrawable() == null) {
                onMessage("Please upload your Bank Statement ");
                binding.imgBankStatement.requestFocus();
            } else if (binding.imgEmpId.getDrawable() == null) {
                onMessage("Please upload your Employee Id ");
                binding.imgEmpId.requestFocus();
            } else if (binding.imgAadharCard.getDrawable() == null) {
                onMessage("Please upload your Aadhar Card ");
                binding.imgAadharCard.requestFocus();
            } else {
                uploadDocuments();
            }

            // onMessage("Please wait! Work in Progress");
//            if (isUploaded) {
//                uploadPicture();
//            } else {
//                onMessage(getString(R.string.plz_upload_front_image));
//            }
        });

    }

    private String selectImage(Context context) {
        final String[] ImageNname = {""};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your option");

        builder.setItems(options, (dialog, item) -> {

            if (options[item].equals("Upload Image")) {
                launchGalleryIntent();
//                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(takePicture, 0);

            } else if (options[item].equals("Upload PDF")) {
                launchPDF1();
                // Navigation.findNavController(binding.getRoot()).navigate(R.id.action_uploadDocumentFragment_to_selectPDF2);

                // ImageNname[0] = viewModel.SelectedImageName.getValue();


//                    Intent intent = new Intent();
//                    intent.setType("application/pdf");
//                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    intent.addCategory(Intent.CATEGORY_OPENABLE);
//                    startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
            }
        });
        builder.show();


        return ImageNname[0];
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
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        if (x == 1) {
            startActivityForResult(intent, REQUEST_IMAGE1);
        } else if (x == 2) {
            startActivityForResult(intent, REQUEST_IMAGE2);
        } else if (x == 3) {
            startActivityForResult(intent, REQUEST_IMAGE3);
        } else if (x == 4) {
            startActivityForResult(intent, REQUEST_IMAGE4);
        } else if (x == 5) {
            startActivityForResult(intent, REQUEST_IMAGE5);
        } else if (x == 6) {
            startActivityForResult(intent, REQUEST_IMAGE6);
        } else if (x == 7) {
            startActivityForResult(intent, REQUEST_IMAGE7);
        }
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        if (x == 1) {
            startActivityForResult(intent, REQUEST_IMAGE1);
        } else if (x == 2) {
            startActivityForResult(intent, REQUEST_IMAGE2);
        } else if (x == 3) {
            startActivityForResult(intent, REQUEST_IMAGE3);
        } else if (x == 4) {
            startActivityForResult(intent, REQUEST_IMAGE4);
        } else if (x == 5) {
            startActivityForResult(intent, REQUEST_IMAGE5);
        } else if (x == 6) {
            startActivityForResult(intent, REQUEST_IMAGE6);
        } else if (x == 7) {
            startActivityForResult(intent, REQUEST_IMAGE7);
        }

    }

    private void launchPDF1() {

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (x == 1) {
            startActivityForResult(Intent.createChooser(intent, "Select PDF"), REQUEST_PDF1);
            binding.linearAddressProof.setVisibility(View.VISIBLE);
        } else if (x == 3) {
            startActivityForResult(Intent.createChooser(intent, "Select PDF"), REQUEST_PDF3);
            binding.linearPaySlip.setVisibility(View.VISIBLE);
        } else if (x == 4) {
            startActivityForResult(Intent.createChooser(intent, "Select PDF"), REQUEST_PDF4);
            binding.linearPancard.setVisibility(View.VISIBLE);
        } else if (x == 5) {
            startActivityForResult(Intent.createChooser(intent, "Select PDF"), REQUEST_PDF5);
            binding.linearBankStatement.setVisibility(View.VISIBLE);
        } else if (x == 6) {
            startActivityForResult(Intent.createChooser(intent, "Select PDF"), REQUEST_PDF6);
            binding.linearEmpId.setVisibility(View.VISIBLE);
        } else if (x == 7) {
            startActivityForResult(Intent.createChooser(intent, "Select PDF"), REQUEST_PDF7);
            binding.linearAadharCard.setVisibility(View.VISIBLE);
        }
        //  Navigation.findNavController(binding.getRoot()).navigate(R.id.action_uploadDocumentFragment_to_selectPDF2);

    }


    public String getStringPdf(Uri filepath) {
        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            inputStream = getActivity().getContentResolver().openInputStream(filepath);

            byte[] buffer = new byte[1024];
            byteArrayOutputStream = new ByteArrayOutputStream();

            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        byte[] pdfByteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(pdfByteArray, Base64.DEFAULT);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE1) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                uri = data.getParcelableExtra("path");
                String path = String.valueOf(uri);
                SelectedImageType1 = path.substring(path.lastIndexOf("/") + 1);


                try {
                    // You can update this bitmap to your server
                    if (uri != null) {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                        isUploaded = true;
                        ImageOne = BitmapHelper.encodeImage(bitmap);
                        assert uri != null;
                        loadProfile1(uri.toString());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else if (requestCode == REQUEST_PDF1) {
            SelectedImageType1 = "";
            assert data != null;
            ImageOne = getStringPdf(data.getData());
            String uriString = data.getData().toString();
            File myFile = new File(uriString);
            path = myFile.getAbsolutePath();
            displayName = null;
            // String[] pathArr = myFile.getAbsolutePath().split(":/");
            // path = pathArr[pathArr.length - 1];
            if (uriString.startsWith("content://")) {
                cursor = null;
                try {
                    cursor = getActivity().getContentResolver().query(data.getData(), null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        SelectedImageType1 = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        // binding.imgAddressProof.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.pdf_img));
                        binding.imgAddressProof.setImageResource(R.drawable.pdf_img);

                    }
                } catch (Exception e) {

                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }

                }
            }

            // binding.addproofImg.getResources().getDrawable(R.drawable.pdf_img);


        } else if (requestCode == REQUEST_IMAGE2) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                uri = data.getParcelableExtra("path");
                String path = String.valueOf(uri);
                SelectedImageType2 = path.substring(path.lastIndexOf("/") + 1);
                try {
                    // You can update this bitmap to your server
                    if (uri != null) {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                        isUploaded = true;
                        ImageTwo = BitmapHelper.encodeImage(bitmap);
                        assert uri != null;
                        loadProfile2(uri.toString());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else if (requestCode == REQUEST_PDF2) {
            SelectedImageType2 = "";
            assert data != null;
            ImageTwo = getStringPdf(data.getData());
            String uriString = data.getData().toString();
            File myFile = new File(uriString);
            path = myFile.getAbsolutePath();
            displayName = null;
            // String[] pathArr = myFile.getAbsolutePath().split(":/");
            // path = pathArr[pathArr.length - 1];
            if (uriString.startsWith("content://")) {
                cursor = null;
                try {
                    cursor = getActivity().getContentResolver().query(data.getData(), null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        SelectedImageType2 = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                    }
                } catch (Exception e) {

                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }

                }
            }
        } else if (requestCode == REQUEST_IMAGE3) {
            assert data != null;
            uri = data.getParcelableExtra("path");
            String path = String.valueOf(uri);
            SelectedImageType3 = path.substring(path.lastIndexOf("/") + 1);
            try {
                // You can update this bitmap to your server
                if (uri != null) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    isUploaded = true;
                    // uploadKYCImageRequest.Image = BitmapHelper.encodeImage(bitmap);
                    ImageThree = BitmapHelper.encodeImage(bitmap);
                    assert uri != null;
                    loadProfile3(uri.toString());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_PDF3) {
            SelectedImageType3 = "";
            assert data != null;
            ImageThree = getStringPdf(data.getData());
            String uriString = data.getData().toString();
            File myFile = new File(uriString);
            path = myFile.getAbsolutePath();
            displayName = null;
            // String[] pathArr = myFile.getAbsolutePath().split(":/");
            // path = pathArr[pathArr.length - 1];
            if (uriString.startsWith("content://")) {
                cursor = null;
                try {
                    cursor = getActivity().getContentResolver().query(data.getData(), null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        SelectedImageType3 = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        // binding.imgPaySlip.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.pdf_img));
                        binding.imgPaySlip.setImageResource(R.drawable.pdf_img);

                    }
                } catch (Exception e) {

                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }

                }
            }
        } else if (requestCode == REQUEST_IMAGE4) {
            assert data != null;
            uri = data.getParcelableExtra("path");
            String path = String.valueOf(uri);
            SelectedImageType4 = path.substring(path.lastIndexOf("/") + 1);
            try {
                // You can update this bitmap to your server\
                if (uri != null) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    isUploaded = true;
                    ImageFour = BitmapHelper.encodeImage(bitmap);
                    assert uri != null;
                    loadProfile4(uri.toString());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_PDF4) {
            SelectedImageType4 = "";
            assert data != null;
            ImageFour = getStringPdf(data.getData());
            String uriString = data.getData().toString();
            File myFile = new File(uriString);
            path = myFile.getAbsolutePath();
            displayName = null;
            // String[] pathArr = myFile.getAbsolutePath().split(":/");
            // path = pathArr[pathArr.length - 1];
            if (uriString.startsWith("content://")) {
                cursor = null;
                try {
                    cursor = getActivity().getContentResolver().query(data.getData(), null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        SelectedImageType4 = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        binding.imgPancard.setImageResource(R.drawable.pdf_img);

                    }
                } catch (Exception e) {

                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }

                }
            }
        } else if (requestCode == REQUEST_IMAGE5) {
            assert data != null;
            uri = data.getParcelableExtra("path");
            String path = String.valueOf(uri);
            SelectedImageType5 = path.substring(path.lastIndexOf("/") + 1);
            try {
                // You can update this bitmap to your server
                if (uri != null) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    isUploaded = true;
                    // uploadKYCImageRequest.Image = BitmapHelper.encodeImage(bitmap);
                    ImageFive = BitmapHelper.encodeImage(bitmap);
                    assert uri != null;
                    loadProfile5(uri.toString());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_PDF5) {
            SelectedImageType5 = "";
            assert data != null;
            ImageFive = getStringPdf(data.getData());
            String uriString = data.getData().toString();
            File myFile = new File(uriString);
            path = myFile.getAbsolutePath();
            displayName = null;
            // String[] pathArr = myFile.getAbsolutePath().split(":/");
            // path = pathArr[pathArr.length - 1];
            if (uriString.startsWith("content://")) {
                cursor = null;
                try {
                    cursor = getActivity().getContentResolver().query(data.getData(), null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        SelectedImageType5 = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        binding.imgBankStatement.setImageResource(R.drawable.pdf_img);

                    }
                } catch (Exception e) {

                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }

                }
            }
        } else if (requestCode == REQUEST_IMAGE6) {
            assert data != null;
            uri = data.getParcelableExtra("path");
            String path = String.valueOf(uri);
            SelectedImageType6 = path.substring(path.lastIndexOf("/") + 1);
            try {
                // You can update this bitmap to your server
                if (uri != null) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    isUploaded = true;
                    // uploadKYCImageRequest.Image = BitmapHelper.encodeImage(bitmap);
                    ImageSix = BitmapHelper.encodeImage(bitmap);
                    assert uri != null;
                    loadProfile6(uri.toString());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_PDF6) {
            SelectedImageType6 = "";
            assert data != null;
            ImageSix = getStringPdf(data.getData());
            String uriString = data.getData().toString();
            File myFile = new File(uriString);
            path = myFile.getAbsolutePath();
            displayName = null;
            // String[] pathArr = myFile.getAbsolutePath().split(":/");
            // path = pathArr[pathArr.length - 1];
            if (uriString.startsWith("content://")) {
                cursor = null;
                try {
                    cursor = getActivity().getContentResolver().query(data.getData(), null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        SelectedImageType6 = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                        binding.imgEmpId.setImageResource(R.drawable.pdf_img);
                    }
                } catch (Exception e) {

                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }

                }
            }
        } else if (requestCode == REQUEST_IMAGE7) {
            assert data != null;
            uri = data.getParcelableExtra("path");
            String path = String.valueOf(uri);
            SelectedImageType7 = path.substring(path.lastIndexOf("/") + 1);
            try {
                // You can update this bitmap to your server
                if (uri != null) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    isUploaded = true;
                    ImageSeven = BitmapHelper.encodeImage(bitmap);
                    assert uri != null;
                    loadProfile7(uri.toString());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_PDF7) {
            SelectedImageType7 = "";
            assert data != null;
            ImageSeven = getStringPdf(data.getData());
            String uriString = data.getData().toString();
            File myFile = new File(uriString);
            path = myFile.getAbsolutePath();
            displayName = null;
            // String[] pathArr = myFile.getAbsolutePath().split(":/");
            // path = pathArr[pathArr.length - 1];
            if (uriString.startsWith("content://")) {
                cursor = null;
                try {
                    cursor = getActivity().getContentResolver().query(data.getData(), null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        SelectedImageType7 = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        binding.imgAadharCard.setImageResource(R.drawable.pdf_img);

                    }
                } catch (Exception e) {

                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }

                }
            }
        }
    }


    private void loadProfile1(String url) {
        if (url.contains(".jpg") || url.contains(".jpeg")) {
            Glide.with(this).load(url)
                    .into(binding.imgAddressProof);

        } else if (url.contains(".pdf")) {
            binding.linearAddressProof.setVisibility(View.VISIBLE);
        } else {
            onMessage("Selected image or file should be .jpg or .jpeg or .pdf type");
        }

    }

    private void loadProfile2(String url) {
        if (url.contains(".jpg") || url.contains(".jpeg")) {
            Glide.with(this).load(url)
                    .into(binding.imgProfilePic);
        } else {
            onMessage("Selected image or file should be .jpg or .jpeg type");
        }
    }

    private void loadProfile3(String url) {
        if (url.contains(".jpg") || url.contains(".jpeg")) {
            Glide.with(this).load(url)
                    .into(binding.imgPaySlip);
        } else if (url.contains(".pdf")) {
            binding.linearPaySlip.setVisibility(View.VISIBLE);
        } else {
            onMessage("Selected image or file should be .jpg or .jpeg or .pdf type");
        }
    }

    private void loadProfile4(String url) {
        if (url.contains(".jpg") || url.contains(".jpeg")) {
            Glide.with(this).load(url)
                    .into(binding.imgPancard);
        } else if (url.contains(".pdf")) {
            binding.linearPancard.setVisibility(View.VISIBLE);
        } else {
            onMessage("Selected image or file should be .jpg or .jpeg or .pdf type");
        }
    }

    private void loadProfile5(String url) {
        if (url.contains(".pdf")) {
            binding.linearBankStatement.setVisibility(View.VISIBLE);
        } else {
            onMessage("Selected file type should be .pdf only");
        }
    }

    private void loadProfile6(String url) {
        if (url.contains(".jpg") || url.contains(".jpeg")) {
            Glide.with(this).load(url)
                    .into(binding.imgEmpId);
        } else if (url.contains(".pdf")) {
            binding.linearEmpId.setVisibility(View.VISIBLE);
        } else {
            onMessage("Selected image or file should be .jpg or .jpeg or .pdf type");
        }
    }

    private void loadProfile7(String url) {
        if (url.contains(".jpg") || url.contains(".jpeg")) {
            Glide.with(this).load(url)
                    .into(binding.imgAadharCard);
        } else if (url.contains(".pdf")) {
            binding.linearAadharCard.setVisibility(View.VISIBLE);
        } else {
            onMessage("Selected image or file should be .jpg or .jpeg or .pdf type");
        }
    }


    public void uploadDocuments() {
        Utils.showCustomProgressDialog(getContext(), false);
        UploadDocumentsRequest request = new UploadDocumentsRequest();
        request.customer_no = customer_no;
        request.iDocument = ImageOne + "|" + ImageTwo + "|" + ImageThree + "|" + ImageFour + "|" + ImageFive +
                "|" + ImageSix + "|" + ImageSeven;


        String DocName = SelectedImageType1 + "|" + SelectedImageType2 + "|" + SelectedImageType3 + "|" +
                SelectedImageType4 + "|" + SelectedImageType5 + "|" + SelectedImageType6 + "|" + SelectedImageType7;

        request.document_name = DocName.replaceAll("%20", "_");

        request.document_type = selected_address_proof + "|" + "profile" + "|" + "payslip" + "|" + "pancard" + "|" +
                "bankStatement" + "|" + "empBadge" + "|" + "aadharcard";

        String request_document_pdfPWD =
                SelectedImageType1 + "," + binding.edtAddProofPdfPassword.getText().toString() + "|" +
                        SelectedImageType3 + "," + binding.edtPaySlipPdfPassword.getText().toString() + "|" +
                        SelectedImageType4 + "," + binding.edtPancardPdfPassword.getText().toString() + "|" +
                        SelectedImageType5 + "," + binding.edtBankStatePassword.getText().toString() + "|" +
                        SelectedImageType6 + "," + binding.edtEmpBadgePdfPassword.getText().toString() + "|" +
                        SelectedImageType7 + "," + binding.edtAadharCardPdfPassword.getText().toString();

        request.document_pdfPWD = request_document_pdfPWD.replaceAll("%20", "_");

        viewModel.uploadDocuments(request, getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()

                , response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            onMessage(response.resource.description);
                            showSucces();
                        } else {
                            onError(response.resource.description);
                        }
                    }
                });

    }


    private void showSucces() {
        SingleButtonMessageDialog dialog = new
                SingleButtonMessageDialog(getString(R.string.successfully_txt)
                , ("Documents Uploaded Successfully!!"), this,
                false);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }


    @Override
    public void onSuccess() {

        // Intent
    }

    @Override
    public int getLayoutId() {
        return R.layout.upload_document_fragment;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        if (adapterView.getId() == R.id.addProofSpinner) {
            int salutation_pos = adapterView.getSelectedItemPosition();
            if (salutation_pos == 1) {
                selected_address_proof = "utilityBill";
            } else if (salutation_pos == 2) {
                selected_address_proof = "telephoneBill";
            } else if (salutation_pos == 3) {
                selected_address_proof = "passport";
            } else if (salutation_pos == 4) {
                selected_address_proof = "voterId";
            } else if (salutation_pos == 5) {
                selected_address_proof = "drivingLicense";
            } else if (salutation_pos == 6) {
                selected_address_proof = "rentalAgreement";
            } else if (salutation_pos == 7) {
                selected_address_proof = "aadharcardAddress";
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onProceed() {
        Navigation.findNavController(binding.getRoot()).
                navigate(R.id.action_uploadDocumentFragment_to_createCustomerFragment);
    }

    @Override
    public void onCancel(boolean goBack) {

    }
}
