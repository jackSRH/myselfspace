package com.mailian.firecontrol.dto.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(description = "监控模块返回参数")
public class DiagramStructResp {
    @ApiModelProperty(value = "模块id")
    private Integer id;
    @ApiModelProperty(value = "数据名称")
    private String structName;
    @ApiModelProperty(value = "图片路径")
    private String structPic;
    @ApiModelProperty(value = "模块数据项")
    private List<DiagramItemResp> diagramItems;

    public List<DiagramItemResp> getDiagramItems() {
        return diagramItems;
    }

    public void setDiagramItems(List<DiagramItemResp> diagramItems) {
        this.diagramItems = diagramItems;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStructName() {
        return structName;
    }

    public void setStructName(String structName) {
        this.structName = structName;
    }

    public String getStructPic() {
        return structPic;
    }

    public void setStructPic(String structPic) {
        this.structPic = structPic;
    }
}
