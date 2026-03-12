package juntendouniversity.genebrowser.it

import cats.effect.IO
import doobie.*
import doobie.implicits.*

object TestFixtures:
  val pgHost: String = sys.env.getOrElse("IT_POSTGRES_HOST", "localhost")
  val pgPort: Int    = sys.env.getOrElse("IT_POSTGRES_PORT", "5432").toInt
  val pgDb: String   = sys.env.getOrElse("IT_POSTGRES_DB", "gene_browser")
  val pgUser: String = sys.env.getOrElse("IT_POSTGRES_USER", "postgres")
  val pgPass: String = sys.env.getOrElse("IT_POSTGRES_PASSWORD", "postgres")
  val jdbcUrl: String = s"jdbc:postgresql://$pgHost:$pgPort/$pgDb"

  val serviceUrl: String = sys.env.getOrElse("IT_SERVICE_URL", "http://localhost:8080")

  def cleanAndSeed(xa: Transactor[IO]): IO[Unit] =
    (for {
      _ <- sql"TRUNCATE genes, base_pairs, chromosomes, species RESTART IDENTITY CASCADE".update.run

      humanId <- sql"INSERT INTO species (scientific_name, tsn) VALUES ('Homo sapiens', 180092) RETURNING id"
        .query[Long].unique
      mouseId <- sql"INSERT INTO species (scientific_name, tsn) VALUES ('Mus musculus', 180366) RETURNING id"
        .query[Long].unique

      humanChr1 <- sql"INSERT INTO chromosomes (species_id, index) VALUES ($humanId, 1) RETURNING id"
        .query[Long].unique
      humanChr2 <- sql"INSERT INTO chromosomes (species_id, index) VALUES ($humanId, 2) RETURNING id"
        .query[Long].unique
      _ <- sql"INSERT INTO chromosomes (species_id, index) VALUES ($mouseId, 1)".update.run

      bp1 <- sql"INSERT INTO base_pairs (chromosome_id, position, left_nucleotide, right_nucleotide) VALUES ($humanChr1, 100, 'A', 'T') RETURNING id"
        .query[Long].unique
      bp2 <- sql"INSERT INTO base_pairs (chromosome_id, position, left_nucleotide, right_nucleotide) VALUES ($humanChr1, 200, 'G', 'C') RETURNING id"
        .query[Long].unique
      bp3 <- sql"INSERT INTO base_pairs (chromosome_id, position, left_nucleotide, right_nucleotide) VALUES ($humanChr1, 300, 'T', 'A') RETURNING id"
        .query[Long].unique
      bp4 <- sql"INSERT INTO base_pairs (chromosome_id, position, left_nucleotide, right_nucleotide) VALUES ($humanChr1, 400, 'C', 'G') RETURNING id"
        .query[Long].unique
      bp5 <- sql"INSERT INTO base_pairs (chromosome_id, position, left_nucleotide, right_nucleotide) VALUES ($humanChr1, 500, 'A', 'T') RETURNING id"
        .query[Long].unique

      _ <- sql"INSERT INTO base_pairs (chromosome_id, position, left_nucleotide, right_nucleotide) VALUES ($humanChr2, 150, 'G', 'C')".update.run

      _ <- sql"INSERT INTO genes (start, stop) VALUES ($bp1, $bp3)".update.run
      _ <- sql"INSERT INTO genes (start, stop) VALUES ($bp2, $bp4)".update.run
      _ <- sql"INSERT INTO genes (start, stop) VALUES ($bp3, $bp5)".update.run
    } yield ()).transact(xa)
