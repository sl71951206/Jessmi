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
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import pe.idat.jessmyapp.R
import pe.idat.jessmyapp.entities.Producto

class ProductoAdapterGuest (private val productoList: ArrayList<Producto>)
    : RecyclerView.Adapter<ProductoAdapterGuest.ProductoViewHolder>() {

    class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProducto: ImageView = itemView.findViewById(R.id.imgProducto)
        val txtProducto: TextView = itemView.findViewById(R.id.txtProducto)
        val txtMarca: TextView = itemView.findViewById(R.id.txtMarca)
        val txtPrecio: TextView = itemView.findViewById(R.id.txtPrecio)
        val btnMinus: ImageButton =itemView.findViewById(R.id.btnMinus)
        val txtCantidad: TextView =itemView.findViewById(R.id.txtCantidad)
        val btnAdd: ImageButton =itemView.findViewById(R.id.btnAdd)
        val btnAgregar: Button =itemView.findViewById(R.id.btnAgregar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.producto_item, parent, false)
        return ProductoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productoList.size
    }


    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productoList[position]
        Glide.with(holder.itemView.context)
            .load(producto.foto)
            .into(holder.imgProducto)
        holder.txtProducto.text = producto.nombre
        holder.txtMarca.text = "MARCA: "+producto.marca
        holder.txtPrecio.text ="S/"+producto.precio.toString()

        holder.btnMinus.setOnClickListener {
            var cantidad = holder.txtCantidad.text.toString().toInt()
            if (cantidad > 1) {
                cantidad--
                holder.txtCantidad.text = cantidad.toString()
            }
        }

        holder.btnAdd.setOnClickListener {
            var cantidad = holder.txtCantidad.text.toString().toInt()
            if (cantidad+1 < producto.stock) {
                cantidad++
                holder.txtCantidad.text = cantidad.toString()
            }
        }

        holder.btnAgregar.setOnClickListener {
            guestMessage(holder.itemView.context)
        }

        holder.imgProducto.setOnClickListener {
            mostrarDialogo(producto, holder.itemView.context)
        }

    }

    private fun mostrarDialogo(producto: Producto, context: Context) {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.detalle_producto_dialog_layout, null)
        dialogBuilder.setView(dialogView)

        val imageView = dialogView.findViewById<ImageView>(R.id.img_Detalle)
        val txtProducto = dialogView.findViewById<TextView>(R.id.txtDetalleProducto)
        val txtMarca = dialogView.findViewById<TextView>(R.id.txtDetalleMarca)
        val txtStock = dialogView.findViewById<TextView>(R.id.txtDetalleStock)
        val txtPrecio = dialogView.findViewById<TextView>(R.id.txtDetallePrecio)

        Glide.with(context)
            .load(producto.foto)
            .into(imageView)
        txtProducto.text = producto.nombre
        txtMarca.text = "MARCA: "+producto.marca
        txtStock.text = "STOCK: "+producto.stock
        txtPrecio.text = "S/"+producto.precio.toString()

        val dialog = dialogBuilder.create()
        dialog.show()

        val btnClose = dialogView.findViewById<Button>(R.id.btnDetalleClose)
        btnClose.setOnClickListener {
            dialog.dismiss() // Cierra el cuadro de diálogo
        }
    }

    //Mensaje para el Usuario Invitado
    private fun guestMessage(context:Context){
        SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("¿QUIERE COMPRAR EN JESSMI?")
            .setContentText("Para Hacer Compras usted debe Iniciar Sesión.")
            .setConfirmButton("OK") { sweetAlertDialog ->
                sweetAlertDialog.dismissWithAnimation()
            }
            .show()
    }

}