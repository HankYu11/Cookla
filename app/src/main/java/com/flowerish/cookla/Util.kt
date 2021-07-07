package com.flowerish.cookla

class Event<T>(val content: T){

    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T?{
        return if (hasBeenHandled) {
            null
        }else{
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): T = content
}