# MesosSchedulerDriver(Java) enters zombie state

This repo is created to show you how MesosSchedulerDriver keeps trying to connect Zookeeper even after MesosSchedulerDriver.run() successfully returned with DRIVER_STOPPED. 

This issue seems to reproduce when Zookeeper died suddenly.  Master detector seems to enter zombie state.

## How to run this program
```
$ mvn package

# start zookeeper and mesos-master/slave
# make sure that mesos-master runs on zk://localhost:2181/mesos

$ ./bin/start zk://localhost:2181/mesos

# follow the instructions which will be printed.
```

## Sample console log
```
$ ./bin/start --jvm-debug 4000 zk://localhost:2181/mesos
MESOS_NATIVE_LIBRARY is not set. Searching in /usr/lib /usr/local/lib.
MESOS_NATIVE_LIBRARY is set to /usr/local/lib/libmesos.so
Listening for transport dt_socket at address: 4000

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

Press enter (Does mesos-master runs on zk://localhost:2181/mesos ??): <Enter>

2014-04-17 04:54:23,988:14722(0x7fb953dc6700):ZOO_INFO@log_env@712: Client environment:zookeeper.version=zookeeper C client 3.4.5
2014-04-17 04:54:23,988:14722(0x7fb953dc6700):ZOO_INFO@log_env@716: Client environment:host.name=mesos
2014-04-17 04:54:23,988:14722(0x7fb953dc6700):ZOO_INFO@log_env@723: Client environment:os.name=Linux
2014-04-17 04:54:23,988:14722(0x7fb953dc6700):ZOO_INFO@log_env@724: Client environment:os.arch=3.8.0-35-generic
2014-04-17 04:54:23,989:14722(0x7fb953dc6700):ZOO_INFO@log_env@725: Client environment:os.version=#50-Ubuntu SMP Tue Dec 3 01:24:59 UTC 2013
2014-04-17 04:54:23,989:14722(0x7fb953dc6700):ZOO_INFO@log_env@733: Client environment:user.name=vagrant
2014-04-17 04:54:23,990:14722(0x7fb953dc6700):ZOO_INFO@log_env@741: Client environment:user.home=/home/vagrant
2014-04-17 04:54:23,990:14722(0x7fb953dc6700):ZOO_INFO@log_env@753: Client environment:user.dir=/mesos-driver-check
2014-04-17 04:54:23,990:14722(0x7fb953dc6700):ZOO_INFO@zookeeper_init@786: Initiating client connection, host=localhost:2181 sessionTimeout=10000 watcher=0x7
fb988f75400 sessionId=0 sessionPasswd=<null> context=0x7fb96400d080 flags=0
2014-04-17 04:54:23,994:14722(0x7fb94f5bd700):ZOO_ERROR@handle_socket_error_msg@1697: Socket [127.0.0.1:2181] zk retcode=-4, errno=111(Connection refused): s
erver refused to accept the client
2014-04-17 04:54:27,491:14722(0x7fb94f5bd700):ZOO_WARN@zookeeper_interest@1557: Exceeded deadline by 164ms
2014-04-17 04:54:27,491:14722(0x7fb94f5bd700):ZOO_ERROR@handle_socket_error_msg@1697: Socket [127.0.0.1:2181] zk retcode=-4, errno=111(Connection refused): s
erver refused to accept the client

2014-04-17 04:54:30,829:14722(0x7fb94f5bd700):ZOO_ERROR@handle_socket_error_msg@1697: Socket [127.0.0.1:2181] zk retcode=-4, errno=111(Connection refused): server refused to accept the client
2014-04-17 04:54:34,166:14722(0x7fb94f5bd700):ZOO_INFO@check_events@1703: initiated connection to server [127.0.0.1:2181]
2014-04-17 04:54:34,187:14722(0x7fb94f5bd700):ZOO_INFO@check_events@1750: session establishment complete on server [127.0.0.1:2181], sessionId=0x1456e0944ee0
000, negotiated timeout=10000
I0417 04:54:34.187605 14740 group.cpp:310] Group process ((2)@127.0.1.1:51833) connected to ZooKeeper
I0417 04:54:34.187657 14740 group.cpp:752] Syncing group operations: queue size (joins, cancels, datas) = (0, 0, 0)
I0417 04:54:34.187672 14740 group.cpp:367] Trying to create path '/mesos' in ZooKeeper
I0417 04:54:34.196684 14740 detector.cpp:134] Detected a new leader: (id='59')
I0417 04:54:34.196961 14739 group.cpp:629] Trying to get '/mesos/info_0000000059' in ZooKeeper
I0417 04:54:34.200134 14739 detector.cpp:351] A new leading master (UPID=master@127.0.1.1:5050) is detected
I0417 04:54:34.200357 14739 sched.cpp:218] No credentials provided. Attempting to register without authentication
I0417 04:54:34.200443 14739 sched.cpp:230] Detecting new master
Apr 17, 2014 4:54:35 AM mesos_driver_check.DummyScheduler registered
INFO:
###############################################################################
 The framework is registered with FrameworkId=mesos-scheduler-check-dummy-0.0.1.
 After several seconds, please stop Zookeeper.
###############################################################################

(Zookeeper was stopped manually at this point.)

2014-04-17 04:54:42,393:14722(0x7fb94f5bd700):ZOO_ERROR@handle_socket_error_msg@1721: Socket [127.0.0.1:2181] zk retcode=-4, errno=112(Host is down): failed
while receiving a server response
I0417 04:54:42.393929 14742 group.cpp:400] Lost connection to ZooKeeper, attempting to reconnect ...
2014-04-17 04:54:45,730:14722(0x7fb94f5bd700):ZOO_WARN@zookeeper_interest@1557: Exceeded deadline by 867ms
2014-04-17 04:54:45,730:14722(0x7fb94f5bd700):ZOO_ERROR@handle_socket_error_msg@1697: Socket [127.0.0.1:2181] zk retcode=-4, errno=111(Connection refused): s
erver refused to accept the client
2014-04-17 04:54:49,068:14722(0x7fb94f5bd700):ZOO_ERROR@handle_socket_error_msg@1697: Socket [127.0.0.1:2181] zk retcode=-4, errno=111(Connection refused): server refused to accept the client
W0417 04:54:52.395151 14741 group.cpp:435] Timed out waiting to reconnect to ZooKeeper (sessionId=1456e0944ee0000)
I0417 04:54:52.395272 14743 detector.cpp:134] Detected a new leader: None
2014-04-17 04:54:52,397:14722(0x7fb9515c1700):ZOO_INFO@zookeeper_close@2522: Freeing zookeeper resources for sessionId=0x1456e0944ee0000

2014-04-17 04:54:52,399:14722(0x7fb9515c1700):ZOO_INFO@log_env@712: Client environment:zookeeper.version=zookeeper C client 3.4.5
2014-04-17 04:54:52,399:14722(0x7fb9515c1700):ZOO_INFO@log_env@716: Client environment:host.name=mesos
2014-04-17 04:54:52,400:14722(0x7fb9515c1700):ZOO_INFO@log_env@723: Client environment:os.name=Linux
2014-04-17 04:54:52,400:14722(0x7fb9515c1700):ZOO_INFO@log_env@724: Client environment:os.arch=3.8.0-35-generic
2014-04-17 04:54:52,401:14722(0x7fb9515c1700):ZOO_INFO@log_env@725: Client environment:os.version=#50-Ubuntu SMP Tue Dec 3 01:24:59 UTC 2013
2014-04-17 04:54:52,403:14722(0x7fb9515c1700):ZOO_INFO@log_env@733: Client environment:user.name=vagrant
2014-04-17 04:54:52,403:14722(0x7fb9515c1700):ZOO_INFO@log_env@741: Client environment:user.home=/home/vagrant
2014-04-17 04:54:52,403:14722(0x7fb9515c1700):ZOO_INFO@log_env@753: Client environment:user.dir=/mesos-driver-check
2014-04-17 04:54:52,404:14722(0x7fb9515c1700):ZOO_INFO@zookeeper_init@786: Initiating client connection, host=localhost:2181 sessionTimeout=10000 watcher=0x7fb988f75400 sessionId=0 sessionPasswd=<null> context=0x7fb940000a90 flags=0
Apr 17, 2014 4:54:52 AM mesos_driver_check.DummyScheduler disconnected
INFO:
################################################################################
 The framework is disconnected.  It will commit to suicide.
################################################################################

Apr 17, 2014 4:54:52 AM mesos_driver_check.Main$ suicide
INFO:
################################################################################
 Shutting down MesosSchdulerDriver.
################################################################################

I0417 04:54:52.413705 14736 sched.cpp:230] Detecting new master
2014-04-17 04:54:52,416:14722(0x7fb94edbc700):ZOO_ERROR@handle_socket_error_msg@1697: Socket [127.0.0.1:2181] zk retcode=-4, errno=111(Connection refused): server refused to accept the client
Apr 17, 2014 4:54:52 AM mesos_driver_check.Main$$anonfun$2 apply
INFO:
################################################################################
 MesosSchedulerDriver stopped with status DRIVER_STOPPED.
 You will see it keeps trying to connect to Zookeeper and this program never
 exit.  If you re-started Zookeeper, new master will be detected by master
 detector.
################################################################################

2014-04-17 04:54:55,753:14722(0x7fb94edbc700):ZOO_ERROR@handle_socket_error_msg@1697: Socket [127.0.0.1:2181] zk retcode=-4, errno=111(Connection refused): server refused to accept the client
2014-04-17 04:54:59,088:14722(0x7fb94edbc700):ZOO_ERROR@handle_socket_error_msg@1697: Socket [127.0.0.1:2181] zk retcode=-4, errno=111(Connection refused): server refused to accept the client
2014-04-17 04:55:02,425:14722(0x7fb94edbc700):ZOO_ERROR@handle_socket_error_msg@1697: Socket [127.0.0.1:2181] zk retcode=-4, errno=111(Connection refused): server refused to accept the client
2014-04-17 04:55:05,764:14722(0x7fb94edbc700):ZOO_ERROR@handle_socket_error_msg@1697: Socket [127.0.0.1:2181] zk retcode=-4, errno=111(Connection refused): server refused to accept the client
... continues forever
```