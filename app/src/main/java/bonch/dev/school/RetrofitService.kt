package bonch.dev.networking.networking

import android.provider.Contacts
import bonch.dev.school.models.Album
import bonch.dev.school.models.Photos
import bonch.dev.school.models.Post
import bonch.dev.school.models.Users
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface RetrofitService{
@GET ("/users")
suspend fun TransferToUsersActivity():Response<List<Users>>
    @GET("/albums")
    suspend fun TransferToAlbumsActivity(@Query("userId") userId:Int = 1):Response<List<Album>>
    @DELETE("/albums/{id}")
    suspend fun DeleteInAlbumsActivity(@Path("id") id:Int):Response<*>
    @GET("/photos")
    suspend fun TransferToPhotosActivity():Response<List<Photos>>

    @FormUrlEncoded
    @POST("/posts")
    suspend fun TransferToFragment(
        @Field("title") title:String,
        @Field("body") body:String

    ) : Response<Post>
}