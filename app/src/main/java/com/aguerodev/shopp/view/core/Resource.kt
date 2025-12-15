package com.aguerodev.shopp.view.core

/**
 * Clase genérica sellada para manejar el estado y los datos de las operaciones asíncronas.
 * @param T El tipo de dato que se espera en el estado Success.
 */
sealed class Resource<T>(val data: T? = null, val message: String? = null) {

    class Idle<T> : Resource<T>()
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}