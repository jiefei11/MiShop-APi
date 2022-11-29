

package com.bhgeek.mishopapi.vo;

import java.io.Serializable;
import java.util.List;

public class CityVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer value;
    private String label;
    private Integer pid;
    private List<CityVo> children;

    public CityVo() {
    }

    public Integer getValue() {
        return this.value;
    }

    public String getLabel() {
        return this.label;
    }

    public Integer getPid() {
        return this.pid;
    }

    public List<CityVo> getChildren() {
        return this.children;
    }

    public void setValue(final Integer value) {
        this.value = value;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public void setPid(final Integer pid) {
        this.pid = pid;
    }

    public void setChildren(final List<CityVo> children) {
        this.children = children;
    }

    public String toString() {
        return "CityVo(value=" + this.getValue() + ", label=" + this.getLabel() + ", pid=" + this.getPid() + ", children=" + this.getChildren() + ")";
    }
}
