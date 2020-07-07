package com.imooc.seckill.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
@SuppressWarnings("serial")
@NoArgsConstructor
@Data
@Accessors(chain=true)

public class MiaoshaGoods implements Serializable {
	private Long id;
	private Long goodsId;
	private Double miaoshaPrice;
	private Integer stockCount;
	private Date startDate;
	private Date endDate;
	
//	public Double getMiaoshaPrice() {
//		return miaoshaPrice;
//	}
//	public void setMiaoshaPrice(Double miaoshaPrice) {
//		this.miaoshaPrice = miaoshaPrice;
//	}
//	public Long getId() {
//		return id;
//	}
//	public void setId(Long id) {
//		this.id = id;
//	}
//	public Long getGoodsId() {
//		return goodsId;
//	}
//	public void setGoodsId(Long goodsId) {
//		this.goodsId = goodsId;
//	}
//	public Integer getStockCount() {
//		return stockCount;
//	}
//	public void setStockCount(Integer stockCount) {
//		this.stockCount = stockCount;
//	}
//	public Date getStartDate() {
//		return startDate;
//	}
//	public void setStartDate(Date startDate) {
//		this.startDate = startDate;
//	}
//	public Date getEndDate() {
//		return endDate;
//	}
//	public void setEndDate(Date endDate) {
//		this.endDate = endDate;
//	}
	

}
