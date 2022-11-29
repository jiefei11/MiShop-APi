package com.bhgeek.mishopapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.bhgeek.mishopapi.common.RespBean;
import com.bhgeek.mishopapi.common.RespBeanEnum;
import com.bhgeek.mishopapi.common.UserContext;
import com.bhgeek.mishopapi.entity.User;
import com.bhgeek.mishopapi.interceptor.AuthCheck;
import com.bhgeek.mishopapi.service.IUserService;
import com.bhgeek.mishopapi.utils.EmailUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author bhgeek
 * @since 2022-10-14
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private IUserService userService;

    @Autowired
    private EmailUtil emailUtil;



    @GetMapping
    @AuthCheck
    public RespBean getUserInfo() {
        User user = userService.getById(UserContext.getUserId());
        return new RespBean(RespBeanEnum.SUCCESS,user);
    }

    @GetMapping("/all")
    @AuthCheck
    public RespBean getAllUser() {
        return new RespBean(RespBeanEnum.SUCCESS, userService.list());
    }


    /**
     * 注册
     * @param phone
     * @param password
     * @return
     */
    @PostMapping("/register")
    public RespBean register(String phone , String password){
        User user = userService.register(phone,password);
        return new RespBean(RespBeanEnum.SUCCESS,user);
    }

    /**
     * 登录
     * @param phone
     * @param password
     * @return
     */
    @PostMapping("/login")
    public RespBean login (String phone , String password , HttpServletResponse response){
        RespBean login = userService.login(phone, password , response);
        return login;
    }

    @PostMapping("/sendEmail")
    @AuthCheck
    public RespBean sendEmail(String email) {
        long codeL = System.nanoTime();
        String codeStr = Long.toString(codeL);
        codeStr = codeStr.substring(codeStr.length() - 8, codeStr.length() - 2);
        String title = "绑定邮箱验证码";
        String content = "亲爱的小米用户，您好！\n" +
                "\n" +
                "您的绑定验证码是："+codeStr+ "\n" +
                "\n" +
                "本邮件由系统自动发送，请勿直接回复！\n" +
                "感谢您的访问，祝您使用愉快！";
        stringRedisTemplate.opsForValue().set(email,codeStr,60 * 2, TimeUnit.SECONDS);
        emailUtil.sendMessage(email,title,content);
        return new RespBean(RespBeanEnum.SUCCESS);
    }

    /**
     * 确认邮箱验证吗
     * @param email
     * @return
     */
    @AuthCheck
    @PostMapping("/confirmEmail")
    public RespBean confirmEmail(String email , String code) {
        String result = stringRedisTemplate.opsForValue().get(email);
        if (StringUtils.equals(code,result)){
            Integer userId = UserContext.getUserId();
            User user = userService.getById(userId);
            user.setEmail(email);
            userService.saveOrUpdate(user);
            return new RespBean(RespBeanEnum.SUCCESS,"邮箱设置成功");
        } else {
            return new RespBean(RespBeanEnum.VALIDATION_ERROR);
        }
    }

    /**
     * 发送验证码
     * @param phone
     * @return
     */
    @PostMapping("/getCode")
    public RespBean getCode(String phone){
        String result  = (String) stringRedisTemplate.opsForValue().get(phone);
        if (!StringUtils.isNotEmpty(result)){
            long codeL = System.nanoTime();
            String codeStr = Long.toString(codeL);
            codeStr = codeStr.substring(codeStr.length() - 8, codeStr.length() - 2);
            stringRedisTemplate.opsForValue().set(phone,codeStr,30, TimeUnit.SECONDS);
            return new RespBean(RespBeanEnum.SUCCESS,(Object) codeStr);
        } else {
            return new RespBean(RespBeanEnum.ERROR,"不要,太频繁了,再忍30秒吧");
        }
    }


    /**
     * 确认验证码
     * @param phone
     * @param code
     * @return
     */
    @PostMapping("/verification")
    public RespBean verification(String phone , String code){
        String result = (String) stringRedisTemplate.opsForValue().get(phone);
        if (StringUtils.equals(code,result)){
            User one = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
            if (one == null){
                return new RespBean(RespBeanEnum.SUCCESS);
            } else {
                return new RespBean(RespBeanEnum.USER_ACCOUNT_EXISTED);
            }
        }else {
            return new RespBean(RespBeanEnum.VALIDATION_ERROR);
        }
    }

    /**
     * 修改密码
     */
    @PostMapping("/resetPwd")
    @AuthCheck
    public RespBean resetRwd(String password){
        Integer userId = UserContext.getUserId();
        User user = userService.getById(userId);
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        user.setPassword(password);
        userService.saveOrUpdate(user);
        return new RespBean(RespBeanEnum.SUCCESS);
    }

    @PostMapping("/editInfo")
    @AuthCheck
    public RespBean editInfo(String avatar , String username) {
        Integer userId = UserContext.getUserId();
        User user = userService.getById(userId);
        user.setAvatar(avatar);
        user.setUsername(username);
        userService.saveOrUpdate(user);
        return new RespBean(RespBeanEnum.SUCCESS, user);
    }

    @PostMapping("/Interdiction")
    @AuthCheck
    public RespBean Interdiction(Integer uid) {
        User user = userService.getById(uid);
        if (user.getStatus() == 0) {
            user.setStatus(1);
        } else {
            user.setStatus(0);
        }
        userService.saveOrUpdate(user);
        return new RespBean(RespBeanEnum.SUCCESS);
    }

}
