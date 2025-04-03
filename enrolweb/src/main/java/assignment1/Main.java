package assignment1;

import java.io.File;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.tomcat.util.net.SSLHostConfig;
import org.apache.tomcat.util.net.SSLHostConfigCertificate;
import org.apache.tomcat.util.net.SSLHostConfigCertificate.Type;

public class Main {
    public static void main(String[] args) {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8866);

        // Configure HTTPS connector
        Connector httpsConnector = new Connector();
        httpsConnector.setPort(8443); // HTTPS Port
        httpsConnector.setSecure(true);
        httpsConnector.setScheme("https");

        Http11NioProtocol protocol = (Http11NioProtocol) httpsConnector.getProtocolHandler();
        protocol.setSSLEnabled(true);

        // Create SSLHostConfig
        SSLHostConfig sslHostConfig = new SSLHostConfig();
        sslHostConfig.setHostName("_default_"); // Required in newer Tomcat versions
        sslHostConfig.setProtocols("TLSv1.2,TLSv1.3"); // Specify allowed TLS versions
        sslHostConfig.setCertificateVerification(
            "optional"); // Can be "none", "optional", or "required"

        // Add certificate information
        SSLHostConfigCertificate certificate = new SSLHostConfigCertificate(sslHostConfig, Type.RSA);
        certificate.setCertificateKeystoreFile("keystore.jks"); // Path to your keystore
        certificate.setCertificateKeystorePassword("123587"); // Keystore password
        certificate.setCertificateKeyAlias("tomcat"); // Alias inside keystore

        // Add certificate to SSLHostConfig
        sslHostConfig.addCertificate(certificate);

        // Register SSLHostConfig with the connector
        protocol.addSslHostConfig(sslHostConfig);
        
        // Add HTTPS connector to Tomcat
        tomcat.getService().addConnector(httpsConnector);

        tomcat.setBaseDir("temp");

        File webAppLogin = new File("enrolweb/src/main/webapps/ROOT");      
        Context context = tomcat.addWebapp("", webAppLogin.getAbsolutePath());

        if (context == null) {
            throw new RuntimeException("Tomcat context initialization failed!");
        }

        tomcat.addServlet("", "LoginVerification", new LoginVerification());
        context.addServletMappingDecoded("/Login", "LoginVerification");

        tomcat.addServlet("", "ChoosingSem", new ChoosingSem());
        context.addServletMappingDecoded("/ChoosingSem", "ChoosingSem");

        tomcat.addServlet("", "Logout", new Logout());
        context.addServletMappingDecoded("/Logout", "Logout");

        tomcat.addServlet("", "Enrollment", new Enrollment());
        context.addServletMappingDecoded("/Enrollment", "Enrollment");
        context.setAllowCasualMultipartParsing(true);
        
        //Hash pass for all students when the website starts
        StudentService service = new StudentService();
        service.hashPassAll();
        
        try {
            tomcat.start();
            tomcat.getConnector();
            System.out.println("Tomcat started successfully.");
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }
}