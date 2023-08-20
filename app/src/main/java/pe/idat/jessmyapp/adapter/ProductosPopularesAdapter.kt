package pe.idat.jessmyapp.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import pe.idat.jessmyapp.R
import pe.idat.jessmyapp.entities.Producto
import pe.idat.jessmyapp.ui.viewmodel.ComunicacionViewModel

class ProductosPopularesAdapter(private val productoList: ArrayList<Producto>, private val viewModel: ComunicacionViewModel)
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

        holder.imgProducto.setOnClickListener {
            val context = holder.itemView.context
            val dialogBuilder = AlertDialog.Builder(context)
            val dialogView = LayoutInflater.from(context).inflate(R.layout.detalle_producto_mas_dialog, null)
            dialogBuilder.setView(dialogView)

            val imageView = dialogView.findViewById<ImageView>(R.id.img_DetalleHome)
            val txtProducto = dialogView.findViewById<TextView>(R.id.txtDetalleProductoHome)
            val txtMarca = dialogView.findViewById<TextView>(R.id.txtDetalleMarcaHome)
            val txtPrecio = dialogView.findViewById<TextView>(R.id.txtDetallePrecioHome)

            Glide.with(context)
                .load(producto.foto)
                .centerCrop()
                .transform(RoundedCorners(16))
                .into(imageView)
            txtProducto.text = producto.nombre
            txtMarca.text = "MARCA: "+producto.marca
            txtPrecio.text = "PRECIO: S/"+producto.precio

            val dialog = dialogBuilder.create()
            dialog.show()

            val btnClose = dialogView.findViewById<Button>(R.id.btnDetalleCloseHome)
            btnClose.setOnClickListener {
                dialog.dismiss()
            }

            //

            val btnMinus = dialogView.findViewById<ImageButton>(R.id.btnMinusHome)
            val txtCantidad = dialogView.findViewById<TextView>(R.id.txtCantidadHome)
            val btnAdd = dialogView.findViewById<ImageButton>(R.id.btnAddHome)
            val btnAgregar = dialogView.findViewById<Button>(R.id.btnAgregarHome)

            btnMinus.setOnClickListener {
                var cantidad = txtCantidad.text.toString().toInt()
                if (cantidad > 1) {
                    cantidad--
                    txtCantidad.text = cantidad.toString()
                }
            }

            btnAdd.setOnClickListener {
                var cantidad = txtCantidad.text.toString().toInt()
                if (cantidad+1 < producto.stock) {
                    cantidad++
                    txtCantidad.text = cantidad.toString()
                }
            }

            // Agregar el clic del botón
            btnAgregar.setOnClickListener {
                var cantidad = txtCantidad.text.toString().toInt()
                val nombreprod = "${producto.nombre} - ${producto.marca}"
                toastAgregarItemCarrito(context,nombreprod)
                val producto = productoList[position]
                producto.cantidad = cantidad
                viewModel.agregarProducto(producto)
            }
        }
    }

    override fun getItemCount(): Int {
        return productoList.size
    }

    private fun toastAgregarItemCarrito(context: Context, nombreproducto:String) {
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.toast_agregar_item_catalogo, null)
        val txtMensaje:TextView=layout.findViewById(R.id.txtMensajeToastAgregarItemCarrito)
        txtMensaje.text= "Se agregó: $nombreproducto"
        val toast = Toast(context)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
    }
}