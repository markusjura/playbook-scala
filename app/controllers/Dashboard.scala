package controllers

import models._
import play.api._
import play.api.libs.json.Json
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.Future

object Dashboard extends Controller {

  def dashboard = Action { implicit request =>
    Ok(views.html.dashboard())
  }

  def getPosts = Action.async { request =>
    val uid = request.session.get("uid").get
    for {
      posts <- Posts.findFollowingsByUId(uid)
      users <- Users.findByIds(posts.map(_.userId))
      postsWithUsers <- Future(Post.createPostsWithUsers(users, posts))
    } yield Ok(Json.toJson(postsWithUsers))
  }
}
