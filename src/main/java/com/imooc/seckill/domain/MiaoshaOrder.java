package com.imooc.seckill.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@SuppressWarnings("serial")
@NoArgsConstructor
@Data
@Accessors(chain=true)
public class MiaoshaOrder implements Serializable {
	private Long id;
	private Long userId;
	private Long orderId;
	private Long goodsId;
//	public Long getId() {
//		return id;
//	}
//	public void setId(Long id) {
//		this.id = id;
//	}
//	public Long getUserId() {
//		return userId;
//	}
//	public void setUserId(Long userId) {
//		this.userId = userId;
//	}
//	public Long getOrderId() {
//		return orderId;
//	}
//	public void setOrderId(Long orderId) {
//		this.orderId = orderId;
//	}
//	public Long getGoodsId() {
//		return goodsId;
//	}
//	public void setGoodsId(Long goodsId) {
//		this.goodsId = goodsId;
//	}
	
	
}
