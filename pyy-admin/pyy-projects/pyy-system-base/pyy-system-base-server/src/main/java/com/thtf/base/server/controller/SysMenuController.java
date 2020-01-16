package com.thtf.base.server.controller;

import com.thtf.base.api.SysMenuControllerApi;
import com.thtf.base.api.vo.*;
import com.thtf.base.server.service.SysMenuService;
import com.thtf.common.auth.token.annonation.RequirePermissions;
import com.thtf.common.core.response.Pager;
import com.thtf.common.core.response.ResponseResult;
import com.thtf.common.log.annotation.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * ---------------------------
 * 菜单 (SysMenuController)         
 * ---------------------------
 * 作者：  pyy
 * 时间：  2020-01-07 11:13:01
 * 版本：  v1.0
 * ---------------------------
 */
@RestController
public class SysMenuController implements SysMenuControllerApi {

	@Autowired
	private SysMenuService sysMenuService;

    @Override
    @Log(desc = "添加菜单")
    @RequirePermissions("system:menu:add")
    public ResponseResult<SysMenuVO> save(SysMenuSaveOrUpdateVO record) {
        return ResponseResult.SUCCESS(sysMenuService.save(record));
    }

    @Override
    @Log(desc = "修改菜单")
    @RequirePermissions("system:menu:update")
    public ResponseResult<SysMenuVO> update(String id, SysMenuSaveOrUpdateVO record) {
        return ResponseResult.SUCCESS(sysMenuService.update(id, record));
    }

    @Override
    @Log(desc = "删除菜单")
    @RequirePermissions("system:menu:del")
    public ResponseResult delete(String id) {
        sysMenuService.delete(id);
        return ResponseResult.SUCCESS();
    }

    @Override
    @RequirePermissions("system:menu:find")
    public ResponseResult<SysMenuVO> findById(String id) {
        return ResponseResult.SUCCESS(sysMenuService.findById(id));
    }

    @Override
    @RequirePermissions("system:menu:find")
    public ResponseResult<List<SysMenuTreeVO>> getList(SysMenuQueryConditionVO queryConditionVO) {
        return ResponseResult.SUCCESS(sysMenuService.findTreeList(queryConditionVO));
    }

    @Override
    @RequirePermissions("system:menu:find")
    public ResponseResult<List<SysMenuRouteVO>> getRouteMenus(HttpServletRequest request) {
        return ResponseResult.SUCCESS(sysMenuService.getRouteMenus(request));
    }

}
