package pe.idat.jessmyapp.retrofit
import pe.idat.jessmyapp.entities.Cliente
import pe.idat.jessmyapp.entities.CompraMapper
import pe.idat.jessmyapp.entities.DetalleCompra
import pe.idat.jessmyapp.entities.Producto
import pe.idat.jessmyapp.entities.ProductoMapper
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface JessmiService {

    @GET("jessmi/producto/listar")
    fun getProductos(): Call<List<Producto>>


    @POST("jessmi/cliente/buscarPorCorreoRetrofit")
    fun validarCredenciales(
        @Body cliente: Cliente
    ): Call<Cliente>

    @PUT("/jessmi/cliente/eliminarCuenta")
    fun eliminarCuenta(@Body cliente: Cliente): Call<Cliente>

    @GET("/jessmi/cliente/buscarPorCorreo/{correo}")
    fun buscarClientePorCorreo(@Path("correo") correo: String): Call<Cliente>

    @POST("/jessmi/cliente/registrar")
    fun registrarCliente(@Body cliente: Cliente): Call<Void>

    @GET("jessmi/producto/masVendidos")
    fun getMasVendidos(): Call<List<Producto>>

    @GET("jessmi/producto/listarConCondicion")
    fun listarConCondicion(): Call<List<Producto>>

    @GET("jessmi/producto/buscarPorNombreOMarca/{x}")
    fun buscarPorNombreOMarca(@Path("x") x: String): Call<List<Producto>>

    @GET("jessmi/producto/masNuevos")
    fun getMasNuevos(): Call<List<Producto>>

    @GET("/jessmi/compra/buscarUltimoPorIdCliente/{idCliente}")
    fun buscarUltimoPorIdCliente(@Path("idCliente") idCliente: Int): Call<CompraMapper>

    //Registrar pedido - Registrar Compra
    @POST("/jessmi/compra/registrarByComponents/{idCliente}")
    fun registrarPedido(@Path("idCliente") idCliente: Int ,@Body productos:List<ProductoMapper>): Call<Void>

    //Historial de Pedidos del Cliente
    @GET("jessmi/compra/buscarTotalPorIdCliente/{idCliente}")
    fun historialPedidos(@Path("idCliente") idCliente: Int): Call<List<CompraMapper>>

    @GET("jessmi/detalle_compra/buscarPorCodCompra/{codCompra}")
    fun buscarDetalleCompraPorCodCompra(@Path("codCompra") codCompra: Int): Call<List<DetalleCompra>>

}