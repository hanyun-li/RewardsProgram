package cloud.lihan.rewardsprogram.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

/**
 * Aliyun服务邮件发送工具包
 *
 * @author: hanyun.li
 * @date: 2021/11/17
 **/
@Slf4j
public class EmailUtil {

    /**
     * aliyun主机名称（例：smtpdm.aliyun.com或smtp.aliyun.com）
     */
    private static final String SMTP_HOST = "smtpdm.aliyun.com";

    /**
     * 服务器端口（例：80或25）
     */
    private static final String SMTP_PORT = "80";

    /**
     * 密码（例：test123）
     */
    private static final String PASSWORD = "test123";

    /**
     * 用户名（发件人邮箱地址）（例：test@gmail.com）
     */
    private static final String EMAIL_USERNAME = "test@gmail.com";

    /**
     * 邮箱地址必须包含的符号
     */
    private static final String CHAR_ONE = "@";
    private static final String CHAR_TWO = ".";

    /**
     * 发送邮件（建议使用场景：给单人发送一封自定义内容和标题的邮件）
     *
     * @param receivingAddress 收件人邮件地址（单个）
     * @param emailContent     发送邮件内容（文本）
     * @param emailTitle       发送邮件的标题
     */
    public static void sendMail(String receivingAddress, String emailContent, String emailTitle) {
        final List<String> emails = new Vector<>(1);
        emails.add(receivingAddress);
        sendMail(emails, emailContent, emailTitle);
    }

    /**
     * 发送邮件（建议使用场景：给多人发送一封自定义内容和标题都相同的邮件）
     *
     * @param receivingAddresses 收件人邮件地址（多个）
     * @param emailContent       发送邮件内容（文本）
     * @param emailTitle         发送邮件的标题
     */
    public static void sendMail(List<String> receivingAddresses, String emailContent, String emailTitle) {
        // 配置发送邮件的环境属性
        String[] emails = receivingAddresses.toArray(new String[0]);
        // 获取验证信息
        MimeMessage message = getMimeMessage(emailTitle, emails);

        try {
            // 设置邮件的内容体
            message.setContent(emailContent, "text/html;charset=UTF-8");
            message.saveChanges();
            // 发送邮件
            Transport.send(message);
        } catch (Exception e) {
            log.error("发送邮件错误, 邮件地址：" + receivingAddresses, e);
        }
    }

    /**
     * 发送邮件（建议使用场景：给多人发送多封自定义标题和内容相同的邮件）
     *
     * @param receivingAddressesAndEmailTitle 收件人邮件地址和发送邮件标题一一对应的map集合
     * @param emailContent                    发送邮件的内容
     */
    public static void sendMail(Map<String, String> receivingAddressesAndEmailTitle, String emailContent) {
        if (receivingAddressesAndEmailTitle.isEmpty()) {
            log.error("发送邮件错误，未指定收件人邮件地址和发送邮件内容一一对应的map集合，入参receivingAddressesAndEmailTitle内容为空!");
            return;
        }

        receivingAddressesAndEmailTitle.forEach((receivingAddresses, emailTitle) -> sendMail(receivingAddresses, emailContent, emailTitle));
    }

    /**
     * 发送邮件（建议使用场景：给多人发送多封自定义标题和内容的邮件）
     *
     * @param sendMailParams 包含了收件人邮件地址、发送邮件内容以及邮件标题的对象list集合
     */
    public static void sendMail(List<SendMailParam> sendMailParams) {
        if (sendMailParams.isEmpty()) {
            log.error("发送邮件错误，未指定收件人邮件地址和发送邮件内容一一对应的map集合，入参receivingAddressesAndEmailTitle内容为空!");
            return;
        }

        sendMailParams.forEach(sendMailParam -> {
            Boolean isPass = checkSendParams(sendMailParam);
            if (isPass) {
                sendMail(sendMailParam);
            }
        });
    }

    /**
     * 发送邮件（建议使用场景：给单人发送一封自定义标题和内容的邮件）
     *
     * @param sendMailParam 包含了收件人邮件地址、发送邮件内容以及邮件标题的对象
     */
    public static void sendMail(SendMailParam sendMailParam) {
        Boolean isPass = checkSendParams(sendMailParam);
        if (isPass) {
            sendMail(sendMailParam.receivingAddress, sendMailParam.getEmailContent(), sendMailParam.getEmailTitle());
        }
    }

    /**
     * 生成空的发送邮件入参对象
     *
     * @return SendMailParam 空的入参对象
     */
    public static SendMailParam builderSingleSendMailParam() {
        return SendMailParam.getInstance();
    }

    /**
     * 生成包含所有值的发送邮件入参对象
     *
     * @param receivingAddress 收件人邮件地址
     * @param emailContent     发送邮件内容（文本）
     * @param emailTitle       发送邮件的标题
     * @return SendMailParam 包含所有值的入参对象
     */
    public static SendMailParam builderMultipleSendMailParams(String receivingAddress, String emailContent, String emailTitle) {
        return SendMailParam.getInstance(receivingAddress, emailContent, emailTitle);
    }

    /**
     * 检查邮箱格式是否正确
     *
     * @param emailAddress 邮箱地址(不能为空)
     * @return true:正确 false:不正确
     */
    public static Boolean checkEmailFormat(String emailAddress) {
        if (!StringUtils.hasText(emailAddress)) {
            return Boolean.FALSE;
        }

        if (emailAddress.contains(CHAR_ONE) && emailAddress.contains(CHAR_TWO)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 内部类（作为群发参数使用）
     */
    public static class SendMailParam {

        /**
         * 收件人邮件地址
         */
        private String receivingAddress;

        /**
         * 发送邮件内容
         */
        private String emailContent;

        /**
         * 发送邮件的标题
         */
        private String emailTitle;

        private SendMailParam() {
        }

        private SendMailParam(String receivingAddress, String emailContent, String emailTitle) {
            this.receivingAddress = receivingAddress;
            this.emailContent = emailContent;
            this.emailTitle = emailTitle;
        }

        public static SendMailParam getInstance() {
            return new SendMailParam();
        }

        public static SendMailParam getInstance(String receivingAddress, String emailContent, String emailTitle) {
            return new SendMailParam(receivingAddress, emailContent, emailTitle);
        }

        public String getReceivingAddress() {
            return receivingAddress;
        }

        public void setReceivingAddress(String receivingAddress) {
            this.receivingAddress = receivingAddress;
        }

        public String getEmailContent() {
            return emailContent;
        }

        public void setEmailContent(String emailContent) {
            this.emailContent = emailContent;
        }

        public String getEmailTitle() {
            return emailTitle;
        }

        public void setEmailTitle(String emailTitle) {
            this.emailTitle = emailTitle;
        }
    }

    /**
     * 获取验证信息（实际赋值属性信息）
     *
     * @param emailTitle         发送邮件的标题
     * @param receivingAddresses 收件人邮件地址（单或多个）
     * @return 认证之后的消息实体
     */
    private static MimeMessage getMimeMessage(String emailTitle, String... receivingAddresses) {
        // 表示SMTP发送邮件，需要进行身份验证
        final Properties props = new Properties();
        // 开启smtp认证
        props.put("mail.smtp.auth", "true");
        // 服务主机地址
        props.put("mail.smtp.host", SMTP_HOST);
        // 服务器端口
        props.put("mail.smtp.port", SMTP_PORT);
        // 发件人的账号
        props.put("mail.user", EMAIL_USERNAME);
        // 访问SMTP服务时需要提供的密码
        props.put("mail.password", PASSWORD);

        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                return new PasswordAuthentication(userName, PASSWORD);
            }
        };

        // 使用环境属性和授权信息，创建邮件会话
        try {
            Session mailSession = Session.getInstance(props, authenticator);
            mailSession.setDebug(false);

            // 创建邮件消息
            MimeMessage message = new MimeMessage(mailSession);
            // 设置发件人
            InternetAddress form = new InternetAddress(EMAIL_USERNAME);
            message.setFrom(form);
            // 设置收件人
            for (String toEmail : receivingAddresses) {
                message.addRecipients(MimeMessage.RecipientType.TO, toEmail);
            }

            // 设置邮件标题
            message.setSubject(emailTitle);
            return message;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 校验群发邮件时的入参
     *
     * @param sendMailParam 群发入参（包含了收件人邮件地址、发送邮件内容以及邮件标题的对象）
     * @return 是否通过校验 true:通过 false:未通过
     */
    private static Boolean checkSendParams(SendMailParam sendMailParam) {
        if (ObjectUtils.isEmpty(sendMailParam)) {
            log.error("发送错误，入参SendMailParam为空值！");
            return Boolean.FALSE;
        } else if (!StringUtils.hasText(sendMailParam.getReceivingAddress())) {
            log.error("发送错误，入参SendMailParam中，未指定发送人地址！");
            return Boolean.FALSE;
        } else if (!StringUtils.hasText(sendMailParam.getEmailContent())) {
            log.error("发送错误，入参SendMailParam中，未指定邮件内容！");
            return Boolean.FALSE;
        } else if (!StringUtils.hasText(sendMailParam.getEmailTitle())) {
            log.error("发送错误，入参SendMailParam中，未指定邮件标题！");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

}
