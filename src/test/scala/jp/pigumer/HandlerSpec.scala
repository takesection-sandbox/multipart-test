package jp.pigumer

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.nio.charset.StandardCharsets
import java.util.Base64

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

    "handler" in new Scope {
      val h = new Handler
      val json = Base64.getEncoder().encodeToString("test".getBytes(StandardCharsets.UTF_8))
      val is = new ByteArrayInputStream(s""""$json"""".getBytes(StandardCharsets.UTF_8))
      val os = new ByteArrayOutputStream()
      h.handleRequest(is, os, null)

      os.toString("UTF-8") must_== "test"
    }
  }
}
