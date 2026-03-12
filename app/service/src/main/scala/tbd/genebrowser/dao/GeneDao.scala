package tbd.genebrowser.dao

import cats.effect.kernel.Async
import doobie.*
import doobie.implicits.*

case class GeneRow(id: Long, startPosition: Long, stopPosition: Long)

trait GeneDao[F[_]]:
  def findByRegion(tsn: Int, chromosomeIndex: Int, startPosition: Long, stopPosition: Long): F[List[GeneRow]]

object GeneDao:
  def make[F[_]: Async](xa: Transactor[F]): GeneDao[F] = new GeneDao[F]:
    def findByRegion(tsn: Int, chromosomeIndex: Int, startPosition: Long, stopPosition: Long): F[List[GeneRow]] =
      sql"""SELECT g.id, bp_start.position, bp_stop.position
            FROM genes g
            JOIN base_pairs bp_start ON g.start = bp_start.id
            JOIN base_pairs bp_stop ON g.stop = bp_stop.id
            JOIN chromosomes c ON bp_start.chromosome_id = c.id
            JOIN species s ON c.species_id = s.id
            WHERE s.tsn = $tsn
              AND c.index = $chromosomeIndex
              AND bp_start.position >= $startPosition
              AND bp_stop.position <= $stopPosition
            ORDER BY bp_start.position"""
        .query[GeneRow]
        .to[List]
        .transact(xa)
