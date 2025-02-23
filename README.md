# Stoppested Application

## Overview

Stoppested is an Android application that provides real-time information about public transportation stops and departures. The app is built using Kotlin and leverages Jetpack Compose for the UI, Koin for dependency injection, and Accompanist for handling permissions.

## Features

- Real-time updates of public transportation stops and departures.
- Search functionality for finding specific stops.
- Permission handling for accessing location data.
- Clean and modern UI using Jetpack Compose.

## Technical Details

### Architecture

The application follows the MVVM (Model-View-ViewModel) architecture pattern. This helps in separating the concerns and makes the codebase more maintainable and testable.

### Dependencies

- **Jetpack Compose**: For building the UI.
- **Koin**: For dependency injection.
- **Accompanist**: For handling permissions.
- **Apollo GraphQL**: For network requests to EnTur GraphQL API.
- **Okhttp/Retrofit**: For making HTTP requests to the Geocoder API

### Permissions

The application requests location permissions to provide accurate information about nearby stops. It uses the `rememberMultiplePermissionsState` from Accompanist to handle the permission requests and show rationale dialogs if necessary.

### Network Requests

The application uses Apollo GraphQL to fetch data from the EnTur journeyPlanner API. The `StopPlaceQuery` is used to get information about stops and their departures. The GraphQL queries are defined in `.graphql` files and are compiled into Kotlin classes using the Apollo Gradle plugin. This allows for type-safe queries and responses.

Retrieval of stops based on location or text input is done using the Geocoder API using the `GeocoderService`. This service makes use of Okhttp and Retrofit to make HTTP requests to the Geocoder API. The text input search is run as an autocomplete search giving a list of suggestions based on the input.

### Dependency Injection

Koin is used for dependency injection. The `StoppestedApplication` class initializes Koin with the necessary modules (`appModule` and `networkModule`). These modules define the dependencies and how they should be provided. For example, the `networkModule` might provide a singleton instance of the Apollo Client, while the `appModule` provides the ViewModels and repositories.

## Setup and Installation

1. Clone the repository:
   ```sh
   git clone https://github.com/leknesh/stoppested.git