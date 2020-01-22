package com.thtf.auth.feign;

import com.thtf.auth.feign.factory.RemoteSysUserFallbackFactory;
import com.thtf.base.api.vo.UserDetailsVO;
import com.thtf.common.core.response.ResponseResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;


/**
 * ---------------------------
 * 用户服务Feign客户端
 * ---------------------------
 * 作者：  pyy
 * 时间：  2019/12/27 15:31
 * 版本：  v1.0
 * ---------------------------
 */
@FeignClient(value = "pyy-system-base-server", fallbackFactory = RemoteSysUserFallbackFactory.class)
public interface RemoteSysUserService {
    /**
     * 根据用户名查询
     * @param username
     * @return
     */
    @ApiOperation(value = "根据用户名查询", notes = "根据用户名查询")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "query")
    @GetMapping("/sysUser")
    ResponseResult<UserDetailsVO> findByUsername(@Valid @NotBlank(message = "用户名不能为空") @RequestParam("username") String username);

}
