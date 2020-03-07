package com.jorge.rest.webservices.restfulwebservicesnew.helloworld;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloWorldController 
{
	@Autowired
	MessageSource messageResource;
	
//	@GetMapping("/hello-world-internationalized")
//	public String helloWorldInternazionalized(@RequestHeader(name="Accept-Language", required=false) Locale locale)
//	{
//		return messageResource.getMessage("good.morning.message", null, locale);
//	}
	
	@GetMapping("/hello-world-internationalized")
	public String helloWorldInternationalized()
	{
		return messageResource.getMessage("good.morning.message", null, LocaleContextHolder.getLocale());
	}
}
