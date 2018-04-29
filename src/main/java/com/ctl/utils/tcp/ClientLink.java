package com.ctl.utils.tcp;

import java.net.Socket;

public class ClientLink {
    Socket clientlinked;
    int clientLinkedPort;

    public ClientLink() {

    }

    public ClientLink(Socket clientlinked, int clientLinkedPort) {
        this.clientlinked = clientlinked;
        this.clientLinkedPort = clientLinkedPort;
    }

    public Socket getClientlinked() {
        return clientlinked;
    }

    public void setClientlinked(Socket clientlinked) {
        this.clientlinked = clientlinked;
    }

    public int getClientLinkedPort() {
        return clientLinkedPort;
    }

    public void setClientLinkedPort(int clientLinkedPort) {
        this.clientLinkedPort = clientLinkedPort;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return this.clientlinked + " " + this.clientLinkedPort;
    }
//	String ip;
//	int index;
//
//	public ClientLink() {
//	}
//
//	public ClientLink(String ip, int index) {
//		this.ip = ip;
//		this.index = index;
//	}
//
//	public String getIp() {
//		return ip;
//	}
//
//	public void setIp(String ip) {
//		this.ip = ip;
//	}
//
//	public int getIndex() {
//		return index;
//	}
//
//	public void setIndex(int index) {
//		this.index = index;
//	}
//
//	@Override
//	public String toString() {
//		// TODO Auto-generated method stub
//		return this.ip + " " + this.index;
//	}
}
