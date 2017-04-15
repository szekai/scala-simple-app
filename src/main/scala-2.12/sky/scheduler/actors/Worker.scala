package sky.scheduler.actors

import akka.actor.Actor
import com.typesafe.scalalogging.LazyLogging
import sky.scheduler.actors.messages.{Done, Work}
import sky.scheduler.config.job.{Sql, Console}
import sky.scheduler.dao.DaoService

import sys.process._

/**
  * Created by Gan on 14-Apr-17.
  */
class Worker(daoService: DaoService) extends Actor with LazyLogging {

  private def doWork(work: Work): Unit = {
    work.jobType match {
      case Console =>
        val result = work.command.! // note - the ! are different methods
        sender ! Done(work.name, work.command, work.jobType, result == 0)
      case Sql =>
        val connection = daoService.getConnection()
        try {
          val statement = connection.prepareStatement(work.command)
          val result: List[String] = daoService.executeSelect(statement) {
            case rs =>
              val metadata = rs.getMetaData
              val numColumns = metadata.getColumnCount
              daoService.readResultSet(rs) {
                case row =>
                  (1 to numColumns).map {
                    case i =>
                      row.getObject(i)
                  }.mkString("\t")
              }
          }
          logger.info("Sql query results: ")
          result.foreach(r => logger.info(r))
          sender ! Done(work.name, work.command, work.jobType, true)
        } finally {
          connection.close()
        }
    }
  }

  override def receive: Receive = {
    case w @ Work(name, command, jobType) => doWork(w)
  }
}
