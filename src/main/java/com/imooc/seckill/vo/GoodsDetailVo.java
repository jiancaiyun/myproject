package com.imooc.seckill.vo;

import com.imooc.seckill.domain.MiaoshaUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
@EqualsAndHashCode()
@SuppressWarnings("serial")
@NoArgsConstructor
@Data
@Accessors(chain=true)
public class GoodsDetailVo implements Serializable {

    private int status=0;
    private int remailSecond = 0;
    private GoodsVo goodsVo;
    private MiaoshaUser miaoshaUser;
}
