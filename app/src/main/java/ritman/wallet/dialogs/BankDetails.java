package ritman.wallet.dialogs;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import ritman.wallet.R;
import ritman.wallet.base.BaseDialogFragment;
import ritman.wallet.databinding.BankDetailsLayoutBinding;
import ritman.wallet.di.JSONdi.restRequest.BankNetworkListRequest;
import ritman.wallet.di.JSONdi.restResponse.BankListResponse;
import ritman.wallet.di.JSONdi.retrofit.RestClient;
import ritman.wallet.fragments.BaseFragment;
import ritman.wallet.interfaces.OnSelectBank;
import ritman.wallet.utils.IsNetworkConnection;

public class BankDetails extends BaseDialogFragment<BankDetailsLayoutBinding> implements
        OnSelectBank {

    @Override
    public int getTheme() {
        return R.style.AppTheme_NoActionBar_FullScreenDialog;
    }

    @Override
    protected void injectView() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.closeDialog.setOnClickListener(v -> {
            cancelUpload();
        });

        binding.titleOfPage.setText(getString(R.string.select_bank));
        binding.btnSearch.setOnClickListener(v -> {
            int count = 0;
            if (TextUtils.isEmpty(binding.ifscCode1.getText())) {
                count += 1;
            }
            if (TextUtils.isEmpty(binding.existingBank1.getText().toString())) {
                count += 1;
            }

            if (count < 2) {
                if (IsNetworkConnection.checkNetworkConnection(getContext())) {
                    getBankList();
//                    binding.bankDetails.setVisibility(View.GONE);
//                    binding.linearDisable.setVisibility(View.VISIBLE);
//                    binding.linearAccountno.setVisibility(View.VISIBLE);
//                    binding.txtSearchBankDetail.setVisibility(View.GONE);
//                    binding.txtPartnerBankDetail.setVisibility(View.VISIBLE);
//
//                    binding.acNo.setEnabled(true);
//                    binding.reEnterAccountNumber.setEnabled(true);
//                    binding.remark.setEnabled(true);

                } else {
                    onMessage(getString(R.string.no_internet));
                }
            } else {
                onMessage("Enter bank name or ifsc code for bank details");
            }

        });
    }

    void getBankList() {
      //  binding.progressBar.setVisibility(View.VISIBLE);
      //  binding.txtSearchBank.setVisibility(View.GONE);
        BankNetworkListRequest request = new BankNetworkListRequest();
        request.BankName = binding.existingBank1.getText().toString();
        request.BranchIFSC = binding.ifscCode1.getText().toString();
        request.BranchName = binding.branchName1.getText().toString();

        Call<BankListResponse> call = RestClient.get().getBankNetworkList(RestClient.makeGSONRequestBody(request)
                , getSessionManager().getMerchantName());
        call.enqueue(new retrofit2.Callback<BankListResponse>() {
            @Override
            public void onResponse(Call<BankListResponse> call, Response<BankListResponse> response) {
              //  binding.progressBar.setVisibility(View.GONE);
              //  binding.txtSearchBank.setVisibility(View.VISIBLE);

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().responseCode.equals(101)) {
                        showBankList(response.body().data);
                    } else {
                        onMessage(response.body().description);
                    }
                } else {
                    onMessage(getString(R.string.some_thing_wrong));
                }
            }

            @Override
            public void onFailure(Call<BankListResponse> call, Throwable t) {
                onMessage(t.getLocalizedMessage());
              //  binding.progressBar.setVisibility(View.GONE);
             //   binding.txtSearchBank.setVisibility(View.VISIBLE);
            }
        });

    }

    void showBankList(List<BankListResponse> responseList) {
        DialogBankNetwork banks = new DialogBankNetwork(responseList, this);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        banks.show(transaction, "");
    }

    @Override
    public int getLayoutId() {
        return R.layout.bank_details_layout;
    }

    @Override
    public void onSelectBank(BankListResponse bankDetails) {

    }

    @Override
    public void onSelectBankName(String bankName) {

    }

    @Override
    public void onSelectBranch(BankListResponse branchName) {

    }
}
