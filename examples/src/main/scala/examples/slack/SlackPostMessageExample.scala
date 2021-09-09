package examples.slack

import com.slack.api.Slack
import com.slack.api.methods.request.chat.ChatPostMessageRequest
import com.slack.api.methods.MethodsClient
import com.slack.api.methods.response.chat.ChatPostMessageResponse

object SlackApi {
  val slack: Slack           = Slack.getInstance
  val token: String          = System.getenv("SLACK_TOKEN")
  val channel_name: String   = System.getenv("SLACK_CHANNEL")
  val methods: MethodsClient = slack.methods(token)

  val request: ChatPostMessageRequest = ChatPostMessageRequest.builder()
    .channel( channel_name ) // Use a channel ID `C1234567` is preferrable
    .text(":wave: Hi from a bot written in Scala!")
    .build()

  def execute(): Unit = {
    val response: ChatPostMessageResponse = methods.chatPostMessage(request)
    println(response)
  }

}