package models

import play.api.Logger
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import scala.slick.lifted.ForeignKeyQuery
import com.github.tototoshi.slick.H2JodaSupport._
import org.joda.time.DateTime
import play.api.libs.json.Json
import utils.JsonReadsWrites._
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._
import play.api.Play.current

case class User(
  id: Option[Long] = None,
  createdAt: DateTime = DateTime.now,
  uid: String, // user-1, user-2, etc.
  name: String,
  bio: Option[String],
  location: Option[String]) extends BaseEntity

object User {
  // json serialization
  implicit val userFormat = Json.format[User]
}

class Users(tag: Tag) extends BaseTable[User](tag, "USERS") {
  def uid = column[String]("U_ID", O.NotNull)
  def name = column[String]("NAME", O.NotNull)
  def bio = column[Option[String]]("BIO")
  def location = column[Option[String]]("LOCATION")

  def uidIndex = index("idx_uid", uid, unique = true)

  def * = (id.?, createdAt, uid, name, bio, location) <> ((User.apply _).tupled, User.unapply _)
}

object Users extends BaseDAO[Users, User] {

  override val entities = TableQuery[Users]
  override val tableName = "USERS"

  def findByUId(uid: String): Future[Either[String, User]] =
    FutureWithSession { implicit session =>
      entities.filter(_.uid === uid).firstOption match {
        case None       => Left(s"Entry with uid $uid in $tableName doesn't exist")
        case Some(user) => Right(user)
      }
    }

  def findByUIdOrNameLike(searchString: String): Future[List[User]] = {
    val s = searchString.toLowerCase
    FutureWithSession { implicit session =>
      entities.filter(u => (u.uid like s"%$s%") || (u.name.toLowerCase like s"%$s%")).list
    }
  }

}

case class Following(
  id: Option[Long] = None,
  createdAt: DateTime = DateTime.now,
  userId: Long,
  followingId: Long) extends BaseEntity

class Followings(tag: Tag) extends BaseTable[Following](tag, "FOLLOWINGS") {
  def userId = column[Long]("USER_ID")
  def followingId = column[Long]("FOLLOWING_ID")

  def userIdFk = foreignKey("FOLLOWING_USER_ID_FK", userId, TableQuery[Users])(_.id)
  def followingIdFk = foreignKey("FOLLOWING_FOLLOWING_ID_FK", followingId, TableQuery[Users])(_.id)

  def * = (id.?, createdAt, userId, followingId) <> ((Following.apply _).tupled, Following.unapply _)
}

object Followings extends BaseDAO[Followings, Following] {
  override val entities = TableQuery[Followings]
  override val tableName = "FOLLOWINGS"
}
