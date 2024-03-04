package com.goudurixx.pokedex.core.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.goudurixx.pokedex.graph.MainNavGraph
import com.goudurixx.pokedex.navigation.MainDestinations

private val mainDestinationsRoutes = MainDestinations.entries.map { it.route }


@Composable
fun PokedexApp(
    navController: NavHostController = rememberNavController()
) {

    var bottomBarState by rememberSaveable { (mutableStateOf(true)) }

    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    bottomBarState = mainDestinationsRoutes.contains(currentDestination?.route)


    Scaffold(
        bottomBar = {
            PokedexBottomBar(
                bottomBarState = bottomBarState,
                currentDestination = currentDestination,
                navigateToDestination = navController::handleBottomNavigationToScreen
            )
        },
    ) {
        Column(modifier = Modifier.padding(bottom = if (bottomBarState) it.calculateBottomPadding() else 0.dp)) {
            MainNavGraph(
                navController = navController,
            )
        }
    }
}

@Composable
fun PokedexBottomBar(
    bottomBarState: Boolean,
    currentDestination: NavDestination?,
    navigateToDestination: (String) -> Unit
) {

    AnimatedVisibility(visible = bottomBarState,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.shadow(8.dp)
            ) {
                var dividerRatio by remember { mutableFloatStateOf(1f) }

                MainDestinations.entries.forEach { dest ->
                    val selected = currentDestination?.route === dest.route
                    NavigationBarItem(
                        icon = { Icon(imageVector = dest.icon, contentDescription = null) },
                        label = {
                            Text(
                                text = stringResource(id = dest.title),
                                textAlign = TextAlign.Center,
                                onTextLayout = {
                                    if (it.lineCount > 1 || it.didOverflowWidth) {
                                        dividerRatio = dividerRatio.times(1.1f)
                                    }
                                },
                                fontSize = MaterialTheme.typography.labelMedium.fontSize.div(
                                    dividerRatio
                                ),
                            )
                        },
                        alwaysShowLabel = true,
                        selected = selected,
                        onClick = {
                            if (currentDestination?.route != dest.route) {
                                navigateToDestination(dest.route)
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = MaterialTheme.colorScheme.primary,
                            selectedIconColor = MaterialTheme.colorScheme.background,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                    )
                }
            }
        }
    )
}

fun NavHostController.handleBottomNavigationToScreen(route: String) = navigate(route) {
    popUpTo(MainDestinations.HOME.route)
    launchSingleTop = true
}