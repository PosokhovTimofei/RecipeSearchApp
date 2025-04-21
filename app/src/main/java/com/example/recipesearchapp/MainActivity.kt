package com.example.recipesearchapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.recipesearchapp.ui.theme.RecipeSearchAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeSearchAppTheme {  // Тема приложения
                Navigation()  // Навигация между экранами
            }
        }
    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
//        composable("register") { RegisterScreen(navController) }
//        composable("search") { RecipeSearchScreen() }
    }
}

@Composable
fun LoginScreen(navController: NavController) {
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
                .padding(bottom = 100.dp) // Только этот текст сдвигается вниз
                .offset(y = (-50).dp)     // Дополнительное смещение вверх/вниз
        )

        Button(onClick = { navController.navigate("search") },
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
