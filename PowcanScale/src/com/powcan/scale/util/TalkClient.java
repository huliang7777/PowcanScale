package com.powcan.scale.util;
import java.io.*;

import java.net.*;

public class TalkClient {
	public static void main(String args[]) {
		try {
			Socket socket = new Socket("120.24.60.164", 6010);

			// �򱾻��4700�˿ڷ����ͻ����� {"cmd":"GAT","app":"zhifangcheng","amount":"6"}
			BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));

			// ��ϵͳ��׼�����豸����BufferedReader����
			PrintWriter os = new PrintWriter(socket.getOutputStream());

			// ��Socket����õ��������������PrintWriter����
			BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// ��Socket����õ�����������������Ӧ��BufferedReader����
			String readline;

			readline = sin.readLine(); // ��ϵͳ��׼�������һ�ַ�

			while (!readline.equals("bye")) {

				// ���ӱ�׼���������ַ�Ϊ "bye"��ֹͣѭ��
				os.println(readline);

				// ����ϵͳ��׼���������ַ������Server
				os.flush();

				// ˢ���������ʹServer�����յ����ַ�
				System.out.println("Client:" + readline);

				// ��ϵͳ��׼����ϴ�ӡ������ַ�
				System.out.println("Server:" + is.readLine());

				// ��Server����һ�ַ�����ӡ����׼�����
				readline = sin.readLine(); // ��ϵͳ��׼�������һ�ַ�

			} // ����ѭ��
			os.close(); // �ر�Socket�����
			is.close(); // �ر�Socket������
			socket.close(); // �ر�Socket
		} catch (Exception e) {
			System.out.println("Error: " + e); // ���?���ӡ������Ϣ
		}

	}

}