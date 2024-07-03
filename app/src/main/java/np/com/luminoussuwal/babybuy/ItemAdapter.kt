package np.com.luminoussuwal.babybuy

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import np.com.luminoussuwal.babybuy.Dashboard.adapters.OffersHorizontalAdapter.ViewHolder
import np.com.luminoussuwal.babybuy.Dashboard.db.Product
import np.com.luminoussuwal.babybuy.databinding.ItemViewBinding

class ItemAdapter(private val items: List<Product>,
                  private val listener: ItemAdapterListener,
                  private val applicationContext: Context
)
    : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    inner class ViewHolder( val binding: ItemViewBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
           // binding.tvItemName.text = "newname"
            // Set the image resource or any other view properties if needed
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemViewBinding.
        inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }



    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int) {
        val product = items[position]

        holder.binding.tvItemName.text = product.name
        holder.binding.tvItemPrice.text = "NPR " + product.price
        holder.binding.tvItemDescription.text = product.description
        holder.binding.tvItemCategory.text = product.category

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
            val drawable = ContextCompat.getDrawable(applicationContext, R.mipmap.blue_check1)
            holder.binding.tvItemName.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
        } else {
            holder.binding.tvItemName.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        }
       // holder.binding.tvTimestamp.text = product.timeStamp
    }
    override fun getItemCount(): Int {
        return items.size
    }

    interface ItemAdapterListener {
        fun onItemClicked(product: Product, position: Int)
    }
}