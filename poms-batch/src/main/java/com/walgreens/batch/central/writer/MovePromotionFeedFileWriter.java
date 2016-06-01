package com.walgreens.batch.central.writer;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import com.walgreens.batch.central.bean.MovePromotionFeedFileBean;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;

public class MovePromotionFeedFileWriter implements ItemWriter<MovePromotionFeedFileBean>{

	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MovePromotionFeedFileWriter.class);
	MovePromotionFeedFileBean objMovePromotionFeedFileBean;

	public void write(List<? extends MovePromotionFeedFileBean> items)
			throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into MovePromotionFeedFileWriter.write() ");
		}
		objMovePromotionFeedFileBean = items.get(0);
		try{

			File source = new File(objMovePromotionFeedFileBean.getInboundFeedLocation()),
					dest = new File(objMovePromotionFeedFileBean.getArchivalFeedLocation());
			objMovePromotionFeedFileBean.getStrLstFileNames().length();

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" objMovePromotionFeedFileBean.getStrLstFileNames() " + objMovePromotionFeedFileBean.getStrLstFileNames() );
			}
			if(null != objMovePromotionFeedFileBean.getStrLstFileNames() && !objMovePromotionFeedFileBean.getStrLstFileNames().isEmpty()){
				String [] strLstfiles  =  objMovePromotionFeedFileBean.getStrLstFileNames().split(",");
				String strSource = "" , strDest = "";
				boolean isOKfile, bmoved;
				for (String file: strLstfiles){
					isOKfile = false;
					strSource = (source+PhotoOmniConstants.FORWARD_SLASH+file.trim());
					strDest = (dest +PhotoOmniConstants.FORWARD_SLASH+file.trim());

					File f1 = new File(strSource);
					bmoved = f1.renameTo(new File(strDest));
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(" File moved or not ? :" + bmoved );
					}
					if(!bmoved && !FilenameUtils.isExtension(strSource,PhotoOmniConstants.PROCESSED_FILE_EXT) ){
						if(FilenameUtils.isExtension(strSource,PhotoOmniConstants.OK_FILE_EXT)) {
							isOKfile = true;
						}
						strSource = strSource.substring(0, strSource.lastIndexOf('.'));
						strSource = (isOKfile ? strSource+PhotoOmniConstants.UNDER_SCORE+PhotoOmniConstants.OK_FILE_EXT : strSource );
						f1.renameTo(new File(strSource+PhotoOmniConstants.DOT_DELIMITER+PhotoOmniConstants.PROCESSED_FILE_EXT));
					}

				}
			}
		}catch (Exception e) {
			LOGGER.error(" Error occoured at MovePromotionFeedFileWriter.write() ----> " + e);
			throw new PhotoOmniException("Feed files not moved to Archive folder.");
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting Form MovePromotionFeedFileWriter.write() ");
			}
		}

	}
}
