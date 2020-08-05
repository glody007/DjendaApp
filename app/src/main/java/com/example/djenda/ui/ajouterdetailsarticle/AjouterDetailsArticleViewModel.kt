package com.example.djenda.ui.ajouterdetailsarticle

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.djenda.reseau.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AjouterDetailsArticleViewModel : ViewModel() {


    var article : Article
    private var image : Bitmap?
    var repository : Repository = Repository.getInstance()
    lateinit var urlImage : String
    lateinit var fileName : String
    lateinit var publicKey: String

    private val _eventArticlePosted = MutableLiveData<Boolean>()
    val eventArticlePosted : LiveData<Boolean>
        get() = _eventArticlePosted

    private val _eventErrorWhenPostingArticle = MutableLiveData<Boolean>()
    val eventErrorWhenPostingArticle : LiveData<Boolean>
        get() = _eventErrorWhenPostingArticle

    init {
        _eventArticlePosted.value = false
        _eventErrorWhenPostingArticle.value = false
        image = BitmapFactory.decodeByteArray(repository.photArticle, 0, repository.photArticle.size)
        article = Article()
    }

    fun setLocation(longitude : String, latitude : String) {
        article.longitude = longitude
        article.latitude = latitude
    }

    fun postArticle() {
        repository.get_auth_for_image_upload(object : Callback<Auth?> {
            override fun onResponse(call: Call<Auth?>, response: Response<Auth?>) {
                response.body()?.apply {
                    postImageToImageKit(this)
                }
            }
            override fun onFailure(call: Call<Auth?>, t: Throwable) {
                Log.d("postImage", "postarticle")
                _eventErrorWhenPostingArticle.value = true
            }
        })

    }

    private fun postImageToImageKit(auth : Auth) {
        urlImage = "https://upload.wikimedia.org/wikipedia/en/a/a9/MarioNSMBUDeluxe.png"
        val imageKitDataToPost = ImageKitDataToPost(fileName, urlImage, auth, publicKey)
        repository.postPhoto(imageKitDataToPost, object : Callback<ImageKitResponse> {
            override fun onResponse(call: Call<ImageKitResponse>, response: Response<ImageKitResponse>) {
                response.body()?.apply {
                    postArticleToDjenda(this)
                }
            }

            override fun onFailure(call: Call<ImageKitResponse>, t: Throwable) {
                Log.d("postImage", "imagekit")
                _eventErrorWhenPostingArticle.value = true
            }
        })
    }

    private fun postArticleToDjenda(imageKitResponse: ImageKitResponse) {
        article.urlPhoto = imageKitResponse.url
        article.urlThumbnailPhoto = imageKitResponse.thumbnailUrl
        repository.createArticle(article, object : Callback<List<Article>> {
            override fun onResponse(call: Call<List<Article>>, response: Response<List<Article>>) {
                _eventArticlePosted.value = true
            }

            override fun onFailure(call: Call<List<Article>>, t: Throwable) {
                Log.d("postImage", "djenda")
                _eventErrorWhenPostingArticle.value = true
            }
        })
    }

    fun onErrorWhenPostingArticleFinished() {
        _eventErrorWhenPostingArticle.value = false
    }

}