package ${package.Mapper};

import ${package.Entity}.${entity};
import ${superMapperClassPackage};
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.math.BigInteger;
<#if mapperAnnotation>
    import org.apache.ibatis.annotations.Mapper;
</#if>

/**
* <p>
    * ${table.comment!} Mapper 接口
    * </p>
*
* @author ${author}
* @since ${date}
*/
<#if mapperAnnotation>
    @Mapper
</#if>


@Mapper
public interface ${table.mapperName}  {
@Select("select * from ${table.name} where is_deleted=0 and id ={id}" )
${entity} getById(@Param("id") BigInteger id);
@Select("select * from ${table.name} where id ={id} ")
${entity} extractById(@Param("id") BigInteger id );
BigInteger insert(@Param("entity") ${entity} entity);
int delete(@Param("id") BigInteger id);
int update(@Param("entity") ${entity} entity);
}

