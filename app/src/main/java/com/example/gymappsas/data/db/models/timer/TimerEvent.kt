package com.example.gymappsas.data.db.models.timer

sealed class TimerEvent(val name: String) {
    object EXERCISE : TimerEvent("EXERCISE")
    object BREAK : TimerEvent("BREAK")
    object FINISH : TimerEvent("FINISH")
    object PAUSED : TimerEvent("PAUSED")
    object RESUMED : TimerEvent("RESUMED")
    object FORWARD : TimerEvent("FORWARD")
    object BACKWARD : TimerEvent("BACKWARD")
    object DEFAULT : TimerEvent("DEFAULT")
}