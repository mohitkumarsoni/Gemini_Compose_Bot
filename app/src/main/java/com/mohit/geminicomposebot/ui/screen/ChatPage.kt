package com.mohit.geminicomposebot.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorModel
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohit.geminicomposebot.R
import com.mohit.geminicomposebot.ui.model.MessageModel
import com.mohit.geminicomposebot.ui.screen.viewmodel.ChatViewModel
import com.mohit.geminicomposebot.ui.theme.ModelColor
import com.mohit.geminicomposebot.ui.theme.UserColor

@Composable
fun ChatPage(modifier: Modifier = Modifier, viewModel: ChatViewModel) {
    Column(modifier =  modifier) {
        AppHeader()
        MessageList(modifier = Modifier.weight(1f), messageList = viewModel.messageList)
        MessageInput(onMessageSend = {
            viewModel.sendMessage(it)
        })
    }
}

@Composable
fun AppHeader() {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)){
        Text(text = "Gemini Chat Bot", fontWeight = FontWeight.Bold, fontSize = 18.sp)
    }
}

@Composable
fun MessageInput(onMessageSend : (String)->Unit ) {
    var message by remember {mutableStateOf("")}

    Row (Modifier.fillMaxWidth()){
        OutlinedTextField(modifier = Modifier.weight(1f),value = message, onValueChange = {message = it}, placeholder = { Text(text = "Ask Anything")}, maxLines = 6)
        IconButton(onClick = {
            if (message.isNotEmpty()) {
                onMessageSend(message)
                message = ""
            }
        }) {
            Icon(imageVector = Icons.Default.Send, contentDescription = "Send")
        }
    }
}

@Composable
fun MessageList(modifier: Modifier = Modifier, messageList : List<MessageModel> ) {

    if (messageList.isEmpty()){
        Column(
            modifier = modifier.fillMaxSize() ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(modifier = Modifier.size(66.dp),painter = painterResource(id = R.drawable.baseline_question_answer_24), contentDescription = "")
            Text(text = "Ask me anything")
        }
    }else {
        LazyColumn(modifier = modifier, reverseLayout = true) {
            items(messageList.reversed()) {
                MessageRow(it)
            }
        }
    }
}

@Composable
fun MessageRow(messageModel: MessageModel)  {
    val isModel = messageModel.role == "model"

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.fillMaxWidth()) {

            Box(
                modifier = Modifier
                    .align(if (isModel) Alignment.BottomStart else Alignment.BottomEnd)
                    .padding(
                        start = if (isModel) 8.dp else 70.dp,
                        end = if (isModel) 70.dp else 8.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    )
                    .clip(RoundedCornerShape(48f))
                    .background(if (isModel) ModelColor else UserColor)
                    .padding(8.dp)
            ) {
                SelectionContainer {
                    Text(
                        text = messageModel.message,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }

}




