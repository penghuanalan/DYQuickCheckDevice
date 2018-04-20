package cn.chinafst.dyquickcheckdevice;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * 打印log工具类,
 * Created by Administrator on 2017/9/6.
 */
public class LogPrint {

	static boolean isOpen =true;
	//static  boolean isOpen=Utils.isOpen
	static String MyTag = "MyTag";
	static final String CLASS_METHOD_LINE_FORMAT = "%s.%s()  Line:%d";

	// 可跟踪的打印
	public static void show(Object msg) {
		if (isOpen == true) {
			StackTraceElement traceElement = Thread.currentThread()
					.getStackTrace()[3];
			String logText = String.format(CLASS_METHOD_LINE_FORMAT,
					traceElement.getClassName(), traceElement.getMethodName(),
					traceElement.getLineNumber());
			LogPrint.e("信息来自:" + logText);
			LogPrint.e(String.valueOf(msg));
			LogPrint.e(" ");
		}
	}

	// 黑色
	public static void v(String msg) {
		if (isOpen == true) {
			Log.v(MyTag, msg);
		}
	}

	// 蓝色，debug
	public static void d(String msg) {
		if (isOpen == true) {
			Log.d(MyTag, msg);
		}
	}

	// 绿色，information
	public static void i(String msg) {
		if (isOpen == true) {
			Log.i(MyTag, msg);
		}
	}

	// 橙色，warning
	public static void w(String msg) {
		if (isOpen == true) {
			Log.w(MyTag, msg);
		}
	}

	// 红色，error
	public static void e(String msg) {
		if (isOpen == true) {
			Log.e(MyTag, msg);
		}
	}
	public static void toast(Context context,String str){
		Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
	}
}
