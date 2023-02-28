package com.mind.market.aimissioncompose.auth.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mind.market.aimissioncompose.auth.data.model.User

class AuthenticationRemoteDataSource(
    private val auth: FirebaseAuth
) : IAuthenticationRemoteDataSource {
    private val TAG = "AuthenticationRemoteDataSource"
    private var user: User? = null

    override suspend fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "User successfully created. User is ${auth.currentUser}")
            } else {
                Log.e(TAG, "Unable to create new user. ${task.exception}")
            }
        }
    }

    override suspend fun loginUser(
        email: String,
        password: String,
        onLoginResult: (User?, Throwable?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Login was successfully. User is ${auth.currentUser}")
                    user = auth.currentUser?.mapFirebaseUserToUser()
                    onLoginResult(user, null)
                } else {
                    Log.e(TAG, "Login was not successful. Check your credentials")
                    onLoginResult(
                        null,
                        Throwable("Login was not successful. Check your credentials")
                    )
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

    private fun FirebaseUser.mapFirebaseUserToUser(): User {
        return User(
            id = uid,
            name = displayName ?: "",
            email = email ?: "",
            tenantId = tenantId ?: ""
        )
    }
}