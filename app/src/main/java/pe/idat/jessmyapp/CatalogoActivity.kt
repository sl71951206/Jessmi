package pe.idat.jessmyapp

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Response
import retrofit2.Callback
import pe.idat.jessmyapp.adapter.ProductoAdapterGuest
import pe.idat.jessmyapp.entities.Producto
import pe.idat.jessmyapp.retrofit.JessmiAdapter
import retrofit2.Call


class CatalogoActivity : AppCompatActivity() {
    private lateinit var rvProducto: RecyclerView
    private lateinit var productoAdapter: ProductoAdapterGuest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogo)

        //Bloquear la Activity en Modo Vertical
        requestedOrientation= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        rvProducto = findViewById(R.id.rv_Catalogo)
        rvProducto.layoutManager = LinearLayoutManager(this)

        val callStaff = JessmiAdapter.getApiService().getProductos()
        callStaff.enqueue(object : Callback<List<Producto>> {
            override fun onResponse(call: Call<List<Producto>>, response: Response<List<Producto>>) {
                if (response.isSuccessful) {
                    val list = ArrayList(response.body())
                    for (producto in list) {
                        println("Nombre: ${producto.nombre}")
                        println("Marca: ${producto.marca}")
                        println("Precio: ${producto.precio}")
                        // Opcionalmente, puedes mostrar los resultados en un TextView o en otro elemento de la interfaz de usuario
                    }
                    productoAdapter = ProductoAdapterGuest(list)
                    rvProducto.adapter = productoAdapter
                    productoAdapter.notifyDataSetChanged() // Agrega esta línea
                }
            }

            override fun onFailure(call: Call<List<Producto>>, t: Throwable) {
                println("HAY UN ERROR JOVEN")
            }
        })
    }
}