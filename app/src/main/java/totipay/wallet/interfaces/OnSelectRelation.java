package totipay.wallet.interfaces;

import totipay.wallet.di.ResponseHelper.GetRelationListResponse;

public interface OnSelectRelation {
    void onSelectRelation(GetRelationListResponse relation);
}
