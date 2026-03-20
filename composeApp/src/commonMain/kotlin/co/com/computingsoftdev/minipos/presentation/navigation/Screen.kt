package co.com.computingsoftdev.minipos.presentation.navigation

sealed class Screen {
    object Products : Screen()
    object AddProduct : Screen()
    object Sale : Screen()
    object Reports : Screen()
}