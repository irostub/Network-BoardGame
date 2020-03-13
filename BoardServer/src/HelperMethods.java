
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class HelperMethods {
	
	// buffer.flip() 은 position(현재위치)를 limit(어디까지 사용할 지)로 설정하고, position을 0으로 바꾼다.
	// 그래서 처음부터 읽으려고 할 때, 사용한다.
	// 가변 길이 메시지 처리
	public static void sendMessage(SocketChannel socketChannel, String message){
		try {
			
			ByteBuffer buffer = ByteBuffer.allocate(message.length() + 1); // 종료 문자를 넣기 위해 +1 을 한다.
			buffer.put(message.getBytes()); 							   // 메세지를 넣는다.
			buffer.put((byte)0x00); 									   // 메세지 끝에 종료 문자를 넣는다.
			buffer.flip(); 												   // 버퍼를 읽을 준비를 한다.
			while(buffer.hasRemaining()){ 	 // 읽을 것이 있으면
				socketChannel.write(buffer); // 쓴다.
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String receiveMessage(SocketChannel socketChannel){
		try {
			ByteBuffer byteBuffer = ByteBuffer.allocate(32); // 버퍼 크기를 16 으로 지정한다.
			String message = "";
			while(socketChannel.read(byteBuffer) > 0){ // 더 읽을 것이 있다면, (16씩 계속 읽는 중)
				char byteRead = 0x00; // 종료 문자를 설정한다.
				byteBuffer.flip(); 	  // 버퍼를 읽을 준비를 한다.
				
				while(byteBuffer.hasRemaining()){ 	   // 읽어온 16 중에서 읽을 것이 있다면,
					byteRead = (char)byteBuffer.get(); // 하나씩 받는다.
					if(byteRead == 0x00){ // 종료 문자라면,  while을 탈출한다.
						break;
					}
					message += byteRead;  // 종료 문자가 아니면, message 에 추가한다.
				}
				
				if(byteRead == 0x00){ // 종료 문자라면 while을 탈출한다.
					break;
				}
				byteBuffer.clear(); // 버퍼 clear.
			}
			return message; // 하나하나씩 모아진 message를 return 한다.
		} catch (IOException e) {

			e.printStackTrace();
		}
		return "";
	}
}