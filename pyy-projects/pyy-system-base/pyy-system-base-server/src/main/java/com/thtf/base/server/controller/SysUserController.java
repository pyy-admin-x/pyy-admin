package com.thtf.base.server.controller;

import com.thtf.base.api.SysUserControllerApi;
import com.thtf.base.api.vo.SysUserQueryConditionVO;
import com.thtf.base.api.vo.SysUserSaveOrUpdateVO;
import com.thtf.base.api.vo.SysUserVO;
import com.thtf.base.server.service.SysUserService;
import com.thtf.common.auth.token.annonation.RequirePermissions;
import com.thtf.common.core.response.Pager;
import com.thtf.common.core.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ---------------------------
 * 用户 (SysUserController)         
 * ---------------------------
 * 作者：  pyy
 * 时间：  2020-01-10 16:06:25
 * 版本：  v1.0
 * ---------------------------
 */
@RestController
public class SysUserController implements SysUserControllerApi {

	@Autowired
	private SysUserService sysUserService;

    @Override
    @RequirePermissions("system:user:add")
    public ResponseResult<SysUserVO> save(SysUserSaveOrUpdateVO record) {
        return ResponseResult.SUCCESS(sysUserService.save(record));
    }

    @Override
    @RequirePermissions("system:user:update")
    public ResponseResult<SysUserVO> update(String id, SysUserSaveOrUpdateVO record) {
        return ResponseResult.SUCCESS(sysUserService.update(id, record));
    }

    @Override
    @RequirePermissions("system:user:del")
    public ResponseResult delete(String id) {
        sysUserService.delete(id);
        return ResponseResult.SUCCESS();
    }

    @Override
    @RequirePermissions("system:user:delBatch")
    public ResponseResult deleteBatch(List<String> ids) {
        sysUserService.deleteBatch(ids);
        return ResponseResult.SUCCESS();
    }

    @Override
    @RequirePermissions("system:user:find")
    public ResponseResult<SysUserVO> findById(String id) {
        return ResponseResult.SUCCESS(sysUserService.findById(id));
    }

    @Override
    @RequirePermissions("system:user:find")
    public ResponseResult<List<SysUserVO>> getList(SysUserQueryConditionVO queryConditionVO) {
        return ResponseResult.SUCCESS(sysUserService.findList(queryConditionVO));
    }

    @Override
    @RequirePermissions("system:user:find")
    public ResponseResult<Pager<SysUserVO>> getPageList(SysUserQueryConditionVO queryConditionVO, int page, int size) {
        return ResponseResult.SUCCESS(sysUserService.findList(queryConditionVO, page, size));
    }
	
}
