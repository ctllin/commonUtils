public class Test {
    public static void main(String args[]){
        System.out.println("linux 下面intelliJ idea中文乱码解决办法\n" +
                "1、下载simsun.zip 下载地址: http://dl.pconline.com.cn/download/367689-1.html\n" +
                "2、将simsun.ttf 拷贝到linux系统中的 /usr/share/fonts/truetype目录\n" +
                "3、建立字体索引信息，更新字体缓存：\n" +
                "    # cd /usr/share/fonts/truetype\n" +
                "    # mkfontscale\n" +
                "    # mkfontdir\n" +
                "    # fc-cache\n" +
                "    至此，字体已经安装完毕！\n" +
                "linux里查看已经安装的字体：\n" +
                " fc-list :lang=zh\n" +
                "4、重启idea");
    }
}
