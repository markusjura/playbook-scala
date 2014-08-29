package models

import play.api.libs.json.Json

case class LoginData(uid: String, password: String)

object LoginData {
  implicit val loginDataFormat = Json.format[LoginData]
}
