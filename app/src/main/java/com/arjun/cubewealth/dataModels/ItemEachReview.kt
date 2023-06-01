package com.arjun.cubewealth.dataModels

data class ItemEachReview(
    val author: String,
    val author_details: ItemAuthorDetails,
    val content: String,
    val created_at: String,
    val id: String,
    val updated_at: String,
    val url: String
)