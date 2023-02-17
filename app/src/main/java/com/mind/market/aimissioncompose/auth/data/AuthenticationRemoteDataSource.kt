package com.mind.market.aimissioncompose.auth.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mind.market.aimissioncompose.auth.data.model.User

class AuthenticationRemoteDataSource : IAuthenticationRemoteDataSource {
    private val TAG = "AuthenticationRemoteDataSource"
    private lateinit var auth: FirebaseAuth
    override suspend fun createUser(email: String, password: String) {
        // TODO MIC make it better, create auth object more globally
        auth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "User successfully created. User is ${auth.currentUser}")
            } else {
                Log.e(TAG, "Unable to create new user. ${task.exception}")
            }
        }
    }

    override fun isUserAuthenticated(): Boolean {
        return auth.currentUser != null
    }

    override fun getUserData(token: String): User {
        return if (isUserAuthenticated()) {
            User(name = auth.currentUser?.displayName ?: "")
        } else {
            User.EMPTY
        }
    }

    override fun loginUser(email: String, password: String) {
        TODO("Not yet implemented")
    }


}