# [[MESOS-1227]](https://issues.apache.org/jira/browse/MESOS-1227): MesosSchedulerDriver(Java) enters zombie state when Zookeeper suddenly dies.

This repo is created to show you how MesosSchedulerDriver keeps trying to connect Zookeeper even after MesosSchedulerDriver.run() successfully returned with DRIVER_STOPPED [[MESOS-1227]](https://issues.apache.org/jira/browse/MESOS-1227).

This issue seems to reproduce when Zookeeper died suddenly.  Master detector seems to enter zombie state.

## How to run this program
```
$ mvn package

# start zookeeper and mesos-master/slave
# make sure that mesos-master runs on zk://localhost:2181/mesos

$ ./bin/start zk://localhost:2181/mesos

# follow the instructions which will be printed.
```

## Sample console log (sample_log.txt)
```
$ ./bin/start zk://localhost:2181/mesos
MESOS_NATIVE_LIBRARY is not set. Searching in /usr/lib /usr/local/lib.
MESOS_NATIVE_LIBRARY is set to /usr/local/lib/libmesos.so

################################################################################
 This program shows you MesosSchedulerDriver keeps trying to connect Zookeeper
 even after MesosSchedulerDriver.run() successfully returned with DRIVER_STOPPED.
 (Master detector seems to enter zombie state.)

[STEPS]
 1. Make sure that mesos-master runs on zk://localhost:2181/mesos.
 2. Press Enter
 3. Please wait until this program(framework) will be registered to mesos-master.
 4. After several seconds, Please kill Zookeeper.
 5. You will see MesosSchedulerDriver will stop with DRIVER_STOPPED.
 6. Logs which show it keeps trying to connect Zookeeper will be continued.
    And this program never exits.
 7. If you re-started zookeeper, you will see master detector will detect new
    master.
################################################################################

Press enter (Does mesos-master runs on zk://localhost:2181/mesos ??):<Enter>

2014-04-17 16:30:59,928:2626(0x7fece67fc700):ZOO_INFO@log_env@712: Client environment:zookeeper.version=zookeeper C client 3.4.5
2014-04-17 16:30:59,928:2626(0x7fece67fc700):ZOO_INFO@log_env@716: Client environment:host.name=mesos
2014-04-17 16:30:59,928:2626(0x7fece67fc700):ZOO_INFO@log_env@723: Client environment:os.name=Linux
2014-04-17 16:30:59,928:2626(0x7fece67fc700):ZOO_INFO@log_env@724: Client environment:os.arch=3.8.0-35-generic
2014-04-17 16:30:59,928:2626(0x7fece67fc700):ZOO_INFO@log_env@725: Client environment:os.version=#50-Ubuntu SMP Tue Dec 3 01:24:59 UTC 2013
2014-04-17 16:30:59,928:2626(0x7fece67fc700):ZOO_INFO@log_env@733: Client environment:user.name=vagrant
2014-04-17 16:30:59,929:2626(0x7fece67fc700):ZOO_INFO@log_env@741: Client environment:user.home=/home/vagrant
2014-04-17 16:30:59,929:2626(0x7fece67fc700):ZOO_INFO@log_env@753: Client environment:user.dir=/mesos-driver-enters-zombie
2014-04-17 16:30:59,929:2626(0x7fece67fc700):ZOO_INFO@zookeeper_init@786: Initiating client connection, host=localhost:2181 sessionTimeout=10000 watcher=0x7fed15d00400 sessionId=0 sessionPasswd=<null> context=0x7fecc4003360 flags=0
2014-04-17 16:30:59,936:2626(0x7fece37f6700):ZOO_INFO@check_events@1703: initiated connection to server [127.0.0.1:2181]
2014-04-17 16:30:59,936:2626(0x7fece37f6700):ZOO_INFO@check_events@1750: session establishment complete on server [127.0.0.1:2181], sessionId=0x145708692780003, negotiated timeout=10000
I0417 16:30:59.937185  2642 group.cpp:310] Group process ((2)@127.0.1.1:47054) connected to ZooKeeper
I0417 16:30:59.937234  2642 group.cpp:752] Syncing group operations: queue size (joins, cancels, datas) = (0, 0, 0)
I0417 16:30:59.937248  2642 group.cpp:367] Trying to create path '/mesos' in ZooKeeper
I0417 16:30:59.945961  2642 detector.cpp:134] Detected a new leader: (id='5')
I0417 16:30:59.946210  2644 group.cpp:629] Trying to get '/mesos/info_0000000005' in ZooKeeper
I0417 16:30:59.948453  2644 detector.cpp:351] A new leading master (UPID=master@127.0.1.1:5050) is detected
I0417 16:30:59.949002  2644 sched.cpp:218] No credentials provided. Attempting to register without authentication
I0417 16:30:59.949201  2644 sched.cpp:230] Detecting new master
Apr 17, 2014 4:30:59 PM mesos_driver_check.DummyScheduler registered
INFO:
###############################################################################
 The framework is registered with FrameworkId=mesos-scheduler-check-dummy-0.0.1.
 After several seconds, please stop Zookeeper.
###############################################################################

< Zookeeper was stopped manually at this point. >

2014-04-17 16:31:12,899:2626(0x7fece37f6700):ZOO_ERROR@handle_socket_error_msg@1721: Socket [127.0.0.1:2181] zk retcode=-4, errno=112(Host is down): failed while receiving a server response
I0417 16:31:12.899622  2644 group.cpp:400] Lost connection to ZooKeeper, attempting to reconnect ...
2014-04-17 16:31:16,235:2626(0x7fece37f6700):ZOO_WARN@zookeeper_interest@1557: Exceeded deadline by 2939ms
2014-04-17 16:31:16,235:2626(0x7fece37f6700):ZOO_ERROR@handle_socket_error_msg@1697: Socket [127.0.0.1:2181] zk retcode=-4, errno=111(Connection refused): server refused to accept the client
2014-04-17 16:31:19,574:2626(0x7fece37f6700):ZOO_ERROR@handle_socket_error_msg@1697: Socket [127.0.0.1:2181] zk retcode=-4, errno=111(Connection refused): server refused to accept the client
W0417 16:31:22.904620  2643 group.cpp:435] Timed out waiting to reconnect to ZooKeeper (sessionId=145708692780003)
I0417 16:31:22.904707  2641 detector.cpp:134] Detected a new leader: None
2014-04-17 16:31:22,905:2626(0x7fece57fa700):ZOO_INFO@zookeeper_close@2522: Freeing zookeeper resources for sessionId=0x145708692780003

2014-04-17 16:31:22,906:2626(0x7fece57fa700):ZOO_INFO@log_env@712: Client environment:zookeeper.version=zookeeper C client 3.4.5
2014-04-17 16:31:22,906:2626(0x7fece57fa700):ZOO_INFO@log_env@716: Client environment:host.name=mesos
2014-04-17 16:31:22,906:2626(0x7fece57fa700):ZOO_INFO@log_env@723: Client environment:os.name=Linux
2014-04-17 16:31:22,906:2626(0x7fece57fa700):ZOO_INFO@log_env@724: Client environment:os.arch=3.8.0-35-generic
2014-04-17 16:31:22,906:2626(0x7fece57fa700):ZOO_INFO@log_env@725: Client environment:os.version=#50-Ubuntu SMP Tue Dec 3 01:24:59 UTC 2013
2014-04-17 16:31:22,906:2626(0x7fece57fa700):ZOO_INFO@log_env@733: Client environment:user.name=vagrant
2014-04-17 16:31:22,906:2626(0x7fece57fa700):ZOO_INFO@log_env@741: Client environment:user.home=/home/vagrant
2014-04-17 16:31:22,906:2626(0x7fece57fa700):ZOO_INFO@log_env@753: Client environment:user.dir=/mesos-driver-enters-zombie
2014-04-17 16:31:22,906:2626(0x7fece57fa700):ZOO_INFO@zookeeper_init@786: Initiating client connection, host=localhost:2181 sessionTimeout=10000 watcher=0x7fed15d00400 sessionId=0 sessionPasswd=<null> context=0x250e3a0 flags=0
Apr 17, 2014 4:31:22 PM mesos_driver_check.DummyScheduler disconnected
INFO:
################################################################################
 The framework is disconnected.  It will commit to suicide.
################################################################################

Apr 17, 2014 4:31:22 PM mesos_driver_check.Main$ suicide
INFO:
################################################################################
 Shutting down MesosSchdulerDriver.
################################################################################

I0417 16:31:22.912137  2641 sched.cpp:230] Detecting new master
2014-04-17 16:31:22,913:2626(0x7fece2ff5700):ZOO_ERROR@handle_socket_error_msg@1697: Socket [127.0.0.1:2181] zk retcode=-4, errno=111(Connection refused): server refused to accept the client
Apr 17, 2014 4:31:22 PM mesos_driver_check.Main$$anonfun$2 apply
INFO:
################################################################################
 MesosSchedulerDriver stopped with status DRIVER_STOPPED.
 You will see it keeps trying to connect to Zookeeper and this program never
 exit.  If you re-started Zookeeper, new master will be detected by master
 detector.
################################################################################

2014-04-17 16:31:26,250:2626(0x7fece2ff5700):ZOO_ERROR@handle_socket_error_msg@1697: Socket [127.0.0.1:2181] zk retcode=-4, errno=111(Connection refused): server refused to accept the client
2014-04-17 16:31:29,586:2626(0x7fece2ff5700):ZOO_ERROR@handle_socket_error_msg@1697: Socket [127.0.0.1:2181] zk retcode=-4, errno=111(Connection refused): server refused to accept the client
2014-04-17 16:31:32,924:2626(0x7fece2ff5700):ZOO_ERROR@handle_socket_error_msg@1697: Socket [127.0.0.1:2181] zk retcode=-4, errno=111(Connection refused): server refused to accept the client
2014-04-17 16:31:36,261:2626(0x7fece2ff5700):ZOO_ERROR@handle_socket_error_msg@1697: Socket [127.0.0.1:2181] zk retcode=-4, errno=111(Connection refused): server refused to accept the client
2014-04-17 16:31:39,597:2626(0x7fece2ff5700):ZOO_ERROR@handle_socket_error_msg@1697: Socket [127.0.0.1:2181] zk retcode=-4, errno=111(Connection refused): server refused to accept the client
2014-04-17 16:31:42,935:2626(0x7fece2ff5700):ZOO_ERROR@handle_socket_error_msg@1697: Socket [127.0.0.1:2181] zk retcode=-4, errno=111(Connection refused): server refused to accept the client
2014-04-17 16:31:46,272:2626(0x7fece2ff5700):ZOO_ERROR@handle_socket_error_msg@1697: Socket [127.0.0.1:2181] zk retcode=-4, errno=111(Connection refused): server refused to accept the client
2014-04-17 16:31:49,609:2626(0x7fece2ff5700):ZOO_ERROR@handle_socket_error_msg@1697: Socket [127.0.0.1:2181] zk retcode=-4, errno=111(Connection refused): server refused to accept the client
2014-04-17 16:31:52,944:2626(0x7fece2ff5700):ZOO_ERROR@handle_socket_error_msg@1697: Socket [127.0.0.1:2181] zk retcode=-4, errno=111(Connection refused): server refused to accept the client
2014-04-17 16:31:56,281:2626(0x7fece2ff5700):ZOO_ERROR@handle_socket_error_msg@1697: Socket [127.0.0.1:2181] zk retcode=-4, errno=111(Connection refused): server refused to accept the client
2014-04-17 16:31:59,618:2626(0x7fece2ff5700):ZOO_ERROR@handle_socket_error_msg@1697: Socket [127.0.0.1:2181] zk retcode=-4, errno=111(Connection refused): server refused to accept the client
.... identical log continues ...
2014-04-17 17:19:53,089:2626(0x7fece2ff5700):ZOO_ERROR@handle_socket_error_msg@1697: Socket [127.0.0.1:2181] zk retcode=-4, errno=111(Connection refused): server refused to accept the client
2014-04-17 17:19:56,427:2626(0x7fece2ff5700):ZOO_ERROR@handle_socket_error_msg@1697: Socket [127.0.0.1:2181] zk retcode=-4, errno=111(Connection refused): server refused to accept the client
... still going on ...
```
