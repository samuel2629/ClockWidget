package com.silho.ideo.clockwidget.utils;

import android.os.Environment;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

/**
 * Created by Samuel on 09/04/2018.
 */

public class GZipFile
{
    private static final String INPUT_GZIP_FILE = "/storage/emulated/0/Download/city.list.json.gz";
    private static final String OUTPUT_FILE = "C:\\Users\\Samuel\\Downloads\\file1.txt";


    /**
     * GunZip it
     */
    public void gunzipIt(){

        byte[] buffer = new byte[1024];

        try{

            GZIPInputStream gzis =
                    new GZIPInputStream(new FileInputStream(INPUT_GZIP_FILE));

            FileOutputStream out =
                    new FileOutputStream(OUTPUT_FILE);

            int len;
            while ((len = gzis.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }

            gzis.close();
            out.close();

            System.out.println("Done");

        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
}