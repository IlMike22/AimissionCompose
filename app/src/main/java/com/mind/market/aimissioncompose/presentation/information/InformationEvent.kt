package com.mind.market.aimissioncompose.presentation.information

sealed interface InformationEvent {
    object OnChangeVersion : InformationEvent
}