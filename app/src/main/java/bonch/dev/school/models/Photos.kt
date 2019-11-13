package bonch.dev.school.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class Photos( @PrimaryKey
                   var id: Long = 0,
                   var albumId: Int? = 0,
                   var url: String? = null) : RealmObject() {
}