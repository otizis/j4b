package cc.jaxer.blog.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BException extends RuntimeException
{
    private static final long serialVersionUID = -2906235205927153377L;
    private String msg;
    private int code = 500;

    public BException(String msg)
    {
        super(msg);
        this.msg = msg;
    }
}
