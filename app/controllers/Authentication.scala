package controllers

import play.api._
import play.api.data.validation.ValidationError
import play.api.libs.json.{JsPath, Json}
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models._
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._

object Authentication extends Controller with Security {

  def login = Action { implicit request =>
    Ok(views.html.login(loginForm))
  }

  val errorKey = "error"

  val loginForm = Form[LoginData] {
    mapping(
      "uid" -> nonEmptyText,
      "password" -> nonEmptyText
    )(LoginData.apply)(LoginData.unapply)
  }

  def loginSubmit = Action.async { implicit request =>
    def onError(formWithErrors: Form[LoginData]): Future[Result] = {
      Future.successful(BadRequest(views.html.login(formWithErrors)))
    }

    def onSuccess(loginData: LoginData): Future[Result] = {
      authenticate(loginData).map {
        case AuthOK(token) =>
          Redirect(routes.Dashboard.dashboard).withSession(
            "token" -> token,
            "uid" -> loginData.uid)

        case AuthError(authErrorMessage) =>
          val formWithErrors = loginForm.fill(loginData).withGlobalError(authErrorMessage)
          BadRequest(views.html.login(formWithErrors))
      }
    }

    loginForm.bindFromRequest.fold(onError, onSuccess)
  }

  /**
   * Logout user with new session
   */
  def logout = Action { implicit request =>
    Redirect(routes.Authentication.login()).withNewSession
  }
}
