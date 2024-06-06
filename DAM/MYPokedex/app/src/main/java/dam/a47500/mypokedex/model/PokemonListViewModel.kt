package dam.a47500.mypokedex.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dam.a47500.mypokedex.domain.database.FirebasePokemonRepository
import dam.a47500.mypokedex.domain.database.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import dam.a47500.mypokedex.domain.entities.PokemonRegion
import dam.a47500.mypokedex.domain.entities.Pokemon

class PokemonListViewModel : ViewModel() {
    private val _pokemons = MutableLiveData<List<Pokemon>?>()
    val pokemons: LiveData<List<Pokemon>?>
        get() = _pokemons

    private lateinit var _repository: FirebasePokemonRepository
    fun initViewMode(repository: FirebasePokemonRepository) {
        _repository = repository
    }

    fun fetchPokemons(region: PokemonRegion) {
        viewModelScope.launch(Dispatchers.Default) {
            val pkList = _repository.getPokemonsByRegion(region)
            _pokemons.postValue(pkList.value)
        }
    }
}