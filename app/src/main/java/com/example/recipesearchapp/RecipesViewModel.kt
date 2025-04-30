import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesearchapp.Recipe
import com.example.recipesearchapp.RecipeInformation
import com.example.recipesearchapp.SpoonacularClient
import kotlinx.coroutines.launch

class RecipesViewModel : ViewModel() {
    private val _recipeInformation = mutableStateOf<RecipeInformation?>(null)
    val recipeInformation: RecipeInformation? get() = _recipeInformation.value

    private val _recipes = mutableStateOf<List<Recipe>>(emptyList())
    val recipes: List<Recipe> get() = _recipes.value

    private val _isLoading = mutableStateOf(false)
    val isLoading: Boolean get() = _isLoading.value

    private val _favorites = mutableStateListOf<Recipe>()
    val favorites: List<Recipe> = _favorites

    fun toggleFavorite(recipe: Recipe) {
        if (_favorites.any { it.id == recipe.id }) {
            _favorites.removeAll { it.id == recipe.id }
        } else {
            _favorites.add(recipe)
        }
    }

    fun isFavorite(recipe: Recipe): Boolean {
        return _favorites.any { it.id == recipe.id }
    }

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

    fun loadRecipeInformation(recipeId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val recipeInfo = SpoonacularClient.api.getRecipeInformation(recipeId)
                _recipeInformation.value = recipeInfo
            } catch (e: Exception) {
                _recipeInformation.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

}








