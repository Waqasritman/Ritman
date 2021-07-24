package ritman.wallet.interfaces;

import java.util.List;

import ritman.wallet.di.XMLdi.ResponseHelper.GetCardDetailsResponse;

public interface OnGetCardDetails extends OnMessageInterface {
    void onCardDetailsGet(List<GetCardDetailsResponse> cardDetailsResponses);

    void onSelectCard(GetCardDetailsResponse cardDetail);
}
