package vn.edu.iuh.fit.config;

import jakarta.servlet.annotation.WebFilter;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

//@WebFilter("/*")
@ApplicationPath("/api")
public class HelloApplication extends Application {

}