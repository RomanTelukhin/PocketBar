package com.pocketcocktails.pocketbar.ui.actions

sealed class UserActionSearchById {
    object OnIdChanged: UserActionSearchById()
}