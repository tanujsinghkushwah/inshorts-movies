# Inshorts Movies App

An Android application that displays trending and now playing movies from The Movie Database (TMDB) API, allows searching for movies, and bookmarking favorites.

## Features

- View trending movies
- View now playing movies
- Search for movies
- Bookmark your favorite movies
- View movie details

## Setup

### API Key

This application uses The Movie Database (TMDB) API which requires an API key. To run the app:

1. Create a file `app/src/main/res/values/api_keys.xml` (you can copy from the template file `api_keys.xml.template`)
2. Add your TMDB API key to the file:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="tmdb_api_key">YOUR_TMDB_API_KEY_HERE</string>
</resources>
```

**Note:** The `api_keys.xml` file is included in `.gitignore` to prevent accidental commit of your API key.

## Architecture

The application follows MVVM (Model-View-ViewModel) architecture with the following components:

- **Data**: Models, Repository, Remote API, and Local Database
- **UI**: Activities, Fragments, and ViewModels
- **Dependency Injection**: Dagger 2

## Libraries Used

- **RxJava/RxAndroid**: For reactive programming
- **Retrofit**: For API communication
- **Room**: For local database storage
- **Glide**: For image loading
- **Navigation Component**: For managing navigation between screens
- **Dagger 2**: For dependency injection