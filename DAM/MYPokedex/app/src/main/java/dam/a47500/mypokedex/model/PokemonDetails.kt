package dam.a47500.mypokedex.model

data class PokemonDetails(
    var pokemon: Pokemon,
    var description: String,
    var weight: Double,
    var height: Double,
    var types: List<PokemonType>
)
