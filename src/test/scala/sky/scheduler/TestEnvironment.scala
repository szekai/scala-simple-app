package sky.scheduler

import sky.scheduler.actors.{ActorFactory, ActorFactoryComponent}
import sky.scheduler.config.app.AppConfigComponent
import sky.scheduler.dao._
import sky.scheduler.io.IOServiceComponent
import sky.scheduler.services.JobConfigReaderServiceComponent
import org.scalatest.mockito.MockitoSugar

import org.mockito.Mockito._
/**
  * Created by Gan on 14-Apr-17.
  */
trait TestEnvironment
  extends AppConfigComponent
    with IOServiceComponent
    with JobConfigReaderServiceComponent
    with DatabaseServiceComponent
    with MigrationComponent
    with DaoServiceComponent
    with ActorFactoryComponent
    with MockitoSugar {

  // use the test configuration file.
  override val appConfigService: AppConfigService = spy(new AppConfigService)
  // override the path here to use the test resources.
  when(appConfigService.configPath).thenReturn(this.getClass.getResource("/").getPath)

  override val ioService: IOService = mock[IOService]
  override val jobConfigReaderService: JobConfigReaderService = mock[JobConfigReaderService]
  override val databaseService: DatabaseService = mock[DatabaseService]
  override val migrationService: MigrationService = mock[MigrationService]
  override val daoService: DaoService = mock[DaoService]
  override val actorFactory: ActorFactory = mock[ActorFactory]
}