package com.goudurixx.pokedex.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.goudurixx.pokedex.core.data.util.NetworkMonitor
import com.goudurixx.pokedex.core.ui.component.PokedexNavigationBar
import com.goudurixx.pokedex.core.ui.component.PokedexNavigationBarItem
import com.goudurixx.pokedex.core.ui.component.PokedexNavigationRail
import com.goudurixx.pokedex.core.ui.component.PokedexNavigationRailItem
import com.goudurixx.pokedex.graph.MainNavGraph
import com.goudurixx.pokedex.navigation.MainDestinations

@Composable
fun PokedexApp(
    windowSizeClass: WindowSizeClass,
    networkMonitor: NetworkMonitor,
    appState: PokedexAppState = rememberPokedexAppState(
        windowSizeClass = windowSizeClass,
        networkMonitor = networkMonitor,
    )
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val isOffline by appState.isOffline.collectAsStateWithLifecycle()
    var wasOffline by remember { mutableStateOf(false) }

    val notConnectedMessage = "⚠️ No internet connection. "
    val backOnline = " Connected to the internet."
    LaunchedEffect(isOffline) {
        if (isOffline) {
            wasOffline = true
            snackbarHostState.showSnackbar(
                message = notConnectedMessage,
                withDismissAction = true,
                duration = SnackbarDuration.Indefinite,
            )
        } else if (wasOffline) {
            wasOffline = false
            snackbarHostState.showSnackbar(
                message = backOnline,
                duration = SnackbarDuration.Short,
            )
        }
    }

    Scaffold(
        bottomBar = {
            if (appState.shouldShowBottomBar) {
                PokedexBottomBar(
                    bottomBarState = appState.isFullScreen,
                    destinations = appState.topLevelDestinations,
                    currentDestination = appState.currentDestination,
                    onNavigateToDestination = appState::navigateToTopLevelDestination,
                )
            }
        },
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),

        ) { padding ->
        Row(
            Modifier
                .fillMaxSize()
                .padding(bottom = if (appState.isFullScreen) padding.calculateBottomPadding() else 0.dp)
                .consumeWindowInsets(padding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal,
                    ),
                ),
        ) {
            if (appState.shouldShowNavRail) {
                PokedexNavRail(
                    destinations = appState.topLevelDestinations,
                    onNavigateToDestination = appState::navigateToTopLevelDestination,
                    currentDestination = appState.currentDestination,
                    modifier = Modifier
                        .testTag("NiaNavRail")
                        .safeDrawingPadding(),
                )
            }
            Column(
                modifier = Modifier
            ) {
                MainNavGraph(
                    navController = appState.navController,
                )
            }
        }
    }
}

@Composable
private fun PokedexNavRail(
    destinations: List<MainDestinations>,
    onNavigateToDestination: (MainDestinations) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    PokedexNavigationRail(modifier = modifier) {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            PokedexNavigationRailItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    Icon(
                        imageVector = destination.selectedIcon,
                        contentDescription = null,
                    )
                },
                selectedIcon = {
                    Icon(
                        imageVector = destination.unselectedIcon,
                        contentDescription = null,
                    )
                },
                label = { Text(stringResource(destination.title)) },
            )
        }
    }
}

@Composable
private fun PokedexBottomBar(
    bottomBarState: Boolean,
    destinations: List<MainDestinations>,
    onNavigateToDestination: (MainDestinations) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = bottomBarState,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            PokedexNavigationBar(
                modifier = modifier,
            ) {
                destinations.forEach { destination ->
                    val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
                    PokedexNavigationBarItem(
                        selected = selected,
                        onClick = { onNavigateToDestination(destination) },
                        icon = {
                            Icon(
                                imageVector = destination.unselectedIcon,
                                contentDescription = null,
                            )
                        },
                        selectedIcon = {
                            Icon(
                                imageVector = destination.selectedIcon,
                                contentDescription = null,
                            )
                        },
                        label = {
                            Text(
                                text = stringResource(id = destination.title),
                                textAlign = TextAlign.Center,
                            )
                        },
                    )
                }
            }
        }
    )
}


private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: MainDestinations) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false
