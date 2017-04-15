package sky.scheduler.services

import org.scalatest.{Matchers, FlatSpec}
import sky.scheduler.TestEnvironment
import sky.scheduler.config.job.{Daily, JobConfig, TimeOptions,Console}

/**
  * Created by Gan on 15-Apr-17.
  */
class JobConfigReaderServiceTest extends FlatSpec with Matchers with TestEnvironment {

  override val ioService: IOService = new IOService
  override val jobConfigReaderService: JobConfigReaderService = new JobConfigReaderService

  "readJobConfigs" should "read and parse configurations successfully." in {
    val result = jobConfigReaderService.readJobConfigs()
    result should have size(1)
    result should contain(
      JobConfig(
        "Test Command",
        "ping google.com -c 10",
        Console,
        Daily,
        TimeOptions(12, 10)
      )
    )
  }
}
