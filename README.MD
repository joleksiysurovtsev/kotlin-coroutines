# Coroutines and basic principles.

## Coroutines are from the program.

Coroutines allow you to perform asynchronous operations that can be suspended. And then continue doing it.
Coroutines are often compared to threads, but this is not true since threads are used to organize multitasking.
Coroutines provide only asynchronous operations that are not executed in parallel (although this is possible due to
Kotlin JWM) based on callbacks.

The advantage of coroutines over threads is the ease of switching between them, since no system calls are needed,
or blocking operations. Therefore, coroutines are also called lightweight threads.

Asynchrony is a programming concept when events occur independently of the main thread.
The way of interaction is not sequential, but happens through callbacks.


A coroutine can be stopped by marking the function with the "suspend" keyword.
A suspend function can only be called from a suspension function or a coroutine created with a coroutine builder.

The compiler converts all coroutine code into a state machine resembling a long switch case loop, where each case
corresponds to a call to the suspend function. This approach allows fewer memory objects to be generated at runtime.
applications.

In the Kotlin standard library, only basic functions have been added to organize the work of coroutines. All major features
were added via the `kotlinx.korutines` library: https://github.com/Kotlin/kotlinx.coroutines



## Modules

* [core](kotlinx-coroutines-core/README.md) &mdash; common coroutines across all platforms:
  * [launch] and [async] coroutine builders returning [Job] and [Deferred] light-weight futures with cancellation support;
  * [Dispatchers] object with [Main][Dispatchers.Main] dispatcher for Android/Swing/JavaFx, and [Default][Dispatchers.Default] dispatcher for background coroutines;
  * [delay] and [yield] top-level suspending functions;
  * [Flow] &mdash; cold asynchronous stream with [flow][_flow] builder and comprehensive operator set ([filter], [map], etc);
  * [Channel], [Mutex], and [Semaphore] communication and synchronization primitives;
  * [coroutineScope][_coroutineScope], [supervisorScope][_supervisorScope], [withContext], and [withTimeout] scope builders;
  * [MainScope()] for Android and UI applications;
  * [SupervisorJob()] and [CoroutineExceptionHandler] for supervision of coroutines hierarchies;
  * [select] expression support and more.
* [core/jvm](kotlinx-coroutines-core/jvm/) &mdash; additional core features available on Kotlin/JVM:
  * [Dispatchers.IO] dispatcher for blocking coroutines;
  * [Executor.asCoroutineDispatcher][asCoroutineDispatcher] extension, custom thread pools, and more.
* [core/js](kotlinx-coroutines-core/js/) &mdash; additional core features available on Kotlin/JS:
  * Integration with `Promise` via [Promise.await] and [promise] builder;
  * Integration with `Window` via [Window.asCoroutineDispatcher], etc.
* [test](kotlinx-coroutines-test/README.md) &mdash; test utilities for coroutines:
  * [Dispatchers.setMain] to override [Dispatchers.Main] in tests;
  * [TestCoroutineScope] to test suspending functions and coroutines.
* [debug](kotlinx-coroutines-debug/README.md) &mdash; debug utilities for coroutines:
  * [DebugProbes] API to probe, keep track of, print and dump active coroutines;
  * [CoroutinesTimeout] test rule to automatically dump coroutines on test timeout.
  * Automatic integration with [BlockHound](https://github.com/reactor/BlockHound).
* [reactive](reactive/README.md) &mdash; modules that provide builders and iteration support for various reactive streams libraries:
  * Reactive Streams ([Publisher.collect], [Publisher.awaitSingle], [kotlinx.coroutines.reactive.publish], etc),
  * Flow (JDK 9) (the same interface as for Reactive Streams),
  * RxJava 2.x ([rxFlowable], [rxSingle], etc), and
  * RxJava 3.x ([rxFlowable], [rxSingle], etc), and
  * Project Reactor ([flux], [mono], etc).
* [ui](ui/README.md) &mdash; modules that provide coroutine dispatchers for various single-threaded UI libraries:
  * Android, JavaFX, and Swing.
* [integration](integration/README.md) &mdash; modules that provide integration with various asynchronous callback- and future-based libraries:
  * JDK8 [CompletionStage.await], Guava [ListenableFuture.await], and Google Play Services [Task.await];
  * SLF4J MDC integration via [MDCContext].

## Documentation

* Presentations and videos:
  * [Kotlin Coroutines in Practice](https://www.youtube.com/watch?v=a3agLJQ6vt8) (Roman Elizarov at KotlinConf 2018, [slides](https://www.slideshare.net/elizarov/kotlin-coroutines-in-practice-kotlinconf-2018))
  * [Deep Dive into Coroutines](https://www.youtube.com/watch?v=YrrUCSi72E8) (Roman Elizarov at KotlinConf 2017, [slides](https://www.slideshare.net/elizarov/deep-dive-into-coroutines-on-jvm-kotlinconf-2017))
  * [History of Structured Concurrency in Coroutines](https://www.youtube.com/watch?v=Mj5P47F6nJg) (Roman Elizarov at Hydra 2019, [slides](https://speakerdeck.com/elizarov/structured-concurrency))
* Guides and manuals:
  * [Guide to kotlinx.coroutines by example](https://kotlinlang.org/docs/coroutines-guide.html) (**read it first**)
  * [Guide to UI programming with coroutines](ui/coroutines-guide-ui.md)
  * [Debugging capabilities in kotlinx.coroutines](docs/topics/debugging.md)
* [Compatibility policy and experimental annotations](docs/topics/compatibility.md)
* [Change log for kotlinx.coroutines](CHANGES.md)
* [Coroutines design document (KEEP)](https://github.com/Kotlin/KEEP/blob/master/proposals/coroutines.md)
* [Full kotlinx.coroutines API reference](https://kotlinlang.org/api/kotlinx.coroutines/)

## Use in your projects

### Gradle

Add dependencies (you can also add other modules you need):
```kotlin
dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
}
```

And make sure you are using the latest version of Kotlin:
```kotlin
plugins {
    // For build.gradle.kts (Kotlin DSL)
    kotlin("jvm") version "1.6.21"
    
    // For build.gradle (Groovy DSL)
    id ("org.jetbrains.kotlin.jvm") version "1.6.21"
}
```

Make sure you have `mavenCentral()` in your repositories list:

```kotlin
repositories {
    mavenCentral()
}
```


Coroutines are called lightweight threads. They use pre-set thread pools and use them smartly and efficiently. 
To suspend keyword suspends the execution of the coroutine, after the coroutine is continued, it can continue its 
execution on any thread from the coroutine dispatcher specified within this coroutine.

![img.png](images/corutines-flow1.png)

This means that traditional single-thread execution guarantees do not work for coroutines.

Coroutines on one thread work efficiently, when the coroutine stops, it does not block the thread, 
but frees up space for another operation.

For example, we can pull data from the database using a coroutine and give it to the user. 
But what if the user interrupted the session while we were getting the data from the database. 
To do this, each coroutine is tied to the ***scope*** lifetime


If you want to stop the java thread, then you won’t be able to do it right away. For this, you need to execute the 
interact method on the thread, then you need to check the interactive status each time if it is running, then you need 
to stop the operation in the thread yourself. Coroutines work according to the same rules, but with one exception: 
the state of the coroutine is checked before continuing its execution after being interrupted. 
That is, before calling any suspend function, the coroutine can be stopped