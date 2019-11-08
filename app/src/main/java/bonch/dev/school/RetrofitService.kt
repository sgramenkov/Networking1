package bonch.dev.networking.networking

import bonch.dev.school.models.Album
import bonch.dev.school.models.photos
import bonch.dev.school.models.Post
import bonch.dev.school.models.users
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface RetrofitService{
@GET ("/users")
suspend fun TransferToUsersActivity():Response<List<users>>
    @GET("/albums")
    suspend fun TransferToAlbumsActivity(@Query("userId") userId:Int = 1):Response<List<Album>>
    @DELETE("/albums/{id}")
    suspend fun DeleteInAlbumsActivity(@Path("id") id:Int):Response<*>
    @GET("/photos")
    suspend fun TransferToPhotosActivity():Response<List<photos>>

    @FormUrlEncoded
    @POST("/posts")
    suspend fun TransferToFragment(
        @Field("title") title:String,
        @Field("body") body:String

    ) : Response<Post>
}