package org.tcde.accounts.report.tools;

import java.io.File;

public interface ReportGenerator {

    ReportGenResult generateReports(File templateFile, File tmplValProviderFile, String outputFolderPath);
}
