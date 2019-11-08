package bonch.dev.school

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import bonch.dev.school.models.Post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.lang.Exception

class PostCreateDialogFragment(context: Context) : DialogFragment() {
    val con=context
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater?.inflate(R.layout.fragment_dialog, null)
        val TitleTextView = view?.findViewById<TextView>(R.id.title_et)
        val BodyTextView = view?.findViewById<TextView>(R.id.body_et)
        builder.setPositiveButton("Send") { dialog, which ->
            val a = TitleTextView?.text.toString()
            val b = BodyTextView?.text.toString()
            val service = RetrofitFactory().makeRetrofitService()
            CoroutineScope(Dispatchers.IO).launch {
                val response = service.TransferToFragment(a, b)
                try {
                    withContext(Dispatchers.Main) {

                        try {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    con,
                                    response.code().toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Log.e("PostRequest", "Fail : ${response.raw()}")
                            }

                        } catch (err: Exception) {
                            Log.e("PostRequest", "Something went wrong : ${err.printStackTrace()}")

                        }
                    }
                } catch (err: HttpException) {
                    Log.e("Retrofit", "${err.printStackTrace()}")
                }
            }
        }
        builder.setView(inflater)
        return builder.create()
    }
}
