package com.walgreens.omni.service;

import org.springframework.ui.ModelMap;

public interface AuthenticationService {

	public String showCorpHome(ModelMap model);

	public String showStoreHome(ModelMap model);

	public String logout(ModelMap model);

}