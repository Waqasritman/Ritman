package ritman.wallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import ritman.wallet.base.RitmanBaseActivity;
import ritman.wallet.databinding.ActivityBankDetailsBinding;
import ritman.wallet.di.JSONdi.restRequest.BankNetworkListRequest;
import ritman.wallet.di.JSONdi.restResponse.BankListResponse;
import ritman.wallet.di.JSONdi.retrofit.RestClient;
import ritman.wallet.dialogs.DialogBankNetwork;
import ritman.wallet.interfaces.OnSelectBank;
import ritman.wallet.personal_loan.fragment.Partner_Bank_Fragment;
import ritman.wallet.utils.IsNetworkConnection;

public class BankDetailsActivity extends RitmanBaseActivity<ActivityBankDetailsBinding> implements
        OnSelectBank {
    String existingBank,branchName,ifscCode;
    @Override
    public int getLayoutId() {
        return R.layout.activity_bank_details;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        binding.bankDetails.setVisibility(View.VISIBLE);
//        if (savedInstanceState==null){
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.partner_Bank_Fragment,new Partner_Bank_Fragment())
//                    .commit();
//        }
        binding.toolBar.backBtn
                .setColorFilter(ContextCompat.getColor(this,
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);
        binding.toolBar.toolBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.toolBar.titleTxt.setText(getString(R.string.create_customer_tool_title));


        binding.toolBar.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.toolBar.crossBtn.setOnClickListener(v->{
            onClose();
        });

        binding.btnSearch.setOnClickListener(v -> {
            binding.bankDetails.setVisibility(View.VISIBLE);
            int count = 0;

            if (TextUtils.isEmpty(binding.ifscCode1.getText())) {
                count += 1;
            }
            if (TextUtils.isEmpty(binding.existingBank1.getText().toString())) {
                count += 1;
            }

            if (count < 2) {
                if (IsNetworkConnection.checkNetworkConnection(getApplicationContext())) {
                    getBankList();


                } else {
                    onMessage(getString(R.string.no_internet));
                }
            } else {
                onMessage("Enter bank name or ifsc code for bank details");
            }

        });
    }

    void getBankList() {
        binding.progressBar.setVisibility(View.VISIBLE);
        // binding.txtSearchBank.setVisibility(View.GONE);
        BankNetworkListRequest request = new BankNetworkListRequest();
        request.BankName = binding.existingBank1.getText().toString();
        request.BranchIFSC = binding.ifscCode1.getText().toString();
        request.BranchName = binding.branchName1.getText().toString();

        Call<BankListResponse> call = RestClient.get().getBankNetworkList(RestClient.makeGSONRequestBody(request)
                , sessionManager.getMerchantName());
        call.enqueue(new retrofit2.Callback<BankListResponse>() {
            @Override
            public void onResponse(Call<BankListResponse> call, Response<BankListResponse> response) {
                binding.progressBar.setVisibility(View.GONE);
                // binding.txtSearchBank.setVisibility(View.VISIBLE);

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
                binding.progressBar.setVisibility(View.GONE);
                // binding.txtSearchBank.setVisibility(View.VISIBLE);
            }
        });

    }


    void showBankList(List<BankListResponse> responseList) {
        DialogBankNetwork banks = new DialogBankNetwork(responseList, this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        banks.show(transaction, "");
    }

    @Override
    public void onSelectBank(BankListResponse bankDetails) {
        existingBank=bankDetails.getBankName();
        branchName=bankDetails.getBranchName();
        ifscCode=bankDetails.getBankCode();


        /*Bundle bundle = new Bundle();
        String myMessage = "Stackoverflow is cool!";
        bundle.putString("message", myMessage );
        Partner_Bank_Fragment fragInfo = new Partner_Bank_Fragment();
        fragInfo.setArguments(bundle);
        transaction.replace(R.id.fragment_single, fragInfo);
        transaction.commit();*/

        Bundle b = new Bundle();
        b.putString("branchName", branchName);
        b.putString("existingBank", existingBank);
        b.putString("ifscCode", ifscCode);
        FragmentManager fragmentManager = getSupportFragmentManager();
        //fragmentManager.popBackStack();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Partner_Bank_Fragment myFragment = new Partner_Bank_Fragment();

        myFragment.setArguments(b);
        fragmentTransaction.replace(R.id.partner_Bank_Fragment,myFragment);
      //  fragmentTransaction.addToBackStack(null);
       // fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onSelectBankName(String bankName) {

    }

    @Override
    public void onSelectBranch(BankListResponse branchName) {

    }
}