package com.ctl.utils.mina;

/**
 * @author 		 guolin
 * @tel			 18515287139
 * @date    	 2016-2-26上午9:35:09
 * @package_name com.ctl.util.mina
 * @project_name ctlUtils
 * @version 	 version.1.0
 * @description
 */



import java.util.Date;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class TimeServerHandler implements IoHandler {

	@Override
	public void exceptionCaught(IoSession arg0, Throwable arg1)
			throws Exception {
		arg1.printStackTrace();

	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {

		String str = message.toString();

		System.out.println("接受到的消息:"+str);

		if( str.trim().equalsIgnoreCase("quit") ) {
			session.close(true);
			return;
		}
		Date date = new Date();
		session.write( date.toString() );
		System.out.println("Message written...");
	}

	@Override
	public void messageSent(IoSession arg0, Object arg1) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("发送信息:"+arg1.toString());
	}

	@Override
	public void inputClosed(IoSession session) throws Exception {
		System.out.println("IP:"+session.getRemoteAddress().toString()+"断开连接inputClosed");
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("IP:"+session.getRemoteAddress().toString()+"断开连接");
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("IP:"+session.getRemoteAddress().toString());
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		// TODO Auto-generated method stub
		System.out.println( "IDLE " + session.getIdleCount( status ));

	}

	@Override
	public void sessionOpened(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub

	}

}

