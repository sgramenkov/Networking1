package bonch.dev.school

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val UsersActivityButton=findViewById<Button>(R.id.users_activity_button)
        val AlbumsActivityButton=findViewById<Button>(R.id.albums_activity_button)
        val photosActivityButton=findViewById<Button>(R.id.photos_activity_button)
        val FragmentButton=findViewById<Button>(R.id.fragment_button)
        UsersActivityButton.setOnClickListener(){
            val intent=Intent(this,UsersActivity::class.java)
            startActivity(intent)
        }
        AlbumsActivityButton.setOnClickListener(){
            val intent=Intent(this,AlbumActivity::class.java)
            startActivity(intent)
        }
        photosActivityButton.setOnClickListener(){
            val intent=Intent(this,PhotosActivity::class.java)
            startActivity(intent)
        }
        FragmentButton.setOnClickListener(){
            val dialog = PostCreateDialogFragment(applicationContext)
            dialog.show(supportFragmentManager,"dialog")
        }
    }
}
