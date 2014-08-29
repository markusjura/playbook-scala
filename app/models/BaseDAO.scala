package models

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._
import play.api.Play.current

import scala.slick.jdbc.JdbcBackend

trait BaseDAO[T <: BaseTable[E], E <: BaseEntity] {
  type Entity = T#TableElementType

  val entities: TableQuery[T]
  val tableName: String

  def find(id: Option[Long]): Future[Either[String, Entity]] =
    FutureWithSession { implicit session =>
      entities.filter(_.id === id).firstOption match {
        case None =>         Left(s"Entry with id $id in $tableName doesn't exist")
        case Some(entity) => Right(entity)
      }
    }

  def findByIds(ids: Seq[Long]): Future[Seq[Entity]] =
    FutureWithSession { implicit session =>
      entities.filter(_.id inSet ids).list
    }

  def all: Future[List[Entity]] =
    FutureWithSession { implicit session =>
      entities.list
    }

  def insert(entity: Entity): Future[Unit] =
    FutureWithSession { implicit session =>
      entities += entity
    }

  def update(entity: Entity): Future[Unit] =
    FutureWithSession { implicit session =>
      entities.filter(_.id === entity.id).update(entity)
    }

  def delete(id: Option[Long]): Future[Unit] =
    FutureWithSession { implicit session =>
      entities.filter(_.id === id).delete
    }

  def deleteAll: Future[Unit] =
    FutureWithSession { implicit session =>
      entities.delete
    }

  def FutureWithSession[T](body: => JdbcBackend.Session => T): Future[T] =
    Future {
      DB.withSession { s =>
        body(s)
      }
    }
}
