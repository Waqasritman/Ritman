package angoothape.wallet.beneficiaryselection;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import angoothape.wallet.MoneyTransferModuleV.MoneyTransferMainLayout;
import angoothape.wallet.adapters.CustomerBeneficiaryListAdapter;
import angoothape.wallet.R;

import angoothape.wallet.beneficairyRegistration.BeneficiaryRegistrationActivity;
import angoothape.wallet.beneficairyRegistration.viewmodel.RegisterBeneficiaryViewModel;
import angoothape.wallet.databinding.ActivitySelectBeneficialyBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.ActiveDeActiveBeneRequest;
import angoothape.wallet.di.JSONdi.restRequest.GetBeneficiaryRequest;
import angoothape.wallet.di.JSONdi.restRequest.VerifyBeneficiaryRequest;
import angoothape.wallet.di.JSONdi.restResponse.GetBalanceCustomerLimit;
import angoothape.wallet.di.JSONdi.restResponse.aepssattlement.AEPSBeneficiary;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.di.XMLdi.ResponseHelper.GetBeneficiaryListResponse;

import angoothape.wallet.dialogs.SingleButtonMessageDialog;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.interfaces.OnDecisionMade;
import angoothape.wallet.interfaces.OnSelectBeneficiary;
import angoothape.wallet.utils.BeneficiarySelector;
import angoothape.wallet.utils.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SelectBeneficiaryFragment extends BaseFragment<ActivitySelectBeneficialyBinding>
        implements OnSelectBeneficiary, OnDecisionMade {

    public List<GetBeneficiaryListResponse> list = null;
    CustomerBeneficiaryListAdapter adapter;
    RegisterBeneficiaryViewModel viewModel;

    String customerN0;
    int type = BeneficiarySelector.BANK_TRANSFER;
    boolean isDMTLive = false;

    @Override
    protected void injectView() {

    }

    @Override
    public void onResume() {
        super.onResume();

        if (getBaseActivity() instanceof BeneficiaryRegistrationActivity) {
            viewModel = ((BeneficiaryRegistrationActivity) getBaseActivity()).viewModel;
            ((BeneficiaryRegistrationActivity) getBaseActivity()).binding.toolBar.titleTxt
                    .setText(getString(R.string.beneficiary));
        } else {
            viewModel = ((MoneyTransferMainLayout) getBaseActivity()).viewModel;
            ((MoneyTransferMainLayout) getBaseActivity()).binding.toolBar.titleTxt
                    .setText(getString(R.string.beneficiary));

            binding.searchBtn.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.colorPrimary));
            binding.seachBene.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.background_beneficiary_bank_pos_red));
            binding.addBene.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.background_button));
            binding.addBene.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.colorPrimary));
        }


        if (list != null) {
            if (list.size() > 0) {
                binding.searchBtn.setVisibility(View.GONE);
                binding.mainView.setVisibility(View.VISIBLE);
            }
        } else if (!binding.seachBene.getText().toString().isEmpty()) {
            getBeneficiary();
        }
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.toolBarFinal.setVisibility(View.GONE);
        if (getArguments() != null) {
            type = getArguments().getInt("transfer_type", BeneficiarySelector.BANK_TRANSFER);
        }

        list = new ArrayList<>();
        setAccountsRecyclerView();


        binding.addBene.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("customer_no", customerN0);
            bundle.putBoolean("isDMTLive", isDMTLive);
            bundle.putBoolean("isAEPSBene", false);
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_selectBeneficiaryFragment_to_sendMoneyViaBankFirstActivity
                            , bundle);
        });


        binding.searchBtn.setOnClickListener(v -> {
            if (TextUtils.isEmpty(binding.seachBene.getText().toString())) {
                onMessage(getString(R.string.please_enter_customr_number));
            } else {
                getBeneficiary();
            }
        });
        binding.seachBene.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.mainView.setVisibility(View.GONE);
                binding.searchBtn.setVisibility(View.VISIBLE);
                binding.searchViw.setVisibility(View.VISIBLE);
            }
        });
    }


    public void getBeneficiary() {
        Utils.showCustomProgressDialog(getContext(), false);

        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim();

        GetBeneficiaryRequest request = new GetBeneficiaryRequest();
        request.CustomerNo = binding.seachBene.getText().toString();
        String body = RestClient.makeGSONString(request);

        AERequest aeRequest = new AERequest();
        aeRequest.body = AESHelper.encrypt(body.trim(), gKey.trim());

        viewModel.getBeneficiaryList(aeRequest, KeyHelper.getKey(getSessionManager().getMerchantName()).trim(), KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim())
                .observe(getViewLifecycleOwner(), response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {

                            list.clear();

                            String bodyy = AESHelper.decrypt(response.resource.data.body
                                    , gKey);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<GetBeneficiaryListResponse>>() {
                                }.getType();
                                List<GetBeneficiaryListResponse> data = gson.fromJson(bodyy, type);
                                list.addAll(data);
                                customerN0 = data.get(0).customerNo;
                                adapter.notifyDataSetChanged();
                                getBalanceCustomerLimit();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            if (list.size() >= 25) {
                                binding.addBene.setVisibility(View.GONE);
                            }


                        } else if (response.resource.responseCode.equals(721)) {
                            registerCustomerDialog();
                        } else if (response.resource.responseCode.equals(206)) {
                            binding.searchBtn.setVisibility(View.GONE);
                            binding.mainView.setVisibility(View.VISIBLE);

                            String[] a = response.resource.description.split("\\|");

                            customerN0 = a[2];
                            binding.seachBene.requestFocus();
                            getBalanceCustomerLimit();
                        } else {
                            onError(response.resource.description);
                        }
                    }
                });
    }

    public void getBalanceCustomerLimit() {
        Utils.showCustomProgressDialog(getContext(), false);
        GetBeneficiaryRequest request = new GetBeneficiaryRequest();
        request.Customer_MobileNo = binding.seachBene.getText().toString();

        viewModel.getBalanceLimitForCustomer(request, getSessionManager().getMerchantName().trim())
                .observe(getViewLifecycleOwner(), response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        binding.searchBtn.setVisibility(View.GONE);
                        binding.mainView.setVisibility(View.VISIBLE);
                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            binding.searchBtn.setVisibility(View.GONE);
                            binding.mainView.setVisibility(View.VISIBLE);

                            binding.dailyLimit.setText("Monthly Limit: "
                                    .concat(response.resource.data.dailyAvailLimit_));
                            //055501500920
                            binding.dailyLimit.setVisibility(View.VISIBLE);
                            isDMTLive = response.resource.isDMTLive;
                        } else {
                            binding.searchBtn.setVisibility(View.GONE);
                            binding.mainView.setVisibility(View.VISIBLE);
                            onError(response.resource.description);
                        }
                    }
                });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_beneficialy;
    }


    @Override
    public void showProgress() {
        super.showProgress();
        binding.mainView.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);
    }


    @Override
    public void hideProgress() {
        super.hideProgress();
        binding.mainView.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.GONE);
    }

    /**
     * setup the recycler view when screen load after that just notify the adapter
     */
    private void setAccountsRecyclerView() {
        handleViews();
        adapter = new
                CustomerBeneficiaryListAdapter(list, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        binding.beneficiaryRecyclerView.setLayoutManager(mLayoutManager);
        binding.beneficiaryRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onSelectBeneficiary(GetBeneficiaryListResponse response) {

        if (type == BeneficiarySelector.VERIFY_BENE) {
            verifyBeneficiary(response.beneficiaryNumber, response.customerNo);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("customer_no", binding.seachBene.getText().toString());
            bundle.putParcelable("bene", response); //bene add

            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_selectBeneficiaryFragment_to_bank_tranfer_navigation
                            , bundle);
        }
    }

    @Override
    public void onSelectAEPSBeneficiary(AEPSBeneficiary response) {

    }

    @Override
    public void onChangeTheStatusOfBeneficiary(GetBeneficiaryListResponse response, int pushToActive
            , int position) {
        activeDeactive(response.beneficiaryNumber, pushToActive, position);
    }


    private void activeDeactive(String beneId, int pushToActive, int position) {
        Utils.showCustomProgressDialog(getContext(), false);
        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim();

        ActiveDeActiveBeneRequest request = new ActiveDeActiveBeneRequest();
        request.CustomerNo = customerN0;
        request.BeneficiaryNo = beneId;
        request.BeneficiaryStatus = pushToActive;
        String body = RestClient.makeGSONString(request);
        Log.e("activeDeactive: ", body);
        AERequest aeRequest = new AERequest();
        aeRequest.body = AESHelper.encrypt(body.trim(), gKey.trim());

        viewModel.activeDeActiveBeneficiary(aeRequest, KeyHelper.getKey(getSessionManager().getMerchantName()).trim(), KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim())
                .observe(getViewLifecycleOwner(), response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        String bodyy = AESHelper.decrypt(response.resource.data.body, gKey);
                        if (response.resource.responseCode.equals(101)) {
                            Log.e("bodyy: ", bodyy);

                            list.remove(position);
                            adapter.notifyDataSetChanged();
                            if (list.size() >= 25) {
                                binding.addBene.setVisibility(View.GONE);
                            } else {
                                binding.addBene.setVisibility(View.VISIBLE);
                            }
                            Log.e("activeDeactive: ", bodyy);
                        } else {
                            onError(response.resource.description);
                        }
                    }
                });
    }


    private void verifyBeneficiary(String beneNo, String customerNo) {
        Utils.showCustomProgressDialog(getContext(), false);
        String gKey = KeyHelper.getKey(getSessionManager().getMerchantName()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(getSessionManager().getMerchantName())).trim();

        VerifyBeneficiaryRequest verifyBeneficiaryRequest = new VerifyBeneficiaryRequest();
        verifyBeneficiaryRequest.Credentials.LanguageID = Integer.parseInt(
                getSessionManager().getlanguageselection());
        verifyBeneficiaryRequest.customerNo = customerNo;
        verifyBeneficiaryRequest.BeneficiaryNo = beneNo;
        String body = RestClient.makeGSONString(verifyBeneficiaryRequest);

        AERequest request = new AERequest();
        request.body = AESHelper.encrypt(body.trim(), gKey.trim());
        viewModel.verifyBeneficairy(request, KeyHelper.getKey(getSessionManager().getMerchantName()).trim(),
                KeyHelper.getSKey(KeyHelper
                        .getKey(getSessionManager().getMerchantName())))
                .observe(getViewLifecycleOwner(), response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        onError(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            showSucces("Beneficiary Verified Successfully", getString(R.string.successfully_txt)
                                    , false);
                        } else {
                            showSucces(response.resource.description, "Beneficiary verification failed", true);
                        }
                    }
                });
    }

    private void showSucces(String message, String title, boolean isError) {
        SingleButtonMessageDialog dialog = new
                SingleButtonMessageDialog(title
                , message, this,
                false, isError);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }


    private void handleViews() {
        if (list.isEmpty()) {
            binding.dailyLimit.setVisibility(View.GONE);
        } else {

            binding.dailyLimit.setVisibility(View.VISIBLE);
        }
    }


    private void showSuccess(String message, String title, boolean isError) {
        SingleButtonMessageDialog dialog = new
                SingleButtonMessageDialog(title
                , message, this,
                false, isError);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }

    private void registerCustomerDialog() {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View deleteDialogView = factory.inflate(R.layout.register_customer_dialog_layout, null);

        final AlertDialog deleteDialog = new AlertDialog.Builder(getActivity()).create();
        deleteDialog.setView(deleteDialogView);
        deleteDialogView.findViewById(R.id.btn_register).setOnClickListener(v -> {
            deleteDialog.dismiss();
            onProceed();
        });
        deleteDialogView.findViewById(R.id.cancel_button).setOnClickListener(v -> deleteDialog.dismiss());

        deleteDialog.show();
    }

    @Override
    public void onProceed() {
        Bundle bundle = new Bundle();
        bundle.putString("phone", binding.seachBene.getText().toString());
        Navigation.findNavController(binding.getRoot())
                .navigate(R.id.registrationActivity, bundle);
    }

    @Override
    public void onCancel(boolean goBack) {

    }
}
