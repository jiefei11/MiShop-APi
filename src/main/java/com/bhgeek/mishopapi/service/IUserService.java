package com.bhgeek.mishopapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bhgeek.mishopapi.common.RespBean;
import com.bhgeek.mishopapi.entity.User;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author bhgeek
 * @since 2022-10-14
 */
public interface IUserService extends IService<User> {

    User register(String phone, String password);

    RespBean login(String phone, String password , HttpServletResponse response );
}
