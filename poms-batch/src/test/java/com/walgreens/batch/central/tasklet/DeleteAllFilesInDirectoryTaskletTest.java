package com.walgreens.batch.central.tasklet;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.StepScopeTestExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.walgreens.batch.central.test.PromotionFeedToCenteralTest;

@TestPropertySource(locations ="classpath:PhotoOmniBatch.properties")
@ContextConfiguration(locations = {
		"classpath*:**/spring/batch/config/database.xml",
		"classpath*:**/spring/batch/config/context.xml",
		"classpath*:**/spring/batch/jobs/job-report.xml",
		"classpath*:**/spring/batch/jobs/test-job-report.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		StepScopeTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class DeleteAllFilesInDirectoryTaskletTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DeleteAllFilesInDirectoryTaskletTest.class);

	
	
	@Autowired
	@Qualifier("purgeTestUtils")
	private JobLauncherTestUtils jobLauncherTest;

	/**
	 * jdbcTemplate
	 */
	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Value( "${tanda.archive.folder.path}" )
	private  String archiveTandA;
	@Value( "${mss.archive.folder.path}" )
	private  String archiveMSS;
	@Value( "${kronos.archive.folder.path}" )
	private  String archiveKronos;
	@Value( "${kpi.archive.folder.path}" )
	private  String archiveKPI;
	
	
	
	private boolean setup(String archivePath,Long timeInMillis){
		
		if(archivePath==null) return false ;
		if(archivePath.length()>2)
			archivePath= archivePath.substring(0,archivePath.length()-1);
			
		File d = new File(archivePath);
		File f = new File(archivePath+"\\file.zip");
		
		try {
			boolean b = d.exists()?true:d.mkdirs();
			boolean c = f.exists()?true:f.createNewFile();
			f.setLastModified(timeInMillis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		final JobExecution jobExecution = jobLauncherTest.launchStep(
				"deleteStep", (ExecutionContext)null);
		// Check if job completed successfully
		return(f.exists());
	}
	
	
	
	@Test
	public void testExecuteForMSSOlderFiles() {
		assertFalse(setup(archiveMSS,1234L));
	}
	
	@Test
	public void testExecuteForTandAOlderFiles() {
		assertFalse(setup(archiveTandA,1234L));
	}

	@Test
	public void testExecuteForKronosOlderFiles() {
		assertFalse(setup(archiveKronos,1234L));
	}
	
	@Test
	public void testExecuteForKPIOlderFiles() {
		assertFalse(setup(archiveKPI,1234L));
	}
	@Test
	public void testExecuteForMSSNewerFiles() {
		assertTrue(setup(archiveMSS,System.currentTimeMillis()));
	}
	
	@Test
	public void testExecuteForTandANewerFiles() {
		assertTrue(setup(archiveTandA,System.currentTimeMillis()));
	}

	@Test
	public void testExecuteForKronosNewerFiles() {
		assertTrue(setup(archiveKronos,System.currentTimeMillis()));
	}
	
	@Test
	public void testExecuteForKPINewerFiles() {
		assertTrue(setup(archiveKPI,System.currentTimeMillis()));
	}
	
}
