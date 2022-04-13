package angoothape.wallet;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;

import angoothape.wallet.TransactionHistory.TransactionHistoryViewModel;
import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.ActivityAEPSReceiptBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AEPSReceiptRequest;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.DecryptedResponse;
import angoothape.wallet.di.JSONdi.restResponse.AEPSReceiptResponse;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.dialogs.ShareDialog;
import angoothape.wallet.interfaces.OnReceiptGenerator;
import angoothape.wallet.utils.Utils;

public class AEPSReceiptActivity extends RitmanBaseActivity<ActivityAEPSReceiptBinding>
        implements OnReceiptGenerator {


    TransactionHistoryViewModel viewModel;
    Bitmap bitmap;
    String PDF_Name, PDF_Type = "";
    public static final String PATH = "/AEPA/AEPA_Reciept/";
    public static int REQUEST_PERMISSIONS = 1;
    boolean boolean_permission;
    String transactionNumber;

    @Override
    public int getLayoutId() {
        return R.layout.activity_a_e_p_s_receipt;
    }


    @Override
    protected void initUi(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(TransactionHistoryViewModel.class);
        transactionNumber = getIntent().getStringExtra("TransactionNumber");
        getReceipt();


        binding.toolBar.backBtn.setOnClickListener(v -> onBackPressed());

        binding.toolBar.toolBarFinal
                .setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.toolBar.backBtn
                .setColorFilter(ContextCompat.getColor(this,
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);


        binding.toolBar.crossBtn.setOnClickListener(v -> {
            onClose();
        });

        binding.shareBtn.setOnClickListener(v -> {
            ShareDialog dialog = new ShareDialog(this);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            dialog.show(transaction, "");
        });
    }


    void getReceipt() {
        Utils.showCustomProgressDialog(this, false);
        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim();

        AEPSReceiptRequest aepsReceiptRequest = new AEPSReceiptRequest();
        aepsReceiptRequest.TransactionNumber = transactionNumber;
        String body = RestClient.makeGSONString(aepsReceiptRequest);

        AERequest request = new AERequest();
        request.body = AESHelper.encrypt(body.trim(), gKey.trim());


        viewModel.getAEPSReceipt(request, KeyHelper.getKey(getSessionManager().getMerchantName()).trim(), KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim())
                .observe(this, response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            String bodyy = AESHelper.decrypt(response.resource.data.body, gKey);
                            Log.e("hs ", bodyy);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<AEPSReceiptResponse>() {
                                }.getType();
                                AEPSReceiptResponse data = gson.fromJson(bodyy, type);
                                showReceipt(data);

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


    public void showReceipt(AEPSReceiptResponse data) {
        getTransactionData(data.decrypted_Response.get(0));
        binding.bcName.setText(data.bC_Name);
        binding.agentId.setText(data.agent_ID);
        binding.transAmountAeps.setText("₹ " +  data.Txn_Amount);
        String adhar_no_last_4_digit = "", masked_aadhar_no = "";

        if (data.Aadhar_Number.toString().length() == 12) {
            adhar_no_last_4_digit = data.Aadhar_Number.substring(8);
            masked_aadhar_no = "********" + adhar_no_last_4_digit;
        } else {
            adhar_no_last_4_digit = data.Aadhar_Number.substring(12);
            masked_aadhar_no = "************" + adhar_no_last_4_digit;
        }

        binding.adharTxt.setText(masked_aadhar_no);
        binding.timeAeps.setText(data.Txn_Time);
        binding.dateAeps.setText(data.Txn_Date);
    }


    public void getTransactionData(DecryptedResponse data) {
        binding.uidaiCode.setText(data.uIDAIAuthenticationCode);
        binding.stanTxt.setText(data.sTAN);
        binding.rrnTxt.setText(data.rRN);

        if (data.responseCode == String.valueOf(68)) {
            binding.aepsStatus.setText("Decline" + " (" + data.responseCode + ")");
        } else {
            binding.aepsStatus.setText("Successful" + " (" + data.responseCode + ")");
        }
        float actualAmount = 0;
        try {
            float parsed = Float.parseFloat(data.balanceAmountActual);
            actualAmount = parsed / 100;
        } catch (Exception e) {
            Toast.makeText(this, String.valueOf(e.getLocalizedMessage()), Toast.LENGTH_SHORT).show();
        }

        binding.acBalanceAeps.setText("₹ " + String.valueOf(actualAmount));

        PDF_Name = data.retailerTxnId + "_" + PDF_Type;
    }


    File filePath;

    private void createPdf() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = binding.pdfView.getChildAt(0).getHeight();
        float width = binding.pdfView.getWidth();

        int convertHighet = (int) hight, convertWidth = (int) width;

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.
                PageInfo.Builder(convertWidth, convertHighet, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();


        Paint paint = new Paint();
        canvas.drawPaint(paint);


        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);

        String directory_path = getFilesDir() + PATH;
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path + PDF_Name + ".pdf";
        filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, getString(R.string.save_transaction_receipt), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error " + e.toString());
            Toast.makeText(this, getString(R.string.some_thing_wrong) + e.toString(), Toast.LENGTH_LONG).show();
        }
        document.close();

    }

    private void saveImage(Bitmap finalBitmap, String image_name) {
        String root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString() + "/TotiPay/";
        File myDir = new File(root);
        myDir.mkdirs();
        String fname = image_name + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        Log.i("LOAD", root + fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Toast.makeText(this, getString(R.string.image_saved), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createPdfForWhatsApp() {
        createPdf();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setPackage("com.whatsapp");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "My sample image text");
        shareIntent.putExtra(Intent.EXTRA_STREAM, getURI());
        shareIntent.setType("application/pdf");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(shareIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "install whatsapp first", Toast.LENGTH_SHORT).show();
        }

        startActivity(shareIntent);
    }

    private void createPdfForOthers() {
        createPdf();
        Intent intent = ShareCompat.IntentBuilder.from(this)
                .setType("application/pdf")
                .setStream(getURI())
                .setChooserTitle("Choose bar")
                .createChooserIntent()
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(intent);
    }


    public Uri getURI() {
        File outputFile = new File(getFilesDir() + PATH,
                PDF_Name + ".pdf");

        return FileProvider.getUriForFile(this,
                getPackageName().concat(".provider"), outputFile);
    }

    public Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(),
                binding.pdfView.getChildAt(0).getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }


    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(AEPSReceiptActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(AEPSReceiptActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }

            if ((ActivityCompat.shouldShowRequestPermissionRationale(AEPSReceiptActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(AEPSReceiptActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }
        } else {
            boolean_permission = true;
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
            RelativeLayout root = (RelativeLayout) inflater.inflate(R.layout.receipt_layout_money_send, null); //RelativeLayout is root view of my UI(xml) file.
            root.setDrawingCacheEnabled(true);
            bitmap = getBitmapFromView(this.getWindow().findViewById(R.id.pdf_view)); // here give id of our root layout (here its my RelativeLayout's id)

            if (!isSaveAsImage) {
                if (isShare) {
                    createPdfForWhatsApp();
                } else if (isShareOthers) {
                    createPdfForOthers();
                } else {
                    createPdf();
                }
            } else {
                saveImage(bitmap, PDF_Name);
            }

        }
    }

    public boolean isShare = false;
    public boolean isShareOthers = false;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                boolean_permission = true;
                LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
                RelativeLayout root = (RelativeLayout) inflater.inflate(R.layout.receipt_layout_money_send, null); //RelativeLayout is root view of my UI(xml) file.
                root.setDrawingCacheEnabled(true);
                bitmap = getBitmapFromView(this.getWindow().findViewById(R.id.pdf_view)); // here give id of our root layout (here its my RelativeLayout's id)

                if (!isSaveAsImage) {
                    if (isShare) {
                        createPdfForWhatsApp();
                    } else if (isShareOthers) {
                        createPdfForOthers();
                    } else {
                        createPdf();
                    }
                } else {
                    saveImage(bitmap, PDF_Name);
                }

                //  createPdf();

            } else {
                Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

            }
        }
    }

    @Override
    public void onSaveAsPdf() {
        isSaveAsImage = false;
        isShareOthers = false;
        isShare = false;
        fn_permission();
    }

    @Override
    public void onSentAsWhatsApp() {
        isShare = true;
        isSaveAsImage = false;
        isShareOthers = false;
        fn_permission();
    }


    @Override
    public void onSentAsOthers() {
        isShareOthers = true;
        isShare = false;
        isSaveAsImage = false;
        fn_permission();
    }


    boolean isSaveAsImage = false;

    @Override
    public void onSaveAsImage() {
        isSaveAsImage = true;
        fn_permission();
    }

}