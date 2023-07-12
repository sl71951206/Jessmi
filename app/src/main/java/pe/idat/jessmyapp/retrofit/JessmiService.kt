package pe.idat.jessmyapp.retrofit
import pe.idat.jessmyapp.entities.Cliente
import pe.idat.jessmyapp.entities.Producto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface JessmiService {

    @GET("jessmi/producto/listar")
    fun getProductos(): Call<List<Producto>>


    @GET("jessmi/cliente/buscar/{correo}/{contrasena}")
    fun validarCredenciales(
        @Path("correo") correo: String,
        @Path("contrasena") contrasena: String
    ): Call<List<Cliente>>

    @POST("/jessmi/cliente/registrar")
    fun registrarCliente(@Body cliente: Cliente): Call<Void>
}