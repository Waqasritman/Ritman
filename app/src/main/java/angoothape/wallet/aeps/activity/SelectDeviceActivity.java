package angoothape.wallet.aeps.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;


import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import angoothape.wallet.AEPSActivity;
import angoothape.wallet.R;
import angoothape.wallet.aeps.viewmodels.AEPSViewModel;
import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.ActivitySelectDeviceBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.SimpleRequest;
import angoothape.wallet.di.JSONdi.restResponse.IINListResponse;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.dialogs.IINListDialog;
import angoothape.wallet.dialogs.SingleButtonMessageDialog;
import angoothape.wallet.interfaces.InterfaceIINList;
import angoothape.wallet.interfaces.OnDecisionMade;
import angoothape.wallet.utils.Utils;

public class SelectDeviceActivity extends RitmanBaseActivity<ActivitySelectDeviceBinding>
        implements OnDecisionMade, InterfaceIINList {

    //  String morpho, matchdata, evolute, mantra;

    Integer value;
    // boolean temp = true;
    List<IINListResponse> IINList;
    AEPSViewModel viewModel;
    String IINNo, Amount, TxnTypeCode, BankName;
    int place;

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_device;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {

        binding.toolBar.titleTxt.setText("AEPS");
        binding.toolBar.backBtn.setOnClickListener(v -> onBackPressed());
        binding.toolBar.toolBarFinal
                .setBackgroundColor(getResources().getColor(R.color.posGreen));
        binding.toolBar.backBtn
                .setColorFilter(ContextCompat.getColor(this,
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);
        binding.toolBar.crossBtn.setOnClickListener(v -> {
            onClose();
        });

        viewModel = new ViewModelProvider(this).get(AEPSViewModel.class);
        IINList = new ArrayList<>();

        String[] arrayServiceType = {"Cash Withdrawal", "Balance Enquiry", "Mini Statement"};
        binding.mantra.setOnClickListener(v -> {
            value = 1;
            showSucces("Mantra Device", getString(R.string.mantra_rd), false);
        });
        binding.morpho.setOnClickListener(v -> {
            value = 2;
            showSucces("Morpho Device", getString(R.string.morpho_rd), false);
        });
        binding.evolute.setOnClickListener(v -> {
            value = 3;
            showSucces("Evolute Device", getString(R.string.evolute_rd), false);
        });
        binding.pairButton.setOnClickListener(v ->
                binding.devicePairLinear.setVisibility(v.getVisibility()));
        binding.btnNext.setOnClickListener(v -> nextClick());
        binding.switchPair.setOnCheckedChangeListener(
                (compoundButton, b) -> {
                    if (binding.switchPair.isChecked()) {
                        binding.devicePairLinear.setVisibility(View.VISIBLE);
                    } else {
                        binding.devicePairLinear.setVisibility(View.GONE);
                    }
                });

        ArrayAdapter adptService = new ArrayAdapter(SelectDeviceActivity.this, android.R.layout.simple_list_item_1, arrayServiceType);
        adptService.setDropDownViewResource(android.R.layout.simple_list_item_1);
        binding.serviceType.setAdapter(adptService);
        binding.serviceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {
                if (position == 0) {
                    place = 1;
                    TxnTypeCode = "01";
                    binding.tAmount.setText("");
                    binding.tAmount.setEnabled(true);
                } else if (position == 1) {
                    place = 2;
                    TxnTypeCode = "31";
                    binding.tAmount.setText("0");
                    binding.tAmount.setEnabled(false);
                } else if (position == 2) {
                    place = 3;
                    TxnTypeCode = "07";
                    binding.tAmount.setText("0");
                    binding.tAmount.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView parent) {
            }
        });


        binding.selectBank.setOnClickListener(v -> {
            Utils.showCustomProgressDialog(SelectDeviceActivity.this, false);
            String gKey = KeyHelper.getKey(sessionManager.getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                    .getKey(sessionManager.getMerchantName())).trim();

            SimpleRequest simpleRequest = new SimpleRequest();
            String body = RestClient.makeGSONString(simpleRequest);

            AERequest request = new AERequest();
            request.body = AESHelper.encrypt(body.trim(), gKey.trim());

            viewModel.getIIN(request, KeyHelper.getKey(sessionManager.getMerchantName()).trim(), KeyHelper.getSKey(KeyHelper
                    .getKey(sessionManager.getMerchantName())).trim()).observe(SelectDeviceActivity.this
                    , response -> {
                        Utils.hideCustomProgressDialog();
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {

                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , gKey);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<IINListResponse>>() {
                                }.getType();
                                List<IINListResponse> data = gson.fromJson(bodyy, type);
                                IINList(data);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Utils.hideCustomProgressDialog();
                            if (response.resource.data != null) {
                                String bodyy = AESHelper.decrypt(response.resource.data.body
                                        , gKey);
                                if (!body.isEmpty()) {
                                    onError(bodyy);
                                } else {
                                    onError(response.resource.description);
                                }
                            } else {
                                onError(response.resource.description);
                            }
                        }

                    });
        });
    }


    public void isValidate() {
        if (TextUtils.isEmpty(binding.selectBank.getText())) {
            onMessage(getString(R.string.plz_select_bank));
        } else {
            if (place == 1) {
                try {
                    int Amount_1 = Integer.parseInt(binding.tAmount.getText().toString());
                    if (Amount_1 >= 100 && Amount_1 <= 10000) {
                        Amount = binding.tAmount.getText().toString();
                        Intent ii = new Intent(this, AEPSActivity.class);
                        ii.putExtra("Amount", Amount);
                        ii.putExtra("IINNo", IINNo);
                        ii.putExtra("BankName", BankName);
                        ii.putExtra("TxnTypeCode", TxnTypeCode);
                        startActivity(ii);
                    } else {
                        onMessage("Amount should be 100 to 10000");
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    onMessage("Amount should be 100 to 10000");
                }

            } else {
                Amount = binding.tAmount.getText().toString();
                Intent ii = new Intent(this, AEPSActivity.class);
                ii.putExtra("Amount", Amount);
                ii.putExtra("IINNo", IINNo);
                ii.putExtra("BankName", BankName);
                ii.putExtra("TxnTypeCode", TxnTypeCode);
                startActivity(ii);
            }
        }
    }


    private void showSucces(String message, String title, boolean isError) {
        SingleButtonMessageDialog dialog = new
                SingleButtonMessageDialog(title
                , message, this,
                false, isError);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }

    @Override
    public void onProceed() {
        if (value == 1) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.mantra.rdservice"));
            startActivity(intent);

        }
        if (value == 2) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.scl.rdservice"));
            startActivity(intent);
        }
        if (value == 3) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.evolute.rdservice"));
            startActivity(intent);
        }

    }


    @Override
    public void IINList(List<IINListResponse> IINlist) {
        IINList.clear();
        IINList.addAll(IINlist);
        showIINDialog();
    }

    @Override
    public void onSelectIINList(IINListResponse SelectIIN) {
        binding.selectBank.setText(SelectIIN.Issuer_Bank_Name);
        IINNo = SelectIIN.IIN;
        BankName = SelectIIN.Issuer_Bank_Name;
    }

    void showIINDialog() {
        IINListDialog dialog = new IINListDialog(IINList
                , this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }

    void nextClick() {
        isValidate();
    }
}