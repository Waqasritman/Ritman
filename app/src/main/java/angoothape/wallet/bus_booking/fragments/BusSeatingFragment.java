package angoothape.wallet.bus_booking.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.navigation.Navigation;
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
import angoothape.wallet.utils.Utils;
import angoothape.wallet.viewmodels.BusBookingViewModel;
import okhttp3.internal.Util;

public class BusSeatingFragment extends BaseFragment<FragmentBusSeatingBinding>
        implements OnSelectSeat {

    List<String> upperDiskSeats;
    List<String> lowerDiskSeats;
    List<BoardingInfo> upperBoardingInfo;
    List<BoardingInfo> lowerBoardingInfo;

    List<BoardingInfo> selectedSeats;

    public BusBookingViewModel viewModel;

    BusSeatingLayoutAdapter lowerAdapter;
    BusSeatingLayoutAdapter upperAdapter;


    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        viewModel = ((BusBookingMainActivity) getBaseActivity()).viewModel;

        selectedSeats = new ArrayList<>();
        selectedSeats.addAll(viewModel.selectedSeats);

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
        });


        binding.lowerDesk.setOnClickListener(v -> {
            binding.lowerDesk.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            binding.lowerDesk.setTextColor(getResources().getColor(R.color.colorWhite));

            binding.upperDesk.setBackgroundColor(getResources().getColor(R.color.white));
            binding.upperDesk.setTextColor(getResources().getColor(R.color.colorBlack));
            binding.upperRecyclerView.setVisibility(View.GONE);
            binding.lowerRecyclerView.setVisibility(View.VISIBLE);
        });


        binding.findBus.setOnClickListener(v -> {
            viewModel.selectedSeats.clear();
            viewModel.selectedSeats.addAll(selectedSeats);
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.busPassengerDetails);
        });
    }


    private void setupLowerRecyclerView() {
        GridLayoutManager mLayoutManager = new GridLayoutManager(getBaseActivity(), 3);
        lowerAdapter = new
                BusSeatingLayoutAdapter(this, lowerBoardingInfo, this);
        binding.lowerRecyclerView.setLayoutManager(mLayoutManager);
        binding.lowerRecyclerView.setHasFixedSize(true);
        binding.lowerRecyclerView.setAdapter(lowerAdapter);
    }

    private void setupUpperRecyclerView() {
        GridLayoutManager mLayoutManager = new GridLayoutManager(getBaseActivity(), 3);
        upperAdapter = new
                BusSeatingLayoutAdapter(this, upperBoardingInfo, this);
        binding.upperRecyclerView.setLayoutManager(mLayoutManager);
        binding.upperRecyclerView.setHasFixedSize(true);
        binding.upperRecyclerView.setAdapter(upperAdapter);
    }


    public void getSeatingLayout() {
        Utils.showCustomProgressDialog(getBaseActivity(), false);
        viewModel.getBusSeatingLayoutRequest(viewModel.busSeatingLayoutRequest,
                getSessionManager().getMerchantName())
                .observe(getViewLifecycleOwner(), response -> {
                    Utils.hideCustomProgressDialog();
                    if (response.status == Status.ERROR) {
                        onMessage(getString(response.messageResourceId));
                    } else if (response.status == Status.SUCCESS) {
                        assert response.resource != null;
                        if (response.resource.responseCode.equals(101)) {
                            upperBoardingInfo = new ArrayList<>();
                            lowerBoardingInfo = new ArrayList<>();
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

                            lowerAdapter.fillData(lowerBoardingInfo);
                            upperAdapter.fillData(upperBoardingInfo);
                            lowerAdapter.notifyDataSetChanged();
                            upperAdapter.notifyDataSetChanged();

                            if (response.resource.data.seatLayout.isAcSeat != null) {
                                viewModel.isAcSeat = response.resource.data.seatLayout.isAcSeat;
                            }
                            viewModel.seatLayoutUniqueId = response.resource.data.seatLayout.seatLayoutUniqueId;
                            viewModel.additionalInfoValue = response.resource.data.seatLayout.additionalInfoValue;

                            if (!selectedSeats.isEmpty()) {
                                detectInvoice();
                            }
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
            boardingInfo.seatNo = layout[0].replace(" ", "");
            boardingInfo.rowNo = layout[1].replace(" ", "");
            boardingInfo.colNo = layout[2].replace(" ", "");
            boardingInfo.seatType = layout[3].replace(" ", "");
            boardingInfo.availbility = layout[4].replace(" ", "");
            boardingInfo.gender = layout[5].replace(" ", "");
            boardingInfo.fare = layout[6].replace(" ", "");
            boardingInfo.seatTypeId = layout[7].replace(" ", "");
            boardingInfo.serviceTax = layout[8].replace(" ", "");
            boardingInfo.childFare = layout[9].replace(" ", "");

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

        if (!boardingInfo.isSelected) {
            boardingInfo.isSelected = true;
            selectedSeats.add(boardingInfo);
            addInvoice(boardingInfo);
        }


    }

    public boolean getIsThere(BoardingInfo selectedSeats) {
        for (int i = 0; i < viewModel.selectedSeats.size(); i++) {
            if (selectedSeats.id == viewModel.selectedSeats.get(i).id) {
                return true;
            }
        }
        return false;
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
        if (selectedSeats.isEmpty()) {
            binding.summaryView.setVisibility(View.GONE);
        } else {
            binding.summaryView.setVisibility(View.VISIBLE);
        }


    }


    public void showSummary() {

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