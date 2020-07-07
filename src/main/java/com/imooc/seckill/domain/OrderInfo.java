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
public class OrderInfo implements Serializable {
	private Long id;
	private Long userId;
	private Long goodsId;
	private Long deliveryAddrId;
	private String goodsName;
	private Integer goodsCount;
	private Double goodsPrice;
	private Integer orderChannel;
	private Integer orderStatus;
	private Date createDate;
	private Date payDate;
	
//	public Integer getOrderStatus() {
//		return orderStatus;
//	}
//	public void setOrderStatus(Integer orderStatus) {
//		this.orderStatus = orderStatus;
//	}
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
//	public Long getGoodsId() {
//		return goodsId;
//	}
//	public void setGoodsId(Long goodsId) {
//		this.goodsId = goodsId;
//	}
//	public Long getDeliveryAddrId() {
//		return deliveryAddrId;
//	}
//	public void setDeliveryAddrId(Long deliveryAddrId) {
//		this.deliveryAddrId = deliveryAddrId;
//	}
//	public String getGoodsName() {
//		return goodsName;
//	}
//	public void setGoodsName(String goodsName) {
//		this.goodsName = goodsName;
//	}
//	public Integer getGoodsCount() {
//		return goodsCount;
//	}
//	public void setGoodsCount(Integer goodsCount) {
//		this.goodsCount = goodsCount;
//	}
//	public Double getGoodsPrice() {
//		return goodsPrice;
//	}
//	public void setGoodsPrice(Double goodsPrice) {
//		this.goodsPrice = goodsPrice;
//	}
//	public Integer getOrderChannel() {
//		return orderChannel;
//	}
//	public void setOrderChannel(Integer orderChannel) {
//		this.orderChannel = orderChannel;
//	}
//	public Date getCreateDate() {
//		return createDate;
//	}
//	public void setCreateDate(Date createDate) {
//		this.createDate = createDate;
//	}
//	public Date getPayDate() {
//		return payDate;
//	}
//	public void setPayDate(Date payDate) {
//		this.payDate = payDate;
//	}
}
