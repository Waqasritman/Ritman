package ritman.wallet.interfaces;

import ritman.wallet.di.JSONdi.restResponse.RelationListResponse;
import ritman.wallet.di.XMLdi.ResponseHelper.GetRelationListResponse;

public interface OnSelectRelation {
    void onSelectRelation(RelationListResponse relation);
}
