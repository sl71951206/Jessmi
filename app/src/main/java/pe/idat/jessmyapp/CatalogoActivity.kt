package pe.idat.jessmyapp

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pe.idat.jessmyapp.adapter.ProductoAdapter
import retrofit2.Response
import retrofit2.Callback
import pe.idat.jessmyapp.adapter.ProductoAdapterGuest
import pe.idat.jessmyapp.entities.Producto
import pe.idat.jessmyapp.retrofit.JessmiAdapter
import retrofit2.Call

class CatalogoActivity : AppCompatActivity() {
    private lateinit var rvProducto: RecyclerView
    private lateinit var productoAdapter: ProductoAdapterGuest
    private lateinit var svBusquedaGuest: SearchView
    private lateinit var imgCatalogoVacioGuest: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogo)

        //Bloquear la Activity en Modo Vertical
        requestedOrientation= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        rvProducto = findViewById(R.id.rv_Catalogo)
        imgCatalogoVacioGuest = findViewById(R.id.imgCatalogoVacioGuest)
        rvProducto.layoutManager = LinearLayoutManager(this)

        svBusquedaGuest = findViewById(R.id.svBusquedaGuest)
        svBusquedaGuest.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                val callStaff = JessmiAdapter.getApiService().buscarPorNombreOMarca(query)
                callStaff.enqueue(object : Callback<List<Producto>> {
                    override fun onResponse(call: Call<List<Producto>>, response: Response<List<Producto>>) {
                        if (response.isSuccessful) {
                            if (response.body() != null) {
                                val list = ArrayList(response.body())
                                for (producto in list) {
                                    imgCatalogoVacioGuest.visibility = View.GONE
                                    rvProducto.visibility = View.VISIBLE
                                    productoAdapter = ProductoAdapterGuest(list)
                                    rvProducto.adapter = productoAdapter
                                    productoAdapter.notifyDataSetChanged()
                                }
                            } else {
                                rvProducto.visibility = View.GONE
                                imgCatalogoVacioGuest.visibility = View.VISIBLE
                            }
                        }
                    }

                    override fun onFailure(call: Call<List<Producto>>, t: Throwable) {
                        println("HAY UN ERROR JOVEN")
                    }
                })
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText == null || newText == "") {
                    imgCatalogoVacioGuest.visibility = View.GONE
                    rvProducto.visibility = View.VISIBLE
                    cargarProductos()
                }
                return false
            }
        })

        cargarProductos()
    }

    fun cargarProductos() {
        val callStaff = JessmiAdapter.getApiService().getProductos()
        callStaff.enqueue(object : Callback<List<Producto>> {
            override fun onResponse(call: Call<List<Producto>>, response: Response<List<Producto>>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        productoAdapter = ProductoAdapterGuest(ArrayList(response.body()))
                        rvProducto.adapter = productoAdapter
                        productoAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<List<Producto>>, t: Throwable) {
                println("HAY UN ERROR JOVEN")
            }
        })
    }
}