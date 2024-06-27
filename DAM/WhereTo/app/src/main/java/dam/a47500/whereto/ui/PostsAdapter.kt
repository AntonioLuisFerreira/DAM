package dam.a47500.whereto.ui


import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import dam.a47500.whereto.R
import dam.a47500.whereto.databinding.ItemShortPostBinding
import dam.a47500.whereto.data.Post

class PostsAdapter(
    private val postList: List<Post>,
    private val context: Context,
    private val onItemClick: (Post) -> Unit
) : RecyclerView.Adapter<PostsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val postItemBinding = ItemShortPostBinding.bind(itemView)

        fun bindView(post: Post, itemClickedListener: (Post) -> Unit) {
            postItemBinding.shortPost = post
            itemView.setOnClickListener{
                itemClickedListener.invoke(post)
            }
        }

        var usernameView = itemView.findViewById<AppCompatTextView>(R.id.textViewProfile)
        val imageView = itemView.findViewById<AppCompatImageView>(R.id.imageView)
        val dataView = itemView.findViewById<AppCompatTextView>(R.id.textViewDate)

        val hoursView = itemView.findViewById<AppCompatTextView>(R.id.textViewHours)
        val locationView = itemView.findViewById<AppCompatTextView>(R.id.textViewLocation)
        val capacityView = itemView.findViewById<AppCompatTextView>(R.id.textViewCapacity)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_short_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = postList[position]
        holder.bindView(post, onItemClick)

        holder.usernameView.text = post.username
        //holder.imageView.setImageResource(post.imageUrls[0])
        Glide.with(holder.imageView.context)
            .asBitmap()
            .load(post.imageUrls[0])
            .listener(object : RequestListener<Bitmap>
            {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>,
                    isFirstResource: Boolean
                ): Boolean {

                    Log.d("TAG", e?.message.toString())
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap,
                    model: Any,
                    p2: Target<Bitmap>?,
                    dataSource: DataSource,
                    p4: Boolean
                ): Boolean {
                    Log.d("TAG", "OnResourceReady")
                    if (resource != null) {
                        val p: Palette = Palette.from(resource).generate()

                        val rgb = p?.lightMutedSwatch?.rgb
                        if (rgb != null) {
                            //holder.cardView.setCardBackgroundColor(rgb)
                        }
                    }
                    return false
                }
            })
            .into(holder.imageView)
        holder.dataView.text = post.date

        holder.locationView.text = post.location
        holder.capacityView.text = "Max. " + post.capacity.toString()

        holder.itemView.setOnClickListener {
            onItemClick(post) // 3. Call interface method
        }
    }


    override fun getItemCount(): Int {
        return postList.size
    }
}