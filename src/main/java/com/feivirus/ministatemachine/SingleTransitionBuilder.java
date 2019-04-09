package com.feivirus.ministatemachine;

/**
 * 
 * @author feivirus
 * 单一转换
 * 现态只有一个次态
 * 现态有多个次态的暂未实现
 */
public interface SingleTransitionBuilder<T extends StateMachine<T, S, E, C>, S, E, C> {
	//源状态
	SingleTransitionBuilder<T, S, E, C> from(S stateId);
	
	//目标状态
	SingleTransitionBuilder<T, S, E, C> to(S stateId);
	
	//事件
	SingleTransitionBuilder<T, S, E, C> on(E event);
	
	//动作
	SingleTransitionBuilder<T, S, E, C> callMethod(String methodName);
}
