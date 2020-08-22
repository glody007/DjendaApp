package com.example.djenda

import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.djenda.reseau.Article
import com.facebook.drawee.view.SimpleDraweeView
import com.google.android.material.textfield.TextInputEditText

@BindingAdapter("articlePrix")
fun TextView.setArticlePrixString(article : Article?) {
    article?.let {
        val prix : Int = article.prix
        text = prix.toString()
    }
}

@BindingAdapter("articleThumbnailImage")
fun SimpleDraweeView.setArticleThumbnailImage(article : Article?) {
    article?.let {
        val uri = Uri.parse(article.urlThumbnailPhoto)
        setImageURI(uri)
    }
}

@BindingAdapter("articleImage")
fun SimpleDraweeView.setArticleImage(article : Article?) {
    article?.let {
        val uri = Uri.parse(article.urlPhoto)
        setImageURI(uri)
    }
}

@BindingAdapter("onTextChange")
fun EditText.onTextChange(callBack : () -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
           callBack()
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }
    })
}

fun TextInputEditText.onTextChange(callBack : () -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            callBack()
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }
    })
}