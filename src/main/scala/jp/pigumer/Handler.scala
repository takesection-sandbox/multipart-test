package jp.pigumer

import java.io.{InputStream, OutputStream}
import java.nio.charset.StandardCharsets
import java.util.Base64

import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}
import spray.json._

case class Request(contentType: String, body: String)

object RequestJsonProtocol extends DefaultJsonProtocol {
  implicit val format = jsonFormat2(Request)
}

class Handler extends RequestStreamHandler {

  override def handleRequest(input: InputStream, output: OutputStream, context: Context) = {

    import RequestJsonProtocol._

    val in: Array[Byte] = Stream.continually(input.read).takeWhile(_ != -1).map(_.toByte).toArray
    val json: String = new String(in, StandardCharsets.UTF_8)

    val request = json.parseJson.convertTo[Request]
    val contentType = request.contentType
    val body = Base64.getDecoder.decode(request.body)
    output.write(body)
  }
}
