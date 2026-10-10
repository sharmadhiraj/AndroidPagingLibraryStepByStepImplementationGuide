# News Feed (Android Paging 3 sample)

A small Android app that shows an endlessly scrolling list of sports news, built to demonstrate the
[Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) library
with a clean, testable structure.

Originally written as a companion to the article
[Android Paging Library Step-By-Step Implementation Guide](https://medium.com/@sharmadhiraj.np/android-paging-library-step-by-step-implementation-guide-75417753d9b9).
The code has since moved to Paging 3 and coroutines, so it no longer matches the article's Paging
2 / RxJava code.

<p align="center"><img src="docs/demo.gif" alt="App demo: launch and endless scrolling" width="270"></p>

## Features

- Infinite scrolling with page loading, a footer spinner and a footer retry
- Pull to refresh
- Loading, empty and error states with retry
- Light and dark theme (Material 3), edge-to-edge layout

## Tech stack

Kotlin, Paging 3, Coroutines and Flow, ViewModel, Retrofit and Gson, Coil, Material 3, View Binding.

## Project structure

```
data/remote   Retrofit API, DTOs and DTO to domain mapping
data/paging   PagingSource (page loading and error handling)
data          NewsRepository (builds the Pager)
domain        News model
di            AppContainer (manual dependency injection)
ui/news       Activity, ViewModel, PagingDataAdapter, load state footer
```

## Modifying items

`PagingDataAdapter` has no add, update or remove methods. Items come from `PagingData`, which is
immutable, so change the data instead:

- Transform the stream in the ViewModel with `PagingData.map`, `filter` or `insertSeparators`. Keep
  the edits (for example removed ids) in a `StateFlow` and combine it with the `Pager` flow, so the
  list is rebuilt when they change.
- For persistent edits, use a single source of truth such as Room with a `RemoteMediator`. Write the
  change to the database and the `PagingSource` invalidates and reloads on its own.
- Call `adapter.refresh()` to reload the list from the first page.

## Run

Requires Android Studio with JDK 21 and Android SDK 37.

```
./gradlew assembleDebug        # build
./gradlew testDebugUnitTest    # unit tests
./gradlew lintDebug            # lint
```

## Tests

Unit tests cover the DTO mapper, `NewsPagingSource` (paging keys, end of list, errors), the API
parsing (with `MockWebServer`) and `NewsListViewModel`.
