package pe.idat.jessmyapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pe.idat.jessmyapp.R
import pe.idat.jessmyapp.entities.Producto

class CarritoAdapter(private val productoList: ArrayList<Producto>)
    : RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    class CarritoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProducto: ImageView = itemView.findViewById(R.id.imgProductoCarrito)
        val txtProducto: TextView = itemView.findViewById(R.id.txtProductoCarrito)
        val txtMarca: TextView = itemView.findViewById(R.id.txtMarcaCarrito)
        val txtPrecio: TextView = itemView.findViewById(R.id.txtPrecioCarrito)
        val btnBorrar: Button = itemView.findViewById(R.id.btnBorrarCarrito)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.producto_item_carrito, parent, false)
        return CarritoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productoList.size
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val producto = productoList[position]
        Glide.with(holder.itemView.context)
            .load(producto.foto)
            .into(holder.imgProducto)
        holder.txtProducto.text = producto.nombre
        holder.txtMarca.text = producto.marca
        holder.txtPrecio.text = "S/${producto.precio}"

        holder.btnBorrar.setOnClickListener {
            // Lógica para borrar el producto del carrito
        }
    }
}