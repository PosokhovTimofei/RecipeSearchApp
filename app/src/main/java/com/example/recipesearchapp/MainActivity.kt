package com.example.recipesearchapp

import RecipesViewModel
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.recipesearchapp.ui.theme.RecipeSearchAppTheme
import androidx.compose.animation.animateContentSize


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
    val recipesViewModel: RecipesViewModel = viewModel() // ✅ ОДИН экземпляр на все экраны

    NavHost(navController = navController, startDestination = "greeting") {
        composable("greeting") {
            GreetingScreen(navController)
        }
        composable("login") {
            LoginScreen(navController, authViewModel)
        }
        composable("register") {
            RegisterScreen(navController, authViewModel)
        }
        composable("search") {
            SearchScreen(navController, recipesViewModel) // ✅ передаём тот же viewModel
        }
        composable("favorites") {
            FavoritesScreen(navController, recipesViewModel) // ✅ тот же viewModel
        }
        composable("category/{categoryName}") { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            CategoryRecipesScreen(
                navController = navController,
                categoryName = categoryName,
                recipesViewModel = recipesViewModel // ← всё ок
            )
        }

        composable("recipeDetail/{recipeId}") { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId")?.toIntOrNull() ?: 0
            RecipeDetailScreen(navController = navController, recipeId = recipeId, recipesViewModel = recipesViewModel)
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
    recipesViewModel: RecipesViewModel
) {
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(searchQuery) {
        if (searchQuery.length > 2) {
            recipesViewModel.searchRecipes(searchQuery)
        } else {
            recipesViewModel.clearResults()
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
                    Icon(Icons.Default.Tune, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Все фильтры")
                }

                Spacer(modifier = Modifier.height(16.dp))

                when {
                    recipesViewModel.isLoading -> {
                        LoadingIndicator()
                    }
                    searchQuery.isNotEmpty() && recipesViewModel.recipes.isEmpty() -> {
                        EmptyResults()
                    }
                    searchQuery.isNotEmpty() -> {
                        RecipesList(recipesViewModel.recipes, navController, recipesViewModel)
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
fun RecipesList(
    recipes: List<Recipe>,
    navController: NavController,
    recipesViewModel: RecipesViewModel,
    contentPadding: PaddingValues = PaddingValues(0.dp) // по умолчанию пусто
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding, // учёт отступов от Scaffold
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(recipes) { recipe ->
            RecipeItem(
                recipe = recipe,
                onClick = { navController.navigate("recipeDetail/${recipe.id}") },
                recipesViewModel = recipesViewModel
            )
        }
    }
}

@Composable
fun RecipeItem(
    recipe: Recipe,
    onClick: () -> Unit,
    recipesViewModel: RecipesViewModel
) {
    val isFavorite = recipesViewModel.isFavorite(recipe)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = recipe.title,
                    modifier = Modifier.weight(1f), // Занимает всё свободное пространство
                    maxLines = 2, // Ограничение строк
                    overflow = TextOverflow.Ellipsis // "..." если текст не помещается
                )
                IconButton(onClick = { recipesViewModel.toggleFavorite(recipe) }) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Избранное",
                        tint = if (isFavorite) Color.Red else Color.Gray
                    )
                }
            }

            AsyncImage(
                model = recipe.image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop
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
    categoryName: String,
    recipesViewModel: RecipesViewModel
) {

    var searchQuery by remember { mutableStateOf("") }

    // Загрузка рецептов по категории при изменении categoryName
    LaunchedEffect(categoryName) {
        recipesViewModel.loadCategoryRecipes(categoryName)
    }

    // Обновление рецептов при изменении поискового запроса
    LaunchedEffect(searchQuery) {
        if (searchQuery.length > 2) {
            recipesViewModel.searchInCategory(categoryName, searchQuery)
        } else {
            recipesViewModel.loadCategoryRecipes(categoryName)
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
            },
            bottomBar = {
                BottomNavigationBar(
                    currentRoute = "category/${categoryName}",
                    onNavigate = { navController.navigate(it) }
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
                    recipesViewModel.isLoading -> {
                        LoadingIndicator()
                    }

                    recipesViewModel.recipes.isEmpty() -> {
                        EmptyResults()
                    }

                    else -> {
                        RecipesList(recipesViewModel.recipes, navController, recipesViewModel)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun IngredientItem(
    ingredient: Ingredient,
    index: Int,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn() + slideInHorizontally { (index + 1) * 50 },
        exit = fadeOut() + slideOutHorizontally { -(index + 1) * 50 }
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "• ${ingredient.name}:",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${ingredient.amount} ${ingredient.unit}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    navController: NavController,
    recipeId: Int,
    recipesViewModel: RecipesViewModel
) {
    // Загружаем информацию о рецепте при изменении recipeId
    LaunchedEffect(recipeId) {
        recipesViewModel.loadRecipeInformation(recipeId)
    }

    val recipeInformation = recipesViewModel.recipeInformation
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Рецепт") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                // Если идет загрузка, показываем индикатор
                if (recipesViewModel.isLoading) {
                    LoadingIndicator()
                } else if (recipeInformation != null) {
                    RecipeDetailContent(recipeInformation)
                } else {
                    EmptyResults()  // Если информации нет, показываем сообщение
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RecipeDetailContent(
    recipeInformation: RecipeInformation,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
            .animateContentSize()
    ) {
        // Анимированное появление заголовка
        AnimatedVisibility(
            visible = true,
            enter = fadeIn() + slideInVertically { it / 2 },
            exit = fadeOut() + slideOutVertically { -it / 2 }
        ) {
            Text(
                text = recipeInformation.title ?: "Без названия",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        // Анимированное изображение
        AnimatedVisibility(
            visible = true,
            enter = fadeIn() + scaleIn(initialScale = 0.9f),
            exit = fadeOut() + scaleOut(targetScale = 0.9f)
        ) {
            AsyncImage(
                model = recipeInformation.image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Анимированные метрики рецепта
        RecipeMetrics(
            time = recipeInformation.readyInMinutes,
            servings = recipeInformation.servings,
            healthScore = recipeInformation.healthScore
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Ингредиенты с последовательной анимацией
        AnimatedVisibility(
            visible = !recipeInformation.ingredients.isNullOrEmpty(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column {
                Text(
                    text = "Ингредиенты:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                recipeInformation.ingredients?.forEachIndexed { index, ingredient ->
                    key(ingredient.name) {
                        IngredientItem(
                            ingredient = ingredient,
                            index = index,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Инструкции с плавным появлением
        recipeInformation.instructions?.let { instructions ->
            AnimatedVisibility(
                visible = instructions.isNotBlank(),
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    Text(
                        text = "Инструкции:",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    HtmlText(
                        html = instructions,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
        }

        // Дополнительная информация
        recipeInformation.cuisines?.let { cuisines ->
            if (cuisines.isNotEmpty()) {
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + slideInHorizontally { it },
                    exit = fadeOut() + slideOutHorizontally { -it }
                ) {
                    Column(modifier = Modifier.padding(top = 12.dp)) {
                        Text(
                            text = "Типы кухни:",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        cuisines.forEach { cuisine ->
                            Text(
                                text = "• $cuisine",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    navController: NavController,
    recipesViewModel: RecipesViewModel
) {
    val favorites = recipesViewModel.favorites
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(title = { Text("Избранное") })
            },
            bottomBar = {
                BottomNavigationBar(
                    currentRoute = "favorites",
                    onNavigate = { navController.navigate(it) }
                )
            }
        ) { padding ->
            if (favorites.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Нет избранных рецептов.")
                }
            } else {
                RecipesList(
                    recipes = favorites,
                    navController = navController,
                    recipesViewModel = recipesViewModel,
                    contentPadding = padding
                )
            }
        }
    }
}


@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == "profile",
            onClick = { onNavigate("profile") },
            icon = { Icon(Icons.Default.Person, contentDescription = "Профиль") },
            label = { Text("Профиль") }
        )
        NavigationBarItem(
            selected = currentRoute == "search",
            onClick = { onNavigate("search") },
            icon = { Icon(Icons.Default.Search, contentDescription = "Поиск") },
            label = { Text("Поиск") }
        )
        NavigationBarItem(
            selected = currentRoute == "favorites",
            onClick = { onNavigate("favorites") },
            icon = { Icon(Icons.Default.FavoriteBorder, contentDescription = "Избранное") },
            label = { Text("Избранное") }
        )
    }
}

@Composable
fun HtmlText(html: String, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            TextView(context).apply {
                text = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
            }
        }
    )
}

@Composable
fun AnimatedCounter(
    targetValue: Number,  // Принимаем любой числовой тип
    style: TextStyle,
    duration: Int = 1000
) {
    val animatedValue by animateFloatAsState(  // Используем animateFloatAsState вместо animateIntAsState
        targetValue = targetValue.toFloat(),
        animationSpec = tween(duration, easing = LinearEasing),
        label = "counterAnimation"
    )

    Text(
        text = "%.0f".format(animatedValue),  // Форматируем без десятичных знаков
        style = style
    )
}

@Composable
fun AnimatedIcon(
    icon: ImageVector,
    contentDescription: String? = null
) {
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(500),
        label = "iconAnimation"
    )

    Icon(
        imageVector = icon,
        contentDescription = contentDescription,
        modifier = Modifier.alpha(alpha),
        tint = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun RecipeMetrics(
    time: Int?,
    servings: Int?,
    healthScore: Float  // Изменили с Float на Int
) {
    val items = listOfNotNull(
        time?.let { Triple(Icons.Default.Timer, "Время", it) },
        servings?.let { Triple(Icons.Default.People, "Порции", it) },
        healthScore?.let { Triple(Icons.Default.MonitorHeart, "Здоровье", it) }
    )

    LazyRow(
        modifier = Modifier.padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items) { (icon, label, value) ->
            MetricItem(icon, label, value)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun MetricItem(
    icon: ImageVector,
    label: String,
    value: Number  // Изменили с Number на Int
) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedIcon(icon)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = label, style = MaterialTheme.typography.labelSmall)
            AnimatedCounter(value, MaterialTheme.typography.bodyMedium)
        }
    }
}









