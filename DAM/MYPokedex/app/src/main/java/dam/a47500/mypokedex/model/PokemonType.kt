package dam.a47500.mypokedex.model

import android.os.Parcelable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

@kotlinx.parcelize.Parcelize
data class PokemonType(
    var id: Int,
    var name: String,
    @DrawableRes val icon: Int,
    @ColorRes val color: Int
) : Parcelable
