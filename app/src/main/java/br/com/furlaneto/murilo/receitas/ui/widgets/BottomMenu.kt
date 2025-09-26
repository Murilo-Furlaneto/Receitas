package br.com.furlaneto.murilo.receitas.ui.widgets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
fun BottomMenu(
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    BottomAppBar(
        containerColor = Color(0xFF607D8B) ,
        contentColor = Color.White
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = { onItemSelected("home") },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = if (selectedItem == "home") Color.White else Color.LightGray
                )
            }
            IconButton(
                onClick = { onItemSelected("categories") },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = "Categorias",
                    tint = if (selectedItem == "categorias") Color.White else Color.LightGray
                )
            }
        }
    }
}