package junjange.core.remote.model.error

class InvalidAccessTokenException(e: Throwable?, val url: String? = null) : Exception(e)

class InvalidAccessTokenExpire(e: Throwable?, val url: String? = null) : Exception(e)

class ServerNotFoundException(e: Throwable?, val url: String? = null) : Exception(e)

class InternalServerErrorException(e: Throwable?, val url: String? = null) : Exception(e)

class BadRequestException(e: Throwable?, val url: String? = null) : Exception(e)
