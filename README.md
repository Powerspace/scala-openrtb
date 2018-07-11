<img src="https://storage.googleapis.com/github-imgs/scala-open-rtb.png" width="270" height="150"/>

Scala OpenRTB is a full Scala library that provides protobuf bindings as well as JSON serialization for [OpenRTB](https://www.iab.com/guidelines/real-time-bidding-rtb-project/)-based entities in a functional fashion. 
This library also provides SerDes for [BidSwitch](http://www.bidswitch.com/) bidding models, and aims to support other DSPs in the near future.

## Building
Build it like any other SBT project. Go to the root folder and run:

```bash
sbt compile
```

## Work in Progress
 - Use cases will be defined into the *readme*
 - A Maven artifact will be produced and uploaded
 - Macro replacement will be included into ScalaOpenRTB
 - Adding Travis Continuous Integration

## Links

* [OpenRTB](https://www.iab.com/guidelines/real-time-bidding-rtb-project/) specifications:
  * [OpenRTB API Spec Version 2.5](https://www.iab.com/wp-content/uploads/2016/03/OpenRTB-API-Specification-Version-2-5-FINAL.pdf)
  * [OpenRTB Dynamic Native Ads API Spec Version 1.1](https://www.iab.com/wp-content/uploads/2016/03/OpenRTB-Native-Ads-Specification-1-1_2016.pdf)
* [Powerspace](https://powerspace.com/en/): Powerspace is the 1st native advertising platform for e-mail. Powerspace allows brands to broadcast their content at scale across hundreds of media newsletters leveraging cutting-edge technology solutions.

## Contributions

Feel free to:

- add support for other DSPs
- raise an issue
- submit your pull request