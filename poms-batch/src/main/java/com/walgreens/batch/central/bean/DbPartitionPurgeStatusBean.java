package com.walgreens.batch.central.bean;

/**
 * This class is a bean that holds all the various attributes used when sending out the confirmation
 * email for the DbPartitionPurge job.
 * 
 */
public class DbPartitionPurgeStatusBean {

    public DbPartitionPurgeStatusBean() {
    }

    private String tName;
    private int numRows;
    private String status;
    private String statusDtls;
    private String maxPartitionAfter;
    private String minPartitionAfter;
    private String maxPartition;
    private String minPartition;
    private boolean partitionOneMonthFuture;
    private String partitonDate; 
    private String partitionName;

    public String getTName() {
        return tName;
    }
    public int getNumRows() {
        return numRows;
    }
    public String getStatus() {
        return status;
    }
    public String getStatusDtls() {
        return statusDtls;
    }
    public String getMaxPartitionAfter() {
        return maxPartitionAfter;
    }
    public String getMinPartitionAfter() {
        return minPartitionAfter;
    }
    public String getMaxPartition() {
        return maxPartition;
    }
    public String getMinPartition() {
        return minPartition;
    }
    public boolean isPartitionOneMonthFuture() {
        return partitionOneMonthFuture;
    }
    public String getPartitonDate() {
        return partitonDate;
    }
    public void setTName(String name) {
        tName = name;
    }
    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setStatusDtls(String statusDtls) {
        this.statusDtls = statusDtls;
    }
    public void setMaxPartitionAfter(String maxPartitionAfter) {
        this.maxPartitionAfter = maxPartitionAfter;
    }
    public void setMinPartitionAfter(String minPartitionAfter) {
        this.minPartitionAfter = minPartitionAfter;
    }
    public void setMaxPartition(String maxPartition) {
        this.maxPartition = maxPartition;
    }
    public void setMinPartition(String minPartition) {
        this.minPartition = minPartition;
    }
    public void setPartitionOneMonthFuture(boolean partitionOneMonthFuture) {
        this.partitionOneMonthFuture = partitionOneMonthFuture;
    }
    public void setPartitonDate(String partitonDate) {
        this.partitonDate = partitonDate;
    }
    public String getPartitionName() {
        return partitionName;
    }
    public void setPartitionName(String partitionName) {
        this.partitionName = partitionName;
    }
}
