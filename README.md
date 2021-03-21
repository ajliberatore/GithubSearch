# Welcome to the GithubSearch 

This project will allow you to interact with the Github API by searching for users or organizations.
The app will display a slice of each search result. You will see 3 results per search term. 

This app will show the search results as you type in order to provide immediate feedback to the user.
One limitation to this is the rate limit on the API. We are limited to 60 calls per minute. In order to
combat this, we do a few things:
 1. We debounce our search to give the user time to finish typing and not make unnecessary calls.
 2. We cancel any outstanding search if a new search one comes in.
 3. We limit our results to 3 items per search.
 4. We cache via retrofit in case the user performs the same search again. We won't need to hit the API if
 the request was made in the same day.

### Architecture
The architecture chosen here is MVVM with the repository pattern. This architecture follows clean coding
standards. It is quick to build new features with and lends itself to testing. The testability from this 
architecture comes from the separation of concerns, and dependency injection. By separating classes into 
specific responsibilities, they become decoupled which means they're easier to test. 

Dagger is used simply here for the sake of time in building this project. Everything is scoped to the 
Application level. This is fine for our use case here since there isn't much scope beyond that.

### Libraries
- Dagger 2 for dependency injection
- Retrofit for networking
- Moshi for json parsing
- Coroutines for asynchronous calls
- Picasso for image loading

### Further Considerations
- Definitely adding unit testing. They were left out due to time. Dependency injection makes this easy though.
We can mock or create custom test objects for our dependencies to give our tests control over their behavior.
This leaves the focus on the behavior of the system under test. 
- Possibly saving some data to disk. I thought about how we could add Room to help offline use and also with the 
rate limiting problem. Ultimately, I decided this wasn't a priority for the time allotted given the 
nature of the project. That being a search for any number of queries. The database would only help for 
existing searches, and at the point, we have the Retrofit cache to help there. 
