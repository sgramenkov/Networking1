package bonch.dev.school

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class AlbumActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)
        val recycler = findViewById<RecyclerView>(R.id.albums_recycler)
        recycler.layoutManager = LinearLayoutManager(this@AlbumActivity)

        val service = RetrofitFactory().makeRetrofitService()

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.TransferToAlbumsActivity()
            try {
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        recycler.adapter = AlbumsAdapter(response.body()!!, this@AlbumActivity)
                        try {

                            /*val id =
                                recycler.getChildAt(pos).findViewById<TextView>(R.id.album_item_id)
                                    .text.toString().toInt()*/


                        } catch (err: HttpException) {

                        }
                    } else {
                        Toast.makeText(
                            this@AlbumActivity,
                            "${response.code()}",
                            Toast.LENGTH_SHORT
                        )
                    }
                }

            } catch (err: HttpException) {

            }
        }
    }

}
