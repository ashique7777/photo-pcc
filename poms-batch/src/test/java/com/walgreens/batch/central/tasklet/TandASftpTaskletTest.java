package com.walgreens.batch.central.tasklet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
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


@TestPropertySource(locations ="classpath:PhotoOmniBatch.properties")
@ContextConfiguration(locations = {
		"classpath*:**/spring/batch/config/database.xml",
		"classpath*:**/spring/batch/config/context.xml",
		"classpath*:**/spring/batch/jobs/job-report.xml",
		"classpath*:**/spring/batch/jobs/test-job-report.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		StepScopeTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class TandASftpTaskletTest {

	
	@Autowired
	@Qualifier("tAndaTestUtils")
	private JobLauncherTestUtils jobLauncherTest;

	@Autowired
	@Qualifier("tAndasftpTasklet")
	private TandASftpTasklet sftpTasklet ;
	/**
	 * jdbcTemplate
	 */
	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Value( "${tanda.archive.folder.path}" )
	private  String archiveTandA;
	
	@SuppressWarnings("deprecation")
	@Test
	public final void testExecute() {
		
		final JobExecution jobExecution = jobLauncherTest.launchStep(
				"tAndasftpStep", (ExecutionContext)null);
		assertEquals("SFTP step passed in T and A batch", BatchStatus.COMPLETED.name(),
				jobExecution.getExitStatus().getExitCode());
		
	}
	


}
