package models

import org.joda.time.DateTime
import play.api.db.slick.Config.driver.simple._
import com.github.tototoshi.slick.H2JodaSupport._

abstract class BaseTable[T](tag: Tag, name: String) extends Table[T](tag, name) {
  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
  def createdAt = column[DateTime]("CREATED_AT", O.NotNull)
}
