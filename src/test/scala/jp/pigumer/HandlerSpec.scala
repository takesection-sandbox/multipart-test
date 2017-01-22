package jp.pigumer

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.nio.charset.StandardCharsets
import java.util.Base64

import org.parboiled.scala.ParsingResult
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

    "mediaType" in new Scope {
      val a: ParsingResult[MediaType] = new MediaTypeRule().parse("multipart/form-data; boundary=----boundary")
      a.result match {
        case Some(x) => {
          x.main must equalTo("multipart")
          x.params
        }
        case None => failure
      }
    }

    "handler" in new Scope {
      val CR = 0x0d.toChar

      val h = new Handler
      val content =
        s"""------boundary$CR
          |Content-Disposition: form-data; name="password"$CR
          |$CR
          |test$CR
          |$CR
          |------boundary$CR
          |Content-Disposition: form-data; name="file"; filename="test.crt"$CR
          |Content-Type: text/plain; charset+UTF-8$CR
          |$CR
          |test
          |$CR
          |------boundary--
        """.stripMargin
      val json = Base64.getEncoder().encodeToString(content.getBytes(StandardCharsets.UTF_8))
      val is = new ByteArrayInputStream(
        s"""{
           |"contentType": "multipart/form-data; boundary=----boundary",
           |"body": "$json"
           |}""".stripMargin.getBytes(StandardCharsets.UTF_8))
      val os = new ByteArrayOutputStream()
      h.handleRequest(is, os, null)

    }
  }
}
