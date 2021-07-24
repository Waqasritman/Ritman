package ritman.wallet.interfaces;

import ritman.wallet.di.XMLdi.ResponseHelper.GetSendRecCurrencyResponse;

import java.util.List;

public interface OnSelectCurrency extends OnMessageInterface{
    void onCurrencyResponse(List<GetSendRecCurrencyResponse> response);
    void onCurrencySelect(GetSendRecCurrencyResponse response);
}
