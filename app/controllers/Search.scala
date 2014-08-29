package controllers

import play.api.libs.json.Json
import play.api.mvc._
import models._
import play.api.libs.concurrent.Execution.Implicits._

object Search extends Controller {

  def search(searchString: String) = Action.async { request =>
    Users.findByUIdOrNameLike(searchString).map { users =>
      Ok(Json.toJson(users.sortWith(_.uid < _.uid)))
    }
  }
}
