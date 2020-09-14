package com.jjenda.ui.avertissementpostrestant

import androidx.lifecycle.ViewModel

class PostRestantViewModel : ViewModel() {

    var nombrePostsRestant = 0
    lateinit var listener : PostRestantDialog.PostRestantDialogListener

    fun canPost() : Boolean { return nombrePostsRestant > 0}

}