package ${package}.dao;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.lang.Integer;

import ${package}.model.domain.${className};

/**
 * ${table.classRemark}
 * Created on ${date}
 * @author ${uname}
 */
@Mapper
public interface ${className}Mapper {

    /**
     * findList
     * @param condition ${className}
     * @return List<${className}> ${className}s
     */
    List<${className}> findList(${className} condition);

    /**
     * selectOneByCondition
     * @param condition ${className}
     * @return ${className}
     */
    ${className} selectOneByCondition(${className} condition);

    /**
     * getById
     * @param id
     * @return ${className}
     */
    ${className} getById(Integer id);

    /**
     * insert
     * @param condition ${className}
     * @return ${className}
     */
    Integer insert(${className} condition);

    /**
     * update
     * @param condition ${className}
     * @return ${className}
     */
    Integer update(${className} condition);
}
