package com.example.mynotes.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.mynotes.BuildConfig
import com.example.mynotes.data.remote.api.ApiService
import com.example.mynotes.data.repository.auth.AuthRepository
import com.example.mynotes.data.repository.auth.AuthRepositoryImp
import com.example.mynotes.data.repository.notes.NotesRepository
import com.example.mynotes.data.repository.notes.NotesRepositoryImp
import com.example.mynotes.utils.Preference
import com.example.mynotes.utils.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun httpLoggingInterceptor() : HttpLoggingInterceptor {
        return if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor) : OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory() : GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideRetrofitBuilder(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ) : Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://diary-test.ifdenewhallaid.com")
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit) : ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideAuthRepository(apiService: ApiService) : AuthRepository {
        return AuthRepositoryImp(apiService)
    }

    @Singleton
    @Provides
    fun provideNotesRepository(apiService: ApiService) : NotesRepository {
        return NotesRepositoryImp(apiService)
    }

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context) : DataStore<Preferences> =
        context.dataStore

    @Singleton
    @Provides
    fun providePreference(dataStore: DataStore<Preferences>) : Preference = Preference(dataStore)
}