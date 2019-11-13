package bonch.dev.school

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bonch.dev.school.models.Users
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.lang.IllegalStateException

class UsersActivity() : AppCompatActivity() {
    lateinit var editor: SharedPreferences.Editor
    private val APP_PREFERENCES_USERS = "User Info"
    lateinit var pref: SharedPreferences
    lateinit var gson: Gson
    lateinit var list: List<Users>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)
        pref = getPreferences(MODE_PRIVATE)
        gson = Gson()
        val recycler = findViewById<RecyclerView>(R.id.users_recycler)
        recycler.layoutManager = LinearLayoutManager(this)
        if (isOnline(applicationContext)) {
            val service = RetrofitFactory().makeRetrofitService()
            CoroutineScope(Dispatchers.IO).launch {
                val response = service.TransferToUsersActivity()
                try {
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            list = response.body()!!
                            recycler.adapter = UsersAdapter(list, this@UsersActivity)


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
        } else {
            if (pref.contains(APP_PREFERENCES_USERS)) {
                var json: String? = pref.getString(APP_PREFERENCES_USERS, "")
                val caseType = object : TypeToken<List<Users>>() {}.type
                list = gson.fromJson(json, caseType)
                recycler.adapter = UsersAdapter(list, this@UsersActivity)
            }
        }
    }


    override fun onPause() {
        super.onPause()
        editor = pref.edit()
        editor.putString(APP_PREFERENCES_USERS, gson.toJson(list))
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

