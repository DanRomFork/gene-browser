package juntendouniversity.genebrowser.dao

import cats.effect.kernel.Async
import doobie.*
import doobie.implicits.*

case class ChromosomeRow(id: Long, index: Int)

trait ChromosomeDao[F[_]]:
  def findByTsn(tsn: Int): F[List[ChromosomeRow]]

object ChromosomeDao:
  def make[F[_]: Async](xa: Transactor[F]): ChromosomeDao[F] = new ChromosomeDao[F]:
    def findByTsn(tsn: Int): F[List[ChromosomeRow]] =
      sql"""SELECT c.id, c.index
            FROM chromosomes c
            JOIN species s ON c.species_id = s.id
            WHERE s.tsn = $tsn
            ORDER BY c.index"""
        .query[ChromosomeRow]
        .to[List]
        .transact(xa)
