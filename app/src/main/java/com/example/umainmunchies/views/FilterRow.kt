package com.example.umainmunchies.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.umainmunchies.R
import com.example.umainmunchies.viewmodels.FilterViewModel

val googleFontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val poppinsFont = GoogleFont("Poppins")

val poppinsFontFamily = FontFamily(
    Font(googleFont = poppinsFont, fontProvider = googleFontProvider)
)

@Composable
fun FiltersRow(
    viewModel: FilterViewModel
) {
    val filters by viewModel.filters.collectAsState()
    val toggledFilterIds by viewModel.toggledFilterIds.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LazyRow(
        modifier = Modifier.fillMaxWidth(0.95f),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (isLoading && filters.isEmpty()) {
            item {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    Text(
                        text = "Loading filters...",
                        modifier = Modifier.padding(start = 8.dp),
                        fontFamily = poppinsFontFamily
                    )
                }
            }
        } else {
            items(filters) { filter ->
                val isToggled = toggledFilterIds.contains(filter.id)

                Button(
                    onClick = {
                        viewModel.toggleFilter(filter.id)
                    },
                    colors = if(isToggled) {
                        ButtonDefaults.buttonColors(
                            containerColor = Color.LightGray,
                            contentColor = Color.Black
                        )
                    } else {
                        ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    },
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 2.dp
                    ),
                    modifier = Modifier
                        .padding(8.dp)
                        .height(48.dp),
                    contentPadding = PaddingValues(start = 0.dp, end = 20.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = filter.imageUrl, // Note: camelCase property name from domain entity
                            contentDescription = null,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = filter.name,
                            fontFamily = poppinsFontFamily,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}