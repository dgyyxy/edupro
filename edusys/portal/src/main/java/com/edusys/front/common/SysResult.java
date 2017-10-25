package com.edusys.front.common;


import com.edu.common.base.BaseResult;

/**
 * upms系统常量枚举类
 * Created by Gary on 2017/2/18.
 */
public class SysResult extends BaseResult {

    public SysResult(SysResultConstant sysResultConstant, Object data) {
        super(sysResultConstant.getCode(), sysResultConstant.getMessage(), data);
    }

}
