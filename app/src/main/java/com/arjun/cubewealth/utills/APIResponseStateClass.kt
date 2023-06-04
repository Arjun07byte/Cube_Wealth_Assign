package com.arjun.cubewealth.utills

sealed class APIResponseStateClass<T>(
    val successResponseData: T? = null,
    val responseMessage: String? = null
) {
    class SuccessResponseClass<T>(successResponseData: T) :
        APIResponseStateClass<T>(successResponseData)

    class ErrorResponseClass<T>(responseErrorMessage: String) :
        APIResponseStateClass<T>(null, responseErrorMessage)

    class LoadingResponseClass<T>() : APIResponseStateClass<T>()
}
