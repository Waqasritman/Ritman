package ritman.wallet.MoneyTransferModuleV.cashtransfer_doortodoor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;

import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ritman.wallet.MerchantLoginActivity;
import ritman.wallet.MoneyTransferModuleV.MoneyTransferMainLayout;
import ritman.wallet.MoneyTransferModuleV.TransactionReceiptActivity;
import ritman.wallet.MoneyTransferModuleV.viewmodels.BankTransferViewModel;
import ritman.wallet.R;
import ritman.wallet.VerifyOtpActivity;
import ritman.wallet.beneficairyRegistration.BeneficiaryRegistrationActivity;
import ritman.wallet.databinding.FragmentMoneyTransferSummaryBinding;
import ritman.wallet.di.JSONdi.AppExecutors;
import ritman.wallet.di.JSONdi.NetworkResource;
import ritman.wallet.di.JSONdi.Status;
import ritman.wallet.di.JSONdi.restRequest.CalTransferRequest;
import ritman.wallet.di.JSONdi.restRequest.GetOtpRequest;
import ritman.wallet.di.JSONdi.restRequest.RitmanPaySendRequest;
import ritman.wallet.di.JSONdi.restRequest.SimpleRequest;
import ritman.wallet.di.JSONdi.restRequest.TransactionRecieptRequest;
import ritman.wallet.di.JSONdi.restResponse.CaltransferResponse;
import ritman.wallet.di.JSONdi.restResponse.GetTransactionReceiptResponse;
import ritman.wallet.di.JSONdi.restResponse.RitmanPaySendResponse;
import ritman.wallet.di.JSONdi.retrofit.RestApi;
import ritman.wallet.di.JSONdi.retrofit.RestClient;
import ritman.wallet.di.XMLdi.RequestHelper.TootiPayRequest;
import ritman.wallet.di.XMLdi.ResponseHelper.GetBeneficiaryListResponse;
import ritman.wallet.di.generic_response.SimpleResponse;
import ritman.wallet.fragments.BaseFragment;
import ritman.wallet.home_module.NewDashboardActivity;
import ritman.wallet.utils.SessionManager;
import ritman.wallet.utils.Utils;

public class CashTransferSummaryFragment extends BaseFragment<FragmentMoneyTransferSummaryBinding> {

    String totalPayable, customerNo;
    boolean isCash = true;
    public SessionManager sessionManager;
    GetBeneficiaryListResponse benedetails;
    Double PayInAmount;
    BankTransferViewModel viewModel;
    String TransactionNumber;


    @Override
    public void onResume() {
        super.onResume();
        if (getBaseActivity() instanceof BeneficiaryRegistrationActivity) {
            ((BeneficiaryRegistrationActivity) getBaseActivity()).binding.toolBar.titleTxt
                    .setText(getString(R.string.summary));
        } else {
            ((MoneyTransferMainLayout) getBaseActivity()).binding.toolBar.titleTxt
                    .setText(getString(R.string.summary));

            binding.confirmBtn.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.colorPrimary));
        }

    }


    @Override
    protected void injectView() {

    }


    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(BankTransferViewModel.class);
        assert getArguments() != null;
        totalPayable = getArguments().getString("total_payable");
        isCash = getArguments().getBoolean("is_from_cash", true);

        benedetails = getArguments().getParcelable("bene");
        customerNo = getArguments().getString("customer_no");
        PayInAmount = getArguments().getDouble("PayInAmount");

        binding.totalPayableAmount.setText(totalPayable);
        binding.transferAmount.setText(PayInAmount.toString());

        binding.beneficairyName.setText(benedetails.firstName.concat("  ").concat(benedetails.lastName));
        binding.sendingCurrency.setText("INR");


        binding.confirmBtn.setOnClickListener(v -> {

            Bundle bundle = new Bundle();
            bundle.putDouble("PayInAmount", PayInAmount);
            bundle.putParcelable("benedetails", benedetails);
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_cashTransferSummaryFragment2_to_generateOtpFragment,bundle
                    );

        });
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_money_transfer_summary;
    }

}