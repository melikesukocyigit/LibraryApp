package com.turkcell.libraryapp.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.turkcell.libraryapp.data.model.Book

@Composable
fun BookCard(
    book: Book,
    onDelete: (String) -> Unit,
    onUpdate: (Book) -> Unit,
    onBorrow: (Book) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var editedTitle by remember { mutableStateOf(book.title) }
    var editedAuthor by remember { mutableStateOf(book.author) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray)
            .padding(16.dp)
    ) {
        if (isEditing) {
            // DÜZENLEME FORMU
            OutlinedTextField(
                value = editedTitle,
                onValueChange = { editedTitle = it },
                label = { Text("Kitap Adı") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = editedAuthor,
                onValueChange = { editedAuthor = it },
                label = { Text("Yazar Adı") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { isEditing = false }) {
                    Text("İptal")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        onUpdate(book.copy(title = editedTitle, author = editedAuthor))
                        isEditing = false
                    }
                ) {
                    Text("Kaydet")
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = book.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Yazar: ${book.author}", fontSize = 14.sp, color = Color.Gray)
                }
                Row {
                    OutlinedButton(
                        onClick = { isEditing = true },
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Text(text = "Düzenle")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedButton(
                        onClick = { onDelete(book.id) },
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Text(text = "Sil")
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically // Buton ile yazıyı hizalamak için eklendi
            ) {
                Text(text = book.category, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)

                if (book.avaiableCopies > 0) {
                    Button(
                        onClick = { onBorrow(book) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)), // Şık bir yeşil tonu
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(text = "ÖDÜNÇ AL", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Text(
                        text = "STOKTA YOK",
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                }
            }
        }
    }
}