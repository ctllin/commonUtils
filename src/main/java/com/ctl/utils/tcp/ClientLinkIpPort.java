package com.ctl.utils.tcp;

public class ClientLinkIpPort {
    String ip;
    int port;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ClientLinkIpPort(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public ClientLinkIpPort() {
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return this.ip + ":" + this.port;
    }

}
