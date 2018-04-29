package com.ctl.utils.db;

public class User {
    private String Host;
    private String User;
    private String Password;
    private String Select_priv;
    private String Insert_priv;
    private String Update_priv;
    private String Delete_priv;
    private String Create_priv;
    private String Drop_priv;
    private String Reload_priv;
    private String Shutdown_priv;
    private String Process_priv;
    private String File_priv;
    private String Grant_priv;
    private String References_priv;
    private String Index_priv;
    private String Alter_priv;
    private String Show_db_priv;
    private String Super_priv;
    private String Create_tmp_table_priv;
    private String Lock_tables_priv;
    private String Execute_priv;
    private String Repl_slave_priv;
    private String Repl_client_priv;
    private String Create_view_priv;
    private String Show_view_priv;
    private String Create_routine_priv;
    private String Alter_routine_priv;
    private String Create_user_priv;
    private String Event_priv;
    private String Trigger_priv;
    private String Create_tablespace_priv;
    private String ssl_type;
    private Integer max_questions;
    private Integer max_updates;
    private Integer max_connections;
    private Integer max_user_connections;
    private String plugin;

    public String getHost() {
        return Host;
    }

    public void setHost(String host) {
        Host = host;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getSelect_priv() {
        return Select_priv;
    }

    public void setSelect_priv(String select_priv) {
        Select_priv = select_priv;
    }

    public String getInsert_priv() {
        return Insert_priv;
    }

    public void setInsert_priv(String insert_priv) {
        Insert_priv = insert_priv;
    }

    public String getUpdate_priv() {
        return Update_priv;
    }

    public void setUpdate_priv(String update_priv) {
        Update_priv = update_priv;
    }

    public String getDelete_priv() {
        return Delete_priv;
    }

    public void setDelete_priv(String delete_priv) {
        Delete_priv = delete_priv;
    }

    public String getCreate_priv() {
        return Create_priv;
    }

    public void setCreate_priv(String create_priv) {
        Create_priv = create_priv;
    }

    public String getDrop_priv() {
        return Drop_priv;
    }

    public void setDrop_priv(String drop_priv) {
        Drop_priv = drop_priv;
    }

    public String getReload_priv() {
        return Reload_priv;
    }

    public void setReload_priv(String reload_priv) {
        Reload_priv = reload_priv;
    }

    public String getShutdown_priv() {
        return Shutdown_priv;
    }

    public void setShutdown_priv(String shutdown_priv) {
        Shutdown_priv = shutdown_priv;
    }

    public String getProcess_priv() {
        return Process_priv;
    }

    public void setProcess_priv(String process_priv) {
        Process_priv = process_priv;
    }

    public String getFile_priv() {
        return File_priv;
    }

    public void setFile_priv(String file_priv) {
        File_priv = file_priv;
    }

    public String getGrant_priv() {
        return Grant_priv;
    }

    public void setGrant_priv(String grant_priv) {
        Grant_priv = grant_priv;
    }

    public String getReferences_priv() {
        return References_priv;
    }

    public void setReferences_priv(String references_priv) {
        References_priv = references_priv;
    }

    public String getIndex_priv() {
        return Index_priv;
    }

    public void setIndex_priv(String index_priv) {
        Index_priv = index_priv;
    }

    public String getAlter_priv() {
        return Alter_priv;
    }

    public void setAlter_priv(String alter_priv) {
        Alter_priv = alter_priv;
    }

    public String getShow_db_priv() {
        return Show_db_priv;
    }

    public void setShow_db_priv(String show_db_priv) {
        Show_db_priv = show_db_priv;
    }

    public String getSuper_priv() {
        return Super_priv;
    }

    public void setSuper_priv(String super_priv) {
        Super_priv = super_priv;
    }

    public String getCreate_tmp_table_priv() {
        return Create_tmp_table_priv;
    }

    public void setCreate_tmp_table_priv(String create_tmp_table_priv) {
        Create_tmp_table_priv = create_tmp_table_priv;
    }

    public String getLock_tables_priv() {
        return Lock_tables_priv;
    }

    public void setLock_tables_priv(String lock_tables_priv) {
        Lock_tables_priv = lock_tables_priv;
    }

    public String getExecute_priv() {
        return Execute_priv;
    }

    public void setExecute_priv(String execute_priv) {
        Execute_priv = execute_priv;
    }

    public String getRepl_slave_priv() {
        return Repl_slave_priv;
    }

    public void setRepl_slave_priv(String repl_slave_priv) {
        Repl_slave_priv = repl_slave_priv;
    }

    public String getRepl_client_priv() {
        return Repl_client_priv;
    }

    public void setRepl_client_priv(String repl_client_priv) {
        Repl_client_priv = repl_client_priv;
    }

    public String getCreate_view_priv() {
        return Create_view_priv;
    }

    public void setCreate_view_priv(String create_view_priv) {
        Create_view_priv = create_view_priv;
    }

    public String getShow_view_priv() {
        return Show_view_priv;
    }

    public void setShow_view_priv(String show_view_priv) {
        Show_view_priv = show_view_priv;
    }

    public String getCreate_routine_priv() {
        return Create_routine_priv;
    }

    public void setCreate_routine_priv(String create_routine_priv) {
        Create_routine_priv = create_routine_priv;
    }

    public String getAlter_routine_priv() {
        return Alter_routine_priv;
    }

    public void setAlter_routine_priv(String alter_routine_priv) {
        Alter_routine_priv = alter_routine_priv;
    }

    public String getCreate_user_priv() {
        return Create_user_priv;
    }

    public void setCreate_user_priv(String create_user_priv) {
        Create_user_priv = create_user_priv;
    }

    public String getEvent_priv() {
        return Event_priv;
    }

    public void setEvent_priv(String event_priv) {
        Event_priv = event_priv;
    }

    public String getTrigger_priv() {
        return Trigger_priv;
    }

    public void setTrigger_priv(String trigger_priv) {
        Trigger_priv = trigger_priv;
    }

    public String getCreate_tablespace_priv() {
        return Create_tablespace_priv;
    }

    public void setCreate_tablespace_priv(String create_tablespace_priv) {
        Create_tablespace_priv = create_tablespace_priv;
    }

    public String getSsl_type() {
        return ssl_type;
    }

    public void setSsl_type(String ssl_type) {
        this.ssl_type = ssl_type;
    }

    public Integer getMax_questions() {
        return max_questions;
    }

    public void setMax_questions(Integer max_questions) {
        this.max_questions = max_questions;
    }

    public Integer getMax_updates() {
        return max_updates;
    }

    public void setMax_updates(Integer max_updates) {
        this.max_updates = max_updates;
    }

    public Integer getMax_connections() {
        return max_connections;
    }

    public void setMax_connections(Integer max_connections) {
        this.max_connections = max_connections;
    }

    public Integer getMax_user_connections() {
        return max_user_connections;
    }

    public void setMax_user_connections(Integer max_user_connections) {
        this.max_user_connections = max_user_connections;
    }

    public String getPlugin() {
        return plugin;
    }

    public void setPlugin(String plugin) {
        this.plugin = plugin;
    }
}
