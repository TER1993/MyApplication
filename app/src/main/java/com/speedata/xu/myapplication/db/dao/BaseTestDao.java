package com.speedata.xu.myapplication.db.dao;

import android.content.Context;

import com.elsw.base.db.orm.dao.ABaseDao;
import com.speedata.xu.myapplication.db.bean.BaseTest;


/**
 * Copyright (c) 2012 All rights reserved 名称：UserDao.java 描述：用户信息
 *
 * @author Echo
 * @version v1.0
 * @date：2014-11-09 下午4:12:36
 */
public class BaseTestDao extends ABaseDao<BaseTest> {
    public BaseTestDao(Context context) {
        super(new DBInsideHelper(context), BaseTest.class);
    }
}
