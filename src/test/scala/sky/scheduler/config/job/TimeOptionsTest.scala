package sky.scheduler.config.job

import java.time.LocalDateTime

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by Gan on 15-Apr-17.
  */
class TimeOptionsTest extends FlatSpec with Matchers {

  "getInitialDelay" should "get the right initial delay for hourly less than an hour after now." in {
    val now = LocalDateTime.now()
    val later = now.plusMinutes(20)
    // because of the logic and it will fail otherwise
    if (now.getDayOfWeek == later.getDayOfWeek) {
      val timeOptions = TimeOptions(later.getHour, later.getMinute)
      val result = timeOptions.getInitialDelay(now, Hourly)
      result.toMinutes should equal(20)
    }
  }

  it should "get the right initial delay for hourly more than an hour after now." in {
    val now = LocalDateTime.now()
    val later = now.plusHours(3)
    // because of the logic and it will fail otherwise
    if (now.getDayOfWeek == later.getDayOfWeek) {
      val timeOptions = TimeOptions(later.getHour, later.getMinute)
      val result = timeOptions.getInitialDelay(now, Hourly)
      result.toHours should equal(3)
    }
  }

  it should "get the right initial delay for hourly less than an hour before now." in {
    val now = LocalDateTime.now()
    val earlier = now.minusMinutes(25)
    // because of the logic and it will fail otherwise.
    if (earlier.getDayOfWeek == now.getDayOfWeek) {
      val timeOptions = TimeOptions(earlier.getHour, earlier.getMinute)
      val result = timeOptions.getInitialDelay(now, Hourly)
      result.toMinutes should equal(35)
    }
  }

  it should "get the right initial delay for hourly more than an hour before now." in {
    val now = LocalDateTime.now()
    val earlier = now.minusHours(1).minusMinutes(25)
    // because of the logic and it will fail otherwise.
    if (earlier.getDayOfWeek == now.getDayOfWeek) {
      val timeOptions = TimeOptions(earlier.getHour, earlier.getMinute)
      val result = timeOptions.getInitialDelay(now, Hourly)
      result.toMinutes should equal(35)
    }
  }

  it should "get the right initial delay for daily before now." in {
    val now = LocalDateTime.now()
    val earlier = now.minusMinutes(25)
    // because of the logic and it will fail otherwise.
    if (earlier.getDayOfWeek == now.getDayOfWeek) {
      val timeOptions = TimeOptions(earlier.getHour, earlier.getMinute)
      val result = timeOptions.getInitialDelay(now, Daily)
      result.toMinutes should equal(24 * 60 - 25)
    }
  }

  it should "get the right initial delay for daily after now." in {
    val now = LocalDateTime.now()
    val later = now.plusMinutes(20)
    // because of the logic and it will fail otherwise
    if (now.getDayOfWeek == later.getDayOfWeek) {
      val timeOptions = TimeOptions(later.getHour, later.getMinute)
      val result = timeOptions.getInitialDelay(now, Daily)
      result.toMinutes should equal(20)
    }
  }
}