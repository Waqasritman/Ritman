package angoothape.wallet.bus_booking.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import angoothape.wallet.R;
import angoothape.wallet.bus_booking.BusBookingMainActivity;
import angoothape.wallet.databinding.FragmentBusSeatConfirmedBinding;
import angoothape.wallet.di.JSONdi.NetworkResource;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.restRequest.BusBookingCancelConfirmRequest;
import angoothape.wallet.di.JSONdi.restRequest.BusBookingPreCancelRequest;
import angoothape.wallet.di.JSONdi.restRequest.BusTicketConfirmTicketRequest;
import angoothape.wallet.di.JSONdi.restResponse.BusBookingCancelResponse;
import angoothape.wallet.di.JSONdi.restResponse.BusBookingPreCancelResponse;
import angoothape.wallet.di.JSONdi.restResponse.BusTicketConfirmResponse;
import angoothape.wallet.dialogs.BusBookingCancelConfirmationDialog;
import angoothape.wallet.dialogs.SingleButtonMessageDialog;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.interfaces.OnDecisionMade;
import angoothape.wallet.utils.Utils;
import angoothape.wallet.viewmodels.BusBookingViewModel;

public class BusSeatConfirmedFragment extends BaseFragment<FragmentBusSeatConfirmedBinding>
        implements OnDecisionMade {

    BusBookingViewModel viewModel;
    boolean isCancel = false;


    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {

        viewModel = ((BusBookingMainActivity) getBaseActivity()).viewModel;
        assert getArguments() != null;
        String referId = getArguments().getString("refer_no");
        confirmTicket(referId);


        binding.cancelButton.setOnClickListener(v -> {
            preCancellation();
        });
    }


    public void preCancellation() {
        Utils.showCustomProgressDialog(getBaseActivity(), false);
        BusBookingPreCancelRequest request = new BusBookingPreCancelRequest();
        request.ticket_no = binding.bookingId.getText().toString();
        request.phone_no = viewModel.busBlockTicketRequest.contactNumber;

        viewModel.busPreCancellation(request, getSessionManager().getMerchantName())
                .observe(getViewLifecycleOwner(), response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                    } else if (response.status == Status.SUCCESS) {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            showPopup(response.resource.data);
                        } else {
                            onMessage(response.resource.description);
                        }
                    }
                });

    }


    public void cancelBooking() {
        BusBookingCancelConfirmRequest request = new BusBookingCancelConfirmRequest();
        request.partial_cancel_no = "0";
        request.cancel_seat_no = viewModel.busBlockTicketRequest.seatNumbersList;
        request.phone_no = viewModel.busBlockTicketRequest.contactNumber;
        request.ticket_no = binding.bookingId.getText().toString();
        request.operator_id = viewModel.busBlockTicketRequest.operator_id;

        viewModel.busCancelBooking(request, getSessionManager().getMerchantName())
                .observe(getViewLifecycleOwner(), response -> {
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                    } else if (response.status == Status.SUCCESS) {
                        assert response.resource != null;
                        showPopup(response.resource.data.message, response.resource.data.status, false);
                    }
                });

    }

    private void showPopup(String message, String title, boolean isError) {
        isCancel = true;
        SingleButtonMessageDialog dialog = new
                SingleButtonMessageDialog(title
                , message, this,
                false, isError);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }

    public void showPopup(BusBookingPreCancelResponse resource) {
        BusBookingCancelConfirmationDialog dialog
                = new BusBookingCancelConfirmationDialog(resource, this);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }

    public void confirmTicket(String referID) {
        BusTicketConfirmTicketRequest request = new BusTicketConfirmTicketRequest();
        request.operator_id = viewModel.busBlockTicketRequest.operator_id;
        request.journey_date = viewModel.busBlockTicketRequest.journey_date;
        request.reference_id = referID;
        Utils.showCustomProgressDialog(getBaseActivity(), false);
        viewModel.busConfirmTicket(request, getSessionManager().getMerchantName())
                .observe(getViewLifecycleOwner(), response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                    } else if (response.status == Status.SUCCESS) {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            showInvoice(response.resource.data.ticketNo);
                        }
                    }
                });
    }


    public void showInvoice(String ticketNo) {
        binding.agentName.setText(viewModel.selectedServices.traveler_Agent_Name);
        binding.busType.setText(viewModel.selectedServices.bus_type);
        binding.journeyDate.setText(viewModel.selectedServices.jdate);
        binding.startingCity.setText(viewModel.busSeatingLayoutRequest.startStation);
        binding.endCity.setText(viewModel.busSeatingLayoutRequest.endStation);
        binding.startingTime.setText(viewModel.busSeatingLayoutRequest.startTime);
        binding.endTime.setText(viewModel.busSeatingLayoutRequest.endTime);

        binding.passengerNames.setText(viewModel.busBlockTicketRequest.namelist);
        binding.selectedSeats.setText(viewModel.busBlockTicketRequest.seatNumbersList);

        binding.bookingId.setText(ticketNo);
        binding.date.setText(viewModel.busBlockTicketRequest.journey_date);

        double serviceTax = getServiceTax();
        double fare = getSeatFareList();

        double total = serviceTax + fare;
        binding.itemTotal.setText(String.valueOf(fare));
        binding.serviceFee.setText(String.valueOf(serviceTax));
        binding.tvInvoiceOderTotal.setText(String.valueOf(total));

    }


    private double getServiceTax() {
        double tax = 0.0;
        for (int i = 0; i < viewModel.selectedSeats.size(); i++) {
            tax = tax + Double.parseDouble(viewModel.selectedSeats.get(i).serviceTax);
        }
        return tax;
    }


    private double getSeatFareList() {
        double fare = 0.0;
        for (int i = 0; i < viewModel.selectedSeats.size(); i++) {
            fare = fare + Double.parseDouble(viewModel.selectedSeats.get(i).fare);
        }
        return fare;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_bus_seat_confirmed;
    }

    @Override
    public void onProceed() {
        if (isCancel) {
            getBaseActivity().finish();
        } else {
            cancelBooking();
        }

    }

    @Override
    public void onCancel(boolean goBack) {

    }
}