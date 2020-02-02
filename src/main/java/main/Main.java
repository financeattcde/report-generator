package main;

import org.docx4j.model.datastorage.migration.VariablePrepare;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("In the main method");
        InputStream templateInputStream = new FileInputStream(new File("/home/rams/TCDE/report-generator/src/test/resources/template/ContrStmtTemplate.docx"));

        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(templateInputStream);
        MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();
        VariablePrepare.prepare(wordMLPackage);

        HashMap<String, String> variables = new HashMap<>();
        variables.put("ReportDate", "5 Feb 2020");
        variables.put("DonorName", "Ramesh Rangaram");
        variables.put("DonationAmount", "1000 (One Thousand USD only)");

        documentPart.variableReplace(variables);

        System.out.println("In the main method");
        wordMLPackage.save(new File("/home/rams/TCDE/report-generator/src/test/resources/template/Ramesh.docx"));

    }
}
