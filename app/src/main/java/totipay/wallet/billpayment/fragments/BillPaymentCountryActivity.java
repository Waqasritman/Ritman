package totipay.wallet.billpayment.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import totipay.wallet.R;
import totipay.wallet.adapters.WRCountryListAdapter;
import totipay.wallet.billpayment.BillPaymentMainActivity;
import totipay.wallet.databinding.ActivityBillPaymentCountryBinding;
import totipay.wallet.di.RequestHelper.GetWRCountryListRequest;
import totipay.wallet.di.ResponseHelper.WRCountryListResponse;
import totipay.wallet.di.apicaller.GetWRCountryListTask;
import totipay.wallet.fragments.BaseFragment;
import totipay.wallet.interfaces.OnWRCountryList;
import totipay.wallet.utils.IsNetworkConnection;

public class BillPaymentCountryActivity extends BaseFragment<ActivityBillPaymentCountryBinding>
        implements OnWRCountryList {


    WRCountryListAdapter adapter;
    List<WRCountryListResponse> countryList;


    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        countryList = new ArrayList<>();
        setSearchView();
        setupRecyclerView();

        if (IsNetworkConnection.checkNetworkConnection(getContext())) {
            GetWRCountryListRequest request = new GetWRCountryListRequest();
            request.languageId = getSessionManager().getlanguageselection();

            GetWRCountryListTask task = new GetWRCountryListTask(getContext(), this);
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

        Collections.sort(countryList, (o1, o2) ->
                o1.countryName.compareToIgnoreCase(o2.countryName));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new
                WRCountryListAdapter(getContext() , countryList, this);
        binding.countryRecyclerView.setLayoutManager(mLayoutManager);
        binding.countryRecyclerView.setHasFixedSize(true);
        binding.countryRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onWRCountryList(List<WRCountryListResponse> list) {
        countryList.clear();
        countryList.addAll(list);
        binding.searchView.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onWRSelectCountry(WRCountryListResponse country) {
        ((BillPaymentMainActivity) getBaseActivity())
                .plansRequest.countryCode = country.countryShortName;

    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }
}