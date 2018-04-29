package com.ctl.utils.rabbitmq;

public class RabbitmqConfig {
//    rabbitmqctl add_user root ctllin
//    rabbitmqctl add_user ctl ctllin
//    rabbitmqctl authenticate_user  root ctllin
//    rabbitmqctl authenticate_user  ctl ctllin
//    rabbitmqctl add_vhost test
//    rabbitmqctl set_permissions -p test root "^root-.*" ".*" ".*"
//    rabbitmqctl set_permissions -p test ctl "^ctl-.*" ".*" ".*"
   // protected static String host="192.168.42.7";
    protected static String host="192.168.42.29";
    protected static String username="root";
    protected static String passward="ctllin";
    //protected static String virtualHost="test";
    protected static String virtualHost="vhost1";
    protected static short port=5672;
//    Fedora 27
//    yum install epel-release
//    yum install erlang
//    yum install scott
//    rpm -ivh rabbitmq-server-3.7.2-1.el7.noarch.rpm
//    rabbitmq-plugins enable rabbitmq_management
//    cp /usr/share/doc/rabbitmq-server-3.7.2/rabbitmq.config.example /etc/rabbitmq/rabbitmq.config
//    rabbitmqctl add_user root 'ctllin'
//    rabbitmqctl add_user ctl 'ctllin'
//    rabbitmqctl set_user_tags  root administrator
//    rabbitmqctl add_vhost vhost1
//    rabbitmqctl  set_permissions -p vhost1  root  ".*"  ".*"   ".*"
}
