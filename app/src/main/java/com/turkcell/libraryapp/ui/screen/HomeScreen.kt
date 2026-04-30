package com.turkcell.libraryapp.ui.screen

import androidx.compose.foundation.background
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
fun HomeScreen(
    authViewModel: AuthViewModel,
    bookViewModel: BookViewModel
) {
    val books by bookViewModel.books.collectAsState()
    val isLoading by bookViewModel.isLoading.collectAsState()
    val profileState by authViewModel.profile.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    var isAddingBook by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf("") }
    var newAuthor by remember { mutableStateOf("") }
    var newCategory by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {

        // --- ÜST BAR (Başlık ve Ekle Butonu) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Kütüphane",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    profileState?.let {
                        Text(
                            text = "Hoş geldin, ${it.fullName}",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                // + Ekle / İptal Butonu
                Button(
                    onClick = { isAddingBook = !isAddingBook },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text(text = if (isAddingBook) "İptal" else "+ Ekle")
                }
            }
        }

        // --- YENİ KİTAP EKLEME FORMU ---
        if (isAddingBook) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = newTitle,
                    onValueChange = { newTitle = it },
                    label = { Text("Kitap Adı") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = newAuthor,
                    onValueChange = { newAuthor = it },
                    label = { Text("Yazar Adı") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = newCategory,
                    onValueChange = { newCategory = it },
                    label = { Text("Kategori") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (newTitle.isNotBlank() && newAuthor.isNotBlank()) {
                            bookViewModel.addBook(newTitle, newAuthor, newCategory)
                            isAddingBook = false // Formu kapat
                            newTitle = "" // Alanları sıfırla
                            newAuthor = ""
                            newCategory = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Kitabı Kaydet")
                }
            }
        }

        // --- ARAMA ÇUBUĞU ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    bookViewModel.searchBooks(it)
                },
                placeholder = { Text("Kitap ara...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        // --- LİSTE VE YÜKLEME KISMI ---
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else if (books.isEmpty()) {
                Text(
                    text = "Sonuç bulunamadı.",
                    color = Color.Gray
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(books) { book ->
                        BookCard(
                            book = book,
                            onDelete = { id -> bookViewModel.deleteBook(id) },
                            onUpdate = { updatedBook -> bookViewModel.updateBook(updatedBook) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}