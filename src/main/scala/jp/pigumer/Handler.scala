package jp.pigumer

import java.io.{InputStream, OutputStream}
import java.nio.charset.StandardCharsets
import java.util.Base64

import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}
import spray.json._

class Handler extends RequestStreamHandler {

  override def handleRequest(input: InputStream, output: OutputStream, context: Context) = {

    import spray.json.DefaultJsonProtocol._

    val in: Array[Byte] = Stream.continually(input.read).takeWhile(_ != -1).map(_.toByte).toArray
    val json: String = new String(in, StandardCharsets.UTF_8)

    val formData = Base64.getDecoder.decode(json.parseJson.convertTo[String])
    output.write(formData)
  }
}
