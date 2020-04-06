package com.nsa.chatapp.utils;

public class PairPackage <K,V> {
	
	private K value1;
	private V value2;
	public K getValue1() {
		return value1;
	}
	public void setValue1(K value1) {
		this.value1 = value1;
	}
	public V getValue2() {
		return value2;
	}
	public void setValue2(V value2) {
		this.value2 = value2;
	}
	public PairPackage(K value1, V value2) {
		super();
		this.value1 = value1;
		this.value2 = value2;
	}
	
	public PairPackage() {};
	
	
}
