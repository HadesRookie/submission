package com.graduation.submission.controller;

import com.graduation.submission.common.ResponseResult;
import com.graduation.submission.pojo.Role;
import com.graduation.submission.pojo.User;
import com.graduation.submission.pojo.dto.UserSearchDTO;
import com.graduation.submission.pojo.vo.UserRoleVO;
import com.graduation.submission.pojo.vo.UserVO;
import com.graduation.submission.service.PermissionService;
import com.graduation.submission.service.RoleService;
import com.graduation.submission.service.UserService;
import com.graduation.submission.utils.IStatusMessage;
import com.graduation.submission.utils.PageDataResult;
import com.graduation.submission.utils.ValidateUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/21 23:03
 * @Version 1.0
 **/
@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory
            .getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private EhCacheManager ecm;

    @RequestMapping("/userList")
    public String toUserList() {
        return "/auth/userList";
    }

    /**
     * 登录【使用shiro中自带的HashedCredentialsMatcher结合ehcache（记录输错次数）配置进行密码输错次数限制】
     * </br>缺陷是，无法友好的在后台提供解锁用户的功能，当然，可以直接提供一种解锁操作，清除ehcache缓存即可，不记录在用户表中；
     * </br>
     * @param user
     * @param rememberMe
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult login(
            UserVO user,
            @RequestParam(value = "rememberMe", required = false) boolean rememberMe) {
        logger.debug("用户登录，请求参数=user:" + user + "，是否记住我：" + rememberMe);
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
        if (null == user) {
            responseResult.setCode(IStatusMessage.SystemStatus.PARAM_ERROR
                    .getCode());
            responseResult.setMessage("请求参数有误，请您稍后再试");
            logger.debug("用户登录，结果=responseResult:" + responseResult);
            return responseResult;
        }
        // 用户是否存在
        User existUser = this.userService.getUser(user.getUsername());
        if (existUser == null) {
            responseResult.setMessage("该用户不存在，请您联系管理员");
            logger.debug("用户登录，结果=responseResult:" + responseResult);
            return responseResult;
        } else {
            // 是否离职
            if (existUser.getIsUse() == 1) {
                responseResult.setMessage("登录用户已被禁用，请您联系管理员");
                logger.debug("用户登录，结果=responseResult:" + responseResult);
                return responseResult;
            }
        }
        // 用户登录
        try {
            // 1、 封装用户名、密码、是否记住我到token令牌对象 [支持记住我]
            AuthenticationToken token = new UsernamePasswordToken(
                    user.getUsername(), DigestUtils.md5Hex(user.getPassword()),
                    rememberMe);
            // 2、 Subject调用login
            Subject subject = SecurityUtils.getSubject();
            // 在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
            // 每个Realm都能在必要时对提交的AuthenticationTokens作出反应
            // 所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
            logger.debug("用户登录，用户验证开始！user=" + user.getUsername());
            subject.login(token);
            responseResult.setCode(IStatusMessage.SystemStatus.SUCCESS
                    .getCode());
            logger.info("用户登录，用户验证通过！user=" + user.getUsername());
        } catch (UnknownAccountException uae) {
            logger.error("用户登录，用户验证未通过：未知用户！user=" + user.getUsername(), uae);
            responseResult.setMessage("该用户不存在，请您联系管理员");
        } catch (IncorrectCredentialsException ice) {
            // 获取输错次数
            logger.error("用户登录，用户验证未通过：错误的凭证，密码输入错误！user=" + user.getUsername(),
                    ice);
            responseResult.setMessage("用户名或密码不正确");
        } catch (LockedAccountException lae) {
            logger.error("用户登录，用户验证未通过：账户已锁定！user=" + user.getUsername(), lae);
            responseResult.setMessage("账户已锁定");
        } catch (ExcessiveAttemptsException eae) {
            logger.error(
                    "用户登录，用户验证未通过：错误次数大于5次,账户已锁定！user=.getUsername()" + user, eae);
            responseResult
                    .setMessage("用户名或密码错误次数大于5次,账户已锁定!</br><span style='color:red;font-weight:bold; '>2分钟后可再次登录，或联系管理员解锁</span>");
            // 这里结合了，另一种密码输错限制的实现，基于redis或mysql的实现；也可以直接使用RetryLimitHashedCredentialsMatcher限制5次
        } catch (AuthenticationException ae) {
            // 通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
            logger.error("用户登录，用户验证未通过：认证异常，异常信息如下！user=" + user.getUsername(),
                    ae);
            responseResult.setMessage("用户名或密码不正确");
        } catch (Exception e) {
            logger.error("用户登录，用户验证未通过：操作异常，异常信息如下！user=" + user.getUsername(), e);
            responseResult.setMessage("用户登录失败，请您稍后再试");
        }
        Cache<String, AtomicInteger> passwordRetryCache = ecm
                .getCache("passwordRetryCache");
        if (null != passwordRetryCache) {
            int retryNum = (passwordRetryCache.get(existUser.getUsername()) == null ? 0
                    : passwordRetryCache.get(existUser.getUsername())).intValue();
            logger.debug("输错次数：" + retryNum);
            if (retryNum > 0 && retryNum < 6) {
                responseResult.setMessage("用户名或密码错误" + retryNum + "次,再输错"
                        + (6 - retryNum) + "次账号将锁定");
            }
        }
        logger.debug("用户登录，user=" + user.getUsername() + ",登录结果=responseResult:"
                + responseResult);
        return responseResult;
    }

    /**
     * 注册
     * @param user
     * @return
     */
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult register(User user){
        ResponseResult responseResult = new ResponseResult();
        try {
            Integer roleId = 5;
            logger.info(roleId.toString());
            responseResult = userService.register(user,roleId);
        }catch (Exception e){
            e.printStackTrace();
            responseResult.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
            return responseResult;
        }
        return responseResult;
    }

    @RequestMapping(value = "/forget",method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult forget(UserVO userVO) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(IStatusMessage.SystemStatus.PARAM_ERROR
                .getCode());
        try {
            if (!ValidateUtil.isMobilephone(userVO.getMobile())) {
                responseResult.setMessage("手机号格式有误，请您重新填写");
                return responseResult;
            }
            if (!ValidateUtil.isPicCode(userVO.getCode())) {
                responseResult.setMessage("图片验证码有误，请您重新填写");
                return responseResult;
            }
            if (!ValidateUtil.isCode(userVO.getSmsCode())) {
                responseResult.setMessage("短信验证码有误，请您重新填写");
                return responseResult;
            }

            User user = userService.findUserByMobile(userVO.getMobile());
            if (user == null){
                responseResult.setCode(IStatusMessage.SystemStatus.NO_LOGIN
                        .getCode());
                responseResult.setMessage("该用户不存在，请先注册！");
                return responseResult;
            }

            // 修改密码
            int num = this.userService.updatePwd(user.getId(),
                    DigestUtils.md5Hex(user.getPassword()));
            if (num != 1) {
                responseResult.setCode(IStatusMessage.SystemStatus.ERROR
                        .getCode());
                responseResult.setMessage("操作失败，请您稍后再试");
            }else {
                responseResult.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
            }
        }catch (Exception e){
            e.printStackTrace();
            responseResult.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
            responseResult.setMessage("操作失败，请您稍后再试");
            return responseResult;
        }
        return responseResult;
    }

    /**
     * 分页查询用户列表
     * @return ok/fail
     */
    @RequestMapping(value = "/getUsers", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = "usermanage")
    public PageDataResult getUsers(@RequestParam("page") Integer page,
                                   @RequestParam("limit") Integer limit, UserSearchDTO userSearch) {
        logger.debug("分页查询用户列表！搜索条件：userSearch：" + userSearch + ",page:" + page
                + ",每页记录数量limit:" + limit);
        PageDataResult pdr = new PageDataResult();
        try {
            if (null == page) {
                page = 1;
            }
            if (null == limit) {
                limit = 10;
            }
            // 获取用户和角色列表
            pdr = userService.getUsers(userSearch, page, limit);
            logger.debug("用户列表查询=pdr:" + pdr);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户列表查询异常！", e);
        }
        return pdr;
    }

    /**
     * 设置用户是否禁用
     * @return ok/fail
     */
    @RequestMapping(value = "/setUserUse", method = RequestMethod.POST)
    @ResponseBody
    public String setJobUser(@RequestParam("id") Integer id,
                             @RequestParam("isUse") Integer isUse,
                             @RequestParam("version") Integer version) {
        logger.debug("设置用户是否禁用！id:" + id + ",isUse:" + isUse + ",version:"
                + version);
        String msg = "";
        try {
            if (null == id || null == isUse || null == version) {
                logger.debug("设置用户是否禁用，结果=请求参数有误，请您稍后再试");
                return "请求参数有误，请您稍后再试";
            }
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (null == existUser) {
                logger.debug("设置用户是否禁用，结果=您未登录或登录超时，请您登录后再试");
                return "您未登录或登录超时，请您登录后再试";
            }
            // 设置用户是否离职
            msg = userService.setUserUse(id, isUse, version);
            logger.info("设置用户是否禁用成功！userID=" + id + ",isUse:" + isUse
                    + "，操作的用户ID=" + existUser.getId());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("设置用户是否禁用异常！", e);
            msg = "操作异常，请您稍后再试！";
        }
        return msg;
    }

    /**
     * 设置用户[新增或更新]
     * @return ok/fail
     */
    @RequestMapping(value = "/setUser", method = RequestMethod.POST)
    @ResponseBody
    public String setUser(@RequestParam("roleIds") String roleIds, User user) {
        logger.debug("设置用户[新增或更新]！user:" + user + ",roleIds:" + roleIds);
        try {
            if (null == user) {
                logger.debug("置用户[新增或更新]，结果=请您填写用户信息");
                return "请您填写用户信息";
            }
            if (StringUtils.isEmpty(roleIds)) {
                logger.debug("置用户[新增或更新]，结果=请您给用户设置角色");
                return "请您给用户设置角色";
            }
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (null == existUser) {
                logger.debug("置用户[新增或更新]，结果=您未登录或登录超时，请您登录后再试");
                return "您未登录或登录超时，请您登录后再试";
            }
            // 设置用户[新增或更新]
            logger.info("设置用户[新增或更新]成功！user=" + user + ",roleIds=" + roleIds
                    + "，操作的用户ID=" + existUser.getId());
            return userService.setUser(user, roleIds);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("设置用户[新增或更新]异常！", e);
            return "操作异常，请您稍后再试";
        }
    }

    /**
     * 查询用户数据
     * @return map
     */
    @RequestMapping(value = "/getUserAndRoles", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getUserAndRoles(@RequestParam("id") Integer id) {
        logger.debug("查询用户数据！id:" + id);
        Map<String, Object> map = new HashMap<>();
        try {
            if (null == id) {
                logger.debug("查询用户数据==请求参数有误，请您稍后再试");
                map.put("msg", "请求参数有误，请您稍后再试");
                return map;
            }
            // 查询用户
            UserRoleVO urvo = userService.getUserAndRoles(id);
            logger.debug("查询用户数据！urvo=" + urvo);
            if (null != urvo) {
                map.put("user", urvo);
                // 获取全部角色数据
                List<Role> roles = this.roleService.getRoles();
                logger.debug("查询角色数据！roles=" + roles);
                if (null != roles && roles.size() > 0) {
                    map.put("roles", roles);
                }
                map.put("msg", "ok");
            } else {
                map.put("msg", "查询用户信息有误，请您稍后再试");
            }
            logger.debug("查询用户数据成功！map=" + map);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "查询用户错误，请您稍后再试");
            logger.error("查询用户数据异常！", e);
        }
        return map;
    }

    /**
     * 删除用户
     * @return ok/fail
     */
    @RequestMapping(value = "/delUser", method = RequestMethod.POST)
    @ResponseBody
    public String delUser(@RequestParam("id") Integer id) {
        logger.debug("删除用户！id:" + id );
        String msg = "";
        try {
            if (null == id) {
                logger.debug("删除用户，结果=请求参数有误，请您稍后再试");
                return "请求参数有误，请您稍后再试";
            }
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (null == existUser) {
                logger.debug("删除用户，结果=您未登录或登录超时，请您登录后再试");
                return "您未登录或登录超时，请您登录后再试";
            }
            // 删除用户
            msg = userService.delUser(id);
            logger.info("删除用户:" + msg + "！userId=" + id + "，操作用户id:"
                    + existUser.getId());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除用户异常！", e);
            msg = "操作异常，请您稍后再试";
        }
        return msg;
    }

    /**
     * 发送短信验证码
     * @param mobile
     * @return
     */
    @RequestMapping(value = "sendMsg", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult sendMsg(@RequestParam("mobile") String mobile) {

        ResponseResult responseResult = new ResponseResult();
        try {
            if (!ValidateUtil.isMobilephone(mobile)) {
                responseResult.setCode(IStatusMessage.SystemStatus.PARAM_ERROR
                        .getCode());
                responseResult.setMessage("手机号格式有误，请您重新填写");
                logger.debug("发送短信验证码，结果=responseResult:" + responseResult);
                return responseResult;
            }
            // 送短信验证码
            // String msg=userService.sendMessage(mobile);
            String msg = "ok";
            if (msg != "ok") {
                responseResult.setCode(IStatusMessage.SystemStatus.ERROR
                        .getCode());
                responseResult.setMessage(msg == "no" ? "发送验证码失败，请您稍后再试" : msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseResult.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
            responseResult.setMessage("发送短信验证码失败，请您稍后再试");
            logger.error("发送短信验证码异常！", e);
        }
        logger.debug("发送短信验证码，结果=responseResult:" + responseResult);
        return responseResult;
    }

    /**
     * 发送短信验证码
     * @param mobile
     * @param picCode
     * @return
     */
    @RequestMapping(value = "sendMessage", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult sendMessage(@RequestParam("mobile") String mobile,
                                      @RequestParam("picCode") String picCode) {
        logger.debug("发送短信验证码！mobile:" + mobile + ",picCode=" + picCode);
        ResponseResult responseResult = new ResponseResult();
        try {
            if (!ValidateUtil.isMobilephone(mobile)) {
                responseResult.setCode(IStatusMessage.SystemStatus.PARAM_ERROR
                        .getCode());
                responseResult.setMessage("手机号格式有误，请您重新填写");
                logger.debug("发送短信验证码，结果=responseResult:" + responseResult);
                return responseResult;
            }
            if (!ValidateUtil.isPicCode(picCode)) {
                responseResult.setCode(IStatusMessage.SystemStatus.PARAM_ERROR
                        .getCode());
                responseResult.setMessage("图片验证码有误，请您重新填写");
                logger.debug("发送短信验证码，结果=responseResult:" + responseResult);
                return responseResult;
            }
            // 判断用户是否登录
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (null == existUser) {
                responseResult.setCode(IStatusMessage.SystemStatus.NO_LOGIN
                        .getCode());
                responseResult.setMessage("您未登录或登录超时，请您重新登录后再试");
                logger.debug("发送短信验证码，结果=responseResult:" + responseResult);
                return responseResult;
            }
            // 送短信验证码
            // String msg=userService.sendMessage(existUser.getId(),mobile);
            String msg = "ok";
            if (msg != "ok") {
                responseResult.setCode(IStatusMessage.SystemStatus.ERROR
                        .getCode());
                responseResult.setMessage(msg == "no" ? "发送验证码失败，请您稍后再试" : msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseResult.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
            responseResult.setMessage("发送短信验证码失败，请您稍后再试");
            logger.error("发送短信验证码异常！", e);
        }
        logger.debug("发送短信验证码，结果=responseResult:" + responseResult);
        return responseResult;
    }

    /**
     * 修改密码之确认手机号
     * @param mobile
     * @param picCode
     * @return
     */
    @RequestMapping(value = "updatePwd", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult updatePwd(@RequestParam("mobile") String mobile,
                                    @RequestParam("picCode") String picCode,
                                    @RequestParam("mobileCode") String mobileCode) {
        logger.debug("修改密码之确认手机号！mobile:" + mobile + ",picCode=" + picCode
                + ",mobileCode=" + mobileCode);
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(IStatusMessage.SystemStatus.PARAM_ERROR
                .getCode());
        try {
            if (!ValidateUtil.isMobilephone(mobile)) {
                responseResult.setMessage("手机号格式有误，请您重新填写");
                logger.debug("修改密码之确认手机号，结果=responseResult:" + responseResult);
                return responseResult;
            }
            if (!ValidateUtil.isPicCode(picCode)) {
                responseResult.setMessage("图片验证码有误，请您重新填写");
                logger.debug("发修改密码之确认手机号，结果=responseResult:" + responseResult);
                return responseResult;
            }
            if (!ValidateUtil.isCode(mobileCode)) {
                responseResult.setMessage("短信验证码有误，请您重新填写");
                logger.debug("发修改密码之确认手机号，结果=responseResult:" + responseResult);
                return responseResult;
            }
            // 判断用户是否登录
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (null == existUser) {
                responseResult.setMessage("您未登录或登录超时，请您重新登录后再试");
                logger.debug("修改密码之确认手机号，结果=responseResult:" + responseResult);
                return responseResult;
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
            responseResult.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
            responseResult.setMessage("操作失败，请您稍后再试");
            logger.error("修改密码之确认手机号异常！", e);
            return responseResult;
        }
        responseResult.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        responseResult.setMessage("SUCCESS");
        logger.debug("修改密码之确认手机号，结果=responseResult:" + responseResult);
        return responseResult;
    }

    /**
     * 修改密码
     * @param pwd
     * @param isPwd
     * @return
     */
    @RequestMapping(value = "setPwd", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult setPwd(@RequestParam("pwd") String pwd,
                                 @RequestParam("isPwd") String isPwd) {
        logger.debug("修改密码！pwd:" + pwd + ",isPwd=" + isPwd);
        ResponseResult responseResult = new ResponseResult();
        try {
            if (!ValidateUtil.isSimplePassword(pwd)
                    || !ValidateUtil.isSimplePassword(isPwd)) {
                responseResult.setCode(IStatusMessage.SystemStatus.PARAM_ERROR
                        .getCode());
                responseResult.setMessage("密码格式有误，请您重新填写");
                logger.debug("修改密码，结果=responseResult:" + responseResult);
                return responseResult;
            }
            if (!pwd.equals(isPwd)) {
                responseResult.setCode(IStatusMessage.SystemStatus.PARAM_ERROR
                        .getCode());
                responseResult.setMessage("两次密码输入不一致，请您重新填写");
                logger.debug("发修改密码，结果=responseResult:" + responseResult);
                return responseResult;
            }
            // 判断用户是否登录
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (null == existUser) {
                responseResult.setCode(IStatusMessage.SystemStatus.NO_LOGIN
                        .getCode());
                responseResult.setMessage("您未登录或登录超时，请您重新登录后再试");
                logger.debug("修改密码，结果=responseResult:" + responseResult);
                return responseResult;
            }
            // 修改密码
            int num = this.userService.updatePwd(existUser.getId(),
                    DigestUtils.md5Hex(pwd));
            if (num != 1) {
                responseResult.setCode(IStatusMessage.SystemStatus.ERROR
                        .getCode());
                responseResult.setMessage("操作失败，请您稍后再试");
                logger.debug("修改密码失败，已经离职或该用户被删除！结果=responseResult:"
                        + responseResult);
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseResult.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
            responseResult.setMessage("操作失败，请您稍后再试");
            logger.error("修改密码异常！", e);
        }
        logger.debug("修改密码，结果=responseResult:" + responseResult);
        return responseResult;
    }

}
