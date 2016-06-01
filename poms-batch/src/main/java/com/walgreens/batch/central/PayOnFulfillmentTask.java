/**
 * 
 */
package com.walgreens.batch.central;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.batch.central.bean.EnvPropertiesBean;
import com.walgreens.batch.central.rowmapper.EnvPropertiesRowMapper;
import com.walgreens.batch.query.EnvPropertiesQuery;


/**
 * @author CTS
 *
 */
public class PayOnFulfillmentTask {
	
	final static Logger logger = LoggerFactory.getLogger(PayOnFulfillmentTask.class); 
	
	/**
     * Load Properties File
     */
    private static Properties prop;
    
    static{
        InputStream is = null;
        try {
            prop = new Properties();
            is = ClassLoader.class.getResourceAsStream("/PhotoOmniBatch.properties");
            prop.load(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * jdbcTemplate
     */
	private static JdbcTemplate jdbcTemplate;

	//public  void run() {
	public static void main(String[] args) {
		
		if(logger.isDebugEnabled()){
			logger.debug("Inside Pay On Fulfillment  task");
		}
		if(logger.isInfoEnabled()){
			logger.info("Inside Pay On Fulfillment  task");
		}
		
		try {
			String[] springConfig  = 
				{	"spring/batch/config/database.xml", 
					"spring/batch/config/context.xml",
					"spring/batch/jobs/job_PayOnFulfillment.xml"
				};
			@SuppressWarnings("resource")
			ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);
			String envName = prop.getProperty("env.name");
			String sysdateVal="";
			jdbcTemplate = (JdbcTemplate)context.getBean("omniJdbcTemplate");
			String qry = EnvPropertiesQuery.getDBEnvProperties().toString();
			List<EnvPropertiesBean> envPropList = (List<EnvPropertiesBean>)jdbcTemplate.query(qry,new Object[]{envName} , new EnvPropertiesRowMapper());
			
			for(int i=0; i< envPropList.size() ;i++){
				EnvPropertiesBean envPropertiesBean = envPropList.get(i);
				if(envPropertiesBean.getPropType().equalsIgnoreCase("payonfulfillment") && envPropertiesBean.getPropName().equalsIgnoreCase("NoOfDays")){
					sysdateVal = envPropertiesBean.getPropValue();
				}
				
			}
			
			Map<String, JobParameter> jobParamMap = new HashMap<String, JobParameter>();
			JobParameter timeJobParameter = new JobParameter(new Long(System.currentTimeMillis()).toString());
			
			//JobParameters jobParameters = new JobParametersBuilder().addString("jobSubmitTime",new Long(System.currentTimeMillis()).toString()).toJobParameters();
			jobParamMap.put("jobSubmitTime", timeJobParameter);
			//sysdateVal="210";
			jobParamMap.put("pofSysDateVal", new JobParameter(sysdateVal));
			//jobParamMap.put("XXX", new JobParameter("Sysdate - 210"));
			JobParameters jobParameters = new JobParameters(jobParamMap);
			JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
			Job job = (Job) context.getBean("PayOnFulfillmentJob");
			JobExecution execution = jobLauncher.run(job, jobParameters);
			logger.debug("Exit Status : " + execution.getStatus());
			System.exit(0);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Exception occured at Pay On fulfillement task .", e);
				System.exit(1);
			}

		}

}
