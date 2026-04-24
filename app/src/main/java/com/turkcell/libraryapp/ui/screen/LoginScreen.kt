package com.turkcell.libraryapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.turkcell.libraryapp.ui.navigation.Screen
import com.turkcell.libraryapp.ui.viewmodel.AuthState
import com.turkcell.libraryapp.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.authState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Kütüphane Girişi",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            enabled = authState !is AuthState.Loading,
            modifier = Modifier.fillMaxWidth(),
            value = email,
            label = { Text("E-posta") },
            onValueChange = { email = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            enabled = authState !is AuthState.Loading,
            modifier = Modifier.fillMaxWidth(),
            value = password,
            label = { Text("Şifre") },
            onValueChange = { password = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Giriş Butonu ve Loading Durumu
        if (authState is AuthState.Loading) {
            Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        } else {
            Button(
                onClick = { authViewModel.signIn(email, password) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Giriş Yap")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Kayıt Ol Yönlendirmesi
        TextButton(
            onClick = { navController.navigate(Screen.Register.route) },
            enabled = authState !is AuthState.Loading
        ) {
            Text("Hesabın yok mu? Kayıt Ol")
        }

        // Durum Mesajları
        Spacer(modifier = Modifier.height(16.dp))

        when (authState) {
            is AuthState.Success -> {
                Text("Giriş Başarılı!", color = MaterialTheme.colorScheme.primary)
            }
            is AuthState.Error -> {
                Text(
                    text = (authState as AuthState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }
            else -> {}
        }
    }
}