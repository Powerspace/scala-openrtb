package com.powerspace.openrtb.json.bidresponse

import com.google.openrtb.BidResponse.SeatBid
import com.google.openrtb.BidResponse.SeatBid.Bid.AdmOneof
import com.google.openrtb._
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumEncoders
import com.powerspace.openrtb.json.util.EncodingUtils
import io.circe.generic.extras.Configuration
import io.circe.parser._

/**
  * OpenRTB Bid Serde
  */
object OpenRtbBidSerde {

  import io.circe._
  import io.circe.syntax._
  import io.circe.generic.extras.semiauto._
  import EncodingUtils._
  import OpenRtbProtobufEnumEncoders._

  private implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames

  val nativeObject: Decoder[Json] = cursor => cursor.downField("native").as[Json]
  val nativeDecoder: Decoder[NativeResponse] = nativeObject.emapTry(OpenRtbNativeSerde.decoder.decodeJson(_).toTry)

  implicit class LoggableEither[L, R](tryInstance: Either[L, R]) {
    def doOnError(action: (L) => Unit): Either[L, R] = {
      tryInstance.fold(throwable => action(throwable), _ => Unit)
      tryInstance
    }
  }

  /**
    * Decoder for OpenRTB adm object.
    * `adm` can contain either a serialized native response string or a whole native response object
    *
    * @todo simplify this code
    * @todo remove println
    */
  private implicit val admOneOfDecoder: Decoder[SeatBid.Bid.AdmOneof] =
    cursor => for {
      adm <- cursor.as[Option[String]]
      parsed = adm.toRight(DecodingFailure("Unparsable tag adm.", List())).flatMap(parse)
      decoded = parsed
        .flatMap(nativeDecoder.decodeJson(_).doOnError(println("An error occurred while decoding adm tag.", _)))
        .toOption
    } yield {
      val maybeNative: Option[AdmOneof.AdmNative] = decoded.map(AdmOneof.AdmNative)
      val maybeOneof: Option[AdmOneof] = maybeNative.orElse(adm.map(AdmOneof.Adm))
      maybeOneof.getOrElse(AdmOneof.Empty)
    }

  /**
    * Decoder for OpenRTB bid object.
    * @todo handle bidid
    */
  def decoder: Decoder[BidResponse.SeatBid.Bid] =
    cursor => for {
      id <- cursor.downField("id").as[String]
      impid <- cursor.downField("impid").as[String]
      bidid <- cursor.downField("bidid").as[String]
      price <- cursor.downField("price").as[Double]
      adid <- cursor.downField("adid").as[Option[String]]
      adomain <- cursor.downField("adomain").as[Seq[String]]
      nurl <- cursor.downField("nurl").as[Option[String]]
      bundle <- cursor.downField("bundle").as[Option[String]]
      iurl <- cursor.downField("iurl").as[Option[String]]
      cid <- cursor.downField("cid").as[Option[String]]
      crid <- cursor.downField("crid").as[Option[String]]
      cat <- cursor.downField("cat").as[Seq[String]]
      attr <- cursor.downField("attr").as[Option[Seq[Int]]]
        .map(_.map(_.map(CreativeAttribute.fromValue)))
      api <- cursor.downField("api").as[Option[Int]]
        .map(_.map(APIFramework.fromValue))
      protocol <- cursor.downField("protocol").as[Option[Int]]
        .map(_.map(Protocol.fromValue))
      qagmediarating <- cursor.downField("qagmediarating").as[Option[Int]]
        .map(_.map(QAGMediaRating.fromValue))
      dealid <- cursor.downField("dealid").as[Option[String]]
      w <- cursor.downField("w").as[Option[Int]]
      h <- cursor.downField("h").as[Option[Int]]
      exp <- cursor.downField("exp").as[Option[Int]]
      burl <- cursor.downField("burl").as[Option[String]]
      lurl <- cursor.downField("lurl").as[Option[String]]
      tactic <- cursor.downField("tactic").as[Option[String]]
      language <- cursor.downField("language").as[Option[String]]
      wratio <- cursor.downField("wratio").as[Option[Int]]
      hratio <- cursor.downField("hratio").as[Option[Int]]
      admOneof <- cursor.downField("adm").as[Option[BidResponse.SeatBid.Bid.AdmOneof]]
    } yield {
      BidResponse.SeatBid.Bid(id, impid = impid, price = price, adid = adid, adomain = adomain,
        nurl = nurl, bundle = bundle, iurl = iurl, cid = cid, crid = crid, cat = cat, attr = attr.getOrElse(Seq()),
        api = api, protocol = protocol, qagmediarating = qagmediarating, dealid = dealid, w = w, h = h,
        exp = exp, burl = burl, lurl = lurl, tactic = tactic, language = language, wratio = wratio,
        hratio = hratio, admOneof = admOneof.getOrElse(AdmOneof.Empty))
    }

  implicit val nativeResponseEncoder: Encoder[NativeResponse] = OpenRtbNativeSerde.nativeResponseEncoder
  implicit val admOneofEncoder: Encoder[AdmOneof] = protobufOneofEncoder[AdmOneof] {
    case AdmOneof.Adm(string) => string.asJson
    case AdmOneof.AdmNative(native) => native.asJson
  }
  def encoder: Encoder[BidResponse.SeatBid.Bid] = deriveEncoder[BidResponse.SeatBid.Bid].cleanRtb

}
