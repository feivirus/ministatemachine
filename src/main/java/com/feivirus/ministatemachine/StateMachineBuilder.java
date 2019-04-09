package com.feivirus.ministatemachine;

/**
 * 
 * @author feivirus
 *
 */
public interface StateMachineBuilder<T extends StateMachine<T, S, E, C>, S, E, C> {
	//返回单一状态机的Builder
	SingleTransitionBuilder<T, S, E, C> singleTransition();
	
	//返回状态机实例
	T newStateMachine(S stateId, Object... constructorParam);
	
	T newStateMachine(S stateId);
}
