# ISPyB Server Administrator’s Guide - OBSOLETE -

## How to stop / restart jboss

**Not possible to connect to ISPyB application or connection is strange** (for
example "error in getting var" message, or jboss very slow, or ...). The server
is not responding.

Connect to ispyb machine (pyproserv) as `delageni` or any user account with
password.

 * Run `ispyb stop` to stop the production ISPyB application. The command asks
   for confirmation, triggers the stop, then waits for stop to complete. It
   should return within less than 1 minute (typically 10-20 seconds).

 * If `ispyb stop` times out (around 3-5 minutes) and tells that Jboss
   could not be stopped, run `ispyb stop force` to kill the faulty Java
   process. Killing occurs only after the original timeout, so you have to wait
   2or 3 minutes. If it still fails, nothing more can be done, you can ask an
   administrator of the machine to kill the java processes.

 * Once Jboss (Java) is stopped, or if it has crashed and was not running,
   restart ISPyB running `ispyb start`. The command returns in a few seconds
   tbut he actual start takes a few minutes (typically 1 minute). Do not
   battempt rowsing to the ISPyB URL before a few minutes.

 * Check [ISPyB application](http://ispyb.esrf.fr/). If behavior did not
   improve, or if it is still strange, make another attempt at stop / start.

 * If it is still not starting well, you may try the [software failover
   procedure](http://intranet.esrf.fr/tid/SC/share-1/how-to-restart-ispyb#software-failover-procedure).

Look at the jboss console log in `/ispyb/logs/jboss_console.log` and the
application logs : `server.log`, `ispyb.log` in `/ispyb/jboss/standalone/log`.

If the error persists, stop again jboss, check the jboss processes by the `top`
command or `ps-ef | grep java` , kill all jboss-java processes and restart
again, for this you will need to connect as su.

**Only if absolutely necessary** (the server seems completely down) and the
previous commands did not change the status of the machine:

 * Reboot the server:

    ```
    $ ispyb reboot
    ```

## Some useful commands on ISPyB server (pyproserv)

List of opened files:

```
$ ispyb check lsof
```

List of useful ispyb commands:

```
$ ispyb help
```

Display the Memory and CPU info:

```
$ top  
```

Display the console log:

```
$ tail -f /ispyb/logs/jboss_console.log
```

Display the server log:

```
$ tail -f /ispyb/jboss/standalone/log/server.log
```

Display the ispyb specific log (with a lot more logs inside):

```
$ tail -f /ispyb/jboss/standalone/log/ispyb.log
```

Reboot the server:

```
$ ispyb reboot
```
