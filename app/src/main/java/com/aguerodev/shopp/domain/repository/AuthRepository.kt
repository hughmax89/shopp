package com.aguerodev.shopp.domain.repository

import com.aguerodev.shopp.domain.entity.User

interface AuthRepository {

    suspend fun loginUser(user: User): String
}