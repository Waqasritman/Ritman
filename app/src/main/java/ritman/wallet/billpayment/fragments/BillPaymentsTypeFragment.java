package ritman.wallet.billpayment.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ritman.wallet.MoneyTransferModuleV.viewmodels.BankTransferViewModel;
import ritman.wallet.R;
import ritman.wallet.adapters.WRBillerTypeAdapter;
import ritman.wallet.billpayment.BillPaymentMainActivity;
import ritman.wallet.databinding.ActivityBillPaymentsBinding;
import ritman.wallet.di.JSONdi.Status;
import ritman.wallet.di.JSONdi.restRequest.GetWRBillerTypeRequestNew;
import ritman.wallet.di.JSONdi.restRequest.TransactionRecieptRequest;
import ritman.wallet.di.JSONdi.restResponse.GetWRBillerTypeResponse;
import ritman.wallet.di.XMLdi.RequestHelper.GetWRBillerTypeRequest;
import ritman.wallet.di.XMLdi.ResponseHelper.WRBillerTypeResponse;
import ritman.wallet.di.XMLdi.apicaller.WRBillerTypeTask;
import ritman.wallet.fragments.BaseFragment;
import ritman.wallet.interfaces.OnWRBillerType;
import ritman.wallet.utils.IsNetworkConnection;

public class BillPaymentsTypeFragment extends BaseFragment<ActivityBillPaymentsBinding>
        implements OnWRBillerType {


    List<GetWRBillerTypeResponse> wrBillerTypeResponseList;
    WRBillerTypeAdapter adapter;
    BankTransferViewModel viewModel;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(BankTransferViewModel.class);

        wrBillerTypeResponseList = new ArrayList<>();
        setSearchView();
        setupRecyclerView();
        binding.title.setText(getString(R.string.select_recharge_type));

        getBillPaymentType();




      /*  if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            GetWRBillerTypeRequest request = new GetWRBillerTypeRequest();
            request.languageId = getSessionManager().getlanguageselection();

            WRBillerTypeTask task = new WRBillerTypeTask(getContext(), this);
            task.execute(request);
        } else {
            onMessage(getString(R.string.no_internet));
        }*/
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_bill_payments;
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


    /**
     * Method will set the recycler view
     */
    private void setupRecyclerView() {

        Collections.sort(wrBillerTypeResponseList, (o1, o2) ->
                o1.getName().compareToIgnoreCase(o2.getName()));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new
                WRBillerTypeAdapter(getContext() , wrBillerTypeResponseList, this);
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBillerTypeList(List<GetWRBillerTypeResponse> billerTypeList) {
        wrBillerTypeResponseList.clear();
        wrBillerTypeResponseList.addAll(billerTypeList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBillerTypeSelect(GetWRBillerTypeResponse billerType) {
        ((BillPaymentMainActivity) getBaseActivity())
                .plansRequest.billerTypeID = String.valueOf(billerType.getID());
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }


    private  void getBillPaymentType() {
        GetWRBillerTypeRequestNew request = new GetWRBillerTypeRequestNew();
        request.CountryCode="IN";


        viewModel.GetWRBillerType(request ,getSessionManager().getMerchantName()).observe(getViewLifecycleOwner()

                , response -> {
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                    } else {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            onBillerTypeList(response.resource.data);
                        } else {
                            onMessage(response.resource.description);
                        }
                    }
                });
    }
}