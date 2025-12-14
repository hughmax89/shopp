package com.aguerodev.shopp.data.repository

import com.aguerodev.shopp.domain.entity.User
import com.aguerodev.shopp.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class AuthRepositoryImplementation @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun loginUser(user: User): String {

        try {
            val result = firebaseAuth.signInWithEmailAndPassword(user.email, user.password)
                .await()

            return result.user?.uid ?: throw IllegalStateException("UID nulo.")

        } catch (e: Exception) {

            throw e
        }
    }
}