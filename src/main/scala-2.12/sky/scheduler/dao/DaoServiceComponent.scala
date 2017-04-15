package sky.scheduler.dao

import java.sql.{Connection, PreparedStatement, ResultSet}

/**
  * Created by Gan on 14-Apr-17.
  */
trait DaoService {
  def getConnection(): Connection

  def executeSelect[T](preparedStatement: PreparedStatement)(f: (ResultSet) => List[T]): List[T] =
    try {
      f(preparedStatement.executeQuery())
    } finally {
      preparedStatement.close()
    }

  def readResultSet[T](rs: ResultSet)(f: ResultSet => T): List[T] =
    Iterator.continually((rs.next(), rs)).takeWhile(_._1).map {
      case (_, row) =>
        f(rs)
    }.toList
}

trait DaoServiceComponent {
  this: DatabaseServiceComponent =>

  val daoService: DaoService

  class DaoServiceImpl extends DaoService {
    override def getConnection(): Connection = databaseService.getConnection
  }
}