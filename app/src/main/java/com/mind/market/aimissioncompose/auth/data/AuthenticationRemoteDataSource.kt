package com.mind.market.aimissioncompose.auth.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mind.market.aimissioncompose.auth.data.model.User

class AuthenticationRemoteDataSource(
    private val auth: FirebaseAuth
) : IAuthenticationRemoteDataSource {
    private var user: User? = null

    override suspend fun createUser(
        email: String,
        password: String,
        onSignUpResult: (Throwable?, User?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                user = auth.currentUser?.mapFirebaseUserToUser()
                onSignUpResult(null, user)
            } else {
                onSignUpResult(Throwable(message = task.exception?.message), null)
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
                    user = auth.currentUser?.mapFirebaseUserToUser()
                    onLoginResult(user, null)
                } else {
                    onLoginResult(
                        null,
                        Throwable("Login was not successful. Check your credentials")
                    )
                }
            }
    }

    override suspend fun logoutUser(onUserLoggedOut: () -> Unit) {
        auth.signOut()
        onUserLoggedOut()
    }

    override fun isUserAuthenticated(): Boolean {
        return auth.currentUser != null
    }

    override fun getUserData(): User {
        return if (isUserAuthenticated()) {
            auth.currentUser?.let { user ->
                User(
                    id = user.uid,
                    email = user.email ?: "",
                    name = user.displayName ?: ""
                )
            } ?: User.EMPTY

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