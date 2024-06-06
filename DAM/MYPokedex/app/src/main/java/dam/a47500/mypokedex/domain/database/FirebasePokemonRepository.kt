package dam.a47500.mypokedex.domain.database

import androidx.annotation.OptIn
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import dam.a47500.mypokedex.domain.entities.Pokemon
import dam.a47500.mypokedex.domain.entities.PokemonRegion
import dam.a47500.mypokedex.model.network.responses.PokemonAPI

class FirebasePokemonRepository(private val pokemonApi: PokemonAPI,
                            private val firebaseRepository: FirebaseRepository
) {

        @OptIn(UnstableApi::class)
        suspend fun getPokemonsByRegion(region: PokemonRegion): LiveData<List<Pokemon>> {
            try {
                val regionWithPokemons = firebaseRepository.readPokemons(region.id)
                Log.i("vazio",regionWithPokemons.toString())
                if (regionWithPokemons.isEmpty()) {
                    var pkByRegionResponse = pokemonApi.fetchPokemonByRegionId(region.id)
                    val pokemons = pkByRegionResponse.pokemons.map {
                        val regexToGetId = "/([^/]+)/?\$".toRegex()
                        var pkId = regexToGetId.find(it.url!!)?.value
                        pkId = pkId?.removeSurrounding("/")
                        val pkName = it.name ?: ""
                        val pkIdInt = pkId?.toInt() ?: 0
                        val pkImageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master" +
                                "/sprites/pokemon/other/official-artwork/${pkId}.png"
                        Pokemon(pkIdInt, pkName, pkImageUrl, regionId = region.id)
                    }
                    firebaseRepository.writePokemons(pokemons)

                    return MutableLiveData(pokemons)
                } else {
                    return MutableLiveData(regionWithPokemons)
                }
            } catch (e: Exception) {
                Log.e("Firebase-ERROR", e.toString())
            }
            return MutableLiveData<List<Pokemon>>()
        }
    }


