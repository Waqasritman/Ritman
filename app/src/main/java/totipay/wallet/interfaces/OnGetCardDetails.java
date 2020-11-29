package totipay.wallet.interfaces;

import java.util.List;

import totipay.wallet.di.ResponseHelper.GetCardDetailsResponse;

public interface OnGetCardDetails extends OnMessageInterface {
    void onCardDetailsGet(List<GetCardDetailsResponse> cardDetailsResponses);

    void onSelectCard(GetCardDetailsResponse cardDetail);
}
