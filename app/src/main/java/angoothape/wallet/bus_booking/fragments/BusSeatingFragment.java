package angoothape.wallet.bus_booking.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import angoothape.wallet.R;
import angoothape.wallet.adapters.BusSeatingLayoutAdapter;
import angoothape.wallet.bus_booking.BusBookingMainActivity;
import angoothape.wallet.databinding.FragmentBusSeatingBinding;
import angoothape.wallet.di.JSONdi.Status;
import angoothape.wallet.di.JSONdi.models.BoardingInfo;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.interfaces.OnSelectSeat;
import angoothape.wallet.viewmodels.BusBookingViewModel;

public class BusSeatingFragment extends BaseFragment<FragmentBusSeatingBinding>
        implements OnSelectSeat {

    List<String> upperDiskSeats;
    List<String> lowerDiskSeats;
    List<BoardingInfo> upperBoardingInfo;
    List<BoardingInfo> lowerBoardingInfo;

    List<BoardingInfo> selectedSeats;

    BusBookingViewModel viewModel;

    BusSeatingLayoutAdapter lowerAdapter;
    BusSeatingLayoutAdapter upperAdapter;

    public BusSeatingFragment() {
        // Required empty public constructor
    }

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = ((BusBookingMainActivity) getBaseActivity()).viewModel;
        upperBoardingInfo = new ArrayList<>();
        lowerBoardingInfo = new ArrayList<>();
        selectedSeats = new ArrayList<>();
        setupLowerRecyclerView();
        setupUpperRecyclerView();
        getSeatingLayout();

        binding.fare.setText("Fare: INR ".concat(viewModel.busSeatingLayoutRequest.seatFareList));


        binding.upperDesk.setOnClickListener(v -> {
            binding.upperDesk.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            binding.upperDesk.setTextColor(getResources().getColor(R.color.colorWhite));

            binding.lowerDesk.setBackgroundColor(getResources().getColor(R.color.white));
            binding.lowerDesk.setTextColor(getResources().getColor(R.color.colorBlack));
            binding.upperRecyclerView.setVisibility(View.VISIBLE);
            binding.lowerRecyclerView.setVisibility(View.GONE);
//            boardingInfoList.clear();
//            boardingInfoList.addAll(getSeatLayout(upperDiskSeats, false));
//            adapter.notifyDataSetChanged();
        });


        binding.lowerDesk.setOnClickListener(v -> {
            binding.lowerDesk.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            binding.lowerDesk.setTextColor(getResources().getColor(R.color.colorWhite));

            binding.upperDesk.setBackgroundColor(getResources().getColor(R.color.white));
            binding.upperDesk.setTextColor(getResources().getColor(R.color.colorBlack));
            binding.upperRecyclerView.setVisibility(View.GONE);
            binding.lowerRecyclerView.setVisibility(View.VISIBLE);
//            boardingInfoList.clear();
//            boardingInfoList.addAll(getSeatLayout(lowerDiskSeats, true));
//            adapter.notifyDataSetChanged();
        });
    }


    private void setupLowerRecyclerView() {
        GridLayoutManager mLayoutManager = new GridLayoutManager(getBaseActivity(), 3);
        lowerAdapter = new
                BusSeatingLayoutAdapter(getContext(), lowerBoardingInfo, this);
        binding.lowerRecyclerView.setLayoutManager(mLayoutManager);
        binding.lowerRecyclerView.setHasFixedSize(true);
        binding.lowerRecyclerView.setAdapter(lowerAdapter);
    }

    private void setupUpperRecyclerView() {
        GridLayoutManager mLayoutManager = new GridLayoutManager(getBaseActivity(), 3);
        upperAdapter = new
                BusSeatingLayoutAdapter(getContext(), upperBoardingInfo, this);
        binding.upperRecyclerView.setLayoutManager(mLayoutManager);
        binding.upperRecyclerView.setHasFixedSize(true);
        binding.upperRecyclerView.setAdapter(upperAdapter);
    }


    public void getSeatingLayout() {
        viewModel.getBusSeatingLayoutRequest(viewModel.busSeatingLayoutRequest, getSessionManager().getMerchantName())
                .observe(getViewLifecycleOwner(), response -> {
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                    } else if (response.status == Status.SUCCESS) {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            lowerBoardingInfo.clear();
                            upperBoardingInfo.clear();
                            lowerDiskSeats = new ArrayList<>();
                            upperDiskSeats = new ArrayList<>();
                            lowerDiskSeats = response.resource.data.seatLayout.totalSeatList.lowerDiskSeats;
                            upperDiskSeats = response.resource.data.seatLayout.totalSeatList.upperDiskSeats;

                            lowerBoardingInfo.addAll(getSeatLayout(lowerDiskSeats, true));
                            upperBoardingInfo.addAll(getSeatLayout(upperDiskSeats, false));

                            binding.lowerRecyclerView.setVisibility(View.VISIBLE);
                            binding.upperRecyclerView.setVisibility(View.GONE);

                            lowerAdapter.notifyDataSetChanged();
                            upperAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    public List<BoardingInfo> getSeatLayout(List<String> seatsLayout, boolean lower) {
        List<BoardingInfo> boardingInfoList = new ArrayList<>();
        int count;
        if (lower) {
            count = 100;
        } else {
            count = 1000;
        }

        for (String la : seatsLayout) {
            String[] layout = la.split(",");
            count++;
            BoardingInfo boardingInfo = new BoardingInfo();
            boardingInfo.id = count;
            boardingInfo.seatNo = layout[0];
            boardingInfo.rowNo = layout[1];
            boardingInfo.colNo = layout[2];
            boardingInfo.seatType = layout[3];
            boardingInfo.availbility = layout[4];
            boardingInfo.gender = layout[5];
            boardingInfo.fare = layout[6];
            boardingInfo.seatTypeId = layout[7];
            boardingInfo.serviceTax = layout[8];
            boardingInfo.childFare = layout[9];

            boardingInfoList.add(boardingInfo);
        }
        return boardingInfoList;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_bus_seating;
    }

    @Override
    public void onSelectSeat(BoardingInfo boardingInfo) {
        binding.summaryView.setVisibility(View.VISIBLE);
        selectedSeats.add(boardingInfo);
        addInvoice(boardingInfo);

//        if (viewModel.busBlockTicketRequest.seatNumbersList.isEmpty()) {
//            viewModel.busBlockTicketRequest.seatNumbersList = boardingInfo.seatNo;
//            viewModel.busBlockTicketRequest.genderlist = boardingInfo.gender;
//            viewModel.busBlockTicketRequest.seatFareList = boardingInfo.fare;
//            viewModel.busBlockTicketRequest.seatTypeIds = boardingInfo.seatTypeId;
//            viewModel.busBlockTicketRequest.seatTypesList = boardingInfo.seatType;
//        } else {
//            viewModel.busBlockTicketRequest.seatNumbersList =
//                    viewModel.busBlockTicketRequest.seatNumbersList.concat(",").concat(boardingInfo.seatNo);
//            viewModel.busBlockTicketRequest.genderlist =
//                    viewModel.busBlockTicketRequest.genderlist.concat(",").concat(boardingInfo.gender);
//            viewModel.busBlockTicketRequest.seatFareList =
//                    viewModel.busBlockTicketRequest.seatFareList.concat(",").concat(boardingInfo.fare);
//            viewModel.busBlockTicketRequest.seatTypeIds =
//                    viewModel.busBlockTicketRequest.seatTypeIds.concat(",").concat(boardingInfo.seatTypeId);
//            viewModel.busBlockTicketRequest.seatTypesList =
//                    viewModel.busBlockTicketRequest.seatTypesList.concat(",").concat(boardingInfo.seatType);
//        }

    }

    @Override
    public void onUnSelectSeat(BoardingInfo boardingInfo) {
        for (int i = 0; i < selectedSeats.size(); i++) {
            if (boardingInfo.id == selectedSeats.get(i).id) {
                selectedSeats.remove(i);
                break;
            }
        }
        if (selectedSeats.size() > 0) {
            detectInvoice();
        } else {
            binding.infoTxt.setText("");
            binding.summaryView.setVisibility(View.GONE);
        }

    }

    public void detectInvoice() {
        if (selectedSeats.size() == 1) {
            BoardingInfo boardingInfo = selectedSeats.get(0);
            binding.infoTxt.setText(boardingInfo.seatNo);
            binding.itemTotal.setText(boardingInfo.fare);
            binding.serviceFee.setText(boardingInfo.serviceTax);
            double total = Double.parseDouble(boardingInfo.fare) + Double.parseDouble(boardingInfo.serviceTax);
            binding.tvInvoiceOderTotal.setText(String.valueOf(total));
        } else {
            String seats = "";
            double fare = 0.0;
            double serviceTax = 0.0;
            for (int i = 0; i < selectedSeats.size(); i++) {
                BoardingInfo boardingInfo = selectedSeats.get(i);
                if (seats.isEmpty()) {
                    seats = boardingInfo.seatNo;
                } else {
                    seats = seats.concat(",").concat(boardingInfo.seatNo);
                }

                fare = fare + Double.parseDouble(boardingInfo.fare);
                serviceTax = serviceTax + Double.parseDouble(boardingInfo.serviceTax);

            }
            binding.infoTxt.setText(seats);

            binding.itemTotal.setText(String.valueOf(fare));
            binding.serviceFee.setText(String.valueOf(serviceTax));
            double total = fare + serviceTax;
            binding.tvInvoiceOderTotal.setText(String.valueOf(total));

        }


    }

    public void addInvoice(BoardingInfo boardingInfo) {
        if (binding.infoTxt.getText().toString().isEmpty()) {
            binding.infoTxt.setText(boardingInfo.seatNo);
            binding.itemTotal.setText(boardingInfo.fare);
            binding.serviceFee.setText(boardingInfo.serviceTax);
            double total = Double.parseDouble(boardingInfo.fare) + Double.parseDouble(boardingInfo.serviceTax);
            binding.tvInvoiceOderTotal.setText(String.valueOf(total));
        } else {
            binding.infoTxt.setText(binding.infoTxt.getText().toString().concat(",").concat(boardingInfo.seatNo));
            double fare = Double.parseDouble(binding.itemTotal.getText().toString()) + Double.parseDouble(boardingInfo.fare);
            double serviceTax = Double.parseDouble(binding.serviceFee.getText().toString()) + Double.parseDouble(boardingInfo.serviceTax);

            binding.itemTotal.setText(String.valueOf(fare));
            binding.serviceFee.setText(String.valueOf(serviceTax));
            double total = fare + serviceTax;
            binding.tvInvoiceOderTotal.setText(String.valueOf(total));
        }
    }
}