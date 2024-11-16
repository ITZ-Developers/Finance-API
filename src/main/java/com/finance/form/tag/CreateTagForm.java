package com.finance.form.tag;

import com.finance.validation.ColorCodeConstraint;
import com.finance.validation.TagKind;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateTagForm {
    @NotBlank(message = "name cannot be null")
    @ApiModelProperty(name = "name", required = true)
    private String name;
    @NotNull(message = "kind cannot be null")
    @TagKind
    @ApiModelProperty(name = "kind", required = true)
    private Integer kind;
    @NotBlank(message = "colorCode cannot be null")
    @ColorCodeConstraint
    @ApiModelProperty(name = "colorCode", required = true)
    private String colorCode;
}
