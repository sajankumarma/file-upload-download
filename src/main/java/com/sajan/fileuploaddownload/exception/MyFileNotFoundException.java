package com.sajan.fileuploaddownload.exception;

public class MyFileNotFoundException extends Exception
{
    public MyFileNotFoundException(String message)
    {
        super(message);
    }
    MyFileNotFoundException(String message, Throwable t)
    {
        super(message,t);
    }

}
