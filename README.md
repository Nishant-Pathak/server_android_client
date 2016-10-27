# Android App for Smart Home Solution.

Design Pattern Used MVP:
![mvp.png](https://bitbucket.org/repo/XqbgGp/images/2049872573-mvp.png)
Image source: [android-architecture](https://github.com/googlesamples/android-architecture)

# Libraries used:

[Retrofit](http://square.github.io/retrofit/) To interact with Smart Homes back-end servers. All communication are rest based.

[Dagger2](https://google.github.io/dagger/) To inject dependencies.

[Sqlbrite](https://github.com/square/sqlbrite) A lightweight wrapper around SQLiteOpenHelper which introduces reactive stream semantics to SQL operations.

[sqldelight](https://github.com/square/sqldelight) Generates Java models from CREATE TABLE statements.

[AutoValue](https://github.com/google/auto/tree/master/value) Value classes code generation

[grpc](http://grpc.io) Mock grpc for talking to the back-end grpc server. Using protobuf for data transmission over the wire.