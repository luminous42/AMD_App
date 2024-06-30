package np.com.luminoussuwal.babybuy

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import np.com.luminoussuwal.babybuy.Dashboard.adapters.OffersHorizontalAdapter.ViewHolder
import np.com.luminoussuwal.babybuy.Dashboard.db.Product
import np.com.luminoussuwal.babybuy.databinding.ItemViewBinding
import np.com.luminoussuwal.babybuy.databinding.LayoutItemOffersBinding

class ItemAdapter(private val items: List<Product>)
    : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    inner class ViewHolder( val binding: ItemViewBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.tvItemName.text = "newname"
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

    }
    override fun getItemCount(): Int {
        return items.size
    }


}