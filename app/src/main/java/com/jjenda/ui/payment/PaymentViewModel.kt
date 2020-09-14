package com.jjenda.ui.payment

import androidx.lifecycle.ViewModel
import com.jjenda.ui.avertissementpostrestant.PostRestantDialog

class PaymentViewModel : ViewModel() {
    lateinit var listener : PaymentDialog.PaymentDialogListener
}