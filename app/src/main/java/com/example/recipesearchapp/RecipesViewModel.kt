import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesearchapp.Recipe
import com.example.recipesearchapp.SpoonacularClient
import kotlinx.coroutines.launch

class RecipesViewModel : ViewModel() {
    private val _recipes = mutableStateOf<List<Recipe>>(emptyList())
    val recipes: List<Recipe> get() = _recipes.value

    private val _isLoading = mutableStateOf(false)
    val isLoading: Boolean get() = _isLoading.value

    // Метод для очистки результатов
    fun clearResults() {
        _recipes.value = emptyList()
    }

    // Метод для поиска рецептов по названию
    fun searchRecipes(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _recipes.value = SpoonacularClient.api.searchRecipes(query).results
            } catch (e: Exception) {
                _recipes.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Метод для загрузки рецептов по категории
    // Метод для загрузки рецептов по категории
    fun loadCategoryRecipes(category: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _recipes.value = SpoonacularClient.api.getRecipesByCategory(category).results
            } catch (e: Exception) {
                _recipes.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }


    // Метод для поиска рецептов внутри категории
    fun searchInCategory(category: String, query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _recipes.value = SpoonacularClient.api.searchRecipesInCategory(category, query).results
            } catch (e: Exception) {
                _recipes.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}





