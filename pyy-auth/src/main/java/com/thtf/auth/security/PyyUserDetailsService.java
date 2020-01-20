package com.thtf.auth.security;

import com.alibaba.fastjson.JSON;
import com.thtf.auth.feign.RemoteSysUserService;
import com.thtf.base.api.vo.SysUserVO;
import com.thtf.base.api.vo.UserDetailsVO;
import com.thtf.common.core.constant.CommonConstant;
import com.thtf.common.core.exception.ExceptionCast;
import com.thtf.common.core.response.CommonCode;
import com.thtf.common.core.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

/**
 * ---------------------------
 * 用户身份验证
 * ---------------------------
 * 作者：  pyy
 * 时间：  2020/1/20 16:05
 * 版本：  v1.0
 * ---------------------------
 */
@Slf4j
@Service
public class PyyUserDetailsService implements UserDetailsService {
    @Autowired
    private RemoteSysUserService remoteSysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ResponseResult<UserDetailsVO> info = remoteSysUserService.findByUsername(username);
        if (info == null || info.getCode() != CommonCode.SUCCESS.code()) {
            log.debug("### 登录用户：{} 不存在 ###", username);
            ExceptionCast.cast(CommonCode.USERNAME_IS_NULL);
        }
        UserDetailsVO userDetailsVO = info.getData();
        SysUserVO sysUser = userDetailsVO.getSysUser();
        Set<String> permissions = userDetailsVO.getPermissions();
        Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(permissions.toArray(new String[0]));

        // 将sysUser转换为json
        String principal = JSON.toJSONString(sysUser);
        UserDetails userDetails = User.withUsername(principal).password(sysUser.getPassword()).authorities(authorities).build();
        return userDetails;
    }
}
