# Guardian news aggregator

#### Questions

1. **if you had another two days, what would you have tackled next?**
   1. Add pagination.
   1. Implement caching, unsure about the strategy, maybe with a very small (~30 min) expiration window, would depend on the API. (Currently only usable online). Would also need to consider a way to delete articles from the cache on API's request.
   1. Use DiffUtils on the RecyclerView.Adapter.
   1. Group the articles in more meaningful date windows, like the specs suggest, e.g. per week if more than 2 weeks old, "yesterday" if one day old, etc
   1. Display a note for articles that were marked as favourites but no longer available.
   1. Display relevant error messages when unable to get data.
   1. Create styles and UI variables for consistency and scalability.
   1. Write unit and instrumentation tests.
   1. Implement shared elements transitions opening the Article detail activity.
   1. Do the UI changes in Sketch before implementing them so it'd be easier for iOS to use it.
   1. Move the api keys outside of the project.
   1. Update libraries, migrate to androidx etc.
   1. Probably spend some time making a logo.
   
1. **what would you change about the structure of the code?**
  
   - Would change the presentation layer so that it doesn't keep a reference to the view and possibly separate the business logic from the presentation layer and the repositories into a Model layer (and not break SOLID) depending on the future plans for the project.
   - Implement DataBinding.
   - Add an Item Presenter to handle the list item views behaviours and data or merge it further with the ArticlesPresenter.
   - Create Utility classes to wrap utility APIs, e.g. Glide, SharedPreferences, java.util.Date (replace with java.time), etc
1. **what bugs did you find but not fix?**
   - Pull to refresh will interfere with the dividers when there are favourite articles.
   - Dates will be converted to GMT/BST rather than the user device's locale.
   - Not a bug but, The ArticlesPresenter is taking O(n^2) to mark the articles as favourite.
   - If you tap on an article multiple times too quickly it will spawn multiple ArticleDetailActivity's.

&nbsp;

___
