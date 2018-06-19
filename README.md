<img src="https://storage.googleapis.com/github-imgs/scala-open-rtb.png" width="270" height="150"/>

Scala OpenRTB is a full Scala library that provides protobuf bindings as well as JSON serialization for [OpenRTB](https://www.iab.com/guidelines/real-time-bidding-rtb-project/)-based entities in a functional fashion. 
This library also provides SerDes for [BidSwitch](http://www.bidswitch.com/) bidding models, and aims to support other DSPs in the near future.

## Building
Build it like any other SBT project. Go to the root folder and run:

```sbt
sbt compile
```

## Work in Progress
 - Maven artifact upload
 - OpenRTB versioning and Lens (?)
 - Macro replacement
 - Add Travis Continuous Integration
 - Optimised extension registry

## Links

* [OpenRTB](https://www.iab.com/guidelines/real-time-bidding-rtb-project/) specifications:
  * [OpenRTB API Spec Version 2.5](https://github.com/twitter/scalding/wiki/Type-safe-api-reference)
  * [OpenRTB Dynamic Native Ads API Spec Version 1.1](https://github.com/twitter/scalding/wiki/Fields-based-API-Reference)
* [Powerspace](https://powerspace.com/en/): Powerspace is the 1st native advertising platform for e-mail. Powerspace allows brands to broadcast their content at scale across hundreds of media newsletters leveraging cutting-edge technology solutions.