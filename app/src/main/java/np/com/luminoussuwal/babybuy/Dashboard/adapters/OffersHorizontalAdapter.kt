package np.com.luminoussuwal.babybuy.Dashboard.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import np.com.luminoussuwal.babybuy.Dashboard.db.Product
import np.com.luminoussuwal.babybuy.R
import np.com.luminoussuwal.babybuy.databinding.LayoutItemProductExpandedBinding

class OffersHorizontalAdapter(val products: List<Product>)
    : RecyclerView.Adapter<OffersHorizontalAdapter.ViewHolder>(){

    class ViewHolder(val binding: LayoutItemProductExpandedBinding
    ): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LayoutItemProductExpandedBinding.inflate(layoutInflater)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int) {
        val product = products[position]

        holder.binding.tvItemTitle.text = product.name
        holder.binding.tvTimestamp.text ="NPR " + product.price
        holder.binding.tvItemDescription.text = product.description
        Glide.with(holder.itemView.context)
            .load(product.image)
            .apply(RequestOptions().centerCrop())
            .placeholder(R.mipmap.img_holder) // Set default image while loading
            .error(R.mipmap.img_holder) // Set default image on error
            .into(holder.binding.ivItemImage) // Set image to ImageView


        // holder.binding.tvTimestamp.text = product.timeStamp
    }

    }

    interface ItemAdapterListener {
        fun onItemClicked(product: Product, position: Int)
    }
