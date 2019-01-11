---

Build you own bidder with Scala OpenRTB
[???]
Real-time bidding refers to the buying and selling of online ad impressions through real-time auctions that occur in the time it takes a webpage to load. 
We're talking about a whole bidding process going on in order to choose the right ad to display in just a bunch of milliseconds. Quite impressive, isn't it? 
Yes, it is. But that's what happens behind the scenes while visiting almost any web page out there.
[Description of  exchange/ bidder/SSP/DSP]
Photo by John Jackson on UnsplashToday we're going to build a full-fledged bidder capable of accepting a bid request and returning a number of bids in the form of bid response. 
[it will be written using Tagless plus Scala OpenRTB]
Scala OpenRTB is a full Scala library that provides protobuf bindings as well as JSON serialization for OpenRTB-based entities in a functional fashion.
Scala OpenRTB is currently deployed in production at Powerspace, hence the project will be maintained for a good while.


---

Let's first define what a bidder is by this trait.
import com.google.openrtb.{BidRequest, BidResponse}
trait Bidder [F[_]] {
  def bidOn(bidRequest: BidRequest): F[Option[BidResponse]]
}
Well it will definitely accept a bid request and eventually answer with a bid response (maybe).
Let's implement it later. Now let's define simple server HTTP that will expose our bidder.
object BidderApp extends App {

  import com.powerspace.openrtb.json.OpenRtbSerdeModule._
  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

  implicit private val system = ActorSystem()
  implicit private val materializer = ActorMaterializer()
  implicit private val scheduler = Scheduler.global

  val bidder = new SimpleBidder[Task]()

  val router =
    path("bidOn") {
      post {
        entity(as[BidRequest]) {
          bidRequest =>
            val bidResponse = bidder.bidOn(bidRequest)
              .runSyncUnsafe(Duration.Inf)
              .getOrElse(BidResponse.defaultInstance)
            val encoded = bidResponseEncoder(bidResponse).toString
            val entity = HttpEntity(ContentTypes.`application/json`, encoded) 
           complete(entity)
        }
      }
    }

  Http().bindAndHandle(router, "0.0.0.0", 8080)

  // shutdown hook
}
Akka HTTP (we skipped some import for semplicity)
OpenRtbSerdeModule provides decoders and encoders for Bid requests and bid responses
as[BidRequest] in addition with FailFastCirceSupport allow us to automatically pick up the decoders and decode


new SimpleBidder[Task]() we tell that we want to work with Monix tasks. we could have used anything else
interesting usage of Scala OpenRTB
 - -  follow the latest reactive programming principles.