package angoothape.wallet.interfaces;

import angoothape.wallet.di.XMLdi.ResponseHelper.GetSendRecCurrencyResponse;

import java.util.List;

public interface OnSelectCurrency extends OnMessageInterface{
    void onCurrencyResponse(List<GetSendRecCurrencyResponse> response);
    void onCurrencySelect(GetSendRecCurrencyResponse response);
}
