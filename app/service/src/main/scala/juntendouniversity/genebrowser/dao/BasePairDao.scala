package juntendouniversity.genebrowser.dao

import cats.effect.kernel.Async
import doobie.*
import doobie.implicits.*

case class BasePairRow(id: Long, position: Long, leftNucleotide: String, rightNucleotide: String)

trait BasePairDao[F[_]]:
  def findByChromosome(tsn: Int, chromosomeIndex: Int, offset: Long): F[List[BasePairRow]]

object BasePairDao:
  def make[F[_]: Async](xa: Transactor[F], windowSize: Long): BasePairDao[F] = new BasePairDao[F]:
    def findByChromosome(tsn: Int, chromosomeIndex: Int, offset: Long): F[List[BasePairRow]] =
      sql"""SELECT bp.id, bp.position, bp.left_nucleotide, bp.right_nucleotide
            FROM base_pairs bp
            JOIN chromosomes c ON bp.chromosome_id = c.id
            JOIN species s ON c.species_id = s.id
            WHERE s.tsn = $tsn AND c.index = $chromosomeIndex
            ORDER BY bp.position
            LIMIT $windowSize OFFSET $offset"""
        .query[BasePairRow]
        .to[List]
        .transact(xa)
