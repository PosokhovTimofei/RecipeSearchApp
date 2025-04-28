package com.example.recipesearchapp

data class RecipeResponse(
    val results: List<Recipe>
)

data class Recipe(
    val id: Int,
    val title: String,
    val image: String
)

data class RecipeInformation(
    val id: Int,
    val title: String,
    val image: String,
    val cuisines: List<String>,
    val ingredients: List<Ingredient>,
    val instructions: String,
    val readyInMinutes: Int,
    val servings: Int,
    val diet: String?,
    val healthScore: Float
)

data class Ingredient(
    val name: String,
    val amount: Float,
    val unit: String
)
