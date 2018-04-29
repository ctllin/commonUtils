package com.ctl.utils.crpty.dao;

import java.util.List;

@SuppressWarnings("rawtypes")
public interface DecryptDao {
    /**
     * @param classPackageAndClassName for exemple: com.ctl.util.CopyUtil
     */
    public Class getClassObject(String classPackageAndClassName)
            throws Exception;

    /**
     * @param for    exemple: com.ctl.util.CopyUtil.class
     *               注意该class必须从getClassObject获取
     * @param flag   if flag=0,method is static ,else method is not static
     * @param method args
     * @return
     * @throws Exception
     */

    public Object execMethod(Class clazz, int flag, String methodName, List<TypeValueBean> typeValue);


}
