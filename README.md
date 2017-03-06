# RxFirebase

RxJava wrapper written in kotlin for Google's [Firebase for Android](https://www.firebase.com/docs/android/) library.


## Usage

```
//Kotlin
//Adds a valueEventListener to a single item
FirebaseDatabase.getInstance().getReference("users/1")
                              .observe()
                              .mapTo(User::class.java)
                              .subscribe { 
                                    user -> Log.v(user.id)
                              }
//Adds a valueEventListener to a list of items
FirebaseDatabase.getInstance().getReference("users")
                              .observe()
                              .mapChildrenTo(User::class.java)
                              .subscribe {
                                    users -> Log.v(users[0].id)
                              }

//Java
RxFirebaseDatabase.observeValueEvent(
                                     FirebaseDatabase.getInstance().getReference("users")
                                     );

```

## Installation

```
compile 'com.github.pixis:rxfirebase:1.0.5'
```

## License

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
