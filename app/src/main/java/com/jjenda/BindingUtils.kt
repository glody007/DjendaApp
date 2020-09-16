package com.jjenda

import android.location.Location
import android.net.Uri
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import com.jjenda.reseau.Article
import com.facebook.drawee.view.SimpleDraweeView
import com.google.android.material.textfield.TextInputEditText
import com.jjenda.utils.ElapsedTime
import com.jjenda.utils.LocationDistance

@BindingAdapter("articlePrix")
fun TextView.setArticlePrixString(article : Article?) {
    article?.let {
        val prix : Int = article.prix
        text = "$prix$"
    }
}

@BindingAdapter("alertPostsRestant")
fun TextView.setAlertPostRestant(nombrePostRestant : Int) {
    text = "Il vous reste $nombrePostRestant article(s) que vous pouvez poster gratuitement ce mois"
}

@RequiresApi(Build.VERSION_CODES.O)
@BindingAdapter("elapsedTimeFromPost")
fun TextView.setElapsedTimeFromPost(article: Article?) {
    article?.let {
        text = if(it.createdAt == null) ElapsedTime().elapsedTimeString()
        else ElapsedTime(pattern = "yyyy-MM-dd HH:mm:ss.SSS", stringDate = it.createdAt).elapsedTimeString()
    }
}

@BindingAdapter("article", "myLocation")
fun TextView.TimeToArticle(article: Article?, myLocation: com.jjenda.reseau.Location) {
    article?.let {
        val articleLocation = Location("")
        articleLocation.longitude = article.location[0]
        articleLocation.latitude = article.location[1]
        val locationB = Location("")
        locationB.longitude = myLocation.longitude
        locationB.latitude = myLocation.latitude
        text = LocationDistance(articleLocation, locationB).timeFromAToBWithBestMeansOfTransportString()
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