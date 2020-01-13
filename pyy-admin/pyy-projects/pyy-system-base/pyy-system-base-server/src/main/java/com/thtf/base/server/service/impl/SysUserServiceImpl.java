package com.thtf.base.server.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thtf.base.api.model.SysUser;
import com.thtf.base.api.vo.SysUserQueryConditionVO;
import com.thtf.base.api.vo.SysUserSaveOrUpdateVO;
import com.thtf.base.api.vo.SysUserVO;
import com.thtf.base.api.vo.ValidateImgVO;
import com.thtf.base.server.enums.BaseServerCode;
import com.thtf.base.server.mapper.SysUserMapper;
import com.thtf.base.server.service.SysUserService;
import com.thtf.common.core.constant.ImgCodeType;
import com.thtf.common.core.exception.ExceptionCast;
import com.thtf.common.core.response.CommonCode;
import com.thtf.common.core.response.Pager;
import com.thtf.common.core.utils.ImgCodeUtil;
import com.thtf.common.core.utils.SnowflakeId;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * ---------------------------
 * 用户 (SysUserServiceImpl)         
 * ---------------------------
 * 作者：  pyy
 * 时间：  2020-01-10 16:06:25
 * 版本：  v1.0
 * ---------------------------
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
public class SysUserServiceImpl implements SysUserService {

    /** 验证码前缀 */
    private static final String CODE_KEY = "img_code_";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private SysUserMapper sysUserMapper;

    /**
     * 用户保存
     * @param sysUserSaveOrUpdateVO
     */
	@Override
	public SysUserVO save(SysUserSaveOrUpdateVO sysUserSaveOrUpdateVO) {
        // 初始化Model对象
        SysUser sysUser = new SysUser();
        // 属性赋值
        BeanUtils.copyProperties(sysUserSaveOrUpdateVO, sysUser);
        sysUser.setId(null); // 确保ID为null，默认使用mybatis-plus ID生成策略
        // 执行保存
        int row = sysUserMapper.insert(sysUser);
        if (row != 1) {
            ExceptionCast.cast(BaseServerCode.SAVE_ERROR);
        }
        // 转换为VO对象
        SysUserVO sysUserVO = new SysUserVO();
        BeanUtils.copyProperties(sysUser, sysUserVO);
        log.info("### 用户保存完毕 ###");
        // 返回保存后结果
        return sysUserVO;
	}

    /**
     * 用户删除
     * @param id
     */
    @Override
    public void delete(String id) {
        // 参数校验
        if (StringUtils.isBlank(id)) {
           ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        // 根据ID查询用户
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            ExceptionCast.cast(BaseServerCode.RESULT_DATA_NONE);
        }
        // 执行删除
        int row = sysUserMapper.deleteById(id);
        if (row != 1) {
           ExceptionCast.cast(BaseServerCode.DELETE_ERROR);
        }
        log.info("### 用户删除完毕 ###");
    }

    @Override
    public void deleteBatch(List<String> ids) {
        // 参数校验
        if (CollUtil.isEmpty(ids)) {
            ExceptionCast.cast(BaseServerCode.DEL_IDS_ISEMPTY);
        }
        int rows = sysUserMapper.deleteBatchIds(ids);
        if (rows < 1) {
            ExceptionCast.cast(BaseServerCode.DELETE_ERROR);
        }
    }

	/**
     * 用户修改
     * @param id
     * @param sysUserSaveOrUpdateVO
     */
    @Override
    public SysUserVO update(String id, SysUserSaveOrUpdateVO sysUserSaveOrUpdateVO) {
        // 参数校验
        if (StringUtils.isBlank(id) || sysUserSaveOrUpdateVO == null) {
           ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        // 根据ID查询用户
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            ExceptionCast.cast(BaseServerCode.RESULT_DATA_NONE);
        }

        // 属性赋值
        BeanUtils.copyProperties(sysUserSaveOrUpdateVO, sysUser);
        sysUser.setId(id);
        // 执行修改
        int row = sysUserMapper.updateById(sysUser);
        if (row != 1) {
           ExceptionCast.cast(BaseServerCode.DELETE_ERROR);
        }
        // 转换为VO对象
        SysUserVO sysUserVO = new SysUserVO();
        BeanUtils.copyProperties(sysUser, sysUserVO);
         log.info("### 用户修改完毕 ###");
        // 返回保存后结果
        return sysUserVO;
    }

   /**
    * 根据用户ID查询
    * @param id
    */
	@Override
	public SysUserVO findById(String id) {
	    // 参数校验
        if (StringUtils.isBlank(id)) {
           ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        // 根据ID查询用户
		SysUser sysUser = sysUserMapper.selectById(id);
		if (sysUser == null) {
            return null;
        }
        // 转换为VO对象
        SysUserVO sysUserVO = new SysUserVO();
        BeanUtils.copyProperties(sysUser, sysUserVO);
        log.info("### 用户查询完毕 ###");
        // 返回保存后结果
        return sysUserVO;
	}

   /**
     * 用户模糊查询
     * @param queryConditionVO
     * @return
     */
    @Override
    public List<SysUserVO> findList(SysUserQueryConditionVO queryConditionVO) {
        // 参数校验
        if (queryConditionVO == null) {
          queryConditionVO = new SysUserQueryConditionVO();
        }
        // 查询条件
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(queryConditionVO.getName()), SysUser::getName, queryConditionVO.getName());
        // 执行查询
        List<SysUser> sysUserList = sysUserMapper.selectList(queryWrapper);
        log.info("### 用户Model模糊查询完毕，总条数：{}条###", sysUserList.size());
        // 用户转换VO数据
        List<SysUserVO> sysUserVOList = new ArrayList<>();
        sysUserList.forEach(sysUser -> {
            SysUserVO sysUserVO = new SysUserVO();
            BeanUtils.copyProperties(sysUser, sysUserVO);
            sysUserVOList.add(sysUserVO);
        });
        log.info("### 用户转换VO数据完毕###");
        return sysUserVOList;
    }

    /**
     * 用户分页模糊查询
     * @param queryConditionVO
     * @param page
     * @param size
     * @return
     */
	@Override
    public Pager<SysUserVO> findList(SysUserQueryConditionVO queryConditionVO, int page, int size) {
        // 参数校验
        if (queryConditionVO == null) {
          queryConditionVO = new SysUserQueryConditionVO();
        }
        // 查询条件
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(queryConditionVO.getName()), SysUser::getName, queryConditionVO.getName());
        // 分页条件
        Page<SysUser> pageInfo = new Page(page, size);
        // 执行查询
        IPage<SysUser> sysUserPage = sysUserMapper.selectPage(pageInfo, queryWrapper);
        long total = sysUserPage.getTotal();
        List<SysUser> sysUserList = sysUserPage.getRecords();
        // 用户转换VO数据
        List<SysUserVO> sysUserVOList = new ArrayList<>();
        sysUserList.forEach(sysUser -> {
            SysUserVO sysUserVO = new SysUserVO();
            BeanUtils.copyProperties(sysUser, sysUserVO);
            sysUserVOList.add(sysUserVO);
        });
        log.info("### 用户转换VO数据完毕###");
        // 分装分页查询结果
        return new Pager(total, sysUserVOList);
    }

    /**
     * 获取图片验证码
     * @param type
     * @return
     */
    @Override
    public ValidateImgVO getImgCode(String type) {
        // 获取图片验证码
        Captcha imgCode = ImgCodeUtil.getImgCode(type, 111, 36);
        // 获取运算的结果：5
        String result = imgCode.text();
        String uuid = CODE_KEY + SnowflakeId.getId();
        ValidateImgVO validateImgVO = new ValidateImgVO(imgCode.toBase64(), uuid);
        // 保存验证码到redis
        stringRedisTemplate.opsForValue().set(uuid, result);
        return validateImgVO;
    }
}
