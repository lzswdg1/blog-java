package com.zw.zw_blog.model.vo.tag;


import lombok.Data;
import java.io.Serializable;

@Data
public class TagVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
}
