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


class HomeFragment : Fragment() {
    private lateinit var rvProductos: RecyclerView
    private lateinit var popularAdapter: ProductosPopularesAdapter
    private var productoList = ArrayList<Producto>()
    private val list = mutableListOf<CarouselItem>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        rvProductos = view.findViewById(R.id.rvProductosPopulares)
        rvProductos.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        productoList.clear()
        productoList.add(Producto(1,"Bolsa Cemento","SOL",30.0,"https://aaroncenter.com.pe/wp-content/uploads/2021/08/Cemento-SOL.jpg"))
        productoList.add(Producto(2,"Balde Pintura Gris","AMERICAN COLORS",50.00,"https://promart.vteximg.com.br/arquivos/ids/6641616-1000-1000/147499.jpg?v=638052608416100000"))
        productoList.add(Producto(3,"Taladro","BAUKER",200.0,"https://sodimac.scene7.com/is/image/SodimacPeru/8739749_00?wid=800&hei=800&qlt=70"))
        productoList.add(Producto(4,"Pegamento Interiores","CHEMA",200.0,"https://promart.vteximg.com.br/arquivos/ids/2984097-1000-1000/9902193.jpg?v=637727874603270000"))
        productoList.add(Producto(5,"Foco Led 4W","PHILIPS",8.0,"https://promart.vteximg.com.br/arquivos/ids/6394206-1000-1000/132826.jpg?v=637959391834630000"))
        productoList.add(Producto(6,"Tanque 1500L","Rotoplas",8.0,"https://promart.vteximg.com.br/arquivos/ids/7133655-1000-1000/128778.jpg?v=638228748341730000"))

        popularAdapter = ProductosPopularesAdapter(productoList)
        rvProductos.adapter = popularAdapter


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