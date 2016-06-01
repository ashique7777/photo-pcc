package com.walgreens.oms.json.bean;

public class EnvHistoryBean {

	    private String dateTime;
	    private String action;
	    private String user;
	    private String reasonForTransfer;
	    private String comments;
		
		/**
		 * @return the dateTime
		 */
		public String getDateTime() {
			return dateTime;
		}
		/**
		 * @param dateTime the dateTime to set
		 */
		public void setDateTime(String dateTime) {
			this.dateTime = dateTime;
		}
		/**
		 * @return the action
		 */
		public String getAction() {
			return action;
		}
		/**
		 * @param action the action to set
		 */
		public void setAction(String action) {
			this.action = action;
		}
		/**
		 * @return the user
		 */
		public String getUser() {
			return user;
		}
		/**
		 * @param user the user to set
		 */
		public void setUser(String user) {
			this.user = user;
		}
		/**
		 * @return the reasonForTransfer
		 */
		public String getReasonForTransfer() {
			return reasonForTransfer;
		}
		/**
		 * @param reasonForTransfer the reasonForTransfer to set
		 */
		public void setReasonForTransfer(String reasonForTransfer) {
			this.reasonForTransfer = reasonForTransfer;
		}
		/**
		 * @return the comments
		 */
		public String getComments() {
			return comments;
		}
		/**
		 * @param comments the comments to set
		 */
		public void setComments(String comments) {
			this.comments = comments;
		}
	    
	    
	    
}
