package controllers

import actors.{ChatActor, ChatManager}
import akka.actor.{ActorSystem, Props}
import akka.stream.Materializer
import javax.inject._
import play.api.libs.streams.ActorFlow
import play.api.mvc._
/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents)(implicit system: ActorSystem, mat:Materializer) extends BaseController {
  val manager = system.actorOf(Props[ChatManager],"Manager")

  def index() = Action{implicit request =>
    Ok{views.html.chatPage()}
  }

  def socket() = WebSocket.accept[String, String]{request =>
    println("Getting socket")
    ActorFlow.actorRef{out =>
      ChatActor.props(out,manager)
    }
  }


}
