package angoothape.wallet.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import angoothape.wallet.refund_module.RefundTransactionNumberFragment;
import angoothape.wallet.refund_module.fragments.RefundHistoryFragment;

public class ViewStateAdapterRefund extends FragmentStateAdapter {

    public ViewStateAdapterRefund(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Hardcoded in this order, you'll want to use lists and make sure the titles match
        if (position == 0) {
            return new RefundTransactionNumberFragment();
        }
        return new RefundHistoryFragment();
    }

    @Override
    public int getItemCount() {
        // Hardcoded, use lists
        return 2;
    }
}
