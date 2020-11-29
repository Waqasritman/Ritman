package totipay.wallet.interfaces;

import totipay.wallet.di.ResponseHelper.GetSendRecCurrencyResponse;

import java.util.List;

public interface OnSelectCurrency extends OnMessageInterface{
    void onCurrencyResponse(List<GetSendRecCurrencyResponse> response);
    void onCurrencySelect(GetSendRecCurrencyResponse response);
}
