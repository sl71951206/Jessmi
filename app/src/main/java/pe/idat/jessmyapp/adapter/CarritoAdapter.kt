package pe.idat.jessmyapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import pe.idat.jessmyapp.R
import pe.idat.jessmyapp.entities.Producto
import pe.idat.jessmyapp.ui.viewmodel.ComunicacionViewModel

class CarritoAdapter(private val productoList: ArrayList<Producto>, private val viewModel: ComunicacionViewModel)
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
            .centerCrop()
            .transform(RoundedCorners(16))
            .into(holder.imgProducto)
        holder.txtProducto.text = producto.nombre
        holder.txtMarca.text = producto.marca
        holder.txtPrecio.text = "S/${producto.precio}"

        holder.btnBorrar.setOnClickListener {
            val nombreproducto:String=producto.nombre+"-"+producto.marca
            viewModel.eliminarProducto(position)
            toastBorrarItemCarrito(holder.itemView.context,nombreproducto)
        }
    }

    private fun toastBorrarItemCarrito(context: Context,nombreproducto:String) {
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.toast_eliminar_item_carrito, null)
        val txtProductoEliminado:TextView=layout.findViewById(R.id.txtMensajeToastEliminarItemCarrito)
        txtProductoEliminado.text= "$nombreproducto ha sido eliminado del Carrito"
        val toast = Toast(context)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
    }
}