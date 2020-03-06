package ${package}.model.domain;

import lombok.Data;
import java.io.Serializable;
<#list table.columnTypeList as tt>
import ${tt};
</#list>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * ${table.classRemark}
 * Created on ${date}
 * @author ${uname}
 */
@Data
@ApiModel(description="${table.classRemark}")
public class ${className} implements Serializable {

	private static final long serialVersionUID = 1L;

<#list table.columns as column>
	@ApiModelProperty(value ="${column.label}")
	private ${column.type} ${column.name};
</#list>
}
