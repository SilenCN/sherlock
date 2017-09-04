package com.silen.applicationAnalyse;


import android.widget.Toast;

import java.io.DataOutputStream;

/**
 * Created by 新宇 on 2015/11/30.
 */
public class rootTool {
  /*  public static Process localProcess;
    public static DataOutputStream localDataOutputStream;
*/
    public static void StartShell() {
        try {
          /*  localProcess = Runtime.getRuntime().exec("sh");
            localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
*/        }catch (Exception e){

        }

    }

    public static void execRootCmdSilent(String paramString) {
        try {
            System.out.println(paramString);
            Process localProcess;
            localProcess = Runtime.getRuntime().exec("sh");
            localProcess.getOutputStream().write(paramString.getBytes());
            localProcess.getOutputStream().flush();
            localProcess.getOutputStream().close();
          /*  localProcess.getOutputStream().write("exit".getBytes());
            localProcess.getOutputStream().flush();
*/            localProcess=null;

/*            DataOutputStream localDataOutputStream;
            localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());

            localDataOutputStream.writeBytes(new StringBuilder().append(paramString).append("\n").toString());
            localDataOutputStream.flush();
            localDataOutputStream.writeBytes("exit\n");
            localDataOutputStream.flush();*/
           // localProcess.waitFor();
           /* localDataOutputStream.close();*/

        } catch (Exception localException) {
            localException.printStackTrace();

        }
    }
    public static void CloseShell(){
        try {
      /*      localDataOutputStream.close();
   */     }catch (Exception e){

        }
    }

}
