package io.github.alanrb.opentv.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Movie class represents video entity with title, description, image thumbs and video url.
 */
@Parcelize
data class MovieModel(
    val id: Long,

    var title: String? = null,
    var overview: String? = null,
    var backgroundImageUrl: String? = null,
    var cardImageUrl: String? = "",
    var videoUrl: String? = "",
    var studio: String? = "",
    var ids: IdModel? = null,
    var released: String? = null,
    var year: String? = null,
    var runtime: Int? = null,
    var certification: String? = null
) : Parcelable {

    override fun toString(): String {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '}'
    }
}