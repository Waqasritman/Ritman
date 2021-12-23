package angoothape.wallet.bus_booking.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import angoothape.wallet.R;
import angoothape.wallet.bus_booking.BusBookingMainActivity;
import angoothape.wallet.databinding.FragmentBusRouteInformationBinding;
import angoothape.wallet.di.JSONdi.models.RoutePickUp;
import angoothape.wallet.di.JSONdi.restRequest.BusSeatingLayoutRequest;
import angoothape.wallet.di.JSONdi.restResponse.Services;
import angoothape.wallet.dialogs.BusRouteDialog;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.interfaces.OnSelectRoute;
import angoothape.wallet.viewmodels.BusBookingViewModel;

public class BusRouteInformationFragment extends BaseFragment<FragmentBusRouteInformationBinding>
        implements OnSelectRoute {


    Services services;
    BusBookingViewModel viewModel;
    public boolean isPickUpSelected = false;


    BusSeatingLayoutRequest busSeatingLayoutRequest;

    public BusRouteInformationFragment() {
        // Required empty public constructor

    }


    @Override
    public boolean isValidate() {
        if (binding.selectedService.getText().toString().isEmpty()) {
            onMessage("Please select boarding point");
            return false;
        } else if (binding.selectDestination.getText().toString().isEmpty()) {
            onMessage("Please select dropping point");
            return false;
        }
        return true;
    }

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = ((BusBookingMainActivity) getBaseActivity()).viewModel;
        busSeatingLayoutRequest = viewModel.busSeatingLayoutRequest;

        services = getArguments().getParcelable("selected_service");
        busSeatingLayoutRequest.Source_ID = String.valueOf(services.source_ID);
        busSeatingLayoutRequest.designation_id = String.valueOf(services.destination_ID);
        busSeatingLayoutRequest.journey_date = services.jdate;
        busSeatingLayoutRequest.operator_id = services.operatorId;
        busSeatingLayoutRequest.service_id = services.service_key;
        busSeatingLayoutRequest.singleLady = services.isSingleLady;
        busSeatingLayoutRequest.layoutId = String.valueOf(services.layout_id);
        busSeatingLayoutRequest.seatFareList = String.valueOf(services.fare);
        busSeatingLayoutRequest.concessionId = "0";


        binding.selectedService.setText(services.traveler_Agent_Name);
        binding.selectSource.setOnClickListener(v -> {
            isPickUpSelected = true;
            openDialog("PickUp Station", viewModel.getBusRoute(services.boarding_info));
        });

        binding.selectDestination.setOnClickListener(v -> {
            isPickUpSelected = false;
            openDialog("DropOff Station", viewModel.getBusRoute(services.dropping_info));
        });


        binding.findBus.setOnClickListener(v -> {
            if (isValidate()) {
                ((BusBookingMainActivity) getBaseActivity()).viewModel
                        .busSeatingLayoutRequest = busSeatingLayoutRequest;
                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.busSeatingFragment);
            }

        });
    }


    void openDialog(String title, List<RoutePickUp> routes) {
        BusRouteDialog dialog = new BusRouteDialog(title, routes, this);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_bus_route_information;
    }

    @Override
    public void onSelectRoute(RoutePickUp routePickUp) {
        if (isPickUpSelected) {
            binding.selectSource.setText(routePickUp.station.concat(" ").concat(routePickUp.time));
        } else {
            binding.selectDestination.setText(routePickUp.station.concat(" ").concat(routePickUp.time));
        }
    }
}