package angoothape.wallet.home_module.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import angoothape.wallet.FundTransferToMerchantActivity;
import angoothape.wallet.MerchantLedgerActivity;
import angoothape.wallet.UploadCashDetailsActivity;
import angoothape.wallet.bill_desk.BillDeskMainActivity;
import angoothape.wallet.bus_booking.BusBookingMainActivity;
import angoothape.wallet.refund_module.RefundActivity;
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
        getUpdatedBalance();
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

        binding.mobileTopUpCarview.setOnClickListener(view ->
                startActivity(new Intent(getActivity(), BillDeskMainActivity.class))
                // startActivity(new Intent(getActivity(), BusBookingMainActivity.class))
                //    startActivity(new Intent(getActivity(), MobileTopUpMainActivity.class))
                //startActivity(new Intent(getActivity(), LoyalityPointsActivity.class))
        );
        binding.billPaymentCarview.setOnClickListener(view ->
                        startActivity(new Intent(getActivity(), BillPaymentMainActivity.class))
                //  startActivity(new Intent(getActivity(), LoyalityPointsActivity.class))
        );

        binding.aeps.setOnClickListener(view -> {
                    if (getSessionManager().getIsVerified()) {

//                        CheckBeneForAeps dialog = new CheckBeneForAeps();
//                        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//                        dialog.show(transaction, "");
                        startActivity(new Intent(getActivity(), SelectDeviceActivity.class));
                    } else {
                        onMessage(getString(R.string.kyc_not_approved));
                    }
                }
        );

        binding.pancard.setOnClickListener(view ->
                        startActivity(new Intent(getActivity(), PanCardActivity.class))
                //  startActivity(new Intent(getActivity(), LoyalityPointsActivity.class))
        );

        binding.ekyc.setOnClickListener(view ->
                        startActivity(new Intent(getActivity(), EKYCMainActivity.class))
                // startActivity(new Intent(getActivity(), AdharBioEKYCActivity.class))

        );

        binding.ledgerHistory.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), MerchantLedgerActivity.class));
        });
        binding.pl.setOnClickListener(view ->
                        startActivity(new Intent(getActivity(), PLActivity.class))
                //  startActivity(new Intent(getActivity(), LoyalityPointsActivity.class))
        );
        binding.insurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), InsuranceActivity.class));
            }
        });


        binding.fundTransferToMerchant.setOnClickListener(v -> {
            //    if (getSessionManager().getIsVerified()) {
            startActivity(new Intent(getActivity(), FundTransferToMerchantActivity.class));
            //  } else {
            //    onMessage(getString(R.string.kyc_not_approved));
            // }
        });


        binding.refund.setOnClickListener(v -> {
            //  if (getSessionManager().getIsVerified()) {
            startActivity(new Intent(getActivity(), RefundActivity.class));
            //  startActivity(new Intent(getActivity(), SelectDeviceActivity.class));
            //} else {
            //  onMessage(getString(R.string.kyc_not_approved));
            //}

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
                    if (response.body().responseCode.equals(101)) {
                        binding.tvBalance.setText("₹ " + response.body().data);
                    } else {
                        onMessage(response.body().description);
                    }
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
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