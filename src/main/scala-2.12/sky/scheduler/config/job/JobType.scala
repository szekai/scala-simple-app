package sky.scheduler.config.job

import org.json4s.CustomSerializer
import org.json4s.JsonAST.{JNull, JString}

/**
  * Created by Gan on 14-Apr-17.
  */
sealed trait JobType
case object Console extends JobType
case object Sql extends JobType

object JobTypeSerializer extends CustomSerializer[JobType](format => (
  {
    case JString(jobType) => jobType match {
      case "Console" => Console
      case "Sql" => Sql
    }
    case JNull => null
  },
  {
    case jobType: JobType => JString(jobType.getClass.getSimpleName.replace("$", ""))
  }
))
