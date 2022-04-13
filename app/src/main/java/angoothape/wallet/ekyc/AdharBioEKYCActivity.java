package angoothape.wallet.ekyc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.mantra.mfs100.FingerData;
import com.mantra.mfs100.MFS100;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restResponse.BiometricKYCErrorResponse;
import angoothape.wallet.di.JSONdi.restResponse.aepssattlement.AEPSSettlementTransaction;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.dialogs.SingleButtonMessageDialog;
import angoothape.wallet.interfaces.OnDecisionMade;
import angoothape.wallet.utils.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import angoothape.wallet.R;
import angoothape.wallet.aeps.global.Verhoeff;
import angoothape.wallet.aeps.maskedittext.MaskedEditText;
import angoothape.wallet.di.model.DeviceInfo;
import angoothape.wallet.di.model.Opts;
import angoothape.wallet.di.model.PidData;
import angoothape.wallet.di.model.PidOptions;
import angoothape.wallet.di.model.uid.AuthReq;
import angoothape.wallet.di.model.uid.AuthRes;
import angoothape.wallet.di.model.uid.Meta;
import angoothape.wallet.di.model.uid.Uses;
import angoothape.wallet.aeps.signer.XMLSigner;
import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.ActivityAdharBioEKYCBinding;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AdharBioKycRequest;
import angoothape.wallet.ekyc.viewmodels.EKYCViewModel;

public class AdharBioEKYCActivity extends RitmanBaseActivity<ActivityAdharBioEKYCBinding> implements OnDecisionMade {

    EKYCViewModel viewModel;
    String mainResult;
    @BindView(R.id.spinnerTotalFingerCount)
    Spinner spinnerTotalFingerCount;
    @BindView(R.id.linearFingerCount)
    LinearLayout linearFingerCount;
    @BindView(R.id.spinnerTotalFingerType)
    Spinner spinnerTotalFingerType;

    @BindView(R.id.spinnerFingerType)
    Spinner spinnerFingerType;

    @BindView(R.id.deviceType)
    Spinner spinnerdeviceType;

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

    @BindView(R.id.btnSubmit)
    Button btnSubmit;

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

    //For Image
    private static long mLastClkTime = 0;
    private static long Threshold = 1500;

    @Override
    public void onProceed() {
        finish();
    }

    @Override
    public void onCancel(boolean goBack) {

    }

    private enum ScannerAction {
        Capture
    }

    private FingerData lastCapFingerData = null;
    ScannerAction scannerAction = ScannerAction.Capture;
    MFS100 mfs100 = null;
    int timeout = 10000;
    private boolean isCaptureRunning = false;
    int deviceType;
    String FingerType;
    String Thumb;
    String deviceTypeItem;
    DeviceInfo info;
    String mobile_no, kyc_token, wadh_value;

    @Override
    public int getLayoutId() {
        return R.layout.activity_adhar_bio_e_k_y_c;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {


        //  viewModel = ((EKYCMainActivity) getBaseActivity()).viewModel;
        viewModel = new ViewModelProvider(this).get(EKYCViewModel.class);

        ButterKnife.bind(this);
        mobile_no = getIntent().getExtras().getString("mobile_no");
        kyc_token = getIntent().getExtras().getString("kyc_token");
        wadh_value = getIntent().getExtras().getString("wadh_value");

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        positions = new ArrayList<>();
        serializer = new Persister();

       /* try {
            mfs100 = new MFS100(this);
            mfs100.SetApplicationContext(AdharBioEKYCActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        String[] arrayFingerTypeThumb = {"Right Thumb", "Left Thumb"};
        String[] arraydeviceType = {"Morpho Device", "3M Device", "Startek Device", "Precision Device", "Secugen Device", "Mantra Device"};

        ArrayAdapter adptDevice = new ArrayAdapter(AdharBioEKYCActivity.this, android.R.layout.simple_list_item_1, arraydeviceType);
        adptDevice.setDropDownViewResource(android.R.layout.simple_list_item_1);

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayFingerTypeThumb);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        binding.spinnerFingerType.setAdapter(aa);
        binding.deviceType.setAdapter(adptDevice);

        binding.spinnerFingerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {

                if (position == 0) {
                    Thumb = "6~RightThumb";
                } else if (position == 1) {
                    Thumb = "5~LeftThumb";
                }
            }

            @Override
            public void onNothingSelected(AdapterView parent) {

            }
        });

        binding.deviceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {

                //deviceTypeItem = parent.getItemAtPosition(position).toString();

                if (position == 0) {
                    deviceTypeItem = "1";
                } else if (position == 1) {
                    deviceTypeItem = "2";
                } else if (position == 2) {
                    deviceTypeItem = "3";
                } else if (position == 3) {
                    deviceTypeItem = "4";
                } else if (position == 4) {
                    deviceTypeItem = "5";
                } else if (position == 5) {
                    deviceTypeItem = "6";
                }

                /*binding.countryspii.setSelection(position);
                binding.countrysp.setText(item);

                if (position == 0) {
                    binding.countrysp.setText("laos");

                } else if (position == 1) {
                    binding.countrysp.setText("laos is");
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView parent) {

            }
        });
        edtxAdharNo.setText("");
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

               /* scannerAction = ScannerAction.Capture;
                if (!isCaptureRunning) {
                    StartSyncCapture();
                }*/

                try {
                    String aadharNo1 = binding.edtxAdharNo.getText().toString();
                    //if (binding.edtxAdharNo.length() != 12 || !Verhoeff.validateVerhoeff(aadharNo1)) {
                    if (binding.edtxAdharNo.length() != 12) {
                        onMessage("Please enter valid aadhaar number.");
                    }/* else if (customer_consent_=="0"){
                        onMessage("Please select Customer Consent (YES)");
                    }*/ else {

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
                // if (aadharNo.length() != 12 || !Verhoeff.validateVerhoeff(aadharNo)) {
                if (aadharNo.length() != 12) {
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
            opts.wadh = wadh_value;
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                                //  binding.imgFinger.setBackground(getResources().getDrawable(R.drawable.thumb));
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

                                // binding.imgFinger.setBackground(getResources().getDrawable(R.drawable.thumb));
                                binding.imgFinger.setBackgroundResource(R.drawable.thumb);

                                scannerAction = ScannerAction.Capture;
                                if (!isCaptureRunning) {
                                    //StartSyncCapture();
                                }
                            }
                        }
                    } catch (Exception e) {
                        Log.e("Error", "Error while deserialze pid data", e);
                    }
                }
                break;
        }
    }

    private class AuthRequest extends AsyncTask<Void, Void, String> {

        private String uid;
        private PidData pidData;
        private ProgressDialog dialog;
        private int posFingerFormat = 0;

        private AuthRequest(String uid, PidData pidData) {
            this.uid = uid;
            this.pidData = pidData;
            dialog = new ProgressDialog(AdharBioEKYCActivity.this);
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
        Utils.showCustomProgressDialog(this, false);
        AdharBioKycRequest request = new AdharBioKycRequest();
        request.mobile_no = mobile_no;//viewModel.mobile.getValue();
        request.kyc_token = kyc_token;//viewModel.kycToken.getValue();
        request.aadhaar_type = "0";
        request.aadhaar_number = binding.edtxAdharNo.getText().toString();
        request.cert_expiry_date = "";//optional
        request.device_data_xml = mainResult;
        request.finger_data = Thumb;
        request.serial_number = "2051I011673";//"3782822";//morpho srno 2051I011673//"3782822";//
        request.session_key = "";//optional
        request.time_stamp = "";//optional
        request.type = deviceTypeItem;// mantra "6";//deviceTypeItem;morpho 1
        request.version_number = "1.1.5";// optional // mandatory for morpho device
        request.consent = "Y";// default value
        request.ekyc_type = "EKYC";// default value
        request.captured_template = "null";// default value
        request.purpose = "Authentication";// default value
        request.wadh_value = wadh_value;//viewModel.wadh.getValue();


        String gKey = KeyHelper.getKey(sessionManager.getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(sessionManager.getMerchantName())).trim();


        String body = RestClient.makeGSONString(request);

        AERequest aeRequest = new AERequest();
        aeRequest.body = AESHelper.encrypt(body.trim(), gKey.trim());


        viewModel.YBAdharBioKYC(aeRequest, KeyHelper.getKey(sessionManager.getMerchantName()).trim(), KeyHelper.getSKey(KeyHelper
                .getKey(sessionManager.getMerchantName())).trim()).observe(this
                , response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            showPopup(response.resource.description, "Successfully", true);
                        } else {
                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , gKey);
                            if(bodyy != null) {
                                try {

                                    Gson gson = new Gson();
                                    Type type = new TypeToken<BiometricKYCErrorResponse>() {
                                    }.getType();
                                    BiometricKYCErrorResponse data = gson.fromJson(bodyy, type);
                                    showPopup(data.responseMessage, "Error", true);
                                } catch (Exception e) {
                                    showPopup(response.resource.description, "Error", true);
                                }
                            } else {
                                showPopup(response.resource.description, "Error", true);
                            }



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

   /* private void StartSyncCapture() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                SetTextOnUIThread("");
                isCaptureRunning = true;
                try {
                    FingerData fingerData = new FingerData();
                    int ret = mfs100.AutoCapture(fingerData, timeout, binding.cbFastDetection.isChecked());
                    Log.e("StartSyncCapture.RET", "" + ret);
                    if (ret != 0) {
                        SetTextOnUIThread(mfs100.GetErrorMsg(ret));
                    } else {
                        lastCapFingerData = fingerData;

                        final Bitmap bitmap = BitmapFactory.decodeByteArray(fingerData.FingerImage(), 0,
                                fingerData.FingerImage().length);
                        AdharBioEKYCActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.imgFinger.setImageBitmap(bitmap);
                            }
                        });

//                        Log.e("RawImage", Base64.encodeToString(fingerData.RawData(), Base64.DEFAULT));
//                        Log.e("FingerISOTemplate", Base64.encodeToString(fingerData.ISOTemplate(), Base64.DEFAULT));
                        SetTextOnUIThread("Capture Success");
                        String log = "\nQuality: " + fingerData.Quality()
                                + "\nNFIQ: " + fingerData.Nfiq()
                                + "\nWSQ Compress Ratio: "
                                + fingerData.WSQCompressRatio()
                                + "\nImage Dimensions (inch): "
                                + fingerData.InWidth() + "\" X "
                                + fingerData.InHeight() + "\""
                                + "\nImage Area (inch): " + fingerData.InArea()
                                + "\"" + "\nResolution (dpi/ppi): "
                                + fingerData.Resolution() + "\nGray Scale: "
                                + fingerData.GrayScale() + "\nBits Per Pixal: "
                                + fingerData.Bpp() + "\nWSQ Info: "
                                + fingerData.WSQInfo();
                        SetLogOnUIThread(log);
                        //SetData2(fingerData);
                    }
                } catch (Exception ex) {
                    SetTextOnUIThread("Error");
                } finally {
                    isCaptureRunning = false;
                }
            }
        }).start();
    }

    private void SetTextOnUIThread(final String str) {

        binding.lblMessage.post(new Runnable() {
            public void run() {
                try {
                    binding. lblMessage.setText(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void SetLogOnUIThread(final String str) {

        txtOutput.post(new Runnable() {
            public void run() {
                try {
                    txtOutput.append("\n" + str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    //
    private long mLastAttTime=0l;
    @Override
    public void OnDeviceAttached(int vid, int pid, boolean hasPermission) {
        if (SystemClock.elapsedRealtime() - mLastAttTime < Threshold) {
            return;
        }
        mLastAttTime = SystemClock.elapsedRealtime();
        int ret;
        if (!hasPermission) {
            SetTextOnUIThread("Permission denied");
            return;
        }
        try {
            if (vid == 1204 || vid == 11279) {
                if (pid == 34323) {
                    ret = mfs100.LoadFirmware();
                    if (ret != 0) {
                        SetTextOnUIThread(mfs100.GetErrorMsg(ret));
                    } else {
                        SetTextOnUIThread("Load firmware success");
                    }
                } else if (pid == 4101) {
                    String key = "Without Key";
                    ret = mfs100.Init();
                    if (ret == 0) {
                        showSuccessLog(key);
                    } else {
                        SetTextOnUIThread(mfs100.GetErrorMsg(ret));
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSuccessLog(String key) {
        try {
            SetTextOnUIThread("Init success");
            String info = "\nKey: " + key + "\nSerial: "
                    + mfs100.GetDeviceInfo().SerialNo() + " Make: "
                    + mfs100.GetDeviceInfo().Make() + " Model: "
                    + mfs100.GetDeviceInfo().Model()
                    + "\nCertificate: " + mfs100.GetCertification();
            SetLogOnUIThread(info);
        } catch (Exception e) {
        }
    }

    long mLastDttTime=0l;

    @Override
    public void OnDeviceDetached() {

        try {

            if (SystemClock.elapsedRealtime() - mLastDttTime < Threshold) {
                return;
            }
            mLastDttTime = SystemClock.elapsedRealtime();
            // UnInitScanner();

            SetTextOnUIThread("Device removed");
        } catch (Exception e) {
        }

    }

    @Override
    public void OnHostCheckFailed(String err) {
        try {
            SetLogOnUIThread(err);
            Toast.makeText(this, err, Toast.LENGTH_LONG).show();
        } catch (Exception ignored) {
        }

    }*/


}