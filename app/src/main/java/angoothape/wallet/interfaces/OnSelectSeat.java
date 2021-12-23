package angoothape.wallet.interfaces;

import angoothape.wallet.di.JSONdi.models.BoardingInfo;

public interface OnSelectSeat {
    void onSelectSeat(BoardingInfo boardingInfo);

    void onUnSelectSeat(BoardingInfo boardingInfo);
}
