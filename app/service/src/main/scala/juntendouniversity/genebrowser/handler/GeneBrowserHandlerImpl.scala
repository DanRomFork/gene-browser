package juntendouniversity.genebrowser.handler

import cats.Monad
import cats.syntax.all.*
import juntendouniversity.genebrowser.api.{ Handler, Resource }
import juntendouniversity.genebrowser.api.definitions.*
import juntendouniversity.genebrowser.dao.*

class GeneBrowserHandlerImpl[F[_]: Monad](
    speciesDao: SpeciesDao[F],
    chromosomeDao: ChromosomeDao[F],
    basePairDao: BasePairDao[F],
    geneDao: GeneDao[F]
) extends Handler[F]:

  override def getSpecies(
      respond: Resource.GetSpeciesResponse.type
  )(): F[Resource.GetSpeciesResponse] =
    speciesDao.findAll.map { rows =>
      respond.Ok(rows.map(r => SpeciesInfo(scientificName = r.scientificName, tsn = r.tsn)).toVector)
    }

  override def getChromosomes(
      respond: Resource.GetChromosomesResponse.type
  )(tsn: Int): F[Resource.GetChromosomesResponse] =
    chromosomeDao.findByTsn(tsn).map { rows =>
      respond.Ok(rows.map(r => ChromosomeInfo(id = r.id, index = r.index)).toVector)
    }

  override def getBasePairs(
      respond: Resource.GetBasePairsResponse.type
  )(tsn: Int, chromosomeIndex: Int, offset: Option[Long]): F[Resource.GetBasePairsResponse] =
    basePairDao.findByChromosome(tsn, chromosomeIndex, offset.getOrElse(0L)).map { rows =>
      respond.Ok(
        rows
          .map(r =>
            BasePairInfo(
              id              = r.id,
              position        = r.position,
              leftNucleotide  = r.leftNucleotide,
              rightNucleotide = r.rightNucleotide
            )
          )
          .toVector
      )
    }

  override def getGenes(
      respond: Resource.GetGenesResponse.type
  )(tsn: Int, chromosomeIndex: Int, startPosition: Long, stopPosition: Long): F[Resource.GetGenesResponse] =
    geneDao.findByRegion(tsn, chromosomeIndex, startPosition, stopPosition).map { rows =>
      respond.Ok(
        rows
          .map(r =>
            GeneInfo(
              id            = r.id,
              startPosition = r.startPosition,
              stopPosition  = r.stopPosition
            )
          )
          .toVector
      )
    }
