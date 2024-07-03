package np.com.luminoussuwal.babybuy.Dashboard.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import np.com.luminoussuwal.babybuy.Dashboard.db.Product
import np.com.luminoussuwal.babybuy.R
import np.com.luminoussuwal.babybuy.databinding.ItemViewBinding

class OffersHorizontalAdapter(val products: List<Product>,
                              private val listener: ItemAdapterListener,
                              private val applicationContext: Context
)
    : RecyclerView.Adapter<OffersHorizontalAdapter.ViewHolder>(){

    class ViewHolder(val binding: ItemViewBinding
    ): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemViewBinding.inflate(layoutInflater)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int) {
        val product = products[position]
        holder.binding.tvItemCategory.text = product.category
        holder.binding.tvItemName.text = product.name
        holder.binding.tvItemPrice.text ="NPR " + product.price
        holder.binding.tvItemDescription.text = product.description
        Glide.with(holder.itemView.context)
            .load(product.image)
            .apply(RequestOptions().centerCrop())
            .placeholder(R.mipmap.img_holder) // Set default image while loading
            .error(R.mipmap.img_holder) // Set default image on error
            .into(holder.binding.ivItem) // Set image to ImageView

        holder.binding.itemRootLayout.setOnClickListener {
            listener.onItemClicked(product, position)
        }
        if (product.markAsPurchased) {
            val drawable = ContextCompat.getDrawable(applicationContext, R.drawable.ic_check_circle)
            holder.binding.tvItemName.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
        } else {
            holder.binding.tvItemName.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        }
        // holder.binding.tvTimestamp.text = product.timeStamp
    }

    interface ItemAdapterListener {
        fun onItemClicked(product: Product, position: Int)
    }

    }


