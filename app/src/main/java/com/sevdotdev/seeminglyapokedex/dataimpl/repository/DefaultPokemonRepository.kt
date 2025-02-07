package com.sevdotdev.seeminglyapokedex.dataimpl.repository

import com.sevdotdev.seeminglyapokedex.data.datasource.PokemonLocalDataSource
import com.sevdotdev.seeminglyapokedex.data.datasource.PokemonRemoteDataSource
import com.sevdotdev.seeminglyapokedex.data.repository.PokemonRepository
import com.sevdotdev.seeminglyapokedex.domain.model.PokemonListItem
import com.sevdotdev.seeminglyapokedex.domain.model.SinglePokemon
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

/**
 * Default implementation of [PokemonRepository] manages when to request data from network and
 * returns data from [localDataSource]
 */
@OptIn(ExperimentalCoroutinesApi::class)
class DefaultPokemonRepository @Inject constructor(
    private val remoteDataSource: PokemonRemoteDataSource,
    private val localDataSource: PokemonLocalDataSource,
) : PokemonRepository {

    /**
     * Save list from network response to the [localDataSource], if there was an error
     * will save and empty list
     */
    override suspend fun refreshPokemonData() {
        val pokemonList = remoteDataSource.getAllPokemon().getOrDefault(emptyList())
        localDataSource.savePokemonList(pokemonList)
    }

    /**
     * Due to a refresh of the pokemon list not always corresponding with request for this flow
     * using an empty list to flag as a failed result.
     */
    override fun getAllPokemonFlow(): Flow<Result<List<PokemonListItem>>> =
        localDataSource.getPokemonListFlow().mapLatest { pokemons ->
            if (pokemons.isNotEmpty()) {
                Result.success(pokemons)
            } else Result.failure(Exception("Missing Data"))
        }

    override fun getPokemonByNameFlow(name: String): Flow<Result<SinglePokemon?>> {
        return localDataSource.getSinglePokemonByNameFlow(name).mapLatest { singleMon ->
            if (singleMon != null) {
                Result.success(singleMon)
            } else {
                remoteDataSource.getPokemonByName(name).also { result ->
                    result.getOrNull()?.let { singlePokemon ->
                        localDataSource.saveSinglePokemon(singlePokemon)
                    }
                }
            }
        }
    }
}