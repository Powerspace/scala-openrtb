package com.powerspace.openrtb.conversion

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




