#!/bin/ksh


# -----------------------------------------------------------------------------
#
# script for launching Ant using the Launcher
#
# -----------------------------------------------------------------------------

#
# Validate the parameters
#
if [[ -z $1 ]]
then
  echo "[ERROR `date '+%Y-%m-%d %H:%M:%S'`] Target Job is unknown!"
  echo "[INFO `date '+%Y-%m-%d %H:%M:%S'`] Usage:  runbatch.sh <target_name>"
  exit 1
fi

# Setup the environment
BATCH_SCRIPT_HOME="/usr/local/ecomm/batch/photoomni/jobs/bin/scripts"
. $BATCH_SCRIPT_HOME/set_environment.ksh

#
# This will execute the Launcher using ANT target from ./launcher/launcher.xml
# USAGE:
#	    "$JAVA_HOME"/bin/java -classpath "$LAUNCHER_PATH" LauncherBootstrap -verbose "$@"
#
echo "[INFO `date '+%Y-%m-%d %H:%M:%S'`] Job Name: $@"
"$JAVA_HOME"/bin/java -Xms256m -Xmx512m -Dsun.net.inetaddr.ttl=600 -Djava.lib.dir="$JAVA_DEPENDENCIES_LIB_DIR" -Djava.program.dir="$JAVA_PROGRAM_DIR" -Dlog.dir="$LOG_DIR" -classpath "$LAUNCHER_PATH" LauncherBootstrap -verbose "$@"
echo "[INFO `date '+%Y-%m-%d %H:%M:%S'`] Job Ended: $@"
