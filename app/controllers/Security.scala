package controllers

import models.LoginData
import play.api.Play._
import play.api.libs.json.Json
import play.api.libs.ws.WS
import scala.concurrent.Future
import play.api.http.Status._
import play.api.libs.concurrent.Execution.Implicits._

trait Security {
  // Retrieve URL from auth service
  private val AuthServiceUrl = configuration.getString("service.auth.url").get

  sealed trait AuthResult
  case class AuthError(authErrorMessage: String) extends AuthResult
  case class AuthOK(token: String) extends AuthResult

  /**
   * Authenticate user with password against the auth service
   * @param loginData contains userId and password
   * @return AuthOK with token if user has been authenticated successfully
   *         AuthError with error message if authentication failed
   */
  def authenticate(loginData: LoginData): Future[AuthResult] = {
    // Call auth service POST /auth with userId and password
    WS.url(s"$AuthServiceUrl/auth")
      .post(Json.toJson(loginData))
      .map { response =>
      response.status match {
        case OK => AuthOK((response.json \ "token").as[String])
        case _  => AuthError((response.json \ "error").as[String])
      }
    }
  }
}
