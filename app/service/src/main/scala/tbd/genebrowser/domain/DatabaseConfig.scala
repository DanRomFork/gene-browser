package tbd.genebrowser.domain

import pureconfig.*
import pureconfig.generic.derivation.default.*

final case class DatabaseConfig(
    host: String,
    port: Int,
    name: String,
    user: String,
    password: String,
    migrationsLocations: List[String]
) derives ConfigReader
