package ritman.wallet.interfaces;

import java.util.List;

import ritman.wallet.di.XMLdi.ResponseHelper.YBranchResponse;

public interface OnGetYBranch extends OnMessageInterface {
    void onGetYBranch(List<YBranchResponse> branchList);

    void onSelectBranch(YBranchResponse branch);
}
