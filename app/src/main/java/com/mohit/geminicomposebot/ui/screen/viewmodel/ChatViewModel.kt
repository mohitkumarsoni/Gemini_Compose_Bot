package com.mohit.geminicomposebot.ui.screen.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.mohit.geminicomposebot.api.Constant
import com.mohit.geminicomposebot.ui.model.MessageModel
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    val messageList by lazy {
        mutableStateListOf<MessageModel>()
    }

    val generativeModel : GenerativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = Constant.API_KEY
    )

    fun sendMessage(question: String) = viewModelScope.launch{

        try {
            val chat = generativeModel.startChat(
                history = messageList.map {
                    content(role = it.role){
                        text(it.message)
                    }
                }.toList()
            )

            messageList.add(MessageModel(question, "user"))
            messageList.add(MessageModel("Typing....", "model"))

            val response = chat.sendMessage(question)
            messageList.removeLast()
            messageList.add(MessageModel(response.text.toString(), "model"))

        }catch (e:Exception){
            messageList.removeLast()
            messageList.add(MessageModel("Error : ${e.localizedMessage}", " model"))
        }


    }

}
