package com.example.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
@TableName("parking_place")
public class ParkingPlace {
    @TableId(type = IdType.AUTO)
    private BigInteger id;//如果表中设置了主键的话，其实没必要加tableId的注解，也可以进行自增插入，而且也不需要表的主键名必须是id,除非你要用雪花算法
    //但是雪花算法必须使用long类型... BigInteger不行
    //反正很恶心...
    //这个必须加，要主键回填
    private String location;
    private Integer createTime;
    private Integer updateTime;
    //@TableLogic 不能要这个字段，不然管理员查不出来被删除的数据
    private Integer isDeleted;

//    @Version
//    private  BigInteger  version;
}
