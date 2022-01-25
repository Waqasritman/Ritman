package angoothape.wallet.bus_booking.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import angoothape.wallet.R;
import angoothape.wallet.adapters.BusSeatingLayoutAdapter;
import angoothape.wallet.adapters.PassengerInformationAdapter;
import angoothape.wallet.bus_booking.BusBookingMainActivity;
import angoothape.wallet.databinding.FragmentBusPassengerDetailsBinding;
import angoothape.wallet.di.JSONdi.NetworkResource;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.models.BoardingInfo;
import angoothape.wallet.di.JSONdi.restRequest.BusBlockTicketRequest;
import angoothape.wallet.di.JSONdi.restResponse.BusBlockTicketResponse;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.interfaces.OnPassengerInformationDetails;
import angoothape.wallet.utils.Utils;
import angoothape.wallet.viewmodels.BusBookingViewModel;


public class BusPassengerDetails extends BaseFragment<FragmentBusPassengerDetailsBinding>
        implements OnPassengerInformationDetails {

    PassengerInformationAdapter adapter;
    // List<BoardingInfo> selectedSeats;

    BusBookingViewModel viewModel;

    @Override
    protected void injectView() {

    }


    @Override
    public boolean isValidate() {
        if (binding.contactNo.getText().toString().isEmpty()) {
            onMessage("Please enter contact no");
            return false;
        } else if (binding.contactNo.getText().toString().length() != 10) {
            onMessage("Please enter valid contact no");
            return false;
        } else if (binding.emailAddress.getText().toString().isEmpty()) {
            onMessage("Please enter email address");
            return false;
        } else if (binding.address.getText().toString().isEmpty()) {
            onMessage("Please enter address");
            return false;
        }

        for (int i = 0; i < viewModel.selectedSeats.size(); i++) {
            if (viewModel.selectedSeats.get(i).passengerName.isEmpty()) {
                onMessage("Please enter passenger name for seat no ".concat(viewModel.selectedSeats.get(i).seatNo));
                return false;
            } else if (viewModel.selectedSeats.get(i).passengerAge.isEmpty()) {
                onMessage("Please enter passenger age for seat no ".concat(viewModel.selectedSeats.get(i).seatNo));
                return false;
            }
        }
        return true;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = ((BusBookingMainActivity) getBaseActivity()).viewModel;
        //   selectedSeats = viewModel.selectedSeats;
        setupRecyclerView();

        binding.bookTicket.setOnClickListener(v -> {
            if (isValidate()) {
                blockTickets();

            }
        });
    }


    private void blockTickets() {
        //  BusBlockTicketRequest request = viewModel.busBlockTicketRequest;
        Utils.showCustomProgressDialog(getBaseActivity(), false);
        viewModel.busBlockTicketRequest.operator_id = viewModel.busSeatingLayoutRequest.operator_id;
        viewModel.busBlockTicketRequest.journey_date = viewModel.busSeatingLayoutRequest.journey_date;
        viewModel.busBlockTicketRequest.service_id = viewModel.busSeatingLayoutRequest.service_id;
        viewModel.busBlockTicketRequest.layoutId = viewModel.busSeatingLayoutRequest.layoutId;
        viewModel.busBlockTicketRequest.source_ID = viewModel.busSeatingLayoutRequest.Source_ID;
        viewModel.busBlockTicketRequest.destinationID = viewModel.busSeatingLayoutRequest.designation_id;
        viewModel.busBlockTicketRequest.boardingPointID = viewModel.busSeatingLayoutRequest.boardingPointID;
        viewModel.busBlockTicketRequest.droppingPointID = viewModel.busSeatingLayoutRequest.droppingPointID;
        viewModel.busBlockTicketRequest.address = binding.address.getText().toString();
        viewModel.busBlockTicketRequest.contactNumber = binding.contactNo.getText().toString();
        viewModel.busBlockTicketRequest.emailid = binding.emailAddress.getText().toString();
        viewModel.busBlockTicketRequest.agelist = getAgeList();
        viewModel.busBlockTicketRequest.genderlist = getGender();
        viewModel.busBlockTicketRequest.namelist = getNameList();
        viewModel.busBlockTicketRequest.seatNumbersList = getSeatNumberList();
        viewModel.busBlockTicketRequest.seatFareList = getSeatFareList();
        viewModel.busBlockTicketRequest.seatTypesList = getSeatTypeList();
        viewModel.busBlockTicketRequest.seatTypeIds = getSeatTypeIds();
        viewModel.busBlockTicketRequest.isAcSeat = "0";
        viewModel.busBlockTicketRequest.seatLayoutUniqueId = viewModel.seatLayoutUniqueId;
        viewModel.busBlockTicketRequest.serviceTaxList = getServiceTax();
        viewModel.busBlockTicketRequest.issingleLadyspecify = true;
        viewModel.busBlockTicketRequest.concessionId = viewModel.concessionId;
        viewModel.busBlockTicketRequest.isconcessionIdspecify = false;
        viewModel.busBlockTicketRequest.additionalInfoValue = viewModel.additionalInfoValue;

        Log.e("blockTickets: ", viewModel.busBlockTicketRequest.toString());
        viewModel.busBlockTickets(viewModel.busBlockTicketRequest, getSessionManager().getMerchantName())
                .observe(getViewLifecycleOwner(), response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                    } else if (response.status == Status.SUCCESS) {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            Bundle bundle = new Bundle();
                            bundle.putString("refer_no", response.resource.data.referenceNo);
                            Navigation.findNavController(binding.getRoot())
                                    .navigate(R.id.busSeatConfirmedFragment, bundle);
                        }
                    }
                });
    }

    private String getServiceTax() {
        String genderText = "";
        for (int i = 0; i < viewModel.selectedSeats.size(); i++) {
            if (genderText.isEmpty()) {
                genderText = viewModel.selectedSeats.get(i).serviceTax;
            } else {
                genderText = genderText.concat(",").concat(viewModel.selectedSeats.get(i).serviceTax);
            }

        }
        return genderText.replace(" ", "");
    }


    private String getSeatTypeIds() {
        String genderText = "";
        for (int i = 0; i < viewModel.selectedSeats.size(); i++) {
            if (genderText.isEmpty()) {
                genderText = viewModel.selectedSeats.get(i).seatTypeId;
            } else {
                genderText = genderText.concat(",").concat(viewModel.selectedSeats.get(i).seatTypeId);
            }

        }
        return genderText.replace(" ", "");
    }

    private String getSeatTypeList() {
        String genderText = "";
        for (int i = 0; i < viewModel.selectedSeats.size(); i++) {
            if (genderText.isEmpty()) {
                genderText = viewModel.selectedSeats.get(i).seatType;
            } else {
                genderText = genderText.concat(",").concat(viewModel.selectedSeats.get(i).seatType);
            }

        }
        return genderText.replace(" ", "");
    }

    private String getSeatFareList() {
        String genderText = "";
        for (int i = 0; i < viewModel.selectedSeats.size(); i++) {
            if (genderText.isEmpty()) {
                genderText = viewModel.selectedSeats.get(i).fare;
            } else {
                genderText = genderText.concat(",").concat(viewModel.selectedSeats.get(i).fare);
            }

        }
        return genderText.replace(" ", "");
    }

    private String getNameList() {
        String genderText = "";
        for (int i = 0; i < viewModel.selectedSeats.size(); i++) {
            if (genderText.isEmpty()) {
                genderText = viewModel.selectedSeats.get(i).passengerName;
            } else {
                genderText = genderText.concat(",").concat(viewModel.selectedSeats.get(i).passengerName);
            }

        }
        return genderText.replace(" ", "");
    }


    private String getSeatNumberList() {
        String text = "";
        for (int i = 0; i < viewModel.selectedSeats.size(); i++) {
            if (text.isEmpty()) {
                text = viewModel.selectedSeats.get(i).seatNo;
            } else {
                text = text.concat(",").concat(viewModel.selectedSeats.get(i).seatNo);
            }

        }
        return text.replace(" ", "");
    }

    private String getGender() {
        String genderText = "";
        for (int i = 0; i < viewModel.selectedSeats.size(); i++) {
            if (genderText.isEmpty()) {
                genderText = viewModel.selectedSeats.get(i).gender;
            } else {
                genderText = genderText.concat(",").concat(viewModel.selectedSeats.get(i).gender);
            }

        }
        return genderText.replace(" ", "");
    }

    private String getAgeList() {
        String text = "";
        for (int i = 0; i < viewModel.selectedSeats.size(); i++) {
            if (text.isEmpty()) {
                text = viewModel.selectedSeats.get(i).passengerAge;
            } else {
                text = text.concat(",").concat(viewModel.selectedSeats.get(i).passengerAge);
            }

        }
        return text.replace(" ", "");
    }


    private void setupRecyclerView() {
        adapter = new
                PassengerInformationAdapter(viewModel.selectedSeats, this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getBaseActivity(), RecyclerView.VERTICAL, false));
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_bus_passenger_details;
    }

    @Override
    public void onChangeName(String name, int position) {
        viewModel.selectedSeats.get(position).passengerName = name;
    }

    @Override
    public void onChangeAge(String age, int position) {
        viewModel.selectedSeats.get(position).passengerAge = age;
    }
}