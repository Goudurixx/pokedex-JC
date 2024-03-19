import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInCubic
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import com.goudurixx.pokedex.core.common.models.OrderByValues
import com.goudurixx.pokedex.features.pokemon.SearchUiState
import com.goudurixx.pokedex.features.pokemon.models.SortOrderItem
import java.util.Locale

@Composable
fun DockedSearchContainer(
    query: String,
    onQueryChange: (String) -> Unit,
    state: SearchUiState,
    modifier: Modifier = Modifier,
    onFilterClick: (SortOrderItem) -> Unit = {},
    sortFilterList: List<SortOrderItem> = emptyList(),
    onClickOnResult: (Int, Int) -> Unit = { i, s -> },
) {

    var isDockedSearchBarActive by rememberSaveable { mutableStateOf(false) }
    var showOrderMenu by remember { mutableStateOf(false) }

    val trailingIcon: @Composable() (() -> Unit) = {
        Row {
            AnimatedVisibility(visible = query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null
                    )
                }
            }
            AnimatedVisibility(visible = !isDockedSearchBarActive && sortFilterList.isNotEmpty()) {
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
            modifier = Modifier
//                .clip(RoundedCornerShape(16.dp)) // TODO FIX THE SHAPE OF THE MENU
//                .background(MaterialTheme.colorScheme.error)
            ,
            properties = PopupProperties(focusable = true)
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
//                    .clip(RoundedCornerShape(16.dp))
//                    .background(MaterialTheme.colorScheme.onError)
            ) {
                sortFilterList.forEach { sortFilterItem ->
                    DropdownMenuItem(
                        text = {
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
    }

    val leadingIcon: @Composable() (() -> Unit) =
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
        }

    val searchBarContent: @Composable() (ColumnScope.() -> Unit) =
        {
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
                                        pokemon.color.ordinal
                                    )
                                }
                        )
                    }
                }
            if (state is SearchUiState.Loading)
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }


    if (LocalConfiguration.current.screenWidthDp < 600.dp.value)
        CompactDeviceSearchBar(
            isDockedSearchBarActive = isDockedSearchBarActive,
            onActiveChange = { isDockedSearchBarActive = it },
            query = query,
            onQueryChange = onQueryChange,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            searchBarContent = searchBarContent,
            modifier = modifier
        )
    else
        FullDeviceSearchBar(
            isDockedSearchBarActive = isDockedSearchBarActive,
            onActiveChange = { isDockedSearchBarActive = it },
            query = query,
            onQueryChange = onQueryChange,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            searchBarContent = searchBarContent,
            modifier = modifier
        )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullDeviceSearchBar(
    isDockedSearchBarActive: Boolean,
    onActiveChange: (Boolean) -> Unit,
    query: String,
    onQueryChange: (String) -> Unit,
    leadingIcon: @Composable() (() -> Unit),
    trailingIcon: @Composable() (() -> Unit),
    searchBarContent: @Composable() (ColumnScope.() -> Unit),
    modifier: Modifier = Modifier
) {
    DockedSearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = { onActiveChange(!isDockedSearchBarActive) }, //TODO: Implement search screen
        active = isDockedSearchBarActive,
        onActiveChange = onActiveChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .statusBarsPadding()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { onActiveChange(false) })
            },
        placeholder = { Text("Search pokemon name") },
        leadingIcon = leadingIcon,
        shadowElevation = 16.dp,
        trailingIcon = trailingIcon,
        content = searchBarContent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CompactDeviceSearchBar(
    isDockedSearchBarActive: Boolean,
    onActiveChange: (Boolean) -> Unit,
    query: String,
    onQueryChange: (String) -> Unit,
    leadingIcon: @Composable() (() -> Unit),
    trailingIcon: @Composable() (() -> Unit),
    searchBarContent: @Composable() (ColumnScope.() -> Unit),
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .semantics { isTraversalGroup = true }
            .zIndex(1f + if (isDockedSearchBarActive) 1f else 0f)
            .fillMaxWidth()

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
            onActiveChange = onActiveChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding),
            placeholder = { Text("Search pokemon name") },
            leadingIcon = leadingIcon,
            shadowElevation = 16.dp,
            trailingIcon = trailingIcon,
            content = searchBarContent
        )
    }
}