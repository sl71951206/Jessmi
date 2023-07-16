package pe.idat.jessmyapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import pe.idat.jessmyapp.R
import pe.idat.jessmyapp.databinding.FragmentHomeBinding



class HomeFragment : Fragment() {
    private val list = mutableListOf<CarouselItem>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

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