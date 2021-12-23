package angoothape.wallet.bus_booking.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harrywhewell.scrolldatepicker.DayScrollDatePicker;
import com.harrywhewell.scrolldatepicker.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import angoothape.wallet.R;
import angoothape.wallet.adapters.BusServicesAdapter;
import angoothape.wallet.adapters.BusStationsDetailAdapter;
import angoothape.wallet.databinding.FragmentBusServiceBinding;
import angoothape.wallet.databinding.FragmentBusStationsBinding;
import angoothape.wallet.di.JSONdi.NetworkResource;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.GetBusAvailableServiceRequest;
import angoothape.wallet.di.JSONdi.restResponse.GetAvailableServicesResponse;
import angoothape.wallet.di.JSONdi.restResponse.Services;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.interfaces.OnSelectBusService;
import angoothape.wallet.utils.DateAndTime;
import angoothape.wallet.utils.Utils;
import angoothape.wallet.viewmodels.BusBookingViewModel;


public class BusServiceFragment extends BaseFragment<FragmentBusServiceBinding>
        implements OnSelectBusService {

    List<Services> servicesResponseList;
    BusBookingViewModel viewModel;

    BusServicesAdapter adapter;

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(BusBookingViewModel.class);
        GetBusAvailableServiceRequest busAvailableServiceRequest = getArguments().getParcelable("data");
        servicesResponseList = new ArrayList<>();
        setupRecyclerView(busAvailableServiceRequest);
        getAvailableServices(busAvailableServiceRequest);

        binding.dayDatePicker.getSelectedDate(date -> {
            if (date != null) {
                busAvailableServiceRequest.journey_date = DateAndTime.getBusFormat(date);
                getAvailableServices(busAvailableServiceRequest);
            }
        });
    }


    public void getAvailableServices(GetBusAvailableServiceRequest busAvailableServiceRequest) {
        Utils.showCustomProgressDialog(getContext(), false);
        viewModel.getAvailableService(busAvailableServiceRequest
                , getSessionManager().getMerchantName()).observe(getViewLifecycleOwner(), response -> {
            Utils.hideCustomProgressDialog();
            if (response.status == Status.ERROR) {
                onMessage(getString(response.messageResourceId));
            } else if (response.status == Status.SUCCESS) {
                assert response.resource != null;
                if (response.resource.responseCode.equals(101)) {
                    servicesResponseList.clear();
                    servicesResponseList.addAll(response.resource.data.servicesList);
                    adapter.notifyDataSetChanged();
                } else {
                    onMessage(response.resource.description);
                }

                Log.e("services count: ", String.valueOf(servicesResponseList.size()));
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_bus_service;
    }


    /**
     * Method will set the recycler view
     *
     * @param busAvailableServiceRequest
     */
    private void setupRecyclerView(GetBusAvailableServiceRequest busAvailableServiceRequest) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new
                BusServicesAdapter(busAvailableServiceRequest.startStation
                , busAvailableServiceRequest.endStation, servicesResponseList, this);
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSelectBusService(Services selectedService) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("selected_service", selectedService);
        Navigation.findNavController(binding.getRoot())
                .navigate(R.id.busRouteInformationFragment, bundle);
    }
}