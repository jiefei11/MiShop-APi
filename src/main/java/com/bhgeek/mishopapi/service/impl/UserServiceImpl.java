package com.bhgeek.mishopapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bhgeek.mishopapi.common.RespBean;
import com.bhgeek.mishopapi.common.RespBeanEnum;
import com.bhgeek.mishopapi.entity.User;
import com.bhgeek.mishopapi.mapper.UserMapper;
import com.bhgeek.mishopapi.service.IUserService;
import com.bhgeek.mishopapi.utils.JwtUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author bhgeek
 * @since 2022-10-14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public User register(String phone, String password) {
        User user = new User();
        user.setPhone(phone);
        user.setMoney(0L);
        user.setStatus(1);
        user.setUsername(phone);
        user.setRole("common");
        user.setAvatar("https://cdn.cnbj0.fds.api.mi-img.com/b2c-data-mishop/f790b51a76afd7b41522048fa779d69d.jpg");
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        user.setPassword(password);
        save(user);
        return user;
    }

    @Override
    public RespBean login(String phone, String password , HttpServletResponse response) {
        User one = getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
        if (one == null) {
            return new RespBean(RespBeanEnum.USER_ACCOUNT_NOT_EXIST);
        }
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        if (!StringUtils.equals(one.getPassword(),password)) {
            return new RespBean(RespBeanEnum.PASSWORD_ERROR);
        }

        if (one.getStatus() != 1){
            return new RespBean(RespBeanEnum.USER_ACCOUNT_LOCKED);
        }

        String token = JwtUtil.createToken(one);

        return new RespBean(RespBeanEnum.SUCCESS,(Object) token);
    }
}
