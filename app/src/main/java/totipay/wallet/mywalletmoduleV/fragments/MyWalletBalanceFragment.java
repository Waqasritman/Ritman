package totipay.wallet.mywalletmoduleV.fragments;

import android.os.Bundle;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import totipay.wallet.R;
import totipay.wallet.adapters.UserAccountsHomeAdapter;
import totipay.wallet.databinding.ActivityMyWalletBinding;
import totipay.wallet.di.RequestHelper.GetCustomerWalletDetailsRequest;
import totipay.wallet.di.ResponseHelper.GetCustomerWalletDetailsResponse;
import totipay.wallet.di.apicaller.GetCustomerWalletDetailsTask;
import totipay.wallet.fragments.BaseFragment;
import totipay.wallet.interfaces.OnCustomerWalletDetails;
import totipay.wallet.utils.IsNetworkConnection;

public class MyWalletBalanceFragment extends BaseFragment<ActivityMyWalletBinding>
        implements OnCustomerWalletDetails {
    UserAccountsHomeAdapter adapter;

    List<GetCustomerWalletDetailsResponse> walletDetailsResponses;

    @Override
    protected void injectView() {

    }


    @Override
    protected void setUp(Bundle savedInstanceState) {
        setAccountsRecyclerView();
        if(walletDetailsResponses.isEmpty()) {
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                GetCustomerWalletDetailsRequest request = new GetCustomerWalletDetailsRequest();
                request.customerNo = getSessionManager().getCustomerNo();
                request.languageId = getSessionManager().getlanguageselection();
                GetCustomerWalletDetailsTask task = new GetCustomerWalletDetailsTask(getContext()
                        , this);
                task.execute(request);
            } else {
                onMessage(getString(R.string.no_internet));
            }
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_wallet;
    }


    /**
     * setup the recycler view when screen load after that just notify the adapter
     */
    private void setAccountsRecyclerView() {

        walletDetailsResponses = new ArrayList<>();

        adapter = new
                UserAccountsHomeAdapter(getContext() , walletDetailsResponses , this);
        LinearLayoutManager accountLayoutManager = new
                LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.userAccountrecyclerview.setLayoutManager(accountLayoutManager);
        binding.userAccountrecyclerview.setHasFixedSize(true);
        binding.userAccountrecyclerview.setAdapter(adapter);
    }


    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }

    @Override
    public void onCustomerWalletDetails(List<GetCustomerWalletDetailsResponse> walletList) {
        walletDetailsResponses.clear();
        walletDetailsResponses.addAll(walletList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSelectWallet(GetCustomerWalletDetailsResponse wallet) {
        Bundle bundle = new Bundle();
        bundle.putString("wallet_type", wallet.currencyShortName);
        Navigation.findNavController(getView())
                .navigate(R.id.action_MyWalletActivity_to_AddMoneyWalletActivity,
                        bundle);
    }
}