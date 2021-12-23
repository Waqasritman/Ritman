package angoothape.wallet.bus_booking.fragments;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


import angoothape.wallet.R;
import angoothape.wallet.databinding.FragmentBusStationsBinding;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.GetBUSStationsRequest;
import angoothape.wallet.di.JSONdi.restRequest.GetBusAvailableServiceRequest;
import angoothape.wallet.di.JSONdi.restRequest.GetBusDestinationsRequest;
import angoothape.wallet.di.JSONdi.restResponse.GetBusStationsResponse;
import angoothape.wallet.dialogs.SelectBusStationDialog;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.interfaces.OnSelectBusStation;
import angoothape.wallet.utils.DateAndTime;
import angoothape.wallet.utils.Utils;
import angoothape.wallet.viewmodels.BusBookingViewModel;

public class BusStationsFragment extends BaseFragment<FragmentBusStationsBinding>
        implements OnSelectBusStation, DatePickerDialog.OnDateSetListener {

    BusBookingViewModel viewModel;
    List<GetBusStationsResponse> sourceList;
    List<GetBusStationsResponse> destinationList;
    boolean isSource = false;
    GetBusAvailableServiceRequest busAvailableServiceRequest;

    @Override
    protected void injectView() {

    }


    @Override
    public void onResume() {
        super.onResume();
        if (busAvailableServiceRequest != null) {
            binding.selectSource.setText(busAvailableServiceRequest.startStation);
            binding.selectDestination.setText(busAvailableServiceRequest.endStation);
            binding.selectDate.setText(busAvailableServiceRequest.journey_date);
        }
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(BusBookingViewModel.class);
        sourceList = new ArrayList<>();
        destinationList = new ArrayList<>();
        busAvailableServiceRequest = new GetBusAvailableServiceRequest();
        binding.selectSource.setOnClickListener(v -> {
            isSource = true;
            if (sourceList.isEmpty()) {
                getSourceDestination();
            } else {
                openSourceDialog(sourceList);
            }
        });


        binding.selectDestination.setOnClickListener(v -> {
            if (!busAvailableServiceRequest.source_id.isEmpty()) {
                isSource = false;
                if (destinationList.isEmpty()) {
                    getDestination();
                } else {
                    openSourceDialog(destinationList);
                }
            } else {
                onMessage("Select Source Bus station");
            }

        });


        binding.selectDate.setOnClickListener(v -> {
            showPickerDialog("Select journey date");
        });


        binding.findBus.setOnClickListener(v -> {

            if (binding.selectSource.getText().toString().isEmpty()) {
                onMessage("Select Source Bus station");
            } else if (binding.selectDestination.getText().toString().isEmpty()) {
                onMessage("Select Destination Bus station");
            } else if (binding.selectDate.getText().toString().isEmpty()) {
                onMessage("Select journey date");
            } else {
                Bundle bundle = new Bundle();
                bundle.putParcelable("data", busAvailableServiceRequest);

                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.busServiceFragment, bundle);
            }


        });

    }


    public void getDestination() {
        Utils.showCustomProgressDialog(getContext(), false);
        GetBusDestinationsRequest request = new GetBusDestinationsRequest();
        request.source_station_id = busAvailableServiceRequest.source_id;

        viewModel.getBusDestination(request, getSessionManager().getMerchantName())
                .observe(getViewLifecycleOwner(), response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                    } else if (response.status == Status.SUCCESS) {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            destinationList.clear();
                            destinationList.addAll(response.resource.getData());
                        }

                        openSourceDialog(destinationList);
                    }
                });
    }


    /**
     * dialog code for show date picker
     */
    private void showPickerDialog(String title) {
        Calendar calendar = Calendar.getInstance();


        int Year = calendar.get(Calendar.YEAR);
        int Month = calendar.get(Calendar.MONTH);
        int Day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = DatePickerDialog
                .newInstance(this, Year, Month, Day);
//        if(isDateOfBirth) {
//            Year = calendar.get(Calendar.YEAR - 18);
//        }
        datePickerDialog.setThemeDark(false);
        datePickerDialog.showYearPickerFirst(false);
        datePickerDialog.setAccentColor(Color.parseColor("#332D78"));
        datePickerDialog.setLocale(new Locale("en"));

        datePickerDialog.setMinDate(calendar);


        datePickerDialog.setTitle(title);
        datePickerDialog.show(getParentFragmentManager(), "");
        datePickerDialog.setOnCancelListener(DialogInterface::dismiss);
    }

    public void getSourceDestination() {
        Utils.showCustomProgressDialog(getContext(), false);
        viewModel.getBusStations(new GetBUSStationsRequest(), getSessionManager().getMerchantName())
                .observe(getViewLifecycleOwner(), response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                    } else if (response.status == Status.SUCCESS) {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            sourceList.clear();
                            sourceList.addAll(response.resource.getData());
                        }

                        openSourceDialog(sourceList);
                    }
                });
    }


    public void openSourceDialog(List<GetBusStationsResponse> sourceList) {
        SelectBusStationDialog dialog = new SelectBusStationDialog(sourceList, this);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        dialog.show(transaction, "");

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_bus_stations;
    }

    @Override
    public void onSelectBusStation(GetBusStationsResponse response) {
        if (isSource) {
            busAvailableServiceRequest.source_id = String.valueOf(response.id);
            busAvailableServiceRequest.startStation = response.name;
            binding.selectSource.setText(response.name);
            //    binding.selectSource.setTextColor(getBaseActivity().getResources().getColor(R.color.colorBlack));
            destinationList.clear();
        } else {
            busAvailableServiceRequest.endStation = response.name;
            busAvailableServiceRequest.designation_id = String.valueOf(response.id);
            binding.selectDestination.setText(response.name);
            //  binding.selectDestination.setTextColor(getBaseActivity().getResources().getColor(R.color.colorBlack));
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        binding.selectDate.setText(DateAndTime.getBusFormat(year, monthOfYear, dayOfMonth));
        //binding.selectDate.setTextColor(getBaseActivity().getResources().getColor(R.color.colorBlack));
        busAvailableServiceRequest.journey_date = binding.selectDate.getText().toString();
    }
}