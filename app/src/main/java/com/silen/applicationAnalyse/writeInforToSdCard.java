package com.silen.applicationAnalyse;


import java.io.File;

public class writeInforToSdCard {
  public writeInforToSdCard(){
      System.out.println("dslmfksdjcmsklmiose");
     // rootTool.StartShell();
  /*    if (new File("/storage/sdcard0/SilenAppAnalyse/app.txt").exists()){
          new File("/storage/sdcard0/SilenAppAnalyse/app.txt").delete();
      }else if (!new File("/storage/sdcard0/SilenAppAnalyse").exists()){
          new File("/storage/sdcard0/SilenAppAnalyse").mkdir();
      }*/

  }
    public static void write(String content){
        rootTool.execRootCmdSilent("echo \""+content+"\" >> /sdcard/app.txt");
        System.out.println(content);
    }
    public void finishAnalyse(){
        rootTool.CloseShell();

    }
}
