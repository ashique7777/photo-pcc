/**
 * 
 */

package com.walgreens.batch.central.bean;

/**
* @author CTS
*
*/
public class SFTPDataBean {

	/*
	 * SFTP host
	 */
	private String sftpHost;

	/*
	 * port of SFTP
	 */
	private int sftpPort;

	/*
	 * SFTP username
	 */
	private String sftpUser;

	/*
	 * SFTP password
	 */
	private String sftpPassword;

	/*
	 * SFTP remote destination directory
	 */
	private String sftpDir;
	/*
	 * Getter for SFTP Host
	 */
	public String getSftpHost() {
		return sftpHost;
	}
	/*
	 * Setter for SFTP Host
	 */
	public void setSftpHost(String sftpHost) {
		this.sftpHost = sftpHost;
	}
	/*
	 * Getter for SFTP Port
	 */
	public int getSftpPort() {
		return sftpPort;
	}
	/*
	 * Setter For SFTP Port
	 */
	public void setSftpPort(int sftpPort) {
		this.sftpPort = sftpPort;
	}
	/*
	 * Getter For User for SFTP
	 */
	public String getSftpUser() {
		return sftpUser;
	}
	/*
	 * Setter for User for SFTP
	 */
	public void setSftpUser(String sftpUser) {
		this.sftpUser = sftpUser;
	}
	/*
	 * Getter for SFTP Password
	 */
	public String getSftpPassword() {
		return sftpPassword;
	}
	/*
	 * Setter for SFTP Password
	 */
	public void setSftpPassword(String sftpPassword) {
		this.sftpPassword = sftpPassword;
	}
	/*
	 * Getter for SFTP Remote Directory
	 */
	public String getSftpDir() {
		return sftpDir;
	}
	/*
	 * Setter for SFTP Remote Directory
	 */
	public void setSftpDir(String sftpDir) {
		this.sftpDir = sftpDir;
	}
}
