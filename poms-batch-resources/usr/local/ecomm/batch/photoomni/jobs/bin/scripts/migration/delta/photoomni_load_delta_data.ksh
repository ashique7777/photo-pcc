#! /bin/ksh

# Setup the environment
env_file="/usr/local/ecomm/batch/photoomni/jobs/bin/scripts/set_environment.ksh";
. $env_file

rootDir=/usr/local/ecomm/batch/photoomni/jobs/bin/scripts
CONTROL_FILE_PATH=/usr/local/ecomm/batch/photoomni/jobs/bin/scripts/controlfiles/migration
ERROR_LOG=/usr/local/ecomm/logs/photoomni/
DELTA_ZIP_DIR=/usr/local/ecomm/data/photoomni/ftp/migration/delta
flatFileDir=/usr/local/ecomm/data/photoomni/work/migration/delta
PLSQL_FILE_PATH=/usr/local/ecomm/batch/photoomni/jobs/bin/scripts/dbscripts/migration
ARCHIVE=/usr/local/ecomm/data/photoomni/admin/archive/migration/delta

update_status()
{
sqlplus -s $DBLOG/$DBPWD << SQL
update Om_transfer_job_status set ind='$3', job_end_tm=sysdate,  error_desc='$1'  where JOB_ID=$2;
SQL
}

###common procedures
invoke_sqlLoader()
{
##TRUNCATE
ksh /usr/local/oracle/bin/run_batch_procedure.sh -t $4 -f TRUNCATE_TABLE
EXITSTATUS=$?
if [ $EXITSTATUS != 0 ]
then
print "Truncate table failed for $4 table." >> $ERROR_LOG 
error_desc="Truncate table failed for $4"
update_status "$error_desc" $1 "F"
exit 1
fi

###INVOKING SQL LOADER 
sqlldr $DBLOG/$DBPWD@$DBSID $CONTROL_FILE_PATH/$2 $log $bad $flatFileDir/$3 $dis SKIP_UNUSABLE_INDEXES=TRUE SKIP_INDEX_MAINTENANCE=TRUE direct=y

## Checking exit code from SQL Loader
EXITSTATUS=$?
case "$EXITSTATUS" in 
	0) echo "SQL*Loader execution successful" 
	error_desc="sql loader successfully invoked for $4(GENERIC Load Job)"
	#update_status "$error_desc" $1 "W"      
	;; 
	1) echo "SQL*Loader execution exited with EX_FAIL, see logfile" 
	error_desc="SQL*Loader execution exited with EX_FAIL for $4(GENERIC Load Job)"
	#update_status "$error_desc" $1 "F"
	exit 1          
	;;  
	2) echo "SQL*Loader exectuion exited with EX_WARN, see logfile" 
	error_desc="SQL*Loader execution exited with EX_WARN for $4(GENERIC Load Job)"
	#update_status "$error_desc" $1 "W"          
	;;  
	3) echo "SQL*Loader execution encountered a fatal error" 
	error_desc="SQL*Loader execution exited with EX_FATAL for $4(GENERIC Load Job)"
	#update_status "$error_desc" $1 "F" 
	exit 1
	;;  
	*) echo "unknown exit code"
	error_desc="SQL*Loader execution exited with ERROR for $4(GENERIC Load Job)"
	#update_status "$error_desc" $1 "F" 
	exit 1
	 ;; 
	esac 

#REBUILDING INDEX
ksh /usr/local/oracle/bin/run_batch_procedure.sh -t $4 -f REBUILD_INDEXES
EXITSTATUS=$?
if [ $EXITSTATUS != 0 ]
then
    print "Alter Index  failed for $4" >> $ERROR_LOG 
    error_desc="Alter Index  failed for $4"
    #update_status "$error_desc" $1 "F"
    exit 1
fi
}

######### MAIN PROGRAM STARTS ##########
# Create ErrorLog file with current date
cd $ERROR_LOG
touch `date +"%d-%m-%y"`PhotoOmniLoadProcessLog.log
## Error Log File
ERROR_LOG=$ERROR_LOG`date +"%d-%m-%y"`PhotoOmniLoadProcessLog.log
## sql loader Log file
log=$ERROR_LOG`date +"%d-%m-%y"`PhotoOmniLoad.log
## sql loader Bad file
bad=$ERROR_LOG`date +"%d-%m-%y"`PhotoOmniLoad.bad
## sql loader Discard file
dis=$ERROR_LOG`date +"%d-%m-%y"`PhotoOmniLoad.dis

##log entry in the table for this job
jobId=`sqlplus -S $DBLOG/$DBPWD << SQL   
set heading off;
select Om_transfer_job_status_seq.nextval "curr" from dual;
<<`
sqlplus -S $DBLOG/$DBPWD << SQL
insert into Om_transfer_job_status values($jobId,'BATCH - PHOTO OMNI DELTA DATA LOAD','W',sysdate,'JOB START(PHOTO OMNI DATA LOAD)',sysdate,sysdate,'photo',sysdate,'photo',sysdate);
SQL

#####################EXTRACT ZIP FILES####################

for f in `ls $DELTA_ZIP_DIR/*.zip -rt`
do
	echo "Lets now clear these Flat Files from Working Directory" >> $ERROR_LOG
	rm -f $flatFileDir/*.*
	
	echo "Lets now move the current zip file to Working Directory" >> $ERROR_LOG
	mv $f $flatFileDir
	
	echo "Extracting zip file in Working Directory" >> $ERROR_LOG
	cd $flatFileDir
	unzip -qo *.zip
	
	#####MOVE TO ARCHIVE##########
	mv *.zip  $ARCHIVE
	
	###################### pf_bundle_product_assoc ######################	
	invoke_sqlLoader $jobId "pf_bundle_product_assoc.ctrl" "pf_bundle_product_assoc.txt" "OM_PF_BUNDLE_PRODUCT_ASSOC_TMP"	
	echo "Loaded Table OM_PF_BUNDLE_PRODUCT_ASSOC_TMP"

	###################### pf_device_dtl ######################	
	invoke_sqlLoader $jobId "pf_device_dtl.ctrl" "pf_device_dtl.txt" "OM_PF_DEVICE_DTL_TMP"	
	echo "Loaded Table OM_PF_DEVICE_DTL_TMP"

	###################### pf_equipment ######################	
	invoke_sqlLoader $jobId "pf_equipment.ctrl" "pf_equipment.txt" "OM_PF_EQUIPMENT_TMP"	
	echo "Loaded Table OM_PF_EQUIPMENT_TMP"

	###################### pf_event_dist_crit ######################	
	invoke_sqlLoader $jobId "pf_event_dist_crit.ctrl" "pf_event_dist_crit.txt" "OM_PF_EVENT_DIST_CRIT_TMP"	
	echo "Loaded Table OM_PF_EVENT_DIST_CRIT_TMP"

	###################### pf_event_dist_store ######################	
	invoke_sqlLoader $jobId "pf_event_dist_store.ctrl" "pf_event_dist_store.txt" "OM_PF_EVENT_DIST_STORE_TMP"	
	echo "Loaded Table OM_PF_EVENT_DIST_STORE_TMP"

	###################### pf_event_distribution_mapping ######################	
	invoke_sqlLoader $jobId "pf_event_distribution_mapping.ctrl" "pf_event_distribution_mapping.txt" "OM_PF_EVENT_DIST_MAPPING_TMP"	
	echo "Loaded Table OM_PF_EVENT_DIST_MAPPING_TMP"

	###################### pf_event_header ######################	
	invoke_sqlLoader $jobId "pf_event_header.ctrl" "pf_event_header.txt" "OM_PF_EVENT_HEADER_TMP"	
	echo "Loaded Table OM_PF_EVENT_HEADER_TMP"

	###################### pf_event_image_mapping ######################	
	invoke_sqlLoader $jobId "pf_event_image_mapping.ctrl" "pf_event_image_mapping.txt" "OM_PF_EVENT_IMAGE_MAPPING_TMP"	
	echo "Loaded Table OM_PF_EVENT_IMAGE_MAPPING_TMP"

	###################### pf_exception_type ######################	
	invoke_sqlLoader $jobId "pf_exception_type.ctrl" "pf_exception_type.txt" "OM_PF_EXCEPTION_TYPE_TMP"	
	echo "Loaded Table OM_PF_EXCEPTION_TYPE_TMP"

	###################### pf_kiosk_product_map ######################	
	invoke_sqlLoader $jobId "pf_kiosk_product_map.ctrl" "pf_kiosk_product_map.txt" "OM_PF_KIOSK_PRODUCT_MAP_TMP"	
	echo "Loaded Table OM_PF_KIOSK_PRODUCT_MAP_TMP"

	###################### pf_kiosk_template ######################	
	invoke_sqlLoader $jobId "pf_kiosk_template.ctrl" "pf_kiosk_template.txt" "OM_PF_KIOSK_TEMPLATE_TMP"	
	echo "Loaded Table OM_PF_KIOSK_TEMPLATE_TMP"

	###################### pf_machine_equipment ######################	
	invoke_sqlLoader $jobId "pf_machine_equipment.ctrl" "pf_machine_equipment.txt" "OM_PF_MACHINE_EQUIPMENT_TMP"	
	echo "Loaded Table OM_PF_MACHINE_EQUIPMENT_TMP"

	###################### pf_machine_type ######################	
	invoke_sqlLoader $jobId "pf_machine_type.ctrl" "pf_machine_type.txt" "OM_PF_MACHINE_TYPE_TMP"	
	echo "Loaded Table OM_PF_MACHINE_TYPE_TMP"

	###################### pf_mb_promotional_money ######################	
	invoke_sqlLoader $jobId "pf_mb_promotional_money.ctrl" "pf_mb_promotional_money.txt" "OM_PF_MB_PROMOTION_MONEY_TMP"	
	echo "Loaded Table OM_PF_MB_PROMOTION_MONEY_TMP"

	###################### pf_prod_retail_lvl_price ######################	
	invoke_sqlLoader $jobId "pf_prod_retail_lvl_price.ctrl" "pf_prod_retail_lvl_price.txt" "OM_PF_PROD_RET_LVL_PRICE_TMP"	
	echo "Loaded Table OM_PF_PROD_RET_LVL_PRICE_TMP"

	###################### pf_product ######################	
	invoke_sqlLoader $jobId "pf_product.ctrl" "pf_product.txt" "OM_PF_PRODUCT_TMP"	
	echo "Loaded Table OM_PF_PRODUCT_TMP"

	###################### pf_product_equip_cost ######################	
	invoke_sqlLoader $jobId "pf_product_equip_cost.ctrl" "pf_product_equip_cost.txt" "OM_PF_PRODUCT_EQUIP_COST_TMP"	
	echo "Loaded Table OM_PF_PRODUCT_EQUIP_COST_TMP"

	###################### pf_product_extn ######################	
	invoke_sqlLoader $jobId "pf_product_extn.ctrl" "pf_product_extn.txt" "OM_PF_PRODUCT_EXTN_TMP"	
	echo "Loaded Table OM_PF_PRODUCT_EXTN_TMP"

	###################### pf_product_machine_equip ######################	
	invoke_sqlLoader $jobId "pf_product_machine_equip.ctrl" "pf_product_machine_equip.txt" "OM_PF_PROD_MACHINE_EQUIP_TMP"	
	echo "Loaded Table OM_PF_PROD_MACHINE_EQUIP_TMP"

	###################### pf_promo_money_store_assoc ######################	
	invoke_sqlLoader $jobId "pf_promo_money_store_assoc.ctrl" "pf_promo_money_store_assoc.txt" "OM_PF_PROMO_MNY_STR_ASSOC_TMP"	
	echo "Loaded Table OM_PF_PROMO_MNY_STR_ASSOC_TMP"

	###################### pf_promo_trgt_prod_assoc ######################	
	invoke_sqlLoader $jobId "pf_promo_trgt_prod_assoc.ctrl" "pf_promo_trgt_prod_assoc.txt" "OM_PF_PROMO_TGT_PROD_ASSOC_TMP"	
	echo "Loaded Table OM_PF_PROMO_TGT_PROD_ASSOC_TMP"

	###################### pf_promo_trggr_prod_assoc ######################	
	invoke_sqlLoader $jobId "pf_promo_trggr_prod_assoc.ctrl" "pf_promo_trggr_prod_assoc.txt" "OM_PF_PROMO_TRGR_PRD_ASSOC_TMP"	
	echo "Loaded Table OM_PF_PROMO_TRGR_PRD_ASSOC_TMP"

	###################### pf_promotion ######################	
	invoke_sqlLoader $jobId "pf_promotion.ctrl" "pf_promotion.txt" "OM_PF_PROMOTION_TMP"	
	echo "Loaded Table OM_PF_PROMOTION_TMP"

	###################### pf_promotion_rule ######################	
	invoke_sqlLoader $jobId "pf_promotion_rule.ctrl" "pf_promotion_rule.txt" "OM_PF_PROMOTION_RULE_TMP"	
	echo "Loaded Table OM_PF_PROMOTION_RULE_TMP"

	###################### pf_promotional_money ######################	
	invoke_sqlLoader $jobId "pf_promotional_money.ctrl" "pf_promotional_money.txt" "OM_PF_PROMOTION_MONEY_TMP"	
	echo "Loaded Table OM_PF_PROMOTION_MONEY_TMP"

	###################### pf_promotional_money_tier ######################	
	invoke_sqlLoader $jobId "pf_promotional_money_tier.ctrl" "pf_promotional_money_tier.txt" "OM_PF_PROMOTION_MONEY_TIER_TMP"	
	echo "Loaded Table OM_PF_PROMOTION_MONEY_TIER_TMP"

	###################### pf_promotionalmoneytier ######################	
	invoke_sqlLoader $jobId "pf_promotionalmoneytier.ctrl" "pf_promotionalmoneytier.txt" "OM_PF_MBPRMOTNMNY_TIER_TMP"	
	echo "Loaded Table OM_PF_MBPRMOTNMNY_TIER_TMP"

	###################### pf_store_machine_assoc ######################	
	invoke_sqlLoader $jobId "pf_store_machine_assoc.ctrl" "pf_store_machine_assoc.txt" "OM_PF_STORE_MACHINE_ASSOC_TMP"	
	echo "Loaded Table OM_PF_STORE_MACHINE_ASSOC_TMP"

	###################### pf_store_machine_equip ######################	
	invoke_sqlLoader $jobId "pf_store_machine_equip.ctrl" "pf_store_machine_equip.txt" "OM_PF_STORE_MACHINE_EQUIP_TMP"	
	echo "Loaded Table OM_PF_STORE_MACHINE_EQUIP_TMP"

	###################### pf_store_master ######################	
	invoke_sqlLoader $jobId "pf_store_master.ctrl" "pf_store_master.txt" "OM_PF_STORE_MASTER_TMP"	
	echo "Loaded Table OM_PF_STORE_MASTER_TMP"

	###################### pf_store_vendor_assoc ######################	
	invoke_sqlLoader $jobId "pf_store_vendor_assoc.ctrl" "pf_store_vendor_assoc.txt" "OM_PF_STORE_VENDOR_ASSOC_TMP"	
	echo "Loaded Table OM_PF_STORE_VENDOR_ASSOC_TMP"

	###################### pf_template ######################	
	invoke_sqlLoader $jobId "pf_template.ctrl" "pf_template.txt" "OM_PF_TEMPLATE_TMP"	
	echo "Loaded Table OM_PF_TEMPLATE_TMP"

	###################### pf_vendor ######################	
	invoke_sqlLoader $jobId "pf_vendor.ctrl" "pf_vendor.txt" "OM_PF_VENDOR_TMP"	
	echo "Loaded Table OM_PF_VENDOR_TMP"

	###################### pf_vendor_product ######################	
	invoke_sqlLoader $jobId "pf_vendor_product.ctrl" "pf_vendor_product.txt" "OM_PF_VENDOR_PRODUCT_TMP"	
	echo "Loaded Table OM_PF_VENDOR_PRODUCT_TMP"

    ###################### pf_store_parameter ######################
    invoke_sqlLoader $jobId "pf_store_parameter.ctrl" "pf_store_parameter.txt" "OM_PF_STORE_PARAMETER_TMP"
    echo "Loaded Table OM_PF_STORE_PARAMETER_TMP"

    ###################### pf_parameter_master ######################
    invoke_sqlLoader $jobId "pf_parameter_master.ctrl" "pf_parameter_master.txt" "OM_PF_PARAMETER_MASTER_TMP"
    echo "Loaded Table OM_PF_PARAMETER_MASTER_TMP"
	
	###################### pf_downtime_reason ######################	
	invoke_sqlLoader $jobId "pf_downtime_reason.ctrl" "pf_downtime_reason.txt" "OM_PF_DOWNTIME_REASON_TMP"	
	echo "Loaded Table OM_PF_DOWNTIME_REASON_TMP"
		
	echo ".................................Started PLSQL.........................................."
	######### PL/SQL CALL starts ###############
	print "Before Invoking the stored procedure" >> $ERROR_LOG
	sqlplus -s $DBLOG/$DBPWD @$rootDir/dbscripts/migration/call_migration_plsql_all.sql "$rootDir/dbscripts/migration"
	######### PL/SQL CALL ends ###############
done

### DELETE ARCHIVE FILES OLDER THAN 30 DAYS
find $ARCHIVE -type f -name "*.*" -mtime +30 -exec rm  {} \;

update_status "Job Completed Successfully" $jobId "S"

print "Shell script executed" >> $ERROR_LOG
