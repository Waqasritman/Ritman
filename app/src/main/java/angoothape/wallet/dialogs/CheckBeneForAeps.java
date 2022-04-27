package angoothape.wallet.dialogs;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import angoothape.wallet.R;
import angoothape.wallet.RegisterFromAEPS;
import angoothape.wallet.aeps.activity.SelectDeviceActivity;
import angoothape.wallet.base.BaseDialogFragment;
import angoothape.wallet.beneficairyRegistration.viewmodel.RegisterBeneficiaryViewModel;
import angoothape.wallet.databinding.ActivitySelectBeneficialyBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.GetBeneficiaryRequest;
import angoothape.wallet.di.JSONdi.restResponse.aepssattlement.AEPSBeneficiary;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.di.XMLdi.ResponseHelper.GetBeneficiaryListResponse;
import angoothape.wallet.interfaces.OnDecisionMade;
import angoothape.wallet.interfaces.OnSelectBeneficiary;
import angoothape.wallet.utils.BeneficiarySelector;
import angoothape.wallet.utils.Utils;

public class CheckBeneForAeps extends BaseDialogFragment<ActivitySelectBeneficialyBinding>
        implements OnSelectBeneficiary, OnDecisionMade {

    RegisterBeneficiaryViewModel viewModel;
    int type = BeneficiarySelector.BANK_TRANSFER;


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
        viewModel = new ViewModelProvider(this).get(RegisterBeneficiaryViewModel.class);
        binding.backBtn.setOnClickListener(v -> {
            getBaseActivity().onBackPressed();
        });

        binding.titleTxt.setText("AEPS Beneficiary");
        binding.crossBtn.setVisibility(View.GONE);


        ////
        binding.searchBtn.setOnClickListener(v -> {
            if (TextUtils.isEmpty(binding.seachBene.getText().toString())) {
                onMessage(getString(R.string.enter_beneficairy_number));
            } else {
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
                                onMessage(getString(response.messageResourceId));
                            } else {
                                assert response.resource != null;
                                if (response.resource.responseCode.equals(101)
                                        || response.resource.responseCode.equals(206)) {
                                    startActivity(new Intent(getActivity(), SelectDeviceActivity.class));
                                } else if (response.resource.responseCode.equals(721)) {
                                    registerCustomerDialog();

                                } else if (response.resource.responseCode.equals(206)) {
                                    binding.searchBtn.setVisibility(View.GONE);
                                    binding.mainView.setVisibility(View.VISIBLE);
                                    binding.seachBene.requestFocus();

                                    // onMessage(response.resource.description);
                                } else {
                                    onMessage(response.resource.description);
                                }
                            }
                        });
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


    private void registerCustomerDialog() {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View deleteDialogView = factory.inflate(R.layout.register_customer_dialog_layout, null);
        final android.app.AlertDialog deleteDialog = new AlertDialog.Builder(getActivity()).create();
        deleteDialog.setView(deleteDialogView);
        deleteDialogView.findViewById(R.id.btn_register).setOnClickListener(v -> {

            Intent intent = new Intent(getActivity(), RegisterFromAEPS.class);
            intent.putExtra("phone", binding.seachBene.getText().toString());
            startActivity(intent);
            //  getBaseActivity().finish();
        });
        deleteDialogView.findViewById(R.id.cancel_button).setOnClickListener(v -> deleteDialog.dismiss());

        deleteDialog.show();
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

    @Override
    public void onProceed() {

    }

    @Override
    public void onCancel(boolean goBack) {

    }

    @Override
    public void onSelectBeneficiary(GetBeneficiaryListResponse response) {

    }

    @Override
    public void onSelectAEPSBeneficiary(AEPSBeneficiary response) {

    }

    @Override
    public void onChangeTheStatusOfBeneficiary(GetBeneficiaryListResponse response, int pushToActive, int position) {

    }
}
