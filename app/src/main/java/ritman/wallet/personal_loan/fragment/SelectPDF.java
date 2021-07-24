package ritman.wallet.personal_loan.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import androidx.navigation.Navigation;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import ritman.wallet.R;
import ritman.wallet.databinding.SelectPdfFragmentLayoutBinding;
import ritman.wallet.fragments.BaseFragment;
import ritman.wallet.utils.BitmapHelper;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class SelectPDF extends BaseFragment<SelectPdfFragmentLayoutBinding> {
    Bitmap selectedImage;
    Uri uri;
    ImageView img_camera;
    String SelectedImageType1,path,displayName;
    Cursor cursor;
    String ImageOne="";

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {

        binding.user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.select_pdf_fragment_layout;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {

            if (resultCode == RESULT_OK && data != null) {
                        uri = data.getData();
//                        String path= String.valueOf(uri);
//                        SelectedImageType1=path.substring(path.lastIndexOf("/")+1);


                        String uriString = uri.toString();
                        File myFile = new File(uriString);
                        path=myFile.getAbsolutePath();
                        displayName = null;
                        // String[] pathArr = myFile.getAbsolutePath().split(":/");
                        // path = pathArr[pathArr.length - 1];
                        if (uriString.startsWith("content://")){
                            cursor = null;
                            try {
                                cursor = getBaseActivity().getContentResolver().query(uri, null, null, null, null);
                                if (cursor != null && cursor.moveToFirst()) {
                                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                                    SelectedImageType1=displayName;
                                    ImageOne=encodeFileToBase64Binary(path);
                                    Bundle bundle=new Bundle();
                                    bundle.putString("SelectedImageType1",SelectedImageType1);
                                    bundle.putString("ImageOne",ImageOne);
                                   // Navigation.findNavController(binding.getRoot()).navigate(R.id.action_selectPDF2_to_uploadDocumentFragment,bundle);
                                    onMessage("upload images");
                                }

                              //  Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                               // ImageOne = BitmapHelper.encodeImage(bitmap);


//                            selectedImage= MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
//                            img_camera.setImageBitmap(selectedImage);
                            }
                            catch (Exception e){

                            }
                            finally {
                                cursor.close();
                            }
                        }

                        else if (uriString.startsWith("file://")) {
                            displayName = myFile.getName();
                        }

                    }


        }
    }

    private String encodeFileToBase64Binary(String path) {
        int size = (int) path.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(path));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String encoded = Base64.encodeToString(bytes,Base64.NO_WRAP);
        return encoded;
    }
}
