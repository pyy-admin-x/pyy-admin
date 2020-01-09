package com.thtf.base.api;

import com.thtf.base.api.vo.*;
import com.thtf.common.core.response.Pager;
import com.thtf.common.core.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * ---------------------------
 * 菜单 (SysMenuControllerApi)接口
 * ---------------------------
 * 作者：  pyy
 * 时间：  2020-01-07 11:26:48
 * 版本：  v1.0
 * ---------------------------
 */
@Api(tags = "菜单管理模块")
@RequestMapping(value = "/sysMenu", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
public interface SysMenuControllerApi {

    /**
     * 保存菜单
     * @param record
     * @return
     */
    @ApiOperation(value = "保存菜单", notes = "创建新菜单")
    @ApiImplicitParam(name = "record", value = "菜单对象", required = true, dataType = "SysMenuSaveOrUpdateVO", paramType = "body")
    @PostMapping
    ResponseResult<SysMenuVO> save(@Valid @RequestBody SysMenuSaveOrUpdateVO record);

    /**
     * 修改菜单
     * @param id
     * @param record
     * @return
     */
    @ApiOperation(value = "修改菜单", notes = "根据ID修改菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "菜单ID", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "record", value = "菜单对象", required = true, dataType = "SysMenuSaveOrUpdateVO", paramType = "body")
    })
    @PutMapping("/{id}")
    ResponseResult<SysMenuVO> update(@Valid @PathVariable(value = "id") String id, @RequestBody SysMenuSaveOrUpdateVO record);

    /**
     * 删除菜单
     * @param id
     * @return
     */
	@ApiOperation(value = "删除菜单", notes = "根据ID菜单")
    @ApiImplicitParam(name = "id", value = "菜单ID", required = true, dataType = "String", paramType = "path")
    @DeleteMapping("/{id}")
    ResponseResult delete(@Valid @PathVariable(value = "id") String id);

    /**
     * 单个菜单查询
     * @param id
     * @return
     */
    @ApiOperation(value = "菜单查询", notes = "根据ID菜单查询")
    @ApiImplicitParam(name = "id", value = "菜单ID", required = true, dataType = "String", paramType = "path")
    @GetMapping("/{id}")
    ResponseResult<SysMenuVO> findById(@Valid @PathVariable("id") String id);

    /**
     * 菜单树列表查询
     * @param queryConditionVO
     * @return
     */
    @ApiOperation(value = "菜单树列表查询", notes = "菜单树列表查询")
    @GetMapping("/treeList")
    ResponseResult<List<SysMenuTreeVO>> getList(SysMenuQueryConditionVO queryConditionVO);

}
