package com.usc.app.entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@EnableAsync
@RestController
@RequestMapping(value = "/dcm")
public class Documentation
{
	@PostMapping(value = "/access")
	public Object access(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request,
			HttpServletResponse response)
	{
		DefaultEventProcessor processor = new DefaultEventProcessor(file, request, response);
		return processor.getResultMap();
	}
}
