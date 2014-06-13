package de.spline.uves.ndef;

import java.io.OutputStreamWriter;
import java.io.InputStreamReader;

import java.io.BufferedWriter;
import java.io.BufferedReader;


abstract public class JcdweTestCase extends NdefTestCase {

        Process jcdwe;

        protected void setUp() throws Exception {
                String[] env = {
                        "JAVA_HOME=/usr/lib/jvm/java-7-openjdk",
                        "CLASSPATH=/home/yves/projects/javacard/ndef-sender/build/classes/main"
                };
                jcdwe = Runtime.getRuntime().exec("/home/yves/opt/java-card-sdk/bin/jcwde -p 9001 -nobanner src/main/ressources/jcwde.conf", env);
        }

        protected void tearDown() throws Exception {
                BufferedReader stdout = new BufferedReader(new InputStreamReader(jcdwe.getInputStream()));

                String line = stdout.readLine();
                while (null != line) {
                        System.out.println(line);
                        line = stdout.readLine();
                }

                BufferedReader stderr = new BufferedReader(new InputStreamReader(jcdwe.getErrorStream()));

                line = stderr.readLine();
                while (null != line) {
                        System.out.println(line);
                        line = stderr.readLine();
                }
        }


        public void assertTrace(String[] trace) throws Exception {                

                Process process = Runtime.getRuntime().exec("/home/yves/opt/java-card-sdk/bin/apdutool -p 9001 -nobanner -noatr");
                BufferedWriter stdin = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
                
                stdin.write("powerup;");
                stdin.newLine();
                
                for (int j = 0; j < trace.length; j += 2) {
                        byte[] apduCommand = stringToBytes(trace[j]);

                        for (int i = 0; i < apduCommand.length; i++) {
                                stdin.write("0x");
                                String hex = Integer.toHexString(apduCommand[i] & 0xFF);
                                if (hex.length() == 1) {
                                        stdin.write('0');
                                }
                                stdin.write(hex);
                                
                                if ((i+1) == apduCommand.length ) {
                                        stdin.write(';');
                                        stdin.newLine();
                                } else {
                                        stdin.write(' ');
                                }
                        }
         

                        stdin.write("powerdown;");
                        stdin.flush();
                        stdin.close();
                        process.waitFor();
                        assertEquals(process.exitValue(), 0);
                        
                        BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        String requestWithResponse = stdout.readLine();

                        byte[] apduResponse = stringToBytes(
                                requestWithResponse
                                  .replaceAll("^.*Le: \\d\\d,","")
                                  .replaceAll("SW\\d:", "")
                                  .replaceAll(",","").toUpperCase());

                        assertEquals(apduResponse, stringToBytes(trace[j+1]));

                }
        }
}

