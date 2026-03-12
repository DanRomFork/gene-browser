package tbd.genebrowser.dao

import cats.effect.kernel.Async
import doobie.*
import doobie.implicits.*

case class SpeciesRow(scientificName: String, tsn: Int)

trait SpeciesDao[F[_]]:
  def findAll: F[List[SpeciesRow]]

object SpeciesDao:
  def make[F[_]: Async](xa: Transactor[F]): SpeciesDao[F] = new SpeciesDao[F]:
    def findAll: F[List[SpeciesRow]] =
      sql"SELECT scientific_name, tsn FROM species"
        .query[SpeciesRow]
        .to[List]
        .transact(xa)
