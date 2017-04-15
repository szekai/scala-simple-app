package sky.scheduler.services

import java.io.File

import com.typesafe.scalalogging.LazyLogging
import org.json4s.{DefaultFormats, FileInput}
import org.json4s.jackson.JsonMethods._
import sky.scheduler.config.app.AppConfigComponent
import sky.scheduler.config.job.{JobConfig, JobFrequencySerializer, JobTypeSerializer}
import sky.scheduler.io.IOServiceComponent

/**
  * Created by Gan on 14-Apr-17.
  */
trait JobConfigReaderServiceComponent {
  this: AppConfigComponent
    with IOServiceComponent =>

  val jobConfigReaderService: JobConfigReaderService

  class JobConfigReaderService() extends LazyLogging {
    private val customSerializers = List(
      JobFrequencySerializer,
      JobTypeSerializer
    )
    implicit val formats = DefaultFormats ++ customSerializers + JobConfig.jobConfigFieldSerializer

    def readJobConfigs(): List[JobConfig] =
      ioService.getAllFilesWithExtension(appConfigService.configPath, appConfigService.configExtension).flatMap {
        case path => try {
          val config = parse(FileInput(new File(path))).extract[JobConfig]
          Some(config)
        } catch {
          case ex: Throwable =>
            logger.error("Error reading config: {}", path, ex)
            None
        }

      }
  }
}
