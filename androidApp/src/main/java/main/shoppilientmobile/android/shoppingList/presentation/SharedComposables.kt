package main.shoppilientmobile.android.shoppingList.presentation

import androidx.compose.foundation.clickable
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

private val appBarTitle = "Shopping List"

@Composable
fun DefaultApplicationTopBar() {
    TopAppBar(
        title = {
            Text(text = appBarTitle)
        },
    )
}

@Composable
fun DeletionApplicationTopBar(
    modifier: Modifier = Modifier,
    onClickOnDeletionIcon: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(text = appBarTitle)
        },
        actions = {
            IconButton(
                modifier = Modifier.testTag("DeletionIcon"),
                onClick = onClickOnDeletionIcon,
            ) {
                DeletionIcon()
            }
        }
    )
}

@Composable
private fun DeletionIcon(
    modifier: Modifier = Modifier,
) {
    Icon(
        modifier = modifier,
        imageVector = Icons.Default.Clear,
        contentDescription = "Delete icon"
    )
}