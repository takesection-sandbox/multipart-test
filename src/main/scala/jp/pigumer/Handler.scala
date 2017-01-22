package jp.pigumer

import java.io.{InputStream, OutputStream}
import java.nio.charset.StandardCharsets
import java.util.Base64

import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}
import org.parboiled.scala._
import spray.json._

import scala.annotation.tailrec

case class Request(contentType: String, body: String)

object RequestJsonProtocol extends DefaultJsonProtocol {
  implicit val format = jsonFormat2(Request)
}

case class MediaType(main: String, sub: String, params: Map[String, String]) {
}

class MediaTypeRule extends Parser {

  type StringMapBuilder = scala.collection.mutable.Builder[(String, String), Map[String, String]]

  def identityFunc[T]: T => T = x => x

  def MediaType = rule {
    MediaTypeValue ~~> { a => a }
  }

  lazy val MediaTypeValue = rule {
    MediaTypeDef ~ EOI ~~> { (main, sub, params) =>
      @tailrec def processParams(remaining: List[(String, String)] = params,
                                 builder: StringMapBuilder = null): Map[String, String] =
        remaining match {
          case Nil => if (builder eq null) Map.empty else builder.result()
          case kvp :: tail =>
            val b = if (builder eq null) Map.newBuilder[String, String] else builder
            b += kvp
            processParams(tail, b)
        }

      val parameters: Map[String, String] = processParams()
      new MediaType(main, sub, parameters)
    }
  }

  def MediaTypeDef: Rule3[String, String, List[(String, String)]] = rule {
    Type ~ "/" ~ SubType ~ zeroOrMore(";" ~ Parameter)
  }

  def Type = rule { Token }

  def SubType = rule { Token }

  def Parameter = rule { Attribute ~ "=" ~ Value ~~> ((_, _)) }

  def Attribute = rule { Token }

  def Value = rule { Token }

  def Token = rule { oneOrMore(TokenChar) ~> identityFunc }

  def TokenChar = rule { !CTL ~ !Separator ~ ANY }

  def CTL = rule { "\u0000" - "\u001F" | "\u007F" }

  def Separator = rule { anyOf("()<>@,;:\\\"/[]?={}\t") }

  def parse(input: String): ParsingResult[MediaType] = {
    ReportingParseRunner(MediaType).run(input)
  }
}

class Handler extends RequestStreamHandler {

  override def handleRequest(input: InputStream, output: OutputStream, context: Context) = {

    import RequestJsonProtocol._

    val in: Array[Byte] = Stream.continually(input.read).takeWhile(_ != -1).map(_.toByte).toArray
    val json: String = new String(in, StandardCharsets.UTF_8)

    val request = json.parseJson.convertTo[Request]

    val body = Base64.getDecoder.decode(request.body)
    output.write(body)
  }
}
