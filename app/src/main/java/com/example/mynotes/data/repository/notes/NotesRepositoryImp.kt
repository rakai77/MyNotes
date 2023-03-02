package com.example.mynotes.data.repository.notes

import com.example.mynotes.data.Resource
import com.example.mynotes.data.remote.api.ApiService
import com.example.mynotes.data.remote.response.DetailNotesResponse
import com.example.mynotes.data.remote.response.ListNotesResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class NotesRepositoryImp @Inject constructor(private val apiService: ApiService) : NotesRepository{

    override fun getAllNotes(token: String): Flow<Resource<ListNotesResponse>> = flow {
        emit(Resource.Loading)
        try {
            val result = apiService.getListNotes(token)
            emit(Resource.Success(result))
        } catch (t: Throwable) {
            if (t is HttpException) {
                when (t.code()) {
                    400 -> emit(Resource.Error(t.message(), t.response()?.errorBody()))
                    401 -> emit(Resource.Error(t.message(), t.response()?.errorBody()))
                    404 -> emit(Resource.Error(t.message(), t.response()?.errorBody()))
                    422 -> emit(Resource.Error(t.message(), t.response()?.errorBody()))
                    500 -> emit(Resource.Error(t.message(), t.response()?.errorBody()))
                    else -> emit(Resource.Error(null, null))
                }
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage, null))
        }
    }

    override fun getDetailNotes(token: String, diaryId: String): Flow<Resource<DetailNotesResponse>> = flow {
        emit(Resource.Loading)
        try {
            val result = apiService.getDetailNotes(token, diaryId)
            emit(Resource.Success(result))
        } catch (t: Throwable) {
            if (t is HttpException) {
                when (t.code()) {
                    400 -> emit(Resource.Error(t.message(), t.response()?.errorBody()))
                    401 -> emit(Resource.Error(t.message(), t.response()?.errorBody()))
                    404 -> emit(Resource.Error(t.message(), t.response()?.errorBody()))
                    422 -> emit(Resource.Error(t.message(), t.response()?.errorBody()))
                    500 -> emit(Resource.Error(t.message(), t.response()?.errorBody()))
                    else -> emit(Resource.Error(null, null))
                }
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage, null))
        }
    }
}