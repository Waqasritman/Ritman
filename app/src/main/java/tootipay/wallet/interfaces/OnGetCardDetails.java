package tootipay.wallet.interfaces;

import java.util.List;

import tootipay.wallet.di.ResponseHelper.GetCardDetailsResponse;

public interface OnGetCardDetails extends OnMessageInterface {
    void onCardDetailsGet(List<GetCardDetailsResponse> cardDetailsResponses);

    void onSelectCard(GetCardDetailsResponse cardDetail);
}
