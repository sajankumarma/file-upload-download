package com.sajan.fileuploaddownload.exception;

public class FileStorageException extends Exception
{
    public FileStorageException(String message)
    {
        super(message);
    }
    public FileStorageException(String message, Throwable t)
    {
        super(message,t);
    }
}
