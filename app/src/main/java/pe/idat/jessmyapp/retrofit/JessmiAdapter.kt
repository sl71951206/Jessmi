package pe.idat.jessmyapp.retrofit
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor

object JessmiAdapter {

    private var API_SERVICE: JessmiService? = null
    private const val BASE_URL = "http://192.168.18.8:8090/"

    fun getApiService(): JessmiService {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

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