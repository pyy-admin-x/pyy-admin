package com.thtf.base.server.controller;

import com.thtf.base.api.SysRoleControllerApi;
import com.thtf.base.api.vo.*;
import com.thtf.base.server.service.SysRoleService;
import com.thtf.common.core.response.Pager;
import com.thtf.common.core.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ---------------------------
 * 角色 (SysRoleController)         
 * ---------------------------
 * 作者：  pyy
 * 时间：  2020-01-09 14:55:26
 * 版本：  v1.0
 * ---------------------------
 */
@RestController
public class SysRoleController implements SysRoleControllerApi {

	@Autowired
	private SysRoleService sysRoleService;

    @Override
    public ResponseResult<SysRoleVO> save(SysRoleSaveOrUpdateVO record) {
        return ResponseResult.SUCCESS(sysRoleService.save(record));
    }

    @Override
    public ResponseResult<SysRoleVO> update(String id, SysRoleSaveOrUpdateVO record) {
        return ResponseResult.SUCCESS(sysRoleService.update(id, record));
    }

    @Override
    public ResponseResult delete(String id) {
        sysRoleService.delete(id);
        return ResponseResult.SUCCESS();
    }

    @Override
    public ResponseResult<SysRoleVO> findById(String id) {
        return ResponseResult.SUCCESS(sysRoleService.findById(id));
    }

    @Override
    public ResponseResult<List<SysRoleVO>> getList(SysRoleQueryConditionVO queryConditionVO) {
        return ResponseResult.SUCCESS(sysRoleService.findList(queryConditionVO));
    }

    @Override
    public ResponseResult<Pager<SysRoleVO>> getPageList(SysRoleQueryConditionVO queryConditionVO, int page, int size) {
        return ResponseResult.SUCCESS(sysRoleService.findList(queryConditionVO, page, size));
    }
	
}
