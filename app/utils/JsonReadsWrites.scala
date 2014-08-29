package utils

import play.api.libs.json._
import org.joda.time.{ DateTimeZone, DateTime }
import org.apache.commons.codec.binary.Base64
import play.api.data.validation.ValidationError

object JsonReadsWrites {

  /**
   * DateTime
   */
  implicit val dateTimeFormat: Format[DateTime] = new Format[DateTime] {
    val dateTimePattern = "yyyy-mm-ddThh:mm:ss"
    val df = org.joda.time.format.ISODateTimeFormat.dateTimeNoMillis

    def reads(json: JsValue): JsResult[DateTime] = json match {
      case JsNumber(d) => JsSuccess(new DateTime(d.toLong))
      case JsString(s) => parseDate(s) match {
        case Some(d) => JsSuccess(d)
        case None    => JsError(s"Parsing error. Timestamp expected to be in the format $dateTimePattern")
      }
      case _ => JsError(s"Timestamp expected to be either a number (with milliseconds) or a string in the format $dateTimePattern")
    }

    private def parseDate(input: String): Option[DateTime] =
      scala.util.control.Exception.allCatch[DateTime] opt (DateTime.parse(input, df))

    def writes(d: org.joda.time.DateTime): JsValue = JsString(d.toDateTime(DateTimeZone.UTC).toString(df))
  }

  /**
   * Array[Byte]
   */
  implicit val byteArrayWrites = new Writes[Array[Byte]] {
    def writes(o: Array[Byte]) = JsString(new String((Base64.encodeBase64(o))))
  }

  implicit val byteArrayReads = new Reads[Array[Byte]] {
    def reads(json: JsValue) = json match {
      case JsString(value) => JsSuccess(Base64.decodeBase64(value.getBytes))
      case _               => JsError(Seq(JsPath() -> Seq(ValidationError("validate.error.expected.jsstring"))))
    }
  }
}
