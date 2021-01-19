package tootipay.wallet.interfaces;

import tootipay.wallet.di.ResponseHelper.GetRelationListResponse;

public interface OnSelectRelation {
    void onSelectRelation(GetRelationListResponse relation);
}
