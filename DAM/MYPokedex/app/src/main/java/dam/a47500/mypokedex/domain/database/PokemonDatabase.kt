package dam.a47500.mypokedex.domain.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dam.a47500.mypokedex.domain.dao.PokemonDao
import dam.a47500.mypokedex.domain.dao.PokemonRegionDao
import dam.a47500.mypokedex.domain.entities.Pokemon
import dam.a47500.mypokedex.domain.entities.PokemonRegion

@Database( entities = [PokemonRegion::class, Pokemon:: class], version = 3, exportSchema = false )
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun regionDao (): PokemonRegionDao

    abstract fun pokemonDao (): PokemonDao
    companion object {
        // For Singleton instantiation
        @Volatile private var instance : PokemonDatabase ? = null
        fun getInstance ( context : Context): PokemonDatabase {
            if ( instance != null ) return instance !!
            synchronized ( this ) {
                instance = Room
                    .databaseBuilder ( context , PokemonDatabase :: class.java , "pokedex_dabase" )
                    .fallbackToDestructiveMigration ()
                    .build ()
            }
            return instance!!
        }
    }
}