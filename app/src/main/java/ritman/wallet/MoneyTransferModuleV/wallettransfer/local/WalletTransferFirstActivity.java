package ritman.wallet.MoneyTransferModuleV.wallettransfer.local;

import android.os.Bundle;

import androidx.navigation.Navigation;

import ritman.wallet.R;
import ritman.wallet.databinding.ActivityWalletTransferFirstBinding;
import ritman.wallet.fragments.BaseFragment;

public class WalletTransferFirstActivity extends BaseFragment<ActivityWalletTransferFirstBinding> {

    @Override
    protected void injectView() {

    }


    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.localTransferLayout.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_walletTransferFirstActivity_to_local_transfer_navigation));


        binding.internatinalTransferLayout.setOnClickListener(v -> {
            if (getSessionManager().getISKYCApproved()) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("to_own_wallet", true);
                Navigation.findNavController(v)
                        .navigate(R.id.action_walletTransferFirstActivity_to_walletTransferActivity2, bundle);
            } else {
                onMessage(getString(R.string.plz_complete_kyc));
            }

        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet_transfer_first;
    }


}
