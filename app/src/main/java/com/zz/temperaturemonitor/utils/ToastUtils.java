package com.zz.temperaturemonitor.utils;

import android.content.Context;
import android.widget.Toast;


public class ToastUtils {
      
    /** 之前显示的内容 */  
    private static String oldMsg="" ;
    /** Toast对象 */  
    private static Toast toast = null ;
    /** 第一次时间 */  
    private static long oneTime = 0 ;  
    /** 第二次时间 */  
    private static long twoTime = 0 ;  
      
    /** 
     * 显示Toast 
     * @param context 
     * @param message 
     */  
    public static void showToast(Context context, String message){
        if(toast == null){  
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);  
            toast.show() ;  
            oneTime = System.currentTimeMillis() ;  
        }else{  
            twoTime = System.currentTimeMillis() ;  
            if(oldMsg.equals(message)){
                if(twoTime - oneTime > Toast.LENGTH_SHORT){  
                    toast.show() ;  
                }  
            }else{  
                oldMsg = message ;  
                toast.setText(message) ;  
                toast.show() ;  
            }  
        }  
        oneTime = twoTime ;  
    }
    public static void showToast(Context context, int res){
        if(toast == null){
            toast = Toast.makeText(context, res, Toast.LENGTH_SHORT);
            toast.show() ;
            oneTime = System.currentTimeMillis() ;
        }else{
            twoTime = System.currentTimeMillis() ;
            if(oldMsg.equals(context.getResources().getString(res))){
                if(twoTime - oneTime > Toast.LENGTH_SHORT){
                    toast.show() ;
                }
            }else{
                oldMsg = context.getResources().getString(res);
                toast.setText(res) ;
                toast.show() ;
            }
        }
        oneTime = twoTime ;
    }
}  