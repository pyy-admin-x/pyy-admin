package com.thtf.base.server.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thtf.base.server.enums.BaseServerCode;
import com.thtf.base.server.mapper.SysRoleMapper;
import com.thtf.base.server.service.SysRoleService;
import com.thtf.common.core.exception.ExceptionCast;
import com.thtf.common.core.response.CommonCode;
import com.thtf.common.core.response.Pager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.thtf.base.api.model.*;
import com.thtf.base.api.vo.*;

import java.util.ArrayList;
import java.util.List;

/**
 * ---------------------------
 * 角色 (SysRoleServiceImpl)         
 * ---------------------------
 * 作者：  pyy
 * 时间：  2020-01-09 14:55:26
 * 版本：  v1.0
 * ---------------------------
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
public class SysRoleServiceImpl implements SysRoleService {

	@Autowired
	private SysRoleMapper sysRoleMapper;

    /**
     * 角色保存
     * @param sysRoleSaveOrUpdateVO
     */
	@Override
	public SysRoleVO save(SysRoleSaveOrUpdateVO sysRoleSaveOrUpdateVO) {
        // 初始化Model对象
        SysRole sysRole = new SysRole();
        // 属性赋值
        BeanUtils.copyProperties(sysRoleSaveOrUpdateVO, sysRole);
        sysRole.setId(null); // 确保ID为null，默认使用mybatis-plus ID生成策略
        // 执行保存
        int row = sysRoleMapper.insert(sysRole);
        if (row != 1) {
            ExceptionCast.cast(BaseServerCode.SAVE_ERROR);
        }
        // 转换为VO对象
        SysRoleVO sysRoleVO = new SysRoleVO();
        BeanUtils.copyProperties(sysRole, sysRoleVO);
        log.info("### 角色保存完毕 ###");
        // 返回保存后结果
        return sysRoleVO;
	}

    /**
     * 角色删除
     * @param id
     */
    @Override
    public void delete(String id) {
        // 参数校验
        if (StringUtils.isBlank(id)) {
           ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        // 根据ID查询角色
        SysRole sysRole = sysRoleMapper.selectById(id);
        if (sysRole == null) {
            ExceptionCast.cast(BaseServerCode.RESULT_DATA_NONE);
        }
        // 执行删除
        int row = sysRoleMapper.deleteById(id);
        if (row != 1) {
           ExceptionCast.cast(BaseServerCode.DELETE_ERROR);
        }
        log.info("### 角色删除完毕 ###");
    }

    @Override
    public void deleteBatch(List<String> ids) {
        // 参数校验
        if (CollUtil.isEmpty(ids)) {
            ExceptionCast.cast(BaseServerCode.DEL_IDS_ISEMPTY);
        }
        int rows = sysRoleMapper.deleteBatchIds(ids);
        if (rows < 1) {
            ExceptionCast.cast(BaseServerCode.DELETE_ERROR);
        }
    }

    /**
     * 角色修改
     * @param id
     * @param sysRoleSaveOrUpdateVO
     */
    @Override
    public SysRoleVO update(String id, SysRoleSaveOrUpdateVO sysRoleSaveOrUpdateVO) {
        // 参数校验
        if (StringUtils.isBlank(id) || sysRoleSaveOrUpdateVO == null) {
           ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        // 根据ID查询角色
        SysRole sysRole = sysRoleMapper.selectById(id);
        if (sysRole == null) {
            ExceptionCast.cast(BaseServerCode.RESULT_DATA_NONE);
        }

        // 属性赋值
        BeanUtils.copyProperties(sysRoleSaveOrUpdateVO, sysRole);
        sysRole.setId(id);
        // 执行修改
        int row = sysRoleMapper.updateById(sysRole);
        if (row != 1) {
           ExceptionCast.cast(BaseServerCode.DELETE_ERROR);
        }
        // 转换为VO对象
        SysRoleVO sysRoleVO = new SysRoleVO();
        BeanUtils.copyProperties(sysRole, sysRoleVO);
         log.info("### 角色修改完毕 ###");
        // 返回保存后结果
        return sysRoleVO;
    }

   /**
    * 根据角色ID查询
    * @param id
    */
	@Override
	public SysRoleVO findById(String id) {
	    // 参数校验
        if (StringUtils.isBlank(id)) {
           ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        // 根据ID查询角色
		SysRole sysRole = sysRoleMapper.selectById(id);
		if (sysRole == null) {
            return null;
        }
        // 转换为VO对象
        SysRoleVO sysRoleVO = new SysRoleVO();
        BeanUtils.copyProperties(sysRole, sysRoleVO);
        log.info("### 角色查询完毕 ###");
        // 返回保存后结果
        return sysRoleVO;
	}

   /**
     * 角色模糊查询
     * @param queryConditionVO
     * @return
     */
    @Override
    public List<SysRoleVO> findList(SysRoleQueryConditionVO queryConditionVO) {
        // 参数校验
        if (queryConditionVO == null) {
          queryConditionVO = new SysRoleQueryConditionVO();
        }
        // 查询条件
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(queryConditionVO.getName()), SysRole::getName, queryConditionVO.getName());
        // 执行查询
        List<SysRole> sysRoleList = sysRoleMapper.selectList(queryWrapper);
        log.info("### 角色Model模糊查询完毕，总条数：{}条###", sysRoleList.size());
        // 角色转换VO数据
        List<SysRoleVO> sysRoleVOList = new ArrayList<>();
        sysRoleList.forEach(sysRole -> {
            SysRoleVO sysRoleVO = new SysRoleVO();
            BeanUtils.copyProperties(sysRole, sysRoleVO);
            sysRoleVOList.add(sysRoleVO);
        });
        log.info("### 角色转换VO数据完毕###");
        return sysRoleVOList;
    }

    /**
     * 角色分页模糊查询
     * @param queryConditionVO
     * @param page
     * @param size
     * @return
     */
	@Override
    public Pager<SysRoleVO> findList(SysRoleQueryConditionVO queryConditionVO, int page, int size) {
        // 参数校验
        if (queryConditionVO == null) {
          queryConditionVO = new SysRoleQueryConditionVO();
        }
        // 查询条件
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(queryConditionVO.getName()), SysRole::getName, queryConditionVO.getName());
        // 分页条件
        Page<SysRole> pageInfo = new Page(page, size);
        // 执行查询
        IPage<SysRole> sysRolePage = sysRoleMapper.selectPage(pageInfo, queryWrapper);
        long total = sysRolePage.getTotal();
        List<SysRole> sysRoleList = sysRolePage.getRecords();
        // 角色转换VO数据
        List<SysRoleVO> sysRoleVOList = new ArrayList<>();
        sysRoleList.forEach(sysRole -> {
            SysRoleVO sysRoleVO = new SysRoleVO();
            BeanUtils.copyProperties(sysRole, sysRoleVO);
            sysRoleVOList.add(sysRoleVO);
        });
        log.info("### 角色转换VO数据完毕###");
        // 分装分页查询结果
        return new Pager(total, sysRoleVOList);
    }
}
