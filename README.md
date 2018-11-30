<img src="https://storage.googleapis.com/github-imgs/scala-open-rtb.png" width="270" height="150"/>

<img src="https://travis-ci.com/Powerspace/scala-openrtb.svg?branch=master" />

Scala OpenRTB is a full Scala library that provides protobuf bindings as well as JSON serialization for [OpenRTB](https://www.iab.com/guidelines/real-time-bidding-rtb-project/)-based entities in a functional fashion. 
This library also provides SerDes for [Powerspace](https://www.powerspace.com/en) and [BidSwitch](http://www.bidswitch.com/) bidding models, and aims to support other DSPs in the near future.

## Building
Build it like any other SBT project. Go to the root folder and run:

```bash
sbt compile
```

## Usage
To leverage ScalaOpenRTB we have to define our bid request/response extensions and their decoders:

```scala
object CustomSerdeModule extends SerdeModule {

  // Define encoder for custom extension
  implicit val directEncoder: Encoder[CustomExtension] = openRtbEncoder[CustomExtension]
 
  // Register extension for specific proto-kind object
  override def extensionRegistry: ExtensionRegistry = ExtensionRegistry()
    .registerExtension(CustomProto.bidRequest)
  
  override def nativeRegistry: ExtensionRegistry = ExtensionRegistry()

```

Then in the bidder client we import defined module and use it for bid requests/response encoding/decoding:

```scala
import CustomSerdeModule._
import io.circe.syntax._
import io.circe.parser.decode

implicit val bidRequestEncoder: Encoder[BidRequest] = PowerspaceSerdeModule.bidRequestEncoder
implicit val bidResponseDecoder: Decoder[BidRequest] = PowerspaceSerdeModule.bidResponseDecoder

val bidRequest = BidRequest(...).asJson
val bidRequest = decode[BidResponse](bidRequest)

```

## Work in Progress
 - A Maven artifact will be produced and uploaded
 - Macro replacement will be included into ScalaOpenRTB
 - Adding Travis Continuous Integration

## Links

* [OpenRTB](https://www.iab.com/guidelines/real-time-bidding-rtb-project/) specifications:
  * [OpenRTB API Spec Version 2.5](https://www.iab.com/wp-content/uploads/2016/03/OpenRTB-API-Specification-Version-2-5-FINAL.pdf)
  * [OpenRTB Dynamic Native Ads API Spec Version 1.1](https://www.iab.com/wp-content/uploads/2016/03/OpenRTB-Native-Ads-Specification-1-1_2016.pdf)
* [Powerspace](https://powerspace.com/en/): Powerspace is the 1st native advertising platform for e-mail. Powerspace allows brands to broadcast their content at scale across hundreds of media newsletters leveraging cutting-edge technology solutions.
* [BidSwitch](http://bidswitch.com/en/): BidSwitch provides immediate and seamless real-time access for Supply and Demand Partners across all media types (display, mobile, video, native, TV, DOOH, VR, etc.).

## Contributions

Feel free to:

- add support for other DSPs
- raise an issue
- submit your pull request
