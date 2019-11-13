package bonch.dev.school

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bonch.dev.school.models.Users
import com.google.gson.Gson

class UsersAdapter(val list: List<Users>, val context: Context) :
    RecyclerView.Adapter<UsersAdapter.UsersHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersHolder {
        return UsersHolder(LayoutInflater.from(context).inflate(R.layout.users_item, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: UsersHolder, position: Int) {
        val user = list[position]
        holder.bind(user)

    }

    inner class UsersHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val usersTextView = itemView.findViewById<TextView>(R.id.users_text_view)
        fun bind(users: Users) {
            usersTextView.text =
                "id: " + users.id.toString() + "\nname: " + users.name + "\nusername: " + users.username + "\nemail: " + users.email + "\naddress: " + "\n   street: " + users.address.street + "\n   suite: " + users.address.suite + "\n   city: " + users.address.city + "\n   zipcode: " + users.address.zipcode + "\ngeo:\n   lat: " + users.address.geo.lat + "\n   lng: " + users.address.geo.lng
        }

    }
}