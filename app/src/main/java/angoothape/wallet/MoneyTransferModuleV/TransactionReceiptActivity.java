package angoothape.wallet.MoneyTransferModuleV;

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


import angoothape.wallet.MoneyTransferModuleV.viewmodels.BankTransferViewModel;
import angoothape.wallet.R;
import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.ReceiptLayoutMoneySendBinding;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.TransactionRecieptRequest;
import angoothape.wallet.di.JSONdi.restResponse.GetTransactionReceiptResponse;
import angoothape.wallet.dialogs.ShareDialog;
import angoothape.wallet.interfaces.OnReceiptGenerator;
import angoothape.wallet.utils.NumberFormatter;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TransactionReceiptActivity extends RitmanBaseActivity<ReceiptLayoutMoneySendBinding>
        implements OnReceiptGenerator {

    Bitmap bitmap;
    String transactionNumber;

    public static final String PATH = "/RitmanDomestic/Transactions/";
    public static int REQUEST_PERMISSIONS = 1;
    boolean boolean_permission;

    BankTransferViewModel viewModel;

    @Override
    public int getLayoutId() {
        return R.layout.receipt_layout_money_send;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(BankTransferViewModel.class);
        transactionNumber = getIntent().getStringExtra("TransactionNumber");


        binding.toolbar.crossBtn.setVisibility(View.GONE);
        binding.toolbar.titleTxt.setText("View Receipt");
        getReciept();

        binding.shareBtn.setOnClickListener(v -> {
            ShareDialog dialog = new ShareDialog(this);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            dialog.show(transaction, "");
        });

        binding.toolbar.backBtn.setOnClickListener(v -> {
            super.onBackPressed();
        });
    }


    private void getReciept() {
        TransactionRecieptRequest request = new TransactionRecieptRequest();
        request.TransactionNumber = transactionNumber;
        request.ClientTxnNumber = "";


        viewModel.getReciept(request, sessionManager.getMerchantName()).observe(this
                , response -> {
                    if (response.status == Status.ERROR) {
                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            onGetTransactionReceipt(response.resource.data);
                        } else {
                            onError(response.resource.description);
                        }
                    }
                });
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }


    public void onGetTransactionReceipt(GetTransactionReceiptResponse receiptResponse) {

        binding.txnNumber.setText(receiptResponse.transactionNumber);
        binding.txnDateTime.setText(receiptResponse.transactionDateTime);

        binding.receivingAmount.setText(
                NumberFormatter.decimal(
                        receiptResponse.receivingAmount).concat(" ")
                        .concat(receiptResponse.payOutCurrency));
        binding.mobileNumber.setText(receiptResponse.remitterContactNo);
        binding.accountNumber.setText(receiptResponse.bankAccountNo);
        binding.bankName.setText(receiptResponse.bankName);
        binding.bankCode.setText(receiptResponse.bankCode);
        binding.payoutagent.setText(receiptResponse.payoutAgent_Name);
        binding.customerAddress.setText(receiptResponse.remitterAddress);
        binding.beneName.setText(receiptResponse.beneficiaryName);
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
        String targetPdf = directory_path + transactionNumber + ".pdf";
        filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            //   Toast.makeText(this, getString(R.string.save_transaction_receipt), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error " + e.toString());
            // Toast.makeText(this, getString(R.string.some_thing_wrong) + e.toString(), Toast.LENGTH_LONG).show();
        }
        document.close();

    }

    private void saveImage(Bitmap finalBitmap, String image_name) {
        String root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString() + "/Angoothape/";
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
                transactionNumber + ".pdf");

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
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(TransactionReceiptActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(TransactionReceiptActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }

            if ((ActivityCompat.shouldShowRequestPermissionRationale(TransactionReceiptActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(TransactionReceiptActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
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
                saveImage(bitmap, transactionNumber);
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
                    saveImage(bitmap, transactionNumber);
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
