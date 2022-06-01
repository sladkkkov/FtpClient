package com.example.demo;

import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.io.IOException;

@SpringBootApplication
@PWA(name = "My App", shortName = "My App")
@NpmPackage(value = "line-awesome", version = "1.3.0")
public class DemoApplication extends SpringBootServletInitializer implements AppShellConfigurator {

	public static void main(String[] args) throws IOException {
		FtpServiceNumberTwo ftpService = new FtpServiceNumberTwo("91.222.128.11", 21, "testftp_guest", "12345");
		ftpService.open();
		try {
			ftpService.getRootItems();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*SpringApplication.run(DemoApplication.class, args);*/
	}

}
