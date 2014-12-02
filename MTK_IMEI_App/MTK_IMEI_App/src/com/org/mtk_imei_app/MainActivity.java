package com.org.mtk_imei_app;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.*;

public class MainActivity extends Activity {

	private EditText imeisEdittext = null;
	private EditText imei1Edittext = null;
	private EditText imei2Edittext = null;
	private EditText modelEdittext = null;

	private Button startWriteBtn = null;
	private Button restartBtn = null;

	int mNegativeCount = 100;
	final int TYPE_COUNTERDOWN_COUNTER = 11;
	AlertDialog.Builder counterDownDialog;
	TextView textView = null;
	private boolean stopHandler = false;
	private int delayTime = 10;

	private String TAG = "APP";
	private String targetImeiDir = "/data/nvram/md/NVRAM/NVD_IMEI/";
	private String targetImeiName = targetImeiDir + "MP0B_001";

	private String imeiDataDir = "imei";
	private String tempFilename = "";
	private String dataNvram = "/data/nvram";
	private String modelDataFile = "model.txt";
	private String typeDataFileDir = "/system/build.prop";
	private String tempTypeDataFileDir = "/system/tempbuild.prop";
	
	private String name = "";
	private String model = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		imei1Edittext = (EditText)findViewById(R.id.imei1Edittext);
		imei2Edittext = (EditText)findViewById(R.id.imei2Edittext);
		imeisEdittext = (EditText)findViewById(R.id.imeisEdittext);
		modelEdittext = (EditText)findViewById(R.id.modelEdittext);

		startWriteBtn = (Button)findViewById(R.id.startWrite);
		restartBtn = (Button)findViewById(R.id.restart);


		startWriteBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {	
				startWriteBtn.setEnabled(false);
				writeImei();
				try {
					writeModel();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				restoreNVRamImei(dataNvram); 
				restoreNVRamImei(typeDataFileDir);
				restartBtn.setEnabled(true);
			}
		});

		restartBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {	
				restart();
			}

		});

	}


	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		if(!isRoot()){	
			AlertDialog.Builder mDialog = new AlertDialog.Builder(MainActivity.this);
			mDialog.setTitle("警告!");
			mDialog.setMessage("您的手机没有root，程序将自动退出！请root重新操作!");
			mDialog.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			}).show();
		}

		if(!isImeiDataExist()){
			AlertDialog.Builder mDialog = new AlertDialog.Builder(MainActivity.this);
			mDialog.setTitle("错误!");
			mDialog.setMessage("SD卡imei目录为空或者不存在！程序将退出！");
			mDialog.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			}).show();
		}

		chmodNVRamImei(dataNvram);
				
		if  (!isExist(targetImeiDir)){     
			AlertDialog.Builder mDialog = new AlertDialog.Builder(MainActivity.this);
			mDialog.setTitle("错误!");
			mDialog.setMessage("抱歉，不支持您的手机！您的手机没有/data/nvram/md/NVRAM/NVD_IMEI/目录或者已损坏！");
			mDialog.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			}).show();
		}
		getSIMInfo();

		chmodNVRamImei(typeDataFileDir);
		try {
			if(!isModelDataExist()){
				AlertDialog.Builder mDialog = new AlertDialog.Builder(MainActivity.this);
				mDialog.setTitle("错误!");
				mDialog.setMessage("机型数据文件model.txt不错在！");
				mDialog.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				}).show();
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		autoInit();

		startWriteBtn.setEnabled(true);
	}	


	private void chmodNVRamImei(String targetImeiDir)
	{

		if(isRoot()){
			java.lang.Process process;
			try {
				process = Runtime.getRuntime().exec("su");
				DataOutputStream os = new DataOutputStream(process.getOutputStream());
				String command = "chmod -R 777 "+targetImeiDir+"\n";
				os.writeBytes(command);
				os.writeBytes("exit\n");
				os.flush();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			Log.i(TAG,"The target directory has not been root."); 
		}
	}

	private void restoreNVRamImei(String targetImeiDir)
	{

		if(isRoot()){
			java.lang.Process process;
			try {
				process = Runtime.getRuntime().exec("su");
				DataOutputStream os = new DataOutputStream(process.getOutputStream());
				String command = "chmod -R 771 "+targetImeiDir+"\n";
				os.writeBytes(command);
				os.writeBytes("exit\n");
				os.flush();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			Log.i(TAG,"The target directory has not been root."); 
		}
	}

	public boolean isRoot(){
		boolean bool = false;

		try{
			if ((!new File("/system/bin/su").exists()) && (!new File("/system/xbin/su").exists())){
				bool = false;
			} else {
				bool = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return bool;
	}


	public boolean isExist(String filePath) {
		//		String paths[] = filePath.split("/");
		//		String dir = paths[0];
		//		for (int i = 0; i < paths.length - 1; i++) {
		//			dir = dir + "/" + paths[i + 1];
		//			Log.i(TAG,"dir:  "+dir); 
		//			File dirFile = new File(dir);
		//			if (!dirFile.exists()) {
		//				return false;
		//			}
		//		}

		return true;
	}

	public boolean isImeiDataExist(){
		File sdDir = null; 
		boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在 
		if(sdCardExist)   
		{                               
			sdDir = Environment.getExternalStorageDirectory(); 
		}else{
			return false;
		}

		tempFilename = sdDir+"/"+ imeiDataDir;
		File dirFile = new File(tempFilename);
		if (!dirFile.exists()) {
			return false;
		}else if(dirFile.list().length == 0){
			return false;
		}

		String filename = dirFile.list()[0];
		File temp = new File(filename);
		imeisEdittext.setText(temp.getName());

		return true;
	}
	
	public boolean isModelDataExist() throws IOException{
		File sdDir = null; 
		boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在 
		if(sdCardExist)   
		{                               
			sdDir = Environment.getExternalStorageDirectory(); 
		}else{
			return false;
		}

		tempFilename = sdDir+"/"+ modelDataFile;
		File dirFile = new File(tempFilename);
		if (!dirFile.exists()) {
			return false;
		}else if(dirFile.length()==0){
			return false;
		}

		BufferedReader readerSD = null;
		List<String> listSD = new ArrayList<String>();
		readerSD = new BufferedReader(new FileReader(dirFile));
		String textSD = readerSD.readLine();
		while (textSD != null) {
			listSD.add(textSD);
			textSD = readerSD.readLine();
		}
		readerSD.close();
		
		Calendar calendar = Calendar.getInstance();
		int num = (calendar.get(Calendar.MINUTE)*60+calendar.get(Calendar.SECOND))%listSD.size();
		Log.i(TAG,"num: "+num+"listSD.size()"+listSD.size());
		String modelAndNameString = listSD.get(num);
		modelEdittext.setText(modelAndNameString);
		
		model = modelAndNameString.split(" ")[0];
		name = modelAndNameString.split(" ")[1];
		
		return true;
	}
	
	private void writeModel() throws IOException{

		File file = new File(typeDataFileDir);
		if(!file.exists()){
			AlertDialog.Builder mDialog = new AlertDialog.Builder(MainActivity.this);
			mDialog.setTitle("错误!");
			mDialog.setMessage("抱歉，您的手机不支持修改机型！");
			mDialog.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			}).show();
		}
		
		BufferedReader reader = null;
		List<String> list = new ArrayList<String>();
		reader = new BufferedReader(new FileReader(file));
		String text = reader.readLine();
		while (text != null) {
			if(text.contains("ro.product.model")){
				text = "ro.product.model="+model+" "+name;
			}else if(text.contains("ro.product.brand")){
				text = "ro.product.brand="+model;
			}else if(text.contains("ro.product.name")){
				text = "ro.product.name="+name;
			}else if(text.contains("ro.product.manufacturer")){
				text = "ro.product.manufacturer="+model;
			}else if(text.contains("ro.product.device")){
				text = "ro.product.device="+name;
			}else if(text.contains("ro.product.board")){
				text = "ro.product.board="+name;
			}else if(text.contains("ro.product.customize")){
				text = "ro.product.customize="+name;
			}
			list.add(text+"\n\r");
			text = reader.readLine();
		}
		reader.close();

		FileOutputStream outputStream = new FileOutputStream(file);
		for (String s : list) {
			outputStream.write(s.getBytes());
		}
		outputStream.close();
		
			
	}


	private void writeImei(){
		File dirFile = new File(Environment.getExternalStorageDirectory()+"/"+imeiDataDir);
		String filename = dirFile.list()[0];

		File temp = new File(Environment.getExternalStorageDirectory()+"/"+imeiDataDir+"/"+filename);
		File targetFile = new File(targetImeiName);
		if (targetFile.exists()) {
			targetFile.delete();
		}

		InputStream in = null;;
		try {
			in = new FileInputStream(temp);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		OutputStream out = null;
		try {
			out = new FileOutputStream(targetFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		byte[] buf = new byte[1024];
		int len;
		try {
			while ((len = in.read(buf)) !=-1) {
				try {
					out.write(buf, 0, len);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			in.close();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (temp.exists()) {
			temp.delete();
		}
	}

	public void autoInit(){		
		TimerDialog dialog = new TimerDialog(MainActivity.this);
		dialog.setTitle("注意！");
		dialog.setMessage("程序将自动执行！");
		dialog.setPositiveButton("取消", new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				stopHandler = true;
			}
		}, delayTime);
		dialog.show();
		dialog.setButtonType(Dialog.BUTTON_POSITIVE, delayTime, true);
	}

	public void restart() {
		try {
			Process process = Runtime.getRuntime().exec("su");
			DataOutputStream out = new DataOutputStream(
					process.getOutputStream());
			out.writeBytes("reboot \n");
			out.writeBytes("exit\n");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private  void getSIMInfo() {
		try {
			TelephonyManager tm =  (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);  
			Class<?> c = Class.forName("com.android.internal.telephony.Phone");
			Field fields1 = c.getField("GEMINI_SIM_1");
			fields1.setAccessible(true);
			Integer simId_1 = (Integer) fields1.get(null);
			Field fields2 = c.getField("GEMINI_SIM_2");
			fields2.setAccessible(true);
			Integer simId_2 = (Integer) fields2.get(null);

			Method m = TelephonyManager.class.getDeclaredMethod(
					"getSubscriberIdGemini", int.class);


			Method m1 = TelephonyManager.class.getDeclaredMethod(
					"getDeviceIdGemini", int.class);
			String imei_1 = (String) m1.invoke(tm, simId_1);
			String imei_2 = (String) m1.invoke(tm, simId_2);

			Log.i(TAG,"imei_1: "+imei_1);
			Log.i(TAG,"imei_2: "+imei_2);

			imei1Edittext.setText(imei_1);
			imei2Edittext.setText(imei_2);


		} catch (Exception e) {
			return;
		}
	}

	public class TimerDialog{

		private static final int TYPE_POSITIVE = 1;
		private static final int TYPE_NEGATIVE = 2;

		private Context mContext;
		private Button p = null;
		private Button n = null;    
		private int mPositiveCount = 0;
		private int mNegativeCount = 0;
		private AlertDialog mDialog = null;

		public TimerDialog(Context ctx){
			mContext = ctx;

			mDialog = new AlertDialog.Builder(mContext).create();
		}	

		public void setMessage(String msg){
			mDialog.setMessage(msg);
		}

		public void setTitle(int resId){
			mDialog.setTitle(resId);
		}

		public void setTitle(String title){
			mDialog.setTitle(title);
		}

		public void show(){
			mDialog.show();

		}

		public void setPositiveButton(String text, DialogInterface.OnClickListener listener, int count){
			text = getTimeText(text, count);
			mDialog.setButton(Dialog.BUTTON_POSITIVE, text, listener);
		}

		public void setNegativeButton(String text, DialogInterface.OnClickListener listener, int count){
			text = getTimeText(text, count);
			mDialog.setButton(Dialog.BUTTON_NEGATIVE, text, listener);
		}


		public void setButtonType(int type, int count, boolean isDisable){

			if(count <= 0){
				return;
			}

			if(type == Dialog.BUTTON_POSITIVE){			
				p = mDialog.getButton(AlertDialog.BUTTON_POSITIVE);
				p.setEnabled(isDisable);
				mPositiveCount = count;
				mHandler.sendEmptyMessageDelayed(TYPE_POSITIVE, 200);
			}else{
				if(type == Dialog.BUTTON_NEGATIVE){				
					n = mDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
					n.setEnabled(isDisable);
					mNegativeCount = count;
					mHandler.sendEmptyMessageDelayed(TYPE_NEGATIVE, 200);
				}
			}
		}

		private Handler mHandler = new Handler(){

			public void handleMessage(Message msg){

				switch(msg.what){
				case TYPE_NEGATIVE:       		
					if(mNegativeCount > 0){
						mNegativeCount--;
						if(n != null){
							String text = (String) n.getText();
							n.setText(getTimeText(text, mNegativeCount));
						}
						mHandler.sendEmptyMessageDelayed(TYPE_NEGATIVE, 1000);
					}else{

						if(n != null){
							if(n.isEnabled()){
								n.performClick();
							}else{
								n.setEnabled(true);
							}
						}
					}
					break;
				case TYPE_POSITIVE:
					if(mPositiveCount > 0){
						mPositiveCount--;
						if(p != null){
							String text = (String) p.getText();
							p.setText(getTimeText(text, mPositiveCount));
						}
						if(stopHandler){
							mHandler.removeMessages(TYPE_POSITIVE);
							return ;
						}
						mHandler.sendEmptyMessageDelayed(TYPE_POSITIVE, 1000);
					}else{

						if(p != null){
							if(p.isEnabled()){
								p.performClick();
							}else{
								p.setEnabled(true);
							}
						}

						writeImei();
						restart();
						try {
							writeModel();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				}
			}
		};


		private String getTimeText(String text, int count){
			if(text != null && text.length() > 0 && count > 0){
				int index = text.indexOf("(");
				if(index > 0){
					text = text.substring(0, index);
					return (text + "("+count+"s)");
				}else{
					return (text + "("+count+"s)");
				}
			}
			return text;
		}
	}
}


