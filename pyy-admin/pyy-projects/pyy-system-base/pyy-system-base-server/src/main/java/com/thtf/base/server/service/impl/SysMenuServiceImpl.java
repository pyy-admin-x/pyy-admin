package com.thtf.base.server.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thtf.base.api.model.SysDept;
import com.thtf.base.api.model.SysMenu;
import com.thtf.base.api.vo.*;
import com.thtf.base.server.enums.BaseServerCode;
import com.thtf.base.server.mapper.SysMenuMapper;
import com.thtf.base.server.service.SysMenuService;
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

import java.util.ArrayList;
import java.util.List;

/**
 * ---------------------------
 * 菜单 (SysMenuServiceImpl)         
 * ---------------------------
 * 作者：  pyy
 * 时间：  2020-01-07 11:13:01
 * 版本：  v1.0
 * ---------------------------
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
public class SysMenuServiceImpl implements SysMenuService {

	@Autowired
	private SysMenuMapper sysMenuMapper;

    /**
     * 菜单保存
     * @param sysMenuSaveOrUpdateVO
     */
	@Override
	public SysMenuVO save(SysMenuSaveOrUpdateVO sysMenuSaveOrUpdateVO) {
        // 初始化Model对象
        SysMenu sysMenu = new SysMenu();
        // 属性赋值
        BeanUtils.copyProperties(sysMenuSaveOrUpdateVO, sysMenu);
        sysMenu.setId(null); // 确保ID为null，默认使用mybatis-plus ID生成策略
        // 执行保存
        int row = sysMenuMapper.insert(sysMenu);
        if (row != 1) {
            ExceptionCast.cast(BaseServerCode.SAVE_ERROR);
        }
        // 转换为VO对象
        SysMenuVO sysMenuVO = new SysMenuVO();
        BeanUtils.copyProperties(sysMenu, sysMenuVO);
        log.info("### 菜单保存完毕 ###");
        // 返回保存后结果
        return sysMenuVO;
	}

    /**
     * 菜单删除
     * @param id
     */
    @Override
    public void delete(String id) {
        // 参数校验
        if (StringUtils.isBlank(id)) {
           ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        // 根据ID查询菜单
        SysMenu sysMenu = sysMenuMapper.selectById(id);
        if (sysMenu == null) {
            ExceptionCast.cast(BaseServerCode.RESULT_DATA_NONE);
        }
        // 执行删除
        int row = sysMenuMapper.deleteById(id);
        if (row != 1) {
           ExceptionCast.cast(BaseServerCode.DELETE_ERROR);
        }
        log.info("### 菜单删除完毕 ###");
    }

	/**
     * 菜单修改
     * @param id
     * @param sysMenuSaveOrUpdateVO
     */
    @Override
    public SysMenuVO update(String id, SysMenuSaveOrUpdateVO sysMenuSaveOrUpdateVO) {
        // 参数校验
        if (StringUtils.isBlank(id) || sysMenuSaveOrUpdateVO == null) {
           ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        // 根据ID查询菜单
        SysMenu sysMenu = sysMenuMapper.selectById(id);
        if (sysMenu == null) {
            ExceptionCast.cast(BaseServerCode.RESULT_DATA_NONE);
        }

        // 属性赋值
        BeanUtils.copyProperties(sysMenuSaveOrUpdateVO, sysMenu);
        sysMenu.setId(id);
        // 执行修改
        int row = sysMenuMapper.updateById(sysMenu);
        if (row != 1) {
           ExceptionCast.cast(BaseServerCode.DELETE_ERROR);
        }
        // 转换为VO对象
        SysMenuVO sysMenuVO = new SysMenuVO();
        BeanUtils.copyProperties(sysMenu, sysMenuVO);
         log.info("### 菜单修改完毕 ###");
        // 返回保存后结果
        return sysMenuVO;
    }

   /**
    * 根据菜单ID查询
    * @param id
    */
	@Override
	public SysMenuVO findById(String id) {
	    // 参数校验
        if (StringUtils.isBlank(id)) {
           ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        // 根据ID查询菜单
		SysMenu sysMenu = sysMenuMapper.selectById(id);
		if (sysMenu == null) {
            return null;
        }
        // 转换为VO对象
        SysMenuVO sysMenuVO = new SysMenuVO();
        BeanUtils.copyProperties(sysMenu, sysMenuVO);
        log.info("### 菜单查询完毕 ###");
        // 返回保存后结果
        return sysMenuVO;
	}

   /**
     * 菜单模糊查询
     * @param queryConditionVO
     * @return
     */
    @Override
    public List<SysMenuVO> findList(SysMenuQueryConditionVO queryConditionVO) {
        // 参数校验
        if (queryConditionVO == null) {
          queryConditionVO = new SysMenuQueryConditionVO();
        }
        // 查询条件
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(queryConditionVO.getName()), SysMenu::getName, queryConditionVO.getName());
        // 执行查询
        List<SysMenu> sysMenuList = sysMenuMapper.selectList(queryWrapper);
        log.info("### 菜单Model模糊查询完毕，总条数：{}条###", sysMenuList.size());
        // 菜单转换VO数据
        List<SysMenuVO> sysMenuVOList = new ArrayList<>();
        sysMenuList.forEach(sysMenu -> {
            SysMenuVO sysMenuVO = new SysMenuVO();
            BeanUtils.copyProperties(sysMenu, sysMenuVO);
            sysMenuVOList.add(sysMenuVO);
        });
        log.info("### 菜单转换VO数据完毕###");
        return sysMenuVOList;
    }

    /**
     * 菜单分页模糊查询
     * @param queryConditionVO
     * @param page
     * @param size
     * @return
     */
	@Override
    public Pager<SysMenuVO> findList(SysMenuQueryConditionVO queryConditionVO, int page, int size) {
        // 参数校验
        if (queryConditionVO == null) {
          queryConditionVO = new SysMenuQueryConditionVO();
        }
        // 查询条件
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(queryConditionVO.getName()), SysMenu::getName, queryConditionVO.getName());
        // 分页条件
        Page<SysMenu> pageInfo = new Page(page, size);
        // 执行查询
        IPage<SysMenu> sysMenuPage = sysMenuMapper.selectPage(pageInfo, queryWrapper);
        long total = sysMenuPage.getTotal();
        List<SysMenu> sysMenuList = sysMenuPage.getRecords();
        // 菜单转换VO数据
        List<SysMenuVO> sysMenuVOList = new ArrayList<>();
        sysMenuList.forEach(sysMenu -> {
            SysMenuVO sysMenuVO = new SysMenuVO();
            BeanUtils.copyProperties(sysMenu, sysMenuVO);
            sysMenuVOList.add(sysMenuVO);
        });
        log.info("### 菜单转换VO数据完毕###");
        // 分装分页查询结果
        return new Pager(total, sysMenuVOList);
    }

    /**
     * 菜单树列表查询
     * @param queryConditionVO
     * @return
     */
    @Override
    public List<SysMenuTreeVO> findTreeList(SysMenuQueryConditionVO queryConditionVO) {
        List<SysMenuTreeVO> menuTreeVOList = new ArrayList<>();
        // 查询所有顶级节点
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.isNull(SysMenu::getParentId)
                .orderByAsc(SysMenu::getSort);
        List<SysMenu> rootMenuList = sysMenuMapper.selectList(queryWrapper);
            log.info("### 顶级菜单树列表查询完毕 ###");
        rootMenuList.forEach(rootDept -> {
            // SysMenuTreeVO
            SysMenuTreeVO treeNodeVO = new SysMenuTreeVO();
            BeanUtils.copyProperties(rootDept, treeNodeVO);
            treeNodeVO.setLabel(rootDept.getName());
            treeNodeVO.setChildren(findChildrenByParentId(rootDept.getId()));

            menuTreeVOList.add(treeNodeVO);
        });
        return menuTreeVOList;
    }
    // 递归查询子节点
    private List<SysMenuTreeVO> findChildrenByParentId(String parentId) {
        if (StringUtils.isBlank(parentId)) {
            return null;
        }
        // 查询所有子部门
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysMenu::getParentId, parentId)
                .orderByAsc(SysMenu::getSort);
        List<SysMenu> sysMenuList = sysMenuMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(sysMenuList)) {
            return null;
        }
        List<SysMenuTreeVO> children = new ArrayList<>();
        sysMenuList.forEach(dept -> {
            // 转换SysMenuTreeVO
            SysMenuTreeVO treeNodeVO = new SysMenuTreeVO();
            BeanUtils.copyProperties(dept, treeNodeVO);
            treeNodeVO.setLabel(dept.getName());
            treeNodeVO.setChildren(findChildrenByParentId(dept.getId()));
            children.add(treeNodeVO);
        });
        return children;
    }
}
