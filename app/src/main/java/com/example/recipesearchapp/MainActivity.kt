package com.example.recipesearchapp

import RecipesViewModel
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.recipesearchapp.ui.theme.RecipeSearchAppTheme


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

        // Для экрана категории используйте viewModel внутри экрана
        composable("category/{categoryName}") { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            CategoryRecipesScreen(
                navController = navController,
                categoryName = categoryName
            )
        }
    }
}


@Composable
fun GreetingScreen(navController: NavController) {
    val buttonModifier = Modifier
        .width(200.dp)
        .height(50.dp)

    val loginColors = ButtonDefaults.buttonColors(
        containerColor = Color(0xFF16b88c),
        contentColor = Color(0xfff5e5e9)
    )

    val registerColors = ButtonDefaults.buttonColors(
        containerColor = Color(0xfff5e5e9),
        contentColor = Color(0xff000000)
    )

    val lobsterFont = FontFamily(Font(R.font.lobster_regular))


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
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontFamily = lobsterFont
                ),
                modifier = Modifier
                    .padding(bottom = 100.dp)
                    .offset(y = (-50).dp)
            )

            AnimatedButton(
                text = "Войти",
                colors = loginColors,
                modifier = buttonModifier,
                onClick = { navController.navigate("login") }
            )

            Spacer(modifier = Modifier.height(12.dp))

            AnimatedButton(
                text = "Регистрация",
                colors = registerColors,
                modifier = buttonModifier,
                onClick = { navController.navigate("register") }
            )
        }
    }
}

@Composable
fun AnimatedButton(
    text: String,
    colors: ButtonColors,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        label = "ButtonScale"
    )

    Button(
        onClick = onClick,
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(8.dp, shape = RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp)),
        colors = colors,
        interactionSource = interactionSource,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 6.dp,
            pressedElevation = 2.dp
        )
    ) {
        Text(text)
    }
}


@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel) {

    // Состояние для текстовых полей
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }


    val buttonModifier = Modifier
        .width(200.dp)
        .height(40.dp)

    val loginColors = ButtonDefaults.buttonColors(
        containerColor = Color(0xFF16b88c),
        contentColor = Color(0xfff5e5e9)
    )

    val loginResult by authViewModel.loginResult.observeAsState()

    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(loginResult) {
        Log.d("LoginDebug", "LaunchedEffect triggered with result: $loginResult")

        loginResult?.let { result ->
            isLoading = false
            result.fold(
                onSuccess = { responseText ->
                    Log.d("LoginDebug", "Login success response: $responseText")

                    if (responseText.contains("success", ignoreCase = true)) {
                        navController.navigate("search") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        errorMessage = "Неизвестный ответ от сервера: $responseText"
                    }
                },
                onFailure = {
                    Log.d("LoginDebug", "Login failed: ${it.message}") // <-- и это
                    errorMessage = it.message ?: "Ошибка входа"
                }
            )
        }
    }

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
                value = username,
                onValueChange = { username = it },
                label = { Text("Username or Email") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
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
                    Checkbox(checked = rememberMe, onCheckedChange = { rememberMe = it })
                    Text("Запомнить меня", fontSize = 14.sp)
                }

                Text(
                    text = "Забыли пароль?",
                    color = Color(0xFF1A8F84),
                    fontSize = 14.sp,
                    modifier = Modifier.clickable {
                        // обработка клика на забытый пароль
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sign In Button
            Button(
                onClick = {
                    if (username.isBlank() || password.isBlank()) {
                        errorMessage = "Заполните все поля"
                    } else if (!isLoading) {
                        isLoading = true
                        authViewModel.loginUser(username, password)
                    }
                },
                modifier = buttonModifier
                    .shadow(4.dp, shape = RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp)),
                colors = loginColors,
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White)
                } else {
                    Text("Войти", color = Color.White)
                }
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
                navController.navigate("login")
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
fun SearchScreen(
    navController: NavController,
    viewModel: RecipesViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf("") }

    // Загрузка данных при изменении запроса
    LaunchedEffect(searchQuery) {
        if (searchQuery.length > 2) {
            viewModel.searchRecipes(searchQuery)
        } else {
            viewModel.clearResults()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Scaffold(
            containerColor = Color.Transparent,
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
                        onClick = { /* already on search */ },
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
                // Поисковая строка
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Поиск рецептов...") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = { /* TODO: открыть экран фильтров */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Все фильтры")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Контент в зависимости от состояния
                when {
                    viewModel.isLoading -> {
                        LoadingIndicator()
                    }
                    searchQuery.isNotEmpty() && viewModel.recipes.isEmpty() -> {
                        EmptyResults()
                    }
                    searchQuery.isNotEmpty() -> {
                        RecipesList(viewModel.recipes, navController)
                    }
                    else -> {
                        CategoriesGrid(navController)
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingIndicator() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyResults() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Ничего не найдено", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun RecipesList(recipes: List<Recipe>, navController: NavController) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(recipes) { recipe ->
            RecipeItem(
                recipe = recipe,
                onClick = { navController.navigate("recipe/${recipe.id}") }
            )
        }
    }
}

@Composable
private fun RecipeItem(recipe: Recipe, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            AsyncImage(
                model = recipe.image,
                contentDescription = null,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Text(
                text = recipe.title,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun CategoriesGrid(navController: NavController) {
    val categories = listOf(
        "breakfast" to R.drawable.soup,
        "lunch" to R.drawable.pasta,
        "dinner" to R.drawable.sides,
        "snack" to R.drawable.snacks,
        "dessert" to R.drawable.desserts,
        "popularity" to R.drawable.baking,
        "healthiness" to R.drawable.salad,
        "time" to R.drawable.sauces,
        "price" to R.drawable.drinks,
        "low-fat" to R.drawable.preserves
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize()
    ) {
        items(categories) { (type, imageRes) ->
            CategoryItem(
                title = type,
                imageRes = imageRes,
                onClick = { navController.navigate("category/$type") }
            )
        }
    }
}


@Composable
private fun CategoryItem(title: String, imageRes: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
    ) {
        // Картинка
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.53f)),
            contentAlignment = Alignment.Center
        ) {
            Text(title, color = Color.White)
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryRecipesScreen(
    navController: NavController,
    categoryName: String
) {
    val viewModel: RecipesViewModel = viewModel()

    var searchQuery by remember { mutableStateOf("") }

    // Загрузка рецептов по категории при изменении categoryName
    LaunchedEffect(categoryName) {
        viewModel.loadCategoryRecipes(categoryName)
    }

    // Обновление рецептов при изменении поискового запроса
    LaunchedEffect(searchQuery) {
        if (searchQuery.length > 2) {
            viewModel.searchInCategory(categoryName, searchQuery)
        } else {
            viewModel.loadCategoryRecipes(categoryName)
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text(categoryName) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Поле для ввода запроса
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Поиск в $categoryName") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { /* TODO: открыть фильтры */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Tune, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Фильтры")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Показываем разные состояния в зависимости от загрузки, ошибки или данных
            when {
                viewModel.isLoading -> {
                    LoadingIndicator()
                }
                viewModel.recipes.isEmpty() -> {
                    EmptyResults()
                }
                else -> {
                    RecipesList(viewModel.recipes, navController)
                }
            }
        }
    }
}











