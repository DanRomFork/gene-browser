package juntendouniversity.genebrowser.domain

import cats.effect.Sync
import com.typesafe.config.Config
import pureconfig.*
import pureconfig.generic.derivation.default.*
import pureconfig.module.catseffect.syntax.*

final case class AppConfig(
    database: DatabaseConfig,
    http: HttpConfig,
    pagination: PaginationConfig
) derives ConfigReader

object AppConfig:
  def load[F[_]: Sync](cfg: Config): F[AppConfig] =
    ConfigSource.fromConfig(cfg).loadF[F, AppConfig]()
