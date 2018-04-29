package com.ctl.utils.tcp;

import com.ctl.utils.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;


@SuppressWarnings({"serial", "deprecation", "unused"})
public class TCPServerUtil extends JPanel {
    private static Logger logger = LoggerFactory.getLogger(TCPServerUtil.class);
    public static StringBuilder receiveMsgTextArea = new StringBuilder();
    static Thread acceptThread;
    static JButton serverBtn;
    static JTextArea jta;
    static JTextArea serverSendMsg;
    static boolean isCloseServer = false;// \u662F\u5426\u5173\u95EDserver
    public static java.util.List<ClientLink> clientsLinkedList = new ArrayList<ClientLink>();
    public static java.util.List<ClientLinkIpPort> clientsLinkedIpPortList = new ArrayList<ClientLinkIpPort>();
    public static java.util.List<Thread> clientLinkedThreads = new ArrayList<Thread>();
    public static ServerSocket server;
    public static JList jlist;
    // \u5168\u805A\u5FB7socket\u7528\u4E8E\u5F53\u70B9\u51FB\u5BA2\u6237\u7AEF\u8FDE\u63A5\u7684list\u662F\u5C06\u9009\u4E2D\u7684\u989Ditem\u4E2D\u7684client\u8D4B\u503C\u7ED9\u8BE5\u5168\u5C40clientGlobal\u7528\u4E8E\u53D1\u9001\u6D88\u606F
    public static Socket clientGlobal;

    public TCPServerUtil(JFrame jf) {
        this.setBounds(0, 0, 300, 600);
        jf.add(this);
        this.setBackground(new Color(199, 237, 204));
        this.setLayout(null);

        JLabel jls1 = new JLabel("\u670D\u6237\u7AEF");
        jls1.setBounds(0, 0, 50, 20);
        this.add(jls1);
        jls1.setFont(new Font("\u6977\u4F53", 11, 10));

        JLabel jls2 = new JLabel("\u672C\u673Aip:");
        jls2.setBounds(0, 5, 50, 45);
        this.add(jls2);
        jls2.setFont(new Font("\u6977\u4F53", 11, 10));

        // IP\u6846
        JTextField jtfip = new JTextField(18);
        jtfip.setBounds(40, 17, 100, 20);
        this.add(jtfip);
        try {
            jtfip.setText(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e2) {
        }
        jtfip.setEditable(false);

        JLabel jls3 = new JLabel("\u7AEF\u53E3:");
        jls3.setBounds(145, 5, 50, 45);
        this.add(jls3);
        jls3.setFont(new Font("\u6977\u4F53", 11, 10));
        // \u7AEF\u53E3\u6846
        JTextField jtfport = new JTextField(15);
        jtfport.setBounds(175, 17, 45, 20);
        this.add(jtfport);
        if (ConfigUtils.getType("tcp.port") == null || "".equals(ConfigUtils.getType("tcp.port").trim())) {
            jtfport.setText("1234");
        } else {
            jtfport.setText(ConfigUtils.getType("tcp.port"));
        }

        serverBtn = new JButton("\u5F00\u59CB\u670D\u52A1");
        serverBtn.setBounds(225, 17, 50, 20);
        this.add(serverBtn);
        serverBtn.setMargin(new Insets(0, 0, 0, 0));// \u8FD9\u6837\u8BBE\u7F6Ebutton\u4E2D\u7684\u5B57\u4F53\u4E0Ebutton\u65E0\u4E0A\u4E0B\u8FB9\u8DDD
        serverBtn.setFont(new Font("\u6977\u4F53", 11, 10));
        MouseListener startServerListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String title = serverBtn.getText();
                if (title.equals("\u505C\u6B62\u670D\u52A1")) {
                    stopServer();
                    return;
                }
                startServer();
            }

        };
        serverBtn.addMouseListener(startServerListener);

        JLabel jls4 = new JLabel("\u670D\u6237\u7AEF\u4FE1\u606F");
        jls4.setBounds(0, 25, 70, 45);
        this.add(jls4);
        jls4.setFont(new Font("\u6977\u4F53", 11, 10));

        // server \u6587\u672C\u6846
        jta = new JTextArea("", 13, 0);
        jta.setBounds(0, 55, 280, 270);
        jta.setWrapStyleWord(true);// \u6FC0\u6D3B\u65AD\u884C\u4E0D\u65AD\u5B57\u529F\u80FD
        jta.setLineWrap(true);
        jta.setFont(new Font("\u6977\u4F53 ", 11, 10));
        jta.setBackground(new Color(240, 240, 240));
        jta.setEditable(false);
        JScrollPane js = new JScrollPane(jta);
        js.setBounds(0, 55, 280, 270);
        this.add(js);

        JLabel jls6 = new JLabel("\u5DF2\u7ECF\u5EFA\u7ACB\u7684\u8FDE\u63A5");
        jls6.setBounds(0, 310, 120, 45);
        this.add(jls6);
        jls6.setFont(new Font("\u6977\u4F53", 11, 10));
        // \u6E05\u9664server\u4E0Eclient\u7684\u4EA4\u4E92\u4FE1\u606F
        JButton clearMsg = new JButton("\u6E05\u9664");
        clearMsg.setBounds(227, 325, 50, 20);
        this.add(clearMsg);
        clearMsg.setMargin(new Insets(0, 0, 0, 0));
        clearMsg.setFont(new Font("\u6977\u4F53", 11, 10));
        MouseListener clearReceiveListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int length = receiveMsgTextArea.length();
                receiveMsgTextArea.delete(0, length);
                jta.setText("");
            }
        };
        clearMsg.addMouseListener(clearReceiveListener);
        jlist = new JList();

        // \u5BA2\u6237\u7AEFlist JScrollPane
        JScrollPane js1 = new JScrollPane(jlist);
        js1.setBounds(0, 347, 280, 130);
        this.add(js1);
        MouseListener jlistListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectIndex = jlist.getSelectedIndex();
                if (selectIndex < 0)
                    return;
                Object s = jlist.getSelectedValue();// 127.0.0.1:49387
                String ss[] = s.toString().split(":");
                String ip = ss[0];
                for (int i = 0; i < clientsLinkedList.size(); i++) {
                    ClientLink client = clientsLinkedList.get(i);
                    String ip1 = client.getClientlinked().getInetAddress()
                            .toString();
                    if (ip1.endsWith(ip)
                            && client.getClientLinkedPort() == Integer
                            .parseInt(ss[1].trim())) {
                        clientGlobal = client.getClientlinked();
                        break;
                    }
                }
                System.out.println(s.toString());
            }
        };
        jlist.addMouseListener(jlistListener);

        JLabel jls8 = new JLabel("\u5F85\u53D1\u9001\u6570\u636E");
        jls8.setBounds(0, 463, 60, 45);
        this.add(jls8);
        jls8.setFont(new Font("\u6977\u4F53", 11, 10));
        // server send \u6587\u672C\u6846
        final JTextArea serverSendMsg = new JTextArea(ConfigUtils.getType("tcp.textarea"), 9, 0);
        serverSendMsg.setBounds(0, 500, 280, 140);
        serverSendMsg.setFont(new Font("\u6977\u4F53 ", 10, 11));
        serverSendMsg.setWrapStyleWord(true);// \u6FC0\u6D3B\u65AD\u884C\u4E0D\u65AD\u5B57\u529F\u80FD
        // serverSendMsg.setBackground(Color.pink);
        serverSendMsg.setLineWrap(true);

        JScrollPane js2 = new JScrollPane(serverSendMsg);
        js2.setBounds(0, 500, 280, 140);
        this.add(js2);

        JButton closeLink = new JButton("\u65AD\u5F00\u8FDE\u63A5");
        closeLink.setBounds(5, 645, 60, 20);
        this.add(closeLink);
        closeLink.setFont(new Font("\u6977\u4F53 ", 11, 10));
        closeLink.setMargin(new Insets(0, 0, 0, 0));
        MouseListener closeClientLinkListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                try {
                    int selectIndex = jlist.getSelectedIndex();
                    if (selectIndex == -1)
                        return;
                    clientGlobal.close();
                    clientGlobal = null;

                    Object s = jlist.getSelectedValue();// 127.0.0.1:49387
                    String ss[] = s.toString().split(":");
                    String ip = ss[0];
                    for (int i = 0; i < clientsLinkedList.size(); i++) {
                        ClientLink client = clientsLinkedList.get(i);
                        String ip1 = client.getClientlinked().getInetAddress()
                                .toString();
                        if (ip1.endsWith(ip)
                                && client.getClientLinkedPort() == Integer
                                .parseInt(ss[1].trim())) {
                            clientsLinkedList.remove(i);
                            clientsLinkedIpPortList.remove(i);
                            jlist.setListData(clientsLinkedIpPortList
                                    .toArray(new ClientLinkIpPort[]{}));
                            break;
                        }
                    }

                    System.out.println("clientGlobal close 0");
                } catch (IOException e1) {
                    e1.printStackTrace();
                } finally {
                }
            }
        };
        closeLink.addMouseListener(closeClientLinkListener);

        JButton clearServerSendMsg = new JButton("\u6E05\u9664");
        clearServerSendMsg.setBounds(117, 645, 60, 20);
        this.add(clearServerSendMsg);
        clearServerSendMsg.setMargin(new Insets(0, 0, 0, 0));
        clearServerSendMsg.setFont(new Font("\u6977\u4F53 ", 11, 10));
        MouseListener clearSendListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                serverSendMsg.setText("");
            }
        };
        clearServerSendMsg.addMouseListener(clearSendListener);

        JButton sendMsgBtn = new JButton("\u53D1\u9001\u6570\u636E");
        sendMsgBtn.setBounds(220, 645, 60, 20);
        this.add(sendMsgBtn);
        sendMsgBtn.setMargin(new Insets(0, 0, 0, 0));
        sendMsgBtn.setFont(new Font("\u6977\u4F53 ", 11, 10));
        MouseListener SendMsgListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                final String str = serverSendMsg.getText().trim();
                if (clientGlobal != null) {
                    if (str != null && !"".equals(str)) {
                        if (str.length() > 4096) {
                            JOptionPane.showMessageDialog(null,
                                    "\u6570\u636E\u592A\u5927");
                            return;
                        }

                        class SendMessage implements Runnable {

                            public void run() {
                                BufferedWriter bufferedWriter = null;
                                try {
                                    String clientSendCode = ConfigUtils.getType("tcp.clientSendCode");
                                    if (clientSendCode == null
                                            || clientSendCode.trim().endsWith(
                                            "")) {
                                        clientSendCode = "utf-8";
                                    }
                                    bufferedWriter = new BufferedWriter(
                                            new OutputStreamWriter(clientGlobal
                                                    .getOutputStream(),
                                                    clientSendCode));
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                int size;
                                try {
                                    size = Integer.parseInt(ConfigUtils
                                            .getType("tcp.sendBufSize"));
                                } catch (Exception e2) {
                                    size = 1024;
                                }
                                int length = str.trim().length();
                                int times = length % size == 0 ? length / size
                                        : length / size + 1;
                                // \u4E0B\u9762\u7528\u6765\u5C06client\u7684\u6570\u636E\u5206\u5272\u591A\u6B21\u53D1\u9001
                                for (int i = 0; i < times; i++) {
                                    if (i != times - 1) {
                                        String w = str.substring(i * size,
                                                (i + 1) * size);
                                        try {
                                            bufferedWriter.write(w.trim());
                                            bufferedWriter.flush();
                                        } catch (Exception e2) {
                                            e2.printStackTrace();
                                        }

                                        setReceiveTextAreaContentServer(
                                                clientGlobal,
                                                " \u53D1\u9001:\n" + w.trim()
                                                        + "\n");
                                    } else {
                                        String w = str.substring(i * size,
                                                length);
                                        try {
                                            bufferedWriter.write(w.trim());
                                            bufferedWriter.flush();
                                        } catch (Exception e2) {
                                            e2.printStackTrace();
                                        }
                                        setReceiveTextAreaContentServer(
                                                clientGlobal,
                                                " \u53D1\u9001:\n" + w.trim()
                                                        + "\n");
                                    }
                                }
                            }

                            private void setReceiveTextAreaContentServer(
                                    Socket clientGlobal, String string) {
                                receiveMsgTextArea
                                        .append("\u670D\u52A1\u7AEF("
                                                + clientGlobal.getInetAddress()
                                                + ":" + clientGlobal.getPort()
                                                + ")" + string);
                                jta.setText(receiveMsgTextArea.toString());
                                jta.setCaretPosition(receiveMsgTextArea
                                        .length());// \u8BA9\u6EDA\u52A8\u6761\u81F3\u4E8E\u6700\u4E0B\u9762ed
                                // method stub

                            }

                        }
                        SendMessage sm = new SendMessage();
                        new Thread(sm).start();

                    }
                }

            }
        };
        sendMsgBtn.addMouseListener(SendMsgListener);

    }

    private void setReceiveTextAreaContent(final Socket client, String param) {
        receiveMsgTextArea.append("\u5BA2\u6237\u7AEF ("
                + client.getInetAddress() + ":" + client.getPort() + ")"
                + param);
        jta.setText(receiveMsgTextArea.toString());
        jta.setCaretPosition(receiveMsgTextArea.length());// \u8BA9\u6EDA\u52A8\u6761\u81F3\u4E8E\u6700\u4E0B\u9762
    }

    private void startServer() {
        try {
            int defaultPort = 1234;
            if (ConfigUtils.getType("tcp.port") == null
                    || "".equals(ConfigUtils.getType("tcp.port").trim())) {
                server = new ServerSocket(defaultPort);
            } else {
                server = new ServerSocket(Integer.parseInt(ConfigUtils.getType(
                        "tcp.port").trim()));
            }
            isCloseServer = false;
            serverBtn.setText("\u505C\u6B62\u670D\u52A1");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        // \u7528\u4E8E\u5904\u7406while\u5FAA\u73AF
        // \u63A5\u6536\u5BA2\u6237\u7AEF\u8FDE\u63A5
        class StartServerThread implements Runnable {
            // Socket client;
            public void run() {
                while (!isCloseServer && server != null) {
                    try {
                        final Socket client = server.accept();
                        setReceiveTextAreaContent(client, " \u8FDE\u63A5 \n");
                        final ClientLink cl = new ClientLink(client,
                                client.getPort());
                        clientsLinkedList.add(cl);
                        String str = client.getInetAddress().toString();
                        final ClientLinkIpPort clip = new ClientLinkIpPort(
                                str.substring(1, str.length()),
                                client.getPort());
                        clientsLinkedIpPortList.add(clip);
                        jlist.setListData(clientsLinkedIpPortList
                                .toArray(new ClientLinkIpPort[]{}));
                        class ClientHandleThread implements Runnable {
                            public void run() {
                                int receiveBufSize;
                                try {
                                    receiveBufSize = Integer
                                            .parseInt(ConfigUtils
                                                    .getType("tcp.receiveBufSize"));
                                } catch (Exception e) {
                                    receiveBufSize = 1024;
                                }
                                char c[] = new char[receiveBufSize];
                                int len = 0;
                                Reader reader = null;
                                try {
                                    String serverSendCode = null;
                                    if (ConfigUtils
                                            .getType("tcp.ServerSendCode") == null
                                            || "".equals(ConfigUtils.getType(
                                            "tcp.ServerSendCode")
                                            .trim())) {
                                        serverSendCode = "utf-8";
                                    }
                                    reader = new InputStreamReader(
                                            client.getInputStream(),
                                            ConfigUtils
                                                    .getType("tcp.ServerSendCode"));
                                    len = reader.read(c);
                                    String str = null;
                                    if (len < 0) {
                                        if (client != null) {
                                            client.close();
                                        }
                                        if (reader != null) {
                                            reader.close();
                                            reader = null;
                                        }
                                        clientsLinkedList.remove(cl);
                                        clientsLinkedIpPortList.remove(clip);
                                        jlist.setListData(clientsLinkedIpPortList
                                                .toArray(new ClientLinkIpPort[]{}));
                                        setReceiveTextAreaContent(client,
                                                " \u5173\u95ED \n");
                                        return;
                                    }
                                    if (len > 0) {
                                        str = new String(c, 0, len);
                                    }
                                    setReceiveTextAreaContent(client,
                                            " \u53D1\u9001 \n" + str.trim()
                                                    + "\n");
                                } catch (IOException e2) {
                                    logger.info("linkClick reader.read(c)   \u5931\u8D25");
                                } // \u8BFB\u53D6
                                while (len > 0 && !isCloseServer
                                        && reader != null && !client.isClosed()) {
                                    try {
                                        len = reader.read(c);
                                    } catch (IOException e1) {
                                        System.err.println("reader error");
                                    }
                                    if (len > 0) {
                                        String str = new String(c, 0, len);
                                        setReceiveTextAreaContent(
                                                client,
                                                " \u53D1\u9001: \n"
                                                        + str.trim() + "\n");
                                    }
                                    try {
                                        Thread.sleep(10);
                                    } catch (InterruptedException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                                if (reader != null) {
                                    try {
                                        reader.close();
                                        client.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    reader = null;
                                }
                                clientsLinkedList.remove(cl);
                                clientsLinkedIpPortList.remove(clip);
                                jlist.setListData(clientsLinkedIpPortList
                                        .toArray(new ClientLinkIpPort[]{}));
                                setReceiveTextAreaContent(client,
                                        " \u5173\u95ED\n");
                                System.out.println("one client link close");
                            }
                        }
                        ClientHandleThread cht = new ClientHandleThread();
                        Thread clientThread = new Thread(cht);
                        clientThread.start();
                        clientLinkedThreads.add(clientThread);
                    } catch (IOException e) {
                        System.err.println("Socket is closed");
                    }
                }
                System.out.println("accept close");
            }
        }
        StartServerThread sst = new StartServerThread();
        acceptThread = new Thread(sst);
        acceptThread.start();
    }

    private static void stopServer() {
        try {
            if (server != null) {
                server.close();
            }
            if (clientGlobal != null) {
                clientGlobal.close();
            }
            for (int i = 0; i < clientLinkedThreads.size(); i++) {
                clientLinkedThreads.get(i).stop();
            }
            for (int i = 0; i < clientsLinkedList.size(); i++) {
                Socket client = clientsLinkedList.get(i).getClientlinked();
                if (client != null) {
                    client.close();
                    client = null;
                }
            }
            serverBtn.setText("\u5F00\u59CB\u670D\u52A1");
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            isCloseServer = true;
            clientGlobal = null;
            server = null;
            clientsLinkedIpPortList.clear();
            clientsLinkedList.clear();
            jlist.setListData(clientsLinkedIpPortList
                    .toArray(new ClientLinkIpPort[]{}));
            if (acceptThread != null)
                acceptThread.stop();
        }
    }

    /**
     *
     tcp.port=9999
     tcp.textarea=server send
     tcp.clientSendCode=gbk
     tcp.ServerSendCode=gbk
     tcp.sendBufSize=1024
     tcp.receiveBufSize=1024
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        JFrame jf = new JFrame("TCPServerUtil");
        //jf.setBounds(300, 0, 287, 700);
        jf.setBounds(350, 0, 350, 800);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setResizable(false);//窗体大小不可以拉动
        new TCPServerUtil(jf);
        jf.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (isCloseServer != false)
                    stopServer();
            }
        });
    }
}
