package com.jjenda.ui.ajouterdetailsarticle

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jjenda.reseau.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AjouterDetailsArticleViewModel : ViewModel() {


    var article : Article
    private lateinit var image : Bitmap
    var repository : Repository = Repository.getInstance()
    lateinit var urlImage : String
    lateinit var fileName : String
    lateinit var publicKey: String
    var articlePrixString: String = ""
        set(value) {
            field = value
            if(field.isBlank()) article.prix = 0
            else article.prix = value.toInt()
        }
    var nombrePostsRestant = 0

    private val _btnEnabled = MutableLiveData<Boolean>()
    val btnEnabled : LiveData<Boolean>
        get() = _btnEnabled

    private val _eventArticlePosted = MutableLiveData<Boolean>()
    val eventArticlePosted : LiveData<Boolean>
        get() = _eventArticlePosted

    private val _eventErrorWhenPostingArticle = MutableLiveData<Boolean>()
    val eventErrorWhenPostingArticle : LiveData<Boolean>
        get() = _eventErrorWhenPostingArticle

    private val _eventShowPostRestantDialog = MutableLiveData<Boolean>()
    val eventshowPostRestantDialog : LiveData<Boolean>
        get() = _eventShowPostRestantDialog

    init {
        _btnEnabled.value = false
        _eventArticlePosted.value = false
        _eventErrorWhenPostingArticle.value = false
        _eventShowPostRestantDialog.value = false
        repository.photArticle?.let {
            image = BitmapFactory.decodeByteArray(it, 0, it.size)
        }
        article = Article()
    }

    fun onTextChanged() { _btnEnabled.value = enabled() }

    fun enabled() : Boolean {
        return (article.description.isNotBlank() and
                article.categorie.isNotBlank() and
                articlePrixString.isNotBlank())
    }

    fun setLocation(longitude : String, latitude : String) {
        article.longitude = longitude
        article.latitude = latitude
    }

    fun preparePostArticle() {
        nombrePostsRestant = 5
        if(nombrePostsRestant <= MINIMUM_FOR_ALERT) { _eventShowPostRestantDialog.value = true }
        else { postArticle() }
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

    fun onArticlePostedFinished() {
        _eventArticlePosted.value = false
    }

    fun onShowPostRestantDialogFinished() {
        _eventShowPostRestantDialog.value = false
    }

    companion object {
        val MINIMUM_FOR_ALERT = 10
    }
}