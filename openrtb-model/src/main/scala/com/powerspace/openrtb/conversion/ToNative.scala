package com.powerspace.openrtb.conversion

import com.google.openrtb.BidRequest
import com.google.openrtb.BidRequest.Imp
import com.google.openrtb.BidRequest.Imp.Native
import com.google.openrtb.BidRequest.Imp.Native.RequestOneof
import monocle.function.Each
import monocle.function.Each.fromTraverse
import scalaz.{Applicative, Traverse}

/** Object definitions for scalaz **/
object SeqTraverse {
  /** Redefine a traverse for seqs since scalaz does not provide one */
  implicit val seqTraverse = new Traverse[Seq] {
    def traverseImpl[F[_], A, B](l: Seq[A])(f: A => F[B])(implicit F: Applicative[F]) = {
      F.map(l.reverse.foldLeft(F.point(Nil: List[B])) { (flb: F[List[B]], a: A) =>
        F.apply2(f(a), flb)(_ :: _)
      })(_.toSeq)
    }
  }

  /** Define an each from the abovementionned traverse object */
  implicit def seqEach[A]: Each[Seq[A], A] = fromTraverse
}


object RequestLenses {
  import SeqTraverse._
  import monocle.function.all._
  import monocle.macros._
  import monocle.std.all._

  val native = GenLens[Imp](_.native)
  val nativeRequestOneOf = GenLens[Native](_.requestOneof)
  val impLens = GenLens[BidRequest](_.imp)

  /** Traversal from req.imps object */
  val impsTraversal = impLens.composeTraversal(each)

  /** Optional from imp.native object */
  val optNative = native.composePrism(some)

  /** Lens composition for req.imp.native.oneOf */
  val nativeRequestOneOfTraversal = impsTraversal.composeTraversal(optNative
    .composeOptional(nativeRequestOneOf.asOptional).asTraversal)

  val getAllNatives: BidRequest => List[RequestOneof] = nativeRequestOneOfTraversal
    .getAll
}


