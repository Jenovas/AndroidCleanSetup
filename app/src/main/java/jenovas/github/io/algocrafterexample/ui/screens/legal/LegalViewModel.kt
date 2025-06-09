package jenovas.github.io.algocrafterexample.ui.screens.legal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LegalViewModel() : ViewModel() {
    private val _state = MutableStateFlow(LegalUiState())
    val state: StateFlow<LegalUiState> = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        LegalUiState()
    )

    private val _effects = Channel<LegalScreenEffect>(capacity = Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun handleEvent(event: LegalScreenEvent) {
        when (event) {
            is LegalScreenEvent.SetTosAccepted -> {
                handleTosAccepted(event.accepted)
            }
            is LegalScreenEvent.SetPrivacyAccepted -> {
                handlePrivacyAccepted(event.accepted)
            }
            is LegalScreenEvent.SetAIDisclaimerAccepted -> {
                handleAIDisclaimerAccepted(event.accepted)
            }
            is LegalScreenEvent.SetAllAccepted -> {
                handleAllAccepted()
            }

            is LegalScreenEvent.NavigateToTermsOfService -> {
                sendNavigateToTerms()
            }
            is LegalScreenEvent.NavigateToPrivacyPolicy -> {
                sendNavigateToPrivacyPolicy()
            }
            is LegalScreenEvent.NavigateToAIDisclaimer -> {
                sendNavigateToAIDisclaimer()
            }
            is LegalScreenEvent.NavigateBack -> {
                sendNavigateBack()
            }
            is LegalScreenEvent.NavigateNext -> {
                sendNavigateNext()
            }
        }
    }

    private fun handleTosAccepted(accepted: Boolean) {
        _state.update { current ->
            val newContentState = current.contentState.copy(tosAccepted = accepted)
            val allAccepted = checkAllAccepted(
                newContentState.tosAccepted,
                newContentState.privacyAccepted,
                newContentState.aiDisclaimerAccepted
            )

            current.copy(
                contentState = newContentState.copy(allAccepted = allAccepted)
            )
        }
    }

    private fun handlePrivacyAccepted(accepted: Boolean) {
        _state.update { current ->
            val newContentState = current.contentState.copy(privacyAccepted = accepted)
            val allAccepted = checkAllAccepted(
                newContentState.tosAccepted,
                newContentState.privacyAccepted,
                newContentState.aiDisclaimerAccepted
            )
            
            current.copy(
                contentState = newContentState.copy(allAccepted = allAccepted)
            )
        }
    }

    private fun handleAIDisclaimerAccepted(accepted: Boolean) {
        _state.update { current ->
            val newContentState = current.contentState.copy(aiDisclaimerAccepted = accepted)
            val allAccepted = checkAllAccepted(
                tosAccepted = newContentState.tosAccepted,
                privacyAccepted = newContentState.privacyAccepted,
                aiDisclaimerAccepted = newContentState.aiDisclaimerAccepted
            )
            
            current.copy(
                contentState = newContentState.copy(allAccepted = allAccepted)
            )
        }
    }
    
    private fun handleAllAccepted() {
        viewModelScope.launch { sendNavigateNext() }
    }
    
    private fun checkAllAccepted(
        tosAccepted: Boolean,
        privacyAccepted: Boolean,
        aiDisclaimerAccepted: Boolean
    ): Boolean {
        return tosAccepted && privacyAccepted && aiDisclaimerAccepted
    }

    private fun sendNavigateToTerms() {
        viewModelScope.launch { _effects.send(LegalScreenEffect.NavigateToTermsOfService) }
    }

    private fun sendNavigateToPrivacyPolicy() {
        viewModelScope.launch { _effects.send(LegalScreenEffect.NavigateToPrivacyPolicy) }
    }

    private fun sendNavigateToAIDisclaimer() {
        viewModelScope.launch { _effects.send(LegalScreenEffect.NavigateToAIDisclaimer) }
    }

    private fun sendNavigateBack() {
        viewModelScope.launch { _effects.send(LegalScreenEffect.NavigateBack) }
    }

    private fun sendNavigateNext() {
        viewModelScope.launch { _effects.send(LegalScreenEffect.NavigateNext) }
    }
}

data class LegalUiState(
    val contentState: LegalScreenContentState = LegalScreenContentState(),
)

data class LegalScreenContentState(
    val tosAccepted: Boolean = false,
    val privacyAccepted: Boolean = false,
    val aiDisclaimerAccepted: Boolean = false,
    val allAccepted: Boolean = false
)

sealed interface LegalScreenEvent {
    data class SetTosAccepted(val accepted: Boolean) : LegalScreenEvent
    data class SetPrivacyAccepted(val accepted: Boolean) : LegalScreenEvent
    data class SetAIDisclaimerAccepted(val accepted: Boolean) : LegalScreenEvent
    object SetAllAccepted : LegalScreenEvent

    object NavigateToTermsOfService : LegalScreenEvent
    object NavigateToPrivacyPolicy : LegalScreenEvent
    object NavigateToAIDisclaimer : LegalScreenEvent
    object NavigateBack : LegalScreenEvent
    object NavigateNext : LegalScreenEvent
}

sealed interface LegalScreenEffect {
    data class ShowMessage(val message: String) : LegalScreenEffect

    object NavigateToTermsOfService : LegalScreenEffect
    object NavigateToPrivacyPolicy : LegalScreenEffect
    object NavigateToAIDisclaimer : LegalScreenEffect
    object NavigateBack : LegalScreenEffect
    object NavigateNext : LegalScreenEffect
}