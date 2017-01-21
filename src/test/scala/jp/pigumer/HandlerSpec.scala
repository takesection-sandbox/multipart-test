package jp.pigumer

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import spray.json._

class HandlerSpec extends Specification {

  "Handler" should {

    "json" in new Scope {

      import spray.json.DefaultJsonProtocol._

      val json = """"test""""
      val str = json.parseJson.convertTo[String]

      str must equalTo("test")
    }
  }
}
