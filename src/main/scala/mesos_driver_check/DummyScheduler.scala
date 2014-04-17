package mesos_driver_check

import org.apache.mesos.{SchedulerDriver, Scheduler}
import org.apache.mesos.Protos._
import java.util
import java.util.logging.Logger

class DummyScheduler extends Scheduler {

  val log = Logger.getLogger(getClass.getName)

  def registered(driver: SchedulerDriver, p2: FrameworkID, p3: MasterInfo) {
    log.info(
      s"""
        |###############################################################################
        | The framework is registered with FrameworkId=${p2.getValue}.
        | After several seconds, please stop Zookeeper.
        |###############################################################################
      """.stripMargin)
  }

  def reregistered(driver: SchedulerDriver, p2: MasterInfo) {
    log.info(
      s"""
        |################################################################################
        | The framework is re-registered.
        | After several seconds, please stop Zookeeper.
        |################################################################################
      """.stripMargin)
  }

  def resourceOffers(driver: SchedulerDriver, offers: util.List[Offer]) {
  }

  def offerRescinded(driver: SchedulerDriver, p2: OfferID) {
  }

  def statusUpdate(driver: SchedulerDriver, status: TaskStatus) {
  }

  def frameworkMessage(driver: SchedulerDriver, executor: ExecutorID, slave: SlaveID, p4: Array[Byte]) {
  }

  def disconnected(driver: SchedulerDriver) {
    log.info(
      s"""
        |################################################################################
        | The framework is disconnected.  It will commit to suicide.
        |################################################################################
      """.stripMargin)
    Main.suicide()
  }

  def slaveLost(driver: SchedulerDriver, slave: SlaveID) {
  }

  def executorLost(driver: SchedulerDriver, executor: ExecutorID, slave: SlaveID, p4: Int) {
  }

  def error(driver: SchedulerDriver, error: String) {
    log.info(
      s"""
        |################################################################################
        | The framework receive error "$error". It will commit to suicide.
        |################################################################################
      """.stripMargin)
    Main.suicide()
  }

}
