package bonch.dev.school

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class PhotosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photos)
        val recycler=findViewById<RecyclerView>(R.id.photos_recycler)
        recycler.layoutManager= LinearLayoutManager(this)
        val service=RetrofitFactory().makeRetrofitService()
        CoroutineScope(Dispatchers.IO).launch {


            val response = service.TransferToPhotosActivity()
            try {
                withContext(Dispatchers.Main){
                    if (response.isSuccessful){
                        recycler.adapter=PhotosAdapter(response.body()!!,this@PhotosActivity)
                    }
                    else{
                        Toast.makeText(applicationContext,"${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

            }catch (err: HttpException){
                Log.e("Retrofit","${err.printStackTrace()}")
            }
        }
    }
    }

