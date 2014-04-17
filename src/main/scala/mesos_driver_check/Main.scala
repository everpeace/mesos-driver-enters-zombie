package mesos_driver_check

import scala.concurrent._
import org.apache.mesos.MesosSchedulerDriver
import org.apache.mesos.Protos._
import scala.util.{Success, Failure}
import java.util.concurrent.Executors
import java.util.logging.Logger

object Main extends App {
  val log = Logger.getLogger(getClass.getName)
  implicit val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(10))

  // Every Mesos framework needs a name
  val frameworkName = "mesos-scheduler-check-dummy-0.0.1"
  // URL of the Mesos master
  val master = args(0)

  // FrameworkID uniquely identifies a framework and can be used for failover
  val frameworkId = FrameworkID.newBuilder
    .setValue(frameworkName)
    .build

  // FrameworkInfo describes a framework
  val frameworkInfo = FrameworkInfo.newBuilder
    .setName(frameworkName)
    .setId(frameworkId)
    .setUser("") // Let Mesos assign the user
    .setFailoverTimeout(3.0) // Allow a 3 second window for failover
    .build

  println(
    s"""
      |################################################################################
      | This program shows you MesosSchedulerDriver keeps trying to connect Zookeeper
      | even after MesosSchedulerDriver.run() successfully returned with DRIVER_STOPPED.
      | (Master detector seems to enter zombie state.)
      |
      |[STEPS]
      | 1. Make sure that mesos-master runs on $master.
      | 2. Press Enter
      | 3. Please wait until this program(framework) will be registered to mesos-master.
      | 4. After several seconds, Please kill Zookeeper.
      | 5. You will see MesosSchedulerDriver will stop with DRIVER_STOPPED.
      | 6. Logs which show it keeps trying to connect Zookeeper will be continued.
      |    And this program never exits.
      | 7. If you re-started zookeeper, you will see master detector will detect new
      |    master.
      |################################################################################
      |""".stripMargin)
  print(s"Press enter (Does mesos-master runs on ${master} ??): ")
  System.in.read
  print("\n\n")

  // Create the scheduler, the driver, and run it
  val scheduler = new DummyScheduler()
  val driver = new MesosSchedulerDriver(scheduler, frameworkInfo, master)

  // start mesos scheduler driver in another thread.
  val driverThread = future {
    driver.run()
  }
  driverThread.onComplete {
    case Success(status) => log.info(
      s"""
        |################################################################################
        | MesosSchedulerDriver stopped with status $status.
        | You will see it keeps trying to connect to Zookeeper and this program never
        | exit.  If you re-started Zookeeper, new master will be detected by master
        | detector.
        |################################################################################
      """.stripMargin)

    case Failure(t) => log.info(
      s"""
        |################################################################################
        | MesosSchedulerDriver stopped with error: $t
        |################################################################################
      """.stripMargin)
  }

  def suicide() = {
    driver.stop(true)
    log.info(
      """
        |################################################################################
        | Shutting down MesosSchdulerDriver.
        |################################################################################
      """.stripMargin)
  }
}