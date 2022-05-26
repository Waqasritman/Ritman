package angoothape.wallet.credopay.paymentsdk

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import angoothape.wallet.R
import angoothape.wallet.base.RitmanBaseActivity
import angoothape.wallet.databinding.ActivityCredoPayTransactionCompletedBinding

class CredoPayTransactionCompletedActivity : RitmanBaseActivity<ActivityCredoPayTransactionCompletedBinding>() {


    override fun getLayoutId(): Int {
        return R.layout.activity_credo_pay_transaction_completed
    }

    override fun initUi(savedInstanceState: Bundle?) {
        if (intent != null) {
            if (intent.getStringExtra("transaction_id") != null) {
                binding.transactionNo.text = intent.getStringExtra("transaction_id")
            }

            if (intent.getStringExtra("masked_pan") != null) {
                binding.cardNumber.text = intent.getStringExtra("masked_pan")
            }

            if (intent.getStringExtra("tsi") != null) {
                binding.transactionStatus.text = intent.getStringExtra("tsi")
            }

            if (intent.getStringExtra("rrn") != null) {
                binding.receRegiNo.text = intent.getStringExtra("rrn")
            }

            if (intent.getStringExtra("approval_code") != null) {
                binding.approvalCode.text = intent.getStringExtra("approval_code")
            }

            if (intent.getStringExtra("card_application_name") != null) {
                binding.nameOfCard.text = intent.getStringExtra("card_application_name")
            }

            if (intent.getStringExtra("card_holder_name") != null) {
                binding.cardHolderName.text = intent.getStringExtra("card_holder_name")
            }

            if (intent.getStringExtra("card_type") != null) {
                binding.cardType.text = intent.getStringExtra("card_type")
            }

            if (intent.getStringExtra("account_balance") != null) {
                binding.balanceEnquiry.text = intent.getStringExtra("account_balance")
            }

            if (intent.getStringExtra("transaction_type") != null) {
                binding.transType.text = intent.getStringExtra("transaction_type")
            }

            binding.toolBar.titleTxt.setText(binding.transType.text)
        }

        binding.toolBar.backBtn
                .setColorFilter(ContextCompat.getColor(this,
                        R.color.colorWhite), PorterDuff.Mode.SRC_IN)
        binding.toolBar.toolBar.setBackgroundColor(resources.getColor(R.color.posRed))

        binding.toolBar.backBtn.setOnClickListener(View.OnClickListener { v: View? -> finish() })
        binding.toolBar.crossBtn.setOnClickListener { finish() }

    }
}