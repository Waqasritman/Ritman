package angoothape.wallet;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restResponse.AEPS_Trans_Response;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.dialogs.SingleButtonMessageDialog;
import angoothape.wallet.interfaces.OnDecisionMade;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ir.androidexception.datatable.DataTable;
import ir.androidexception.datatable.model.DataTableHeader;
import ir.androidexception.datatable.model.DataTableRow;
import angoothape.wallet.aeps.global.Verhoeff;
import angoothape.wallet.aeps.maskedittext.MaskedEditText;
import angoothape.wallet.aeps.model.DeviceInfo;
import angoothape.wallet.aeps.model.Opts;
import angoothape.wallet.aeps.model.PidData;
import angoothape.wallet.aeps.model.PidOptions;
import angoothape.wallet.aeps.model.uid.AuthReq;
import angoothape.wallet.aeps.model.uid.AuthRes;
import angoothape.wallet.aeps.model.uid.Meta;
import angoothape.wallet.aeps.model.uid.Uses;
import angoothape.wallet.aeps.signer.XMLSigner;
import angoothape.wallet.aeps.viewmodels.AEPSViewModel;
import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.ActivityAEPSBinding;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.DecryptedResponse;
import angoothape.wallet.di.JSONdi.restRequest.GetAEPSTransaction;
import angoothape.wallet.dialogs.ShareDialog;
import angoothape.wallet.interfaces.OnReceiptGenerator;
import angoothape.wallet.utils.Utils;

public class AEPSActivity extends RitmanBaseActivity<ActivityAEPSBinding> /*implements MFS100Event*/
        implements OnReceiptGenerator, OnDecisionMade {

    AEPSViewModel viewModel;
    String mainResult;
    @BindView(R.id.spinnerTotalFingerCount)
    Spinner spinnerTotalFingerCount;
    @BindView(R.id.linearFingerCount)
    LinearLayout linearFingerCount;
    @BindView(R.id.spinnerTotalFingerType)
    Spinner spinnerTotalFingerType;
    @BindView(R.id.spinnerTotalFingerFormat)
    Spinner spinnerTotalFingerFormat;
    @BindView(R.id.linearFingerFormat)
    LinearLayout linearFingerFormat;
    @BindView(R.id.edtxTimeOut)
    EditText edtxTimeOut;
    @BindView(R.id.edtxPidVer)
    EditText edtxPidVer;
    @BindView(R.id.linearTimeoutPidVer)
    LinearLayout linearTimeoutPidVer;
    @BindView(R.id.txtSelectPosition)
    TextView txtSelectPosition;
    @BindView(R.id.chbxUnknown)
    CheckBox chbxUnknown;
    @BindView(R.id.chbxLeftIndex)
    CheckBox chbxLeftIndex;
    @BindView(R.id.chbxLeftMiddle)
    CheckBox chbxLeftMiddle;
    @BindView(R.id.chbxLeftRing)
    CheckBox chbxLeftRing;
    @BindView(R.id.chbxLeftSmall)
    CheckBox chbxLeftSmall;
    @BindView(R.id.chbxLeftThumb)
    CheckBox chbxLeftThumb;
    @BindView(R.id.chbxRightIndex)
    CheckBox chbxRightIndex;
    @BindView(R.id.chbxRightMiddle)
    CheckBox chbxRightMiddle;
    @BindView(R.id.chbxRightRing)
    CheckBox chbxRightRing;
    @BindView(R.id.chbxRightSmall)
    CheckBox chbxRightSmall;
    @BindView(R.id.chbxRightThumb)
    CheckBox chbxRightThumb;
    @BindView(R.id.linearSelectPosition)
    LinearLayout linearSelectPosition;
    @BindView(R.id.edtxAdharNo)
    MaskedEditText edtxAdharNo;
    @BindView(R.id.linearAdharNo)
    LinearLayout linearAdharNo;
    @BindView(R.id.btnDeviceInfo)
    Button btnDeviceInfo;
    @BindView(R.id.btnCapture)
    Button btnCapture;
    @BindView(R.id.btnAuthRequest)
    Button btnAuthRequest;
    @BindView(R.id.btnReset)
    Button btnReset;
    @BindView(R.id.txtDataLabel)
    TextView txtDataLabel;
    @BindView(R.id.txtOutput)
    TextView txtOutput;
    @BindView(R.id.activity_main)
    LinearLayout activityMain;
    @BindView(R.id.spinnerEnv)
    Spinner spinnerEnv;


    private int fingerCount = 0;
    private PidData pidData = null;
    private Serializer serializer = null;
    private ArrayList<String> positions;
    String amount_a, iin_, txn_code_, customer_consent_, BankName_;
    String[] columns_;
    DataTable dataTable;
    String typeMini;
    Bitmap bitmap;
    String PDF_Name, PDF_Type, PDF_Date;


    public static final String PATH = "/AEPA/AEPA_Reciept/";
    public static int REQUEST_PERMISSIONS = 1;
    boolean boolean_permission;

    @Override
    public int getLayoutId() {
        return R.layout.activity_a_e_p_s;
    }


    @Override
    protected void initUi(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(AEPSViewModel.class);
        dataTable = findViewById(R.id.data_table);
        ButterKnife.bind(this);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        positions = new ArrayList<>();
        serializer = new Persister();

        //   edtxAdharNo.setText("");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            amount_a = bundle.getString("Amount");
            iin_ = bundle.getString("IINNo");
            BankName_ = bundle.getString("BankName");
            txn_code_ = bundle.getString("TxnTypeCode");
        }

//
//        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
//                .getKey(getSessionManager().getMerchantName())).trim();
//        Log.e("gKey: ", gKey);
//        String bodyy = AESHelper.decrypt("8isXkblNu5rp8H12Scp9vNq1xxVEwk3e15DOJDz7u+ne8R7ehieSM8sQymJ88beJ+1qiyf53LE0NyIB1nqBsxOeyUsZVK4yg+dE31ipI3z5ZXbqcIuhdiZDVE9kAm1qormXSMkWpShYgZzSUHNrTTTBpsPEkl0cPZ5HvkNNVKDEReq5/FrUZR2mNK+hZAiGuCc1XZsbPWcSgtLu8IVRrtE5tnLUR082CmO5mSAubyzGhUi5+XD0GLawq9a9hj7o1CrRDm1hxlIxKHsCXNKOoVbiXLWSddF/uv/vKJqWpBANEs427kVPA8BG0equKEz10sEtSedzmh2w7K1N2bHitnGBIZ/QXatw5DoyqCCMRS3QR9H5PUDahbGdbNBKANL5BMCM35OpAmn6vVmYJ8xc7B++R2JbVRzweFNfpB0xAOQv/TFe+M7wNCLt1TGt+lldskOv9W35PFbL0MpqM6VjxPWnNHf4Uv3kN38kygmwclWew5dEGKqVd1Yb2l2olxyUcBL38AgoSTtheocHL2VMD6NmrZp38vXrgTM5P16P0NmTB0b+8bujtYQ2RhF5aInzqoeVKufS89PGCqXtvejSdvJA4ns8nXDkganb5bVj5DfUsINs5LFjVb1NFPkQNdyAV+vCGM4AAe42E5wvJQ6GDWDjJWjGlGN2eSghwvFmYN+E7ZxReVhD+zCs2Y5vS+WqFS6aE50PV692ODl+v1jg33MYP8T04DjGLW2EP7GGADB/8Am1cgqydlG7d2OQ3cYrxJcD+TbeAC3T66184B2lnybrNtxIDYAU9x4MZmSy4eiu1nx7CTC/K7WqZjKGLw1CPonEVZS9d5QMKwqOZV0Kgp14CFSD23spG3ywOtQDUGu6z3ac4xYmymR1PAsjNKAcM+3ZSkvrcrp2PWSy1hbiCQg==", gKey);
//
//        Log.e("setUp: ", bodyy);
//
//        binding.activityMain.setVisibility(View.INVISIBLE);
//        binding.relativeReciept.setVisibility(View.VISIBLE);
//
//        try {
//            Gson gson = new Gson();
//            Type type = new TypeToken<AEPS_Trans_Response>() {
//            }.getType();
//
//
//            AEPS_Trans_Response data = gson.fromJson(bodyy, type);
//
//
//            getTransactionData(data.decrypted_Response);
//
//
//            binding.bcName.setText(data.bC_Name);
//            binding.agentId.setText(data.agent_ID);
//
//
////            float actualAmount = 0;
////            try {
////                float parsed = Float.parseFloat(data.decrypted_Response.balanceAmountActual);
////                actualAmount = parsed / 100;
////            } catch (Exception e) {
////                Toast.makeText(this, String.valueOf(e.getLocalizedMessage()), Toast.LENGTH_SHORT).show();
////            }
//
////
////            Log.e("setUp: ", String.valueOf(actualAmount));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }





        String[] arrayServiceType = {"Yes", "No"};
        ArrayAdapter adptService = new ArrayAdapter(AEPSActivity.this, android.R.layout.simple_list_item_1, arrayServiceType);
        adptService.setDropDownViewResource(android.R.layout.simple_list_item_1);
        binding.customerConsent.setAdapter(adptService);
        binding.customerConsent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {


                if (position == 0) {
                    customer_consent_ = "1";

                } else if (position == 1) {
                    customer_consent_ = "0";
                }
            }

            @Override
            public void onNothingSelected(AdapterView parent) {
            }
        });


      /*  String MiniStatementData="040621DR UMA 0000000000000125010004062100000000000000000000000000000000000000000000000000DR UMA   000002200000000000000000000000000040621DR UMA 000004500000040619DR ATM 000001500000040619DR UMA 000004500000040619DR UMA 000001400000040619DR000000000000000000000000 UMA 000001400000040618DR ATM 000001500000040617DR ATM 000000540000Balance 000014354300";

        String MiniStatemen_ = MiniStatementData.substring(0,31);

        ListItem li_ = null ;

        for (int z = 0; z< MiniStatementData.length();z++)
        {



        }

        String[] arrayMini=MiniStatementData.split("\\s");
        List<String> al = new ArrayList<String>();
        // Array to ArrayList Conversion
        for (String ArrayListmini : arrayMini)
            al.add(ArrayListmini);*/


        binding.testu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (txn_code_.equals("31")) {
                    binding.transAmountLinear.setVisibility(View.GONE);
                    binding.activityMain.setVisibility(View.INVISIBLE);
                    binding.relativeReciept.setVisibility(View.VISIBLE);
                    // binding.titleService.setText("");

                } else if (txn_code_.equals("07")) {
                    binding.acBalanceLinear.setVisibility(View.GONE);
                    binding.transAmountLinear.setVisibility(View.GONE);
                    binding.activityMain.setVisibility(View.INVISIBLE);
                    binding.relativeReciept.setVisibility(View.VISIBLE);
                    binding.titleService.setText("Mini Statement");
                } else if (txn_code_.equals("01")) {
                    binding.transAmountLinear.setVisibility(View.VISIBLE);
                    binding.activityMain.setVisibility(View.INVISIBLE);
                    binding.relativeReciept.setVisibility(View.VISIBLE);
                    binding.titleService.setText("Mini Statement");
                    getColomData();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss ");
                    String currentDateandTime1 = sdf1.format(new Date());
                    binding.timeAeps.setText(currentDateandTime1);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy ");
                    String currentDateandTime = sdf.format(new Date());
                    binding.dateAeps.setText(currentDateandTime);
                    binding.inernAmount.setVisibility(View.GONE);
                }
            }
        });

        binding.shareBtn.setOnClickListener(v -> {
            ShareDialog dialog = new ShareDialog(AEPSActivity.this);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            dialog.show(transaction, "");
        });


    }

    public void getColomData() {
        DataTableHeader header = new DataTableHeader.Builder()
                .item("Date", 3)
                .item("Mode of TXN", 5)
                /* .item("Type", 2)
                 .item("REF No", 3)*/
                .item("D/C", 2)
                .item("Amount", 4)

                .build();
        //Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/iran_sans.ttf");
        String MiniStatementData = "38100100207002003UID00500210006350 040621DR UMA\n" +
                "000001250100 040621CR UMA 000002200000 040621DR UMA 000004500000\n" +
                "040619DR ATM 000001500000 040619DR UMA 000004500000 040619DR\n" +
                "UMA 000001400000 040619DR UMA 000001400000 040618DR ATM\n" +
                "000001500000 040617DR ATM 000000540000 Balance 000014354303";

        String MiniStatemen_ = MiniStatementData.substring(35, MiniStatementData.length());

        String MiniState_ = MiniStatemen_.split("Balance")[0].toString().replace("\n", " ");

        String[] final_MiniState_ = MiniState_.split(" ");

        String test_ = "", final_test_ = "";
        int k_ = 0;
        ArrayList<DataTableRow> rows_ = new ArrayList<>();
        for (int i_ = 0; i_ < final_MiniState_.length / 3; i_++) {
            test_ = "";
            k_ = i_ * 3 + 3;
            for (int j_ = i_ * 3; j_ < k_; j_++) {
                test_ = test_ + "," + final_MiniState_[j_];
            }

            columns_ = test_.substring(1).split(",");

            String final_date_ = (columns_[0].substring(0, 6)).substring(0, 2) + "/" +
                    (columns_[0].substring(0, 6)).substring(2, 4) + "/" +
                    (columns_[0].substring(0, 6)).substring(4);
           /* if (columns_[0].substring(6).equals("DR")){
                 typeMini="D";
            }else if (columns_[0].substring(6).equals("CR")){
               typeMini="C";
            }*/


            DataTableRow row = new DataTableRow.Builder()
                    .value(final_date_)
                    .value(columns_[1])
                    /* .value(typeMini)*/
                    /*.value("12145252")*/
                    .value(columns_[0].substring(6))
                    .value(columns_[2])

                    .build();
            rows_.add(row);

        }

        //  dataTable.setTypeface(tf);
        dataTable.setHeader(header);
        dataTable.setRows(rows_);
        dataTable.inflate(this);
        // binding.dataTable.setHeaderBackgroundColor(Color.parseColor("#141E61"));


    }

    @OnClick({R.id.btnDeviceInfo, R.id.btnCapture, R.id.btnAuthRequest, R.id.btnReset, R.id.btnSubmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDeviceInfo:
                try {
                    Intent intent = new Intent();
                    intent.setAction("in.gov.uidai.rdservice.fp.INFO");
                    startActivityForResult(intent, 1);
                } catch (Exception e) {
                    Log.e("Error", e.toString());
                }
                break;
            case R.id.btnCapture:

                try {
                    String aadharNo1 = binding.edtxAdharNo.getText().toString();
                    if (binding.edtxAdharNo.length() != 12 || !Verhoeff.validateVerhoeff(aadharNo1)) {
                        onMessage("Please enter valid aadhaar number.");
                    } else if (customer_consent_ == "0") {
                        onMessage("Please select Customer Consent (YES)");
                    } else {

                        String pidOption = getPIDOptions();

                    /*String pidOption = "<PidOptions ver=\"2.0\">" +
                            "   <Opts env=\"S\" fCount=\"1\" fType=\"0\" format=\"0\" iCount=\"0\" iType=\"0\" otp=\"1234\" wadh=\"Hello\" pCount=\"0\" pType=\"0\" pidVer=\"2.0\" posh=\"UNKNOWN\" timeout=\"10000\"/>" +
                            "   <Demo lang=\"05\">" +
                            "   <Pi ms=\"P\" mv=\"Jigar Shekh\" name=\"Jigar\" lname=\"Shekh\" lmv=\"\" gender=\"M\" dob=\"\" dobt=\"V\" age=\"24\" phone=\"\" email=\"\"/>" +
                            "   <Pa ms=\"E\" co=\"\" house=\"\" street=\"\" lm=\"\" loc=\"\"" +
                            "  vtc=\"\" subdist=\"\" dist=\"\" state=\"\" country=\"\" pc=\"\" po=\"\"/>" +
                            "   <Pfa ms=\"E|P\" mv=\"\" av=\"\" lav=\"\" lmv=\"\"/>" +
                            "   </Demo>" +
                            "</PidOptions>";*/

                    /*String pidOption = "<PidOptions ver=\"2.0\">" +
                            "   <Opts env=\"S\" fCount=\"1\" fType=\"0\" format=\"0\" iCount=\"0\" iType=\"0\" pCount=\"0\" pType=\"0\" pidVer=\"2.0\" posh=\"UNKNOWN\" timeout=\"10000\"/>" +
                            "   <Demo lang=\"05\">" +
                            "   <Pi ms=\"P\" mv=\"100\" name=\"Mahesh\" gender=\"M\"/>" +
                            "   </Demo>" +
                            "</PidOptions>";*/
                        if (pidOption != null) {
                            Log.e("PidOptions", pidOption);


                            Intent intent2 = new Intent();
                            intent2.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
                            intent2.putExtra("PID_OPTIONS", pidOption);
                            startActivityForResult(intent2, 2);
                        }
                    }
                } catch (Exception e) {
                    Log.e("Error", e.toString());
                    onMessage("Please enter valid aadhaar number.");
                }
                break;
            case R.id.btnAuthRequest:
                String aadharNo = edtxAdharNo.getText().toString();
                if (aadharNo.contains("-")) {
                    aadharNo = aadharNo.replaceAll("-", "").trim();
                }
                if (aadharNo.length() != 12 || !Verhoeff.validateVerhoeff(aadharNo)) {
                    setText("Please enter valid aadhaar number.");
                } else if (pidData == null) {
                    setText("Please scan your finger.");
                } else if (!pidData._Resp.errCode.equals("0")) {
                    setText("Error: " + pidData._Resp.errInfo);
                } else {
                    new AuthRequest(aadharNo, pidData).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
                break;
            case R.id.btnReset:
                txtOutput.setText("");
                onResetClicked();
                break;

            case R.id.btnSubmit:
                // txtOutput.setText("");

                getBioData();
                break;
        }
    }

  /*  public boolean isValidate() {
        if (TextUtils.isEmpty(binding.edtxAdharNo.getText())) {
            onMessage(getString(R.string.plz_select_top_up_type));
            return false;
        } else if (TextUtils.isEmpty(binding.selectCategory.getText())) {
            onMessage(getString(R.string.plz_select_category));
            return false;
        } else if (TextUtils.isEmpty(binding.selectOperator.getText())) {
            onMessage(getString(R.string.plz_select_operator));
            return false;
        }
        return true;
    }*/

    public void onCheckboxClicked(View view) {
        CheckBox cb = (CheckBox) view;
        boolean checked = cb.isChecked();
        if (checked) {
            int pos = spinnerTotalFingerCount.getSelectedItemPosition();
            if ((pos + 1) > fingerCount) {
                fingerCount++;
                positions.add(cb.getText().toString());
            } else {
                ((CheckBox) view).setChecked(false);
                Toast.makeText(this, "Please Select Total Finger Count Proper", Toast.LENGTH_LONG).show();
            }
        } else {
            fingerCount--;
            String val = cb.getText().toString();
            if (positions.contains(val)) {
                positions.remove(val);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void onResetClicked() {
        fingerCount = 0;
        edtxTimeOut.setText("10000");
        edtxAdharNo.setText("");
        edtxPidVer.setText("2.0");
        spinnerTotalFingerCount.setSelection(0);
        spinnerTotalFingerType.setSelection(0);
        spinnerTotalFingerFormat.setSelection(0);
//        spinnerEnv.setSelection(0);
        chbxLeftIndex.setChecked(false);
        chbxLeftMiddle.setChecked(false);
        chbxLeftRing.setChecked(false);
        chbxLeftSmall.setChecked(false);
        chbxLeftThumb.setChecked(false);
        chbxRightIndex.setChecked(false);
        chbxRightMiddle.setChecked(false);
        chbxRightRing.setChecked(false);
        chbxRightSmall.setChecked(false);
        chbxRightThumb.setChecked(false);
        chbxUnknown.setChecked(false);
        pidData = null;
        positions.clear();
        positions = new ArrayList<>();
    }

    private void setText(final String message) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtOutput.setText(message);
                mainResult = message;
            }
        });
    }


    private String getPIDOptions() {
        try {
            int fingerCount = spinnerTotalFingerCount.getSelectedItemPosition() + 1;
            int fingerType = spinnerTotalFingerType.getSelectedItemPosition();

            spinnerTotalFingerFormat.setSelection(1);

            int fingerFormat = spinnerTotalFingerFormat.getSelectedItemPosition();


            String pidVer = edtxPidVer.getText().toString();
            String timeOut = edtxTimeOut.getText().toString();
            String posh = "UNKNOWN";
            if (positions.size() > 0) {
                posh = positions.toString().replace("[", "").replace("]", "").replaceAll("[\\s+]", "");
            }

            Opts opts = new Opts();
            opts.fCount = String.valueOf(fingerCount);
            opts.fType = String.valueOf(fingerType);
            opts.iCount = "0";
            opts.iType = "0";
            opts.pCount = "0";
            opts.pType = "0";
            opts.format = String.valueOf(fingerFormat);
            opts.pidVer = pidVer;
            opts.timeout = timeOut;
//            opts.otp = "123456";
//            opts.wadh = "Hello";
            opts.posh = posh;
            opts.env = spinnerEnv.getSelectedItem().toString();

            PidOptions pidOptions = new PidOptions();
            pidOptions.ver = "1.0";
            pidOptions.Opts = opts;

            Serializer serializer = new Persister();
            StringWriter writer = new StringWriter();
            serializer.write(pidOptions, writer);
            return writer.toString();
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        if (data != null) {
                            String result = data.getStringExtra("DEVICE_INFO");
                            String rdService = data.getStringExtra("RD_SERVICE_INFO");
                            String display = "";
                            if (rdService != null) {
                                display = "RD Service Info :\n" + rdService + "\n\n";
                            }
                            if (result != null) {
                                /*DeviceInfo info = serializer.read(DeviceInfo.class, result);
                                display = display + "Device Code: " + info.dc + "\n\n"
                                        + "Serial No: " + info.srno + "\n\n"
                                        + "dpId: " + info.dpId + "\n\n"
                                        + "MC: " + info.mc + "\n\n"
                                        + "MI: " + info.mi + "\n\n"
                                        + "rdsId: " + info.rdsId + "\n\n"
                                        + "rdsVer: " + info.rdsVer;*/
                                display += "Device Info :\n" + result;
                                setText(display);
                            }
                        }
                    } catch (Exception e) {
                        Log.e("Error", "Error while deserialze device info", e);
                    }
                }
                break;
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        if (data != null) {
                            String result = data.getStringExtra("PID_DATA");
                            if (result != null) {
                                pidData = serializer.read(PidData.class, result);
                                setText(result);
                                binding.imgFinger.setBackgroundResource(R.drawable.thumb);
                            }
                        }
                    } catch (Exception e) {
                        Log.e("Error", "Error while deserialze pid data", e);
                    }
                }
                break;
        }
    }

    @Override
    public void onProceed() {

    }

    @Override
    public void onCancel(boolean goBack) {

    }

    private class AuthRequest extends AsyncTask<Void, Void, String> {

        private String uid;
        private PidData pidData;
        private ProgressDialog dialog;
        private int posFingerFormat = 0;

        private AuthRequest(String uid, PidData pidData) {
            this.uid = uid;
            this.pidData = pidData;
            dialog = new ProgressDialog(AEPSActivity.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            posFingerFormat = spinnerTotalFingerFormat.getSelectedItemPosition();
            dialog.setMessage("Please wait...");
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                DeviceInfo info = pidData._DeviceInfo;

                Uses uses = new Uses();
                uses.pi = "n";
                uses.pa = "n";
                uses.pfa = "n";
                uses.bio = "y";
                if (posFingerFormat == 1) {
                    uses.bt = "FIR";
                } else {
                    uses.bt = "FMR";
                }
                uses.pin = "n";
                uses.otp = "n";

                Meta meta = new Meta();
                meta.udc = "MANT0";
                meta.rdsId = info.rdsId;
                meta.rdsVer = info.rdsVer;
                meta.dpId = info.dpId;
                meta.dc = info.dc;
                meta.mi = info.mi;
                meta.mc = info.mc;

                AuthReq authReq = new AuthReq();
                authReq.uid = uid;
                authReq.rc = "Y";
                authReq.tid = "registered";
                authReq.ac = "public";
                authReq.sa = "public";
                authReq.ver = "2.0";
                authReq.txn = generateTXN();
                authReq.lk = "MEaMX8fkRa6PqsqK6wGMrEXcXFl_oXHA-YuknI2uf0gKgZ80HaZgG3A"; //AUA
                authReq.skey = pidData._Skey;
                authReq.Hmac = pidData._Hmac;
                authReq.data = pidData._Data;
                authReq.meta = meta;
                authReq.uses = uses;

                StringWriter writer = new StringWriter();
                serializer.write(authReq, writer);
                String pass = "public";
                String reqXML = writer.toString();
                String signAuthXML = XMLSigner.generateSignXML(reqXML, getAssets().open("staging_signature_privateKey.p12"), pass);

                URL url = new URL(getAuthURL(uid));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(30000);
                conn.setConnectTimeout(30000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/xml");
                conn.setUseCaches(false);
                conn.setDefaultUseCaches(false);
                OutputStreamWriter writer2 = new OutputStreamWriter(conn.getOutputStream());
                writer2.write(signAuthXML);
                writer2.flush();
                conn.connect();

                StringBuilder sb = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String response;
                while ((response = reader.readLine()) != null) {
                    sb.append(response).append("\n");
                }
                response = sb.toString();

                AuthRes authRes = serializer.read(AuthRes.class, response);
                String res;
                if (authRes.err != null) {
                    if (authRes.err.equals("0")) {
                        res = "Authentication Success" + "\n"
                                + "Auth Response: " + authRes.ret.toUpperCase() + "\n"
                                + "TXN: " + authRes.txn + "\n"
                                + "";
                    } else {
                        res = "Error Code: " + authRes.err + "\n"
                                + "Auth Response: " + authRes.ret.toUpperCase() + "\n"
                                + "TXN: " + authRes.txn + "\n"
                                + "";
                    }
                } else {
                    res = "Authentication Success" + "\n"
                            + "Auth Response: " + authRes.ret.toUpperCase() + "\n"
                            + "TXN: " + authRes.txn + "\n"
                            + "";
                }
                return res;
            } catch (Exception e) {
                Log.e("Error", "Error while auth request", e);
                return "Error: " + e.toString();
            }
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            if (res != null) {
                setText(res);
            }
            onResetClicked();
            if (dialog.isShowing())
                dialog.dismiss();
        }
    }

    private String generateTXN() {
        try {
            Date tempDate = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.ENGLISH);
            String dTTXN = formatter.format(tempDate);
//            return "UKC:public:" + dTTXN;
            return dTTXN;
        } catch (Exception e) {
            Log.e("generateTXN.Error", e.toString());
            return "";
        }
    }


    private String getAuthURL(String UID) {
        String url = "http://developer.uidai.gov.in/auth/";
//        String url = "http://developer.uidai.gov.in/uidauthserver/";
        url += "public/" + UID.charAt(0) + "/" + UID.charAt(1) + "/";
        url += "MG41KIrkk5moCkcO8w-2fc01-P7I5S-6X2-X7luVcDgZyOa2LXs3ELI"; //ASA
        return url;
    }

    void getBioData() {
        Utils.showCustomProgressDialog(AEPSActivity.this, false);
        GetAEPSTransaction request = new GetAEPSTransaction();
        request.aadhar_number_ = binding.edtxAdharNo.getText().toString();
        request.iin_ = iin_;
        request.amount_ = amount_a;
        request.biometric_data_ = "<PidData" + mainResult.split("PidData")[1] + "PidData>";
        request.txn_code_ = txn_code_;
        request.customer_consent_ = customer_consent_;
        request.txn_latitude_ = "0.0";
        request.txn_longitude_ = "0.0";
        request.terminal_IP_address_ = "10.0.0.1";


        String gKey = KeyHelper.getKey(sessionManager.getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(sessionManager.getMerchantName())).trim();


        String body = RestClient.makeGSONString(request);

        AERequest aeRequest = new AERequest();
        aeRequest.body = AESHelper.encrypt(body.trim(), gKey.trim());


        viewModel.getAEPSData(aeRequest, KeyHelper.getKey(sessionManager.getMerchantName()).trim(), KeyHelper.getSKey(KeyHelper
                .getKey(sessionManager.getMerchantName())).trim()).observe(this
                , response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        //  onMessage(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            Toast.makeText(this, response.resource.description, Toast.LENGTH_SHORT).show();


                            binding.activityMain.setVisibility(View.INVISIBLE);
                            binding.relativeReciept.setVisibility(View.VISIBLE);
                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , gKey);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<AEPS_Trans_Response>() {
                                }.getType();


                                AEPS_Trans_Response data = gson.fromJson(bodyy, type);


                                getTransactionData(data.decrypted_Response);


                                binding.bcName.setText(data.bC_Name);
                                binding.agentId.setText(data.agent_ID);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            showPopup(response.resource.description, "Error", true);
                            //     Toast.makeText(this, response.resource.description, Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }


    private void showPopup(String message, String title, boolean isError) {
        SingleButtonMessageDialog dialog = new
                SingleButtonMessageDialog(title
                , message, this,
                false, isError);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }

    public void getTransactionData(DecryptedResponse getAEPSTransactionResponse) {


        if (txn_code_.equals("31")) {
            binding.transAmountLinear.setVisibility(View.GONE);
            binding.activityMain.setVisibility(View.INVISIBLE);
            binding.relativeReciept.setVisibility(View.VISIBLE);
            // binding.titleService.setText("");
            PDF_Type = "Balance_Enquiry";

        } else if (txn_code_.equals("07")) {
            binding.acBalanceLinear.setVisibility(View.GONE);
            binding.transAmountLinear.setVisibility(View.GONE);
            binding.activityMain.setVisibility(View.INVISIBLE);
            binding.relativeReciept.setVisibility(View.VISIBLE);
            binding.titleService.setText("Mini Statement");
            PDF_Type = "Mini_Statement";
            getColomData();
        } else if (txn_code_.equals("01")) {
            binding.transAmountLinear.setVisibility(View.VISIBLE);
            binding.activityMain.setVisibility(View.INVISIBLE);
            binding.relativeReciept.setVisibility(View.VISIBLE);
            binding.titleService.setText("Cash Withdrawal");
            PDF_Type = "Cash_Withdrawal";
            binding.transAmountAeps.setText(amount_a);
        }

        binding.uidaiCode.setText(getAEPSTransactionResponse.uIDAIAuthenticationCode);
        binding.stanTxt.setText(getAEPSTransactionResponse.sTAN);
        binding.rrnTxt.setText(getAEPSTransactionResponse.rRN);


        String adhar_no_last_4_digit = "", masked_aadhar_no = "";

        if (binding.edtxAdharNo.getText().toString().length() == 12) {
            adhar_no_last_4_digit = binding.edtxAdharNo.getText().toString().substring(8);
            masked_aadhar_no = "********" + adhar_no_last_4_digit;
        } else {
            adhar_no_last_4_digit = binding.edtxAdharNo.getText().toString().substring(12);
            masked_aadhar_no = "************" + adhar_no_last_4_digit;
        }

        binding.adharTxt.setText(masked_aadhar_no);
      //  binding.adharTxt.setText(binding.edtxAdharNo.getText().toString());
       // binding.aepsStatus.setText(getAEPSTransactionResponse.responseCode);
        if (getAEPSTransactionResponse.responseCode == String.valueOf(68)) {
            binding.aepsStatus.setText("Decline" + " (" + getAEPSTransactionResponse.responseCode + ")");
        } else {
            binding.aepsStatus.setText("Successful" + " (" + getAEPSTransactionResponse.responseCode + ")");
        }
        float actualAmount = 0;
        try {
            float parsed = Float.parseFloat(getAEPSTransactionResponse.balanceAmountActual);
            actualAmount = parsed / 100;
        } catch (Exception e) {
            Toast.makeText(this, String.valueOf(e.getLocalizedMessage()), Toast.LENGTH_SHORT).show();
        }

        binding.acBalanceAeps.setText("₹ " + String.valueOf(actualAmount));
      //  binding.transAmountAeps.setText("INR " + getAEPSTransactionResponse.balanceAmount);


        binding.timeAeps.setText(getTime());
        binding.dateAeps.setText(getDate());
        PDF_Name = getAEPSTransactionResponse.retailerTxnId + "_" + PDF_Type;
    }


    private String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getTime() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    ///--------------------------PDF--------------------------

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

        String directory_path = Environment.getExternalStorageDirectory().getAbsolutePath() + PATH;
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
        File outputFile = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + PATH,
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

            if ((ActivityCompat.shouldShowRequestPermissionRationale(AEPSActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(AEPSActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }

            if ((ActivityCompat.shouldShowRequestPermissionRationale(AEPSActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(AEPSActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
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
