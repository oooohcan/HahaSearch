package edu.zuel.hahasearch.common;

/**
 * 返回工具类
 *
 * @author oooohcan
 */
public class ResultUtils {
    /**
     * 成功
     *
     * @param data
     * @return
     * @param <T>
     */
    public static <T> BaseRespone<T> success(T data){
        return new BaseRespone<>(0,data,"ok");
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
    public static BaseRespone error(ErrorCode errorCode){
        return new BaseRespone<>(errorCode);
    }

    /**
     * 失败
     *
     * @param errorCode
     * @param description
     * @return
     */
    public static BaseRespone error(ErrorCode errorCode,String description){
        return new BaseRespone<>(errorCode.getCode(),null,errorCode.getMessage(),description);
    }

    /**
     * 失败
     *
     * @param errorCode
     * @param message
     * @param description
     * @return
     */
    public static BaseRespone error(ErrorCode errorCode,String message,String description){
        return new BaseRespone<>(errorCode.getCode(),null,message,description);
    }

    /**
     * 失败
     *
     * @param code
     * @param message
     * @param description
     * @return
     */
    public static BaseRespone error(int code,String message,String description){
        return new BaseRespone<>(code,null,message,description);
    }
}
