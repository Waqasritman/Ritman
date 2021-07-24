package ritman.wallet.home_module.fragments;


import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Response;
import ritman.wallet.AEPSActivity;
import ritman.wallet.KYC.KYCMainActivity;
import ritman.wallet.Mobile_Top_Up.MobileTopUpMainActivity;
import ritman.wallet.MoneyTransferModuleV.MoneyTransferMainLayout;
import ritman.wallet.ekyc.eKYCActivity;
import ritman.wallet.insurance.InsuranceActivity;
import ritman.wallet.pancard.PanCardActivity;
import ritman.wallet.personal_loan.fragment.PLActivity;
import ritman.wallet.RegistrationActivity;
import ritman.wallet.TransactionHistory.TransactionHistoryActivity;
import ritman.wallet.beneficairyRegistration.BeneficiaryRegistrationActivity;
import ritman.wallet.billpayment.BillPaymentMainActivity;
import ritman.wallet.di.JSONdi.restRequest.SimpleRequest;
import ritman.wallet.di.generic_response.SimpleResponse;
import ritman.wallet.di.JSONdi.retrofit.RestClient;
import ritman.wallet.R;
import ritman.wallet.databinding.FragmentHomeBinding;
import ritman.wallet.fragments.BaseFragment;
import ritman.wallet.home_module.NewDashboardActivity;
import ritman.wallet.interfaces.OnDecisionMade;
import ritman.wallet.utils.BitmapHelper;


public class HomeFragment extends BaseFragment<FragmentHomeBinding> implements OnDecisionMade {

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        getUpdatedBalance();
        binding.swipeRefresh.setOnRefreshListener(this::getUpdatedBalance);


        binding.moneyTransfer.setOnClickListener(view ->
                startActivity(new Intent(getActivity(), MoneyTransferMainLayout.class)));

        binding.checkRatesCard.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), BeneficiaryRegistrationActivity.class);
            startActivity(intent);
        });


        binding.createWallet.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RegistrationActivity.class);
            startActivity(intent);
        });


        binding.transactionHistoryCard.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TransactionHistoryActivity.class);
            startActivity(intent);
        });

        Glide.with(this)
                .load(BitmapHelper.decodeImage(((NewDashboardActivity) getBaseActivity()).sessionManager.getCustomerImage()))
                .placeholder(R.drawable.user_profile_home)
                .into(binding.userImageHome);


        binding.mobileTopUpCarview.setOnClickListener(view ->
                        startActivity(new Intent(getActivity(), MobileTopUpMainActivity.class))
                //startActivity(new Intent(getActivity(), LoyalityPointsActivity.class))
        );
        binding.billPaymentCarview.setOnClickListener(view ->
                        startActivity(new Intent(getActivity(), BillPaymentMainActivity.class))
                //  startActivity(new Intent(getActivity(), LoyalityPointsActivity.class))
        );

        binding.aeps.setOnClickListener(view ->
                        startActivity(new Intent(getActivity(), AEPSActivity.class))
                //  startActivity(new Intent(getActivity(), LoyalityPointsActivity.class))
        );

        binding.pancard.setOnClickListener(view ->
                        startActivity(new Intent(getActivity(), PanCardActivity.class))
                //  startActivity(new Intent(getActivity(), LoyalityPointsActivity.class))
        );

        binding.ekyc.setOnClickListener(view ->
                        startActivity(new Intent(getActivity(), eKYCActivity.class))
                //  startActivity(new Intent(getActivity(), LoyalityPointsActivity.class))
        );

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
                        binding.tvBalance.setText(response.body().data);
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