# GalleryML - Show images from the server with infinite scrolling & offline data sync functionality

## Overview of the project

 - Show list of photos as gallery (Using picsumm api).
 - User is able to tap on the photo.
 - User can see full-screen view of the photo.
 - User is able to zoom in/out the full-screen image.
 - Implemented infinite scrolling(pagination) to load more data on gallery list.
 - Added product flavor on gradle for releasing different product version with different API.
 - Cache images using Glide.
 - Cache API response and stored to Room db after each network call.
 - Allow saving photos in JPEG format to the local gallery
 - The user is able to share the photo by tapping share button

 
## Technology used

- **Room** -> for store persistance data
- **Coroutine** -> for background/long running task
- **Dagger-hilt** -> Dependency injection
- **Retrofit** -> For networking related task
- **GSON** -> json parser/mapper
- **Glide** -> For showing gallery image
- **View-Binding** -> for bind view and set data
- **Navigation** -> UI transaction
- **ProductFlavour** -> for use different API on stage and production
- **DownloadManager** -> for downloading image

## Project architecture

## Followed MVVM design pattern by following DRY principle

 - **data** -> for data management related code
 - **ui** -> for activity,fragemnt,viewmodel,adapter etc UI related class
 - **di** -> for depencency injection related code
 - **utils** -> all utils code should be here

# Things can be done to improve project performance

- Pagination can be done with jetpack pagination library.
- Instead of using livedata coroutine-flow can be used.
- There is a better image loader library in kotlin called (Coil). Can be improved image loading using coil.
- Better UI can be done, if i get some more time.


