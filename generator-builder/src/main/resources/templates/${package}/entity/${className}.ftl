package ${package}.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

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
