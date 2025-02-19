package com.sevdotdev.seeminglyapokedex.ui.pokelist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.sevdotdev.seeminglyapokedex.R
import com.sevdotdev.seeminglyapokedex.domain.model.PokemonListItem
import com.sevdotdev.seeminglyapokedex.ui.common.TypePill
import com.sevdotdev.seeminglyapokedex.ui.theme.SeeminglyAPokeDexTheme
import com.sevdotdev.seeminglyapokedex.ui.util.res.toStringResource
import com.sevdotdev.seeminglyapokedex.ui.util.theme.toColor

/**
 * Card to display a [PokemonListItem] this is an internal
 * component intended to be used specifically for the "PokeList" feature.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PokeListItemCard(
    pokemon: PokemonListItem,
    onPokemonClicked: (name: String) -> Unit,
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = Modifier
            .fillMaxWidth(),
        onClick = { onPokemonClicked(pokemon.name) }) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current).data(pokemon.imageUrl)
                .crossfade(true).placeholder(R.drawable.pokeball_ic).build()
        )
        Column(Modifier.fillMaxWidth()) {
            Image(
                painter = painter, contentDescription = null,
                Modifier
                    .aspectRatio(1.71f)
                    .background(MaterialTheme.colorScheme.secondary),

                )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = pokemon.id, style = MaterialTheme.typography.titleLarge)
                    Row {
                        TypePill(
                            typeName = stringResource(id = pokemon.type.toStringResource()),
                            typeColor = pokemon.type.toColor()
                        )
                    }
                }
                Text(
                    text = pokemon.name.replaceFirstChar { c -> c.uppercase() },
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        }
    }
}


@Preview
@Composable
private fun PreviewPokeListItem() {
    SeeminglyAPokeDexTheme {
        Column {
            PokeListItemCard(
                pokemon = PokemonListItem("Pikachu", "image", "50"),
                onPokemonClicked = {})
        }
    }
}