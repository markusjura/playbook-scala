package models

import play.api.Logger
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import scala.slick.lifted.ForeignKeyQuery
import org.joda.time.DateTime
import play.api.libs.json.Json
import utils.JsonReadsWrites._
import scala.concurrent.{Await, Future}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.Play.current
import scala.concurrent.duration._

case class Post(
  id: Option[Long] = None,
  createdAt: DateTime = DateTime.now,
  message: String,
  userId: Long) extends BaseEntity

object Post {
  implicit val postFormat = Json.format[Post]

  def createPostsWithUsers(users: Seq[User], posts: Seq[Post]) =
      posts.map(p => PostWithUser(p, users.find(u => u.id.get == p.userId).get))
}

case class PostWithUser(post: Post, user: User)

object PostWithUser {
  implicit val postWithUserFormat = Json.format[PostWithUser]
}

class Posts(tag: Tag) extends BaseTable[Post](tag, "POSTS") {
  def message = column[String]("MESSAGE", O.NotNull)
  def userId = column[Long]("USER_ID", O.NotNull)

  def userFk: ForeignKeyQuery[Users, User] =
    foreignKey("POST_FK", userId, TableQuery[Users])(_.id)

  def * = (id.?, createdAt, message, userId) <> ((Post.apply _).tupled, Post.unapply _)
}

object Posts extends BaseDAO[Posts, Post] {

  override val entities = TableQuery[Posts]
  override val tableName = "POSTS"

  /**
   * Find all posts a user is following
   * @param uid
   */
  def findFollowingsByUId(uid: String): Future[Seq[Post]] =
    FutureWithSession { implicit session =>
      (for {
        users <- Users.entities.filter(_.uid === uid)
        followings <- Followings.entities if followings.userId === users.id
        posts <- entities if posts.userId === followings.followingId
      } yield posts).list
    }

  def findByUserId(userId: Long): Future[Seq[Post]] =
    FutureWithSession { implicit session =>
      entities.filter((_.userId === userId)).list
    }

  def findByUserIds(userIds: Seq[Long]): Future[Seq[Post]] =
    FutureWithSession { implicit session =>
      entities.filter(_.userId inSet userIds).list
    }
}
