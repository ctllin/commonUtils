package com.ctl.utils.mina;

/**
 * @author 		 guolin
 * @tel			 18515287139
 * @date    	 2016-2-26上午9:34:20
 * @package_name com.ctl.util.mina
 * @project_name ctlUtils
 * @version 	 version.1.0
 * @description
 */


import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class TimeClientHander implements IoHandler {
	@Override
	public void inputClosed(IoSession session) throws Exception {

	}

	@Override
	public void exceptionCaught(IoSession arg0, Throwable arg1)
			throws Exception {
		// TODO Auto-generated method stub
		arg1.printStackTrace();
	}

	@Override
	public void messageReceived(IoSession arg0, Object message) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("client接受信息:"+message.toString());
	}

	@Override
	public void messageSent(IoSession arg0, Object message) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("client发送信息"+message.toString());
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("client与:"+session.getRemoteAddress().toString()+"断开连接");
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("client与:"+session.getRemoteAddress().toString()+"建立连接");
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		// TODO Auto-generated method stub
		System.out.println( "IDLE " + session.getIdleCount( status ));
	}

	@Override
	public void sessionOpened(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("打开连接");
	}

}
