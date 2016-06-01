package com.walgreens.batch.central.tasklet;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;

import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.PhotoOmniFileUtil;

public class TandAFolderCheckTasklet implements Tasklet {

	 @Value("${tanda.archive.folder.path}")
	private String archiveFolderpath;

	 @Value("${tanda.work.folder.path}")
	private String workFolderPath;

	 @Value("${tanda.destination.folder.path}")
	private String exactPath;

	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {

		String[] filePath = { archiveFolderpath, workFolderPath, exactPath };
		ArrayList<String> arrayList = new ArrayList<String>(
				Arrays.asList(filePath));
		checkDirectoryExists(arrayList);
		
		return RepeatStatus.FINISHED;
	}

	public void checkDirectoryExists(ArrayList<String> filePathList)
			throws PhotoOmniException {
		for (String str : filePathList) {
			File files = new File(str);
			if (!PhotoOmniFileUtil.isDirectoryOrFileExists(true, str, false)) {
				if (!files.mkdir()) {
					throw new PhotoOmniException(
							"Error occured while creating the Directory and the directory path is -- "
									+ str);
				}
			}
		}
	}
}
