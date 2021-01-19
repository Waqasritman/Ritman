package tootipay.wallet.billpayment.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tootipay.wallet.R;
import tootipay.wallet.adapters.WRBillerOperatorAdapter;
import tootipay.wallet.billpayment.BillPaymentMainActivity;
import tootipay.wallet.databinding.ActivityBillPaymentCountryBinding;
import tootipay.wallet.di.RequestHelper.GetWRBillerNamesRequest;
import tootipay.wallet.di.ResponseHelper.WRBillerNamesResponse;
import tootipay.wallet.di.apicaller.GetWRBillerNamesTask;
import tootipay.wallet.fragments.BaseFragment;
import tootipay.wallet.interfaces.OnWRBillerNames;
import tootipay.wallet.utils.IsNetworkConnection;

public class UtilityPaymentOperatorFragment extends BaseFragment<ActivityBillPaymentCountryBinding>
        implements OnWRBillerNames {

    List<WRBillerNamesResponse> wrBillerNamesResponseList;
    WRBillerOperatorAdapter adapter;


    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        wrBillerNamesResponseList = new ArrayList<>();
        setSearchView();
        setupRecyclerView();
        binding.title.setText(getString(R.string.plz_select_bill_comppany));

        GetWRBillerNamesRequest request = new GetWRBillerNamesRequest();
        request.countryCode = ((BillPaymentMainActivity) getBaseActivity()).plansRequest.countryCode;
        request.billerCategoryID = ((BillPaymentMainActivity) getBaseActivity()).plansRequest.billerCategoryId;
        request.billerTypeID = ((BillPaymentMainActivity) getBaseActivity()).plansRequest.billerTypeID;
        request.languageId = getSessionManager().getlanguageselection();
        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            GetWRBillerNamesTask task = new GetWRBillerNamesTask(getActivity(), this);
            task.execute(request);
        } else {
            onMessage(getString(R.string.no_internet));
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_bill_payment_country;
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
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new
                WRBillerOperatorAdapter(getContext() , wrBillerNamesResponseList, this);
        binding.countryRecyclerView.setLayoutManager(mLayoutManager);
        binding.countryRecyclerView.setHasFixedSize(true);
        binding.countryRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onBillerNamesResponse(List<WRBillerNamesResponse> responses) {
        wrBillerNamesResponseList.clear();
        wrBillerNamesResponseList.addAll(responses);
        adapter.notifyDataSetChanged();
        binding.searchView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSelectBillerName(WRBillerNamesResponse billerName) {
        ((BillPaymentMainActivity) getBaseActivity()).plansRequest.billerID = billerName.billerId;

    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }
}
