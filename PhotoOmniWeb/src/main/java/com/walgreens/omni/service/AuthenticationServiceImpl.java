package com.walgreens.omni.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.providers.ExpiringUsernameAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.walgreens.common.security.oam.SAMLUserDetails;
import com.walgreens.common.security.oam.bo.UserDetailBO;
import com.walgreens.omni.factory.OmniBOFactory;

@Controller
@RequestMapping("/auth")
public class AuthenticationServiceImpl implements AuthenticationService {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
	@Autowired
	private OmniBOFactory reportBOFactory;
	
	@Value( "${version.central}" )
	private String version;
	
	@Autowired
	@Qualifier("userDetailBO")
	private UserDetailBO userDetailBO;

	
	@RequestMapping(value = "/corphome", method = RequestMethod.GET)
	public String showCorpHome(ModelMap model) {
		logger.trace("Inside showCorpHome");
		logger.debug("Inside showCorpHome");
		logger.info("Inside showCorpHome");
		model.addAttribute("appVersion", version);
		ExpiringUsernameAuthenticationToken authToken = (ExpiringUsernameAuthenticationToken) SecurityContextHolder
				.getContext().getAuthentication();
		SAMLUserDetails userDetails = (SAMLUserDetails)authToken.getDetails(); 
		userDetailBO.loadUserDetails(userDetails);
		model.addAttribute("loggedFullName", userDetails.getFirstname() + " "+ userDetails.getLastname());
		logger.info("authToken", authToken);
		if (logger.isDebugEnabled()) {
			logger.debug("authToken" + authToken.toString());
			logger.debug("loggedFullName" + userDetails.getFullname());
			logger.debug("getFirstName" + userDetails.getFirstname());
			logger.debug("getFirstName" + userDetails.getLastname());
		}		
			
		return "pages/corphome";
	}

	@RequestMapping(value = "/storehome", method = RequestMethod.GET)
	public String showStoreHome(ModelMap model) {
		logger.trace("Inside showStoreHome");
		logger.debug("Inside showStoreHome");
		logger.info("Inside showStoreHome");
		return "pages/storehome";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(ModelMap model) {
		logger.trace("Inside logout");
		logger.debug("Inside logout");
		logger.info("Inside logout");
		return "list";
	}

}