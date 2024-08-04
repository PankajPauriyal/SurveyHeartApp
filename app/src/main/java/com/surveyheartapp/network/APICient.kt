package com.verbio.verbioapp.Retrofit

import com.surveyheartapp.R
import com.surveyheartapp.application.SurveyApplication
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIClient {
    companion object {
        private val baseURL: String = SurveyApplication.getContext().getString(R.string.baseUrl)
        private var retrofit: Retrofit? = null

        val client: Retrofit
            get() {
                if (retrofit == null) {
                    // Create logging interceptor
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)

                    // Create OkHttpClient
                    val client = OkHttpClient.Builder()
                        .addInterceptor(logging)
                        .build()

                    // Initialize Retrofit
                    retrofit = Retrofit.Builder()
                        .baseUrl(baseURL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build()
                }
                return retrofit!!
            }
    }
}

