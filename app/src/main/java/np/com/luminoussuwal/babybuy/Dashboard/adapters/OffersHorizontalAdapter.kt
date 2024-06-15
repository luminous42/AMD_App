package np.com.luminoussuwal.babybuy.Dashboard.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import np.com.luminoussuwal.babybuy.Dashboard.Product
import np.com.luminoussuwal.babybuy.databinding.LayoutItemOffersBinding

class OffersHorizontalAdapter(val products: List<Product>)
    : RecyclerView.Adapter<OffersHorizontalAdapter.ViewHolder>(){

    class ViewHolder(val binding: LayoutItemOffersBinding
    ): RecyclerView.ViewHolder(binding.root){


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LayoutItemOffersBinding.inflate(layoutInflater)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int) {
        val product = products[position]

        holder.binding.tvItemOffersName.text = product.name

    }
}