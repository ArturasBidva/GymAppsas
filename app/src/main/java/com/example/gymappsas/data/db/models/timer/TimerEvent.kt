package com.example.gymappsas.data.db.models.timer

sealed class TimerEvent {
    object EXERCISE : TimerEvent()
    object BREAK : TimerEvent()
    object FINISH : TimerEvent()
    object PAUSED : TimerEvent()
    object RESUMED : TimerEvent()
    object FORWARD : TimerEvent()
    object BACKWARD : TimerEvent()
    object DEFAULT : TimerEvent()
}