package angoothape.wallet.credopay.createMerchant

import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import angoothape.wallet.R
import angoothape.wallet.base.RitmanBaseActivity
import angoothape.wallet.databinding.ActivityCreateCreadMerchantBinding

class CreateCreadoMerchantActivity : RitmanBaseActivity<ActivityCreateCreadMerchantBinding>() {

    lateinit var navController: NavController

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (navController.currentDestination!!.id == R.id.microATMChooseFragment) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_create_cread_merchant
    }

    override fun initUi(savedInstanceState: Bundle?) {
        navController = Navigation.findNavController(this@CreateCreadoMerchantActivity, R.id.ekycnav)
        binding.toolBar.backBtn.setOnClickListener { v -> onBackPressed() }
        binding.toolBar.backBtn
                .setColorFilter(ContextCompat.getColor(this,
                        R.color.colorWhite), PorterDuff.Mode.SRC_IN)
        binding.toolBar.toolBar.setBackgroundColor(resources.getColor(R.color.posRed))
        binding.toolBar.titleTxt.setText("Micro ATM")
        binding.toolBar.backBtn.setOnClickListener(View.OnClickListener { v: View? -> onBackPressed() })
        binding.toolBar.crossBtn.setOnClickListener { onClose() }
    }

}