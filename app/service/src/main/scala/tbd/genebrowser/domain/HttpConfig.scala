package tbd.genebrowser.domain

import com.comcast.ip4s.{Host, Port}
import pureconfig.*
import pureconfig.error.CannotConvert
import pureconfig.generic.derivation.default.*

final case class HttpConfig(
    host: Host,
    port: Port
)

object HttpConfig:
  given ConfigReader[Host] = ConfigReader[String].emap(s =>
    Host.fromString(s).toRight(CannotConvert(s, "Host", "invalid host"))
  )

  given ConfigReader[Port] = ConfigReader[Int].emap(i =>
    Port.fromInt(i).toRight(CannotConvert(i.toString, "Port", "port out of range"))
  )

  given ConfigReader[HttpConfig] = ConfigReader.derived
