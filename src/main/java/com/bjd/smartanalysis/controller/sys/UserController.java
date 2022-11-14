package com.bjd.smartanalysis.controller.sys;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSONObject;
import com.bjd.smartanalysis.common.ResponseData;
import com.bjd.smartanalysis.config.sys.JWTUtil;
import com.bjd.smartanalysis.entity.sys.SysUser;
import com.bjd.smartanalysis.service.sys.SysUserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private SysUserService userService;

    @PostMapping("login")
    private ResponseData Login(@RequestParam String username, @RequestParam String password) {
        SysUser user = new SysUser();

        user.setUsername(username);
        user.setPassword(DigestUtil.md5Hex(password));

        SysUser exUser = userService.getUserByName(username);
        if (exUser != null) {
            if(!exUser.getPassword().equals(user.getPassword())){
                return ResponseData.FAIL("密码不正确！");
            } else {
                String token = JWTUtil.sign(user.getUsername(), user.getPassword());
                JSONObject obj = new JSONObject();
                obj.put("token", token);
                return ResponseData.OK(obj);
            }
        } else {
            return ResponseData.FAIL("用户名不存在！");
        }
    }

    @GetMapping("userinfo")
    private ResponseData GetUserInfo() {
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        if (user != null) {
            return ResponseData.OK(user);
        }
        return ResponseData.NOAUTH();
    }
}
