package com.turkcell.libraryapp.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.turkcell.libraryapp.ui.viewmodel.AuthState
import com.turkcell.libraryapp.ui.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(onNavigateToLogin: () -> Unit) {
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.authState.collectAsState()
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Başarı durumunda otomatik yönlendirme
    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            Toast.makeText(context, (authState as AuthState.Success).message, Toast.LENGTH_SHORT).show()
            authViewModel.resetState() // Durumu sıfırla ki döngüye girmesin
            onNavigateToLogin()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Yeni Hesap Oluştur", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-posta") },
            modifier = Modifier.fillMaxWidth(),
            enabled = authState !is AuthState.Loading
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Şifre") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            enabled = authState !is AuthState.Loading
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Şifre Tekrar") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            enabled = authState !is AuthState.Loading
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (authState is AuthState.Loading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    if (password != confirmPassword) {
                        Toast.makeText(context, "Şifreler uyuşmuyor!", Toast.LENGTH_SHORT).show()
                    } else if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(context, "Lütfen tüm alanları doldurun!", Toast.LENGTH_SHORT).show()
                    } else {
                        authViewModel.signUp(email, password)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Kayıt Ol")
            }
        }

        TextButton(onClick = onNavigateToLogin) {
            Text("Zaten hesabın var mı? Giriş Yap")
        }
    }
}