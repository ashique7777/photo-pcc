package com.walgreens.batch.central.writer;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.ApplicationContext;

import com.walgreens.batch.central.bean.UserPrefOrDefaultRepDataBean;
import com.walgreens.batch.central.utility.ApplicationContextProvider;

public class PSReportMasterCustomWriter implements ItemWriter<UserPrefOrDefaultRepDataBean> {

	private static ApplicationContextProvider ctx;
	

	@Override
	public void write(List<? extends UserPrefOrDefaultRepDataBean> items) throws Exception {
		ApplicationContext context = ctx.getApplicationContext();	
		JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
		Job job = (Job) context.getBean("printSignsChildReportJob");
		
		try {
			JobParameters jobParameters =  new JobParametersBuilder().addLong("SYS_USER_REPORT_PREF_ID", items.get(0).getReportPrefId()).toJobParameters();
					 
			JobExecution execution = jobLauncher.run(job, jobParameters);
			

			System.out.println("Exit Status : " + execution.getStatus());
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{

		}
	}


	/**
	 * @return the ctx
	 */
	public static ApplicationContextProvider getCtx() {
		return ctx;
	}


	/**
	 * @param ctx the ctx to set
	 */
	public static void setCtx(ApplicationContextProvider ctx) {
		PSReportMasterCustomWriter.ctx = ctx;
	}
	
	
}
