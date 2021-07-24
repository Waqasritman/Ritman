package ritman.wallet.beneficiaryselection;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ritman.wallet.MoneyTransferModuleV.MoneyTransferMainLayout;
import ritman.wallet.MoneyTransferModuleV.viewmodels.BankTransferViewModel;
import ritman.wallet.RegistrationActivity;
import ritman.wallet.adapters.CustomerBeneficiaryListAdapter;
import ritman.wallet.R;

import ritman.wallet.beneficairyRegistration.BeneficiaryRegistrationActivity;
import ritman.wallet.beneficairyRegistration.viewmodel.RegisterBeneficiaryViewModel;
import ritman.wallet.databinding.ActivitySelectBeneficialyBinding;
import ritman.wallet.di.JSONdi.NetworkResource;
import ritman.wallet.di.JSONdi.Status;
import ritman.wallet.di.JSONdi.restRequest.GetBeneficiaryRequest;
import ritman.wallet.di.JSONdi.restRequest.VerifyBeneficiaryRequest;
import ritman.wallet.di.JSONdi.restResponse.VerifyBeneficiary;
import ritman.wallet.di.XMLdi.ResponseHelper.GetBeneficiaryListResponse;


import ritman.wallet.dialogs.SingleButtonMessageDialog;
import ritman.wallet.fragments.BaseFragment;
import ritman.wallet.interfaces.OnDecisionMade;
import ritman.wallet.interfaces.OnSelectBeneficiary;
import ritman.wallet.utils.BeneficiarySelector;
import ritman.wallet.utils.IsNetworkConnection;
import ritman.wallet.utils.Utils;


import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;


public class SelectBeneficiaryFragment extends BaseFragment<ActivitySelectBeneficialyBinding>
        implements OnSelectBeneficiary, OnDecisionMade {

    public List<GetBeneficiaryListResponse> list = null;
    CustomerBeneficiaryListAdapter adapter;
    RegisterBeneficiaryViewModel viewModel;


    int type = BeneficiarySelector.BANK_TRANSFER;

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

            binding.searchBene.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.colorPrimary));
            binding.seachBene.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.background_beneficiary_bank_pos_red));
            binding.addBene.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.background_button));
            binding.addBene.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.colorPrimary));
        }


        if (list != null) {
            if (list.size() > 0) {
                binding.searchBene.setVisibility(View.GONE);
                binding.mainView.setVisibility(View.VISIBLE);
            }
        }


    }

    @Override
    protected void setUp(Bundle savedInstanceState) {

        if (getArguments() != null) {
            type = getArguments().getInt("transfer_type", BeneficiarySelector.BANK_TRANSFER);
        }

        list = new ArrayList<>();
        setAccountsRecyclerView();
        setSearchView();

        binding.addBene.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("customer_no", binding.seachBene.getText().toString());
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_selectBeneficiaryFragment_to_sendMoneyViaBankFirstActivity
                            , bundle);
        });


        binding.searchBene.setOnClickListener(v -> {
            if (TextUtils.isEmpty(binding.seachBene.getText().toString())) {
                onMessage(getString(R.string.enter_beneficairy_number));
            } else {
                Utils.showCustomProgressDialog(getContext(), false);
                GetBeneficiaryRequest request = new GetBeneficiaryRequest();
                request.CustomerNo = binding.seachBene.getText().toString();
                viewModel.getBeneficiaryList(request, getSessionManager().getMerchantName())
                        .observe(getViewLifecycleOwner(), response -> {
                            Utils.hideCustomProgressDialog();
                            if (response.status == Status.ERROR) {
                                onMessage(getString(response.messageResourceId));
                            } else {
                                assert response.resource != null;
                                if (response.resource.responseCode.equals(101)) {
                                    binding.searchBene.setVisibility(View.GONE);
                                    binding.mainView.setVisibility(View.VISIBLE);
                                    onMessage(response.resource.description);
                                    list.clear();
                                    list.addAll(response.resource.data);
                                    adapter.notifyDataSetChanged();
                                    handleViews();
                                } else if (response.resource.responseCode.equals(721)) {
                                    registerCustomerDialog();

                                }
                                else if (response.resource.responseCode.equals(206)
                                        && response.resource.data.isEmpty()) {
                                    binding.searchBene.setVisibility(View.GONE);
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
                binding.searchBene.setVisibility(View.VISIBLE);
                binding.searchViw.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_beneficialy;
    }

    /**
     * method will init the search box
     */
    private void setSearchView() {
        binding.searchView.setMaxWidth(Integer.MAX_VALUE);
        binding.searchView.requestFocus();
        binding.searchView.setFocusable(true);
        binding.searchView.setHint(getString(R.string.search));

        binding.searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) {
                    adapter.getFilter().filter(s);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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


    private void verifyBeneficiary(String beneNo, String customerNo) {
        VerifyBeneficiaryRequest request = new VerifyBeneficiaryRequest();
        request.Credentials.LanguageID = Integer.parseInt(
                getSessionManager().getlanguageselection());
        request.customerNo = customerNo;
        request.BeneficiaryNo = beneNo;

        Utils.showCustomProgressDialog(getContext(), false);
        viewModel.verifyBeneficairy(request, getSessionManager().getMerchantName())
                .observe(getViewLifecycleOwner(), response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                    } else {
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


    private void handleViews() {
        if (list.isEmpty()) {
            binding.searchView.setVisibility(View.GONE);
            binding.titleSlectBene.setVisibility(View.GONE);
        } else {
            binding.searchView.setVisibility(View.VISIBLE);
            binding.titleSlectBene.setVisibility(View.VISIBLE);
        }
    }


    private void showSucces(String message, String title, boolean isError) {
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
        deleteDialogView.findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), RegistrationActivity.class);
                startActivity(intent);
                getBaseActivity().finish();
            }
        });
        deleteDialogView.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });

        deleteDialog.show();
    }

    @Override
    public void onProceed() {

    }

    @Override
    public void onCancel() {

    }
}
