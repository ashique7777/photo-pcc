<#assign home_url><@spring.url relativeUrl="${servletPath}/"/></#assign>
<#assign company_url><@spring.messageText code="company.url" text=companyUrl!"http://www.spring.io"/></#assign>
<#assign company_name><@spring.messageText code="company.name" text=companyName!"Spring"/></#assign>
<#assign product_url><@spring.messageText code="product.url" text=productUrl!"http://projects.spring.io/spring-batch/"/></#assign>
<#assign product_name><@spring.messageText code="product.name" text=productName!"Spring Batch"/></#assign>
<div id="primary-navigation">
    <div id="primary-left">
        <ul>
           <#list menuManager.menus as menu>
             <#if menu_index != 0 && menu_index != 3>
                <#assign menu_url><@spring.url relativeUrl="${menu.url}"/></#assign>
                <li><a href="${menu_url}">${menu.label}</a></li>    
             </#if>
           </#list>
        </ul>
    </div>

</div><!-- /primary-navigation -->