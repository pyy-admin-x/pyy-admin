package com.thtf.base.server.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 自动填充配置
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    private String userId;
    private String username;

    public MyMetaObjectHandler() {
        this.userId = "";
        this.username = "";
        log.info("### 当前操用户：userId={}, username={} ###", userId, username);
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        // 创建人ID
        if (metaObject.hasSetter("createId")) {
            this.setFieldValByName("createId", userId, metaObject);
        }
        // 创建人名称
        if (metaObject.hasSetter("createName")) {
            this.setFieldValByName("createName", username, metaObject);
        }
        // 创建时间
        if (metaObject.hasSetter("createTime")) {
            this.setFieldValByName("createTime", new Date(), metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 修改人ID
        if (metaObject.hasSetter("updateId")) {
            this.setFieldValByName("updateId", userId, metaObject);
        }
        // 修改人名称
        if (metaObject.hasSetter("updateName")) {
            this.setFieldValByName("updateName", username, metaObject);
        }
        // 修改人时间
        if (metaObject.hasSetter("updateTime")) {
            this.setFieldValByName("updateTime", new Date(), metaObject);
        }
    }
}
