package pe.idat.jessmyapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import pe.idat.jessmyapp.R
import pe.idat.jessmyapp.entities.Producto

class ProductosPopularesAdapter(private val productoList: ArrayList<Producto>)
    :RecyclerView.Adapter<ProductosPopularesAdapter.ProductosPopularesViewHolder>() {

    class ProductosPopularesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProducto: ImageView = itemView.findViewById(R.id.img_prod_popular)
        val txtProducto: TextView = itemView.findViewById(R.id.txt_nombre_prod_popular)
        val txtMarcaProduct: TextView = itemView.findViewById(R.id.txt_marca_prod_popular)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ProductosPopularesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.maspopulares_item, parent, false)
        return ProductosPopularesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductosPopularesViewHolder, position: Int) {
        val producto = productoList[position]
        Glide.with(holder.itemView.context)
            .load(producto.foto)
            .centerCrop()
            .transform(RoundedCorners(16))
            .into(holder.imgProducto)
        holder.txtProducto.text = "${producto.nombre}"
        holder.txtMarcaProduct.text = "${producto.marca}"
    }

    override fun getItemCount(): Int {
        return productoList.size
    }


}