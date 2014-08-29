package models

import org.joda.time.DateTime
import play.api.Logger

import scala.util.Random
import models._

object Testdata {

  /**
   * Users
   */
  private var i = 0
  lazy val users: Seq[User] = Seq.fill(10) {
    val uid = s"user-$i"
    val userInfo = getUserInfo(uid)
    val user = User(uid = uid, name = userInfo.name, bio = userInfo.bio, location = userInfo.location)
    i = i + 1
    user
  }

  case class NameBioLocation(name: String, bio: Option[String], location: Option[String])

  private def getUserInfo(uid: String): NameBioLocation = {
    val map = Map(
      "user-0" -> NameBioLocation("Roland Kuhn", Some("Akka Tech Lead; bringing actors to the JVM stage"), Some("Uppsala, Sweden")),
      "user-1" -> NameBioLocation("Viktor Klang", Some("Chief Architect at Typesafe Inc.\n \n I write slick, performant, low-overhead Scala code with HOFs that will run on anything, period.\n \n My tweets are my own."), Some("Sweden")),
      "user-2" -> NameBioLocation("Jonas Bonér", Some("Co-founder & CTO of Typesafe—PGP: http://keybase.io/jonas"), Some("Uppsala, Sweden")),
      "user-3" -> NameBioLocation("Martin Odersky", Some("lead designer of Scala"), Some("Switzerland")),
      "user-4" -> NameBioLocation("Josh Suereth", Some("Author: Scala In Depth\n Developer: Geeky Scala Stuff\n All Around: Big Nerd"), None),
      "user-5" -> NameBioLocation("Adriaan Moors", Some("I'll be back. In October."), None),
      "user-6" -> NameBioLocation("Jason Zaugg", Some("Compiler Engineer @ Typesafe"), Some("Brisbane, Australia")),
      "user-7" -> NameBioLocation("Christopher Hunt", Some("A guy that works for Typesafe and loves twiddling with bits and bytes."), Some("Bulli, NSW, Australia")),
      "user-8" -> NameBioLocation("James Roper", Some("Australian Scala developer, @typesafe tech lead for @playframework, husband of @beth_e_roper, loves sport and outdoors, lives for Christ."), Some("Sydney")),
      "user-9" -> NameBioLocation("Rich Dougherty", Some("Programmer. Working on the Play Framework."), Some("Wellington, New Zealand")))

    map(uid)
  }

  private var count = 0
  val followingPerUser = 2
  def followings(userIds: Seq[Long]): Seq[Following] = Seq.fill(users.size * followingPerUser) {
    val userIdIndex = count / followingPerUser
    val followingIdIndex = count match {
      case evenFirst if((count & 1) == 0 && userIdIndex == 0)             => users.size - 1
      case evenLast if((count & 1) == 0 && userIdIndex == users.size - 1) => 0
      case even if((count & 1) == 0)                                      => userIdIndex - 1
      case oddLast if((count & 1) != 0 && userIdIndex == users.size - 1)  => 0
      case odd                                                            => userIdIndex + 1
    }
    count = count + 1
    Following(userId = userIds(userIdIndex), followingId = userIds(followingIdIndex))
  }

 /**
  * Posts
  */
 def posts(userIds: Seq[Long]) = Seq.fill(50)(
  Post(createdAt = getRandomDate, message = getRandomMessage, userId = getRandomUser(userIds)))

 private def getRandomDate = DateTime.now minus Random.nextInt(60480000) // Random date in last week
 private def getRandomMessage = messages(Random.nextInt(messages.size))
 private def getRandomUser(userIds: Seq[Long]) = userIds(Random.nextInt(userIds.size))

 private val messages: Array[String] = Array(
  "Scala research paper are posted! <a href=\"http://lampwww.epfl.ch/~hmiller/scala2014\" target=\"_blank\">http://lampwww.epfl.ch/~hmiller/scala2014</a>",
  "T- minus 5 weeks until the inaugural Scala Downunder. Register today to secure a place: <a href=\"http://bit.ly/1o6CMVy\" target=\"_blank\">bit.ly/1o6CMVy</a>",
  "Atos and Typesafe sign global partnership agreement to serve growing demand for reactive applications: <a href=\"http://www.nasdaq.com/press-release/atos-and-typesafe-sign-global-partnership-agreement-to-serve-growing-demand-for-reactive-20140731-01071\" target=\"_blank\">http://nasdaq.com/press-release/atos-and-typesafe</a>",
  "Scalawags #18 (@ScalaDays 2014), #19 (5 years Akka w/ @viktorklang): show notes, audio, video now available at <a href=\"http://scalawags.tv/\" target=\"_blank\">http://scalawags.tv</a>",
  "The third milestone release of @ScalaIDE 4.0.0 is out ! have a look at the new features ! <a href=\"http://scala-ide.org/blog/release-notes-4.0.0-M3.html\" target=\"_blank\">http://scala-ide.org/blog/release-notes-4.0.0-M3.html</a> #scala #eclipse",
  "Martin @odersky announces future development directions of #scala #miniboxing #scalamacros #scalablitz #dotty ",
  "<a href=\"http://playframework.com\" target=\"_blank\">http://playframework.com</a> now serves complete Japanese translations and an in progress Turkish translation: <a href=\"http://playframework.com/documentation\" target=\"_blank\">http://playframework.com/documentation</a>",
  "CQRS is magic. Forget performance; it separates creation of history from interpretation. Enables world-impacting functional programming.",
  "Find out what makes #Akka incredible to work with: <a href=\"http://bldrd.us/1mEsV4p\" target=\"_blank\">http://bldrd.us/1mEsV4p</a>",
  "Major win in Play 2.3, thanks to the community! <a href=\"https://typesafe.com/blog/major-win-in-play-23-brought-to-you-by-the-community\" target=\"_blank\">https://typesafe.com/blog/major-win-in</a>",
  "Scala was always fascinating and constant evolution makes it a great JVM language: <a href=\"http://pages.zeroturnaround.com/Kickass-Technologies.html?utm_source=10%20Kickass%20Technologies&utm_medium=reportDL&utm_campaign=kick-ass-tech&utm_rebellabsid=89\" target=\"_blank\">http://pages.zeroturnaround.com/Kickass-Technologies.html</a>",
  "Scala 2.11.2 is available! The release brings important bugfixes and new lint-options. Upgrade now! <a href=\"http://scala-lang.org/news/2.11.2\" target=\"_blank\">http://scala-lang.org/news/2.11.2</a>",
  "Scala and Play 2 IntelliJ IDEA plugins Early Access Program is Open! <a href=\"http://ow.ly/zuq1s\" target=\"_blank\">http://ow.ly/zuq1s</a>",
  "Awesome talk by @evonox about #mesosphere and how #akka is used to deploy new instances.",
  "Watching the #akka days conference at the #scala meetup in Hamburg!",
  "Getting excited to hear @ironfish discuss lessons learned in deploying Akka Persistence @typesafe's #AkkaDays.",
  "#Akka Days starting in half an hour: miss out on your own peril!",
  "<a href=\"http://info.typesafe.com/acton/fs/blocks/showLandingPage/a/3608/p/p-001f/t/page/fm/2?_ga=1.235047881.956986043.1347871563\" target=\"_blank\">http://info.typesafe.com/acton/fs</a>",
  "Play 2.3 performance improvements yielded 3x latency and memory usage improvement at the Guardian",
  "Want to know how Akka Clustering works? Checkout the blog post and Activator template by @will_sargent: <a href=\"http://tersesystems.com/2014/06/25/akka-clustering/\" target=\"_blank\">http://tersesystems.com/2014</a>",
  "Tune in to @thescalawags with @viktorklang now, discussing 5 years of #akka <a href=\"https://plus.google.com/112145465018184674652/posts\" target=\"_blank\">https://plus.google.com/112145465018184674652/posts</a>",
  "Looking for a Scala, Akka or Play job? Check these out: <a href=\"http://typesafe.com/how/together/our-customers-are-hiring\" target=\"_blank\">http://typesafe.com/how/together</a>",
  "New introduction videos has been uploaded to <a href=\"http://playframework.com\" target=\"_blank\">http://playframework.com</a> #playframework",
  "Great read about how #kifi build their blazing fast application completely with the #typesafe stack: <a href=\"https://typesafe.com/blog/case-study-kifi-powered-by-typesafe\" target=\"_blank\">https://typesafe.com/blog/case-study-kifi</a>",
  "New podcast channel to learn how to build real world reactive applications: <a href=\"https://soundcloud.com/reactive-application-development\" target=\"_blank\">https://soundcloud.com/reactive-application</a> #scala #akka #reactive #playframework",
  "Going to open source a #kafka journal for #akka-persistence soon. Currently using it for scalable event stream processing with #spark",
  "RT @skillsmatter In The Brain of Heiko Seeberger, HA with #Akka #Cluster and Akka #Persistence <a href=\"http://ow.ly/z2Lq9\" target=\"_blank\">http://ow.ly/z2Lq9</a> @hseeberger",
  "Interesting interview with @adrianco on how to build software rapidly using microservices and devops: <a href=\"http://www.infoq.com/interviews/adrian-cockcroft-microservices-devops?utm_campaign=infoq_content&utm_source=infoq&utm_medium=feed&utm_term=global\" target=\"_blank\">http://www.infoq.com/interviews/adrian</a>",
  "Curated list of Scala frameworks, libraries and software <a href=\"https://github.com/lauris/awesome-scala\" target=\"_blank\">https://github.com/lauris/awesome-scala</a>",
  "Composing Dependent Futures by @will_sargent: <a href=\"http://tersesystems.com/2014/07/10/composing-dependent-futures/\" target=\"_blank\">http://tersesystems.com/2014/07/10</a>",
  "Just saw a homeless man on market street wearing a Scala Days t-shirt.",
  "Happy 5 year anniversary #Akka! <a href=\"https://typesafe.com/company/news/java-concurrency-and-scalability-platform-akka-celebrates-fifth-anniversary\" target=\"_blank\">https://typesafe.com/company/news</a> @akkateam",
  "New blog post: Example Play project with SSL/TLS with all the docs and scripts you could want. <a href=\"http://tersesystems.com/2014/07/07/play-tls-example-with-client-authentication\" target=\"_blank\">http://tersesystems.com/2014/07/07</a>",
  "#Akka turns 5 this month! To celebrate, register for upcoming Akka Days webinar and hear some cool use cases: <a href=\"http://info.typesafe.com/acton/media/3608/akka-days\" target=\"_blank\">http://info.typesafe.com/acton/media</a>",
  "Great talk about sbt-web by @mariussoutier at the Play Meetup in Hamburg.\n#playframework #sbt #sbtweb",
  "I highly recommend watching @ironfish talk for understanding event sourcing and #Akka persistence concepts: <a href=\"http://www.parleys.com/play/53a7d2cce4b0543940d9e55c\" target=\"_blank\">http://www.parleys.com/play/53a7d2cce4b0543940d9e55c</a>",
  "All sessions of the #ScalaDays 2014 are online now: <a href=\"http://www.parleys.com/channel/53a7d269e4b0543940d9e535/presentations?sort=views&state=public\" target=\"_blank\">http://www.parleys.com/channel/53a7d269e4b0543940d9e535</a> #scala",
  "The slides from @MarkusJura's #Akka introduction: <a href=\"http://markusjura.github.io/akka-intro-slides\" target=\"_blank\">http://markusjura.github.io/akka-intro-slides</a>",
  "#Akka user group Hamburg, first meeting at @holisticon office starts now. @MarkusJura from @typesafe introducing Akka",
  "Happy that our #scala modules are finding devoted maintainers! scala-swing: @andy1138; scala-parser-combinators: @gourlaysama. Thank you!",
  "Liking the look of #scalajs at @skillsmatter London meetup as presented by @lutzhuehnken",
  "#relate is new persistence framework in Scala, developed by #lucidchart. Much better performance than Anorm: <a href=\"https://www.lucidchart.com/techblog/2014/06/17/performant-database-access-relate/\" target=\"_blank\">https://www.lucidchart.com/techblog/2014/06/17</a>",
  "Party at weekend for #ScalaDays",
  "So we've started naming streets after programming languages now? #scala #softwareeatingtheworld",
  "Typesafe Activator - An Update and Roadmap Preview: <a href=\"http://shar.es/Vx7ps\" target=\"_blank\">http://shar.es/Vx7ps</a>",
  "First virtual and free #playframework conference takes place on 3rd of June. Join us: <a href=\"http://info.typesafe.com/acton/fs/blocks/showLandingPage/a/3608/p/p-0017/t/page/fm/0?utm_medium=email&utm_source=Act-On+Software&utm_content=email&utm_campaign=Play%20All%20Day%2C%20Webinar%20Series%2C%20June%203rd&utm_term=Register\" target=\"_blank\">http://info.typesafe.com/acton/fs</a>",
  "#Scala Testing Webcast with @kipsigman: <a href=\"http://shar.es/VxvDw\" target=\"_blank\">http://shar.es/VxvDw</a>",
  "Already 25 #hackers joined us for the #scala hack weekend in #Berlin: <a href=\"http://www.meetup.com/Scala-Berlin-Brandenburg/events/182906492/\" target=\"_blank\">http://www.meetup.com/Scala-Berlin</a> \nLooking forward to build some cool apps :)",
  "Wow! 39 hackers within 3 days joined our #akka user group in Hamburg: <a href=\"http://www.meetup.com/Hamburg-Akka-Meetup/\" target=\"_blank\">http://www.meetup.com/Hamburg-Akka</a> \n\nLooking forward to the first talk :)",
  "Reviewing upcoming chapter 3 of Reactive Design Patterns by @rolandkuhn & @jamie_allen. Blown away. A must read. <a href=\"http://www.manning.com/kuhn/\" target=\"_blank\">http://www.manning.com/kuhn</a>",
  "Attending the new Fast Track to #Play training by @MarkusJura at the innoQ office in Monheim.",
  "Play 2.3.0-RC2 is released! <a href=\"https://groups.google.com/d/msg/play-framework/rqzbPBj_rUk/v_3y29LptiEJ\" target=\"_blank\">https://groups.google.com/d/msg/play-framework</a>",
  "Next week: #Scala Hamburg meetup: \"Activator 1.1 - Getting started\" by @MarkusJura at @holisticon. Join us! <a href=\"http://www.meetup.com/Scala-Hamburg/events/176447642/\" target=\"_blank\">http://www.meetup.com/Scala-Hamburg</a>",
  "Berlin is the place to be! Especially when you are a #scala enthusiast! Join the hackathon May 30 to June 1 <a href=\"http://bit.ly/1jzjzLt\" target=\"_blank\">http://bit.ly/1jzjzLt</a>",
  "Thoughtworks Pune offering free #scala course an awesome initiative <a href=\"http://www.punescala.org/training/2014/05/15/principles-of-programming-in-scala/\" target=\"_blank\">http://www.punescala.org/training</a>",
  "#akka persistence looks pretty good. Can do event sourcing, command sourcing, snapshotting and even projected views! Last talk on #geecon",
  "Typesafe Activator - What does it do today? What will it become next? Answers from our tech team: <a href=\"https://typesafe.com/blog/typesafe-activator---an-update-and-roadmap-preview\" target=\"_blank\">https://typesafe.com/blog/typesafe-activator</a>",
  "Music for evening hakking. Inspired by the #Akka roadmap. By @ktonga. Simply awesome. <a href=\"https://play.spotify.com/user/ktonga/playlist/4RKMK65sd5vlpDWGxpW5ho\" target=\"_blank\">https://play.spotify.com/user</a>",
  "Retail giants Gilt, Tomax and Walmart are going #Reactive with Typesafe! <a href=\"https://typesafe.com/company/news/typesafes-reactive-programming-platform-grows-in-popularity-as-retail-giants-gilt-tomax-and-walmart-face-huge-web-traffic\" target=\"_blank\">https://typesafe.com/company/news</a>",
  "Great post on how to build apps with #Akka, #Spray and #AngularJS (by @honzam399): <a href=\"http://www.cakesolutions.net/teamblogs/2014/05/08/spray-akka-and-angularjs/\" target=\"_blank\">http://www.cakesolutions.net/teamblogs</a>",
  "Great webinar about #reactivestreams by @rolandkuhn: <a href=\"https://www.youtube.com/watch?v=khmVMvlP_QA&feature=youtu.be\" target=\"_blank\">https://www.youtube.com/watch?v=khmVMvlP</a>",
  "\"Scala—The Simple Parts\": @odersky Presents at Gilt (and Hunter College)! <a href=\"http://tech.gilt.com/post/85138508459/scala-the-simple-parts-martin-odersky-presents-at\" target=\"_blank\">http://tech.gilt.com/post</a>",
  "Radically impressed with support by @typesafe for Scala suite! Kudos 2 the @sprayio and @akkateam members, will be first purchase 4 startup!",
  "Will Java 8 Kill Scala? <a href=\"https://www.linkedin.com/groups/Will-Java-8-Kill-Scala-746917.S.245445785\" target=\"_blank\">https://www.linkedin.com/groups</a>",
  "Java 8 might be the fastest JVM everlet's check out some performance benchmarking! :-) <a href=\"http://0t.ee/1gxZjZe\" target=\"_blank\">http://0t.ee/1gxZjZe</a> via @rebellabs",
  "#atom is out now for everyone: <a href=\"https://github.com/blog/1831-atom-free-and-open-source-for-everyone\" target=\"_blank\">https://github.com/blog</a>",
  "Build an Activator template to win the 'Go Reactive Activator Contest'. Winner will be announced at @scaladays <a href=\"http://typesafe.com/go-reactive-activator-contest\" target=\"_blank\">http://typesafe.com/go-reactive-activator-contest</a>",
  "Love this conference room at @WhitePages named @odersky",
  "SSL/TLS documentation in Play WS has landed. You wouldn’t believe how long this took to write. <a href=\"http://playframework.com/documentation/2.3-SNAPSHOT/WsSSL\" target=\"_blank\">http://playframework.com/documentation</a>",
  "Play 2.2.3 is released! <a href=\"https://groups.google.com/d/msg/play-framework/KP1_DbhcxlU/Hgq6RxsHXUcJ\" target=\"_blank\">https://groups.google.com/d/msg/play-framework</a>",
  "Congrats @akkateam! Akka has been selected as a finalist for the JAX Innovation Award and voting is now open: <a href=\"http://jax.de/awards2014/\" target=\"_blank\">http://jax.de/awards2014</a>",
  "Going #reactive with #java8 - today at @JUG_DA with @MarkusJura from @typesafe",
  "4th edition of Functional Programming Principles in Scala has started this week. Still time to enroll! #progfun <a href=\"https://www.coursera.org/course/progfun\" target=\"_blank\">https://www.coursera.org/course/progfun</a>",
  "Haven't learned a new programming language in a while. #Scala seems the best option.",
  "@SoundCloud joins @typesafe as a sponsor for students @ Scala’14: <a href=\"http://bit.ly/scala14\" target=\"_blank\">http://bit.ly/scala14</a>",
  "Scala Days Berlin tickets are going fast, will sell out soon!! Don't miss out, register today: <a href=\"http://scaladays.org/\" target=\"_blank\">http://scaladays.org</a>",
  "Great explanation of the new #java 8 Streaming API: <a href=\"http://blog.hartveld.com/2013/03/jdk-8-33-stream-api.html\" target=\"_blank\">http://blog.hartveld.com/2013</a>",
  "SSE (Server-Sent Events) Sample with AngularJS in Play Java: <a href=\"https://typesafe.com/activator/template/sse-chat-template-java\" target=\"_blank\">https://typesafe.com/activator</a>",
  "#Apple is now up to 17 #Scala job postings.",
  "#shapeless 2.0.0 final is finally out builds for #Scala 2.10.2+ and 2.11.0 available from Maven Central: <a href=\"https://github.com/milessabin/shapeless/blob/master/notes/2.0.0.markdown\" target=\"_blank\">https://github.com/milessabin/shapeless</a>",
  "Good article on Reactive & Akka Streams - <a href=\"http://adtmag.com/articles/2014/04/22/akka-reactive-streams-spec.aspx\" target=\"_blank\">http://adtmag.com/articles/2014/04/22</a> #reactive",
  "Microservices, not a free lunch: <a href=\"http://highscalability.com/blog/2014/4/8/microservices-not-a-free-lunch.html\" target=\"_blank\">http://highscalability.com/blog</a>",
  "Real world problems of the other 99% developers: <a href=\"http://www.monadic.nl/blog/2014/4/9/the-other-99\" target=\"_blank\">http://www.monadic.nl/blog</a>",
  "The basis for akka-http:\n<a href=\"http://reactive-streams.org\" target=\"_blank\">http://reactive-streams.org</a>",
  "Nice intro to #sbt-web, shows what's coming with #playframework 2.3 - <a href=\"http://presos.jamesward.com/intro-to-sbt-web/\" target=\"_blank\">http://presos.jamesward.com/intro-to-sbt-web</a>",
  "It’s a Good Friday, the remaining React videos are now available on line: <a href=\"http://youtube.com/user/reactconf\" target=\"_blank\">http://youtube.com/user/reactconf</a>",
  "Just implemented some initial support for reactive-streams in #akka-persistence. Views are now stream producers <a href=\"https://github.com/krasserm/akka/blob/release-2.3/akka-samples/akka-sample-persistence-scala/src/main/scala/sample/persistence/StreamExample.scala\" target=\"_blank\">https://github.com/krasserm/akka</a>",
  "Want to get an overview of the changes in #scala 2.11? Watch this webinar by @retronym : <a href=\"http://shar.es/TCCST\" target=\"_blank\">http://shar.es/TCCST</a>",
  "Play with sbt-web is awesome. I can't wait until 2.3 gets out of beta. Hope Slick gets in there soon. #scala #playframework #webjars #sbt",
  "Activator 1.1 Integrates App Inspection and a New UI <a href=\"http://shar.es/T6Jy3\" target=\"_blank\">http://shar.es/T6Jy3</a>",
  "Reactive Programming Patterns in Akka using Java 8 <a href=\"http://shar.es/T6Jc9\" target=\"_blank\">http://shar.es/T6Jc9</a>",
  "Reactive streams: <a href=\"http://www.reactive-streams.org/\" target=\"_blank\">http://www.reactive-streams.org</a> Looking forward to using this to provide Java developers async IO handling capabilities in Play.",
  "Reactive Streams is live! Goal is to provide a standard for asynchronous stream processing: <a href=\"https://typesafe.com/blog/typesafe-announces-akka-streams\" target=\"_blank\">https://typesafe.com/blog</a>",
  "Coding in @java feels strange after writing code mostly in #scala for the past two years.",
  "Oo, loving the actor-based WebSocket API in #playframework 2.3. Much cleaner for my typical use case. <a href=\"http://www.playframework.com/documentation/2.3-SNAPSHOT/ScalaWebSockets\" target=\"_blank\">http://www.playframework.com/documentation</a>",
  "First milestone of Play 2.3 has been released: <a href=\"https://groups.google.com/forum/#!topic/play-framework/-Wuopt6NAxg\" target=\"_blank\">https://groups.google.com/forum</a>",
  "Play Framework with Java 8: <a href=\"http://shar.es/BSEgB\" target=\"_blank\">http://shar.es/BSEgB</a>",
  "How to transition to #scala and @playframework by @kvnwbbr who helped rebuild Walmart.ca with Scala and Play: <a href=\"https://medium.com/p/d1818f25b2b7\" target=\"_blank\">https://medium.com/p/d1818f25b2b7</a>",
  "30% performance boost upgrading to #Akka 2.3.0 and #Spray 1.3.0. Really cool! :)",
  "Activator 1.1 has been released, featuring a new UI and a new way to inspect your apps. Give it a try! <a href=\"http://typesafe.com/blog/activator-11-integrates-app-inspection-and-a-new-ui\" target=\"_blank\">http://typesafe.com/blog/activator-11</a>"
 )
}
