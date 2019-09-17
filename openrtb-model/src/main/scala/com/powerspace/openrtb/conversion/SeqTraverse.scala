package com.powerspace.openrtb.conversion

import cats.{Applicative, Eval, Traverse}
import monocle.function.Each
import monocle.function.Each.fromTraverse

/** Object definitions for cats **/
object SeqTraverse {

  /** Redefine a traverse for seqs since cats does not provide one */
  implicit val seqTraverse = new Traverse[Seq] {

    override def traverse[G[_], A, B](fa: Seq[A])(f: A => G[B])(implicit evidence$1: Applicative[G]): G[Seq[B]] = {
      evidence$1.map(fa.reverse.foldLeft(evidence$1.point(Nil: List[B])) { (flb: G[List[B]], a: A) =>
        evidence$1.map2(f(a), flb)(_ :: _)
      })(_.toSeq)
    }

    override def foldLeft[A, B](fa: Seq[A], b: B)(f: (B, A) => B): B = fa.foldLeft(b)(f)

    override def foldRight[A, B](fa: Seq[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] = fa.foldRight(lb)(f)
  }

  /** Define an each from the aforementioned traverse object */
  implicit def seqEach[A]: Each[Seq[A], A] = fromTraverse[Seq, A]

}
