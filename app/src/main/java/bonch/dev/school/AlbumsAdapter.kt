package bonch.dev.school

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import bonch.dev.school.models.Album
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class AlbumsAdapter(val list: List<Album>, val context: Context/*, val listener: (Int) -> Unit*/) :
    RecyclerView.Adapter<AlbumsAdapter.AlbumsHolder>() {

    var albumList: MutableList<Album> = list.toMutableList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumsHolder {
        return AlbumsHolder(
            LayoutInflater.from(context).inflate(
                R.layout.albums_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return albumList.size
    }

    override fun onBindViewHolder(holder: AlbumsHolder, position: Int) {
        val albums = albumList[position]
        holder.bind(albums,position/*,listener*/)

    }

    fun removeItem(position: Int) {
        albumList.removeAt(position)
    }

    inner class AlbumsHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        private val AlbumsTextView = itemView.findViewById<TextView>(R.id.albums_text_view)
        private val albumId = itemview.findViewById<TextView>(R.id.album_item_id)
        fun bind(
            albums: Album,
            position: Int/*, listener: (Int) -> Unit*/
        )/*= with(itemView)*/ {
            albumId.text = albums.id.toString()
            AlbumsTextView.text = "\ntitle: " + albums.title
           /* this.setOnClickListener(){
                listener(position)
            }*/
            itemView.setOnClickListener(){
                test(position)
            }

        }
    }
    fun test(position: Int){
        val service = RetrofitFactory().makeRetrofitService()
        CoroutineScope(Dispatchers.Main).launch {
            //val bundle = Bundle()
            //val pos = bundle.getInt("pos")
            notifyDataSetChanged()
            Log.e("E","$position")
            val responsedel = service.DeleteInAlbumsActivity(position)
            try {
                withContext(Dispatchers.Main) {

                    if (responsedel.isSuccessful) {
                        albumList.removeAt(position)
                        notifyDataSetChanged()
                        Toast.makeText(
                            context,
                            "Соединение выполнено",
                            Toast.LENGTH_SHORT
                        ).show()


                    } else {
                        Toast.makeText(
                            context,
                            "${responsedel.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (err: HttpException) {

            }
        }

        //val bundle=Bundle()
        //bundle.putInt("pos",position)
    }
}