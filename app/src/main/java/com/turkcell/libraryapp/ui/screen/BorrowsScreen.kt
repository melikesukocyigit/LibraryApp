package com.turkcell.libraryapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.turkcell.libraryapp.ui.viewmodel.AuthViewModel
import com.turkcell.libraryapp.ui.viewmodel.BookViewModel

@Composable
fun BorrowsScreen(
    authViewModel: AuthViewModel,
    bookViewModel: BookViewModel,
    onNavigateBack: () -> Unit // Geri dönüş fonksiyonu eklendi
) {
    val profileState by authViewModel.profile.collectAsState()
    val borrowRecords by bookViewModel.borrowRecords.collectAsState()
    val allBooks by bookViewModel.books.collectAsState()
    val isLoading by bookViewModel.isLoading.collectAsState()

    LaunchedEffect(profileState?.userId) {
        profileState?.userId?.let { studentId ->
            bookViewModel.loadBorrowRecords(studentId)
        }
    }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        // --- ÜST BAR (Geri Butonu ve Başlık) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // GERİ DÖN BUTONU
                Button(
                    onClick = { onNavigateBack() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text("< Geri", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Kiralamalarım",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        // --- LİSTE KISMI ---
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else if (borrowRecords.isEmpty()) {
                Text(text = "Henüz hiç kitap ödünç almadınız.", color = Color.Gray)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(borrowRecords) { record ->
                        val borrowedBook = allBooks.find { it.id == record.bookId }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color.LightGray)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = borrowedBook?.title ?: "Bilinmeyen Kitap",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Kiralama: ${record.borrowedAt.take(10)}",
                                fontSize = 14.sp,
                                color = Color.DarkGray
                            )
                            Text(
                                text = "Son Teslim: ${record.dueDate.take(10)}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}