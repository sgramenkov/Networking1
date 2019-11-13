package bonch.dev.school

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bonch.dev.school.models.Photos
import com.bumptech.glide.Glide
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class PhotosActivity : AppCompatActivity() {
    lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photos)
        Realm.init(applicationContext)
        val config = RealmConfiguration.Builder()
            .name("PhotosDB.realm")
            .build()
        realm = Realm.getInstance(config)
        val recycler = findViewById<RecyclerView>(R.id.photos_recycler)
        recycler.layoutManager = LinearLayoutManager(this)
        if (isOnline(applicationContext)) {
            val service = RetrofitFactory().makeRetrofitService()
            CoroutineScope(Dispatchers.IO).launch {


                val response = service.TransferToPhotosActivity()
                try {
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            val list: List<Photos> = response.body()!!

                            saveData(list)
                            recycler.adapter = PhotosAdapter(list, this@PhotosActivity)
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "${response.code()}",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }

                } catch (err: HttpException) {
                    Log.e("Retrofit", "${err.printStackTrace()}")
                }
            }
        }
        else{
            val list:List<Photos> = getData()
            recycler.adapter=PhotosAdapter(list, this@PhotosActivity)
        }
        }
    private fun getData(): List<Photos> {
        var list: ArrayList<Photos> = arrayListOf()
        val realmResult = realm.where(Photos::class.java).findAll()
        if (realmResult != null) {
            for(i in realmResult.indices){
                val tempDataPhoto = Photos(realmResult[i]!!.id,realmResult[i]!!.albumId!!, realmResult[i]!!.url!!)
                list.add(tempDataPhoto)
            }
        } else {
            Toast.makeText(applicationContext, "No Internet connection!", Toast.LENGTH_SHORT).show()
        }
        return list
    }
    private fun saveData(list: List<Photos>) {
        val arrList: ArrayList<Photos> = arrayListOf()
        if (list.isNotEmpty()) {
//сначала все упакуем в один массив, а потом одной транзакцией отправим в БД
            list.forEach {
                val photo = Photos()
                photo.id=it.id
                photo.albumId = it.albumId
                photo.url = it.url
                //photo.img = Glide.with(this).load(it.url)
                arrList.add(photo)
            }
//одним траншем пишем в базу
            realm.executeTransactionAsync({ bgRealm ->
                bgRealm.insertOrUpdate(arrList) //сохраняем первый раз или обновляем уже имеющееся
            }, {
                Toast.makeText(applicationContext, "Success write", Toast.LENGTH_SHORT).show()
            }, {
                Toast.makeText(applicationContext, "Fail write", Toast.LENGTH_SHORT).show()
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
    @Suppress("DEPRECATION")
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
    }

