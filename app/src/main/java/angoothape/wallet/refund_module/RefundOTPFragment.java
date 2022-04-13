package angoothape.wallet.refund_module;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;

import angoothape.wallet.R;
import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.GenerateOtpFragmentLayoutBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.RefundTransactionRequest;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.utils.Utils;

public class RefundOTPFragment extends RitmanBaseActivity<GenerateOtpFragmentLayoutBinding> {


    String txnNo;
    RefundViewModel viewModel;


    protected void injectView() {

    }


    public boolean isValidate() {
        if (binding.edtOtp.getText().toString().isEmpty()) {
            onMessage(getString(R.string.enter_otp));
            return false;
        }
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.generate_otp_fragment_layout;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(RefundViewModel.class);
        txnNo = getIntent().getExtras().getString("txn_no", "");

        binding.btnVerifyOtp.setOnClickListener(v -> {
            if (isValidate()) {
                Utils.showCustomProgressDialog(this, false);
                String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                        .getKey(getSessionManager().getMerchantName())).trim();

                RefundTransactionRequest otpRequest = new RefundTransactionRequest();
                otpRequest.Txn_no = txnNo;
                otpRequest.Refund_otp = binding.edtOtp.getText().toString();
                String body = RestClient.makeGSONString(otpRequest);
                Log.e("body", body);
                AERequest request = new AERequest();
                request.body = AESHelper.encrypt(body.trim(), gKey.trim());


                viewModel.refundTransaction(request
                        , KeyHelper.getKey(getSessionManager().getMerchantName()).trim(), KeyHelper.getSKey(KeyHelper
                                .getKey(getSessionManager().getMerchantName())).trim())
                        .observe(this, response -> {

                            Utils.hideCustomProgressDialog();
                            if (response.status == Status.ERROR) {
                                onError(getString(response.messageResourceId));
                            } else if (response.status == Status.SUCCESS) {
                                assert response.resource != null;
                                onMessage(response.resource.description);
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

                        });
            }
        });
    }
}
