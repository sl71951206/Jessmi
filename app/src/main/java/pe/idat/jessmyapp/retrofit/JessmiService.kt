package pe.idat.jessmyapp.retrofit
import pe.idat.jessmyapp.entities.Cliente
import pe.idat.jessmyapp.entities.DetalleCompraMapper
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
    ): Call<Cliente>

    @POST("/jessmi/cliente/registrar")
    fun registrarCliente(@Body cliente: Cliente): Call<Void>

    //Registrar pedido - Registrar Compra
    @POST("/jessmi/compra/registrarByProductos/{idCliente}")
    fun registrarPedido(@Path("idCliente") idCliente: Int ,@Body productos:List<Producto>): Call<Void>

    //Historial de Pedidos del Cliente
    @GET("jessmi/detalle_compra/DetalleConTotal/{idCliente}")
    fun historialPedidos(@Path("idCliente") idCliente: Int): Call<List<DetalleCompraMapper>>



}