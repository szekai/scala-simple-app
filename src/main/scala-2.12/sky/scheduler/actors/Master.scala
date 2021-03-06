package sky.scheduler.actors

import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

import akka.actor.{Actor, Cancellable, Props}
import akka.routing.RoundRobinPool
import com.typesafe.scalalogging.LazyLogging
import sky.scheduler.actors.messages.{Done, Schedule, Work}
import sky.scheduler.config.job.{Daily, Hourly}

import scala.collection.mutable.ListBuffer
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Gan on 14-Apr-17.
  */
class Master(numWorkers: Int, actorFactory: ActorFactory) extends Actor with LazyLogging {
  val cancelables = ListBuffer[Cancellable]()

  val router = context.actorOf(
    Props(actorFactory.createWorkerActor()).withRouter(RoundRobinPool(numWorkers)),
    "scheduler-master-worker-router"
  )

  override def receive: Receive = {
    case Done(name, command, jobType, success) =>
      if (success) {
        logger.info("Successfully completed {} ({}).", name, command)
      } else {
        logger.error("Failure! Command {} ({}) returned a non-zero result code.", name, command)
      }
    case Schedule(configs) =>
      configs.foreach {
        case config =>
          val cancellable = this.context.system.scheduler.schedule(
            config.timeOptions.getInitialDelay(LocalDateTime.now(), config.frequency),
            config.frequency match {
              case Hourly => Duration.create(1, TimeUnit.HOURS)
              case Daily => Duration.create(1, TimeUnit.DAYS)
            },
            router,
            Work(config.name, config.command, config.jobType)
          )
          cancellable +: cancelables
          logger.info("Scheduled: {}", config)
      }
  }

  override def postStop(): Unit = {
    cancelables.foreach(_.cancel())
  }
}
