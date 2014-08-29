package models

import org.joda.time.DateTime

trait BaseEntity {
  val id: Option[Long]
  val createdAt: DateTime
}
