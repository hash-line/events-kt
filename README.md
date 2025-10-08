# Events-KT

A lightweight, coroutine-based event bus library for Kotlin applications. Built with Kotlin Coroutines and Flow, this library provides a simple and efficient way to handle events in your Kotlin applications.

## Features

- 🚀 **Coroutine-based**: Built on Kotlin Coroutines and Flow for modern async programming
- 📦 **Lightweight**: Minimal dependencies, maximum performance
- 🔄 **Replay Support**: Configurable replay cache for event history
- 🎯 **Type-safe**: Strongly typed event system with sealed interfaces
- ⚡ **Non-blocking**: Uses `tryEmit` for optimal performance
- 🛡️ **Error Handling**: Built-in error handling and recovery mechanisms

## Installation

### Gradle (Kotlin DSL)

```kotlin
dependencies {
    implementation("co.hashline:events-kt:1.0")
}
```

### Gradle (Groovy)

```groovy
dependencies {
    implementation 'co.hashline:events-kt:1.0'
}
```

### Maven

```xml
<dependency>
    <groupId>co.hashline</groupId>
    <artifactId>events-kt</artifactId>
    <version>1.0</version>
</dependency>
```

## Quick Start

### Basic Usage

```kotlin
import co.hashline.events.EventBus
import co.hashline.events.EventBusImpl
import co.hashline.events.EmptyEvent
import kotlinx.coroutines.*

// Create an event bus with replay cache
val eventBus = EventBusImpl(replayCache = 10)

// Collect events
val job = CoroutineScope(Dispatchers.IO).launch {
    eventBus.events.collect { event ->
        println("Received event: $event")
    }
}

// Raise an event
runBlocking {
    eventBus.raiseEvent(EmptyEvent)
}
```

### Custom Events

```kotlin
import kotlinx.serialization.Serializable

@Serializable
data class UserLoginEvent(
    val userId: String,
    val timestamp: Long = System.currentTimeMillis()
) : Event

@Serializable
data class OrderCreatedEvent(
    val orderId: String,
    val customerId: String,
    val amount: Double
) : Event

// Usage
runBlocking {
    eventBus.raiseEvent(UserLoginEvent("user123"))
    eventBus.raiseEvent(OrderCreatedEvent("order456", "customer789", 99.99))
}
```

### Aggregate Events

```kotlin
import co.hashline.events.AggregateEvent

// Create multiple events that will be emitted with a delay
val aggregateEvent = AggregateEvent(
    events = listOf(
        UserLoginEvent("user1"),
        UserLoginEvent("user2"),
        UserLoginEvent("user3")
    ),
    delayInMillis = 1000 // 1 second delay between events
)

runBlocking {
    eventBus.raiseEvent(aggregateEvent)
}
```

### Error Handling

```kotlin
import co.hashline.events.ErrorEvent

// Handle errors
val job = CoroutineScope(Dispatchers.IO).launch {
    eventBus.events.collect { event ->
        when (event) {
            is ErrorEvent -> {
                println("Error occurred: ${event.error}")
                event.exception?.printStackTrace()
            }
            else -> {
                println("Processing event: $event")
            }
        }
    }
}

// Raise an error event
runBlocking {
    eventBus.raiseEvent(ErrorEvent("Something went wrong", RuntimeException("Test error")))
}
```

## API Reference

### EventBus Interface

```kotlin
interface EventBus {
    val events: SharedFlow<Event>
    suspend fun raiseEvent(event: Event)
}
```

### Event Interface

```kotlin
interface Event {
    val id: String
}
```

### Built-in Event Types

- `EmptyEvent`: A no-op event
- `ErrorEvent`: For error handling with message and optional exception
- `AggregateEvent`: For emitting multiple events with optional delays

## Configuration

### EventBusImpl Constructor

```kotlin
EventBusImpl(
    replayCache: Int = 0 // Number of events to replay to new subscribers
)
```

## Best Practices

1. **Use appropriate replay cache size**: Set replay cache based on your application needs
2. **Handle errors gracefully**: Always handle `ErrorEvent` in your collectors
3. **Use coroutine scopes**: Manage your coroutine scopes properly to avoid memory leaks
4. **Type safety**: Create specific event types for different use cases

## Examples

The README above contains comprehensive usage examples for all the main features of the library.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Requirements

- Kotlin 1.8+
- Kotlin Coroutines 1.8+
- JVM 21+

## Changelog

### 1.0.0
- Initial release
- Basic event bus functionality
- Aggregate event support
- Error handling
- Replay cache support
