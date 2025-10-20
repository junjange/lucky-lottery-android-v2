package junjange.core.domain.repository

interface FirebaseRepository {
    suspend fun getToken(): Result<String>
}
