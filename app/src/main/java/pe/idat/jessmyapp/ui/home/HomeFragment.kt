package pe.idat.jessmyapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import pe.idat.jessmyapp.R
import pe.idat.jessmyapp.adapter.CarritoAdapter
import pe.idat.jessmyapp.adapter.ProductoAdapter
import pe.idat.jessmyapp.adapter.ProductosPopularesAdapter
import pe.idat.jessmyapp.entities.Producto
import pe.idat.jessmyapp.retrofit.JessmiAdapter
import pe.idat.jessmyapp.ui.viewmodel.ComunicacionViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var rvProductos: RecyclerView
    private lateinit var rvProductosNuevos: RecyclerView
    private lateinit var popularAdapter: ProductosPopularesAdapter
    private lateinit var popularAdapter2: ProductosPopularesAdapter
    private lateinit var viewModel: ComunicacionViewModel
    private val list = mutableListOf<CarouselItem>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel = ViewModelProvider(requireActivity())[ComunicacionViewModel::class.java]

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        rvProductos = view.findViewById(R.id.rvProductosPopulares)
        rvProductos.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

        val callStaff = JessmiAdapter.getApiService().getMasVendidos()
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
                    popularAdapter = ProductosPopularesAdapter(list,viewModel)
                    rvProductos.adapter = popularAdapter
                    popularAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<Producto>>, t: Throwable) {
                println("HAY UN ERROR JOVEN")
            }
        })

        rvProductosNuevos = view.findViewById(R.id.rvProductosNuevos)
        rvProductosNuevos.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        val callStaff2 = JessmiAdapter.getApiService().getMasNuevos()
        callStaff2.enqueue(object : Callback<List<Producto>> {
            override fun onResponse(call: Call<List<Producto>>, response: Response<List<Producto>>) {
                if (response.isSuccessful) {
                    val list = ArrayList(response.body())
                    for (producto in list) {
                        println("Nombre: ${producto.nombre}")
                        println("Marca: ${producto.marca}")
                        println("Precio: ${producto.precio}")
                        // Opcionalmente, puedes mostrar los resultados en un TextView o en otro elemento de la interfaz de usuario
                    }
                    popularAdapter2 = ProductosPopularesAdapter(list,viewModel)
                    rvProductosNuevos.adapter = popularAdapter2
                    popularAdapter2.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<Producto>>, t: Throwable) {
                println("HAY UN ERROR JOVEN")
            }
        })

        list.clear()

        val carousel1 :ImageCarousel=view.findViewById(R.id.carruselpublicidad1)

        list.add(CarouselItem("https://media.discordapp.net/attachments/1103761724219326606/1129863370808496148/descuento1.png?width=656&height=468"))
        list.add(CarouselItem("https://media.discordapp.net/attachments/1103761724219326606/1129874326515097610/descuento3.png?width=656&height=468"))
        list.add(CarouselItem("https://media.discordapp.net/attachments/1103761724219326606/1129874305543577731/descuento2.png?width=656&height=468"))
        list.add(CarouselItem("https://media.discordapp.net/attachments/1103761724219326606/1129875995265081474/descuento4.png?width=656&height=468"))
        carousel1.addData(list)


        return view
    }
}