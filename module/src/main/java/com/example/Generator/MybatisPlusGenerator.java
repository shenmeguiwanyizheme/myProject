package com.example.Generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;
import java.util.function.Consumer;

public class MybatisPlusGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/myDb?serverTimezone=Asia/Shanghai", "root", "123456")
                .globalConfig(builder -> {
                    builder.author("maoring") // 设置作者
                            //.enableSwagger() // 开启 swagger 模式
                            .fileOverride()
                            // 覆盖已生成文件
                            .outputDir("D:\\Users\\侠岚\\IdeaProjects\\demo\\module\\src\\main\\java"); // 指定输出目录
                })
                .templateConfig(new Consumer<TemplateConfig.Builder>() {
                    @Override
                    public void accept(TemplateConfig.Builder builder) {
                        builder
                                .disable(TemplateType.ENTITY)
                                .entity("/templates/entity.java")
                                .service("/templates/service.java")
                                .serviceImpl("/templates/serviceImpl.java")
                                .mapper("/templates/mapper.java")
                                .controller("/templates/controller.java")
                                .xml("templates/myMapper.xml")

                                .build();
                    }
//                }).injectionConfig(new Consumer<InjectionConfig.Builder>() {
//                    @Override
//                    public void accept(InjectionConfig.Builder builder) {
//                        HashMap<String,String> hashMap=new HashMap<>();
//                        hashMap.put("left","#{");
//                        hashMap.put("right","}");
//                        builder.customMap(hashMap);
//                    }
//                })

                    // 什么是父包名 什么是父模块名 怎么生成mapper
                }).packageConfig(builder -> {
                    builder.parent("com.example") // 设置父包名
                            .moduleName("") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "D:\\Users\\侠岚\\IdeaProjects\\demo\\module\\src\\main\\resources\\com\\example\\mapper"))
                            .controller("tmp.controller").service("tmp.service").serviceImpl("tmp.impl").entity("tmp.pojo").mapper("tmp.mapper")
                    ; // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("parking_place") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_")
                    ; // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

}
