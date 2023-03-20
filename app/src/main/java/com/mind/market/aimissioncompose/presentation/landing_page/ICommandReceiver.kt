package com.mind.market.aimissioncompose.presentation.landing_page

import com.mind.market.aimissioncompose.domain.models.Goal
import com.mind.market.aimissioncompose.presentation.utils.SortingMode

interface ICommandReceiver {
    fun onAddGoalClicked()
    fun onNavigateToDetailGoal(goal: Goal? = null)
    fun onNavigateToAuthenticationScreen()
    fun onDeleteGoalClicked(goal: Goal)
    fun onStatusChangedClicked(goal: Goal)
    fun onUndoDeletedGoalClicked()
    fun onShowDialog(title: String, message: String)
    fun onLogoutUserClicked()
    fun onGoalUpdated()
    fun onSearchTextUpdate(newText: String)
    fun onDropDownStateChanged(isVisible: Boolean = false)
    fun onSortingChanged(sortMode: SortingMode)
    fun onClearSearchText()
    fun processCommand(command: ICommand) {
        command.execute(this)
    }

}

interface ICommand {
    fun execute(receiver: ICommandReceiver)
}

class AddCommand : ICommand {
    override fun execute(receiver: ICommandReceiver) {
        receiver.onAddGoalClicked()
    }
}

class NavigateDetailCommand(private val goal: Goal?) : ICommand {
    override fun execute(receiver: ICommandReceiver) {
        receiver.onNavigateToDetailGoal(goal)
    }
}

class NavigateAuthenticationCommand() : ICommand {
    override fun execute(receiver: ICommandReceiver) {
        receiver.onNavigateToAuthenticationScreen()
    }
}

class DeleteCommand(private val goal: Goal) : ICommand {
    override fun execute(receiver: ICommandReceiver) {
        receiver.onDeleteGoalClicked(goal)
    }
}

class StatusChangeCommand(private val goal: Goal) : ICommand {
    override fun execute(receiver: ICommandReceiver) {
        receiver.onStatusChangedClicked(goal)
    }
}

class ClearSearchTextCommand() : ICommand {
    override fun execute(receiver: ICommandReceiver) {
        receiver.onClearSearchText()
    }
}

class LogoutCommand() : ICommand {
    override fun execute(receiver: ICommandReceiver) {
        receiver.onLogoutUserClicked()
    }
}

class SearchTextUpdateCommand(private val newText: String) : ICommand {
    override fun execute(receiver: ICommandReceiver) {
        receiver.onSearchTextUpdate(newText)
    }
}

class DropDownStateChangeCommand(private val isVisible: Boolean) : ICommand {
    override fun execute(receiver: ICommandReceiver) {
        receiver.onDropDownStateChanged(isVisible)
    }
}

class SortingChangeCommand(private val sortMode: SortingMode) : ICommand {
    override fun execute(receiver: ICommandReceiver) {
        receiver.onSortingChanged(sortMode)
    }
}

class GoalUpdateCommand() : ICommand {
    override fun execute(receiver: ICommandReceiver) {
        receiver.onGoalUpdated()
    }
}

class UndoDeletedGoalCommand() : ICommand {
    override fun execute(receiver: ICommandReceiver) {
        receiver.onUndoDeletedGoalClicked()
    }
}

class ShowDialogCommand(private val title: String, private val text: String) : ICommand {
    override fun execute(receiver: ICommandReceiver) {
        receiver.onShowDialog(title, text)
    }
}

