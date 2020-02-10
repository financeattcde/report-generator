package org.tcde.accounts.report.tools.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.tcde.accounts.report.tools.exception.ReportGenConfigLoadException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

public class ReportGenConfigLoader {

    private static final Logger _logger = LoggerFactory.getLogger("ReportGenConfigLoader");
    private static final String REPORT_GEN_CONFIG_BASE_FILE = "report-gen-config.properties";
    public static final String REPORT_GEN_CONFIG_SYSTEM_PROP_NAME = "config";
    private static ReportGenConfig reportGenConfig;

    private ReportGenConfigLoader(){

    }

    public static ReportGenConfig getReportGenConfig(){
        if(reportGenConfig == null){
            loadReportGenConfig();
        }
        return reportGenConfig;
    }

    private static void loadReportGenConfig() {
        File configFile;
        String configFileParam = System.getProperty("config");
        if(StringUtils.isEmpty(configFileParam)){
            _logger.debug("config file parameter is not specified so looking in to classpath" );
             configFile = getClasspathConfigFile();
        } else {
            _logger.debug("config file parameter specified path => {} ",configFileParam);
             configFile = loadFileFromPath(configFileParam);
        }
        Map<String, String> configProperties = loadPropertiesFile(configFile);
        buildReportGenConfig(configProperties);
    }

    private static void buildReportGenConfig(Map<String, String> configProperties) {
        reportGenConfig = ReportGenConfig.builder()
                .tmplBasePath(configProperties.get(ReportGenConfigKeys.TEMPLATES_BASE_PATH))
                .outputBasePath(configProperties.get(ReportGenConfigKeys.OUTPUT_FOLDER_PATH))
                .tmplValProBasePath(configProperties.get(ReportGenConfigKeys.REPORT_VALUE_PROVIDER_BASE_PATH))
                .auditFolderPath(configProperties.get(ReportGenConfigKeys.AUDIT_FOLDER_PATH))
                .build();
    }


    private static Map<String, String> loadPropertiesFile(File configFile) {
        Map<String,String> configProps = new HashMap<>();
        try{
            Properties configProperties = new Properties();
            configProperties.load(new FileInputStream(configFile));
        }catch(IOException e){
            throw new ReportGenConfigLoadException("Unable to load config file with path => "+configFile.getAbsolutePath(), e);
        }
        return configProps;
    }

    private static File getClasspathConfigFile() {
        Resource configFileRes = new ClassPathResource("classpath:"+REPORT_GEN_CONFIG_BASE_FILE);
        File file;
        try{
             file =  configFileRes.getFile();
        }catch(IOException e){
            _logger.error("Exception in loading the classpath config file",e);
            throw new ReportGenConfigLoadException("Unable to read classpath config file", e);
        }
       return file;
    }

    private static File loadFileFromPath(String configFilePath){
        File file = new File(configFilePath);
        if(!file.exists()){
            throw new ReportGenConfigLoadException("There is no file exists with the path => "+configFilePath);
        }
        _logger.debug("Config exists with the path= {}", configFilePath);
        return file;
    }
}
