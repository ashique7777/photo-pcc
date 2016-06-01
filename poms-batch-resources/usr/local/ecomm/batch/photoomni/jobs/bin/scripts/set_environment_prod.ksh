#!/bin/ksh
######################################################
# 
# Script Name: env_file.ksh
# Purpose: This script sets up the application environment for
#          the batch modules
#
######################################################

################################################################
# Setup env, and file - this will be kept in some env file later
################################################################
#DB parameters

ORACLE_HOME=/u01/app/oracle/product/12.1.0.2/client_1/
export ORACLE_HOME

PATH=$PATH:/u01/app/oracle/product/12.1.0.2/client_1/bin
export PATH

DBLOG=photoomni_batch
DBPWD="a3er1canpc\$pd0lbch"
DBSID=svc_prd_pomni_oltp

export DBLOG
export DBPWD
export DBSID

# To be used in run_batch_procudure.sh
TWO_TASK=$DBSID
export TWO_TASK

XX_DB_SQLCONNECT=$DBLOG/$DBPWD
export XX_DB_SQLCONNECT

# Directories Path
BASE_PATH=/usr/local/ecomm/batch/photoomni
export BASE_PATH

# Directory where error log files are created.
LOG_DIR=/usr/local/ecomm/logs/photoomni/
export LOG_DIR

# Directory where files are archived.
ARCHIVE_DIR=/usr/local/ecomm/data/photoomni/admin/archive/
export ARCHIVE_DIR

# Launcher Path.
LAUNCHER_PATH=/usr/local/ecomm/batch/photoomni/jobs/bin/scripts/launcher
export LAUNCHER_PATH

# Java Lib.
JAVA_DEPENDENCIES_LIB_DIR=/usr/local/ecomm/batch/photoomni/jobs/javalib
export JAVA_DEPENDENCIES_LIB_DIR

# Directory where files are archived.
JAVA_PROGRAM_DIR=/usr/local/ecomm/batch/photoomni/jobs/bin/programs
export JAVA_PROGRAM_DIR

# Java Home.
JAVA_HOME=/usr/local/ecomm/batch/java/jdk1.7.0_79
export JAVA_HOME

echo "Environment variables set successfully"
