package ru.sber.bootcamp;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class StartAppTest{

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Test
    public void startApp(){
        String[] args = {""};
        InputStream in = new ByteArrayInputStream("exit".getBytes());
        System.setIn(in);
        StartApp.main(args);
        assertEquals("System arg is: \n" +
                "Auth service is running\n" +
                "Server autoCommit mode: true\n" +
                "Server TCP/IP mode: false\n" +
                "true\n" +
                "Connect to bd main is successful\n" +
                "Server start on : /0:0:0:0:0:0:0:0:8000\n" +
                "BankAPI start successful!!\n" +
                "Auth service has been stopped\n" +
                "DB Close\n" +
                "", outContent.toString());

    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

}