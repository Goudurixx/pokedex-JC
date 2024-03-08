import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInCubic
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import com.goudurixx.pokedex.core.common.models.OrderByValues
import com.goudurixx.pokedex.features.pokemon.SearchUiState
import com.goudurixx.pokedex.features.pokemon.models.SortOrderItem
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DockedSearchContainer(
    sortFilterList : List<SortOrderItem>,
    query : String,
    onQueryChange : (String) -> Unit,
    onFilterClick : (SortOrderItem) -> Unit,
    onClickOnResult : (Int, Int) -> Unit,
    state : SearchUiState,

    ){

    var isDockedSearchBarActive by rememberSaveable { mutableStateOf(false) }
    val backgroundColor = MaterialTheme.colorScheme.background
    var showOrderMenu by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .semantics { isTraversalGroup = true }
            .zIndex(1f + if(isDockedSearchBarActive) 1f else 0f)
            .fillMaxWidth()
            .drawBehind {
                val brush = Brush.verticalGradient(
                    0.0f to backgroundColor,
                    1.0f to backgroundColor.copy(alpha = 0.0f)
                )
                drawRect(brush = brush)
            }
    ) {

        val transition =
            updateTransition(isDockedSearchBarActive, label = "container transform")
        val padding by transition.animateDp(
            label = "corner radius",
            transitionSpec = {
                when (targetState) {
                    true -> tween(
                        durationMillis = 400,
                        easing = EaseOutCubic,
                    )

                    else -> tween(
                        durationMillis = 200,
                        easing = EaseInCubic,
                    )
                }
            }
        ) { state ->
            when (state) {
                true -> 0.dp
                false -> 16.dp
            }
        }
        SearchBar(
            query = query,
            onQueryChange = onQueryChange,
            onSearch = {
                //TODO: Implement search screen
            },
            active = isDockedSearchBarActive,
            onActiveChange = { isDockedSearchBarActive = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding),
            placeholder = { Text("Search pokemon name") },
            leadingIcon =
            {
                AnimatedContent(isDockedSearchBarActive, label = "") { active ->
                    if (active) {
                        IconButton(onClick = { isDockedSearchBarActive = false }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = null
                            )
                        }
                    } else {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    }
                }
            },
            shadowElevation = 16.dp,
            trailingIcon = {
                Row {
                    AnimatedVisibility(visible = query.isNotEmpty()) {
                        IconButton(onClick = { onQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null
                            )
                        }
                    }
                    AnimatedVisibility(visible = !isDockedSearchBarActive){
                        IconButton(onClick = { showOrderMenu = !showOrderMenu }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Sort,
                                contentDescription = "Search"
                            )
                        }
                    }
                }
                DropdownMenu(
                    expanded = showOrderMenu,
                    onDismissRequest = { showOrderMenu = false },
                    properties = PopupProperties(focusable = true)
                ) {
                    sortFilterList.forEach { sortFilterItem ->
                        DropdownMenuItem(text = {
                            Text(text = sortFilterItem.parameter.parameterName)
                        }, trailingIcon = {
                            sortFilterItem.order?.let {
                                Icon(
                                    imageVector = when (it) {
                                        OrderByValues.ASC -> Icons.Filled.TrendingUp
                                        OrderByValues.DESC -> Icons.Filled.TrendingDown
                                    },
                                    contentDescription = null
                                )
                            }
                        }, onClick = {
                            onFilterClick(sortFilterItem)
                            showOrderMenu = false
                        }
                        )
                    }
                }
            }
        ) {
            if (state is SearchUiState.Success)
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(state.list) { pokemon ->
                        ListItem(
                            headlineContent = {
                                Text(
                                    text = pokemon.name.capitalize(Locale.ROOT),
                                )
                            },
                            overlineContent = {
                                Text(
                                    text = pokemon.id.toString(),
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onClickOnResult(
                                        pokemon.id,
                                        backgroundColor.toArgb()
                                    )
                                }
                        )
                    }
                }
            if (state is SearchUiState.Loading)
                LinearProgressIndicator()
        }
    }
}