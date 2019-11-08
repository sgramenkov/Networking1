package bonch.dev.school.models

import retrofit2.http.Field

data class Post (
    val id : Int,
    val title:String,
    val body:String,
    val userId : Int
)