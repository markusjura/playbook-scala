package models

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.libs.json.Json
import utils.JsonReadsWrites._

//case class UserImage(id: Option[Long] = None, path: String, image: Array[Byte])
//
//object UserImage {
//  // json serialization
//  implicit val userImageFormat = Json.format[UserImage]
//}
//
//class UserImages(tag: Tag) extends BaseTable[UserImage](tag, "USER_IMAGES") {
//  def path = column[String]("PATH", O.NotNull)
//  def image = column[Array[Byte]]("IMAGE", O.NotNull)
//
//  def * = (id.?, path, image) <> ((UserImage.apply _).tupled, UserImage.unapply _)
//}
//
//object UserImages extends BaseDAO[UserImages, UserImage] {
//
//}
