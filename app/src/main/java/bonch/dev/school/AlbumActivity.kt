package bonch.dev.school

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bonch.dev.school.models.Album
import bonch.dev.school.models.Users
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class AlbumActivity : AppCompatActivity() {
    lateinit var editor: SharedPreferences.Editor
    private val APP_PREFERENCES_ALBUMS = "Album Info"
    lateinit var pref: SharedPreferences
    lateinit var gson: Gson
    lateinit var list: List<Album>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)
        val recycler = findViewById<RecyclerView>(R.id.albums_recycler)
        recycler.layoutManager = LinearLayoutManager(this@AlbumActivity)
        pref = getPreferences(MODE_PRIVATE)
        gson = Gson()
        if (isOnline(applicationContext)) {
            val service = RetrofitFactory().makeRetrofitService()

            CoroutineScope(Dispatchers.IO).launch {
                val response = service.TransferToAlbumsActivity()
                try {
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            list = response.body()!!
                            recycler.adapter = AlbumsAdapter(list, this@AlbumActivity)

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
        } else {
            if (pref.contains(APP_PREFERENCES_ALBUMS)) {
                var json: String? = pref.getString(APP_PREFERENCES_ALBUMS, "")
                val caseType = object : TypeToken<List<Album>>() {}.type
                list = gson.fromJson(json, caseType)
                recycler.adapter = AlbumsAdapter(list, this@AlbumActivity)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        editor = pref.edit()
        editor.putString(APP_PREFERENCES_ALBUMS, gson.toJson(list))
        editor.apply()
    }

    @Suppress("DEPRECATION")
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}
