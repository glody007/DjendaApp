package com.jjenda.reseau

import com.google.gson.annotations.SerializedName

data class Plan(@SerializedName("NOM") val type : String,
                @SerializedName("ADVANTAGE") val advantage : String,
                @SerializedName("PRICE") val price : String)