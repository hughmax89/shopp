import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.aguerodev.shopp.view.core.CountryColorScheme
import com.aguerodev.shopp.view.core.bottomDestinations

@Composable
fun BottomBarNavigation(
    navController: NavHostController,
    colorScheme: CountryColorScheme
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    NavigationBar(
        containerColor = colorScheme.primaryColor
    ) {
        bottomDestinations.forEach { item ->
            val selected = currentDestination.isRouteInHierarchy(item.route)

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title
                    )
                },
                label = {
                    // 💡 Aplicar el color de texto del país
                    Text(text = item.title)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colorScheme.onPrimaryColor,
                    unselectedIconColor = colorScheme.onPrimaryColor.copy(alpha = 0.7f),
                    indicatorColor = colorScheme.onPrimaryColor.copy(alpha = 0.2f),
                    selectedTextColor = colorScheme.onPrimaryColor,
                    unselectedTextColor = colorScheme.onPrimaryColor.copy(alpha = 0.7f),
                )
            )
        }
    }
}

private fun NavDestination?.isRouteInHierarchy(route: String): Boolean {
    return this?.hierarchy?.any { it.route == route } == true
}