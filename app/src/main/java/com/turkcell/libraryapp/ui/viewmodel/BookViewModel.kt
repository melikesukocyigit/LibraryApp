package com.turkcell.libraryapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.libraryapp.data.model.Book
import com.turkcell.libraryapp.data.model.BorrowRecord
import com.turkcell.libraryapp.data.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class BookViewModel : ViewModel() {
    private val repository = BookRepository()

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _borrowRecords = MutableStateFlow<List<BorrowRecord>>(emptyList())
    val borrowRecords: StateFlow<List<BorrowRecord>> = _borrowRecords

    init {
        loadBooks()
    }

    fun loadBooks() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getAllBooks()
                .onSuccess { _books.value = it }
                .onFailure { _error.value = it.message }
            _isLoading.value = false
        }
    }

    fun searchBooks(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            if (query.isBlank()) {
                loadBooks()
            } else {
                repository.searchBooks(query)
                    .onSuccess { _books.value = it }
                    .onFailure { _error.value = it.message }
            }
            _isLoading.value = false
        }
    }

    fun deleteBook(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.deleteBook(id).onSuccess { loadBooks() }
            _isLoading.value = false
        }
    }

    fun updateBook(updatedBook: Book) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.updateBook(updatedBook.id, updatedBook).onSuccess { loadBooks() }
            _isLoading.value = false
        }
    }

    // Kitap Ekleme Fonksiyonu
    fun addBook(title: String, author: String, category: String, stock: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val newBook = Book(
                id = java.util.UUID.randomUUID().toString(),
                title = title,
                author = author,
                category = category,
                pageCount = 0,
                totalCopies = stock,       // Kullanıcının girdiği stok
                avaiableCopies = stock     // Başlangıçta tamamı kütüphanede
            )
            repository.addBook(newBook).onSuccess { loadBooks() }
            _isLoading.value = false
        }
    }

    fun borrowBook(book: Book, studentId: String) {
        viewModelScope.launch {
            _isLoading.value = true

            repository.getBorrowRecordsByUserId(studentId).onSuccess { records ->

                val alreadyBorrowed = records.any { it.bookId == book.id && it.returnedAt == null }

                if (alreadyBorrowed) {
                    _error.value = "Bu kitabı zaten ödünç aldınız!"
                    _isLoading.value = false
                    return@launch // Fonksiyondan çık, aşağıdaki işlemleri yapma
                }

                val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                val calendar = java.util.Calendar.getInstance()
                val borrowedAt = sdf.format(calendar.time) // Bugün

                calendar.add(java.util.Calendar.DAY_OF_YEAR, 5)
                val dueDate = sdf.format(calendar.time) // 5 Gün Sonra

                val record = BorrowRecord(
                    id = java.util.UUID.randomUUID().toString(),
                    studentId = studentId,
                    bookId = book.id,
                    borrowedAt = borrowedAt,
                    dueDate = dueDate,
                    returnedAt = null
                )

                repository.addBorrowRecord(record).onSuccess {
                    val updatedBook = book.copy(avaiableCopies = book.avaiableCopies - 1)
                    repository.updateBook(updatedBook.id, updatedBook).onSuccess {
                        loadBooks()
                    }
                }
            }
            _isLoading.value = false
        }
    }

    // Öğrencinin kiraladığı kitapları yükle
    fun loadBorrowRecords(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getBorrowRecordsByUserId(userId)
                .onSuccess { _borrowRecords.value = it }
                .onFailure { _error.value = it.message }
            _isLoading.value = false
        }
    }
    // Uyarı penceresi kapatıldığında hata mesajını sıfırlar
    fun clearError() {
        _error.value = null
    }
}