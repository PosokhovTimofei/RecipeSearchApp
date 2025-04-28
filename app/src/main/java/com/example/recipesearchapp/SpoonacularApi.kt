package com.example.recipesearchapp

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

object SpoonacularClient {
    private const val BASE_URL = "https://api.spoonacular.com/"

    val api: SpoonacularApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(createOkHttpClient())
            .build()
            .create(SpoonacularApi::class.java)
    }

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()
    }
}

interface SpoonacularApi {
    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("query") query: String,
        @Query("apiKey") apiKey: String = "f5341e440ba84573a6c3d76a69175021"
    ): RecipeResponse

    @GET("recipes/complexSearch")
    suspend fun getRecipesByCategory(
        @Query("type") type: String,
        @Query("apiKey") apiKey: String = "f5341e440ba84573a6c3d76a69175021"
    ): RecipeResponse

    // 🔥 Поиск внутри категории
    @GET("recipes/complexSearch")
    suspend fun searchRecipesInCategory(
        @Query("type") type: String,
        @Query("query") query: String,
        @Query("apiKey") apiKey: String = "f5341e440ba84573a6c3d76a69175021"
    ): RecipeResponse
}

