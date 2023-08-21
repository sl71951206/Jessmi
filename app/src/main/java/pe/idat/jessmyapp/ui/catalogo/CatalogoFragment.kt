package pe.idat.jessmyapp.ui.catalogo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import pe.idat.jessmyapp.R
import pe.idat.jessmyapp.adapter.ProductoAdapter
import pe.idat.jessmyapp.entities.Producto
import pe.idat.jessmyapp.retrofit.JessmiAdapter
import pe.idat.jessmyapp.ui.carrito.CarritoFragment
import pe.idat.jessmyapp.ui.viewmodel.ComunicacionViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatalogoFragment : Fragment() {
    private lateinit var rvProductos: RecyclerView
    private lateinit var imgCatalogoVacio: ImageView
    private lateinit var productoAdapter: ProductoAdapter
    private lateinit var viewModel: ComunicacionViewModel
    private lateinit var svBusqueda: SearchView
    private lateinit var srlCatalogo: SwipeRefreshLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_catalogo, container, false)
        rvProductos = view.findViewById(R.id.rv_Catalogue)
        imgCatalogoVacio = view.findViewById(R.id.imgCatalogoVacio)
        rvProductos.layoutManager = LinearLayoutManager(context)
        // Obtener referencia al ComunicacionViewModel
        viewModel = ViewModelProvider(requireActivity())[ComunicacionViewModel::class.java]

        svBusqueda = view.findViewById(R.id.svBusqueda)
        svBusqueda.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                val callStaff = JessmiAdapter.getApiService().buscarPorNombreOMarca(query)
                callStaff.enqueue(object : Callback<List<Producto>> {
                    override fun onResponse(call: Call<List<Producto>>, response: Response<List<Producto>>) {
                        if (response.isSuccessful) {
                            if (response.body() != null) {
                                val list = ArrayList(response.body())
                                for (producto in list) {
                                    println("Nombre: ${producto.nombre}")
                                    println("Marca: ${producto.marca}")
                                    println("Precio: ${producto.precio}")
                                    // Opcionalmente, puedes mostrar los resultados en un TextView o en otro elemento de la interfaz de usuario
                                    imgCatalogoVacio.visibility = View.GONE
                                    rvProductos.visibility = View.VISIBLE
                                    productoAdapter = ProductoAdapter(list,viewModel)
                                    rvProductos.adapter = productoAdapter
                                    productoAdapter.notifyDataSetChanged()
                                }
                            } else {
                                rvProductos.visibility = View.GONE
                                imgCatalogoVacio.visibility = View.VISIBLE
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
                    imgCatalogoVacio.visibility = View.GONE
                    rvProductos.visibility = View.VISIBLE
                    cargarProductos()
                }
                return false
            }
        })

        srlCatalogo = view.findViewById(R.id.srlCatalogo)
        srlCatalogo.setOnRefreshListener {
            if (svBusqueda.query.toString() != "") {
                svBusqueda.setQuery("", false)
            } else {
                cargarProductos()
            }
            srlCatalogo.isRefreshing = false
        }

        cargarProductos()
        //retornar vista
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun cargarProductos() {
        val callStaff = JessmiAdapter.getApiService().listarConCondicion()
        callStaff.enqueue(object : Callback<List<Producto>> {
            override fun onResponse(call: Call<List<Producto>>, response: Response<List<Producto>>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        productoAdapter = ProductoAdapter(ArrayList(response.body()),viewModel)
                        rvProductos.adapter = productoAdapter
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
