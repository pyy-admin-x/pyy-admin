package com.thtf.base.server.service;

import com.thtf.base.api.vo.SysUserQueryConditionVO;
import com.thtf.base.api.vo.SysUserSaveOrUpdateVO;
import com.thtf.base.api.vo.SysUserVO;
import com.thtf.base.api.vo.ValidateImgVO;
import com.thtf.common.core.response.Pager;

import java.util.List;

/**
 * ---------------------------
 * 用户 (SysUserService)         
 * ---------------------------
 * 作者：  pyy
 * 时间：  2020-01-10 16:06:25
 * 版本：  v1.0
 * ---------------------------
 */
public interface SysUserService {

    /**
     * 用户保存
     * @param sysUserSaveOrUpdateVO
     */
    SysUserVO save(SysUserSaveOrUpdateVO sysUserSaveOrUpdateVO);

	/**
     * 用户修改
     * @param id
     * @param sysUserSaveOrUpdateVO
     */
    SysUserVO update(String id, SysUserSaveOrUpdateVO sysUserSaveOrUpdateVO);

    /**
     * 用户删除
     * @param id
     */
	void delete(String id);

    /**
     * 批量删除用户
     * @param ids
     */
    void deleteBatch(List<String> ids);

    /**
     * 根据用户ID查询
     * @param id
     */
	SysUserVO findById(String id);

    /**
     * 用户模糊查询
     * @param queryConditionVO
     * @return
     */
    List<SysUserVO> findList(SysUserQueryConditionVO queryConditionVO);

    /**
     * 用户分页模糊查询
     * @param queryConditionVO
     * @param page
     * @param size
     * @return
     */
    Pager<SysUserVO> findList(SysUserQueryConditionVO queryConditionVO, int page, int size);

    /**
     * 获取图片验证码
     * @param type
     * @return
     */
    ValidateImgVO getImgCode(String type);
}
