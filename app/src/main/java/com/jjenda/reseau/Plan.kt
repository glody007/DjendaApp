package com.jjenda.reseau

import com.google.gson.annotations.SerializedName

data class Plan(@SerializedName("type") val type : String, @SerializedName("advantage") val advantage : String)