package com.example.recipesearchapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.recipesearchapp.ui.theme.RecipeSearchAppTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeSearchAppTheme {
                Navigation()
            }
        }
    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    NavHost(navController = navController, startDestination = "greeting") {
        composable("greeting") { GreetingScreen(navController) }
        composable("login") { LoginScreen(navController, authViewModel) }
        composable("search") { SearchScreen(navController) }
        composable("register") { RegisterScreen(navController, authViewModel) }
    }
}

@Composable
fun GreetingScreen(navController: NavController) {
    val buttonModifier = Modifier
        .width(200.dp)
        .height(40.dp)

    val loginColors = ButtonDefaults.buttonColors(
        containerColor = Color(0xFF16b88c),
        contentColor = Color(0xfff5e5e9)
    )

    val registerColors = ButtonDefaults.buttonColors(
        containerColor = Color(0xfff5e5e9),
        contentColor = Color(0xff000000)
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Фон
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Поиск рецептов",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .padding(bottom = 100.dp)
                .offset(y = (-50).dp)
        )

        Button(onClick = { navController.navigate("login") },
            modifier = buttonModifier
                .shadow(4.dp, shape = RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp)),
            colors = loginColors

        ) {
            Text("Войти")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { navController.navigate("register") },
            modifier = buttonModifier
                .shadow(4.dp, shape = RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp)),
            colors = registerColors
        ) {
            Text("Регистрация")
        }
    }
}
}

@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel) {

    val buttonModifier = Modifier
        .width(200.dp)
        .height(40.dp)

    val loginColors = ButtonDefaults.buttonColors(
        containerColor = Color(0xFF16b88c),
        contentColor = Color(0xfff5e5e9)
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Фон
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        IconButton(
            onClick = { navController.navigate("greeting") },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Назад",
                tint = Color.DarkGray
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 120.dp), // отступ от верха
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Вход",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.DarkGray,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Username
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Username or Email") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Password") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null)
                },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Remember + Forgot
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = false, onCheckedChange = {})
                    Text("Запомнить меня", fontSize = 14.sp)
                }

                Text(
                    text = "Забыли пароль?",
                    color = Color(0xFF1A8F84),
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { /* обработка */ }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sign In Button
            Button(
                onClick = { navController.navigate("search") },
                modifier = buttonModifier
                    .shadow(4.dp, shape = RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp)),
                colors = loginColors
            ) {
                Text("Войти", color = Color.White)
            }

            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}

@Composable
fun RegisterScreen(navController: NavController, authViewModel: AuthViewModel) {
    val buttonModifier = Modifier
        .width(200.dp)
        .height(40.dp)

    val loginColors = ButtonDefaults.buttonColors(
        containerColor = Color(0xFF16b88c),
        contentColor = Color(0xfff5e5e9)
    )

    // Поля ввода
    val fullName = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }

    // Для показа сообщений
    val context = LocalContext.current

    // Наблюдаем за результатом регистрации
    val registerResult by authViewModel.registerResult.observeAsState()

    // Проверка результата и навигация при успехе
    LaunchedEffect(registerResult) {
        registerResult?.let {
            if (it.isSuccess) {
                Toast.makeText(context, "Регистрация успешна", Toast.LENGTH_SHORT).show()
                navController.navigate("home")
            } else {
                Toast.makeText(context, "Ошибка: ${it.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Фон
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Назад
        IconButton(
            onClick = { navController.navigate("greeting") },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.DarkGray
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 120.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Создание аккаунта",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.DarkGray,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = fullName.value,
                onValueChange = { fullName.value = it },
                label = { Text("Fullname") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmPassword.value,
                onValueChange = { confirmPassword.value = it },
                label = { Text("Confirm Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // Простая проверка
                    if (password.value != confirmPassword.value) {
                        Toast.makeText(context, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
                    } else if (email.value.isBlank() || fullName.value.isBlank() || password.value.isBlank()) {
                        Toast.makeText(context, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
                    } else {
                        authViewModel.registerUser(
                            username = fullName.value,
                            email = email.value,
                            password = password.value
                        )
                    }
                },
                modifier = buttonModifier
                    .shadow(4.dp, shape = RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp)),
                colors = loginColors
            ) {
                Text("Зарегистрироваться", color = Color.White)
            }

            Spacer(modifier = Modifier.height(36.dp))

            Text("Уже есть аккаунт?", fontSize = 14.sp)
            Text(
                text = "Войти.",
                color = Color(0xFF1A8F84),
                fontSize = 14.sp,
                modifier = Modifier.clickable { navController.navigate("login") }
            )
        }
    }
}



@Composable
fun SearchScreen(navController: NavController) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("profile") },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Профиль") },
                    label = { Text("Профиль") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { /* Уже на поиске */ },
                    icon = { Icon(Icons.Default.Search, contentDescription = "Поиск") },
                    label = { Text("Поиск") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("favorites") },
                    icon = { Icon(Icons.Default.FavoriteBorder, contentDescription = "Избранное") },
                    label = { Text("Избранное") }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Поиск",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Поиск по рецептам и материалам") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Навигация к фильтрам */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF6F6F6))
            ) {
                Icon(Icons.Default.Tune, contentDescription = null, tint = Color.Black)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Все фильтры", color = Color.Black)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Категории
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                val categories = listOf(
                    "Первые блюда" to R.drawable.soup,
                    "Вторые блюда" to R.drawable.pasta,
                    "Салаты" to R.drawable.salad,
                    "Закуски" to R.drawable.snacks,
                    "Выпечка" to R.drawable.baking,
                    "Соусы и маринады" to R.drawable.sauces,
                    "Заготовки" to R.drawable.preserves,     // добавь соответствующие картинки
                    "Напитки" to R.drawable.drinks,
                    "Десерты" to R.drawable.desserts,
                    "Гарниры" to R.drawable.sides
                )

                items(categories.size) { index ->
                    val (title, imageRes) = categories[index]
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.LightGray)
                            .clickable { /* Перейти в категорию */ }
                            .height(140.dp)
                    ) {
                        Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.4f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(title, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}




