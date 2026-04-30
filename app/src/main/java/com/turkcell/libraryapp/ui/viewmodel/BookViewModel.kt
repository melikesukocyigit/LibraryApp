package com.turkcell.libraryapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.libraryapp.data.model.Book
import com.turkcell.libraryapp.data.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class BookViewModel : ViewModel() {
    private val repository = BookRepository()

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

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
    fun addBook(title: String, author: String, category: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val newBook = Book(
                id = java.util.UUID.randomUUID().toString(), // Benzersiz ID üretiyoruz
                title = title,
                author = author,
                category = category,
                pageCount = 0,
                totalCopies = 1,
                avaiableCopies = 1
            )
            repository.addBook(newBook).onSuccess { loadBooks() }
            _isLoading.value = false
        }
    }
}