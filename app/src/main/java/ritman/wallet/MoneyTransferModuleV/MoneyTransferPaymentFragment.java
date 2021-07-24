package ritman.wallet.MoneyTransferModuleV;

import android.content.Intent;
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
import ritman.wallet.databinding.ActivityCashPaymentFourthBinding;
import ritman.wallet.di.XMLdi.RequestHelper.GetCardDetailsRequest;
import ritman.wallet.di.XMLdi.RequestHelper.TootiPayRequest;
import ritman.wallet.di.XMLdi.RequestHelper.WalletBalanceRequest;
import ritman.wallet.di.XMLdi.ResponseHelper.GetCardDetailsResponse;
import ritman.wallet.di.XMLdi.apicaller.GetCardDetailsTask;
import ritman.wallet.di.XMLdi.apicaller.TootiPaySendTask;
import ritman.wallet.di.XMLdi.apicaller.WalletBalanceRequestTask;
import ritman.wallet.dialogs.BankDepositDialog;
import ritman.wallet.dialogs.GetCVVDialog;
import ritman.wallet.dialogs.SingleButtonMessageDialog;
import ritman.wallet.fragments.BaseFragment;
import ritman.wallet.interfaces.OnCardDetailsSubmit;
import ritman.wallet.interfaces.OnDecisionMade;
import ritman.wallet.interfaces.OnEnterCVv;
import ritman.wallet.interfaces.OnGetCardDetails;
import ritman.wallet.interfaces.OnSendTransferTootiPay;
import ritman.wallet.interfaces.OnWalletBalanceReceived;
import ritman.wallet.dialogs.AddCardDialog;
import ritman.wallet.utils.IsNetworkConnection;

public class MoneyTransferPaymentFragment extends BaseFragment<ActivityCashPaymentFourthBinding>
        implements OnSendTransferTootiPay, OnWalletBalanceReceived, OnDecisionMade, OnCardDetailsSubmit
        , OnGetCardDetails, OnEnterCVv {

    Float walletBalance = 0.0f;
    TootiPayRequest tootiPayRequest;
    String transactionNumber = "";

    CardDetailsAdapter detailsAdapter;
    List<GetCardDetailsResponse> responseList;
    boolean isWalletAllowed = true;

    @Override
    public void onResume() {
        super.onResume();
        ((MoneyTransferMainLayout) getBaseActivity()).binding.toolBar.titleTxt
                .setText(getString(R.string.payment));
    }

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        setupRecyclerView();
        tootiPayRequest = ((MoneyTransferMainLayout) getBaseActivity()).bankTransferViewModel.request;
        tootiPayRequest.ipAddress = getSessionManager().getIpAddress();
        tootiPayRequest.ipCountryName = getSessionManager().getIpCountryName();

        binding.walletText.setText(getString(R.string.wallet)
                .concat(" ").concat("(").concat(tootiPayRequest.payInCurrency).concat(")"));
        getWalletBalance();

        binding.walletLayout.setOnClickListener(v -> {
            if(isWalletAllowed) {
                tootiPayRequest.paymentTypeId = PaymentTypeHelper.WALLET;
                tootiPayRequest.languageId = getSessionManager().getlanguageselection();
                getPin();
            } else {
                onMessage("Selected Sending Currency is not in wallet");
            }

        });

        binding.addNewCard.setOnClickListener(v -> {
            AddCardDialog dialog = new AddCardDialog(this::onCardDetailsSSubmit);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            dialog.show(transaction, "");
        });


        binding.throughBank.setOnClickListener(v -> {
            tootiPayRequest.cardNumber = "";
            tootiPayRequest.expireDate = "";
            tootiPayRequest.securityNumber = "";
            tootiPayRequest.paymentTypeId = PaymentTypeHelper.BANK_DEPOSIT;
            tootiPayRequest.languageId = getSessionManager().getlanguageselection();
            getPin();

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
        return R.layout.activity_cash_payment_fourth;
    }

    @Override
    public void onMoneyTransferSuccessfully(String transactionNumber) {
        this.transactionNumber = transactionNumber;
        getSessionManager().putWalletNeedToUpdate(true);
        if (tootiPayRequest.paymentTypeId == PaymentTypeHelper.CREDIT_CARD) {
            SingleButtonMessageDialog dialog = new SingleButtonMessageDialog(getString(R.string.in_process)
                    , getString(R.string.in_process_msg_card), this
                    , false);
            dialog.setCancelable(false);

            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            dialog.show(transaction, "");
        } else if (tootiPayRequest.paymentTypeId == PaymentTypeHelper.BANK_DEPOSIT) {

            BankDepositDialog depositDialog = new BankDepositDialog(this.transactionNumber, this ,isYemenClient());
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            depositDialog.show(transaction, "");
        } else {
            goToReceipt();
        }
    }

    private void getWalletBalance() {
        WalletBalanceRequest request = new WalletBalanceRequest();
        request.customerNo = getSessionManager().getCustomerNo();
        request.walletCurrency = tootiPayRequest.payInCurrency;
        request.languageId = getSessionManager().getlanguageselection();
        WalletBalanceRequestTask task = new WalletBalanceRequestTask(getContext(),
                this);
        task.execute(request);
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }

    @Override
    public void onWalletBalanceReceived(String walletBalance) {
        binding.walletBalance.setText(walletBalance);
    }

    @Override
    public void onLockWalletOption(boolean isLocked) {
        isWalletAllowed = isLocked;
    }

    @Override
    public void onProceed() {
        goToReceipt();
    }

    @Override
    public void onCancel() {
        goToReceipt();
    }

    @Override
    public void onCardDetailsSSubmit(String cardNumber, String cardExpire, String cardCVV) {
        tootiPayRequest.securityNumber = cardCVV;
        tootiPayRequest.cardNumber = cardNumber;
        tootiPayRequest.expireDate = cardExpire;
        tootiPayRequest.paymentTypeId = PaymentTypeHelper.CREDIT_CARD;
        getPin();
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
        tootiPayRequest.cardNumber = cardDetail.cardNumber;
        tootiPayRequest.expireDate = cardDetail.cardExpireDate;
        tootiPayRequest.paymentTypeId = PaymentTypeHelper.CREDIT_CARD;
        tootiPayRequest.languageId = getSessionManager().getlanguageselection();


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
        tootiPayRequest.securityNumber = cvv;
        tootiPayRequest.paymentTypeId = PaymentTypeHelper.CREDIT_CARD;
        getPin();
    }


    void loadTotiPaySend() {
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            TootiPaySendTask task = new TootiPaySendTask(getActivity(), this);
            task.execute(tootiPayRequest);
        } else {
            onMessage(getString(R.string.no_internet));
        }
    }


    void goToReceipt() {
        Intent intent = new Intent(getBaseActivity() , TransactionReceiptActivity.class);
        intent.putExtra("txn_number"  , transactionNumber);
        startActivity(intent);
        getBaseActivity().finish();
    }


    @Override
    public void onVerifiedPin() {
        loadTotiPaySend();
    }
}
