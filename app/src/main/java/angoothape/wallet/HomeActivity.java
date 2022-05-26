package angoothape.wallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.ActivityHomeBinding;
import angoothape.wallet.home_module.fragments.HomeFragment;

public class HomeActivity extends RitmanBaseActivity<ActivityHomeBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        moveToFragment(new DashBoardFragment());
    }

    public void moveToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame, fragment).commit();
    }

}