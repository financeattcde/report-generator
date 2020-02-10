package org.tcde.accounts.report.tools;

import org.docx4j.model.fields.merge.DataFieldName;
import org.docx4j.model.fields.merge.MailMerger;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.tcde.accounts.report.tools.config.ReportGenConfig;
import org.tcde.accounts.report.tools.config.ReportGenConfigLoader;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class ReportGenApp implements CommandLineRunner {
    
    private static final Logger _logger = LoggerFactory.getLogger("ReportGenApp");

    private static final String TEMPLATE_FILE_BASE_PATH = "/home/rams/TCDE/report-generator/src/test/resources/template/";
    private static final String VALUE_PROVIDER_FILE_BASE_PATH= TEMPLATE_FILE_BASE_PATH;
    private static final String OUTPUT_FILE_BASE_PATH ="/home/rams/TCDE/report-generator/src/test/resources/output/";
    private static final String DONATION_YR = "2019";
    private static final String REPORT_GENERATION_DATE = "5 Feb 2020";

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ReportGenApp.class);
        app.run(args);
    }

    private static List<InputRecord> readInputRecords(File valueProviderFile) {
        List<InputRecord> records = new ArrayList<>();
      _logger.info("Reading the input file");
        try{
            if(valueProviderFile.exists()){
                BufferedReader reader = new BufferedReader(new FileReader(valueProviderFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] inputValues = line.split(",");
                    InputRecord inputRecord = new InputRecord(inputValues[0],inputValues[1]);
                    records.add(inputRecord);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
      _logger.info("TotalRecords Found=> "+records.size());
        return records;
    }


    private static void generateReport(File templateFile, Map<DataFieldName, String> mergeData, File outputFile){
        try{
          _logger.info("In generation of the file => "+outputFile.getName());
            InputStream templateInputStream = new FileInputStream(templateFile);
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(templateInputStream);
            org.docx4j.model.fields.merge.MailMerger.setMERGEFIELDInOutput(MailMerger.OutputField.KEEP_MERGEFIELD);
            org.docx4j.model.fields.merge.MailMerger.performMerge(wordMLPackage, mergeData, false);
            wordMLPackage.save(outputFile);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void run(String... args) {
        ReportGenConfig config = ReportGenConfigLoader.getReportGenConfig();
      _logger.info("In the main method with args {} and length => {}", new Object[]{args}, args.length);
        File templateFile = new File(TEMPLATE_FILE_BASE_PATH+"ContrStmtTemplate.docx");
        File valueProviderFile = new File(VALUE_PROVIDER_FILE_BASE_PATH+"DonationSummaryByMember.csv");

        List<InputRecord> inputRecords =  readInputRecords(valueProviderFile);

        for(InputRecord record: inputRecords){
          _logger.info("Creating file for => "+record.getDonorName());
            String donorName = record.getDonorName();
            Map<DataFieldName, String> item = new HashMap<>();
            item.put(new DataFieldName("DocCreationDate"), REPORT_GENERATION_DATE);
            item.put(new DataFieldName("DonorName"), record.getDonorName());
            item.put(new DataFieldName("DonationYear"), DONATION_YR);
            item.put(new DataFieldName("DonationAmtNumber"), record.getDonationAmt());
            String fileName = donorName.replaceAll(" ","");
            File outputFile = new File(OUTPUT_FILE_BASE_PATH+fileName+".docx");
            generateReport(templateFile,item,outputFile);
          _logger.info("Done Creating file for => "+record.getDonorName());
        }
    }
}
