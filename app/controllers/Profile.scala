package controllers

import play.api.libs.json.Json
import play.api.mvc._
import models._
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.Future

object Profile extends Controller {

  def profile(uid: String) = Action { implicit request =>
    Ok(views.html.profile(uid))
  }

  def getPosts(uid: String) = Action.async { request =>
    for {
      user <- Users.findByUId(uid)
      posts <- Posts.findByUserId(user.right.get.id.get)
    } yield Ok(Json.toJson(Post.createPostsWithUsers(Seq(user.right.get), posts)))
  }

  def getUser(uid: String) = Action.async { request =>
    Users.findByUId(uid).map { userEither =>
      userEither.fold(
        notFound => BadRequest(Json.obj("error" -> s"User id $uid not found.")),
        user => Ok(Json.toJson(user))
      )
    }
  }
}
