
# HackerNews - List stories from API

-> This repository contains a sample app that lists out the top stories of HackerNews.




## Technical Requirements:

- **Architecture:** MVVM
- **Dependency injection:** Dagger-Hilt
- **REST client:** Retrofit
- **Local DB:** Room



## App has the following packages:

- **data:** It contains all the data accessing and manipulating components.
- **di:** It contains the files required by Dagger
- **ui:** View classes along with their corresponding ViewModel.
- **util:** Utility classes.


## Libraries

- Dagger Hilt
build.gradle(project)
``` plugins
  id 'com.google.dagger.hilt.android' version '2.42' apply false
```

build.gradle(app)
``` plugins
  id 'dagger.hilt.android.plugin'
```
``` dependencies
  implementation 'com.google.dagger:hilt-android:2.41'
  kapt 'com.google.dagger:hilt-compiler:2.41'
```

- Retrofit
``` dependencies
  implementation 'com.squareup.retrofit2:retrofit:2.9.0'
  implementation 'com.google.code.gson:gson:2.8.9'
  implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
```

- Room DB 
``` dependencies
  implementation 'androidx.room:room-runtime:2.4.3'
  kapt 'androidx.room:room-compiler:2.4.3'
```
- Paging
``` dependencies
  implementation "androidx.paging:paging-runtime-ktx:3.1.1"
  implementation 'androidx.room:room-paging:2.4.3'
```


## API Reference

#### Get all articleIds

```https
  GET   /v0/topstories.json?print=pretty
```
#### Get particular Article

```https
  GET  /v0/item/{articleid}.json?print=pretty
```

| Parameter  | Type      | Description                       |
| :--------  | :-------  | :-------------------------------- |
| `articleid`| Int       | **Required**. articleid of item to fetch |




## Product Requirements

 - [**Search** to filter the stories](https://github.com/RajeshDomaru/HackerNews/blob/master/app/src/main/java/com/hackernews/ui/home/StoriesFragment.kt)
 - [List the stories by **PagingDataAdapter**](https://github.com/RajeshDomaru/HackerNews/blob/master/app/src/main/java/com/hackernews/ui/home/StoriesAdapter.kt)
 - [HiltViewModel](https://github.com/RajeshDomaru/HackerNews/blob/master/app/src/main/java/com/hackernews/ui/home/StoriesViewModel.kt)
 


## Stories list
``` PagingDataAdapter
class StoriesAdapter(private val itemClicked: (url: String?, title: String?) -> Unit) :
    PagingDataAdapter<StoryEntity, StoriesAdapter.StoriesViewHolder>(StoriesDiffUtils) {

    inner class StoriesViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(storyEntity: StoryEntity) {

            with(storyEntity) {

                with(binding) {

                    tvTitle.text = title.validateString()

                    tvBy.text = by.validateString()

                    root.setOnClickListener {

                        if (InternetService.instance.isOnline()) itemClicked.invoke(url, title)
                        else Snackbar.make(
                            root,
                            root.context.getStringResources(R.string.no_internet),
                            Snackbar.LENGTH_LONG
                        ).show()

                    }

                }

            }

        }

    }

    object StoriesDiffUtils : DiffUtil.ItemCallback<StoryEntity>() {

        override fun areItemsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
            return oldItem == newItem
        }

    }

    override fun onBindViewHolder(holder: StoriesViewHolder, position: Int) {

        getItem(position)?.let { storyEntity ->

            holder.bind(storyEntity)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesViewHolder {

        return StoriesViewHolder(
            ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    }

}

```

## Search stories by **SearchView**

``` StoriesFragment
val menuHost: MenuHost = requireActivity()

menuHost.addMenuProvider(object : MenuProvider {

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {

        menuInflater.inflate(R.menu.main_activity_menu, menu)

        val searchItem: MenuItem = menu.findItem(R.id.actionStoriesSearch)

        val searchView: SearchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,

            android.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(p0: String?): Boolean = false

            override fun onQueryTextChange(searchQuery: String): Boolean {

                storiesViewModel.searchStories(searchQuery)

                return false

            }

        })

    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

        return when (menuItem.itemId) {
            R.id.actionStoriesSearch -> true
            else -> false
        }

    }

}, viewLifecycleOwner, Lifecycle.State.RESUMED)

``` 

## Refresh option to force fetch stories from remote by SwipeRefreshLayout.
``` StoriesFragment
srlStories.setOnRefreshListener {
    storiesViewModel.loadTopStories(true)
}

```
## Stories are save into **Room DB**
``` StoryDao
@Dao
interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveStory(vararg storyEntities: StoryEntity)

    @Query("SELECT * FROM story_tbl ORDER BY id DESC")
    fun getAllStories(): PagingSource<Int, StoryEntity>

    @Query("SELECT COUNT(*) AS size FROM story_tbl")
    fun storySize(): Int
    
    @Query("SELECT * FROM story_tbl WHERE title LIKE '%' || :searchString || '%' OR `by` LIKE '%' || :searchString || '%'")
    fun getSearchStories(searchString: String): PagingSource<Int, StoryEntity>

    @Query("DELETE FROM story_tbl")
    suspend fun clearAll()

}
```
## Screenshots

![List of Stories](https://github.com/RajeshDomaru/HackerNews/blob/master/app/src/main/res/drawable/list_of_stories.jpg)

![Search to filter the stories.](https://github.com/RajeshDomaru/HackerNews/blob/master/app/src/main/res/drawable/search.jpg)

![WebView page](https://github.com/RajeshDomaru/HackerNews/blob/master/app/src/main/res/drawable/web_view.jpg)

