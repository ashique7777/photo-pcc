<%@ page import="org.springframework.security.saml.metadata.MetadataManager"%>
<%@ page import="org.springframework.web.context.WebApplicationContext"%>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="java.util.Set"%>
<%
	WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletConfig().getServletContext());
	MetadataManager metadataManager = context.getBean("metadata",MetadataManager.class);
	Set<String> idps = metadataManager.getIDPEntityNames();
	String idp = "";
	for(String str:idps){
		idp = str;
	}
	response.sendRedirect(request.getContextPath() + "/saml/login?idp=" + idp);
%>
