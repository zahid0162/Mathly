package com.zahid.mathly.utils


import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.composable

private const val DefaultDuration = 300

/**
 * Horizontal slide:
 * - Forward (navigate): new enters from RIGHT
 * - Back (pop): current exits to RIGHT
 */
fun NavGraphBuilder.horizontalAnimatedComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    durationMs: Int = DefaultDuration,
    easing: Easing = FastOutSlowInEasing,
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left, // new screen comes from RIGHT
                animationSpec = tween(durationMs, easing = easing)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left, // old goes LEFT on push
                animationSpec = tween(durationMs, easing = easing)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right, // on back, re-enter from LEFT
                animationSpec = tween(durationMs, easing = easing)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right, // on back, exit to RIGHT (your ask)
                animationSpec = tween(durationMs, easing = easing)
            )
        }
    ) { backStackEntry ->
        content(backStackEntry)
    }
}

/**
 * Vertical slide:
 * - Forward (navigate): new enters from BOTTOM
 * - Back (pop): current exits to BOTTOM
 */
fun NavGraphBuilder.verticalAnimatedComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    durationMs: Int = DefaultDuration,
    easing: Easing = FastOutSlowInEasing,
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Up, // new screen comes from BOTTOM
                animationSpec = tween(durationMs, easing = easing)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Up, // old goes UP on push
                animationSpec = tween(durationMs, easing = easing)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Down, // on back, re-enter from TOP
                animationSpec = tween(durationMs, easing = easing)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Down, // on back, exit to BOTTOM (your ask)
                animationSpec = tween(durationMs, easing = easing)
            )
        }
    ) { backStackEntry ->
        content(backStackEntry)
    }
}
