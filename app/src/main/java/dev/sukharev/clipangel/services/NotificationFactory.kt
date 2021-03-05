package dev.sukharev.clipangel.services

class NotificationFactory {

    private constructor()


    companion object {
        fun create(notification: Notification) {
            when(notification) {
                is Notification.InputClipNotification -> {
                    
                }
            }
        }
    }

    sealed class Notification {
        class InputClipNotification(val text: String): Notification()
        class ErrorNotification(val title: String, val message: String): Notification()
    }

}