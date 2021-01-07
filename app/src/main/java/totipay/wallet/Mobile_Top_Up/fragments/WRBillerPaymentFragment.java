package totipay.wallet.Mobile_Top_Up.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import totipay.wallet.Mobile_Top_Up.MobileTopUpMainActivity;
import totipay.wallet.Mobile_Top_Up.helpers.MobileTopUpType;
import totipay.wallet.MoneyTransferModuleV.Helpers.PaymentTypeHelper;
import totipay.wallet.R;
import totipay.wallet.adapters.CardDetailsAdapter;
import totipay.wallet.databinding.WrPaymentScreenBinding;
import totipay.wallet.di.RequestHelper.CalTransferRequest;
import totipay.wallet.di.RequestHelper.GetCardDetailsRequest;
import totipay.wallet.di.RequestHelper.GetWalletCurrencyListRequest;
import totipay.wallet.di.RequestHelper.WRPayBillRequest;
import totipay.wallet.di.RequestHelper.WRPrepaidRechargeRequest;
import totipay.wallet.di.ResponseHelper.CalTransferResponse;
import totipay.wallet.di.ResponseHelper.GetCardDetailsResponse;
import totipay.wallet.di.ResponseHelper.GetSendRecCurrencyResponse;
import totipay.wallet.di.apicaller.CheckRatesTask;
import totipay.wallet.di.apicaller.GetCardDetailsTask;
import totipay.wallet.di.apicaller.GetWalletCurrencyListTask;
import totipay.wallet.di.apicaller.WRPayBillTask;
import totipay.wallet.di.apicaller.WRPrepaidPayBillTask;
import totipay.wallet.dialogs.AddCardDialog;
import totipay.wallet.dialogs.DialogCurrency;
import totipay.wallet.dialogs.GetCVVDialog;
import totipay.wallet.fragments.BaseFragment;
import totipay.wallet.interfaces.OnCardDetailsSubmit;
import totipay.wallet.interfaces.OnEnterCVv;
import totipay.wallet.interfaces.OnGetCardDetails;
import totipay.wallet.interfaces.OnGetTransferRates;
import totipay.wallet.interfaces.OnSelectCurrency;
import totipay.wallet.interfaces.OnSuccessMessage;
import totipay.wallet.utils.IsNetworkConnection;

public class WRBillerPaymentFragment extends BaseFragment<WrPaymentScreenBinding>
        implements OnSelectCurrency, OnCardDetailsSubmit, OnSuccessMessage, OnGetTransferRates
        , OnGetCardDetails, OnEnterCVv {


    WRPayBillRequest request;
    List<GetSendRecCurrencyResponse> userWalletList;
    CalTransferRequest calTransferRequest;
    Integer paymentMode = 0;

    CardDetailsAdapter detailsAdapter;
    List<GetCardDetailsResponse> responseList;


    WRPrepaidRechargeRequest prepaidRechargeRequest;

    int topUptype;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        setupRecyclerView();

        topUptype = ((MobileTopUpMainActivity) getBaseActivity()).topUpType;
        userWalletList = new ArrayList<>();
        calTransferRequest = new CalTransferRequest();
        if (topUptype
                == MobileTopUpType.POST_PAID) {
            request = new WRPayBillRequest();
            if (getArguments() != null) {
                request = getArguments().getParcelable("biller_plan");
                assert request != null;
            }
        } else if (topUptype
                == MobileTopUpType.PRE_PAID) {
            prepaidRechargeRequest = ((MobileTopUpMainActivity) getBaseActivity()).prepaidRechargeRequest;
        }


        binding.walletLayout.setOnClickListener(v -> {
            if (userWalletList.isEmpty()) {
                if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                    GetWalletCurrencyListRequest request = new GetWalletCurrencyListRequest();
                    request.languageId = getSessionManager().getlanguageselection();
                    GetWalletCurrencyListTask getWalletCurrencyListTask = new GetWalletCurrencyListTask(getContext()
                            , this);
                    getWalletCurrencyListTask.execute(request);
                } else {
                    onMessage(getString(R.string.no_internet));
                }
            } else {
                showCurrencyDialog();
            }

        });


        binding.processedToPay.setOnClickListener(v -> {

            if (getSessionManager().getISKYCApproved()) {
                if (IsNetworkConnection.checkNetworkConnection(getContext())) {

                    if (TextUtils.isEmpty(binding.walletLayout.getText().toString())) {
                        onMessage(getString(R.string.choose_wallet));
                        return;
                    }

                    if (topUptype == MobileTopUpType.POST_PAID) {
                        request.languageId = getSessionManager().getlanguageselection();
                        request.paymentTypeId = paymentMode;
                        postPaid();
                    } else if (topUptype == MobileTopUpType.PRE_PAID) {
                        prepaidRechargeRequest.languageId = getSessionManager().getlanguageselection();
                        prepaidRechargeRequest.paymentTypeId = paymentMode;
                        prepaid();
                    }


                } else {
                    onMessage(getString(R.string.no_internet));
                }
            } else {
                onMessage(getString(R.string.need_to_complete_kyc));
            }


        });


        binding.addNewCard.setOnClickListener(v -> {
            AddCardDialog dialog = new AddCardDialog(this);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            dialog.show(transaction, "");
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
        return R.layout.wr_payment_screen;
    }

    @Override
    public void onCardDetailsSSubmit(String cardNumber, String cardExpire, String cardCVV) {
        paymentMode = PaymentTypeHelper.CREDIT_CARD;

        if (topUptype == MobileTopUpType.POST_PAID) {
            request.cardNumber = cardNumber;
            request.expireDate = cardExpire;
            request.securityCode = cardCVV;
        } else if (topUptype == MobileTopUpType.PRE_PAID) {
            prepaidRechargeRequest.cardNumber = cardNumber;
            prepaidRechargeRequest.expireDate = cardExpire;
            prepaidRechargeRequest.securityCode = cardCVV;
        }

    }

    @Override
    public void onCurrencyResponse(List<GetSendRecCurrencyResponse> response) {
        userWalletList.clear();
        userWalletList.addAll(response);
        if (response.size() == 1) {
            binding.walletLayout.setText(response.get(0).currencyShortName);
            if (topUptype == MobileTopUpType.POST_PAID) {
                request.payInCurrency = response.get(0).currencyShortName;
            } else if (topUptype == MobileTopUpType.PRE_PAID) {
                prepaidRechargeRequest.payInCurrency = response.get(0).currencyShortName;
            }
            calTransferRequest.PayInCurrency = response.get(0).currencyShortName;
            calTransferRequest.TransferCurrency = response.get(0).currencyShortName;
        } else {
            //show dialog
            showCurrencyDialog();
        }
    }


    public void showCurrencyDialog() {
        DialogCurrency dialogCurrency = new DialogCurrency(userWalletList, this);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        dialogCurrency.show(transaction, "");
    }


    public void getTransferRates() {
        calTransferRequest.PaymentMode = "Pay_Bill";
        if (topUptype == MobileTopUpType.POST_PAID) {
            calTransferRequest.TransferAmount = Double.parseDouble(request.payOutAmount);
            calTransferRequest.PayoutCurrency = request.payoutCurrency;
            calTransferRequest.TransferCurrency = request.payoutCurrency;
        } else if (topUptype == MobileTopUpType.PRE_PAID) {
            calTransferRequest.TransferAmount = Double.parseDouble(prepaidRechargeRequest.payOutAmount);
            calTransferRequest.PayoutCurrency = prepaidRechargeRequest.payoutCurrency;
            calTransferRequest.TransferCurrency = prepaidRechargeRequest.payoutCurrency;
        }

        if (IsNetworkConnection.checkNetworkConnection(getActivity())) {
            CheckRatesTask task = new CheckRatesTask(getActivity(), this);
            task.execute(calTransferRequest);
        } else {
            onMessage(getString(R.string.no_internet));
        }
    }

    @Override
    public void onCurrencySelect(GetSendRecCurrencyResponse response) {
        binding.walletLayout.setText(response.currencyShortName);
        if (topUptype == MobileTopUpType.POST_PAID) {
            request.payInCurrency = response.currencyShortName;
        } else if (topUptype == MobileTopUpType.PRE_PAID) {
            prepaidRechargeRequest.payInCurrency = response.currencyShortName;
        }
        paymentMode = PaymentTypeHelper.WALLET;
        calTransferRequest.PayInCurrency = response.currencyShortName;
       // calTransferRequest.TransferCurrency = response.currencyShortName;
        calTransferRequest.languageId = getSessionManager().getlanguageselection();
        getTransferRates();
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }

    @Override
    public void onSuccess(String s) {
        onMessage(s);

        if (topUptype == MobileTopUpType.PRE_PAID) {
            Bundle bundle = new Bundle();
            bundle.putString("request_id", s);
            bundle.putString("transfer_amount", prepaidRechargeRequest.payOutAmount);
            bundle.putString("operator_name", ((MobileTopUpMainActivity) getBaseActivity()).prepaidRechargeRequest
                    .operatorName);
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_WRBillerPaymentFragment_to_mobileTopupStatusFragment, bundle);
        } else {
            getBaseActivity().finish();
        }
    }

    @Override
    public void onGetTransferRates(CalTransferResponse response) {
        binding.totalPayableAmount.setText(response.totalPayable.toString());
        binding.totalPayableLayout.setVisibility(View.VISIBLE);
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
        if (topUptype == MobileTopUpType.POST_PAID) {
            request.cardNumber = cardDetail.cardNumber;
            request.expireDate = cardDetail.cardExpireDate;
            request.paymentTypeId = PaymentTypeHelper.CREDIT_CARD;
            request.languageId = getSessionManager().getlanguageselection();
        } else if (topUptype == MobileTopUpType.PRE_PAID) {
            prepaidRechargeRequest.cardNumber = cardDetail.cardNumber;
            prepaidRechargeRequest.expireDate = cardDetail.cardExpireDate;
            prepaidRechargeRequest.paymentTypeId = PaymentTypeHelper.CREDIT_CARD;
            prepaidRechargeRequest.payInCurrency = "GBP";
            prepaidRechargeRequest.languageId = getSessionManager().getlanguageselection();

        }

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
        if (topUptype == MobileTopUpType.POST_PAID) {
            request.securityCode = cvv;
            request.paymentTypeId = PaymentTypeHelper.CREDIT_CARD;
            postPaid();
        } else if (topUptype == MobileTopUpType.PRE_PAID) {
            prepaidRechargeRequest.securityCode = cvv;
            prepaidRechargeRequest.payInCurrency = "GBP";
            prepaidRechargeRequest.paymentTypeId = PaymentTypeHelper.CREDIT_CARD;
            prepaid();
        }
    }


    void postPaid() {
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            WRPayBillTask task = new WRPayBillTask(getContext(), this);
            task.execute(request);
        } else {
            onMessage(getString(R.string.no_internet));
        }
    }

    void prepaid() {
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            WRPrepaidPayBillTask task = new WRPrepaidPayBillTask(getContext(), this);
            task.execute(prepaidRechargeRequest);
        } else {
            onMessage(getString(R.string.no_internet));
        }
    }
}
