package dam.a47500.mypokedex.domain.database

import androidx.media3.common.util.Log
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.firebase.database.*
import dam.a47500.mypokedex.domain.entities.Pokemon
import dam.a47500.mypokedex.domain.entities.PokemonRegion


import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class FirebaseRepository() {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://pokedex-ac9d0-default-rtdb.europe-west1.firebasedatabase.app/")
    private val pokemonReference: DatabaseReference = database.getReference("pokemons")
    private val pokemonRegionReference: DatabaseReference = database.getReference("region")

    //Pokemons

    /*suspend fun readPokemons(): List<Pokemon> {
        // Fetch data from Realtime Database

        val dataSnapshot = pokemonReference.get().await()
        val pokemonList = mutableListOf<Pokemon>()
        for (snapshot in dataSnapshot.children) {

            Log.i("Info",snapshot.getValue().)
            //Pokemon(pkIdInt, pkName, pkImageUrl, regionId = region.id)
        }

        return pokemonList
    }*/

    suspend fun readPokemons(regionId: Int): List<Pokemon> {
        // Fetch data from Realtime Database
        val dataSnapshot = pokemonReference.get().await()
        val pokemonList = mutableListOf<Pokemon>()

        for (snapshot in dataSnapshot.children) {
            val parsed = parsePokemonFromString(snapshot.value.toString())

            if(parsed.get(3).equals(regionId.toString())){

                val pokemon = Pokemon(parsed.get(0)!!.toInt(), parsed.get(1)!!, parsed.get(2)!!, parsed.get(3)!!.toInt())
                pokemonList.add(pokemon)
            }

        }

        return pokemonList
    }

    fun parsePokemonFromString(input: String): Array<String?> {
        val keyValuePairs = input
            .substring(1, input.length - 1) // Remove braces
            .split(", ") // Split by ", "
            .map { it.split("=") } // Split each key-value pair by "="
            .associate { it[0] to it[1] } // Convert to map

        val id = keyValuePairs["id"]

        val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master" +
                "/sprites/pokemon/other/official-artwork/${id}.png"

        val name = keyValuePairs["name"]

        val regionId = keyValuePairs["regionId"]

        return arrayOf(id, name, imageUrl, regionId)
    }


    suspend fun writePokemons(pokemons: List<Pokemon>) {
        // Write data to Realtime Database
        for (pokemon in pokemons) {
            pokemonReference.child(pokemon.id.toString()).setValue(pokemon)
        }
    }

    //Regions

    suspend fun readPokemonRegions(): List<PokemonRegion> {

        val dataSnapshot = pokemonRegionReference.get().await()

        val regionList = mutableListOf<PokemonRegion>()

        for (snapshot in dataSnapshot.children) {

            val region = PokemonRegion(snapshot.key?.toInt() ?: 0, snapshot.getValue().toString())

            regionList.add(region)
        }

        return regionList
    }

    suspend fun writePokemonRegions(region: PokemonRegion) {
            pokemonRegionReference.child(region.id.toString()).setValue(region.name).await()

    }


}

