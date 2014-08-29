package base

import models._
import play.api.mvc.Results._
import play.api.mvc.{Result, RequestHeader}
import play.api.{Logger, Application, GlobalSettings}
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import play.api.libs.concurrent.Execution.Implicits._

object Global extends GlobalSettings {

  override def onStart(app: Application): Unit = {
    Await.result(Posts.deleteAll, 5.seconds)
    Await.result(Followings.deleteAll, 5.seconds)
    Await.result(Users.deleteAll, 5.seconds)

    // Insert testdata
    Await.result(Future(Testdata.users.foreach(Users.insert(_))), 10.seconds)
    Users.all.map { users =>
      val userIds = users.map(_.id.get)
      Testdata.followings(userIds).foreach(Followings.insert(_))
      val posts = Testdata.posts(userIds)
      posts.foreach(Posts.insert(_))
    }
  }

  override def onHandlerNotFound(request: RequestHeader): Future[Result] =
    Future.successful(BadRequest(views.html.notFound(request.uri)))
}
