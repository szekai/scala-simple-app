package sky.scheduler.actors

import sky.scheduler.config.app.AppConfigComponent
import sky.scheduler.dao.DaoServiceComponent

/**
  * Created by Gan on 14-Apr-17.
  */
trait ActorFactory {
  def createMasterActor(): Master
  def createWorkerActor(): Worker
}

trait ActorFactoryComponent {
  this: AppConfigComponent
    with DaoServiceComponent =>

  val actorFactory: ActorFactory

  class ActorFactoryImpl extends ActorFactory {
    override def createMasterActor(): Master = new Master(appConfigService.workers, this)

    override def createWorkerActor(): Worker = new Worker(daoService)
  }
}
