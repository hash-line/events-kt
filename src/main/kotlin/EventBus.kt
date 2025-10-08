import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

interface EventBus {
    val events: SharedFlow<Event>

    suspend fun raiseEvent(event: Event)
}

class EventBusImpl(
    replayCache: Int
) : EventBus {
    private val _events = MutableSharedFlow<Event>(replay = replayCache)
    override val events: SharedFlow<Event> = _events

//    private val _events = MutableSharedFlow<Event>(
//        replay = replayCache,
//        extraBufferCapacity = 64, // Add buffer capacity to prevent blocking
//        onBufferOverflow = BufferOverflow.DROP_OLDEST
//    )

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun raiseEvent(event: Event) {
        when (event) {
            is AggregateEvent -> {
                GlobalScope.launch {
                    event.events.forEach { e ->
                        _events.emit(e)
                        if (event.delayInMillis > 0) {
                            delay(event.delayInMillis)
                        }
                    }
                }
            }

            else -> {
                emitSafely(event)
            }
        }
    }

    private suspend fun emitSafely(event: Event) {
        try {
            val result = _events.tryEmit(event) // Use tryEmit instead of emit to avoid suspension
            if (!result) {
                // Fallback to regular emit if tryEmit fails
                _events.emit(event)
            }
        } catch (e: Exception) {
            // Handle exception silently
        }
    }
}
