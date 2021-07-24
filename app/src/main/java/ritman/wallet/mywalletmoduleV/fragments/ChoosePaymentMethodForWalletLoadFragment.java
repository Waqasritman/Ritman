package ritman.wallet.mywalletmoduleV.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ritman.wallet.MoneyTransferModuleV.Helpers.PaymentTypeHelper;
import ritman.wallet.R;

import ritman.wallet.adapters.CardDetailsAdapter;
import ritman.wallet.databinding.ActivitySelectCardBinding;
import ritman.wallet.di.XMLdi.RequestHelper.GetCardDetailsRequest;
import ritman.wallet.di.XMLdi.RequestHelper.LoadWalletRequest;
import ritman.wallet.di.XMLdi.ResponseHelper.GetCardDetailsResponse;
import ritman.wallet.di.XMLdi.apicaller.GetCardDetailsTask;
import ritman.wallet.di.XMLdi.apicaller.LoadWalletTask;
import ritman.wallet.dialogs.AddCardDialog;
import ritman.wallet.dialogs.BankDepositDialog;
import ritman.wallet.dialogs.GetCVVDialog;
import ritman.wallet.dialogs.SingleButtonMessageDialog;
import ritman.wallet.fragments.BaseFragment;
import ritman.wallet.interfaces.OnCardDetailsSubmit;
import ritman.wallet.interfaces.OnDecisionMade;
import ritman.wallet.interfaces.OnEnterCVv;
import ritman.wallet.interfaces.OnGetCardDetails;
import ritman.wallet.interfaces.OnSuccessMessage;
import ritman.wallet.utils.IsNetworkConnection;

public class ChoosePaymentMethodForWalletLoadFragment extends BaseFragment<ActivitySelectCardBinding>
        implements OnCardDetailsSubmit, OnSuccessMessage, OnDecisionMade, OnGetCardDetails
        , OnEnterCVv {

    String amountToLoad = "0.0";
    String currencyToLoad = "GBP";
    LoadWalletRequest request;

    CardDetailsAdapter detailsAdapter;
    List<GetCardDetailsResponse> responseList;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        setupRecyclerView();
        if (getArguments() != null) {
            amountToLoad = getArguments().getString("load_amount");
            currencyToLoad = getArguments().getString("load_currency");
        }

        request = new LoadWalletRequest();
        request.customerNo = getSessionManager().getCustomerNo();
        request.transferAmount = amountToLoad;
        request.walletCurrency = currencyToLoad;
        request.languageId = getSessionManager().getlanguageselection();
        request.ipCountryName = getSessionManager().getIpCountryName();
        request.ipAddress = getSessionManager().getIpAddress();

        binding.addNewCard.setOnClickListener(v -> {
            AddCardDialog addCardDialog = new AddCardDialog(this);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            addCardDialog.show(transaction, "");
        });

        binding.throughBank.setOnClickListener(v -> {
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                request.cardNumber = "";
                request.expireDate = "";
                request.securityNumber = "";
                request.languageId = getSessionManager().getlanguageselection();
                request.paymentType = PaymentTypeHelper.BANK_DEPOSIT;
                loadWallet();
            }
        });


        binding.loadCards.setOnClickListener(v -> {
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                GetCardDetailsRequest request = new GetCardDetailsRequest();
                request.customerNo = getSessionManager().getCustomerNo();
                request.languageID = getSessionManager().getlanguageselection();

                GetCardDetailsTask task = new GetCardDetailsTask(getContext(),
                        this);
                task.execute(request);
            } else {
                onMessage(getString(R.string.no_internet));
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_card;
    }

    @Override
    public void onCardDetailsSSubmit(String cardNumber, String cardExpire, String cardCVV) {
        request.cardNumber = cardNumber;
        request.expireDate = cardExpire;
        request.securityNumber = cardCVV;
        request.paymentType = PaymentTypeHelper.CREDIT_CARD;
        loadWallet();
    }


    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }

    @Override
    public void onSuccess(String s) {
        getSessionManager().putWalletNeedToUpdate(true);

        if (request.paymentType == PaymentTypeHelper.CREDIT_CARD) {
            SingleButtonMessageDialog dialog = new SingleButtonMessageDialog(
                    getString(R.string.in_process), getString(R.string.in_process_msg_card)
                    , this,
                    false);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            dialog.show(transaction, "");
        } else if (request.paymentType == PaymentTypeHelper.BANK_DEPOSIT) {
            BankDepositDialog depositDialog = new BankDepositDialog(s, this ,
                    isYemenClient());
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            depositDialog.show(transaction, "");
        }

    }

    @Override
    public void onProceed() {
        getBaseActivity().finish();
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onCardDetailsGet(List<GetCardDetailsResponse> cardDetailsResponses) {
        this.responseList.clear();
        this.responseList.addAll(cardDetailsResponses);
        detailsAdapter.notifyDataSetChanged();

        if (cardDetailsResponses.size() > 0) {
            binding.loadCards.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSelectCard(GetCardDetailsResponse cardDetail) {
        request.cardNumber = cardDetail.cardNumber;
        request.expireDate = cardDetail.cardExpireDate;

        GetCVVDialog dialog = new GetCVVDialog(this);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }


    /**
     * Method will set the recycler view
     */
    private void setupRecyclerView() {
        responseList = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        detailsAdapter = new
                CardDetailsAdapter(responseList, this);
        binding.cardDetailsRecyclerView.setLayoutManager(mLayoutManager);
        binding.cardDetailsRecyclerView.setHasFixedSize(true);
        binding.cardDetailsRecyclerView.setAdapter(detailsAdapter);
    }

    @Override
    public void onCVV(String cvv) {
        request.securityNumber = cvv;
        request.paymentType = PaymentTypeHelper.CREDIT_CARD;
        loadWallet();
    }


    void loadWallet() {
        if (getSessionManager().getISKYCApproved()) {
            if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                LoadWalletTask task = new LoadWalletTask(getContext(), this);
                task.execute(request);
            } else {
                onMessage(getString(R.string.no_internet));
            }
        } else {
            onMessage(getString(R.string.complete_profile));
        }
    }
}