package totipay.wallet.home_module.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;

import totipay.wallet.KYC.KYCMainActivity;
import totipay.wallet.LoyalityPointsActivity;
import totipay.wallet.Mobile_Top_Up.MobileTopUpMainActivity;
import totipay.wallet.MoneyTransferModuleV.MoneyTransferMainLayout;
import totipay.wallet.OffersActivity;
import totipay.wallet.prepaid_cards_module.PrepaidCardsActivity;
import totipay.wallet.R;
import totipay.wallet.TransferToOwnWalletActivity;
import totipay.wallet.adapters.Offer_Adapter;
import totipay.wallet.adapters.UserAccountsHomeAdapter;
import totipay.wallet.billpayment.BillPaymentMainActivity;
import totipay.wallet.databinding.FragmentHomeBinding;
import totipay.wallet.di.RequestHelper.GetCustomerWalletDetailsRequest;
import totipay.wallet.di.ResponseHelper.GetCustomerWalletDetailsResponse;
import totipay.wallet.di.apicaller.GetCustomerWalletDetailsTask;
import totipay.wallet.dialogs.AlertDialog;
import totipay.wallet.fragments.BaseFragment;
import totipay.wallet.home_module.ClassChangerHelper;
import totipay.wallet.home_module.NewDashboardActivity;
import totipay.wallet.interfaces.OnCustomerWalletDetails;
import totipay.wallet.interfaces.OnDecisionMade;
import totipay.wallet.interfaces.OnSelectOffers;
import totipay.wallet.mywalletmoduleV.MyWalletMainActivity;
import totipay.wallet.utils.BitmapHelper;
import totipay.wallet.utils.IsNetworkConnection;
import totipay.wallet.utils.NumberFormatter;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends BaseFragment<FragmentHomeBinding> implements OnDecisionMade
        , OnSelectOffers, OnCustomerWalletDetails {

    ArrayList<String> stringArrayList = new ArrayList<>();
    UserAccountsHomeAdapter adapter;

    @Override
    protected void injectView() {

    }


    @Override
    public void onResume() {
        super.onResume();

        ((NewDashboardActivity) getActivity()).hideHumBurger(ClassChangerHelper.HOME);

        if (getSessionManager().getIsDocumentsUploaded()) {
            binding.kycLayouts.setVisibility(View.GONE);
        }
        Glide.with(this)
                .load(BitmapHelper.decodeImage(((NewDashboardActivity) getBaseActivity()).sessionManager.getCustomerImage()))
                .placeholder(getResources().getDrawable(R.drawable.user_profile_home))
                .into(binding.userImageHome);

        if (adapter != null && getSessionManager().isWalletNeedToUpdate()) {
            ((NewDashboardActivity) getBaseActivity()).homeViewModel.walletDetailsResponses.clear();
            getWallet();
        }


    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        setOffers();

        setAccountsRecyclerView();

        onLoadView();


        binding.swipeRefresh.setOnRefreshListener(() -> {
            ((NewDashboardActivity) getBaseActivity()).homeViewModel.walletDetailsResponses.clear();
            getWallet();
        });


        binding.kycLayouts.setOnClickListener(view -> startActivity(new Intent(getActivity(), KYCMainActivity.class)));

        binding.addMoneyCardview.setOnClickListener(view -> startActivity(new Intent(getActivity(),
                MyWalletMainActivity.class)));

        binding.moneyTransfer.setOnClickListener(view ->
                startActivity(new Intent(getActivity(), MoneyTransferMainLayout.class)));

        binding.checkRatesCard.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), TransferToOwnWalletActivity.class);
            startActivity(intent);
        });


        Glide.with(this)
                .load(BitmapHelper.decodeImage(((NewDashboardActivity) getBaseActivity()).sessionManager.getCustomerImage()))
                .placeholder(R.drawable.user_profile_home)
                .into(binding.userImageHome);


        binding.mobileTopUpCarview.setOnClickListener(view ->
              //  startActivity(new Intent(getActivity(), MobileTopUpMainActivity.class))
                startActivity(new Intent(getActivity(), LoyalityPointsActivity.class))
        );
        binding.billPaymentCarview.setOnClickListener(view ->
              //  startActivity(new Intent(getActivity(), BillPaymentMainActivity.class))
                startActivity(new Intent(getActivity(), LoyalityPointsActivity.class))
        );
        binding.eGift.setOnClickListener(view -> startActivity(new Intent(getActivity(), PrepaidCardsActivity.class)));
    }


    private void setOffers() {
        stringArrayList.add("15% OFF");
        stringArrayList.add("25% OFF");
        stringArrayList.add("40% OFF");
        stringArrayList.add("25% OFF");
        stringArrayList.add("40% OFF");
        Offer_Adapter qtyListAdater = new Offer_Adapter(getActivity(), stringArrayList, this::onSelectOffer);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.offerRcv.setLayoutManager(mLayoutManager);
        binding.offerRcv.setItemAnimator(new DefaultItemAnimator());
        binding.offerRcv.setAdapter(qtyListAdater);
        qtyListAdater.notifyDataSetChanged();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onProceed() {
        startActivity(new Intent(getActivity(), KYCMainActivity.class));
    }

    @Override
    public void onCancel() {

    }


    public void onLoadView() {
        if (((NewDashboardActivity) getBaseActivity()).homeViewModel.walletDetailsResponses == null) {
            ((NewDashboardActivity) getBaseActivity()).homeViewModel.walletDetailsResponses = new ArrayList<>();
        }

        getWallet();
        if (getSessionManager().getIsDocumentsUploaded()) {
            binding.kycLayouts.setVisibility(View.GONE);
        } else {
            binding.kycLayouts.setVisibility(View.VISIBLE);
        }

        ((NewDashboardActivity) getBaseActivity())
                .showUserName(getSessionManager().getUserName());

        if (!((NewDashboardActivity) getBaseActivity())
                .sessionManager.getISKYCApproved()) {
            if (!((NewDashboardActivity) getBaseActivity())
                    .sessionManager.getIsDocumentsUploaded()) {
                binding.kycLayouts.setVisibility(View.VISIBLE);
                AlertDialog dialog = new AlertDialog(getString(R.string.complete_profile)
                        , getString(R.string.please_complete_kyc), this);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                dialog.show(transaction, "");
            } else {
                binding.kycLayouts.setVisibility(View.GONE);
            }
        }


        if (((NewDashboardActivity) getBaseActivity())
                .sessionManager.getISKYCApproved()) {
            binding.kycLayouts.setVisibility(View.GONE);
        } else {
            binding.kycLayouts.setVisibility(View.VISIBLE);
        }
        String walletBalance = NumberFormatter.decimal(getSessionManager().getWalletBalance());
        ((NewDashboardActivity) getBaseActivity()).showWalletBalance(walletBalance);
    }


    public void getWallet() {
        if (((NewDashboardActivity) getBaseActivity()).homeViewModel.walletDetailsResponses.isEmpty()) {

            if(!binding.swipeRefresh.isRefreshing()) {
                binding.swipeRefresh.setRefreshing(true);
            }
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                GetCustomerWalletDetailsRequest request = new GetCustomerWalletDetailsRequest();
                request.customerNo = getSessionManager().getCustomerNo();

                GetCustomerWalletDetailsTask task = new GetCustomerWalletDetailsTask(getContext()
                        , this, true);
                task.execute(request);
            } else {
                onMessage(getString(R.string.no_internet));
            }
        }
    }


    /**
     * setup the recycler view when screen load after that just notify the adapter
     */
    private void setAccountsRecyclerView() {

        if (((NewDashboardActivity) getBaseActivity()).homeViewModel.walletDetailsResponses == null) {
            ((NewDashboardActivity) getBaseActivity()).homeViewModel.walletDetailsResponses = new ArrayList<>();
        }
        adapter = new
                UserAccountsHomeAdapter(getContext(), ((NewDashboardActivity) getBaseActivity()).homeViewModel.walletDetailsResponses);
        LinearLayoutManager accountLayoutManager = new
                LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.userAccountrecyclerview.setLayoutManager(accountLayoutManager);
        binding.userAccountrecyclerview.setHasFixedSize(true);
        binding.userAccountrecyclerview.setAdapter(adapter);
    }


    @Override
    public void onSelectOffer() {
        startActivity(new Intent(getActivity(), OffersActivity.class));
    }

    @Override
    public void onCustomerWalletDetails(List<GetCustomerWalletDetailsResponse> walletList) {
        if (((NewDashboardActivity) getBaseActivity()).homeViewModel != null) {
            ((NewDashboardActivity) getBaseActivity()).homeViewModel.walletDetailsResponses.clear();
            ((NewDashboardActivity) getBaseActivity()).homeViewModel.walletDetailsResponses.addAll(walletList);
        }


        Log.e("onCustomer ", String.valueOf(walletList.size()));
        getSessionManager().putWalletNeedToUpdate(false);
        adapter.notifyDataSetChanged();
        binding.swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onSelectWallet(GetCustomerWalletDetailsResponse wallet) {

    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
        binding.swipeRefresh.setRefreshing(false);
    }
}