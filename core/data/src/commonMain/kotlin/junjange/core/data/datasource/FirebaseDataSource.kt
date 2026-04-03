package junjange.core.data.datasource

interface FirebaseDataSource {
    suspend fun getToken(): Result<String>
}
