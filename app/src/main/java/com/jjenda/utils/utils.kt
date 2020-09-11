package com.jjenda.utils

import android.app.Activity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task

fun signOut(activity : Activity): Task<Void> {
    // Configure sign-in to request the user's ID, email address, and basic
    // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

    // Build a GoogleSignInClient with the options specified by gso.
    val googleSignInClient : GoogleSignInClient = GoogleSignIn.getClient(activity, gso)

    return googleSignInClient.signOut()
}