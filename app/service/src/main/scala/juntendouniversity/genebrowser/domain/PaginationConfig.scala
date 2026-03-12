package juntendouniversity.genebrowser.domain

import pureconfig.*
import pureconfig.generic.derivation.default.*

final case class PaginationConfig(
    basePairsWindowSize: Long
) derives ConfigReader
