import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.bindToBrowserNavigation
import androidx.navigation.compose.rememberNavController
import com.safetymarcus.mygroceries.presentation.ui.MainView
import org.jetbrains.compose.resources.configureWebResources

@OptIn(ExperimentalComposeUiApi::class, ExperimentalBrowserHistoryApi::class)
fun main() {
    configureWebResources {
        resourcePathMapping { path -> "./$path" }
    }
    ComposeViewport("composeApp") {
        val nav = rememberNavController()
        LaunchedEffect(Unit) {
            nav.bindToBrowserNavigation()
        }
        MainView(nav)
    }
}
