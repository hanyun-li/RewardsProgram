package cloud.lihan.rewardsprogram.common.utils;

import cloud.lihan.rewardsprogram.common.constants.SessionConstant;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * 登录相关工具
 *
 * @author hanyun.li
 * @createTime 2022/07/20 16:05:00
 */
@Slf4j
public class LoginUtil {

    /**
     * 检查本次请求用户是否登录
     *
     * @param request http请求对象
     * @return {@link Boolean} true:已登录 false:未登录
     */
    public static Boolean checkLogin(HttpServletRequest request) {
        if (Objects.isNull(request)) {
            log.error("LoginUtil.isLogin() throw Exception! errorInfo: [param: request is null!]");
            return Boolean.FALSE;
        }

        HttpSession session = request.getSession();
        if (Objects.isNull(session)) {
            log.error("LoginUtil.isLogin() throw Exception! errorInfo: [session is null!]");
            return Boolean.FALSE;
        }

        // 从session中获取userId
        String userId = getLoginTokenBySession(session);
        if (StringUtils.isEmpty(userId)) {
            log.error("LoginUtil.isLogin() throw Exception! errorInfo: [Not Exist login_token in session!]");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 从request对象中获取login_token(userId)
     *
     * @param request http请求对象
     * @return {@link String} 用户ID
     */
    public static String getLoginTokenByRequest(HttpServletRequest request) {
        return getLoginTokenBySession(request.getSession());
    }

    /**
     * 从session中获取login_token(userId)
     *
     * @param session 当前用户的会话对象
     * @return {@link String} 用户ID
     */
    public static String getLoginTokenBySession(HttpSession session) {
        return String.valueOf(session.getAttribute(SessionConstant.LOGIN_TOKEN));
    }

}
