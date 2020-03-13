
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class HelperMethods {
	
	// buffer.flip() �� position(������ġ)�� limit(������ ����� ��)�� �����ϰ�, position�� 0���� �ٲ۴�.
	// �׷��� ó������ �������� �� ��, ����Ѵ�.
	// ���� ���� �޽��� ó��
	public static void sendMessage(SocketChannel socketChannel, String message){
		try {
			
			ByteBuffer buffer = ByteBuffer.allocate(message.length() + 1); // ���� ���ڸ� �ֱ� ���� +1 �� �Ѵ�.
			buffer.put(message.getBytes()); 							   // �޼����� �ִ´�.
			buffer.put((byte)0x00); 									   // �޼��� ���� ���� ���ڸ� �ִ´�.
			buffer.flip(); 												   // ���۸� ���� �غ� �Ѵ�.
			while(buffer.hasRemaining()){ 	 // ���� ���� ������
				socketChannel.write(buffer); // ����.
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String receiveMessage(SocketChannel socketChannel){
		try {
			ByteBuffer byteBuffer = ByteBuffer.allocate(32); // ���� ũ�⸦ 16 ���� �����Ѵ�.
			String message = "";
			while(socketChannel.read(byteBuffer) > 0){ // �� ���� ���� �ִٸ�, (16�� ��� �д� ��)
				char byteRead = 0x00; // ���� ���ڸ� �����Ѵ�.
				byteBuffer.flip(); 	  // ���۸� ���� �غ� �Ѵ�.
				
				while(byteBuffer.hasRemaining()){ 	   // �о�� 16 �߿��� ���� ���� �ִٸ�,
					byteRead = (char)byteBuffer.get(); // �ϳ��� �޴´�.
					if(byteRead == 0x00){ // ���� ���ڶ��,  while�� Ż���Ѵ�.
						break;
					}
					message += byteRead;  // ���� ���ڰ� �ƴϸ�, message �� �߰��Ѵ�.
				}
				
				if(byteRead == 0x00){ // ���� ���ڶ�� while�� Ż���Ѵ�.
					break;
				}
				byteBuffer.clear(); // ���� clear.
			}
			return message; // �ϳ��ϳ��� ����� message�� return �Ѵ�.
		} catch (IOException e) {

			e.printStackTrace();
		}
		return "";
	}
}