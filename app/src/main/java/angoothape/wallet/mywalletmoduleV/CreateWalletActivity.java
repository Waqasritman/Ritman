package angoothape.wallet.mywalletmoduleV;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

import angoothape.wallet.R;
import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.ActivityCreateWalletBinding;
import angoothape.wallet.di.XMLdi.RequestHelper.CreateWalletRequest;
import angoothape.wallet.di.XMLdi.RequestHelper.GetCCYForWalletRequest;
import angoothape.wallet.di.XMLdi.ResponseHelper.GetSendRecCurrencyResponse;
import angoothape.wallet.di.XMLdi.apicaller.CreateWalletTask;
import angoothape.wallet.di.XMLdi.apicaller.GetCCYForWalletTask;
import angoothape.wallet.dialogs.DialogCurrency;
import angoothape.wallet.interfaces.OnSelectCurrency;
import angoothape.wallet.interfaces.OnSuccessMessage;
import angoothape.wallet.utils.IsNetworkConnection;
import angoothape.wallet.utils.WalletTypeHelper;

public class CreateWalletActivity extends RitmanBaseActivity<ActivityCreateWalletBinding>
        implements OnSelectCurrency, OnSuccessMessage {

    CreateWalletRequest createWalletRequest;

    @Override
    public int getLayoutId() {
        return R.layout.activity_create_wallet;
    }


    @Override
    protected void initUi(Bundle savedInstanceState) {
        createWalletRequest = new CreateWalletRequest();

        binding.sendingCurrencyLayout.setOnClickListener(v -> {
            showDialog();
        });

        binding.toolBar.backBtn.setOnClickListener(v -> {
            finish();
        });

        binding.createWallet.setOnClickListener(v -> {

            if(sessionManager.getISKYCApproved()) {
                if (!TextUtils.isEmpty(binding.requestCurrency.getText().toString())) {
                    if (IsNetworkConnection.checkNetworkConnection(this)) {
                        createWalletRequest.customerNo = sessionManager.getCustomerNo();
                        createWalletRequest.languageId = sessionManager.getlanguageselection();

                        CreateWalletTask task = new CreateWalletTask(this, this);
                        task.execute(createWalletRequest);

                    } else {
                        onMessage(getString(R.string.no_internet));
                    }
                } else {
                    onMessage(getString(R.string.select_new_wallet));
                }
            } else {
                onMessage(getString(R.string.please_complete_kyc));
            }


        });
    }


    public void showDialog() {
        if (IsNetworkConnection.checkNetworkConnection(this)) {
            GetCCYForWalletRequest request = new GetCCYForWalletRequest();
            request.customerNo = sessionManager.getCustomerNo();
            request.languageID = sessionManager.getlanguageselection();
            request.actionType = WalletTypeHelper.CREATE;

            GetCCYForWalletTask task = new GetCCYForWalletTask(this, this);
            task.execute(request);

        } else {
            onMessage(getString(R.string.no_internet));
        }
    }


    public void setReceivingCurrencyImage(String url) {
        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource,
                                                @Nullable Transition<? super Bitmap> transition) {
                        binding.requestCurrencyImage.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    @Override
    public void onSuccess(String s) {
        onMessage(s);
        sessionManager.putWalletNeedToUpdate(true);
        Handler mHandler;
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.removeCallbacks(this);
                finish();
            }
        }, 400);
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }

    @Override
    public void onCurrencyResponse(List<GetSendRecCurrencyResponse> response) {
        DialogCurrency dialogCurrency = new DialogCurrency(response, this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        dialogCurrency.show(transaction, "");
    }

    @Override
    public void onCurrencySelect(GetSendRecCurrencyResponse response) {
        binding.requestCurrency.setText(response.currencyShortName);
        createWalletRequest.walletCurrency = response.currencyShortName;
        setReceivingCurrencyImage(response.image_URL);
    }
}