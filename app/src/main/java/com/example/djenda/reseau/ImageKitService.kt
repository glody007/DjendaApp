package com.example.djenda.reseau

import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface ImageKitService {
    @Multipart
    @POST("files/upload")
    fun postPhoto(
            @Part("signature") signature: RequestBody,
            @Part("expire") expire: RequestBody,
            @Part("token") token: RequestBody,
            @Part("file") file: RequestBody,
            @Part("publicKey") publicKey: RequestBody,
            @Part("fileName") fileName: RequestBody
    ): Call<ImageKitResponse>
}