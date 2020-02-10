package org.tcde.accounts.report.tools.config;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportGenConfig {

    private String tmplBasePath;
    private String tmplValProBasePath;
    private String outputBasePath;
    private String auditFolderPath;


    public void validateAllPaths(){

    }
}
