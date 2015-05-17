package com.powcan.scale.net;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.google.gson.Gson;

import android.content.Context;
import android.util.Log;

/**
 * 网络连接类
 * @author Administrator
 *
 */
public class NetRequest {
	
	public static final String TAG = NetRequest.class.getSimpleName();

	public static final String HOST = "120.24.60.164";
	public static final int PORT = 6010;
	
	public Gson mGson;

	private static NetRequest instance_;
	private Context mContext;

	/**
	 * 构造函数
	 * @param context
	 */
	private NetRequest(Context context) {
		this.mContext = context;
		mGson = new Gson();
	}

	/**
	 * 返回当前对象
	 * @param context
	 * @return
	 */
	public synchronized static NetRequest getInstance(Context context) {
		Context applicationContext = context.getApplicationContext();
		if (instance_ == null || instance_.mContext != applicationContext) {
			instance_ = new NetRequest(applicationContext);
		}

		return instance_;
	}
	
	/**
	 * 发送数据
	 * @param obj 数据对象
	 * @return
	 */
	public String send(Object obj) {
		return send(mGson.toJson(obj));
	}
	
	/**
	 * 泛型发送数据
	 * @param obj
	 * @param c
	 * @return
	 */
	public <T> T send(Object obj, Class<T> c) {
		String response = send(obj);
		return mGson.fromJson(response, c);
	}
	
	/**
	 * 发送数据
	 * @param content 字符串数据
	 * @return
	 */
	public String send(String content) {
		Log.d(TAG, "send request - " + content);
		String response = null;
		try {
			Socket socket = new Socket(HOST, PORT);

			// 由Socket对象得到输出流
			DataOutputStream os = new DataOutputStream(socket.getOutputStream());

			// 由Socket对象得到输入流，并构造相应的BufferedReader对象
			BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			byte[] dataBuf = content.getBytes();
			int len = dataBuf.length;

			ByteBuffer dataLenBuf = ByteBuffer.allocate(4);
			dataLenBuf.order(ByteOrder.LITTLE_ENDIAN);
			dataLenBuf.putInt(0, len);
			
			ByteBuffer sendBuf = ByteBuffer.allocate(len + 4);
			sendBuf.order(ByteOrder.LITTLE_ENDIAN);
			sendBuf.put(dataLenBuf.array(), 0, 4);
			sendBuf.put(dataBuf, 0, len);
			
			// 向120.24.60.164的6010端口发出客户请求
			os.write(sendBuf.array(), 0, len + 4);
			
			// 刷新输出流，使Server马上收到该字符串
			os.flush();
			
			StringBuffer responseBuf = new StringBuffer();
			String line;
			while ((line = is.readLine()) != null) {
				responseBuf.append(line);				
			}
			response = responseBuf.toString();
			Log.d(TAG, "send response - " + response);

			os.close(); // 关闭Socket输出流
			is.close(); // 关闭Socket输入流
			socket.close(); // 关闭Socket
		} catch (Exception e) {
			e.printStackTrace(); // 出错，则打印出错信息
		}
		return response;
	}

}
