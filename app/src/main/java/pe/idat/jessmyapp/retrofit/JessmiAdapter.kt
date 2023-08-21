package pe.idat.jessmyapp.retrofit
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor

object JessmiAdapter {

    private var API_SERVICE: JessmiService? = null
    private const val BASE_URL = "https://b4c3-2001-1388-540-e872-e4b1-c7ac-8bbe-c65a.ngrok-free.app"
    private const val USERNAME = "verde"
    private const val PASSWORD = "123"

    fun getApiService(): JessmiService {

        val authInterceptor = Interceptor { chain ->
            val credentials = Credentials.basic(USERNAME, PASSWORD)
            val request = chain.request().newBuilder()
                .header("Authorization", credentials)
                .build()
            chain.proceed(request)
        }
        /*
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        */
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(authInterceptor)

        if (API_SERVICE == null) {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
            API_SERVICE = retrofit.create(JessmiService::class.java)
        }
        return API_SERVICE!!
    }
}