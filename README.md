# Passwordless Mobile Authentication with Android

## Requirements

Before you begin you'll need the following:

- Android capable IDE ([Android Studio](https://developer.android.com/studio) recommended)
- A [**tru.ID** account](https://tru.id)
- An Android phone with mobile data connection
- Node.js

## Getting Started

Clone the `starter-files` branch via:

```bash
git clone -b starter-files --single-branch https://github.com/tru-ID/android-passwordless-auth.git
```

If you're only interested in the finished code in `main` then run:

```bash
git clone -b main --single-branch https://github.com/tru-ID/android-passwordless-auth.git
```

Create a [tru.ID Account](https://tru.id)

Install the tru.ID CLI via:

```bash
npm i -g @tru_id/cli

```

Input your **tru.ID** credentials which can be found within the tru.ID [console](https://developer.tru.id/console)

Install the **tru.ID** CLI [development server plugin](https://github.com/tru-ID/cli-plugin-dev-server)

Create a new **tru.ID** project within the root directory via:

```bash
tru projects:create passwordless-auth-android
```

Run the development server, pointing it to the directly containing the newly created project configuration. This will also open up a localtunnel to your development server making it publicly accessible to the Internet so that your mobile phone can access it when only connected to mobile data.

```bash
tru server -t --project-dir ./passwordless-auth-android
```
You will get a localtunnel URL in the form `https://{subdomain}.loca.lt` 

Open the project up in your Android capable IDE, navigate to `app/src/main/java/com/example/tru_phonecheck/api/retrofit/RetrofitService.kt` [here](https://github.com/tru-ID/passwordless-auth-android/blob/main/app/src/main/java/com/example/tru_phonecheck/api/retrofit/RetrofitService.kt#L17) and replace the value of `base_url` with the localtunnel URL. 
Finally, connect your phone to your computer so it's used for running the Android project and run the application from your IDE.

## Attribution 
The images used from: 
- https://www.flaticon.com/authors/google 
- https://www.freepik.com
- https://www.flaticon.com/search?search-type=icons

## Meta

Distributed under the MIT License. See [LICENSE](https://github.com/tru-ID/android-passwordless-auth/blob/main/LICENSE.md)

[**tru.ID**](https://tru.id)
