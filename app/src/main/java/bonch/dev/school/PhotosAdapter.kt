package bonch.dev.school

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bonch.dev.school.models.photos
import com.bumptech.glide.Glide

class PhotosAdapter(val list: List<photos>,val context: Context):
    RecyclerView.Adapter<PhotosAdapter.PhotosHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosHolder {
        return PhotosHolder(LayoutInflater.from(context).inflate(R.layout.photos_item,parent,false))
    }
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PhotosHolder, position: Int) {
        val photo=list[position]
        holder.bind(photo,position)
    }

    inner class PhotosHolder(itemview:View):RecyclerView.ViewHolder(itemview){
        private val imageView=itemView.findViewById<ImageView>(R.id.photos_image_view)
        private val PhotosTextView=itemView.findViewById<TextView>(R.id.photos_text_view)
        fun bind(photos: photos,position: Int){
            PhotosTextView.text="${position+1}-я картинка"
            Glide.with(context).load(photos.url).into(imageView)
        }
    }
}