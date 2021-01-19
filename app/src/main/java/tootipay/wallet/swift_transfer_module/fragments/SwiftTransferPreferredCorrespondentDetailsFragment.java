package tootipay.wallet.swift_transfer_module.fragments;

import android.os.Bundle;

import android.os.Handler;
import android.text.TextUtils;

import tootipay.wallet.R;
import tootipay.wallet.databinding.FragmentSwiftTransferPreferredCorrespondentDetailsBinding;
import tootipay.wallet.di.RequestHelper.B2BTransferDetails;
import tootipay.wallet.di.apicaller.B2BTransferDetailsTask;
import tootipay.wallet.fragments.BaseFragment;
import tootipay.wallet.interfaces.OnSuccessMessage;
import tootipay.wallet.swift_transfer_module.SwiftTransferActivity;
import tootipay.wallet.utils.IsNetworkConnection;

public class SwiftTransferPreferredCorrespondentDetailsFragment
        extends BaseFragment<FragmentSwiftTransferPreferredCorrespondentDetailsBinding>
        implements OnSuccessMessage {

    @Override
    protected void injectView() {

    }


    @Override
    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.correspondentBank.getText().toString())) {
            onMessage(getString(R.string.enter_correspondent_detals));
            return false;
        } else if (TextUtils.isEmpty(binding.cbAccountNo.getText().toString())) {
            onMessage(getString(R.string.enter_cb_account_no));
            return false;
        } else if (TextUtils.isEmpty(binding.cbSwiftBic.getText().toString())) {
            onMessage(getString(R.string.enter_cb_swift_bic));
            return false;
        }
        return true;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.nextLayout.setOnClickListener(v -> {
            if (isValidate()) {
                if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                    B2BTransferDetails details = ((SwiftTransferActivity) getBaseActivity()).b2BTransferDetails;
                    details.customerNo = getSessionManager().getCustomerNo();

                    details.correspondentBank = binding.correspondentBank.getText().toString();
                    details.cbSwiftBIC = binding.cbSwiftBic.getText().toString();
                    details.cbAccountNumber = binding.cbAccountNo.getText().toString();
                    details.languageId = getSessionManager().getlanguageselection();

                    B2BTransferDetailsTask task = new B2BTransferDetailsTask(getContext()
                            , this);
                    task.execute(details);
                } else {
                    onMessage(getString(R.string.no_internet));
                }
            }

        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_swift_transfer_preferred_correspondent_details;
    }

    @Override
    public void onSuccess(String s) {
        onMessage(s);
        Handler mHandler;
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.removeCallbacks(this);
                getBaseActivity().finish();
            }
        }, 700);
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }
}