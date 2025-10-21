import kotlinx.serialization.Serializable
import java.util.UUID

/**
 * Represents a base model for an event in the application.
 * An event can be any occurrence within the application. Key property of an event is its ability
 * to be transformed or mapped into another event, due to the payload being of type `Any`.
 * This allows for flexible event handling and transformation.
 */
interface Event {
    /**
     *  A unique identifier for the event.
     */
    val id: String get() = UUID.randomUUID().toString()
}


@Serializable
data class AggregateEvent(
    val events: List<Event>,
    val delayInMillis: Long = 0, // Delay between each event
) : Event


/**
 * Represents a NaN or no action event
 * @see [Event]
 */
data object EmptyEvent : Event

data class ErrorEvent(
    val error: String,
    val exception: Throwable? = null
) : Event
