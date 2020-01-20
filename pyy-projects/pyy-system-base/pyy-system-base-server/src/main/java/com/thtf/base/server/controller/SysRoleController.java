package com.thtf.base.server.controller;

import com.thtf.base.api.SysRoleControllerApi;
import com.thtf.base.api.vo.*;
import com.thtf.base.server.service.SysRoleService;
import com.thtf.common.auth.token.annonation.RequirePermissions;
import com.thtf.common.core.response.Pager;
import com.thtf.common.core.response.ResponseResult;
import com.thtf.common.log.annotation.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    @Log(desc = "添加角色")
    public ResponseResult<SysRoleVO> save(SysRoleSaveOrUpdateVO record) {
        return ResponseResult.SUCCESS(sysRoleService.save(record));
    }

    @Override
    @Log(desc = "修改角色")
    @RequirePermissions("system:role:update")
    public ResponseResult<SysRoleVO> update(String id, SysRoleSaveOrUpdateVO record) {
        return ResponseResult.SUCCESS(sysRoleService.update(id, record));
    }

    @Override
    @Log(desc = "删除角色")
    @RequirePermissions("system:role:del")
    public ResponseResult delete(String id) {
        sysRoleService.delete(id);
        return ResponseResult.SUCCESS();
    }

    @Override
    @Log(desc = "批量删除角色")
    @RequirePermissions("system:role:delBatch")
    public ResponseResult deleteBatch(List<String> ids) {
        sysRoleService.deleteBatch(ids);
        return ResponseResult.SUCCESS();
    }

    @Override
    @RequirePermissions("system:role:find")
    public ResponseResult<SysRoleVO> findById(String id) {
        return ResponseResult.SUCCESS(sysRoleService.findById(id));
    }

    @Override
    @RequirePermissions("system:role:find")
    public ResponseResult<List<SysRoleVO>> getList(SysRoleQueryConditionVO queryConditionVO) {
        return ResponseResult.SUCCESS(sysRoleService.findList(queryConditionVO));
    }

    @Override
    @RequirePermissions("system:role:find")
    public ResponseResult<Pager<SysRoleVO>> getPageList(SysRoleQueryConditionVO queryConditionVO, int page, int size) {
        return ResponseResult.SUCCESS(sysRoleService.findList(queryConditionVO, page, size));
    }

    @Override
    @RequirePermissions("system:role:setPermission")
    public ResponseResult<SysRoleVO> setPermissions(String id, List<String> menuIds) {
        sysRoleService.setPermissions(id, menuIds);
        return ResponseResult.SUCCESS();
    }

}
