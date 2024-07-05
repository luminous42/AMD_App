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

// Adapter for displaying a horizontal list of product offers in a RecyclerView
class OffersHorizontalAdapter(
    val products: List<Product>,  // List of products to display
    private val listener: ItemAdapterListener,  // Listener for item interactions
    private val applicationContext: Context  // Application context
) : RecyclerView.Adapter<OffersHorizontalAdapter.ViewHolder>() {

    // ViewHolder class that holds the view for each item
    class ViewHolder(
        val binding: ItemViewBinding
    ) : RecyclerView.ViewHolder(binding.root)

    // Inflate the item layout and create the ViewHolder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemViewBinding.inflate(layoutInflater)
        return ViewHolder(binding)
    }

    // Return the total number of items in the list
    override fun getItemCount(): Int {
        return products.size
    }

    // Bind data to the views in the ViewHolder
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val product = products[position]  // Get the product at the current position

        // Set the product details to the corresponding views
        holder.binding.tvItemCategory.text = product.category
        holder.binding.tvItemName.text = product.name
        holder.binding.tvItemPrice.text = "NPR " + product.price
        holder.binding.tvItemDescription.text = product.description

        // Load the product image using Glide
        Glide.with(holder.itemView.context)
            .load(product.image)
            .apply(RequestOptions().centerCrop())
            .placeholder(R.mipmap.img_holder) // Set default image while loading
            .error(R.mipmap.img_holder) // Set default image on error
            .into(holder.binding.ivItem) // Set image to ImageView

        // Set click listener for the item root layout
        holder.binding.itemRootLayout.setOnClickListener {
            listener.onItemClicked(product, position)  // Notify listener of item click
        }

        // Show check icon if the product is marked as purchased
        if (product.markAsPurchased) {
            val drawable = ContextCompat.getDrawable(applicationContext, R.drawable.ic_check_circle)
            holder.binding.tvItemName.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                drawable,
                null
            )
        } else {
            holder.binding.tvItemName.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                null,
                null
            )
        }
        // holder.binding.tvTimestamp.text = product.timeStamp
    }

    // Interface for item interaction callbacks
    interface ItemAdapterListener {
        fun onItemClicked(product: Product, position: Int)  // Called when an item is clicked
    }
}