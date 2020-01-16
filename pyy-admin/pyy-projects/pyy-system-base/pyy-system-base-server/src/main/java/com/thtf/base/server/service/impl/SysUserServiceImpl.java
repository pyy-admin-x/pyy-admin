package com.thtf.base.server.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thtf.base.api.model.SysUser;
import com.thtf.base.api.model.SysUserRole;
import com.thtf.base.api.vo.*;
import com.thtf.base.server.constants.BaseServerCode;
import com.thtf.base.server.mapper.*;
import com.thtf.base.server.service.SysUserService;
import com.thtf.common.core.constant.CommonConstant;
import com.thtf.common.core.exception.ExceptionCast;
import com.thtf.common.core.properties.JwtProperties;
import com.thtf.common.core.response.CommonCode;
import com.thtf.common.core.response.Pager;
import com.thtf.common.core.utils.*;
import com.wf.captcha.base.Captcha;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    private JwtProperties jwtProperties;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private SysUserMapper sysUserMapper;

	@Autowired
    private SysDeptMapper sysDeptMapper;

	@Autowired
    private SysJobMapper sysJobMapper;

	@Autowired
    private SysMenuMapper sysMenuMapper;

	@Autowired
    private SysUserRoleMapper sysUserRoleMapper;

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
        // 密码加密
        String encoderPassword = SpringSecurityUtil.encoderPassword(sysUser.getPassword());
        sysUser.setPassword(encoderPassword);
        // 执行保存
        int row = sysUserMapper.insert(sysUser);
        if (row != 1) {
            ExceptionCast.cast(BaseServerCode.SAVE_ERROR);
        }
        // 保存用户关联角色
        List<String> roleIds = sysUserSaveOrUpdateVO.getRoleIds();
        if (CollUtil.isNotEmpty(roleIds)) {
            roleIds.forEach(roleId -> {
                SysUserRole userRole = new SysUserRole();
                userRole.setRoleId(roleId);
                userRole.setUserId(sysUser.getId());
                sysUserRoleMapper.insert(userRole);
            });
            log.info("### 用户角色关联关系保存成功 ###");
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
        // 删除用户角色关联关系
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId, id);
        sysUserRoleMapper.delete(queryWrapper);
        log.info("### 用户角色关联关系删除完毕 ###");

        // 删除用户
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
        ids.forEach(userId -> {
            delete(userId);
        });
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
        sysUser.setName(sysUserSaveOrUpdateVO.getName());
        sysUser.setUsername(sysUserSaveOrUpdateVO.getUsername());
        sysUser.setDeptId(sysUserSaveOrUpdateVO.getDeptId());
        sysUser.setJobId(sysUserSaveOrUpdateVO.getJobId());
        sysUser.setEmail(sysUserSaveOrUpdateVO.getEmail());
        sysUser.setPhone(sysUserSaveOrUpdateVO.getPhone());
        sysUser.setAvatar(sysUserSaveOrUpdateVO.getAvatar());
        sysUser.setStatus(sysUserSaveOrUpdateVO.getStatus());
        // 执行修改
        int row = sysUserMapper.updateById(sysUser);
        if (row != 1) {
           ExceptionCast.cast(BaseServerCode.DELETE_ERROR);
        }

        // 删除用户角色关联关系
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId, id);
        sysUserRoleMapper.delete(queryWrapper);
        log.info("### 用户角色关联关系删除完毕 ###");

        List<String> roleIds = sysUserSaveOrUpdateVO.getRoleIds();
        if (CollUtil.isNotEmpty(roleIds)) {
            // 添加新的用户角色关联关系
            roleIds.forEach(roleId -> {
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(id);
                userRole.setRoleId(roleId);
                sysUserRoleMapper.insert(userRole);
            });
            log.info("### 用户角色关联关系更新完毕 ###");
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
		SysUserVO sysUserVO = sysUserMapper.selectUserById(id);
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
        // 执行查询
        List<SysUserVO> sysUserVOList = sysUserMapper.selectUserList(queryConditionVO);
        log.info("### 用户模糊查询完毕，总条数：{}条###", sysUserVOList.size());
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
        // 分页条件
        Page<SysUserVO> pageInfo = new Page(page, size);
        // 执行查询
        List<SysUserVO> sysUserVOList = sysUserMapper.selectUserList(pageInfo, queryConditionVO);
        pageInfo.setRecords(sysUserVOList);
        long total = pageInfo.getTotal();
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
        Captcha imgCode = ImgCodeUtil.getImgCode(type, 150, 46);
        // 获取运算的结果：5
        String result = imgCode.text();
        log.info("### 验证码：{} ###", result);
        String uuid = CODE_KEY + SnowflakeId.getId();
        ValidateImgVO validateImgVO = new ValidateImgVO(imgCode.toBase64(), uuid);
        // 保存验证码到redis, 默认10分钟
        stringRedisTemplate.opsForValue().set(uuid, result, 10, TimeUnit.MINUTES);
        return validateImgVO;
    }

    /**
     * 用户登录
     * @param loginUserVO
     * @return
     */
    @Override
    public String login(LoginUserVO loginUserVO) {
        if (loginUserVO == null) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        // 查询验证码
        String code = stringRedisTemplate.opsForValue().get(loginUserVO.getUuid());
        // 清除验证码
        stringRedisTemplate.delete(loginUserVO.getUuid());
        if (StringUtils.isBlank(code)) {
            log.debug("### 验证码已过期 ###");
            ExceptionCast.cast(CommonCode.VALIDATE_CODE_EXPIRED);
        }
        if (StringUtils.isBlank(loginUserVO.getCode()) || !loginUserVO.getCode().equals(code)) {
            log.debug("### 验证码错误 ###");
            ExceptionCast.cast(CommonCode.VALIDATE_CODE_INVALID);
        }
        // 根据用户查询用户
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, loginUserVO.getUsername())
                .eq(SysUser::getDeletedFlag, CommonConstant.UN_DELETED_FLAG);
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
        if (sysUser == null || !SpringSecurityUtil.checkpassword(loginUserVO.getPassword(), sysUser.getPassword())) {
            log.info("### 登录失败：用户不存在或密码错误 ###");
            ExceptionCast.cast(CommonCode.USERNAME_OR_PASSWORD_ERROR);
        }
        // 判断用户状态
        if (StrUtil.equals(CommonConstant.USER_STATUS_DISABLED, sysUser.getStatus())){
            log.info("### 账号已停用，请联系管理员 ###");
            ExceptionCast.cast(CommonCode.USER_ACCOUNT_FORBIDDEN);
        }
        // 登录成功：生成令牌
        else {
            // 获取当前用户的角色和权限信息
            ProfileVO profileVO = this.getProfileVO(sysUser.getId());
            Map<String, Object> roles = profileVO.getRoles();

            Map<String, Object> extAttribute = new HashMap<>();
            extAttribute.put(jwtProperties.getRolePremissionKey(), roles);
            String token = JwtUtil.createToken(sysUser.getId(), sysUser.getUsername(), extAttribute);
            log.info("### 用户登录成功 ###");
            // 保存token到redis 超时时间设置比本地jwt多10分钟
            long timtOut = jwtProperties.getExpiresSecond() + 600;
            stringRedisTemplate.opsForValue().set(token, loginUserVO.getUsername(), timtOut, TimeUnit.SECONDS);
            log.info("### redis token 保存成功 ###");
            // 返回token
            return token;
        }
        return null;
    }

    /**
     * 用户退出
     * @param request
     */
    @Override
    public void logout(HttpServletRequest request) {
        // 清除redis 等业务
        String token = request.getHeader(jwtProperties.getTokenKey());
        if (StrUtil.isNotBlank(token)) {
            // 前后端约定头信息内容以 Bearer+空格+token 形式组成
            stringRedisTemplate.delete(token.replace("Bearer ", ""));
            log.info("### redis token 删除成功 ###");
        }
    }

    /**
     * 获取当前登录用户配置信息
     * @return
     */
    @Override
    public ProfileVO getProfileInfo(HttpServletRequest request) {
        String token = request.getHeader(jwtProperties.getTokenKey());
        if (StrUtil.isBlank(token)) {
            ExceptionCast.cast(CommonCode.UNAUTHENTICATED);
        }
        Claims claims = JwtUtil.parseToken(token, jwtProperties.getBase64Secret());
        String userId = JwtUtil.getUserId(claims);
        log.info("### userId={} ###", userId);
        ProfileVO profileVO = getProfileVO(userId);
        return profileVO;
    }

    // 根据id获取用户配置信息
    private ProfileVO getProfileVO(String userId) {
        SysUserVO sysUserVO = this.findById(userId);
        ProfileVO profileVO = new ProfileVO();
        BeanUtils.copyProperties(sysUserVO, profileVO);

        Map<String, Object> roles = new HashMap<>();
        // 取出当前用户拥有所有角色信息
        List<SysRoleVO> roleList = sysUserVO.getRoleList();
        // 根据角色ids查询关联权限信息
        if (CollUtil.isNotEmpty(roleList)) {
            List<String> roleIds = roleList.stream().map(SysRoleVO::getId).collect(Collectors.toList());
            List<SysMenuVO> menuVOList = sysMenuMapper.selectMenuListByRoleIds(roleIds);
            List<String> permissionList = menuVOList.stream().map(SysMenuVO::getPermission).collect(Collectors.toList());
            roles.put("permissionList", permissionList);
            roles.put("roleList", roleList);
        }
        profileVO.setRoles(roles);
        return profileVO;
    }
}
