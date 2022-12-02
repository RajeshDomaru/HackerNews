# HackerNews
-> This repository contains a sample app that lists out the top stories of HackerNews.
-> That data from API implemented by Dagger-Hilt, Retrofit and Room database in MVVM architecture using Kotlin.

Decisions:
-> I get an articleIds as List of integer from API - https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty
-> Based on the articleIds I get some information by articleId from API - https://hacker-news.firebaseio.com/v0/item/{articleid}.json?print=pretty
-> Then, I stored those List of Articles into local database.
-> Finally, I list out that Articles from local database

App has the following packages:
data: It contains all the data accessing and manipulating components.
di: It contains the files required by Dagger
ui: View classes along with their corresponding ViewModel.
util: Utility classes.
