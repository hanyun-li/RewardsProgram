package cloud.lihan.rewardsprogram.common.controller;


import cloud.lihan.rewardsprogram.common.core.Base;

/**
 * 基础控制层
 *
 * @author hanyun.li
 * @createTime 2022/06/29 15:23:00
 */
public class BaseController {

    /**
     * 状态码
     */
    private static final String SUCCESS_CODE = "200";
    private static final String ERROR_CODE = "500";

    /**
     * 返回状态描述信息
     */
    private static final String SUCCESS_MSG = "成功";
    private static final String ERROR_MSG = "失败";

    protected Base apiSuccess(Object list)
    {
        Base base = new Base();
        base.setCode("0");
        base.setData(list);
        base.setMsg("成功");
        return base;
    }

    protected Base apiReturn(String code, String msg, Object list)
    {
        Base base = new Base();
        base.setCode(code);
        base.setData(list);
        base.setMsg(msg);
        return base;
    }

    protected Base apiReturn(Object list)
    {
        Base base = new Base();
        base.setCode(SUCCESS_CODE);
        base.setData(list);
        base.setMsg(SUCCESS_MSG);
        return base;
    }

    protected Base apiOk(String code,String msg){
        Base base = new Base();
        base.setCode(code);
        base.setMsg(msg);
        return base;
    }

    protected Base apiOk(String msg,Object data){
        Base base = new Base();
        base.setCode(SUCCESS_CODE);
        base.setData(data);
        base.setMsg(msg);
        return base;
    }

    protected Base apiOk(Object data){
        Base base = new Base();
        base.setCode(SUCCESS_CODE);
        base.setData(data);
        base.setMsg(SUCCESS_MSG);
        return base;
    }

    protected Base apiOk(){
        Base base = new Base();
        base.setCode(SUCCESS_CODE);
        base.setMsg(SUCCESS_MSG);
        return base;
    }

    protected Base apiErr(){
        Base base = new Base();
        base.setCode(ERROR_CODE);
        base.setMsg(ERROR_MSG);
        return base;
    }

    protected Base apiErr(String msg){
        Base base = new Base();
        base.setCode(ERROR_CODE);
        base.setMsg(msg);
        return base;
    }

    protected Base ApiReturn(String code,String msg)
    {
        Base base = new Base();
        base.setCode(code);
        base.setMsg(msg);
        return base;
    }

}
