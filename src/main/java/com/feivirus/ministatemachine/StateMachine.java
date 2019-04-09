package com.feivirus.ministatemachine;

/**
 * 
 * @author feivirus
 * 状态机
 * @param <T>
 * 事件
 * @param <E>
 * 状态
 * @param <S>
 * 上下文
 * @param <C>
 */
public interface StateMachine<T extends StateMachine<T, S, E, C>, S, E, C> {
	//触发事件
	void fire(E event);
	
	void fire(E event, C context);
	
	void start();
	
	void start(C context);
	
	boolean isStarted();
	
	T getThis();
}
