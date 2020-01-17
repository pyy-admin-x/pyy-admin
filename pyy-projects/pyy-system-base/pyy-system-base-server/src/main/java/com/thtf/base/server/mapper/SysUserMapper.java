package com.thtf.base.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thtf.base.api.model.SysUser;
import com.thtf.base.api.vo.SysJobQueryConditionVO;
import com.thtf.base.api.vo.SysJobVO;
import com.thtf.base.api.vo.SysUserQueryConditionVO;
import com.thtf.base.api.vo.SysUserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ---------------------------
 * 用户 (SysUserMapper)         
 * ---------------------------
 * 作者：  pyy
 * 时间：  2020-01-10 16:06:25
 * 版本：  v1.0
 * ---------------------------
 */
public interface SysUserMapper extends BaseMapper<SysUser>{

    SysUserVO selectUserById(String id);

    List<SysUserVO> selectUserList(@Param("queryConditionVO") SysUserQueryConditionVO queryConditionVO);

    List<SysUserVO> selectUserList(Page<SysUserVO> page, @Param("queryConditionVO") SysUserQueryConditionVO queryConditionVO);
}