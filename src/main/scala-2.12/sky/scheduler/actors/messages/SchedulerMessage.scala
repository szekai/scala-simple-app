package sky.scheduler.actors.messages

import sky.scheduler.config.job.{JobConfig, JobType}

/**
  * Created by Gan on 14-Apr-17.
  */
sealed trait SchedulerMessage
case class Work(name: String, command: String, jobType: JobType)
case class Done(name: String, command: String, jobType: JobType, success: Boolean)
case class Schedule(configs: List[JobConfig])
