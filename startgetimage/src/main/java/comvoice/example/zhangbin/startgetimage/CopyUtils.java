package comvoice.example.zhangbin.startgetimage;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by zhangbin on 2018/5/28.
 */

public class CopyUtils {
    private Context context;
    public CopyUtils(Context context){
        this.context=context;
    }

    /**
     * 将默认路径下的文件复制到指定的目录下
     */

    public int copy(String fromFile,String toFile){
        //要复制的文件目录
        File[]currentFiles;
        File root=new File(fromFile);
        //判断文件是否存在
        if(!root.exists()){
            return -1;
        }
        //如果存在则获取当前目录下的所有文件，填充数组
        currentFiles=root.listFiles();
        //目标目录
        File targetDir=new File(toFile);
        //创建目录
        if(!targetDir.exists()){
            targetDir.mkdirs();
        }
        //遍历要复制的全部文件
        for(int i=0;i<currentFiles.length;i++){
            if(currentFiles[i].isDirectory()){//如果当前项为子目录，进行递归
                copy(currentFiles[i].getPath()+"/",toFile+currentFiles[i].getName()+"/");
            }else {//如果当前项为文件则进行文件拷贝
                CopySdcardFile(currentFiles[i].getPath(),toFile+currentFiles[i].getName());
            }
        }
        return 0;
    }
    //文件拷贝
    public int CopySdcardFile(String fromFile,String toFile){
        try {
            InputStream fosfrom=new FileInputStream(fromFile);
            OutputStream fosto=new FileOutputStream(toFile);
            byte bt[]=new byte[1024];
            int d;
            while((d=fosfrom.read(bt))>0){
                fosto.write(bt,0,d);
            }
            fosfrom.close();
            fosto.close();
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }
}
