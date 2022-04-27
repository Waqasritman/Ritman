package angoothape.wallet.home_module.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import angoothape.wallet.FundTransferToMerchantActivity;
import angoothape.wallet.LoyalityPointsActivity;
import angoothape.wallet.MerchantLedgerActivity;
import angoothape.wallet.UploadCashDetailsActivity;
import angoothape.wallet.bill_desk.BillDeskMainActivity;
import angoothape.wallet.bus_booking.BusBookingMainActivity;
import angoothape.wallet.refund_module.RefundActivity;
import angoothape.wallet.settlementaeps.AEPSSettlementTransactionActivity;
import retrofit2.Call;
import retrofit2.Response;
import angoothape.wallet.KYC.KYCMainActivity;
import angoothape.wallet.Mobile_Top_Up.MobileTopUpMainActivity;
import angoothape.wallet.MoneyTransferModuleV.MoneyTransferMainLayout;
import angoothape.wallet.aeps.activity.SelectDeviceActivity;
import angoothape.wallet.ekyc.EKYCMainActivity;
import angoothape.wallet.insurance.InsuranceActivity;
import angoothape.wallet.pancard.PanCardActivity;
import angoothape.wallet.personal_loan.fragment.PLActivity;
import angoothape.wallet.TransactionHistory.TransactionHistoryActivity;
import angoothape.wallet.billpayment.BillPaymentMainActivity;
import angoothape.wallet.di.JSONdi.restRequest.SimpleRequest;
import angoothape.wallet.di.generic_response.SimpleResponse;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.R;
import angoothape.wallet.databinding.FragmentHomeBinding;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.interfaces.OnDecisionMade;


public class HomeFragment extends BaseFragment<FragmentHomeBinding> implements OnDecisionMade {

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
     //   getUpdatedBalance();
        binding.swipeRefresh.setOnRefreshListener(this::getUpdatedBalance);

        if (getSessionManager().getIsVerified()) {
            binding.ekyc.setVisibility(View.INVISIBLE);
        } else {
            binding.ekyc.setVisibility(View.VISIBLE);
        }


        binding.moneyTransfer.setOnClickListener(view ->
                startActivity(new Intent(getActivity(), MoneyTransferMainLayout.class)));

        binding.transactionHistoryCard.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TransactionHistoryActivity.class);
            startActivity(intent);
        });

        binding.aepsSettlement.setOnClickListener(v -> {
            // startActivity(new Intent(getActivity(), LoyalityPointsActivity.class));
            Intent intent = new Intent(getActivity(), AEPSSettlementTransactionActivity.class);
            startActivity(intent);
        });

        binding.mobileTopUpCarview.setOnClickListener(view ->
                        //     startActivity(new Intent(getActivity(), LoyalityPointsActivity.class))
                        // startActivity(new Intent(getActivity(), BusBookingMainActivity.class))
                        startActivity(new Intent(getActivity(), MobileTopUpMainActivity.class))
                //startActivity(new Intent(getActivity(), LoyalityPointsActivity.class))
        );

        binding.billPaymentCarview.setOnClickListener(view ->
                        startActivity(new Intent(getActivity(), BillPaymentMainActivity.class))
                //startActivity(new Intent(getActivity(), LoyalityPointsActivity.class))
        );


        binding.aeps.setOnClickListener(v -> {
            if (getSessionManager().getIsVerified()) {
                startActivity(new Intent(getActivity(), SelectDeviceActivity.class));
            } else {
                onMessage(getString(R.string.kyc_not_approved));
            }
        });

        binding.pancard.setOnClickListener(view ->
                //   startActivity(new Intent(getActivity(), PanCardActivity.class))
                startActivity(new Intent(getActivity(), LoyalityPointsActivity.class))
        );

        binding.ekyc.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), EKYCMainActivity.class);
            intent.putExtra("is_customer", false);
            startActivity(intent);
        });

        binding.customerEkyc.setOnClickListener(v -> {
            //    startActivity(new Intent(getActivity(), LoyalityPointsActivity.class));
            Intent intent = new Intent(getActivity(), EKYCMainActivity.class);
            intent.putExtra("is_customer", true);
            startActivity(intent);
        });

        binding.ledgerHistory.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), MerchantLedgerActivity.class));
        });
        binding.pl.setOnClickListener(view ->
                startActivity(new Intent(getActivity(), PLActivity.class))
        );
        binding.insurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), InsuranceActivity.class));
                startActivity(new Intent(getActivity(), LoyalityPointsActivity.class));
            }
        });


        binding.fundTransferToMerchant.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), FundTransferToMerchantActivity.class));
        });


        binding.refund.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), RefundActivity.class));
        });


        binding.uploadCashDetails.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), UploadCashDetailsActivity.class));
        });
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
    public void onCancel(boolean goBack) {

    }

    @Override
    public void onResume() {
        super.onResume();
        getUpdatedBalance();
    }

    public void getUpdatedBalance() {
        SimpleRequest request = new SimpleRequest();
        Call<SimpleResponse> call = RestClient.get().getBalance(RestClient.makeGSONRequestBody(request)
                , getSessionManager().getMerchantName());
        call.enqueue(new retrofit2.Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                binding.swipeRefresh.setRefreshing(false);
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().responseCode.equals(101)) {
                        binding.tvBalance.setText("₹ " + response.body().data);
                    } else {
                        onError(response.body().description);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<SimpleResponse> call, @NotNull Throwable t) {
                Log.e("TAG", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
        binding.swipeRefresh.setRefreshing(false);
    }
}