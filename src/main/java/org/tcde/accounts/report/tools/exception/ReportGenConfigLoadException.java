package org.tcde.accounts.report.tools.exception;

public class ReportGenConfigLoadException extends RuntimeException{

    public ReportGenConfigLoadException(String message, Exception cause){
        super(message,cause);
    }

    public ReportGenConfigLoadException(String message, Throwable cause){
        super(message,cause);
    }

    public ReportGenConfigLoadException(String message){
        super(message);
    }
}
