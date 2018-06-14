package com.powerspace.openrtb.json

import com.google.openrtb.NativeRequest
import scalapb.GeneratedMessage
import io.circe.generic.extras.Configuration
import scalapb.{ExtendableMessage, GeneratedExtension}
import io.circe._

import scala.reflect.ClassTag

/**
  * The encoder provider is an encoder builder
  *
  * @tparam T the type we want to provide an encoder for
  */
trait EncoderProvider[T] {
  protected implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames.withDefaults
}

trait NativeDependencies {
  implicit val nativeRequestEncoder: Encoder[NativeRequest]
}

/** Set of features that should be able to manage json openrtb extensions */
object OpenrtbExtensions {

  /** An openrtb extension is a generated extension with Optional availability. The [[scalapb.GeneratedExtension]] comes from scalapb stuff */
  type OpenrtbExtension[C <: ExtendableMessage[C], T] = GeneratedExtension[C, Option[T]]

  /**
    * An extension handle conveys 4 things
    *
    * @param baseMessage      the class we want to provide an extension for
    * @param extensionMessage the class of the extension
    * @param extension        the [[OpenrtbExtension]] that will serve as a lens
    * @param encoder          the encoder for {B}
    * @param decoder          the decoder for {B}
    * @tparam A the extendable message we want to provide an extension for
    * @tparam B the extension message that will be added to the base message
    *
    */
  case class ExtensionHandle[A <: ExtendableMessage[A], B <: GeneratedMessage](
                                                                                baseMessage: Class[A],
                                                                                extensionMessage: Class[B],
                                                                                extension: OpenrtbExtension[A, B],
                                                                                encoder: Encoder[B], decoder: Decoder[B])

  /** An extension registry is a mean to provide encoders based on a list of extensions */
  case class ExtensionRegistry(private val extensions: Seq[ExtensionHandle[_ <: ExtendableMessage[_], _]] = Seq()) {

    private val extensionMap: Map[Class[_], Seq[ExtensionHandle[_, _]]] = extensions.groupBy(_.baseMessage)

    /** register a {B} [[OpenrtbExtension]] for {A}, using an [[io.circe.Encoder]] and [[io.circe.Decoder]] */
    def registerExtension[Extendable <: ExtendableMessage[Extendable], Extension <: scalapb.GeneratedMessage](
                                                                                                               extension: OpenrtbExtension[Extendable, Extension],
                                                                                                               encoder: Encoder[Extension],
                                                                                                               decoder: Decoder[Extension])(implicit tagA: ClassTag[Extendable], tagB: ClassTag[Extension]): ExtensionRegistry = {
      copy(extensions :+ ExtensionHandle[Extendable, Extension](tagA.runtimeClass.asInstanceOf[Class[Extendable]], tagB.runtimeClass.asInstanceOf[Class[Extension]], extension, encoder, decoder))
    }

    /** Provide an [[Encoder]] configured with extensions from the extension registry */
    def encoderWithExtensions[Extendable <: ExtendableMessage[Extendable]](baseEncoder: Encoder[Extendable])(implicit tag: ClassTag[Extendable]): Encoder[Extendable] = {
      val extEncodersForThis: Seq[ExtensionHandle[Extendable, GeneratedMessage]] = extensionMap
        .getOrElse(tag.runtimeClass.asInstanceOf[Class[Extendable]], Seq()).asInstanceOf[Seq[ExtensionHandle[Extendable, GeneratedMessage]]]


      extEncodersForThis.foldLeft(baseEncoder) {
        case (currentEncoder, handle: ExtensionHandle[Extendable, GeneratedMessage]) =>
          val encoder = new Encoder[Extendable] {
            override def apply(extendable: Extendable): Json = {
              val ExtensionHandle(_, _, extension: OpenrtbExtension[Extendable, GeneratedMessage], encoder: Encoder[GeneratedMessage], _) = handle

              currentEncoder.apply(extendable)
                .mapObject(obj => {
                  val possibleExtension: Option[GeneratedMessage] = extendable.extension(extension.asInstanceOf[OpenrtbExtension[Extendable, GeneratedMessage]])
                  alterObject(encoder.asInstanceOf[Encoder[GeneratedMessage]])(possibleExtension, obj)
                })
            }
          }
          encoder
      }
    }

    private def alterObject(encoder: Encoder[GeneratedMessage])(ext: Option[GeneratedMessage], jsonObject: JsonObject) = {
      ext.map(e => jsonObject.add("ext", encoder.apply(e))).getOrElse(jsonObject)
    }
  }

}

