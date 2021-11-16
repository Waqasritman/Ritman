package angoothape.wallet.home_module;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;


import angoothape.wallet.CustomerTransactionHistoryActivity;
import angoothape.wallet.EditProfileActivity;
import angoothape.wallet.FundingBankActivity;
import angoothape.wallet.MerchantLedgerActivity;
import angoothape.wallet.NotificationActivity;
import angoothape.wallet.TransactionHistory.TransactionHistoryActivity;
import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.beneficiary_list_module.BeneficiaryListActivity;
import angoothape.wallet.billpayment.BillPaymentMainActivity;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.restResponse.AEPS_Trans_Response;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.dialogs.AlertDialog;
import angoothape.wallet.MoneyTransferModuleV.MoneyTransferMainLayout;

import angoothape.wallet.menumodules.CustomerServiceDetailsActivity;
import angoothape.wallet.menumodules.SettingsActivity;
import angoothape.wallet.R;
import angoothape.wallet.databinding.ActivityNewDashboardBinding;


import androidx.core.content.ContextCompat;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;

import angoothape.wallet.home_module.fragments.HomeFragment;
import angoothape.wallet.home_module.viewmodel.HomeViewModel;
import angoothape.wallet.interfaces.OnDecisionMade;
import angoothape.wallet.menumodules.ChangePinActivity;
import angoothape.wallet.qrcodemodule.QrCodeActivity;
import angoothape.wallet.utils.BitmapHelper;


public class NewDashboardActivity extends RitmanBaseActivity<ActivityNewDashboardBinding>
        implements OnDecisionMade {

    int[] btnIds;
    int[] textIds;

    public HomeViewModel homeViewModel;
    boolean isLogout = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_new_dashboard;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        moveToFragment(new HomeFragment());

        btnIds = new int[]{binding.inculdeLayout.homeImg.getId()
                , binding.inculdeLayout.historyImg.getId()};

        textIds = new int[]{binding.inculdeLayout.homeTv.getId()
                , binding.inculdeLayout.historyTv.getId()};

        binding.settings.setOnClickListener(view -> startActivity(new
                Intent(getApplicationContext(), SettingsActivity.class)));

        binding.changePin.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), ChangePinActivity.class));
        });


        binding.ledgerHistory.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), MerchantLedgerActivity.class));
        });

        binding.banks.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), FundingBankActivity.class));
        });

        binding.editBeneficiaryList.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), BeneficiaryListActivity.class));
        });

//        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
//                .getKey(getSessionManager().getMerchantName())).trim();
//        Log.e("gKey: ", gKey);
//        String bodyy = AESHelper.decrypt("8isXkblNu5rp8H12Scp9vNq1xxVEwk3e15DOJDz7u+ne8R7ehieSM8sQymJ88beJ+1qiyf53LE0NyIB1nqBsxOeyUsZVK4yg+dE31ipI3z5ZXbqcIuhdiZDVE9kAm1qormXSMkWpShYgZzSUHNrTTTBpsPEkl0cPZ5HvkNNVKDEReq5/FrUZR2mNK+hZAiGuCc1XZsbPWcSgtLu8IVRrtE5tnLUR082CmO5mSAubyzGhUi5+XD0GLawq9a9hj7o1CrRDm1hxlIxKHsCXNKOoVbiXLWSddF/uv/vKJqWpBANEs427kVPA8BG0equKEz10sEtSedzmh2w7K1N2bHitnGBIZ/QXatw5DoyqCCMRS3QR9H5PUDahbGdbNBKANL5BMCM35OpAmn6vVmYJ8xc7B++R2JbVRzweFNfpB0xAOQv/TFe+M7wNCLt1TGt+lldskOv9W35PFbL0MpqM6VjxPWnNHf4Uv3kN38kygmwclWew5dEGKqVd1Yb2l2olxyUcBL38AgoSTtheocHL2VMD6NmrZp38vXrgTM5P16P0NmTB0b+8bujtYQ2RhF5aInzqoeVKufS89PGCqXtvejSdvJA4ns8nXDkganb5bVj5DfUsINs5LFjVb1NFPkQNdyAV+vCGM4AAe42E5wvJQ6GDWDjJWjGlGN2eSghwvFmYN+E7ZxReVhD+zCs2Y5vS+WqFS6aE50PV692ODl+v1jg33MYP8T04DjGLW2EP7GGADB/8Am1cgqydlG7d2OQ3cYrxJcD+TbeAC3T66184B2lnybrNtxIDYAU9x4MZmSy4eiu1nx7CTC/K7WqZjKGLw1CPonEVZS9d5QMKwqOZV0Kgp14CFSD23spG3ywOtQDUGu6z3ac4xYmymR1PAsjNKAcM+3ZSkvrcrp2PWSy1hbiCQg==", gKey);
//
//        Log.e("setUp: ", bodyy);
//
//        try {
//            Gson gson = new Gson();
//            Type type = new TypeToken<AEPS_Trans_Response>() {
//            }.getType();
//
//
//            AEPS_Trans_Response data = gson.fromJson(bodyy, type);
//
//            float actualAmount = 0;
//            try {
//                float parsed = Float.parseFloat(data.decrypted_Response.balanceAmountActual);
//                actualAmount = parsed / 100;
//            } catch (Exception e) {
//                Toast.makeText(this, String.valueOf(e.getLocalizedMessage()), Toast.LENGTH_SHORT).show();
//            }
//
//
//            Log.e("setUp: ", String.valueOf(actualAmount));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        binding.customerTransactionHistory.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), CustomerTransactionHistoryActivity.class));
        });

        binding.customerService.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), CustomerServiceDetailsActivity.class));
        });


//        binding.inculdeLayout.refreshBalance.setOnClickListener(v -> {
//            startActivity(new Intent(getApplicationContext(), WalletViaQRCodeActivity.class));
//        });


//        binding.myQrCodeLayout.setOnClickListener(v -> {
//            startActivity(new Intent(getApplicationContext(), QrCodeActivity.class));
//        });

        binding.inculdeLayout.send.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), MoneyTransferMainLayout.class));
        });

        binding.inculdeLayout.historyLayout.setOnClickListener(view -> {
         //   changeImageColor(binding.inculdeLayout.historyImg.getId());
           // binding.inculdeLayout.historyTv.setTextColor(getResources().getColor(R.color.colorPrimary));
           // binding.inculdeLayout.homeTv.setTextColor(getResources().getColor(R.color.colorDarkGray));
       //     moveToFragment(new MainHistoryFragment());
          startActivity(new Intent(getApplicationContext(), TransactionHistoryActivity.class));
        });


//        binding.inculdeLayout.homeLayout.setOnClickListener(view -> {
//            changeImageColor(binding.inculdeLayout.homeImg.getId());
//            binding.inculdeLayout.historyTv.setTextColor(getResources().getColor(R.color.colorDarkGray));
//            binding.inculdeLayout.homeTv.setTextColor(getResources().getColor(R.color.colorPrimary));
//            moveToFragment(new HomeFragment());
//        });


        binding.inculdeLayout.drawerImgBtn.setOnClickListener(v -> {
            binding.drawerLayout.isDrawerOpen(Gravity.START);
            binding.drawerLayout.openDrawer(Gravity.LEFT);
        });


        binding.logout.setOnClickListener(v -> {
            isLogout = true;
            AlertDialog alertDialog = new AlertDialog(getString(R.string.logout_txt)
                    , getString(R.string.logout_message), this);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            alertDialog.show(transaction, "");
        });


        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), PackageManager.GET_ACTIVITIES);
            binding.versionNumber.setText(getString(R.string.version).concat(" ")
                    .concat(String.valueOf(info.versionName)));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();


    }


    public void hideHumBurger(int id) {
        binding.inculdeLayout.drawerImgBtn.setVisibility(View.GONE);
        binding.inculdeLayout.refreshBalance.setVisibility(View.GONE);
        binding.inculdeLayout.notifiacaitonBtn.setVisibility(View.GONE);
        if (id == ClassChangerHelper.HOME) {
            changeImageColor(binding.inculdeLayout.homeImg.getId());
            changeTextColor(binding.inculdeLayout.homeTv.getId());
            binding.inculdeLayout.drawerImgBtn.setVisibility(View.VISIBLE);
            binding.inculdeLayout.refreshBalance.setVisibility(View.VISIBLE);
            binding.inculdeLayout.notifiacaitonBtn.setVisibility(View.VISIBLE);
        } else if (id == ClassChangerHelper.HISTORY) {
            changeImageColor(binding.inculdeLayout.historyImg.getId());
            changeTextColor(binding.inculdeLayout.historyTv.getId());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        walletBalance = "";
        clearUserData();
    }

    public void changeImageColor(int id) {
        for (int btnId : btnIds) {
            ImageView imageView = (ImageView) findViewById(btnId);
            if (btnId == id) {
                imageView.setColorFilter(ContextCompat.getColor(this,
                        R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
            } else {
                imageView.setColorFilter(ContextCompat.getColor(this,
                        R.color.colorDarkGray), android.graphics.PorterDuff.Mode.SRC_IN);
            }
        }
    }


    public void changeTextColor(int id) {
        for (int txtIds : textIds) {
            TextView textView = findViewById(txtIds);
            if (txtIds == id) {
                textView.setTextColor(getResources().getColor(R.color.colorPrimary));
            } else {
                textView.setTextColor(getResources().getColor(R.color.colorDarkGray));
            }
        }
    }

    static final int TIME_INTERVAL = 2000;
    long mBackPressed;

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            showExitDialog();
            return;
        } else {
            showExitDialog();
        }
        mBackPressed = System.currentTimeMillis();
    }


    public void showExitDialog() {
        isLogout = true;
        AlertDialog alertDialog = new AlertDialog(getString(R.string.exit)
                , getString(R.string.are_sure_exit), this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        alertDialog.show(transaction, "");
    }

    public void moveToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame, fragment).commit();
    }


    @Override
    public void onProceed() {
        if (isLogout) {
            // finish();
            finishAffinity();
        }
    }

    @Override
    public void onCancel(boolean goBack) {

    }


    @Override
    public void onCancel() {

    }
}
