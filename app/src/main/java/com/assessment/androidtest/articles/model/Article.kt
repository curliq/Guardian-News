package com.assessment.androidtest.articles.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Article(
        val id: String,
        val thumbnail: String,
        val sectionId: String,
        val sectionName: String,
        val published: Date,
        val title: String,
        val body: String,
        val url: String,
        var isFavourite: Boolean,
        var dividerText: String
) : Parcelable