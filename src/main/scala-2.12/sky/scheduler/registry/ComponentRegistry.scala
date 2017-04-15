package sky.scheduler.registry

import sky.scheduler.actors.{ActorFactory, ActorFactoryComponent}
import sky.scheduler.config.app.AppConfigComponent
import sky.scheduler.dao._
import sky.scheduler.io.IOServiceComponent
import sky.scheduler.services.JobConfigReaderServiceComponent

/**
  * Created by Gan on 15-Apr-17.
  */
object ComponentRegistry extends AppConfigComponent
  with IOServiceComponent
  with JobConfigReaderServiceComponent
  with DatabaseServiceComponent
  with MigrationComponent
  with DaoServiceComponent
  with ActorFactoryComponent {

  override val appConfigService: ComponentRegistry.AppConfigService = new AppConfigService
  override val ioService: ComponentRegistry.IOService = new IOService
  override val jobConfigReaderService: ComponentRegistry.JobConfigReaderService = new JobConfigReaderService
  override val databaseService: DatabaseService = new H2DatabaseService
  override val migrationService: ComponentRegistry.MigrationService = new MigrationService
  override val daoService: DaoService = new DaoServiceImpl
  override val actorFactory: ActorFactory = new ActorFactoryImpl
}