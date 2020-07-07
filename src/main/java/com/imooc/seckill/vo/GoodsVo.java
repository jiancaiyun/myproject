package com.imooc.seckill.vo;

import com.imooc.seckill.domain.Goods;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
@NoArgsConstructor
@Data
@Accessors(chain=true)
public class GoodsVo extends Goods implements Serializable {
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
    private Double miaoshaPrice;

//    public Integer getStockCount() {
//        return stockCount;
//    }
//
//    public void setStockCount(Integer stockCount) {
//        this.stockCount = stockCount;
//    }
//
//    public Date getStartDate() {
//        return startDate;
//    }
//
//    public void setStartDate(Date startDate) {
//        this.startDate = startDate;
//    }
//
//    public Date getEndDate() {
//        return endDate;
//    }
//
//    public void setEndDate(Date endDate) {
//        this.endDate = endDate;
//    }
//
//    public Double getMiaoshaPrice() {
//        return miaoshaPrice;
//    }
//
//    public void setMiaoshaPrice(Double miaoshaPrice) {
//        this.miaoshaPrice = miaoshaPrice;
//    }
}
