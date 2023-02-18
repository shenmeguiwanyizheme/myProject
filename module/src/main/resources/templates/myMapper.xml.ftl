<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package.Mapper}.${table.mapperName}">

    <#if enableCache>
        <!-- 开启二级缓存 -->
        <cache type="${cacheClassName}"/>
    </#if>
    <insert id="insert" useGeneratedKeys="true" keyProperty="id" parameterType="${package.Entity}.${entity}">
        insert into ${table.name}(
        <#list table.fields as field>
            <#if field.propertyName=="id">
            <#elseif field.propertyName=="isDeleted">
                is_deleted
            <#else >
                <if test="entity.${field.propertyName}!=null and entity.${field.propertyName}!=''">${field.propertyName}<#if field_has_next>,</#if></if>
            </#if>
        </#list>
        )
        values(
        <#list table.fields as field>
            <#if field.propertyName=="id" >
            <#elseif field.propertyName=="isDeleted">
                leftSymbol entity.${field.propertyName} rightSymbol
            <#else>
                <if test="entity.${field.propertyName}!=null and entity.${field.propertyName}!=''">
                    leftSymbol entity.${field.propertyName}rightSymbol<#if field_has_next>,</#if>
                </if>
            </#if>
        </#list>
        )
    </insert>

    <update id="update">
        update ${table.name} set id=leftSymbol id rightSymbol
        <#list table.fields as field>
            <#if field.propertyName=="id">
            <#else>
                <if test="entity.${field.propertyName}!=null and entity.${field.propertyName}!=''">
                    leftSymbol${field.propertyName}=leftSymbol entity.${field.propertyName}rightSymbol
                </if>
            </#if>
        </#list>
        where id=leftSymbol entity.id rightSymbol
    </update>
    <delete id="delete">
        update ${table.name} set is_deleted =1 where entity.id =leftSymbol id rightSymbol
    </delete>


</mapper>
