package sky.scheduler.io

import java.io.File

/**
  * Created by Gan on 14-Apr-17.
  */
trait IOServiceComponent {
  val ioService: IOService

  class IOService {
    def getAllFilesWithExtension(basePath: String, extension: String): List[String] = {
      val dir = new File(basePath)
      if (dir.exists() && dir.isDirectory) {
        dir.listFiles().filter(f => f.isFile && f.getPath.toLowerCase.endsWith(s".${extension}")).map {
          case f => f.getAbsolutePath
        }.toList
      } else {
        List.empty
      }
    }
  }
}
